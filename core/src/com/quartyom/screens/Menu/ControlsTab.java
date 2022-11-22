package com.quartyom.screens.Menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.quartyom.LayThePath;
import com.quartyom.game_elements.Button;
import com.quartyom.game_elements.InputState;
import com.quartyom.game_elements.Label;
import com.quartyom.game_elements.QuScreen;
import com.quartyom.game_elements.Scroller;
import com.quartyom.game_elements.TextField;
import com.quartyom.interfaces.QuEvent;

public class ControlsTab extends QuScreen {

    final LayThePath game;

    Button activate_button, back_button;
    Label controls_label;
    TextField information_field;
    Scroller scroller;

    public ControlsTab(final LayThePath game){
        this.game = game;

        controls_label = new Label(game, game.locale.get("Controls"));

        information_field = new TextField(game, Gdx.files.internal("texts/" + game.userData.locale + "/controls.txt").readString());

        activate_button = new Button("in_main_menu", game, new QuEvent() {
            @Override
            public void execute() {
                if (game.userData.abstract_input_is_on){
                    game.userData.abstract_input_is_on = false;
                    activate_button.setLabel(game.locale.get("Activate"));
                }
                else {
                    game.userData.abstract_input_is_on = true;
                    activate_button.setLabel(game.locale.get("Deactivate"));
                }
                game.save_user_data();
            }
        });
        if (game.userData.abstract_input_is_on){
            activate_button.setNinePatch(6).setLabel(game.locale.get("Deactivate"));
        }
        else {
            activate_button.setNinePatch(6).setLabel(game.locale.get("Activate"));
        }

        back_button = new Button("in_main_menu", game, new QuEvent() {
            @Override
            public void execute() {
                game.setScreen("menu_settings");
            }
        });
        back_button.setNinePatch(6).setLabel(game.locale.get("Back"));

        scroller = new Scroller(game);
        scroller.physics_on = true;

    }

    @Override
    public void resize(int width, int height) {
        controls_label.resize(game.upper_button_corner_x, game.upper_button_corner_y, game.button_w, game.button_h, 1);

        int font_size = (int) (game.HEIGHT * (1.0f / 32.0f));
        information_field.resize(game.upper_button_corner_x, game.upper_button_corner_y - game.down_margin + game.button_h, game.button_w, font_size);

        activate_button.resize(game.upper_button_corner_x, game.upper_button_corner_y - game.down_margin - information_field.get_height(), game.button_w, game.button_h);

        back_button.resize(game.upper_button_corner_x, game.upper_button_corner_y - game.down_margin * 5, game.button_w, game.button_h);

        scroller.resize_full();
    }

    public void update(){
        back_button.update();
        activate_button.update();
        scroller.update();

        if (scroller.value.y < 0){ scroller.value.y = 0; }  // нельзя листать вверх
        else if (scroller.value.y > information_field.get_height()){    // вниз нельзя листать дальше, чем высота текста
            scroller.value.y = information_field.get_height();
        }

        controls_label.offset.y = scroller.value.y;
        information_field.offset.y = scroller.value.y;
        activate_button.offset.y = scroller.value.y;
    }

    @Override
    public void render(float delta) {
        Gdx.gl20.glClear(Gdx.gl20.GL_COLOR_BUFFER_BIT);

        controls_label.draw();
        information_field.draw();
        activate_button.draw();
        back_button.draw();

        update();

        if (game.is_back_button_pressed){
            game.is_back_button_pressed = false;
            game.setScreen("menu_settings");
        }
    }

}
