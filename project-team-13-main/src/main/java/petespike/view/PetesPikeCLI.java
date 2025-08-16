package petespike.view;

import java.util.*;
import petespike.model.*;

public class PetesPikeCLI {
    public static void main(String[] args) throws PetesPikeException {
        try(Scanner scanner = new Scanner(System.in)) {
            System.out.println("Puzzle Filename: ");
            String filename = scanner.nextLine();
            PetesPike game = new PetesPike(filename);
            boolean sentinel = true;
            while(sentinel) {
                System.out.println(gameToString(game));
                System.out.println("Moves:" + game.getMoveCount());
                System.out.println(game.getGameState());
                System.out.println("Command: ");
                String[] command = scanner.nextLine().split(" ");
                switch(command[0]) {
                    case "quit":
                        sentinel = !areYouSure(scanner);
                        break;
                    case "reset":
                        if(areYouSure(scanner)) {
                            game.reset(filename);
                        }
                        break;
                    case "move":
                        move(command, game);
                        break;
                    case "help":
                        help();
                        break;
                    case "board":
                        // System.out.println(gameToString(game));
                        break;
                    case "new":
                        while (command.length <= 1){
                            System.out.println("Use the format: new filename");
                            command = scanner.nextLine().split(" ");
                        }
                        game = new PetesPike(command[1]);
                        break;
                    case "hint":
                        hint(game);
                        break;
                    default:
                        invalid(command);
                        break;
                }
            }
            System.out.println("Goodbye!");
        }
    }

    private static String gameToString(PetesPike board) throws PetesPikeException {
        StringBuilder builder = new StringBuilder();
        int column_label = 0;
        int row_label = 0;
        // space to even out labels
        builder.append("  ");
        for(int col = 0; col < board.getCols(); col++) {
            builder.append(column_label++);
            builder.append(" ");
        }
        builder.append("\n");
        for(int row = 0; row < board.getRows(); row++) {
            builder.append(row_label++);
            builder.append(" ");
            for (int col = 0; col < board.getCols(); col++){
                char symbol = (char)board.getSymbolAt(new Position(row, col));
                switch(symbol) {
                    case PetesPike.PETE_SYMBOL:
                        builder.append(AsciiColorCodes.RED + "P" + AsciiColorCodes.RESET);
                        break;
                    case PetesPike.MOUNTAINTOP_SYMBOL:
                        builder.append(AsciiColorCodes.BLUE + "T" + AsciiColorCodes.RESET);
                        break;
                    case PetesPike.EMPTY_SYMBOL:
                        builder.append("-");
                        break;
                    default:
                        if (board.isGoat(symbol)){
                            ArrayList<String> goatColors = new ArrayList<>();
                            goatColors.add(AsciiColorCodes.ORANGE);
                            goatColors.add(AsciiColorCodes.GREEN);
                            goatColors.add(AsciiColorCodes.YELLOW);
                            goatColors.add(AsciiColorCodes.MAGENTA);
                            goatColors.add(AsciiColorCodes.GOLD);
                            goatColors.add(AsciiColorCodes.PURPLE);
                            goatColors.add(AsciiColorCodes.LT_GRAY);
                            goatColors.add(AsciiColorCodes.CYAN);
                            //pick color for goat
                            int goatNumber = (int)symbol-'0';
                            builder.append(goatColors.get(goatNumber) + "G" + AsciiColorCodes.RESET);
                        } else {
                            throw(new PetesPikeException("Attempted to add an Unknown Piece"));
                        }
                }
            builder.append(" ");
            }
            builder.append("\n");
        }
        return builder.toString();
    }
    
    private static boolean areYouSure(Scanner scanner) {
        System.out.print("Are you sure? (y/n): ");
        String response = scanner.nextLine();
        return response.equalsIgnoreCase("y");
    }

    private static void move(String[] command, PetesPike game) throws PetesPikeException {
        if(command.length != 4) {
            System.err.println("Invalid move!");
        } else {
            try {
                // gets row and col entered
                int row = Integer.parseInt(command[1]);
                int col = Integer.parseInt(command[2]);
                // makes sure move is in the board
                if (row < 0 || row >= game.getRows() || col < 0 || col >= game.getCols()){
                    throw new PetesPikeException("The position entered is out of bounds.");
                }
                // makes new move for the position (row, col) and the direction entered
                // gets direction by turning direction entered into uppercase like in the enum
                Position yourPosition = new Position(row, col);
                Direction yourDirection = Direction.valueOf(command[3].toUpperCase());
                Move yourMove = new Move(yourPosition, yourDirection);
                game.makeMove(yourMove);
            // makes sure row and col are ints
            } catch(NumberFormatException nfe) {
                System.out.println("Row and Column must be integers.");
            // makes sure direction exists
            } catch(IllegalArgumentException iae) {
                System.out.println("Invalid direction.");
            }
        }
    }
    public static void help(){
        System.out.println("Commands:"
            + "\n   help - this help menu"
            + "\n   board - display current board"
            + "\n   reset - reset current puzzle to the start"
            + "\n   new <puzzle_filename> - start a new puzzle"
            + "\n   move <row> <col> <direction> - move the piece at <row>, <col> where"
            + "\n       <direction> one of up, down, left, right"
            + "\n   hint - get a valid move, if one exists"
            + "\n   quit - quit");
    }

    public static void invalid(String[] command) {
        System.out.println("Invalid command: " + command[0]);
    }

    public static void hint(PetesPike game) throws PetesPikeException{
        List<Move> pmoves = game.getPossibleMoves();
        if (!pmoves.isEmpty()){
            System.out.println("Try: " + pmoves.get(0));
            return;
        }
        System.out.println(new PetesPikeException("No hints found. Please reset the board."));
    } 

    // private void runCommandLoop(Scanner scanner) {
    //     while (true) {
    //         System.out.print("Enter command (new, move, hint, or quit): ");
    //         String command = scanner.nextLine();

    //         if (command.startsWith("new ")) {
    //             String[] parts = command.split(" ");
    //             if (parts.length != 2) {
    //                 System.out.println("Invalid command format. Use: new <puzzle_filename>");
    //                 continue;
    //             }
    //             String newFilename = parts[1];
    //             try {
    //                 game = loadPuzzle(newFilename);
    //                 moves = 0;
    //                 displayBoard();
    //             } catch (PetesPikeException e) {
    //                 System.out.println("Error: " + e.getMessage());
    //             }
    //         } else if (command.startsWith("move ")) {
    //             String[] parts = command.split(" ");
    //             if (parts.length != 4) {
    //                 System.out.println("Invalid command format. Use: move <row> <col> <direction>");
    //                 continue;
    //             }
    //             int row = Integer.parseInt(parts[1]);
    //             int col = Integer.parseInt(parts[2]);
    //             String direction = parts[3];
    //             try {
    //                 game.move(row, col, direction);
    //                 moves++;
    //                 displayBoard();
    //             } catch (PetesPikeException e) {
    //                 System.out.println("Error: " + e.getMessage());
    //             }
    //         } else if (command.equals("hint")) {
    //             try {
    //                 int[] hint = game.getHint();
    //                 System.out.println("Hint: Move piece at (" + hint[0] + ", " + hint[1] + ") " + hint[2]);
    //             } catch (PetesPikeException e) {
    //                 System.out.println("Error: " + e.getMessage());
    //             }
    //         } else if (command.equals("quit")) {
    //             return;
    //         } else {
    //             System.out.println("Invalid command. Try again.");
    //         }
    //     }
    // }
}
