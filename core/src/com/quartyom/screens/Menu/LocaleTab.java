package com.quartyom.screens.Menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.Align;
import com.quartyom.MakeTheWay;
import com.quartyom.game_elements.Button;
import com.quartyom.game_elements.InputState;
import com.quartyom.game_elements.Label;
import com.quartyom.game_elements.Scroller;
import com.quartyom.game_elements.TextField;
import com.quartyom.interfaces.EventHandler;

public class LocaleTab implements Screen {
    final MakeTheWay game;

    Button english_button, spanish_button, german_button, russian_button;
    Scroller scroller;

    public LocaleTab(final MakeTheWay game){
        this.game = game;

        english_button = new Button("english", game, new EventHandler() {
            @Override
            public void execute() {
                game.set_locale("en");
                game.setScreen("menu_settings");
            }
        });

        spanish_button = new Button("spanish", game, new EventHandler() {
            @Override
            public void execute() {
                game.set_locale("esp");
                game.setScreen("menu_settings");
            }
        });

        german_button = new Button("german", game, new EventHandler() {
            @Override
            public void execute() {
                game.set_locale("ger");
                game.setScreen("menu_settings");
            }
        });

        russian_button = new Button("russian", game, new EventHandler() {
            @Override
            public void execute() {
                game.set_locale("ru");
                game.setScreen("menu_settings");
            }
        });


        scroller = new Scroller(game);
        scroller.physics_on = true;

    }

    public void update(){
        english_button.update();
        spanish_button.update();
        german_button.update();
        russian_button.update();

        if (english_button.inputState == InputState.TOUCHED || spanish_button.inputState == InputState.TOUCHED || german_button.inputState == InputState.TOUCHED || russian_button.inputState == InputState.TOUCHED){
            scroller.inputState = InputState.UNTOUCHED;
            return;
        }

        scroller.update();

        if (scroller.value.y < 0){ scroller.value.y = 0; }
        else if (scroller.value.y > game.HALF_HEIGHT){
            scroller.value.y = game.HALF_HEIGHT;
        }

        english_button.offset.y = scroller.value.y;
        spanish_button.offset.y = scroller.value.y;
        german_button.offset.y = scroller.value.y;
        russian_button.offset.y = scroller.value.y;

    }

    @Override
    public void resize(int width, int height) {
        english_button.resize(game.upper_button_corner_x, game.upper_button_corner_y - game.down_margin, game.button_h, game.button_h);

        float theme_button_corner_x = game.upper_button_corner_x + game.button_w - game.button_h;
        spanish_button.resize(theme_button_corner_x, game.upper_button_corner_y - game.down_margin, game.button_h, game.button_h);

        german_button.resize(game.upper_button_corner_x, game.upper_button_corner_y - game.down_margin * 2, game.button_h, game.button_h);
        russian_button.resize(theme_button_corner_x, game.upper_button_corner_y - game.down_margin * 2, game.button_h, game.button_h);

        scroller.resize_full();
    }

    @Override
    public void show() {
        Gdx.gl20.glClearColor(0, 0, 0, 1);
    }

    @Override
    public void render(float delta) {
        Gdx.gl20.glClear(Gdx.gl20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();

        english_button.draw();
        spanish_button.draw();
        german_button.draw();
        russian_button.draw();

        game.batch.end();

        update();

        if (game.is_back_button_pressed){
            game.is_back_button_pressed = false;
            game.setScreen("menu_info");
        }
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
