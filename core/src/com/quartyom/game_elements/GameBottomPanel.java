package com.quartyom.game_elements;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.quartyom.LayThePath;

public class GameBottomPanel {
    protected final LayThePath game;

    protected  TextureRegion texture;

    protected float panel_x, panel_y, panel_w, panel_h;
    protected float first_button_x, first_button_y, button_w, button_h;

    protected GameBottomPanel(final LayThePath game){
        this.game = game;

        texture = game.field_atlas.findRegion("bottom_panel");
    }

    public void resize(){
        panel_x = -game.HALF_WIDTH;
        panel_y = -game.HALF_HEIGHT;
        panel_w = game.WIDTH;
        panel_h = (0.5f / 4) * game.HEIGHT;

        float first_button_center_x = panel_x + panel_w / 2 / 4; // тк 4 кнопки
        float first_button_center_y = panel_y + panel_h / 2;

        float button_actual_size = panel_h / 2;
        //System.out.println(button_actual_size);
        if (panel_w / 2 / 4 < button_actual_size){
            button_actual_size = panel_w / 2 / 4;
        }
        button_actual_size *= 0.9f; // Отступ кнопки от краёв

        first_button_x = first_button_center_x - button_actual_size;
        first_button_y = first_button_center_y - button_actual_size;

        button_w = button_actual_size * 2;
        button_h = button_actual_size * 2;
    }

    public void draw(){
        game.batch.draw(texture, panel_x, panel_y, panel_w, panel_h);
    }
}
