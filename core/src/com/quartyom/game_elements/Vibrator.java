package com.quartyom.game_elements;

import com.badlogic.gdx.Gdx;
import com.quartyom.LayThePath;

public class Vibrator {
    LayThePath game;

    public Vibrator(LayThePath game) {
        this.game = game;
    }

    public void vibrate(int milliseconds) {
        if (game.userData.vibration_is_on) {
            Gdx.input.vibrate(milliseconds);
        }
    }
}
