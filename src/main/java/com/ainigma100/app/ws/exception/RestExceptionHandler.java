package com.ainigma100.app.ws.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.dao.DataAccessException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String REQUESTED_URL = "Requested URL=";
    private static final String FROM_IP = " from ip: ";

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request, HttpServletRequest servletrequest) {
        log.error(REQUESTED_URL + servletrequest.getContextPath()	+ FROM_IP + servletrequest.getRemoteAddr());
        log.error("Exception Raised=" + ex);

        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());

        ErrorMessage error = new ErrorMessage("Server Error", details);

        return handleExceptionInternal(ex, error, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }


    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<Object> handleNullPointerException(NullPointerException ex, WebRequest request, HttpServletRequest servletrequest) {
        log.error(ex.getLocalizedMessage());
        log.error(REQUESTED_URL + servletrequest.getContextPath() + FROM_IP + servletrequest.getRemoteAddr());

        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());

        ErrorMessage error = new ErrorMessage("Null Pointer Exception", details);

        return handleExceptionInternal(ex, error, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }


    @ExceptionHandler(RecordNotFoundException.class)
    public ResponseEntity<Object> handleRecordNotFoundException(RecordNotFoundException ex, WebRequest request, HttpServletRequest servletrequest) {
        log.error(ex.getLocalizedMessage());
        log.error(REQUESTED_URL + servletrequest.getContextPath() + FROM_IP + servletrequest.getRemoteAddr());

        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());

        ErrorMessage error = new ErrorMessage("Record Not Found Exception", details);

        return handleExceptionInternal(ex, error, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(RecordAlreadyExistsException.class)
    public ResponseEntity<Object> handleRecordAlreadyExistsException(RecordAlreadyExistsException ex, WebRequest request, HttpServletRequest servletrequest) {
        log.error(ex.getLocalizedMessage());
        log.error(REQUESTED_URL + servletrequest.getContextPath() + FROM_IP + servletrequest.getRemoteAddr());

        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());

        ErrorMessage error = new ErrorMessage("Record Already Exists Exception", details);

        return handleExceptionInternal(ex, error, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }


    @ExceptionHandler(ConstraintViolationException.class)
    public final ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex, WebRequest request, HttpServletRequest servletrequest) {
        log.error(ex.getLocalizedMessage());
        log.error(REQUESTED_URL + servletrequest.getContextPath() + FROM_IP + servletrequest.getRemoteAddr());

        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());

        ErrorMessage error = new ErrorMessage("Constraint Violation", details);

        return handleExceptionInternal(ex, error, new HttpHeaders(), HttpStatus.NOT_ACCEPTABLE, request);
    }



    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<Object> handleException(DataAccessException ex, WebRequest request, HttpServletRequest servletrequest) {
        log.error(ex.getLocalizedMessage());
        log.error(REQUESTED_URL + servletrequest.getContextPath() + FROM_IP + servletrequest.getRemoteAddr());

        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());

        ErrorMessage error = new ErrorMessage("DataAccessException occurred in Repository/DAO interface", details);

        return handleExceptionInternal(ex, error, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {

            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);

        });

        log.error(errors.toString());

        return handleExceptionInternal(ex, errors, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }


    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {

        HttpServletRequest servletrequest = ((ServletWebRequest)request).getRequest();

        log.error(ex.getLocalizedMessage());
        log.error(REQUESTED_URL + servletrequest.getRequestURI() + FROM_IP + servletrequest.getRemoteAddr());
        String parameterName = ex.getParameterName();

        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());

        ErrorMessage error = new ErrorMessage("Missing Request Param '" + parameterName + "' Exception", details);

        return handleExceptionInternal(ex, error, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }


    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(
            MissingPathVariableException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {

        HttpServletRequest servletrequest = ((ServletWebRequest)request).getRequest();

        log.error(ex.getLocalizedMessage());
        log.error(REQUESTED_URL + servletrequest.getRequestURI() + FROM_IP + servletrequest.getRemoteAddr());
        String variableName = ex.getVariableName();

        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());

        ErrorMessage error = new ErrorMessage("Missing Path Variable '" + variableName + "' Exception", details);

        return handleExceptionInternal(ex, error, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);

    }

}
