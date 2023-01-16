package com.quartyom.game_elements;

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

    private Label label;
    private Hint hint;
    private Notification notification;

    public InputState inputState;

    QuEvent action;
    LayThePath game;

    Vector2 touch_pos, initial_touch_pos;

    public Button(LayThePath game, QuEvent action){
        this.action = action;
        this.game = game;

        inputState = InputState.UNTOUCHED;
        touch_pos = new Vector2();
        initial_touch_pos = new Vector2();
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
        return setLabel(string, FontType.LOCALIZED_LIGHT);
    }

    public Button setLabel(String string, FontType fontType){
        if (label == null) {
            label = new Label(game, string);
            label.offset = this.offset;
            label.fontType = fontType;
        }
        else {
            label.set_string(string);
        }
        return this;
    }

    public Button setHint(String string){
        hint = new Hint(game);
        hint.set_string(string);
        return this;
    }

    public Button addNotification(){
        notification = new Notification(game);
        notification.offset = this.offset;
        return this;
    }

    public Button setNotification(String string){
        notification.set_string(string);
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

        if (notification != null){
            notification.resize(button_x, button_y + button_h, button_w / 3);
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
        if (notification != null){
            notification.draw();
        }
    }


    // нажал и отпустил - клик
    public void update(){
        // если нажато
        if (game.isTouched()){
            touch_pos.x = game.touch_pos.x - offset.x;
            touch_pos.y = game.touch_pos.y - offset.y;

            // попали ли по кнопке
            if (touch_pos.x > button_x && touch_pos.y > button_y && touch_pos.x < button_x + button_w && touch_pos.y < button_y + button_h){
                if (game.inputState == InputState.JUST_TOUCHED) {
                    inputState = InputState.TOUCHED;
                    initial_touch_pos.x = game.touch_pos.x;
                    initial_touch_pos.y = game.touch_pos.y;
                    if (click_sound != null) { click_sound.play(game.userData.volume * 0.5f); }
                    if (hint != null) { hint.is_active = true; }
                }
                else if (game.touch_pos.dst(initial_touch_pos) > button_h * 0.5f){
                    inputState = InputState.UNTOUCHED;
                    if (hint != null) { hint.is_active = false; }
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
                if (touch_pos.x > button_x && touch_pos.y > button_y && touch_pos.x < button_x + button_w && touch_pos.y < button_y + button_h){
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
