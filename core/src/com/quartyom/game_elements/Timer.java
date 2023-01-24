package com.quartyom.game_elements;

import com.badlogic.gdx.utils.TimeUtils;
import com.quartyom.LayThePath;
import com.quartyom.interfaces.QuEvent;

public class Timer {

    private LayThePath game;
    private QuEvent action;

    private long whenToAct;

    public Timer(LayThePath game, QuEvent action) {
        this.game = game;
        this.action = action;
    }

    public void set(long ms) {
        whenToAct = TimeUtils.millis() + ms;
    }

    public void update() {
        if (TimeUtils.millis() >= whenToAct) {
            action.execute();
        }
    }
}
