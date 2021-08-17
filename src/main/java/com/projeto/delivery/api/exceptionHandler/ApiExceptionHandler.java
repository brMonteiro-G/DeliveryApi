package com.projeto.delivery.api.exceptionHandler;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.projeto.delivery.domain.exception.DomainException;
import com.projeto.delivery.domain.exception.EntidadeNaoEncontradaException;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler{

	@Autowired
	private MessageSource messageSource;
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		List<Error.Campo> campos = new ArrayList<>();
		
		
		for(Object error : ex.getBindingResult().getAllErrors()) { //tentar fazer com streams depois 
			
			String nome = ((FieldError) error).getField();
			String titulo =  messageSource.getMessage((MessageSourceResolvable) error, LocaleContextHolder.getLocale());
			
			campos.add(new Error.Campo(nome, titulo));
			
		}
		
		
		Error error = new Error();
		error.setStatus(status.value());
		error.setDataHora(OffsetDateTime.now());
		error.setMessage("Um ou mais campos inválidos, por favor preencha o formulário corretamente.");
		error.setCampos(campos);
		
		return handleExceptionInternal( ex, error , headers, status, request);
	}
	
	@ExceptionHandler(DomainException.class)
	public ResponseEntity<Object> handleDomainException(DomainException ex, WebRequest req){
		
		HttpStatus status = HttpStatus.BAD_REQUEST;
		
		
		Error error = new Error();
		error.setStatus(status.value());
		error.setDataHora(OffsetDateTime.now());
		error.setMessage(ex.getMessage());
		
		
		return handleExceptionInternal(ex, error, new HttpHeaders(), status, req);
	}
	
	
	@ExceptionHandler(EntidadeNaoEncontradaException.class)
	public ResponseEntity<Object> handleEntidadeNaoEncontradaException(EntidadeNaoEncontradaException ex, WebRequest req){
		
		HttpStatus status = HttpStatus.NOT_FOUND;
		
		
		Error error = new Error();
		error.setStatus(status.value());
		error.setDataHora(OffsetDateTime.now());
		error.setMessage(ex.getMessage());
		
		
		return handleExceptionInternal(ex, error, new HttpHeaders(), status, req);
	}
	
	
}
