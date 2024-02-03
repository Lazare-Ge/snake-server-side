package org.example.rsocketdemo.logic;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.LinkedList;
import java.util.List;

@RequiredArgsConstructor
@Getter
public class Game {
    private final Board board;
    private final Snake snake;
    private final Food food;
    private final Score score;
    private final int tickRateMillis;

    @Setter
    private GameInput input;
    @Setter
    private GameStatus status;

    public static Game getDefaultGame(){
        Board board = new Board(30, 30);
        Snake snake = new Snake(new LinkedList<>());
        Food food = new Food();
        food.setBoard(board);
        Score score = new Score();
        Game game = new Game(board, snake, food, score, 300);
        snake.setGame(game);
        return game;
    }

    public void initGame() {
        board.initBoard();
        snake.spawnRandom();
        snake.setDirection(Direction.RIGHT);
        food.spawnRandom();
        setStatus(GameStatus.RUNNING);
        GameInput initialInput = new GameInput();
        initialInput.setInputDirection(Direction.DOWN);
        setInput(initialInput);
    }

    public void changeDirection(String direction){
        if("up".equals(direction))
            snake.setDirection(Direction.UP);
        if("down".equals(direction))
            snake.setDirection(Direction.DOWN);
        if("left".equals(direction))
            snake.setDirection(Direction.LEFT);
        if("right".equals(direction))
            snake.setDirection(Direction.RIGHT);
    }

    public void tick(){
        if(status != GameStatus.RUNNING) return;
        snake.move();
    }
    public GameStateUpdate getState(){
        List<Tuple2<Integer, Integer>> snakePixels = snake.getContained().stream()
                .map(cell -> Tuples.of(cell.getX(), cell.getY()))
                .toList();
        ;
        Cell foodCell = this.food.getPosition();
        Tuple2<Integer, Integer> foodPixel = Tuples.of(foodCell.getX(), foodCell.getY());

        return new GameStateUpdate(snakePixels, foodPixel);
    }

}
