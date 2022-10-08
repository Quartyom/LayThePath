package com.quartyom.screens.Zen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.Align;
import com.quartyom.MakeTheWay;
import com.quartyom.game_elements.Button;
import com.quartyom.interfaces.EventHandler;
import com.quartyom.game_elements.Label;
import com.quartyom.game_elements.TextField;

public class ZenSkipTab implements Screen {
    final MakeTheWay game;
    ZenScreen zenScreen;

    Label skip_label;
    TextField info_field;
    Button back_button;//, accept_button;

    public ZenSkipTab(final ZenScreen zenScreen){
        this.zenScreen = zenScreen;
        game = zenScreen.game;

        skip_label = new Label(game, zenScreen.game.locale.get("Attention"));

        info_field = new TextField(zenScreen.game, Gdx.files.internal("texts/" + game.userData.locale + "/skip_level.txt").readString());

        back_button = new Button("in_main_menu", game, new EventHandler() {
            @Override
            public void execute() {
                game.setScreen("zen");
            }
        });
        back_button.setNinePatch(6).setLabel(zenScreen.game.locale.get("Back"));

        /*accept_button = new Button("in_main_menu", game, new EventHandler() {
            @Override
            public void execute() {
                ZenSkipTab.this.zenScreen.zenBoard.current_level++;
                ZenSkipTab.this.zenScreen.zenBoard.load_level();
                game.setScreen("zen");
            }
        });
        accept_button.setNinePatch(6).setLabel("Accept");*/

    }

    @Override
    public void resize(int width, int height) {
        skip_label.resize(game.upper_button_corner_x, game.upper_button_corner_y, game.button_w, game.button_h, Align.center);

        int font_size = (int) (zenScreen.game.HEIGHT * (1.0f / 32.0f));
        info_field.resize(game.upper_button_corner_x, game.upper_button_corner_y - game.down_margin + game.button_h, game.button_w, font_size);
        // gap
        //accept_button.resize(game.upper_button_corner_x, game.upper_button_corner_y - game.down_margin * 4, game.button_w, game.button_h);
        back_button.resize(game.upper_button_corner_x, game.upper_button_corner_y - game.down_margin * 5, game.button_w, game.button_h);
    }

    @Override
    public void show() {
        Gdx.gl20.glClearColor(0, 0, 0, 1);
    }

    @Override
    public void render(float delta) {
        Gdx.gl20.glClear(Gdx.gl20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();

        skip_label.draw();
        info_field.draw();
        back_button.draw();
        //accept_button.draw();

        game.batch.end();

        back_button.update();
        //accept_button.update();
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
