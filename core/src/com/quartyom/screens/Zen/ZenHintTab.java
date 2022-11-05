package com.quartyom.screens.Zen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.Align;
import com.quartyom.LayThePath;
import com.quartyom.game_elements.Button;
import com.quartyom.game_elements.QuScreen;
import com.quartyom.interfaces.QuEvent;
import com.quartyom.game_elements.Label;
import com.quartyom.game_elements.TextField;

import java.util.Random;

public class ZenHintTab extends QuScreen {
    final LayThePath game;

    public ZenScreen zenScreen;

    Label hint_label;
    TextField info_field;
    Button back_button;//, accept_button;

    private Random random;

    public ZenHintTab(final ZenScreen zenScreen){
        this.zenScreen = zenScreen;
        game = zenScreen.game;

        random = new Random();

        hint_label = new Label(game, zenScreen.game.locale.get("Attention"));

        info_field = new TextField(zenScreen.game, Gdx.files.internal("texts/" + game.userData.locale + "/hints_are_over.txt").readString());

        back_button = new Button("in_main_menu", game, new QuEvent() {
            @Override
            public void execute() {
                game.setScreen("zen");
            }
        });
        back_button.setNinePatch(6).setLabel(zenScreen.game.locale.get("Back"));

        /*accept_button = new Button("in_main_menu", game, new EventHandler() {
            @Override
            public void execute() {
                game.userData.hints_amount += random.nextInt(3) + 1;
                game.setScreen("zen");
            }
        });
        accept_button.setNinePatch(6).setLabel("Accept");*/

    }

    @Override
    public void resize(int width, int height) {
        hint_label.resize(game.upper_button_corner_x, game.upper_button_corner_y, game.button_w, game.button_h, Align.center);

        int font_size = (int) (zenScreen.game.HEIGHT * (1.0f / 32.0f));
        info_field.resize(game.upper_button_corner_x, game.upper_button_corner_y - game.down_margin + game.button_h, game.button_w, font_size);
        // gap
        //accept_button.resize(game.upper_button_corner_x, game.upper_button_corner_y - game.down_margin * 4, game.button_w, game.button_h);
        back_button.resize(game.upper_button_corner_x, game.upper_button_corner_y - game.down_margin * 5, game.button_w, game.button_h);
    }

    @Override
    public void render(float delta) {
        Gdx.gl20.glClear(Gdx.gl20.GL_COLOR_BUFFER_BIT);

        hint_label.draw();
        info_field.draw();
        back_button.draw();

        back_button.update();
        //accept_button.update();
    }

}
