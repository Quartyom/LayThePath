package com.quartyom.screens.Colors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Align;
import com.quartyom.LayThePath;
import com.quartyom.game_elements.Button;
import com.quartyom.game_elements.Label;
import com.quartyom.game_elements.QuScreen;
import com.quartyom.game_elements.TextField;
import com.quartyom.interfaces.QuEvent;

public class ColorsLevelsAreOver extends QuScreen {
    final LayThePath game;
    public ColorsScreen colorsScreen;

    Label hintLabel;
    TextField infoField;
    Button startOverButton, menuButton;

    public ColorsLevelsAreOver(final ColorsScreen colorsScreen) {
        this.colorsScreen = colorsScreen;
        game = colorsScreen.game;

        hintLabel = new Label(game, colorsScreen.game.locale.get("Attention"));

        infoField = new TextField(colorsScreen.game, Gdx.files.internal("texts/" + game.userData.locale + "/levels_are_over.txt").readString());

        startOverButton = new Button("in_main_menu", game, new QuEvent() {
            @Override
            public void execute() {
                colorsScreen.colorsBoard.currentColorsLevel = 1;
                game.userData.current_level = 1;
                game.saveUserData();
                colorsScreen.colorsBoard.loadLevel();

                game.setScreen("colors_level");
            }
        });
        startOverButton.setNinePatch(6).setLabel(colorsScreen.game.locale.get("Start over"));

        menuButton = new Button("in_main_menu", game, new QuEvent() {
            @Override
            public void execute() {
                colorsScreen.game.setScreen("menu");
            }
        });
        menuButton.setNinePatch(6).setLabel(colorsScreen.game.locale.get("Go to Menu"));

    }

    @Override
    public void render(float delta) {
        Gdx.gl20.glClear(Gdx.gl20.GL_COLOR_BUFFER_BIT);

        hintLabel.draw();
        infoField.draw();
        startOverButton.draw();
        menuButton.draw();

        startOverButton.update();
        menuButton.update();

        if (game.isBackButtonPressed) {
            game.isBackButtonPressed = false;
            game.setScreen("menu");
        }
    }

    @Override
    public void resize(int width, int height) {
        hintLabel.resize(game.upperButtonCornerX, game.upperButtonCornerY, game.buttonW, game.buttonH, Align.center);

        int font_size = (int) (colorsScreen.game.HEIGHT * (1.0f / 32.0f));
        infoField.resize(game.upperButtonCornerX, game.upperButtonCornerY - game.downMargin + game.buttonH, game.buttonW, font_size);
        // gap
        startOverButton.resize(game.upperButtonCornerX, game.upperButtonCornerY - game.downMargin * 4, game.buttonW, game.buttonH);
        menuButton.resize(game.upperButtonCornerX, game.upperButtonCornerY - game.downMargin * 5, game.buttonW, game.buttonH);
    }

}
