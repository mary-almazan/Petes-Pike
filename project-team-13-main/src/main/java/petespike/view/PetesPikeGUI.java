package petespike.view;

import petespike.model.Direction;
import petespike.model.Move;
import petespike.model.PetesPike;
import petespike.model.PetesPikeException;
import petespike.model.PetesPikeObserver;
import petespike.model.Position;
import petespike.model.PetesPikeSolver;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.util.HashMap;
import java.util.List;

public class PetesPikeGUI extends Application {
    private PetesPike game;
    private String filename = "data/petes_pike_9_9_9_0.txt"; 
    private Button[][] boardButtons;
    private Label statusLabel;
    private Label moveCountLabel;
    private GridPane boardGrid = new GridPane();
    private Button selectedButton;
    private Position selectedPosition;
    private VBox hintBox;
    private Position hintPosition;
    private Direction hintDirection;
    private boolean gameWon = false;
    private PetesPikeObserver observer;
    private static HashMap<Character, Image> images = new HashMap<>();
    private BorderPane root;

    @Override
    public void start(Stage primaryStage) {
        // Create an initial game
        game = new PetesPike(filename);

        // Create the GUI elements
        root = new BorderPane();
        root.setPadding(new Insets(10));

        VBox statusArea = createStatusArea();
        root.setTop(statusArea);
        this.game.registerObserver(observer);

        boardGrid = createGameBoard();
        root.setCenter(boardGrid);

        VBox controlPanel = createControlPanel();
        root.setRight(controlPanel);

        Scene scene = new Scene(root);
        primaryStage.setTitle("Pete's Pike");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Initialize and display the initial game state
        loadGoatImages(); 
        updateBoard(); 
        updateStatus("New game started!"); 
    }

    private void loadGoatImages() {
        int imageSize = calculateImageSize();
        images.clear();
        images.put('P', new Image("file:data/images/pete.png", imageSize, imageSize, true, true));
        images.put('T', new Image("file:data/images/mountain.png", imageSize, imageSize, true, true));
        images.put('0', new Image("file:data/images/goat0.jpeg", imageSize, imageSize, true, true));
        images.put('1', new Image("file:data/images/goat1.png", imageSize, imageSize, true, true));
        images.put('2', new Image("file:data/images/goat2.png", imageSize, imageSize, true, true));
        images.put('3', new Image("file:data/images/goat3.png", imageSize, imageSize, true, true));
        images.put('4', new Image("file:data/images/goat4.jpg", imageSize, imageSize, true, true));
        images.put('5', new Image("file:data/images/goat5.png", imageSize, imageSize, true, true));
        images.put('6', new Image("file:data/images/goat6.png", imageSize, imageSize, true, true));
        images.put('7', new Image("file:data/images/goat7.png", imageSize, imageSize, true, true));
        images.put('8', new Image("file:data/images/goat8.png", imageSize, imageSize, true, true));
        images.put('-', new Image("file:data/images/blanksquare.png", imageSize, imageSize, true, true));
    }

    private int calculateImageSize() {
        int rows = game.getRows();
        int cols = game.getCols();
        int maxDimension = Math.max(rows, cols);
        int baseSize = 600;
        return Math.max(baseSize / maxDimension, 20);
    }

    private int calculateButtonSize() {
        int rows = game.getRows();
        int cols = game.getCols();
        int maxDimension = Math.max(rows, cols);
        int baseSize = 600;
        return Math.max(baseSize / maxDimension, 20);
    }

    private int calculateGapSize(int boardSize) {
        return Math.max(2, 600 / (boardSize * 10));
    }

    private GridPane createGameBoard() {
        boardGrid = new GridPane();
        boardGrid.getChildren().clear();
        boardGrid.setAlignment(Pos.CENTER);

        int boardSize = Math.max(game.getRows(), game.getCols());
        int gap = calculateGapSize(boardSize);
        boardGrid.setHgap(gap);
        boardGrid.setVgap(gap);

        Image BACKGROUND_IMAGE = new Image("file:data/images/bg.png");
        BackgroundSize BG_SIZE = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true);
        boardGrid.setBackground(new Background(new BackgroundImage(BACKGROUND_IMAGE, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BG_SIZE)));

        boardButtons = new Button[game.getRows()][game.getCols()];

        for (int row = 0; row < game.getRows(); row++) {
            for (int col = 0; col < game.getCols(); col++) {
                Button button = createBoardButton(row, col);
                boardButtons[row][col] = button;
                boardGrid.add(button, col, row);
            }
        }

