package petespike.model;

import java.io.*;
import java.util.*;


public class PetesPike {
    //placeholders for the variables on the board
    public static final char MOUNTAINTOP_SYMBOL = 'T';
    public static final char EMPTY_SYMBOL = '-';
    public static final char PETE_SYMBOL = 'P';
    public final static Set<Character> GOAT_SYMBOLS = Set.of('0', '1', '2', '3','4', '5', '6', '7', '8');

    char[][] board;
    private int ROWS;
    private int COLS;
    private Position mountaintop = null;
    private int moveCount = -1;
    private int GOAT_COUNT;
    private GameState gameState = GameState.NEW;

    private Position oldPosition;
    private Position newPosition;

    private PetesPikeObserver observer;

    private String filename;
        
    
        public PetesPike(String filename){
            this.filename = filename;
            reset(filename);
        }
        public PetesPike() {}

        public PetesPike clone() {
            PetesPike clonedGame = new PetesPike();
            clonedGame.board = board;
            clonedGame.ROWS = ROWS;
            clonedGame.COLS = COLS;
            clonedGame.mountaintop = mountaintop;
            clonedGame.moveCount = moveCount;
            clonedGame.GOAT_COUNT = GOAT_COUNT;
            clonedGame.gameState = gameState;
            clonedGame.oldPosition = oldPosition;
            clonedGame.newPosition = newPosition;
            clonedGame.observer = observer;
            clonedGame.filename = filename;
            return clonedGame;
        }
        public String getFilename() {
            return this.filename;
        }
        public int getRows() {
            return ROWS;
        }
        public int getCols() {
            return COLS;
        }
        public int getGoatCount() {
            return GOAT_COUNT;
        }
        public int getMoveCount() { 
            return moveCount;
        }
        public GameState getGameState() {
            return gameState;
        }
        public void setGameState(){
            if (moveCount < 0){
                gameState = GameState.NEW;
            }
            else if (moveCount >= 0){
                gameState = GameState.IN_PROGRESS; 
                Position pete_position = getPositionOf(PETE_SYMBOL);
                if (pete_position.getRow() == getMountaintop().getRow()
                    && pete_position.getCol() == getMountaintop().getCol()){
                    gameState = GameState.WON;
                }
            }
            else if (getPossibleMoves().isEmpty()){
                gameState = GameState.NO_MOVES;
            }
    
        }
    
        //checks if position is on board
        public boolean isValid(Position position){
            if (position == null){
                return false;
            }
            return (position.getRow() >= 0
                && position.getRow() < ROWS
                && position.getCol() >= 0
                && position.getCol() < COLS);
        }
    
        public boolean isGoat(char symbol){
            return GOAT_SYMBOLS.contains(symbol);
        }
    
        public boolean isPossibleMove(Move move){
            List<Move> possibleMoves = getPossibleMoves();
            //for each possible move in possible moves
            for (Move possibleMove : possibleMoves) {
                //if the move in possible moves
                if (possibleMove.equals(move)) {
                    return true;
                }
            }
            //it went through all possible moves and move not in there so false
            return false;
        }
    
        // return the symbol at position (row, col)
        public char getSymbolAt(Position position){
            if (!isValid(position)){
                System.out.println(new PetesPikeException("The given position is an invalid location on the board."));
                return '\0';
            }
            return board[position.getRow()][position.getCol()];
        }
    
        // returns the position of either pete or goat
        public Position getPositionOf(char symbol){
            //iterates thru the grid
            for (int row = 0; row < ROWS; row++){
                for (int col = 0; col < COLS; col++){
                    //creates position
                    Position aPosition = new Position(row, col); 
                    //checks if they match
                    if (symbol == getSymbolAt(aPosition)){
                        return aPosition;
                    }
                }
            }
            //not found then its null
            return null;
        }
    
        public List<Move> getPossibleMoves(){
            // initializing possible moves
            List<Move> possibleMoves = new ArrayList<>();
            if (gameState == GameState.WON || gameState == GameState.NEW){
                System.out.println(new PetesPikeException("There must be an active game to use this command"));
                return possibleMoves;
            }
            //iterate all space
            for (int row = 0 ; row < ROWS ; row++){
                for (int col = 0 ; col < COLS ; col++){
                    char moveSymbol = board[row][col];
                    //skip empties and mountain bc they cant move
                    if (moveSymbol == EMPTY_SYMBOL || moveSymbol == MOUNTAINTOP_SYMBOL){
                        continue;
                    }
                    //iterates through directions
                    for (Direction direction : Direction.values()) {
                        Position currentPosition = new Position(row, col);
                        //add the move to possible moves if not null
                        Position newPosition = getNewPosition(currentPosition, direction);
                        if (newPosition != null) {
                            Move newMove = new Move(currentPosition, direction);
                            possibleMoves.add(newMove);
                        }
                    }
                }
            }
            return possibleMoves;
        }
    
