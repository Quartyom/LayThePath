package com.quartyom.screens.Level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Align;
import com.quartyom.LayThePath;
import com.quartyom.game_elements.Button;
import com.quartyom.game_elements.Label;
import com.quartyom.game_elements.QuScreen;
import com.quartyom.game_elements.TextField;
import com.quartyom.interfaces.QuEvent;

public class LevelHintTab extends QuScreen {
    final LayThePath game;
    public LevelScreen levelScreen;

    Label hintLabel;
    TextField infoField;
    Button backButton;

    public LevelHintTab(final LevelScreen levelScreen) {
        this.levelScreen = levelScreen;
        game = levelScreen.game;

        hintLabel = new Label(game, levelScreen.game.locale.get("Attention"));

        infoField = new TextField(levelScreen.game, Gdx.files.internal("texts/" + game.userData.locale + "/hints_are_over.txt").readString());

        backButton = new Button("in_main_menu", game, new QuEvent() {
            @Override
            public void execute() {
                game.setScreen("level");
            }
        });
        backButton.setNinePatch(6).setLabel(levelScreen.game.locale.get("Back"));

    }

    @Override
    public void resize(int width, int height) {
        hintLabel.resize(game.upperButtonCornerX, game.upperButtonCornerY, game.buttonW, game.buttonH, Align.center);

        int font_size = (int) (levelScreen.game.HEIGHT * (1.0f / 32.0f));
        infoField.resize(game.upperButtonCornerX, game.upperButtonCornerY - game.downMargin + game.buttonH, game.buttonW, font_size);

        backButton.resize(game.upperButtonCornerX, game.upperButtonCornerY - game.downMargin * 5, game.buttonW, game.buttonH);
    }

    @Override
    public void render(float delta) {
        Gdx.gl20.glClear(Gdx.gl20.GL_COLOR_BUFFER_BIT);

        hintLabel.draw();
        infoField.draw();
        backButton.draw();

        backButton.update();

        if (game.isBackButtonPressed) {
            game.isBackButtonPressed = false;
            game.setScreen("level");
        }
    }

}
