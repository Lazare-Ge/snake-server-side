package org.example.rsocketdemo.logic;

import lombok.RequiredArgsConstructor;

import java.util.stream.IntStream;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class Board {
    private final int height;
    private final int width;
    public Stream<Coordinate> getBoardAsCoordinateStream(){
        return IntStream.range(0, width*height)
                .boxed()
                .map(index -> Coordinate.asCoordinate(index, height));
    }

    public boolean coordinateOut(Coordinate coordinate){
        int x = coordinate.getX();
        int y = coordinate.getY();
        if(x < 0 || x >= width) return true;
        if(y < 0 || y >= height) return true;
        return false;
    }


}
