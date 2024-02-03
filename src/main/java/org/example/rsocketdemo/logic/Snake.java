package org.example.rsocketdemo.logic;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import reactor.util.function.Tuple2;

import java.util.List;

@RequiredArgsConstructor
@Getter
public class Snake {
    private final List<Cell> contained;

    @Setter
    private Game game;

    private Direction direction;

    public void setDirection(Direction direction){
        this.direction = direction;
    }
    public void spawnRandom(){
        Cell cell = game.getBoard().getCell(RandomHolder.random.nextInt(0, game.getBoard().getNumCells()));
        cell.setType(CellType.SNAKE);
        contained.add(cell);
        direction = Direction.LEFT;
    }

    public Cell getHeadCell(){
        return contained.get(0);
    }

    public void move(){
        Tuple2<Integer, Integer> nextCoordinates = Cell.getNextCellCoordinates(direction, getHeadCell());
        // Check game over situations
        if(isOutOfBounds(nextCoordinates) || hasCollision(nextCoordinates)) {
            game.setStatus(GameStatus.ENDED);
            return;
        }
        // Check if eats
        Cell nextCell = game.getBoard().getCell(nextCoordinates);
        boolean eats = false;
        if(nextCell.getType() == CellType.FOOD){
            eats = true;
            game.getScore().updateScore();
            game.getFood().spawnRandom();
        }

        // Move
        nextCell.setType(CellType.SNAKE);

        // Update snake
        contained.add(0, nextCell);
        if(!eats){
            Cell cell = contained.remove(snakeSize() - 1);
            cell.setType(CellType.EMPTY);
        }
    }

    public boolean hasCollision(Tuple2<Integer, Integer> coordinates){
        return game.getBoard().getCell(coordinates.getT1(), coordinates.getT2()).getType() == CellType.SNAKE;
    }
    public boolean isOutOfBounds(Tuple2<Integer, Integer> coordinates){
        int x = coordinates.getT1();
        int y = coordinates.getT2();
        if(x < 0 || x >= game.getBoard().getHeight()) return true;
        if(y < 0 || y >= game.getBoard().getWidth()) return true;
        return false;
    }

    public int snakeSize(){
        return contained.size();
    }



}
