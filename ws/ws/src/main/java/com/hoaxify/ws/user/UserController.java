package com.hoaxify.ws.user;



import java.util.stream.Collectors;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hoaxify.ws.error.ApiError;
import com.hoaxify.ws.shared.GenericMessage;
import com.hoaxify.ws.shared.Messages;
import com.hoaxify.ws.user.dto.UserCreate;
import com.hoaxify.ws.user.dto.UserDTO;
import com.hoaxify.ws.user.exception.ActivationNotificationException;
import com.hoaxify.ws.user.exception.InvalidTokenException;
import com.hoaxify.ws.user.exception.NotUniqueEmailException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
public class UserController {

    @Autowired
    UserService userService;


    @PostMapping("/api/v1/users")
    GenericMessage createUser(@Valid @RequestBody UserCreate user){
        userService.save(user.toUser());
        String message = Messages.getMessageForLocale("hoaxify.create.user.success.message", LocaleContextHolder.getLocale());
        return new GenericMessage(message);
    }


    @PatchMapping("api/v1/users/{token}/activate")
    GenericMessage activateUser(@PathVariable String token){
        userService.activateUser(token);
        String message = Messages.getMessageForLocale("hoaxify.activate.user.success.message", LocaleContextHolder.getLocale());
        return new GenericMessage(message);
    }


    @GetMapping("api/v1/users/{id}")
    UserDTO getUserById(@PathVariable long id){
        return new UserDTO(userService.getUser(id));
    }



    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ApiError> handleMethodArgNotValidEx(MethodArgumentNotValidException exception){
        ApiError apiError = new ApiError();
        apiError.setPath("/api/v1/users");
        apiError.setStatus(400);
        String message = Messages.getMessageForLocale("hoaxify.error.validation", LocaleContextHolder.getLocale());
        apiError.setMessage(message);
        var validationErrors = exception.getBindingResult().getFieldErrors().stream().collect(Collectors
        .toMap(FieldError::getField,
               FieldError::getDefaultMessage,
               (existing, replacing) -> existing));

        apiError.setValidationErrors(validationErrors);
        return ResponseEntity.status(400).body(apiError);
    }


    @ExceptionHandler(NotUniqueEmailException.class)
    ResponseEntity<ApiError> handleNotUniqueEmailEx(NotUniqueEmailException exception){
        ApiError apiError = new ApiError();
        apiError.setPath("/api/v1/users");
        apiError.setMessage(exception.getMessage());
        apiError.setStatus(400);
        apiError.setValidationErrors(exception.getValidationErrors());
        return ResponseEntity.status(400).body(apiError);
    }


    @ExceptionHandler(ActivationNotificationException.class)
    ResponseEntity<ApiError> handleActivationNotificationException(ActivationNotificationException exception){
        ApiError apiError = new ApiError();
        apiError.setPath("/api/v1/users");
        apiError.setMessage(exception.getMessage());
        apiError.setStatus(502);
        return ResponseEntity.status(502).body(apiError);
    }


    @ExceptionHandler(InvalidTokenException.class)
    ResponseEntity<ApiError> handleInvalidTokenException(InvalidTokenException exception, HttpServletRequest request){
        ApiError apiError = new ApiError();
        apiError.setPath(request.getRequestURI());
        apiError.setMessage(exception.getMessage());
        apiError.setStatus(400);
        return ResponseEntity.status(400).body(apiError);
    }

    @GetMapping("api/v1/users")
    Page<UserDTO> getUsers(Pageable page){
        return userService.getUsers(page).map(UserDTO::new);
    }

    @ExceptionHandler(NotFoundException.class)
    ResponseEntity<ApiError> handleEntityNotFoundException(NotFoundException exception, HttpServletRequest request){
        ApiError apiError = new ApiError();
        apiError.setMessage(exception.getMessage());
        apiError.setPath(request.getRequestURI());
        apiError.setStatus(404);
        return ResponseEntity.status(404).body(apiError);
    }

}
