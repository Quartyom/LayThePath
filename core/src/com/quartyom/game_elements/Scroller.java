package com.quartyom.game_elements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.quartyom.LayThePath;

public class Scroller {
    private float view_x, view_y, view_w, view_h;

    private LayThePath game;

    public Vector2 value;

    private Vector2 touch_pos;
    private Vector2 prev_touch_pos;

    public InputState inputState;

    public boolean physics_on;
    private Vector2 scroll_speed;

    public Scroller(LayThePath game){
        this.game = game;

        value = new Vector2();

        touch_pos = new Vector2();
        prev_touch_pos = new Vector2();
        scroll_speed = new Vector2();

        inputState = InputState.UNTOUCHED;
    }

    public void resize_full(){
        resize(-game.HALF_WIDTH, -game.HALF_HEIGHT, game.WIDTH, game.HEIGHT);
    }

    public void resize(float view_x, float view_y, float view_w, float view_h){
        this.view_x = view_x;
        this.view_y = view_y;
        this.view_w = view_w;
        this.view_h = view_h;
    }

    public void update(){
        // если нажато
        if (game.isTouched()) {
            touch_pos.x = game.touch_pos.x;
            touch_pos.y = game.touch_pos.y;

            if (touch_pos.x > view_x && touch_pos.y > view_y && touch_pos.x < view_x + view_w && touch_pos.y < view_y + view_h){
                game.isTouchRead = true;
                if (inputState == InputState.UNTOUCHED) {   // по какой-то причине justTouched() не работает
                    inputState = InputState.JUST_TOUCHED;
                    prev_touch_pos.x = touch_pos.x;
                    prev_touch_pos.y = touch_pos.y;
                    return;
                }
                else {
                    inputState = InputState.TOUCHED;
                    scroll_speed.x = touch_pos.x - prev_touch_pos.x;    // по факту, это не скорость, а разница
                    scroll_speed.y = touch_pos.y - prev_touch_pos.y;
                    value.x += scroll_speed.x;
                    value.y += scroll_speed.y;
                    prev_touch_pos.x = touch_pos.x;
                    prev_touch_pos.y = touch_pos.y;
                }

            }

            return;
        }

        if (inputState == InputState.TOUCHED || inputState == InputState.JUST_TOUCHED){
            inputState = InputState.JUST_UNTOUCHED;

            if (physics_on) {
                float coef = 1.25f;  // чтобы скроллинг был быстрее
                scroll_speed.x = scroll_speed.x / Gdx.graphics.getDeltaTime() * coef;  // до этого момента это была не скорость, а измененение
                scroll_speed.y = scroll_speed.y / Gdx.graphics.getDeltaTime() * coef;
            }
        }
        else {
            inputState = InputState.UNTOUCHED;
        }

        if (physics_on){
            float coef = game.HEIGHT * 2;

            if (scroll_speed.x < 0){
                scroll_speed.x += coef * Gdx.graphics.getDeltaTime();
                if (scroll_speed.x > 0){
                    scroll_speed.x = 0;
                }
            }
            else if (scroll_speed.x > 0){
                scroll_speed.x -= coef * Gdx.graphics.getDeltaTime();
                if (scroll_speed.x < 0){
                    scroll_speed.x = 0;
                }
            }

            if (scroll_speed.y < 0){
                scroll_speed.y += coef * Gdx.graphics.getDeltaTime();
                if (scroll_speed.y > 0){
                    scroll_speed.y = 0;
                }
            }

            else if (scroll_speed.y > 0){
                scroll_speed.y -= coef * Gdx.graphics.getDeltaTime();
                if (scroll_speed.y < 0){
                    scroll_speed.y = 0;
                }
            }


            value.x += scroll_speed.x * Gdx.graphics.getDeltaTime();
            value.y += scroll_speed.y * Gdx.graphics.getDeltaTime();
        }


    }



}
