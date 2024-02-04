package org.example.rsocketdemo.logic;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.LinkedList;
import java.util.List;

@RequiredArgsConstructor
@Getter
public class Snake {
    private final List<Coordinate> snake;
    public static Snake defaultInstance(){
        return new Snake(new LinkedList<>(List.of(new Coordinate(0,0))));
    }
    public Coordinate head(){
        return snake.get(0);
    }

    public Coordinate tail(){
        return snake.get(snake.size()-1);
    }
    public boolean collides(Coordinate destination){
        return snake.size() > 1 && snake.contains(destination);
    }

    public void eat(){
        snake.add(tail());
    }

    public void move(Coordinate destination) {
        snake.remove(snake.size()-1);
        snake.add(0, destination);
    }
}
