package org.example.rsocketdemo.logic;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@RequiredArgsConstructor
public class Food {

    Board board;

    private Cell position;
    public void spawnRandom() {
        List<Cell> freeCells = board.getBoardAsCellStream()
                .filter(cell -> cell.getType() == CellType.EMPTY)
                .toList();
        int index = RandomHolder.random.nextInt(0, freeCells.size());
        Cell cell = freeCells.get(index);
        cell.setType(CellType.FOOD);
        this.position = cell;
    }

}
