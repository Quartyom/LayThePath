package com.quartyom.screens.Menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Align;
import com.quartyom.LayThePath;
import com.quartyom.game_elements.Button;
import com.quartyom.game_elements.Label;
import com.quartyom.game_elements.QuScreen;
import com.quartyom.interfaces.QuEvent;

public class MenuTab extends QuScreen {

    final LayThePath game;

    Button easterButton, playButton, zenButton, editorButton, infoButton, settingsButton;
    Label menuLabel;

    private int timesEasterButtonClicked;
    private boolean backPressedBefore = false;

    public MenuTab(final LayThePath game) {
        this.game = game;

        game.add("menu_about", new AboutTab(game));
        game.add("menu_editor_is_unavailable", new EditorIsUnavailable(game));
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
        game.add("menu_zen_is_unavailable", new ZenIsUnavailable(game));

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

        playButton = new Button("in_main_menu", game, new QuEvent() {
            @Override
            public void execute() {
                game.setScreen("level");
            }
        });
        playButton.setNinePatch(6).setLabel(game.locale.get("Play"));

        zenButton = new Button("in_main_menu", game, new QuEvent() {
            @Override
            public void execute() {
                if (game.userData.zen_is_available) {
                    /*if (game.userData.editor_is_available){
                        game.setScreen("zen");
                    }
                    else {
                        game.setScreen("menu_zen_after_ads");
                    }*/
                    game.setScreen("zen");
                } else {
                    game.setScreen("menu_zen_is_unavailable");
                }
            }
        });
        zenButton.setNinePatch(6).setLabel(game.locale.get("Zen"));

        editorButton = new Button("in_main_menu", game, new QuEvent() {
            @Override
            public void execute() {
                if (game.userData.editor_is_available) {
                    game.setScreen("editor");
                } else {
                    game.setScreen("menu_editor_is_unavailable");
                }
            }
        });
        editorButton.setNinePatch(6).setLabel(game.locale.get("Editor"));

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
        playButton.resize(game.upperButtonCornerX, game.upperButtonCornerY - game.downMargin,
                game.buttonW, game.buttonH);
        zenButton.resize(game.upperButtonCornerX, game.upperButtonCornerY - game.downMargin * 2,
                game.buttonW, game.buttonH);
        editorButton.resize(game.upperButtonCornerX,
                game.upperButtonCornerY - game.downMargin * 3, game.buttonW, game.buttonH);
        infoButton.resize(game.upperButtonCornerX,
                game.upperButtonCornerY - game.downMargin * 4, game.buttonW, game.buttonH);
        settingsButton.resize(game.upperButtonCornerX,
                game.upperButtonCornerY - game.downMargin * 5, game.buttonW, game.buttonH);
    }

    @Override
    public void show() {
        Gdx.gl20.glClearColor(0, 0, 0, 1);
        menuLabel.setString(game.locale.get("Menu"));
        timesEasterButtonClicked = 0;
        backPressedBefore = false;
    }

    @Override
    public void render(float delta) {
        Gdx.gl20.glClear(Gdx.gl20.GL_COLOR_BUFFER_BIT);

        menuLabel.draw();
        playButton.draw();
        zenButton.draw();
        editorButton.draw();
        infoButton.draw();
        settingsButton.draw();

        easterButton.update();
        playButton.update();
        zenButton.update();
        editorButton.update();
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
