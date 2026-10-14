package br.edu.ufpi.sd.common;

public final class MessagingConfig {
    public static final String EXCHANGE = "messages.topic";
    public static final String REVIEW_ROUTING_KEY = "review.product";
    public static final String SUPPORT_ROUTING_KEY = "support.request";
    public static final String REVIEW_QUEUE = "reviews.queue";
    public static final String SUPPORT_QUEUE = "support.queue";
    public static final String REVIEW_DLQ = "reviews.dlq";
    public static final String SUPPORT_DLQ = "support.dlq";

    private MessagingConfig() {
    }
}
