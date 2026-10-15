package br.edu.ufpi.sd.sentiment;

import br.edu.ufpi.sd.common.Message;
import br.edu.ufpi.sd.common.MessageCodec;
import br.edu.ufpi.sd.common.MessagingConfig;
import br.edu.ufpi.sd.common.QueueTopology;
import br.edu.ufpi.sd.common.RabbitConnection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;

import java.nio.charset.StandardCharsets;

public final class SentimentConsumer {
    private SentimentConsumer() {
    }

    public static void main(String[] args) throws Exception {
        try (Connection connection = RabbitConnection.open();
             Channel channel = connection.createChannel()) {
            QueueTopology.declare(channel);
            channel.basicQos(1);
            SentimentClassifier classifier = new SentimentClassifier();
            DeliverCallback callback = (consumerTag, delivery) -> {
                try {
                    Message message = MessageCodec.decode(delivery.getBody());
                    if (message.content().contains("[ERRO]")) {
                        throw new IllegalArgumentException("mensagem marcada para teste de DLQ");
                    }
                    String result = classifier.classify(message.content());
                    System.out.printf("[sentiment] id=%s sentimento=%s texto=\"%s\"%n",
                            message.id(), result, message.content());
                    Thread.sleep(1200);
                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                } catch (Exception exception) {
                    System.err.printf("[sentiment] erro: %s; encaminhando para DLQ%n",
                            exception.getMessage());
                    channel.basicNack(delivery.getEnvelope().getDeliveryTag(), false, false);
                }
            };
            channel.basicConsume(MessagingConfig.REVIEW_QUEUE, false, callback, consumerTag -> {
            });
            System.out.println("[sentiment] Consumidor ativo.");
            Thread.currentThread().join();
        }
    }
}
