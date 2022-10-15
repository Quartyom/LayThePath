package com.quartyom.game_elements;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.quartyom.LayThePath;

// Большой текст в несколько строк, заданный шрифт и ширина, возвращается размер объекта
public class TextField {

    private float text_x, text_y, text_w, text_h;
    public Vector2 offset;

    private int font_size;

    private LayThePath game;
    private BitmapFont font;

    public String string;

    public TextField(LayThePath game, String string){
        this.game = game;
        this.string = string;
        offset = new Vector2();
    }

    public void resize(float x, float y, float w, int font_size){
        this.font_size = font_size;

        text_w = w;

        font = game.fontHolder.get(font_size);
        game.glyphLayout.setText(font, string, Color.WHITE, text_w, Align.left, true);
        text_h = game.glyphLayout.height;

        text_x = x;
        text_y = y;
    }

    public float get_height(){
        return text_h;
    }
    public float get_lower_y() { return text_y - text_h;}

    public void draw(){
        font.draw(game.batch, string, text_x + offset.x, text_y + offset.y, text_w, Align.left, true);
    }

}
