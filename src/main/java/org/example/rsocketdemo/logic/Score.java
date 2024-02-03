package org.example.rsocketdemo.logic;

import lombok.Getter;
import lombok.Setter;

public class Score {

    @Getter
    @Setter
    private int score = 0;

    public void updateScore(){
        score += 15;
    }

}
