package org.example.rsocketdemo.logic;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.time.Duration;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class Game {
    @Getter
    private final UUID id;
    @Getter
    private final int dimension;
    @Getter
    private final int speed;

    private final Board board;
    private final Snake snake;
    private final Direction direction;
    private final GameStatus status;
    private final Food food;
    public GameState state(){
        return new GameState(new LinkedList<>(snake.getSnake()), new Coordinate(food.getFoodCoordinates().getX(), food.getFoodCoordinates().getY()), status.getStatus());
    }
    public void changeDirection(String direction){
        this.direction.setDirection(direction);
    }
    public Flux<Tuple2<Long, GameState>> start() {
        return Mono.fromRunnable(() -> {
                    status.setStatus("running");
                }).thenMany(Flux.interval(Duration.ofMillis(getSpeed())))
                .flatMap(tickNum -> Mono.just(tickNum).zipWith(Mono.fromCallable(this::tick)))
                .takeUntil(tickNumGameState -> tickNumGameState.getT2().getStatus().equals("ended")) ;
    }
    private GameState tick(){
        Coordinate destination = snake.head().next(direction.getDirection());
        if(board.coordinateOut(destination) || snake.collides(destination)){
            status.setStatus("ended");
            return state();
        }
        if(snake.head().equals(food.getFoodCoordinates())){
            snake.eat();
            List<Coordinate> emptyCoordinates = emptyCoordinates();
            food.spawnRandom(emptyCoordinates);
        }
        snake.move(destination);
        return state();
    }

    private List<Coordinate> emptyCoordinates(){
        return board.getBoardAsCoordinateStream()
                .filter(coordinate -> !snake.getSnake().contains(coordinate))
                .toList();
    }

}
