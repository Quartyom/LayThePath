package com.quartyom.game_elements;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.quartyom.LayThePath;
import com.quartyom.interfaces.QuEvent;

public class GameTopPanel {
    public final LayThePath game;

    protected TextureRegion texture;

    protected float panel_x, panel_y, panel_w, panel_h;
    protected float first_button_x, first_button_y, button_w, button_h;

    protected Button menu_button;

    protected GameTopPanel(final LayThePath game){
        this.game = game;
        texture = game.field_atlas.findRegion("top_panel");
        menu_button = new Button("menu", game, new QuEvent() {
            @Override
            public void execute() {
                game.setScreen("menu");
            }
        });
    }

    public void resize(){
        panel_x = -game.HALF_WIDTH;
        panel_h = (0.5f / 4) * game.HEIGHT;
        panel_y = game.HALF_HEIGHT - panel_h;
        panel_w = game.WIDTH;

        float button_actual_size = panel_h  / 2;

        float first_button_center_x = panel_x + panel_w - button_actual_size;
        float first_button_center_y = panel_y + panel_h / 2;

        button_actual_size *= 0.9f; // Отступ кнопки от краёв

        first_button_x = first_button_center_x - button_actual_size;
        first_button_y = first_button_center_y - button_actual_size;

        button_w = button_actual_size * 2;
        button_h = button_actual_size * 2;

        menu_button.resize(first_button_x, first_button_y, button_w, button_h);
    }

    public void draw(){
        game.batch.draw(texture, panel_x, panel_y, panel_w, panel_h);
        menu_button.draw();
    }

    public void update(){
        menu_button.update();
    }

    public float getHeight(){
        return panel_h;
    }
}
