package petespike.model;

public enum GameState {
    NEW("There must be an active game to use this command."),
    IN_PROGRESS(""),
    NO_MOVES("No more possible moves. Please reset the board or start a new game."),
    WON("Congratulations, you have scaled the mountain!");

    private final String message;

    GameState(String message){
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }
}
