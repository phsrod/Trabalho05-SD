package br.edu.ufpi.sd.producer;

import br.edu.ufpi.sd.common.Message;
import br.edu.ufpi.sd.common.MessageCodec;
import br.edu.ufpi.sd.common.MessageType;
import br.edu.ufpi.sd.common.MessagingConfig;
import br.edu.ufpi.sd.common.QueueTopology;
import br.edu.ufpi.sd.common.RabbitConnection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public final class MessageProducer {
    private static final Random RANDOM = new Random();
    private static final List<String> REVIEWS = List.of(
            "O produto chegou rápido e a qualidade superou minhas expectativas.",
            "A bateria dura muito e o acabamento é excelente.",
            "O produto funciona, mas o resultado é apenas razoável.",
            "A embalagem veio danificada e o produto parou de funcionar.",
            "Estou decepcionado, a compra apresentou defeito no primeiro dia.",
            "Não é ruim, porém também não apresenta nada de especial.");
    private static final List<String> SUPPORT_REQUESTS = List.of(
            "Não consigo acessar minha conta depois de trocar a senha.",
            "O aplicativo está muito lento para abrir os relatórios.",
            "A conexão cai sempre que tento enviar um arquivo.",
            "Minha senha foi recusada e o login está bloqueado.",
            "O sistema demora vários minutos para carregar.",
            "O serviço perdeu conexão com o servidor.");

    private MessageProducer() {
    }

    public static void main(String[] args) throws Exception {
        try (Connection connection = RabbitConnection.open();
             Channel channel = connection.createChannel()) {
            QueueTopology.declare(channel);
            System.out.println("[producer] Gerando 6 mensagens por segundo. Ctrl+C para encerrar.");
            while (true) {
                publish(channel, MessageType.PRODUCT_REVIEW, REVIEWS);
                publish(channel, MessageType.SUPPORT_REQUEST, SUPPORT_REQUESTS);
                Thread.sleep(333);
            }
        }
    }

    private static void publish(Channel channel, MessageType type, List<String> samples) throws Exception {
        String routingKey = type == MessageType.PRODUCT_REVIEW
                ? MessagingConfig.REVIEW_ROUTING_KEY : MessagingConfig.SUPPORT_ROUTING_KEY;
        Message message = new Message(UUID.randomUUID(), Instant.now(), type,
                samples.get(RANDOM.nextInt(samples.size())));
        channel.basicPublish(MessagingConfig.EXCHANGE, routingKey, null, MessageCodec.encode(message));
        System.out.printf("[producer] %s | %s | %s%n", message.id(),
                type == MessageType.PRODUCT_REVIEW ? "review" : "support",
                new String(MessageCodec.encode(message), StandardCharsets.UTF_8));
    }
}
