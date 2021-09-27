package com.github.leozin.dto;

import java.util.Date;

public record ErrorResponse(String message, int httpCode, Date time) {
}
