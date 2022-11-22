package com.quartyom.screens.Menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.Align;
import com.quartyom.LayThePath;
import com.quartyom.game_elements.Button;
import com.quartyom.game_elements.QuScreen;
import com.quartyom.game_elements.Timer;
import com.quartyom.interfaces.QuEvent;
import com.quartyom.game_elements.InputState;
import com.quartyom.game_elements.Label;
import com.quartyom.game_elements.Scroller;
import com.quartyom.game_elements.TextField;

public class HowICanHelpTab extends QuScreen {
    final LayThePath game;

    Button bitcoin_button, ethereum_button, back_button;
    Label about_label, copied_label;
    TextField information_field;
    Scroller scroller;

    Timer timer;
    boolean to_show_copied_label;

    public HowICanHelpTab(final LayThePath game){
        this.game = game;

        timer = new Timer(game, new QuEvent() {
            @Override
            public void execute() {
                to_show_copied_label = false;
            }
        });

        about_label = new Label(game, game.locale.get("How I can help"));
        copied_label = new Label(game, game.locale.get("Copied to clipboard"));

        information_field = new TextField(game, Gdx.files.internal("texts/" + game.userData.locale + "/how_i_can_help.txt").readString());

        bitcoin_button = new Button("bitcoin", game, new QuEvent() {
            @Override
            public void execute() {
                Gdx.app.getClipboard().setContents("bc1qekxx6tgmk6hh8rqhugn20wvngkrgl5sw2rx9mu");
                to_show_copied_label = true;
                timer.set(2000);
            }
        });
        bitcoin_button.setSound("click_1").setHint(game.locale.get("copy address to clipboard"));

        ethereum_button = new Button("ethereum", game, new QuEvent() {
            @Override
            public void execute() {
                Gdx.app.getClipboard().setContents("0xF639A0c69E1E3FB3Eaf0092988404Ec99214E097");
                to_show_copied_label = true;
                timer.set(2000);
            }
        });
        ethereum_button.setSound("click_1").setHint(game.locale.get("copy address to clipboard"));

        back_button = new Button("in_main_menu", game, new QuEvent() {
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
        timer.update();
        back_button.update();
        bitcoin_button.update();
        ethereum_button.update();
        scroller.update();

        if (scroller.value.y < 0){ scroller.value.y = 0; }  // нельзя листать вверх
        else if (scroller.value.y > information_field.get_height()){    // вниз нельзя листать дальше, чем высота текста
            scroller.value.y = information_field.get_height();
        }

        about_label.offset.y = scroller.value.y;
        information_field.offset.y = scroller.value.y;
        copied_label.offset.y = scroller.value.y;

        bitcoin_button.offset.y = scroller.value.y;
        ethereum_button.offset.y = scroller.value.y;
    }

    @Override
    public void resize(int width, int height) {
        about_label.resize(game.upper_button_corner_x, game.upper_button_corner_y, game.button_w, game.button_h, Align.center);

        int font_size = (int) (game.HEIGHT * (1.0f / 32.0f));
        information_field.resize(game.upper_button_corner_x, game.upper_button_corner_y - game.down_margin + game.button_h, game.button_w, font_size);

        copied_label.resize(game.upper_button_corner_x, information_field.get_lower_y() - font_size, game.button_w, font_size, Align.center);

        bitcoin_button.resize(game.upper_button_corner_x, copied_label.get_lower_y() - game.button_h, game.button_h, game.button_h);
        float theme_button_corner_x = game.upper_button_corner_x + game.button_w - game.button_h;
        ethereum_button.resize(theme_button_corner_x, copied_label.get_lower_y() - game.button_h, game.button_h, game.button_h);

        back_button.resize(game.upper_button_corner_x, game.upper_button_corner_y - game.down_margin * 5, game.button_w, game.button_h);

        scroller.resize_full();
    }

    @Override
    public void render(float delta) {
        Gdx.gl20.glClear(Gdx.gl20.GL_COLOR_BUFFER_BIT);

        about_label.draw();
        information_field.draw();
        if (to_show_copied_label) {
            copied_label.draw();
        }
        bitcoin_button.draw();
        ethereum_button.draw();
        back_button.draw();

        update();

        if (game.is_back_button_pressed){
            game.is_back_button_pressed = false;
            game.setScreen("menu_about");
        }
    }

}
