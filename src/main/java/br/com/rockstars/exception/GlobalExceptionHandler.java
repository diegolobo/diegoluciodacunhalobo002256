package br.com.rockstars.exception;

import br.com.rockstars.domain.dto.ErrorResponseDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;
import java.util.List;
import java.util.stream.Collectors;

public class GlobalExceptionHandler {

    @Context
    UriInfo uriInfo;

    @ServerExceptionMapper
    public Response handleNotFoundException(NotFoundException ex) {
        ErrorResponseDTO error = ErrorResponseDTO.of(
            Response.Status.NOT_FOUND.getStatusCode(),
            "Not Found",
            ex.getMessage(),
            getPath()
        );
        return Response.status(Response.Status.NOT_FOUND).entity(error).build();
    }

    @ServerExceptionMapper
    public Response handleBusinessException(BusinessException ex) {
        ErrorResponseDTO error = ErrorResponseDTO.of(
            Response.Status.BAD_REQUEST.getStatusCode(),
            "Bad Request",
            ex.getMessage(),
            getPath()
        );
        return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
    }

    @ServerExceptionMapper
    public Response handleConstraintViolationException(ConstraintViolationException ex) {
        List<ErrorResponseDTO.FieldError> fieldErrors = ex.getConstraintViolations().stream()
            .map(this::toFieldError)
            .collect(Collectors.toList());

        ErrorResponseDTO error = ErrorResponseDTO.of(
            Response.Status.BAD_REQUEST.getStatusCode(),
            "Validation Error",
            "Erro de validacao nos campos",
            getPath()
        );
        error.setFieldErrors(fieldErrors);

        return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
    }

    @ServerExceptionMapper
    public Response handleGenericException(Exception ex) {
        ErrorResponseDTO error = ErrorResponseDTO.of(
            Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
            "Internal Server Error",
            "Erro interno do servidor",
            getPath()
        );
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(error).build();
    }

    private ErrorResponseDTO.FieldError toFieldError(ConstraintViolation<?> violation) {
        String field = violation.getPropertyPath().toString();
        if (field.contains(".")) {
            field = field.substring(field.lastIndexOf('.') + 1);
        }
        return new ErrorResponseDTO.FieldError(field, violation.getMessage());
    }

    private String getPath() {
        return uriInfo != null ? uriInfo.getPath() : "";
    }
}
