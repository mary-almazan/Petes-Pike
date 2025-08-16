// Creates PetesPikeException to return a message
// if a file cannot be opened, a position on the board is invalid,
// or move cannot be completed

package petespike.model;

public class PetesPikeException extends Exception {
    public PetesPikeException(String message){
        super(message);
    }
}
