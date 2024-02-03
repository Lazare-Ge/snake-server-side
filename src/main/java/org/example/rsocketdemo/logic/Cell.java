package org.example.rsocketdemo.logic;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

@RequiredArgsConstructor
@Getter
public class Cell {

    private final Board board;
    private final int x;
    private final int y;

    @Setter
    CellType type;

    public int getCellNum(){
        return board.getWidth() * x + y;
    }

    public static Tuple2<Integer, Integer> getNextCellCoordinates(Direction direction, Cell currentCell){
        int currentX = currentCell.getX();
        int currentY = currentCell.getY();
        if(direction == Direction.LEFT) return Tuples.of(currentX, currentY-1);
        if(direction == Direction.RIGHT) return Tuples.of(currentX, currentY+1);
        if(direction == Direction.UP) return Tuples.of(currentX-1, currentY);
        return Tuples.of(currentX+1, currentY);
    }

    public static Tuple2<Integer, Integer> getCellCoordinates(Board board, int cellNum){
        int x = cellNum / board.getHeight();
        int y = cellNum % board.getHeight();
        return Tuples.of(x, y);
    }

    @Override
    public int hashCode() {
        return getCellNum();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Cell && getCellNum() == ((Cell) obj).getCellNum();
    }
}
