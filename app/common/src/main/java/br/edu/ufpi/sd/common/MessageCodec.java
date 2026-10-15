package br.edu.ufpi.sd.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.nio.charset.StandardCharsets;

public final class MessageCodec {
    private static final ObjectMapper MAPPER = JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .build();

    private MessageCodec() {
    }

    public static byte[] encode(Message message) throws Exception {
        return MAPPER.writeValueAsString(message).getBytes(StandardCharsets.UTF_8);
    }

    public static Message decode(byte[] payload) throws Exception {
        return MAPPER.readValue(payload, Message.class);
    }
}
