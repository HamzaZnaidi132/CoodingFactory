package com.codingfactory.shared;

import java.time.Instant;

public record ApiError(String message, Instant timestamp) {
}
