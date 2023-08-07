package com.quartyom.screens.Menu;


import com.badlogic.gdx.Gdx;
import com.quartyom.LayThePath;
import com.quartyom.game_elements.Button;
import com.quartyom.game_elements.InputState;
import com.quartyom.game_elements.Label;
import com.quartyom.game_elements.QuScreen;
import com.quartyom.game_elements.Slider;
import com.quartyom.game_elements.SwitchButton;
import com.quartyom.game_elements.Timer;
import com.quartyom.game_elements.Vibrator;
import com.quartyom.interfaces.QuEvent;

import java.util.Random;

public class SettingsTab extends QuScreen {

    final LayThePath game;

    Vibrator vibrator;

    Label settingsLabel;
    Button localeButton, controlsButton, backButton;
    SwitchButton soundButton, vibrationButton, hintsButton, premiumButton, themeButton;
    Slider soundSlider;

    boolean isThemeButtonVisible = false;
    Timer timer;

    public SettingsTab(final LayThePath game) {
        this.game = game;

        vibrator = game.vibrator;

        settingsLabel = new Label(game, game.locale.get("Settings"));

        soundSlider = new Slider("regular", game);
        soundSlider.value = game.userData.volume;

        soundButton = new SwitchButton(game, new QuEvent() {
            @Override
            public void execute() {
                if (soundButton.state == 0) {
                    game.userData.volume = 0;
                    soundSlider.value = 0;
                } else {
                    game.userData.volume = 0.5f;
                    soundSlider.value = 0.5f;
                }
                game.saveUserData();
            }
        });
        soundButton.add("sound_off").add("sound_on");
        soundButton.state = game.userData.volume > 0 ? 1 : 0;

        vibrationButton = new SwitchButton(game, new QuEvent() {
            @Override
            public void execute() {
                game.userData.vibration_is_on = vibrationButton.state == 1;
                if (game.userData.vibration_is_on) {
                    vibrator.vibrate(150);
                }
                game.saveUserData();
            }
        });
        vibrationButton.add("vibration_off").add("vibration_on");
        vibrationButton.state = game.userData.vibration_is_on ? 1 : 0;

        hintsButton = new SwitchButton(game, new QuEvent() {
            @Override
            public void execute() {
                game.userData.button_hints_are_on = hintsButton.state == 1;
                game.saveUserData();
            }
        });
        hintsButton.add("button_hints_off").add("button_hints_on");    // обратный порядок
        hintsButton.state = game.userData.button_hints_are_on ? 1 : 0;

        localeButton = new Button("locale", game, new QuEvent() {
            @Override
            public void execute() {
                game.setScreen("menu_locale");
            }
        });

        premiumButton = new SwitchButton(game, new QuEvent() {
            @Override
            public void execute() {
                if (game.userData.premium_is_on) {
                    game.setScreen("menu_premium_is_activated");
                } else {
                    game.setScreen("menu_premium");
                }
            }
        });
        premiumButton.add("premium_normal").add("premium_pressed");
        //premium_button.state = game.userData.premium_is_on ? 1 : 0;   // уже есть в show()
        premiumButton.toChangeStateOnClick = false;

        controlsButton = new Button("controls", game, new QuEvent() {
            @Override
            public void execute() {
                game.setScreen("menu_controls");
            }
        });

        timer = new Timer(new QuEvent() {
            @Override
            public void execute() {
                isThemeButtonVisible = false;
            }
        });

        themeButton = new SwitchButton(game, new QuEvent() {
            @Override
            public void execute() {
                isThemeButtonVisible = true;
                timer.set(1500);
            }
        });
        themeButton.add("dark_theme");
        themeButton.click_sound = null;

        backButton = new Button("in_main_menu", game, new QuEvent() {
            @Override
            public void execute() {
                game.setScreen("menu");
            }
        });
        backButton.setNinePatch(6).setLabel(game.locale.get("Back"));

    }

