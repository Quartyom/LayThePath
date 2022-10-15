package com.quartyom.game_elements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.quartyom.LayThePath;
import com.quartyom.interfaces.Drawable;

// произвольный текст, максимальный размер половина ширины, половина высоты игры, позиция зависит от курсора
public class Hint implements Drawable {

    public boolean is_active;
    private float hint_x, hint_y, hint_w, hint_h;
    private float text_x, text_y, text_w, text_h;

    private int font_size;

    private LayThePath game;
    private BitmapFont font;

    private TextureRegion texture;

    private String string;

    Vector2 touch_pos;


    public Hint(LayThePath game){
        this.game = game;

        touch_pos = new Vector2();

        texture = game.field_atlas.findRegion("light_square");
    }

    public void set_string(String string){
        this.string = string;
    }

    public void resize(int font_size, float margin_x, float margin_y){
        this.font_size = font_size;

        font = game.fontHolder.get(font_size);

        game.glyphLayout.setText(font, string, Color.WHITE, game.HALF_WIDTH, Align.left, true);

        text_w = game.glyphLayout.width;
        text_h = game.glyphLayout.height;

        text_x = -text_w / 2 + margin_x;
        text_y = text_h + margin_y;

        hint_w = text_w + font_size;
        hint_h = text_h + font_size;
    }


    public void draw() {
        if (!is_active){return;}

        // ЭТО КОНЕЧНО ПЛОХО, НО ЛУЧШЕ Я ПОКА НЕ ПРИДУМАЛ
        float resulting_text_x = touch_pos.x + text_x;
        if (resulting_text_x - font_size/2.0f < -game.HALF_WIDTH){ resulting_text_x = -game.HALF_WIDTH + font_size/2.0f; }
        else if (resulting_text_x + text_w + font_size/2.0f > game.HALF_WIDTH){ resulting_text_x = game.HALF_WIDTH - text_w - font_size/2.0f;}

        float resulting_hint_y = touch_pos.y + text_y;
        if (resulting_hint_y - text_h - font_size/2.0f < -game.HALF_HEIGHT){ resulting_hint_y += game.HALF_HEIGHT; }
        else if (resulting_hint_y + font_size/2.0f > game.HALF_HEIGHT){ resulting_hint_y -= game.HALF_HEIGHT;}

        game.batch.draw(texture, resulting_text_x - font_size/2.0f, resulting_hint_y - text_h - font_size/2.0f, hint_w, hint_h);
        font.draw(game.batch, string, resulting_text_x, resulting_hint_y, text_w, Align.left, true);
    }

    public void update(){
        if (!is_active){return;}

        // если нажато
        if (Gdx.input.isTouched()) {
            touch_pos.x = Gdx.input.getX() - game.HALF_WIDTH;
            touch_pos.y = game.HALF_HEIGHT - Gdx.input.getY();
        }
    }

}
