package com.quartyom.screens.Menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.quartyom.LayThePath;
import com.quartyom.game_elements.Button;
import com.quartyom.game_elements.QuScreen;
import com.quartyom.interfaces.QuEvent;
import com.quartyom.game_elements.InputState;
import com.quartyom.game_elements.Label;
import com.quartyom.game_elements.Scroller;
import com.quartyom.game_elements.TextField;

public class HowToPlayTab extends QuScreen {
    final LayThePath game;

    Button back_button;
    Label how_to_play_label;
    TextField information_field;
    Scroller scroller;

    public HowToPlayTab(final LayThePath game){
        this.game = game;

        how_to_play_label = new Label(game, game.locale.get("How to play"));

        information_field = new TextField(game, Gdx.files.internal("texts/" + game.userData.locale + "/how_to_play.txt").readString());

        back_button = new Button("in_main_menu", game, new QuEvent() {
            @Override
            public void execute() {
                game.setScreen("menu_info");
            }
        });
        back_button.setNinePatch(6).setLabel(game.locale.get("Back"));

        scroller = new Scroller(game);
        scroller.physics_on = true;

    }

    public void update(){
        back_button.update();

        if (back_button.inputState == InputState.TOUCHED){
            return;     // не обрабатываем
        }

        scroller.update();

        if (scroller.value.y < 0){ scroller.value.y = 0; }  // нельзя листать вверх
        else if (scroller.value.y > information_field.get_height()){    // вниз нельзя листать дальше, чем высота текста
            scroller.value.y = information_field.get_height();
        }

        how_to_play_label.offset.y = scroller.value.y;
        information_field.offset.y = scroller.value.y;
    }

    public void resize(float upper_button_corner_x, float upper_button_corner_y, float button_w, float button_h, float down_margin){

        how_to_play_label.resize(upper_button_corner_x, upper_button_corner_y, button_w, button_h, 1);

        int font_size = (int) (game.HEIGHT * (1.0f / 32.0f));
        information_field.resize(upper_button_corner_x, upper_button_corner_y + button_h - down_margin, button_w, font_size);

        back_button.resize(upper_button_corner_x, upper_button_corner_y - down_margin * 5, button_w, button_h);

        scroller.resize_full();

    }

    @Override
    public void render(float delta) {
        Gdx.gl20.glClear(Gdx.gl20.GL_COLOR_BUFFER_BIT);

        how_to_play_label.draw();
        information_field.draw();
        back_button.draw();

        update();

        if (game.is_back_button_pressed){
            game.is_back_button_pressed = false;
            game.setScreen("menu_info");
        }
    }

    @Override
    public void resize(int width, int height) {
        how_to_play_label.resize(game.upper_button_corner_x, game.upper_button_corner_y, game.button_w, game.button_h, 1);

        int font_size = (int) (game.HEIGHT * (1.0f / 32.0f));
        information_field.resize(game.upper_button_corner_x, game.upper_button_corner_y + game.button_h - game.down_margin, game.button_w, font_size);

        back_button.resize(game.upper_button_corner_x, game.upper_button_corner_y - game.down_margin * 5, game.button_w, game.button_h);

        scroller.resize_full();
    }

}
