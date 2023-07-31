package telran.spring.exceptions;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
	
	@ExceptionHandler(NotFoundException.class)
	ResponseEntity<String> notFoundHandler(NotFoundException e){
		String errorMessage = e.getMessage();
		log.error(errorMessage);
		return new ResponseEntity<>(errorMessage,HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler({IllegalArgumentException.class, HttpMessageNotReadableException.class, IllegalStateException.class})
	ResponseEntity<String> illegalArgumentHandler(RuntimeException e) {
		String errorMessage = e.getMessage();
		log.error(errorMessage);
		return new ResponseEntity<>(errorMessage,HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	ResponseEntity<String> methodArgumentNotValidHandler(MethodArgumentNotValidException e) {
		String errorMessage = e.getAllErrors().stream()
				.map(err -> err.getCodes()[0] + ": " + err.getDefaultMessage()).collect(Collectors.joining(";"));
		log.error(errorMessage);
		return new ResponseEntity<>(errorMessage,HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(ConstraintViolationException.class)
	ResponseEntity<String> constraintViolationException(ConstraintViolationException e) {
		String errorMessage = e.getConstraintViolations().stream()
				.map(err -> err.getPropertyPath() + ": " + err.getMessage()).collect(Collectors.joining(";"));
		log.error(errorMessage);
		return new ResponseEntity<>(errorMessage,HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(RuntimeException.class)
	ResponseEntity<String> otherExceptionHandler(RuntimeException e){
		String errorMessage = e.getMessage();
		log.error(errorMessage);
		return new ResponseEntity<>(errorMessage,HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	
}
