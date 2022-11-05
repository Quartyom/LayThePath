package com.quartyom.screens.Level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.Align;
import com.quartyom.LayThePath;
import com.quartyom.game_elements.Button;
import com.quartyom.game_elements.QuScreen;
import com.quartyom.interfaces.QuEvent;
import com.quartyom.game_elements.Label;
import com.quartyom.game_elements.TextField;


public class LevelSkipTab extends QuScreen {
    final LayThePath game;
    public LevelScreen levelScreen;

    Label skip_label;
    TextField info_field;
    Button back_button;//, accept_button;

    public LevelSkipTab(final LevelScreen levelScreen){
        this.levelScreen = levelScreen;
        game = levelScreen.game;

        skip_label = new Label(game, levelScreen.game.locale.get("Attention"));

        info_field = new TextField(levelScreen.game, Gdx.files.internal("texts/" + game.userData.locale + "/skip_level.txt").readString());

        back_button = new Button("in_main_menu", game, new QuEvent() {
            @Override
            public void execute() {
                game.setScreen("level");
            }
        });
        back_button.setNinePatch(6).setLabel(levelScreen.game.locale.get("Back"));

        /*accept_button = new Button("in_main_menu", game, new EventHandler() {
            @Override
            public void execute() {
                levelScreen.levelBoard.current_level++;
                levelScreen.levelBoard.userData.current_level = levelScreen.levelBoard.current_level;
                levelScreen.game.save_user_data();
                levelScreen.levelBoard.load_level();

                game.setScreen("level");
            }
        });
        accept_button.setNinePatch(6).setLabel("Accept");*/

    }

    @Override
    public void show() {
        Gdx.gl20.glClearColor(0, 0, 0, 1);
    }

    @Override
    public void render(float delta) {
        Gdx.gl20.glClear(Gdx.gl20.GL_COLOR_BUFFER_BIT);

        skip_label.draw();
        info_field.draw();
        back_button.draw();
        //accept_button.draw();

        back_button.update();
        //accept_button.update();
    }

    @Override
    public void resize(int width, int height) {
        skip_label.resize(game.upper_button_corner_x, game.upper_button_corner_y, game.button_w, game.button_h, Align.center);

        int font_size = (int) (levelScreen.game.HEIGHT * (1.0f / 32.0f));
        info_field.resize(game.upper_button_corner_x, game.upper_button_corner_y - game.down_margin + game.button_h, game.button_w, font_size);
        // gap
        //accept_button.resize(game.upper_button_corner_x, game.upper_button_corner_y - game.down_margin * 4, game.button_w, game.button_h);
        back_button.resize(game.upper_button_corner_x, game.upper_button_corner_y - game.down_margin * 5, game.button_w, game.button_h);
    }
}