        public Position getNewPosition(Position currentPosition, Direction direction){
            // //the position in front of new position that will go on the occupied spot
            // //resets for each direction
            // Position frontPosition = new Position(currentPosition.getRow(), currentPosition.getCol());
            // //while front position is on board, keep moving
            // while (isValid(frontPosition)) {
            //     //save new position which will be actual space the symbol moves to
            //     Position newPosition = new Position(frontPosition.getRow(), frontPosition.getCol());
            //     // if the case direction, move one space that way
            //     switch(direction) {
            //         case UP:
            //             frontPosition.setRow(frontPosition.getRow() - 1);
            //             break;
            //         case DOWN:
            //             frontPosition.setRow(frontPosition.getRow() + 1);
            //             break;
            //         case LEFT:
            //             frontPosition.setCol(frontPosition.getCol() - 1);
            //             break;
            //         case RIGHT:
            //             frontPosition.setCol(frontPosition.getCol() + 1);
            //             break;
            //     }
            //     if (!isValid(frontPosition)){
            //         break;
            //     }
            //     if (isValid(newPosition) && getSymbolAt(frontPosition) != MOUNTAINTOP_SYMBOL
            //         && (getSymbolAt(frontPosition) == PETE_SYMBOL || isGoat(getSymbolAt(frontPosition)))){
            //         return newPosition;
            //     }
            // }
            // return null;
            boolean isPete = getSymbolAt(currentPosition) == PETE_SYMBOL;
            Position newPosition = new Position(currentPosition.getRow(), currentPosition.getCol());
            while (isValid(newPosition)) {
                // Check what's in front at the dir
                Position frontPosition = new Position(newPosition.getRow(), newPosition.getCol());
                switch(direction) {
                    case UP:
                        frontPosition.setRow(newPosition.getRow() - 1);
                        break;
                    case DOWN:
                        frontPosition.setRow(newPosition.getRow() + 1);
                        break;
                    case LEFT:
                        frontPosition.setCol(newPosition.getCol() - 1);
                        break;
                    case RIGHT:
                        frontPosition.setCol(newPosition.getCol() + 1);
                        break;
                }
                // If it's at the mountain
                if (isPete && getSymbolAt(frontPosition) == MOUNTAINTOP_SYMBOL) {
                    return frontPosition;
                } else if (getSymbolAt(frontPosition) == PETE_SYMBOL || isGoat(getSymbolAt(frontPosition))) {
                    return newPosition;
                }
                // Move piece in dir
                newPosition = frontPosition;
            }
            return null;
        }
    
        public Position getMountaintop(){
            return this.mountaintop;
        }
    
        public void makeMove(Move move) throws PetesPikeException {            
            if (gameState == GameState.WON || gameState == GameState.NEW){
                throw new PetesPikeException("There must be an active game to use this command");
            }
            Position currentPosition = move.getPosition();
            Direction direction = move.getDirection();
            
            if (! isValid(currentPosition)){
                // Position Invalid
                throw new PetesPikeException("The given position is invalid");
            }
            // Check if there is a piece to move
            char moveSymbol = getSymbolAt(currentPosition);
            if (moveSymbol == EMPTY_SYMBOL){
                throw new PetesPikeException("There is no piece at the specified position");
            }
            List<Move> possibleMoves = getPossibleMoves();
            System.out.println("possible moves: " + possibleMoves.toString());
            if (possibleMoves.isEmpty()){
                throw new PetesPikeException("No more possible moves. Please reset the board or start a new game.");
            }
    
            if (!isPossibleMove(move)) {
                throw new PetesPikeException("Invalid move made at current position");
            }
            newPosition = getNewPosition(currentPosition, direction);
            oldPosition = getPositionOf(moveSymbol);
        //set the old position to empty
        board[oldPosition.getRow()][oldPosition.getCol()] = EMPTY_SYMBOL;
        //move the symbol to the new position
        board[newPosition.getRow()][newPosition.getCol()] = moveSymbol;
    
        moveCount++;
        notifyObserver();
        setGameState();
    }
    
    // resets the board
    public void reset(String filename) {
        makeBoard(filename);
        notifyObserver();
    }

    //moved the board making stuff here to make it make sense if that makes sense
    public void makeBoard(String filename){
        String[] boardConfig = filename.split("_");
        ROWS = Integer.parseInt(boardConfig[2]);
        COLS = Integer.parseInt(boardConfig[3]);
        GOAT_COUNT = Integer.parseInt(boardConfig[4]);
        board = new char[ROWS][COLS];
        //use the txt to configure board
        try(BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            //skips first line
            reader.readLine();
            for (int row = 0; row < ROWS; row++) {
                String line = reader.readLine();
                for (int col = 0; col < COLS; col++) {
                    //convert each space on the line(which is string) into symbol(which is char)
                    char symbol = line.charAt(col);
                    board[row][col] = symbol;
                    if (symbol == MOUNTAINTOP_SYMBOL) {
                        mountaintop = new Position(row, col);
                    }
                    
                    }
            }
        //put here in case of typo but ignore bc the instructions say assume all files valid
        } catch(FileNotFoundException fnfe) {
            System.out.println(new PetesPikeException("File not found: " + filename));
            return;
        } catch(IOException ioe){
            System.out.println(new PetesPikeException("Failed to read file: " + filename));
            return;
        }
        moveCount = 0;
        notifyObserver();
        setGameState();
    }
    public void registerObserver(PetesPikeObserver observer) {
        this.observer = observer;
    }
    public void notifyObserver() {
        if (this.observer != null) {
            this.observer.pieceMoved(oldPosition, newPosition);
        }
    }
}