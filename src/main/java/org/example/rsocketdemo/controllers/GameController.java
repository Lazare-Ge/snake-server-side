package org.example.rsocketdemo.controllers;

import lombok.RequiredArgsConstructor;
import org.example.rsocketdemo.dtos.GameInput;
import org.example.rsocketdemo.dtos.GameInfo;
import org.example.rsocketdemo.logic.Coordinate;
import org.example.rsocketdemo.logic.Game;
import org.example.rsocketdemo.services.GameManager;
import org.example.rsocketdemo.logic.GameState;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuples;

import java.util.UUID;

@RequiredArgsConstructor
@Controller
public class GameController {

    private final GameManager gameManager;

    @MessageMapping("game.start")
    public Mono<GameInfo> startGame() {
        return Mono.fromCallable(() -> {
            Game game = GameManager.getDefaultGame();
            gameManager.addGame(game);
            UUID gameId = game.getId();
            int dimension = game.getDimension();
            GameState state = game.state();
            Coordinate snake = state.getSnake().get(0);
            Coordinate food = state.getFood();
            return  new GameInfo(gameId, dimension, snake, food);
        }).log();
    }

    @MessageMapping("game.stream")
    public Flux<GameState> streamGameState(Mono<String> gameId) {
        return gameManager.getGameStream(gameId);
    }

    @MessageMapping("game.input")
    public Mono<Void> processGameInput(Mono<GameInput> input) {
        return input
                .mapNotNull(gameInput -> Tuples.of(gameManager.getGame(gameInput.getGameId()), gameInput))
                .doOnNext(gameAndInput -> gameAndInput.getT1().changeDirection(gameAndInput.getT2().getDirection()))
                .then()
                ;
//        Game game = gameManager.getGame(input.getGameId());
//        if (game != null) {
//            game.changeDirection(input.getDirection());
//        }
    }

}
