package com.quartyom.screens.Menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.Align;
import com.quartyom.MakeTheWay;
import com.quartyom.game_elements.Button;
import com.quartyom.interfaces.EventHandler;
import com.quartyom.game_elements.InputState;
import com.quartyom.game_elements.Label;
import com.quartyom.game_elements.Scroller;
import com.quartyom.game_elements.TextField;

public class HowCanIHelpTab implements Screen {
    final MakeTheWay game;

    Button bitcoin_button, ethereum_button, back_button;
    Label about_label;
    TextField information_field;
    Scroller scroller;

    public HowCanIHelpTab(final MakeTheWay game){
        this.game = game;

        about_label = new Label(game, game.locale.get("How can I help"));

        information_field = new TextField(game, Gdx.files.internal("texts/" + game.userData.locale + "/how_can_i_help.txt").readString());

        bitcoin_button = new Button("bitcoin", game, new EventHandler() {
            @Override
            public void execute() {
                Gdx.app.getClipboard().setContents("bc1qekxx6tgmk6hh8rqhugn20wvngkrgl5sw2rx9mu");
            }
        });
        bitcoin_button.setSound("click_1").setHint(game.locale.get("copy address to clipboard"));

        ethereum_button = new Button("ethereum", game, new EventHandler() {
            @Override
            public void execute() {
                Gdx.app.getClipboard().setContents("0xF639A0c69E1E3FB3Eaf0092988404Ec99214E097");
            }
        });
        ethereum_button.setSound("click_1").setHint(game.locale.get("copy address to clipboard"));

        back_button = new Button("in_main_menu", game, new EventHandler() {
            @Override
            public void execute() {
                game.setScreen("menu_about");
            }
        });
        back_button.setNinePatch(6).setLabel(game.locale.get("Back"));

        scroller = new Scroller(game);
        scroller.physics_on = true;

    }

    public void update(){
        back_button.update();

        if (back_button.inputState == InputState.UNTOUCHED) {
            bitcoin_button.update();
            ethereum_button.update();
        }

        if (back_button.inputState == InputState.TOUCHED || bitcoin_button.inputState == InputState.TOUCHED || ethereum_button.inputState == InputState.TOUCHED){
            scroller.inputState = InputState.UNTOUCHED;
            return;     // не обрабатываем
        }

        scroller.update();

        if (scroller.value.y < 0){ scroller.value.y = 0; }  // нельзя листать вверх
        else if (scroller.value.y > information_field.get_height()){    // вниз нельзя листать дальше, чем высота текста
            scroller.value.y = information_field.get_height();
        }

        about_label.offset.y = scroller.value.y;
        information_field.offset.y = scroller.value.y;

        bitcoin_button.offset.y = scroller.value.y;
        ethereum_button.offset.y = scroller.value.y;
    }

    @Override
    public void resize(int width, int height) {
        about_label.resize(game.upper_button_corner_x, game.upper_button_corner_y, game.button_w, game.button_h, Align.center);

        int font_size = (int) (game.HEIGHT * (1.0f / 32.0f));
        information_field.resize(game.upper_button_corner_x, game.upper_button_corner_y - game.down_margin + game.button_h, game.button_w, font_size);

        bitcoin_button.resize(game.upper_button_corner_x, game.upper_button_corner_y - game.down_margin - information_field.get_height(), game.button_h, game.button_h);
        float theme_button_corner_x = game.upper_button_corner_x + game.button_w - game.button_h;
        ethereum_button.resize(theme_button_corner_x, game.upper_button_corner_y - game.down_margin - information_field.get_height(), game.button_h, game.button_h);

        back_button.resize(game.upper_button_corner_x, game.upper_button_corner_y - game.down_margin * 5, game.button_w, game.button_h);

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

        about_label.draw();
        information_field.draw();
        bitcoin_button.draw();
        ethereum_button.draw();
        back_button.draw();

        game.batch.end();

        update();

        if (game.is_back_button_pressed){
            game.is_back_button_pressed = false;
            game.setScreen("menu_about");
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
