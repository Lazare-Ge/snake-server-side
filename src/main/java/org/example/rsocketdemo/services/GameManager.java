package org.example.rsocketdemo.services;

import org.example.rsocketdemo.logic.*;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GameManager {
    private final Map<UUID, Game> games = new ConcurrentHashMap<>();

    public static Game getDefaultGame() {
        UUID id = UUID.randomUUID();
        int dimension = 30;
        int speed = 65;
        Board board = new Board(dimension, dimension);
        Snake snake = Snake.defaultInstance();
        Direction direction = new Direction("right");
        GameStatus statusHolder = new GameStatus("initializing");
        Food food = new Food(new Coordinate(0, 5));
        return new Game(id, dimension, speed, board, snake, direction, statusHolder, food);
    }

    public Flux<GameState> getGameStream(Mono<String> gameId) {
        return gameId
                .map(UUID::fromString)
                .map(this::getGame)
                .flatMapMany(Game::start)
                .map(Tuple2::getT2);
    }


    public void addGame(Game game) {
        games.put(game.getId(), game);
    }

    public Game getGame(UUID gameId) {
        return games.get(gameId);
    }


}
