package br.edu.ufpi.sd.common;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.util.Map;

public final class QueueTopology {
    private QueueTopology() {
    }

    public static void declare(Channel channel) throws Exception {
        channel.exchangeDeclare(MessagingConfig.EXCHANGE, BuiltinExchangeType.TOPIC, true);
        declareQueue(channel, MessagingConfig.REVIEW_QUEUE, MessagingConfig.REVIEW_DLQ,
                MessagingConfig.REVIEW_ROUTING_KEY);
        declareQueue(channel, MessagingConfig.SUPPORT_QUEUE, MessagingConfig.SUPPORT_DLQ,
                MessagingConfig.SUPPORT_ROUTING_KEY);
    }

    private static void declareQueue(Channel channel, String queue, String dlq, String routingKey)
            throws Exception {
        channel.queueDeclare(dlq, true, false, false, Map.of());
        channel.queueBind(dlq, MessagingConfig.EXCHANGE, dlq);
        channel.queueDeclare(queue, true, false, false,
                Map.of("x-dead-letter-exchange", MessagingConfig.EXCHANGE,
                        "x-dead-letter-routing-key", dlq));
        channel.queueBind(queue, MessagingConfig.EXCHANGE, routingKey);
    }
}
