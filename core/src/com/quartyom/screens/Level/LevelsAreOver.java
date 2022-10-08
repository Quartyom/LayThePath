package com.quartyom.screens.Level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.Align;
import com.quartyom.MakeTheWay;
import com.quartyom.game_elements.Button;
import com.quartyom.interfaces.EventHandler;
import com.quartyom.game_elements.Label;
import com.quartyom.game_elements.TextField;

import java.util.Random;

public class LevelsAreOver implements Screen {
    final MakeTheWay game;
    public LevelScreen levelScreen;

    Label hint_label;
    TextField info_field;
    Button start_over_button, menu_button;

    private Random random;

    public LevelsAreOver(final LevelScreen levelScreen){
        this.levelScreen = levelScreen;
        game = levelScreen.game;

        random = new Random();

        hint_label = new Label(game, levelScreen.game.locale.get("Attention"));

        info_field = new TextField(levelScreen.game, Gdx.files.internal("texts/" + game.userData.locale + "/levels_are_over.txt").readString());

        start_over_button = new Button("in_main_menu", game, new EventHandler() {
            @Override
            public void execute() {
                levelScreen.levelBoard.current_level = 1;
                game.userData.current_level = 1;
                game.save_user_data();
                levelScreen.levelBoard.load_level();

                game.setScreen("level");
            }
        });
        start_over_button.setNinePatch(6).setLabel(levelScreen.game.locale.get("Start over"));

        menu_button = new Button("in_main_menu", game, new EventHandler() {
            @Override
            public void execute() {
                levelScreen.game.setScreen("menu");
            }
        });
        menu_button.setNinePatch(6).setLabel(levelScreen.game.locale.get("Go to Menu"));

    }

    @Override
    public void show() {
        Gdx.gl20.glClearColor(0, 0, 0, 1);
    }

    @Override
    public void render(float delta) {
        Gdx.gl20.glClear(Gdx.gl20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();

        hint_label.draw();
        info_field.draw();
        start_over_button.draw();
        menu_button.draw();

        game.batch.end();

        start_over_button.update();
        menu_button.update();
    }

    @Override
    public void resize(int width, int height) {
        hint_label.resize(game.upper_button_corner_x, game.upper_button_corner_y, game.button_w, game.button_h, Align.center);

        int font_size = (int) (levelScreen.game.HEIGHT * (1.0f / 32.0f));
        info_field.resize(game.upper_button_corner_x, game.upper_button_corner_y - game.down_margin + game.button_h, game.button_w, font_size);
        // gap
        start_over_button.resize(game.upper_button_corner_x, game.upper_button_corner_y - game.down_margin * 4, game.button_w, game.button_h);
        menu_button.resize(game.upper_button_corner_x, game.upper_button_corner_y - game.down_margin * 5, game.button_w, game.button_h);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
