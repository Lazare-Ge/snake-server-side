package org.example.rsocketdemo.logic;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
public class GameState {
    private final List<Coordinate> snake;
    private final Coordinate food;
    private final String status;
}
