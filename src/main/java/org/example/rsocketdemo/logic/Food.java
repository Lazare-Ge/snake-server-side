package org.example.rsocketdemo.logic;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Random;

@Getter
@Setter
@AllArgsConstructor
public class Food {

    private final Random random = new Random();
    private Coordinate foodCoordinates;
    public void spawnRandom(List<Coordinate> coordinates) {
        Coordinate newCoordinates = coordinates.get(random.nextInt(0, coordinates.size()));
        System.out.println("New food spawned! " + newCoordinates);
        this.foodCoordinates = newCoordinates;
    }
}
