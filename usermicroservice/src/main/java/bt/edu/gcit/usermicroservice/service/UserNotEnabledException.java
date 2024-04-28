package bt.edu.gcit.usermicroservice.service;

public class UserNotEnabledException extends RuntimeException {
    public UserNotEnabledException(String message) {
        super(message);
    }
}