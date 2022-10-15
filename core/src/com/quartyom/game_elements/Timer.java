package com.quartyom.game_elements;

import com.badlogic.gdx.utils.TimeUtils;
import com.quartyom.LayThePath;
import com.quartyom.interfaces.QuEvent;

public class Timer {

    private LayThePath game;
    private QuEvent action;

    private long when_to_act;

    public Timer(LayThePath game, QuEvent action){
        this.game = game;
        this.action = action;

    }

    public void set(long ms){
        when_to_act = TimeUtils.millis() + ms;
    }

    public void update(){
        if (TimeUtils.millis() >= when_to_act){
            action.execute();
        }
    }
}
