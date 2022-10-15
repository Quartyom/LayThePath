package com.quartyom.screens.Menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.quartyom.LayThePath;
import com.quartyom.game_elements.Button;
import com.quartyom.game_elements.QuScreen;
import com.quartyom.interfaces.QuEvent;
import com.quartyom.game_elements.Label;

public class InfoTab extends QuScreen {
    final LayThePath game;

    Label info_label;
    Button how_to_play_button, about_button, stats_button, /*bug_button,*/ back_button;

    public InfoTab(final LayThePath game){
        this.game = game;

        info_label = new Label(game, game.locale.get("Info"));

        how_to_play_button = new Button("in_main_menu", game, new QuEvent() {
            @Override
            public void execute() {
                game.setScreen("menu_how_to_play");
            }
        });
        how_to_play_button.setNinePatch(6).setLabel(game.locale.get("How to play"));

        about_button = new Button("in_main_menu", game, new QuEvent() {
            @Override
            public void execute() {
                game.setScreen("menu_about");
            }
        });
        about_button.setNinePatch(6).setLabel(game.locale.get("About"));

        stats_button = new Button("in_main_menu", game, new QuEvent() {
            @Override
            public void execute() {
                game.setScreen("menu_stats");
            }
        });
        stats_button.setNinePatch(6).setLabel(game.locale.get("Stats"));

        back_button = new Button("in_main_menu", game, new QuEvent() {
            @Override
            public void execute() {
                game.setScreen("menu");
            }
        });
        back_button.setNinePatch(6).setLabel(game.locale.get("Back"));

    }

    @Override
    public void resize(int width, int height) {
        info_label.resize(game.upper_button_corner_x, game.upper_button_corner_y, game.button_w, game.button_h, 1);
        how_to_play_button.resize(game.upper_button_corner_x, game.upper_button_corner_y - game.down_margin, game.button_w, game.button_h);
        about_button.resize(game.upper_button_corner_x, game.upper_button_corner_y - game.down_margin * 2, game.button_w, game.button_h);
        stats_button.resize(game.upper_button_corner_x, game.upper_button_corner_y - game.down_margin * 3, game.button_w, game.button_h);
        // gap
        back_button.resize(game.upper_button_corner_x, game.upper_button_corner_y - game.down_margin * 5, game.button_w, game.button_h);
    }

    @Override
    public void render(float delta) {
        Gdx.gl20.glClear(Gdx.gl20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();

        info_label.draw();
        how_to_play_button.draw();
        about_button.draw();
        stats_button.draw();
        back_button.draw();

        game.batch.end();

        how_to_play_button.update();
        about_button.update();
        stats_button.update();
        back_button.update();

        if (game.is_back_button_pressed){
            game.is_back_button_pressed = false;
            game.setScreen("menu");
        }
    }

}
