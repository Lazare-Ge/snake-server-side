package org.example.rsocketdemo.controllers;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.rsocketdemo.dtos.*;
import org.example.rsocketdemo.logic.GameStateUpdate;
import org.example.rsocketdemo.service.SnakeService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Getter
@Controller
@Slf4j
@RequiredArgsConstructor
public class RsocketController {

    private final SnakeService snakeService;


    @MessageMapping("change.direction")
    public Mono<Void> changeDirection(Mono<DirectionFire> directionMono) {
        return directionMono
                .doOnNext(direction -> {
                    snakeService.changeDirection(direction.getDirection());
                })
                .log()
                .then()
                ;
    }

    @MessageMapping("stream.game")
    public Flux<GameStateUpdate> streamGame(){
        snakeService.initNewGame();
        snakeService.startGame();
        return snakeService.streamGameUpdates().doOnComplete(() -> System.out.println("Sub Completed!"));
    }


}











