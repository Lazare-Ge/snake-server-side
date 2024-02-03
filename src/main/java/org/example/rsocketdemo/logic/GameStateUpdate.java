package org.example.rsocketdemo.logic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import reactor.util.function.Tuple2;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameStateUpdate {
    List<Tuple2<Integer, Integer>> snakePixels;
    Tuple2<Integer, Integer> foodPixel;
}
