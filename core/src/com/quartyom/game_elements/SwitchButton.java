package com.quartyom.game_elements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.quartyom.LayThePath;

import java.util.ArrayList;

public class SwitchButton {
    private float button_x, button_y, button_w, button_h;

    public ArrayList<TextureRegion> textures;

    public int state;
    public boolean recently_changed = false;
    public boolean to_change_state_on_click = true;

    LayThePath game;

    Sound click_sound;

    Vector2 touch_pos;

    public SwitchButton(LayThePath game){
        this.game = game;
        touch_pos = new Vector2();

        textures = new ArrayList<TextureRegion>();

        click_sound = game.soundHolder.get("click_0");
    }

    public SwitchButton add(String path){
        textures.add(game.buttons_atlas.findRegion(path));
        return this;
    }

    public SwitchButton setSound(String name){
        click_sound = game.soundHolder.get(name);
        return this;
    }

    public void resize(float x, float y, float w, float h){
        button_x = x;
        button_y = y;
        button_w = w;
        button_h = h;
    }

    public void draw(){
        game.batch.draw(textures.get(state), button_x, button_y, button_w, button_h);
    }


    // нажал и отпустил - клик
    public void update(){
        // если нажато
        if (Gdx.input.justTouched()){
            touch_pos.x = Gdx.input.getX() - game.HALF_WIDTH;
            touch_pos.y = game.HALF_HEIGHT - Gdx.input.getY();

            // попали ли по кнопке
            if (touch_pos.x >= button_x && touch_pos.y >= button_y && touch_pos.x <= button_x + button_w && touch_pos.y <= button_y + button_h){

                if (to_change_state_on_click) {
                    state++;
                    if (state >= textures.size()) {
                        state = 0;
                    }
                }

                recently_changed = true;
                click_sound.play(game.userData.volume * 0.5f);
                return;
            }
        }
        recently_changed = false;

    }
}
