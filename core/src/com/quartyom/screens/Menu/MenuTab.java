package com.quartyom.screens.Menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Align;
import com.quartyom.LayThePath;
import com.quartyom.UserData;
import com.quartyom.game_elements.AttentionScreenWithBackButton;
import com.quartyom.game_elements.Button;
import com.quartyom.game_elements.InputState;
import com.quartyom.game_elements.Label;
import com.quartyom.game_elements.QuScreen;
import com.quartyom.game_elements.SwitchButton;
import com.quartyom.interfaces.QuEvent;

public class MenuTab extends QuScreen {

    final LayThePath game;

    Button easterButton, classicButton, colorsButton, infoButton, settingsButton;
    SwitchButton colorsEasterButton;
    Label menuLabel;

    private int timesEasterButtonClicked;
    private int timesColorsEasterButtonClicked;
    private boolean backPressedBefore = false;

    public MenuTab(final LayThePath game) {
        this.game = game;

        game.add("menu_classic", new ClassicTab(game));
        game.add("menu_about", new AboutTab(game));
        game.add("menu_editor_is_unavailable", new AttentionScreenWithBackButton(game,
                "editor_is_unavailable", "menu_classic"));
        game.add("menu_how_can_i_help", new HowICanHelpTab(game));
        game.add("menu_how_to_play", new HowToPlayTab(game));
        game.add("menu_info", new InfoTab(game));
        game.add("menu_settings", new SettingsTab(game));
        game.add("menu_premium", new PremiumTab(game));
        game.add("menu_premium_is_activated", new PremiumIsActivated(game));
        game.add("menu_locale", new LocaleTab(game));
        game.add("menu_controls", new ControlsTab(game));
        game.add("menu_stats", new StatsTab(game));
        //game.add("menu_zen_after_ads", new ZenAfterAds(game));
        game.add("menu_zen_is_unavailable", new AttentionScreenWithBackButton(game, "zen_is_unavailable",
                "menu_classic"));
        game.add("menu_colors_is_unavailable", new AttentionScreenWithBackButton(game,
                "colors_is_unavailable", "menu"));


        menuLabel = new Label(game, game.locale.get("Menu"));

        easterButton = new Button(game, new QuEvent() {
            @Override
            public void execute() {
                timesEasterButtonClicked++;

                if (timesEasterButtonClicked >= 30) {
                    menuLabel.setString(game.locale.get("Ughhhh"));
                } else if (timesEasterButtonClicked >= 22) {
                    menuLabel.setString(game.locale.get("stop?"));
                } else if (timesEasterButtonClicked >= 20) {
                    menuLabel.setString(game.locale.get("you please"));
                } else if (timesEasterButtonClicked >= 18) {
                    menuLabel.setString(game.locale.get("Would"));
                } else if (timesEasterButtonClicked >= 10) {
                    menuLabel.setString(game.locale.get("It's annoying"));
                } else if (timesEasterButtonClicked >= 3) {
                    menuLabel.setString(game.locale.get("Auch!"));
                }
            }
        });

        classicButton = new Button("in_main_menu", game, new QuEvent() {
            @Override
            public void execute() {
                game.setScreen("menu_classic");
            }
        });
        classicButton.setNinePatch(6).setLabel(game.locale.get("Classic"));

        colorsButton = new Button("in_main_menu", game, new QuEvent() {
            @Override
            public void execute() {
                UserData userData = game.userData;
                if (!userData.colors_is_available) {
                    if (userData.max_level_achieved > 70) {
                        userData.colors_is_available = true;
                        game.saveUserData();
                    }
                    else {
                        game.setScreen("menu_colors_is_unavailable");
                        return;
                    }
                }
                game.setScreen("colors_level");
            }
        });
        colorsButton.setNinePatch(6).setLabel(game.locale.get("Colors"));

        colorsEasterButton = new SwitchButton(game, new QuEvent() {
            @Override
            public void execute() {
                timesColorsEasterButtonClicked++;
                if (timesColorsEasterButtonClicked >= 2) {
                    game.setScreen("easter_colors");
                    colorsButton.inputState = InputState.UNTOUCHED;
                }
            }
        });
        colorsEasterButton.click_sound = null;

        // gap

        infoButton = new Button("in_main_menu", game, new QuEvent() {
            @Override
            public void execute() {
                game.setScreen("menu_info");
            }
        });
        infoButton.setNinePatch(6).setLabel(game.locale.get("Info"));

        settingsButton = new Button("in_main_menu", game, new QuEvent() {
            @Override
            public void execute() {
                game.setScreen("menu_settings");
            }
        });
        settingsButton.setNinePatch(6).setLabel(game.locale.get("Settings"));

    }

    @Override
    public void resize(int width, int height) {
        menuLabel.resize(game.upperButtonCornerX, game.upperButtonCornerY, game.buttonW,
                game.buttonH, Align.center);
        easterButton.resize(game.upperButtonCornerX, game.upperButtonCornerY, game.buttonW,
                game.buttonH);
        classicButton.resize(game.upperButtonCornerX, game.upperButtonCornerY - game.downMargin,
                game.buttonW, game.buttonH);
        colorsButton.resize(game.upperButtonCornerX, game.upperButtonCornerY - game.downMargin * 2,
                game.buttonW, game.buttonH);
        // same as colorsButton
        colorsEasterButton.resize(game.upperButtonCornerX, game.upperButtonCornerY - game.downMargin * 2,
                game.buttonW, game.buttonH);
        infoButton.resize(game.upperButtonCornerX,
                game.upperButtonCornerY - game.downMargin * 3, game.buttonW, game.buttonH);
        // gap
        settingsButton.resize(game.upperButtonCornerX,
                game.upperButtonCornerY - game.downMargin * 5, game.buttonW, game.buttonH);
    }

    @Override
    public void show() {
        Gdx.gl20.glClearColor(0, 0, 0, 1);
        menuLabel.setString(game.locale.get("Menu"));
        timesEasterButtonClicked = 0;
        timesColorsEasterButtonClicked = 0;
        backPressedBefore = false;
    }

    @Override
    public void render(float delta) {
        Gdx.gl20.glClear(Gdx.gl20.GL_COLOR_BUFFER_BIT);

        menuLabel.draw();
        classicButton.draw();
        colorsButton.draw();
        infoButton.draw();
        settingsButton.draw();

        easterButton.update();
        classicButton.update();
        colorsButton.update();
        colorsEasterButton.update();
        infoButton.update();
        settingsButton.update();

        if (game.isBackButtonPressed) {
            if (backPressedBefore) {
                Gdx.app.exit();
            } else {
                backPressedBefore = true;
                game.isBackButtonPressed = false;
            }
        }
    }

}
