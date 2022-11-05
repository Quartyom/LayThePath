package com.quartyom.screens.Menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.Align;
import com.quartyom.LayThePath;
import com.quartyom.game_elements.Button;
import com.quartyom.game_elements.QuScreen;
import com.quartyom.interfaces.QuEvent;
import com.quartyom.game_elements.InputState;
import com.quartyom.game_elements.Label;
import com.quartyom.game_elements.Scroller;
import com.quartyom.game_elements.TextField;

public class AboutTab extends QuScreen {
    final LayThePath game;

    Button back_button, how_can_i_help_button;
    Label about_label;
    TextField information_field;
    Scroller scroller;

    public AboutTab(final LayThePath game){
        this.game = game;

        about_label = new Label(game, game.locale.get("About"));

        information_field = new TextField(game, Gdx.files.internal("texts/" + game.userData.locale + "/about.txt").readString());

        back_button = new Button("in_main_menu", game, new QuEvent() {
            @Override
            public void execute() {
                game.setScreen("menu_info");
            }
        });
        back_button.setNinePatch(6).setLabel(game.locale.get("Back"));

        how_can_i_help_button = new Button("in_main_menu", game, new QuEvent() {
            @Override
            public void execute() {
                game.setScreen("menu_how_can_i_help");
            }
        });
        how_can_i_help_button.setNinePatch(6).setLabel(game.locale.get("How can I help?"));

        scroller = new Scroller(game);
        scroller.physics_on = true;

    }

    public void update(){
        back_button.update();
        if (back_button.inputState == InputState.UNTOUCHED) {   // если нажатия нет, то тогда проверяем нажатие на другую кнопку, которая снизу
            how_can_i_help_button.update();
        }

        if (back_button.inputState == InputState.TOUCHED || how_can_i_help_button.inputState == InputState.TOUCHED){
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
        how_can_i_help_button.offset.y = scroller.value.y;

    }

    @Override
    public void resize(int width, int height) {
        about_label.resize(game.upper_button_corner_x, game.upper_button_corner_y, game.button_w, game.button_h, Align.center);

        int font_size = (int) (game.HEIGHT * (1.0f / 32.0f));
        information_field.resize(game.upper_button_corner_x, game.upper_button_corner_y - game.down_margin + game.button_h, game.button_w, font_size);

        how_can_i_help_button.resize(game.upper_button_corner_x, game.upper_button_corner_y - game.down_margin * 2 - information_field.get_height(), game.button_w, game.button_h);

        back_button.resize(game.upper_button_corner_x, game.upper_button_corner_y - game.down_margin * 5, game.button_w, game.button_h);

        scroller.resize_full();
    }

    @Override
    public void render(float delta) {
        Gdx.gl20.glClear(Gdx.gl20.GL_COLOR_BUFFER_BIT);

        about_label.draw();
        information_field.draw();
        how_can_i_help_button.draw();
        back_button.draw();

        update();

        if (game.is_back_button_pressed){
            game.is_back_button_pressed = false;
            game.setScreen("menu_info");
        }
    }

}
