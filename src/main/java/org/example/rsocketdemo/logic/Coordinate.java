package org.example.rsocketdemo.logic;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@ToString
public class Coordinate {
    private final int x;
    private final int y;
    public static Coordinate of(int x, int y){
        return new Coordinate(x, y);
    }
    public Coordinate next(String direction){
        if("left".equals(direction)) return Coordinate.of(x, y-1);
        if("right".equals(direction)) return Coordinate.of(x, y+1);
        if("down".equals(direction)) return Coordinate.of(x+1, y);
        return Coordinate.of(x-1, y);
    }

    public static Coordinate asCoordinate(int index, int dimension){
        return Coordinate.of(index / dimension, index % dimension);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Coordinate target){
            return target.getX() == getX() && target.getY() == getY();
        }
        return false;
    }
}
