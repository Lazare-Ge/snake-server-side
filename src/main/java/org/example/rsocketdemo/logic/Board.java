package org.example.rsocketdemo.logic;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import reactor.util.function.Tuple2;

import java.util.stream.IntStream;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class Board {

    @Getter
    private final int height;

    @Getter
    private final int width;
    private Cell[][] board;

    public void initBoard(){
        Cell[][] board = new Cell[height][width];
        for(int x=0; x<height; x++){
            for(int y=0; y<width; y++){
                board[x][y] = new Cell(this, x, y);
                board[x][y].setType(CellType.EMPTY);
            }
        }
        this.board = board;
    }

    public Stream<Cell> getBoardAsCellStream(){
        return IntStream.range(0, getNumCells())
                .boxed()
                .map(this::getCell);
    }

    public Cell getCell(int x, int y){
        return board[x][y];
    }
    public Cell getCell(int cellNum) {
        Tuple2<Integer, Integer> coordinates = Cell.getCellCoordinates(this, cellNum);
        return getCell(coordinates.getT1(), coordinates.getT2());
    }

    public Cell getCell(Tuple2<Integer, Integer> coordinates){
        return getCell(coordinates.getT1(), coordinates.getT2());
    }

    public int getNumCells() { return width * height;}

}
