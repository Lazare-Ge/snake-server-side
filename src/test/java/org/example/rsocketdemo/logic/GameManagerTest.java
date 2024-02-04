package org.example.rsocketdemo.logic;

import org.example.rsocketdemo.services.GameManager;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.util.function.Tuple2;

import static org.junit.jupiter.api.Assertions.*;

class GameManagerTest {


    @Test
    void testStartGame(){
        GameManager gameManager = new GameManager();
        assertNotNull(gameManager);
        Game game = GameManager.getDefaultGame();
        Flux<Tuple2<Long, GameState>> startGameFlux = game.start();
        StepVerifier.create(startGameFlux.log())
                .expectNextCount(5)
                .expectNextMatches(tickNumGameState -> {
                    GameState state = tickNumGameState.getT2();
                    return state.getFood().getX() != 0 && state.getFood().getY() != 5 && state.getSnake().size() == 2;
                })
                .expectNextCount(24)
                .verifyComplete();
    }

    @Test
    void gameManagementTest() {
        GameManager gameManager = new GameManager();
        assertNotNull(gameManager);
        Game game = GameManager.getDefaultGame();
        gameManager.addGame(game);
        assertNotNull(gameManager.getGame(game.getId()));
        System.out.println(game.state());

        StepVerifier.create(gameManager.getGameStream(Mono.just(game.getId().toString()))
                        .map(GameState::getStatus)
                        .take(6)
                )
                .expectNextCount(6)
                .verifyComplete();
    }


}