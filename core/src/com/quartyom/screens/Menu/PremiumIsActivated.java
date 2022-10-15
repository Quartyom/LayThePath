package com.quartyom.screens.Menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.Align;
import com.quartyom.LayThePath;
import com.quartyom.game_elements.Button;
import com.quartyom.game_elements.Label;
import com.quartyom.game_elements.QuScreen;
import com.quartyom.game_elements.TextField;
import com.quartyom.interfaces.QuEvent;

public class PremiumIsActivated extends QuScreen {
    final LayThePath game;

    Label zen_label;
    TextField info_field;
    Button deactivate_button, back_button;


    public PremiumIsActivated(final LayThePath game){
        this.game = game;

        zen_label = new Label(game, game.locale.get("Attention"));

        info_field = new TextField(game, game.locale.get("Premium is activated already."));

        deactivate_button = new Button("in_main_menu", game, new QuEvent() {
            @Override
            public void execute() {
                game.userData.premium_is_on = false;
                game.save_user_data();
                game.setScreen("menu_settings");
            }
        });
        deactivate_button.setNinePatch(6).setLabel(game.locale.get("Deactivate"));

        back_button = new Button("in_main_menu", game, new QuEvent() {
            @Override
            public void execute() {
                game.setScreen("menu_settings");
            }
        });
        back_button.setNinePatch(6).setLabel(game.locale.get("Back"));

    }

    @Override
    public void resize(int width, int height) {
        zen_label.resize(game.upper_button_corner_x, game.upper_button_corner_y, game.button_w, game.button_h, Align.center);

        int font_size = (int) (game.HEIGHT * (1.0f / 32.0f));
        info_field.resize(game.upper_button_corner_x, game.upper_button_corner_y - game.down_margin + game.button_h, game.button_w, font_size);
        // gap
        deactivate_button.resize(game.upper_button_corner_x, game.upper_button_corner_y - game.down_margin * 4, game.button_w, game.button_h);
        back_button.resize(game.upper_button_corner_x, game.upper_button_corner_y - game.down_margin * 5, game.button_w, game.button_h);
    }

    @Override
    public void render(float delta) {
        Gdx.gl20.glClear(Gdx.gl20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();

        zen_label.draw();
        info_field.draw();
        deactivate_button.draw();
        back_button.draw();

        game.batch.end();

        deactivate_button.update();
        back_button.update();

        if (game.is_back_button_pressed){
            game.is_back_button_pressed = false;
            game.setScreen("menu_settings");
        }
    }

}
