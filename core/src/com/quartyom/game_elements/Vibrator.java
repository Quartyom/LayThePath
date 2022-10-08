package com.quartyom.game_elements;

import com.badlogic.gdx.Gdx;
import com.quartyom.UserData;

public class Vibrator {
    UserData userData;
    public Vibrator(UserData userData){
        this.userData = userData;
    }

    public void vibrate(int milliseconds){
        if (userData.vibration_is_on){
            Gdx.input.vibrate(milliseconds);
        }
    }
}
