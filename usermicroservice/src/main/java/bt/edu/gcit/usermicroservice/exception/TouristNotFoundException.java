package bt.edu.gcit.usermicroservice.exception;

public class TouristNotFoundException extends RuntimeException {
    public TouristNotFoundException(String message) {
        super(message);
    }
}
