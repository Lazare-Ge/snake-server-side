package org.example.rsocketdemo.dtos;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.rsocketdemo.logic.Coordinate;

import java.util.UUID;

@RequiredArgsConstructor
@Getter
public class GameInfo {
    private final UUID gameId;
    private final int dimension;
    private final Coordinate snake;
    private final Coordinate food;
}