        updateBoard();
        return boardGrid;
    }

    private void updateBoard() {
        for (int row = 0; row < game.getRows(); row++) {
            for (int col = 0; col < game.getCols(); col++) {
                updateButtonSymbol(new Position(row, col));
            }
        }
    }

    private void updateButtonSymbol(Position pos) {
        char symbol = game.getSymbolAt(pos);
        Button button = boardButtons[pos.getRow()][pos.getCol()];

        // if (symbol != PetesPike.EMPTY_SYMBOL) {
        ImageView imgView = new ImageView(images.get(symbol));
        button.setGraphic(imgView);
        // } else {
        //     button.setGraphic(null);
        //     button.setStyle("-fx-font-size: 12px; -fx-background-color: #F0F8FF; -fx-opacity: 0.82");
        // }
    }

    private Button createBoardButton(int row, int col) {
        Button button = new Button();
        int buttonSize = calculateButtonSize();
        button.setPrefSize(buttonSize, buttonSize);
        button.setMaxSize(buttonSize, buttonSize);
        button.setPadding(new Insets(0));
        button.setStyle("-fx-font-size: 12px; -fx-background-color: #F0F8FF; -fx-opacity: 0.82");

        button.setOnAction(e -> handleBoardClick(row, col));

        return button;
    }

    private VBox createStatusArea() {
        VBox statusArea = new VBox(10);
        statusArea.setPadding(new Insets(10));

        HBox puzzleArea = new HBox(10);
        ComboBox<String> dropdownMenu = new ComboBox<>();
        dropdownMenu.setPromptText("Select Map");
        dropdownMenu.getItems().addAll(
            "data/petes_pike_4_8_5_no_solution.txt",
            "data/petes_pike_5_5_2_0.txt",
            "data/petes_pike_5_5_4_0.txt",
            "data/petes_pike_5_5_4_1.txt",
            "data/petes_pike_5_5_5_0.txt",
            "data/petes_pike_5_7_4_0.txt",
            "data/petes_pike_9_9_9_0.txt",
            "Select puzzle from computer"
        );
        dropdownMenu.setOnAction(e -> {
            String selectedMap = dropdownMenu.getValue();
            if (selectedMap.equals("Select puzzle from computer")) {
                loadNewGameFromSearch();
            } else {
                filename = selectedMap;
                loadNewGame();
            }
        });
        
        Label puzzleLabel = new Label("Puzzle: ");
        puzzleArea.getChildren().addAll(puzzleLabel, dropdownMenu);

        statusLabel = new Label("Welcome to Pete's Pike!");
        moveCountLabel = new Label("Moves: 0");

        statusArea.getChildren().addAll(puzzleArea, statusLabel, moveCountLabel);
        return statusArea;
    }

    private void loadNewGameFromSearch() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose A Puzzle File: ");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Text Files", "*.txt")
        );

        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            filename = file.getPath();
            loadNewGame();
        }
    }

    private void loadNewGame() {
        game = new PetesPike(filename); 
        // Load the button images
        loadGoatImages(); 
        boardGrid.getChildren().clear(); 
        boardButtons = new Button[game.getRows()][game.getCols()];
        boardGrid = createGameBoard();
        root.setCenter(boardGrid);
        updateBoard(); 
    
        moveCountLabel.setText("Moves: 0");
        clearHint();
        gameWon = false;
        updateStatus("New game start!");
    }

    private void handleBoardClick(int row, int col) {
        if (gameWon) {
            updateStatus("You won the game. Play again?");
            return;
        }

        clearHint();

        if (selectedButton != null) {
            selectedButton.setStyle("-fx-font-size: 18px;");
        }

        Position pos = new Position(row, col);
        char symbol = game.getSymbolAt(pos);

        if (symbol == PetesPike.PETE_SYMBOL || game.isGoat(symbol)) {
            selectedButton = boardButtons[row][col];
            selectedButton.setStyle("-fx-font-size: 18px; -fx-background-color: yellow;");
            selectedPosition = pos;
            updateStatus("Selected piece is at " + row + ", " + col);
        } else {
            selectedButton = null;
            selectedPosition = null;
            updateStatus("Select 'Pete' or a 'Goat'");
        }
    }

    private void tryMove(Direction direction) {
        if (gameWon) {
            updateStatus("Game Over. Play Again?");
            return;
        }

        if (selectedPosition == null) {
            updateStatus("Select 'Pete' or a 'Goat' first: ");
            return;
        }

        clearHint();

        Move move = new Move(selectedPosition, direction);

        Position oldPos = new Position(selectedPosition.getRow(), selectedPosition.getCol());
        char symbol = game.getSymbolAt(oldPos);

        try {
            game.makeMove(move);
        } catch (PetesPikeException e) {
            updateStatus("There is no piece in that direction to stop you.");
        }

        Position newPos = game.getPositionOf(symbol);
        updateButtonSymbol(newPos);
        updateButtonSymbol(oldPos);

        moveCountLabel.setText("Moves: " + game.getMoveCount());
        selectedButton.setStyle("-fx-font-size: 18px;");
        selectedButton = null;
        selectedPosition = null;

        checkWinCondition();
    }

    private void showHint() {
        if (gameWon) {
            updateStatus("Game Over. Play Again?");
            return;
        }

        PetesPikeSolver solver = PetesPikeSolver.solve(game);
        if (solver != null && !solver.getSolutionMoves().isEmpty()) {
            Move hint = solver.getSolutionMoves().get(0);
            hintPosition = hint.getPosition();
            hintDirection = hint.getDirection();

            Button hintButton = boardButtons[hintPosition.getRow()][hintPosition.getCol()];
            hintButton.setStyle("-fx-background-color: lightgreen;");

            Text hintText = new Text("Move from ( " + hintPosition.getRow() + ", " + hintPosition.getCol() + ") " + hintDirection);
            hintBox.getChildren().clear();
            hintBox.getChildren().add(hintText);

            updateStatus("Hint is highlighted in green");
        } else {
            updateStatus("No solution found!");
        }
    }

    private void resetGame() {
        game.reset(filename);
        updateBoard();
        moveCountLabel.setText("Moves: 0");
        clearHint();
        gameWon = false;
        updateStatus("Game reset!");
    }

    private void solveGame() {
        if (gameWon) {
            updateStatus("Game Won! Play Again?");
            return;
        }

        PetesPikeSolver solver = PetesPikeSolver.solve(game);
        if (solver != null) {
            List<Move> solutionMoves = solver.getSolutionMoves();

            new Thread(() -> {
                for (Move move : solutionMoves) {
                    Platform.runLater(() -> {
                        try {
                            game.makeMove(move);
                        } catch (PetesPikeException e) {
                            updateStatus("There is no piece in that direction to stop you.");
                        }

                        updateBoard();
                        moveCountLabel.setText("Moves: " + game.getMoveCount());
                        checkWinCondition();
                    });

                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }).start();
        } else {
            updateStatus("No solution found!");
        }
    }

    private void checkWinCondition() {
        Position petePos = game.getPositionOf(PetesPike.PETE_SYMBOL);
        Position mountaintop = game.getMountaintop();

        if (petePos.equals(mountaintop)) {
            gameWon = true;
            updateStatus("Congrats! You won in " + game.getMoveCount() + " moves!");
        }
    }

    private void clearHint() {
        if (hintPosition != null) {
            boardButtons[hintPosition.getRow()][hintPosition.getCol()]
                .setStyle("-fx-font-size: 18px;");
            hintPosition = null;
            hintDirection = null;
            hintBox.getChildren().clear();
        }
    }

    private void updateStatus(String message) {
        statusLabel.setText(message);
    }

    private VBox createControlPanel() {
        VBox panel = new VBox(10);
        panel.setPadding(new Insets(10));
        panel.setAlignment(Pos.TOP_CENTER);

        GridPane directionGrid = new GridPane();
        directionGrid.setAlignment(Pos.CENTER);

        Button upButton = new Button("", new ImageView(new Image("file:data/images/upbutton.png", 25, 25, true, true)));
        Button downButton = new Button("", new ImageView(new Image("file:data/images/downbutton.png", 25, 25, true, true)));
        Button leftButton = new Button("", new ImageView(new Image("file:data/images/leftbutton.png", 25, 25, true, true)));
        Button rightButton = new Button("", new ImageView(new Image("file:data/images/rightbutton.png", 25, 25, true, true)));
        upButton.setPadding(new Insets(0));
        downButton.setPadding(new Insets(0));
        leftButton.setPadding(new Insets(0));
        rightButton.setPadding(new Insets(0));

        upButton.setOnAction(e -> tryMove(Direction.UP));
        downButton.setOnAction(e -> tryMove(Direction.DOWN));
        leftButton.setOnAction(e -> tryMove(Direction.LEFT));
        rightButton.setOnAction(e -> tryMove(Direction.RIGHT));
        
        directionGrid.add(upButton, 1, 0);
        directionGrid.add(leftButton, 0, 1);
        directionGrid.add(rightButton, 2, 1);
        directionGrid.add(downButton, 1, 2);
        
        Button hintButton = new Button("Get Hint");
        hintButton.setOnAction(e -> showHint());

        Button resetButton = new Button("Reset Game");
        resetButton.setOnAction(e -> resetGame());

        Button solveButton = new Button("Solve Game");
        solveButton.setOnAction(e -> solveGame());

        panel.getChildren().addAll(directionGrid, hintButton, solveButton, resetButton);
        return panel;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
