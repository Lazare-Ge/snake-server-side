package org.example.rsocketdemo.dtos;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class GameInput {
    @Getter
    private final String direction;
    @Getter
    private final UUID gameId;
}
