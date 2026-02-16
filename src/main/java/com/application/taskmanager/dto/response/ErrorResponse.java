package com.application.taskmanager.dto.response;

import lombok.Builder;
import lombok.Getter;

/**
 * { "error": "<message>" }
 */
@Getter
@Builder
public class ErrorResponse {

    private final String error;
}