    public void update() {
        soundButton.update();
        if (!soundButton.recentlyChanged) {
            soundSlider.update();
            if (game.userData.volume != soundSlider.value) {
                game.userData.volume = soundSlider.value;
                if (soundSlider.value != 0) {
                    soundButton.state = 1;
                } else {
                    soundButton.state = 0;
                }
                //game.save_user_data();
            }
            if (soundSlider.inputState == InputState.JUST_UNTOUCHED) {
                game.saveUserData();
            }
        }

        vibrationButton.update();
        hintsButton.update();
        premiumButton.update();
        localeButton.update();
        timer.update();

        if (!isTooFast) {
            controlsButton.update();
            themeButton.update();
        }
        backButton.update();
    }

    @Override
    public void resize(int width, int height) {
        settingsLabel.resize(game.upperButtonCornerX, game.upperButtonCornerY, game.buttonW,
                game.buttonH, 1);

        float sound_button_corner_y = game.upperButtonCornerY - game.downMargin;
        float slider_margin_x = game.buttonH / 4;
        float sound_slider_corner_x = game.upperButtonCornerX + game.buttonH + slider_margin_x;
        float slider_w = game.buttonW - game.buttonH - slider_margin_x;
        float sound_slider_corner_y = sound_button_corner_y;

        soundButton.resize(game.upperButtonCornerX, sound_button_corner_y, game.buttonH,
                game.buttonH);
        soundSlider.resize(sound_slider_corner_x, sound_slider_corner_y + game.buttonH / 4, slider_w,
                game.buttonH / 2);

        vibrationButton.resize(game.upperButtonCornerX,
                game.upperButtonCornerY - game.downMargin * 2, game.buttonH, game.buttonH);

        float theme_button_corner_x = game.upperButtonCornerX + game.buttonW - game.buttonH;
        hintsButton.resize(theme_button_corner_x, game.upperButtonCornerY - game.downMargin * 2,
                game.buttonH, game.buttonH);

        premiumButton.resize(game.upperButtonCornerX,
                game.upperButtonCornerY - game.downMargin * 3, game.buttonH, game.buttonH);
        localeButton.resize(theme_button_corner_x, game.upperButtonCornerY - game.downMargin * 3,
                game.buttonH, game.buttonH);

        if (isTooFast) {
            backButton.resize(game.upperButtonCornerX,
                    game.upperButtonCornerY - game.downMargin * 4, game.buttonW, game.buttonH);
        } else {
            controlsButton.resize(game.upperButtonCornerX,
                    game.upperButtonCornerY - game.downMargin * 4, game.buttonH, game.buttonH);
            themeButton.resize(theme_button_corner_x,
                    game.upperButtonCornerY - game.downMargin * 4, game.buttonH, game.buttonH);
            backButton.resize(game.upperButtonCornerX,
                    game.upperButtonCornerY - game.downMargin * 5, game.buttonW, game.buttonH);
        }
    }

    private long previouslyLaunched;
    private boolean isTooFast = false;

    @Override
    public void show() {
        Gdx.gl20.glClearColor(0, 0, 0, 1);

        if (System.currentTimeMillis() - previouslyLaunched < 200) {
            Random random = new Random();
            if (random.nextInt(6) == 0) {
                settingsLabel.setString(game.locale.get("Not so fast"));
                isTooFast = true;
                resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            }
        }
        previouslyLaunched = System.currentTimeMillis();

        premiumButton.state = game.userData.premium_is_on ? 1 : 0;
    }

    @Override
    public void render(float delta) {
        Gdx.gl20.glClear(Gdx.gl20.GL_COLOR_BUFFER_BIT);

        settingsLabel.draw();
        soundButton.draw();
        soundSlider.draw();
        vibrationButton.draw();
        hintsButton.draw();

        premiumButton.draw();
        localeButton.draw();
        if (!isTooFast) {
            controlsButton.draw();
            if (isThemeButtonVisible) {
                themeButton.draw();
            }
        }
        backButton.draw();

        update();

        if (game.isBackButtonPressed) {
            game.isBackButtonPressed = false;
            game.setScreen("menu");
        }
    }


    @Override
    public void hide() {
        settingsLabel.setString(game.locale.get("Settings"));
        if (isTooFast) {
            isTooFast = false;
            resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }
    }

}
