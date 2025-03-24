package com.bookstore.exception;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.LinkedHashMap;
import java.util.Map;

@Provider
public class ValidationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(ConstraintViolationException exception) {
        Map<String, String> errors = new LinkedHashMap<>();

        for (ConstraintViolation<?> violation : exception.getConstraintViolations()) {
            String field = extractFieldName(violation.getPropertyPath().toString()); // Extract clean field name
            String message = violation.getMessage();
            errors.put(field, message);
        }

        // Use LinkedHashMap to maintain order
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("statusCode", Response.Status.BAD_REQUEST.getStatusCode()); // 400
        response.put("description", errors); // Errors appear after statusCode

        return Response.status(Response.Status.BAD_REQUEST)
                .entity(response)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    // Extracts the clean field name by removing method argument prefixes
    private String extractFieldName(String propertyPath) {
        if (propertyPath.contains(".")) {
            return propertyPath.substring(propertyPath.lastIndexOf('.') + 1);
        }
        return propertyPath;
    }
}
