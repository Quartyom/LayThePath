package com.quartyom.game_elements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.quartyom.MakeTheWay;

// Хитбокс слайдэра задаётся, параметры элементов слайдэра вычисляются, чтобы поместиться в хитбокс
// Значение слайдэра изменяется в пределах от 0 до 1
public class Slider {
    private float slider_x, slider_y, slider_w, slider_h;   // хитбокс слайдэра
    private float scale_x, scale_y, scale_w, scale_h;       // размеры шкалы

    public float value;

    private TextureRegion normal_texture, pressed_texture, line_texture;
    private NinePatch line_patch;

    public InputState inputState;
    MakeTheWay game;

    Vector2 touch_pos;

    public Slider(MakeTheWay game){
        this.game = game;

        inputState = InputState.UNTOUCHED;
        touch_pos = new Vector2();
    }

    public Slider(String name, MakeTheWay game){
        this(game);

        normal_texture = game.sliders_atlas.findRegion( name + "_untouched");
        pressed_texture =  game.sliders_atlas.findRegion(name + "_touched");
        line_texture =  game.sliders_atlas.findRegion(name + "_scale");


        line_patch = new NinePatch(line_texture, 31, 31, 0, 0); // пока так

    }

    public void resize(float x, float y, float w, float h){
        slider_x = x;
        slider_y = y;
        slider_w = w;
        slider_h = h;

        scale_h = slider_h / 3;
        scale_w = slider_w - slider_h; // 2 раза по половине (слева и справа)

        scale_x = slider_x + slider_h / 2;
        scale_y = slider_y + slider_h / 2 - scale_h / 2;

    }

    public void draw(){

        line_patch.draw(game.batch, scale_x, scale_y, scale_w, scale_h);

        if (inputState == inputState.UNTOUCHED) {
            game.batch.draw(normal_texture, slider_x + scale_w * value, slider_y, slider_h, slider_h) ;
        }
        else if (inputState == inputState.TOUCHED) {
            game.batch.draw(pressed_texture, slider_x + scale_w * value, slider_y, slider_h, slider_h);
        }


    }
    public void update(){
        // если нажато
        if (Gdx.input.isTouched()){
            touch_pos.x = Gdx.input.getX() - game.HALF_WIDTH;
            touch_pos.y = game.HALF_HEIGHT - Gdx.input.getY();

            if (Gdx.input.justTouched()){
                // попали ли
                if (touch_pos.x >= slider_x && touch_pos.y >= slider_y && touch_pos.x <= slider_x + slider_w && touch_pos.y <= slider_y + slider_h){
                    inputState = InputState.TOUCHED;
                }
                else {
                    inputState = InputState.UNTOUCHED;
                }
                // не попали по кнопке
            }
            if (inputState == InputState.TOUCHED){
                value = (touch_pos.x - scale_x) / scale_w;
                if (value < 0){
                    value = 0;
                }
                else if (value > 1){
                    value = 1;
                }
            }
        }
        else {
            inputState = InputState.UNTOUCHED;
        }
    }
}
