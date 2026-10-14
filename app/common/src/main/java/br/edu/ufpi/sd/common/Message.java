package br.edu.ufpi.sd.common;

import java.time.Instant;
import java.util.UUID;

public record Message(UUID id, Instant generatedAt, MessageType type, String content) {
}
