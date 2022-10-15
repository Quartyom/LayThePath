package com.quartyom.screens.Menu;

import com.badlogic.gdx.Gdx;
import com.quartyom.LayThePath;
import com.quartyom.game_elements.Button;
import com.quartyom.game_elements.QuScreen;
import com.quartyom.interfaces.QuEvent;
import com.quartyom.game_elements.Label;
import com.quartyom.game_elements.TextField;

public class StatsTab extends QuScreen {
    boolean is_active = false;

    final LayThePath game;

    Button back_button;
    Label how_to_play_label;
    TextField information_field;

    public StatsTab(final LayThePath game){
        this.game = game;

        how_to_play_label = new Label(game, game.locale.get("Stats"));

        information_field = new TextField(game, new String());

        back_button = new Button("in_main_menu", game, new QuEvent() {
            @Override
            public void execute() {
                game.setScreen("menu_info");
            }
        });
        back_button.setNinePatch(6).setLabel(game.locale.get("Back"));

    }

    @Override
    public void resize(int width, int height) {
        how_to_play_label.resize(game.upper_button_corner_x, game.upper_button_corner_y, game.button_w, game.button_h, 1);

        int font_size = (int) (game.HEIGHT * (1.0f / 32.0f));
        information_field.resize(game.upper_button_corner_x, game.upper_button_corner_y + game.button_h - game.down_margin, game.button_w, font_size);

        back_button.resize(game.upper_button_corner_x, game.upper_button_corner_y - game.down_margin * 5, game.button_w, game.button_h);
    }

    @Override
    public void show() {
        Gdx.gl20.glClearColor(0, 0, 0, 1);

        is_active = true;
        game.userData.stats_views++;
        game.save_user_data();
        information_field.string = String.format(Gdx.files.internal("texts/" + game.userData.locale + "/stats.txt").readString(), game.userData.launches, game.userData.zen_levels_passed, game.userData.stats_views);
    }

    @Override
    public void render(float delta) {
        Gdx.gl20.glClear(Gdx.gl20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();

        how_to_play_label.draw();
        information_field.draw();
        back_button.draw();

        game.batch.end();

        back_button.update();

        if (game.is_back_button_pressed){
            game.is_back_button_pressed = false;
            game.setScreen("menu_info");
        }
    }

}
