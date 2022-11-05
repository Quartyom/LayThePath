package com.quartyom.screens.Menu;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.quartyom.LayThePath;
import com.quartyom.game_elements.Button;
import com.quartyom.game_elements.InputState;
import com.quartyom.game_elements.QuScreen;
import com.quartyom.interfaces.QuEvent;
import com.quartyom.game_elements.Label;
import com.quartyom.game_elements.Slider;
import com.quartyom.game_elements.SwitchButton;
import com.quartyom.game_elements.Vibrator;

import java.util.Random;

public class SettingsTab extends QuScreen {
    final LayThePath game;

    Vibrator vibrator;

    Label settings_label;
    Button locale_button, controls_button, back_button;
    SwitchButton sound_button, vibration_button, hints_button, premium_button;
    Slider sound_slider;

    public SettingsTab(final LayThePath game){
        this.game = game;

        vibrator = game.vibrator;

        settings_label = new Label(game, game.locale.get("Settings"));

        sound_slider = new Slider("regular", game);
        sound_slider.value = game.userData.volume;

        sound_button = new SwitchButton(game);
        sound_button.add("sound_off").add("sound_on");
        sound_button.state = game.userData.volume > 0 ? 1 : 0;

        vibration_button = new SwitchButton(game);
        vibration_button.add("vibration_off").add("vibration_on");
        vibration_button.state = game.userData.vibration_is_on ? 1 : 0;

        hints_button = new SwitchButton(game);
        hints_button.add("button_hints_off").add("button_hints_on");    // обратный порядок
        hints_button.state = game.userData.button_hints_are_on ? 1 : 0;

        locale_button = new Button("locale", game, new QuEvent() {
            @Override
            public void execute() {
                game.setScreen("menu_locale");
            }
        });


        premium_button = new SwitchButton(game);
        premium_button.add("premium_normal").add("premium_pressed");
        //premium_button.state = game.userData.premium_is_on ? 1 : 0;   // уже есть в show()
        premium_button.to_change_state_on_click = false;

        controls_button = new Button("controls", game, new QuEvent() {
            @Override
            public void execute() {
                game.setScreen("menu_controls");
            }
        });

        back_button = new Button("in_main_menu", game, new QuEvent() {
            @Override
            public void execute() {
                game.setScreen("menu");
            }
        });
        back_button.setNinePatch(6).setLabel(game.locale.get("Back"));

    }

    public void update(){
        sound_button.update();
        if (sound_button.recently_changed){
            if (sound_button.state == 0){
                game.userData.volume = 0;
                sound_slider.value = 0;
                game.save_user_data();
            }
            else {
                game.userData.volume = 0.5f;
                sound_slider.value = 0.5f;
                game.save_user_data();
            }
        }
        else {
            sound_slider.update();
            if (game.userData.volume != sound_slider.value) {
                game.userData.volume = sound_slider.value;
                if (sound_slider.value != 0) {
                    sound_button.state = 1;
                }
                else {
                    sound_button.state = 0;
                }
                //game.save_user_data();
            }
            if (sound_slider.inputState == InputState.JUST_UNTOUCHED){
                game.save_user_data();
            }
        }

        vibration_button.update();
        if (vibration_button.recently_changed){
            game.userData.vibration_is_on = vibration_button.state == 1;
            if (game.userData.vibration_is_on){
                vibrator.vibrate(150);
            }
            game.save_user_data();
        }

        hints_button.update();
        if (hints_button.recently_changed){
            game.userData.button_hints_are_on = hints_button.state == 1;
            game.save_user_data();
        }

        premium_button.update();
        if (premium_button.recently_changed){
            if (game.userData.premium_is_on){
                game.setScreen("menu_premium_is_activated");
            }
            else {
                game.setScreen("menu_premium");
            }
        }

        locale_button.update();
        if (!is_too_fast) { controls_button.update(); }
        back_button.update();
    }

    @Override
    public void resize(int width, int height) {
        settings_label.resize(game.upper_button_corner_x, game.upper_button_corner_y, game.button_w, game.button_h, 1);

        float sound_button_corner_y = game.upper_button_corner_y - game.down_margin;
        float slider_margin_x = game.button_h / 4;
        float sound_slider_corner_x = game.upper_button_corner_x + game.button_h + slider_margin_x;
        float slider_w = game.button_w - game.button_h - slider_margin_x;
        float sound_slider_corner_y = sound_button_corner_y;

        sound_button.resize(game.upper_button_corner_x, sound_button_corner_y, game.button_h, game.button_h);
        sound_slider.resize(sound_slider_corner_x, sound_slider_corner_y + game.button_h / 4, slider_w, game.button_h / 2);

        vibration_button.resize(game.upper_button_corner_x, game.upper_button_corner_y - game.down_margin * 2, game.button_h, game.button_h);

        float theme_button_corner_x = game.upper_button_corner_x + game.button_w - game.button_h;
        hints_button.resize(theme_button_corner_x, game.upper_button_corner_y - game.down_margin * 2, game.button_h, game.button_h);

        premium_button.resize(game.upper_button_corner_x, game.upper_button_corner_y - game.down_margin * 3, game.button_h, game.button_h);
        locale_button.resize(theme_button_corner_x, game.upper_button_corner_y - game.down_margin * 3, game.button_h, game.button_h);

        if (is_too_fast){
            back_button.resize(game.upper_button_corner_x, game.upper_button_corner_y - game.down_margin * 4, game.button_w, game.button_h);
        }
        else {
            controls_button.resize(game.upper_button_corner_x, game.upper_button_corner_y - game.down_margin * 4, game.button_h, game.button_h);
            back_button.resize(game.upper_button_corner_x, game.upper_button_corner_y - game.down_margin * 5, game.button_w, game.button_h);
        }
    }

    private long previously_launched;
    private boolean is_too_fast = false;

    @Override
    public void show() {
        Gdx.gl20.glClearColor(0, 0, 0, 1);

        if (System.currentTimeMillis() - previously_launched < 200){
            Random random = new Random();
            if (random.nextInt(6) == 0){
                settings_label.set_string(game.locale.get("Not so fast"));
                is_too_fast = true;
            }
        }
        previously_launched = System.currentTimeMillis();

        premium_button.state = game.userData.premium_is_on ? 1 : 0;
    }

    @Override
    public void render(float delta) {
        Gdx.gl20.glClear(Gdx.gl20.GL_COLOR_BUFFER_BIT);

        settings_label.draw();
        sound_button.draw();
        sound_slider.draw();
        vibration_button.draw();
        hints_button.draw();

        premium_button.draw();
        locale_button.draw();
        if (!is_too_fast){ controls_button.draw();}
        back_button.draw();

        update();

        if (game.is_back_button_pressed){
            game.is_back_button_pressed = false;
            game.setScreen("menu");
        }
    }


    @Override
    public void hide() {
        settings_label.set_string(game.locale.get("Settings"));
        is_too_fast = false;
    }

}
