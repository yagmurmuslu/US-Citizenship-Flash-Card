package USCitizenshipFlashcard.controller;

import USCitizenshipFlashcard.dto.BaseResponse;
import USCitizenshipFlashcard.dto.UserResponse;
import USCitizenshipFlashcard.exeptions.ResourceNotFoundException;
import USCitizenshipFlashcard.jpa.UserRepository;
import USCitizenshipFlashcard.model.User;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/users")
    public List<User> getAllUsers() { return userRepository.findAll();}

    @GetMapping("/users/{id}")
    public UserResponse getUserById(@PathVariable(value = "id") Long userId){
        Optional<User> user = userRepository.findById(userId);
        if(user.isPresent()) {
            return new UserResponse(user.get());
        } else {
            UserResponse response = new UserResponse(null);
            response.setStatus(HttpStatus.NOT_FOUND.toString());
            response.setErrorMessage("User with ID: " + userId + " does not exist.");
            return response;
        }
    }

    @PostMapping("/users")
    public UserResponse createUser (@RequestBody User user) {
        BCryptPasswordEncoder bCrypt = new BCryptPasswordEncoder();
        String encryptPwd = bCrypt.encode(user.getPassword());
        user.setPassword(encryptPwd);
        try {
            return new UserResponse(userRepository.save(user));
        } catch (DataIntegrityViolationException exception) {
            UserResponse userResponse = new UserResponse(null);
            userResponse.setErrorMessage("unknown error");

            Throwable cause = exception.getMostSpecificCause();
            System.out.println(cause.getClass());
            if(cause.getClass() == PSQLException.class) {
                PSQLException psqlException = (PSQLException) cause;
                userResponse.setErrorMessage("Can not create user because: " + psqlException.getMessage());
            }
            return userResponse;
        }
    }

    @PutMapping("/users/{id}")
    public UserResponse updateUser (@PathVariable("id") Long userId , @RequestBody User userUpdate) throws ResourceNotFoundException {
        Optional<User> user = userRepository.findById(userId);
        if(user.isPresent()) {
            User currentUser = user.get();
            if(userUpdate.getUsername() != null) {
                currentUser.setUsername(userUpdate.getUsername());
            }
            if(userUpdate.getFirstname() != null) {
                currentUser.setFirstname(userUpdate.getFirstname());
            }
            if(userUpdate.getLastname() != null) {
                currentUser.setLastname(userUpdate.getLastname());
            }
            if(userUpdate.getEmail() != null) {
                currentUser.setEmail(userUpdate.getEmail());
            }
            if(userUpdate.getPassword() != null) {
                BCryptPasswordEncoder bCrypt = new BCryptPasswordEncoder();
                String encryptPwd = bCrypt.encode(userUpdate.getPassword());
                currentUser.setPassword(encryptPwd);
            }
            try {
                final User updateUser = userRepository.save(currentUser);
                return new UserResponse(updateUser);
            } catch (TransactionSystemException exception) {
                UserResponse userResponse = new UserResponse(null);
                userResponse.setErrorMessage(exception.getMessage());
                userResponse.setStatus(HttpStatus.BAD_REQUEST.toString());

                Throwable cause = exception.getMostSpecificCause();
                if(cause.getClass() == ConstraintViolationException.class) {
                    ConstraintViolationException constraintViolationException = (ConstraintViolationException) cause;
                    Set<ConstraintViolation<?>> violations = constraintViolationException.getConstraintViolations();
                    String errorMsj = "Can not update user do to following violations: ";
                    for(ConstraintViolation<?> violation : violations) {
                        errorMsj += violation.getMessage();
                    }
                    userResponse.setErrorMessage(errorMsj);
                }
                return userResponse;
            }
        } else {
            UserResponse response = new UserResponse(null);
            response.setStatus(HttpStatus.NOT_FOUND.toString());
            response.setErrorMessage("User with ID: " + userId + " does not exist.");
            return response;
        }
    }

    @DeleteMapping("/users/{id}")
    public BaseResponse deleteUser (@PathVariable (value = "id") Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if(user.isPresent()) {
            User currentUser = user.get();
            userRepository.delete(currentUser);
            return new BaseResponse();
        } else {
            BaseResponse response = new BaseResponse();
            response.setStatus(HttpStatus.NOT_FOUND.toString());
            response.setErrorMessage("User with ID: " + userId + " does not exist.");
            return response;
        }
    }
}
