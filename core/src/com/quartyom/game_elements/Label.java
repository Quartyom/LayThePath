package com.quartyom.game_elements;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.quartyom.LayThePath;

// Текст размещается внутри заданной рамки, в одну строку, отцентрированный, либо к левому краю
public class Label {
    private float label_x, label_y, label_w, label_h;
    private float text_x, text_y, text_w, text_h;
    public Vector2 offset;

    private LayThePath game;
    private BitmapFont font;

    public String string;
    public String target_string;    // для фиксированного размера изменяющегося лэйбла

    public Label(LayThePath game){
        this(game, new String());
    }

    public Label(LayThePath game, String string){
        this.game = game;
        this.string = string;
        offset = new Vector2();
    }

    public void set_string(String string){
        set_string(string, Align.center);
    }

    public void set_string(String string, int halign){
        if (string != this.string){
            this.string = string;
            resize(label_x, label_y, label_w, label_h, halign);
        }
    }

    public void draw(){
        font.draw(game.batch, string, text_x + offset.x, text_y + offset.y);
    }

    public void resize(float x, float y, float w, float h, int halign){
        label_x = x;
        label_y = y;
        label_w = w;
        label_h = h;

        font = game.fontHolder.get((int)h);    // шрифт для теста

        if (target_string == null) {
            game.glyphLayout.setText(font, string);
        }
        else {
            game.glyphLayout.setText(font, target_string);
        }

        float w_ratio = w / game.glyphLayout.width;
        if (w_ratio > 1){w_ratio = 1;}

        int font_size = (int)(h * w_ratio * 0.8f);

        if (font_size > 200){font_size = 200;}  // чтобы не было проблем при отрисовке (хотя теперь и не должно)

        font = game.fontHolder.get(font_size); // окончательный шрифт
        game.glyphLayout.setText(font, string);

        text_w = game.glyphLayout.width;
        text_h = game.glyphLayout.height;

        if (halign == Align.center) {
            text_x = x + w / 2 - text_w / 2;
        }
        else {
            text_x = x;
        }
        text_y = y + h / 2 + text_h / 2;

    }

    public float get_lower_y(){
        return label_y - label_h;
    }

}
