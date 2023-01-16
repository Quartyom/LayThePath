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
    private float hint_half_w, hint_half_h;
    private float text_x, text_y, text_w, text_h;
    private float text_half_w, text_half_h;

    private float margin_x, margin_y;

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
        font = game.fontHolder.get(font_size, FontType.LOCALIZED_WITH_LATIN);

        game.glyphLayout.setText(font, string, Color.WHITE, game.HALF_WIDTH, Align.left, true);

        text_w = game.glyphLayout.width;
        text_h = game.glyphLayout.height;
        text_half_w = text_w / 2;
        text_half_h = text_h / 2;

        this.margin_x = margin_x;
        this.margin_y = margin_y;

        hint_w = text_w + font_size;
        hint_h = text_h + font_size;
        hint_half_w = hint_w / 2;
        hint_half_h = hint_h / 2;
    }

    public void draw() {
        if (!is_active){return;}

        // делаем hint так, чтобы не выходили за пределы экрана
        if (touch_pos.x - hint_half_w < -game.HALF_WIDTH){
            touch_pos.x = -game.HALF_WIDTH + hint_half_w;
        }
        else if (touch_pos.x + hint_half_w > game.HALF_WIDTH){
            touch_pos.x = game.HALF_WIDTH - hint_half_w;
        }

        if (touch_pos.y - hint_half_h < -game.HALF_HEIGHT){
            touch_pos.y = -game.HALF_HEIGHT + hint_half_h;
        }
        else if (touch_pos.y + hint_half_h > game.HALF_HEIGHT){
            touch_pos.y = game.HALF_HEIGHT - hint_half_h;
        }

        hint_x = touch_pos.x - hint_half_w;
        hint_y = touch_pos.y - hint_half_h;

        text_x = touch_pos.x - text_half_w;
        text_y = touch_pos.y + text_half_h;

        // отрисовка
        game.batch.draw(texture, hint_x, hint_y, hint_w, hint_h);
        font.draw(game.batch, string, text_x, text_y, text_w, Align.left, true);

    }

    public void update(){
        if (!is_active){return;}

        // если нажато
        if (game.isTouched) {
            touch_pos.x = game.touch_pos.x + margin_x;
            touch_pos.y = game.touch_pos.y + margin_y;
        }
    }

}
