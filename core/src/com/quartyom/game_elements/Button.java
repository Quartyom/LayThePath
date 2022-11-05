package com.quartyom.game_elements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.quartyom.LayThePath;
import com.quartyom.interfaces.Drawable;
import com.quartyom.interfaces.QuEvent;

public class Button implements Drawable {
    private float button_x, button_y, button_w, button_h;
    public Vector2 offset;

    public TextureRegion normal_texture, pressed_texture;
    public NinePatch normal_patch, pressed_patch;
    Sound click_sound;

    private boolean has_nine_patch = false;

    Label label;
    Hint hint;

    public InputState inputState;

    QuEvent action;
    LayThePath game;

    Vector2 touch_pos;

    public Button(LayThePath game, QuEvent action){
        this.action = action;
        this.game = game;

        inputState = InputState.UNTOUCHED;
        touch_pos = new Vector2();
        offset = new Vector2();
    }

    public Button(String name, LayThePath game, QuEvent action){
        this(game, action);
        normal_texture = game.buttons_atlas.findRegion(name + "_normal");
        pressed_texture = game.buttons_atlas.findRegion(name + "_pressed");
        click_sound = game.soundHolder.get("click_0");

    }

    public Button setNinePatch(int padding){
        normal_patch = new NinePatch(normal_texture, padding, padding, padding, padding);
        pressed_patch = new NinePatch(pressed_texture, padding, padding, padding, padding);
        has_nine_patch = true;
        return this;
    }

    public Button setLabel(String string){
        label = new Label(game, string);
        label.offset = this.offset;
        return this;
    }

    public Button changeLabel(String string){
        label = new Label(game, string);
        label.offset = this.offset;
        label.resize(button_x, button_y, button_w, button_h, Align.center);
        return this;
    }

    public Button setHint(String string){
        hint = new Hint(game);
        hint.set_string(string);
        return this;
    }

    public Button setSound(String name){
        click_sound = game.soundHolder.get(name);
        return this;
    }

    public void on_click(){
        action.execute();
    }

    public void resize(float x, float y, float w, float h){
        button_x = x;
        button_y = y;
        button_w = w;
        button_h = h;

        if (label != null){
            label.resize(x, y, w, h, Align.center);
        }

        if (hint != null){
            hint.resize((int)(game.HEIGHT * (1.0f / 32.0f)), 0, game.HALF_HEIGHT * (1 / 4.0f));
        }
    }

    public void draw(){
        if (!has_nine_patch) {
            if (inputState == InputState.UNTOUCHED) {
                game.batch.draw(normal_texture, button_x + offset.x, button_y + offset.y, button_w, button_h);
            } else if (inputState == InputState.TOUCHED) {
                game.batch.draw(pressed_texture, button_x + offset.x, button_y + offset.y, button_w, button_h);
            }
        }
        else {
            if (inputState == InputState.UNTOUCHED) {
                normal_patch.draw(game.batch, button_x + offset.x, button_y + offset.y, button_w, button_h);
            } else if (inputState == InputState.TOUCHED) {
                pressed_patch.draw(game.batch, button_x + offset.x, button_y + offset.y, button_w, button_h);
            }
        }
        if (label != null){
            label.draw();
        }
        if (hint != null){
            if (game.userData.button_hints_are_on){     // подсказки могут быть выключены
                game.drawingQueue.add(hint);
            }
        }
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
                    if (click_sound != null) { click_sound.play(game.userData.volume * 0.5f); }
                    if (hint != null) { hint.is_active = true; }
                }
            }
            else {
                inputState = InputState.UNTOUCHED;
                if (hint != null) { hint.is_active = false; }
            }
            // не попали по кнопке
        }
        // не нажато
        else {
            // но было нажато
            if (inputState == InputState.TOUCHED) {
                // палец убрали над кнопкой = клик
                if (touch_pos.x >= button_x && touch_pos.y >= button_y && touch_pos.x <= button_x + button_w && touch_pos.y <= button_y + button_h){
                    on_click();
                }
                inputState = InputState.UNTOUCHED;
                if (hint != null) { hint.is_active = false; }
            }
        }

        if (hint != null){
            hint.update();
        }

    }

}
