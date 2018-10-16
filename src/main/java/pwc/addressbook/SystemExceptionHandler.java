package pwc.addressbook;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;


/**
 * Handles exceptions
 */
@ControllerAdvice
public class SystemExceptionHandler {
	
	@Autowired LocalValidatorFactoryBean localValidatorFactoryBean;
	
	
	/**
	 * Returns a better exception message, such as return 400 error status code for validation errors with the individual validation errors added to the message
	 * @param exception
	 * @param request
	 * @return
	 */
	@ExceptionHandler({ Exception.class })
	public ResponseEntity<Object> handleConstraintViolation(Exception exception, WebRequest request) {
		
		if (exception.getCause().getCause() instanceof ConstraintViolationException) {
			ConstraintViolationException ex = (ConstraintViolationException) exception.getCause().getCause();
			
			List<ValidationError> errors = new ArrayList<ValidationError>();
			for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
				errors.add(new ValidationError(violation));
			}

			ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors);
			return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
		}
		else
		{
			ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, exception.getLocalizedMessage(), new ArrayList<ValidationError>());
			return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
		}
	}
	
	/**
	 * Represents a validation error
	 */
	public class ValidationError {
		String path;
		String message;
		
		public String getPath() {
			return path;
		}
		public void setPath(String path) {
			this.path = path;
		}

		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}
		
		public ValidationError(ConstraintViolation<?> violation) {
			this(violation.getPropertyPath().toString(), violation.getMessage());
		}
		
		public ValidationError(String path, String message) {
			super();
			this.path = path;
			this.message = message;
		}
	}

	
	/**
	 * Represents an API error
	 */
	public class ApiError {

		private HttpStatus status;
		private String message;
		private List<ValidationError> errors;

		public ApiError(HttpStatus status, String message, List<ValidationError> errors) {
			super();
			this.status = status;
			this.message = message;
			this.errors = errors;
		}

		public ApiError(HttpStatus status, String message, ValidationError error) {
			super();
			this.status = status;
			this.message = message;
			errors = Arrays.asList(error);
		}

		public HttpStatus getStatus() {
			return status;
		}

		public void setStatus(HttpStatus status) {
			this.status = status;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public List<ValidationError> getErrors() {
			return errors;
		}

		public void setErrors(List<ValidationError> errors) {
			this.errors = errors;
		}

	}
}