package com.application.taskmanager.exception.handler;

import com.application.taskmanager.dto.response.ErrorResponse;
import com.application.taskmanager.exception.BadRequestException;
import com.application.taskmanager.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * GlobalExceptionHandler
 *
 * <p><b>Functional Purpose:</b>
 * Provides centralized error handling for the Task Management API.
 * Ensures that all exceptions are converted into a consistent
 * response format as required by the problem statement:
 *
 * <pre>
 *     { "error": "<message>" }
 * </pre>
 *
 * <p>
 * Handles:
 * <ul>
 *     <li>Business exceptions (404, 400)</li>
 *     <li>Validation failures</li>
 *     <li>Enum parsing errors</li>
 *     <li>Unexpected system exceptions</li>
 * </ul>
 *
 * <p><b>Technical Design Notes:</b>
 * <ul>
 *     <li>Uses {@code @RestControllerAdvice} for global exception interception.</li>
 *     <li>Each {@code @ExceptionHandler} maps a specific exception to HTTP status.</li>
 *     <li>Maintains strict response format compliance for CLI evaluation.</li>
 *     <li>Prevents stack traces from leaking to clients.</li>
 * </ul>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Helper method to construct standardized ErrorResponse objects.
     *
     * <p>
     * Centralizing response creation ensures:
     * <ul>
     *     <li>Consistency in output format</li>
     *     <li>Ease of future modification</li>
     * </ul>
     *
     * @param message human-readable error description
     * @return ErrorResponse object
     */
    private ErrorResponse buildErrorResponse(String message) {
        return ErrorResponse.builder()
                .error(message)
                .build();
    }

    /**
     * Handles cases where a requested resource is not found.
     *
     * <p><b>Functional Behavior:</b>
     * Triggered when a Task or TaskList does not exist.
     *
     * <p><b>HTTP Response:</b> 404 NOT FOUND
     *
     * @param ex ResourceNotFoundException
     * @return standardized error response
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(buildErrorResponse(ex.getMessage()));
    }

    /**
     * Handles client-side bad requests.
     *
     * <p><b>Functional Behavior:</b>
     * Triggered when business validation fails
     * (e.g., task limit exceeded, invalid move request).
     *
     * <p><b>HTTP Response:</b> 400 BAD REQUEST
     *
     * @param ex BadRequestException
     * @return standardized error response
     */
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(BadRequestException ex) {
        return ResponseEntity.badRequest()
                .body(buildErrorResponse(ex.getMessage()));
    }

    /**
     * Handles validation failures from @Valid annotated DTOs.
     *
     * <p><b>Functional Behavior:</b>
     * Triggered when request body fails bean validation
     * (e.g., missing required fields, invalid size).
     *
     * <p>
     * Extracts the first field error and formats it as:
     * <pre>
     *     fieldName: errorMessage
     * </pre>
     *
     * <p><b>HTTP Response:</b> 400 BAD REQUEST
     *
     * @param ex MethodArgumentNotValidException
     * @return standardized error response
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {

        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .findFirst()
                .orElse("Validation failed");

        return ResponseEntity.badRequest()
                .body(buildErrorResponse(message));
    }

    /**
     * Handles invalid enum values or incorrect request parameters.
     *
     * <p><b>Functional Behavior:</b>
     * Triggered when:
     * <ul>
     *     <li>Invalid priority value</li>
     *     <li>Invalid effort value</li>
     *     <li>Invalid state value</li>
     * </ul>
     *
     * <p>
     * This typically occurs during enum parsing from CSV filters.
     *
     * <p><b>HTTP Response:</b> 400 BAD REQUEST
     *
     * @param ex IllegalArgumentException
     * @return standardized error response
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleEnumError(IllegalArgumentException ex) {
        return ResponseEntity.badRequest()
                .body(buildErrorResponse("Invalid request parameter"));
    }

    /**
     * Fallback handler for all unexpected exceptions.
     *
     * <p><b>Functional Behavior:</b>
     * Catches any unhandled runtime exception.
     *
     * <p>
     * Prevents exposure of internal stack traces and
     * ensures API stability.
     *
     * <p><b>HTTP Response:</b> 500 INTERNAL SERVER ERROR
     *
     * @param ex unexpected exception
     * @return generic standardized error response
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(buildErrorResponse("Unexpected error occurred"));
    }
}