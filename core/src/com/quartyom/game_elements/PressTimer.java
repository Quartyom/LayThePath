package com.quartyom.game_elements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.quartyom.LayThePath;

// двойной тап нажал отпустил через 100мс нажал через 100мс
public class PressTimer {
    private float button_x, button_y, button_w, button_h;
    public Vector2 offset;

    public InputState inputState;

    LayThePath game;

    Vector2 touch_pos, prev_touch_pos;

    private long when_just_touched, when_just_untouched;
    private boolean double_tap;

    public PressTimer(LayThePath game){
        this.game = game;

        inputState = InputState.UNTOUCHED;
        touch_pos = new Vector2();
        prev_touch_pos = new Vector2();
        offset = new Vector2();
    }

    public void resize(float x, float y, float w, float h){
        button_x = x;
        button_y = y;
        button_w = w;
        button_h = h;
    }

    public void reset(){
        inputState = InputState.UNTOUCHED;
        when_just_touched = 0;
        when_just_untouched = 0;
    }

    // нажал и отпустил - клик
    public void update(){
        // если нажато
        if (Gdx.input.isTouched()){
            touch_pos.x = Gdx.input.getX() - game.HALF_WIDTH - offset.x;
            touch_pos.y = game.HALF_HEIGHT - Gdx.input.getY() - offset.y;

            // попали ли по кнопке
            if (touch_pos.x >= button_x && touch_pos.y >= button_y && touch_pos.x <= button_x + button_w && touch_pos.y <= button_y + button_h){
                if (Gdx.input.justTouched()) {
                    inputState = InputState.TOUCHED;

                    long current_time = TimeUtils.millis();

                    // двойное ли нажатие?
                    // куча магических чисел
                    if (current_time - when_just_untouched <= 200 && when_just_untouched - when_just_touched <= 200){
                        if (touch_pos.dst(prev_touch_pos) <= game.WIDTH / 8) {
                            double_tap = true;
                        }
                    }
                    prev_touch_pos = new Vector2(touch_pos);
                    when_just_touched = TimeUtils.millis();

                }
            }
            else {
                inputState = InputState.UNTOUCHED;
            }
            // не попали по кнопке
        }
        // не нажато
        else {
            // но было нажато
            if (inputState == InputState.TOUCHED) {
                // палец убрали над кнопкой = клик
                if (touch_pos.x >= button_x && touch_pos.y >= button_y && touch_pos.x <= button_x + button_w && touch_pos.y <= button_y + button_h){
                    when_just_untouched = TimeUtils.millis();
                }
                inputState = InputState.UNTOUCHED;

            }
        }
    }

    public boolean handle_double_tap(){
        boolean result = double_tap;
        double_tap = false;
        return result;
    }
}
