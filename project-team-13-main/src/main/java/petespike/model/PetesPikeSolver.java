package petespike.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import backtracker.Backtracker;
import backtracker.Configuration;

public class PetesPikeSolver implements Configuration<PetesPikeSolver> {
    private PetesPike game;
    private List<Move> solutionMoves;

    public PetesPikeSolver(PetesPike game) {
        // making a copy
        this.game = game.clone();

        // char[][] originalBoard = new char[game.getRows()][game.getCols()];
        // for (int row = 0; row < game.getRows(); row++) {
        //     for (int col = 0; col < game.getCols(); col++) {
        //         originalBoard[row][col] = game.getSymbolAt(new Position(row, col));
        //     }
        // }

        // // set the copy
        // for (int row = 0; row < game.getRows(); row++) {
        //     for (int col = 0; col < game.getCols(); col++) {
        //         // this.game.board[row][col] = originalBoard[row][col];
        //         this.game.board[row][col] = game.getSymbolAt(new Position(row, col));
        //     }
        // }

        this.solutionMoves = new ArrayList<>();
    }

    private PetesPikeSolver(PetesPike game, List<Move> previousMoves) {
        this.game = game;
        this.solutionMoves = new ArrayList<>(previousMoves);
    }

    // puzzle solver
    public static PetesPikeSolver solve(PetesPike game) {
        Backtracker<PetesPikeSolver> backtracker = new Backtracker<>(false);
        PetesPikeSolver solver = new PetesPikeSolver(game);
        return backtracker.solve(solver);
    }

    @Override
    public boolean isGoal() {
        Position petePos = game.getPositionOf(PetesPike.PETE_SYMBOL);
        Position mountaintop = game.getMountaintop();
        return petePos != null && petePos.equals(mountaintop);
    }

    @Override
    public boolean isValid() {
        return game.getPossibleMoves().size() > 0 || isGoal();
    }

    @Override
    public Collection<PetesPikeSolver> getSuccessors() throws PetesPikeException {
        List<PetesPikeSolver> successors = new ArrayList<>();
        List<Move> possibleMoves = game.getPossibleMoves();
        System.out.println("Possible moves: " + possibleMoves.toString());

        for (Move move : possibleMoves) {
            // cp of game
            PetesPike newGame = game.clone();

            // // cp of board
            // char[][] originalBoard = new char[game.getRows()][game.getCols()];
            // for (int row = 0; row < game.getRows(); row++) {
            //     for (int col = 0; col < game.getCols(); col++) {
            //         originalBoard[row][col] = game.getSymbolAt(new Position(row, col));
            //     }
            // }

            // for (int row = 0; row < game.getRows(); row++) {
            //     for (int col = 0; col < game.getCols(); col++) {
            //         // newGame.board[row][col] = originalBoard[row][col];
            //         newGame.board[row][col] = game.getSymbolAt(new Position(row, col));
            //     }
            // }

            try {
                newGame.makeMove(move);
            } catch (PetesPikeException e) {
                System.out.println("Bad move: %s" + move.toString());
                continue;
            }

            PetesPikeSolver successor = new PetesPikeSolver(newGame, solutionMoves);
            successor.solutionMoves.add(move);
            successors.add(successor);
            System.out.println(successor.toString());
            // System.out.println("Solution moves: " + successor.solutionMoves.toString());
        }
        System.out.println("successors: " + successors.toString());
        // How to intentionally make your program crash to debug:
        // int something = 0;
        // int somethingElse = 1 / something;
        return successors;
    }

    // solution for moves
    public List<Move> getSolutionMoves() {
        return new ArrayList<>(solutionMoves);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
    
        sb.append("Solution moves\n");
        for (int i = 0; i < solutionMoves.size(); i++) {
            sb.append(i + 1).append(". ")
              .append(solutionMoves.get(i)).append("\n");
        }

        sb.append("\nThe Current Board: \n");
        for (int row = 0; row < game.getRows(); row++) {
            for (int col = 0; col < game.getCols(); col++) {
                sb.append(game.getSymbolAt(new Position(row, col)));
            }
            sb.append("\n");
        }

        return sb.toString();
    }
}