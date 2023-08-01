package com.quartyom.screens.Menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Align;
import com.quartyom.LayThePath;
import com.quartyom.game_elements.Button;
import com.quartyom.game_elements.Label;
import com.quartyom.game_elements.QuScreen;
import com.quartyom.interfaces.QuEvent;

public class InfoTab extends QuScreen {

    final LayThePath game;

    Label infoLabel;
    Button howToPlayButton, aboutButton, statsButton, backButton;

    public InfoTab(final LayThePath game) {
        this.game = game;

        infoLabel = new Label(game, game.locale.get("Info"));

        howToPlayButton = new Button("in_main_menu", game, new QuEvent() {
            @Override
            public void execute() {
                game.setScreen("menu_how_to_play");
            }
        });
        howToPlayButton.setNinePatch(6).setLabel(game.locale.get("How to play"));

        aboutButton = new Button("in_main_menu", game, new QuEvent() {
            @Override
            public void execute() {
                game.setScreen("menu_about");
            }
        });
        aboutButton.setNinePatch(6).setLabel(game.locale.get("About"));

        statsButton = new Button("in_main_menu", game, new QuEvent() {
            @Override
            public void execute() {
                game.setScreen("menu_stats");
            }
        });
        statsButton.setNinePatch(6).setLabel(game.locale.get("Stats"));

        backButton = new Button("in_main_menu", game, new QuEvent() {
            @Override
            public void execute() {
                game.setScreen("menu");
            }
        });
        backButton.setNinePatch(6).setLabel(game.locale.get("Back"));

    }

    @Override
    public void resize(int width, int height) {
        infoLabel.resize(game.upperButtonCornerX, game.upperButtonCornerY, game.buttonW,
                game.buttonH, Align.center);
        howToPlayButton.resize(game.upperButtonCornerX,
                game.upperButtonCornerY - game.downMargin, game.buttonW, game.buttonH);
        aboutButton.resize(game.upperButtonCornerX,
                game.upperButtonCornerY - game.downMargin * 2, game.buttonW, game.buttonH);
        statsButton.resize(game.upperButtonCornerX,
                game.upperButtonCornerY - game.downMargin * 3, game.buttonW, game.buttonH);
        // gap
        backButton.resize(game.upperButtonCornerX,
                game.upperButtonCornerY - game.downMargin * 5, game.buttonW, game.buttonH);
    }

    @Override
    public void render(float delta) {
        Gdx.gl20.glClear(Gdx.gl20.GL_COLOR_BUFFER_BIT);

        infoLabel.draw();
        howToPlayButton.draw();
        aboutButton.draw();
        statsButton.draw();
        backButton.draw();

        howToPlayButton.update();
        aboutButton.update();
        statsButton.update();
        backButton.update();

        if (game.isBackButtonPressed) {
            game.isBackButtonPressed = false;
            game.setScreen("menu");
        }
    }

}
