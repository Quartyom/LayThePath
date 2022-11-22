package com.quartyom.game_elements;

import java.lang.Math;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.quartyom.LayThePath;

public class Notification {
    public boolean is_active;
    private float center_x, center_y;
    private float notification_x, notification_y, notification_w;
    private float text_x, text_y, text_w, text_h;

    public Vector2 offset;

    public String string;

    private LayThePath game;
    private BitmapFont font;

    private TextureRegion texture;


    public Notification(LayThePath game){
        this.game = game;
        offset = new Vector2();
        texture = game.sliders_atlas.findRegion("regular_notification");
    }

    public void set_string(String string){
        if (string != this.string) {
            this.string = string;
            if (string == null) { return; }

            font = game.fontHolder.get((int)notification_w);    // шрифт для теста
            game.glyphLayout.setText(font, string);

            text_w = game.glyphLayout.width;
            text_h = game.glyphLayout.height;

            float w_ratio = notification_w / (float) Math.sqrt(text_w * text_w + text_h * text_h);  // диаметр уведомления / диагональ текста
            if (w_ratio > 1){w_ratio = 1;}

            int font_size = (int)(notification_w * w_ratio * 0.8f);

            if (font_size > 200) { font_size = 200; }

            font = game.fontHolder.get(font_size); // окончательный шрифт
            game.glyphLayout.setText(font, string);

            text_w = game.glyphLayout.width;
            text_h = game.glyphLayout.height;

            text_x = center_x - text_w / 2;
            text_y = center_y + text_h / 2;
        }
    }

    public void draw(){
        if (string != null) {
            game.batch.draw(texture, notification_x, notification_y, notification_w, notification_w);
            font.draw(game.batch, string, text_x + offset.x, text_y + offset.y);
        }
    }

    public void resize(float x, float y, float w){
        notification_w = w;
        center_x = x + notification_w / 4;
        center_y = y - notification_w / 4;
        notification_x = center_x - notification_w / 2;
        notification_y = center_y - notification_w / 2;
    }


}
