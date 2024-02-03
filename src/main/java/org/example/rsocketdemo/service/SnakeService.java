package org.example.rsocketdemo.service;

import org.example.rsocketdemo.logic.Game;
import org.example.rsocketdemo.logic.GameStateUpdate;
import org.example.rsocketdemo.logic.GameStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;


@Service
public class SnakeService {
    private Game game;
    public void initNewGame(){
        this.game = Game.getDefaultGame();
    }

    public void startGame(){
        game.initGame();
        Flux.interval(Duration.ofMillis(50))
                .flatMap(aLong -> Mono.fromRunnable(() -> {
                    game.tick();
                    if(game.getStatus() == GameStatus.ENDED)
                        throw new RuntimeException();
                }))
                .onErrorComplete()
                .subscribe()
        ;
    }

    public Flux<GameStateUpdate> streamGameUpdates(){
        return Flux.interval(Duration.ofMillis(50))
                .flatMap(aLong -> Mono.fromCallable(() -> game.getState()))
                .doOnNext(gameStateUpdate -> {
                    if(game.getStatus() == GameStatus.ENDED){
                        throw new RuntimeException();
                    }
                })
                .onErrorComplete();
    }

    public void changeDirection(String direction){
        game.changeDirection(direction);
    }


}
