package com.quartyom.screens.Level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Align;
import com.quartyom.LayThePath;
import com.quartyom.game_elements.Button;
import com.quartyom.game_elements.Label;
import com.quartyom.game_elements.QuScreen;
import com.quartyom.game_elements.TextField;
import com.quartyom.interfaces.QuEvent;


public class LevelSkipTab extends QuScreen {
    final LayThePath game;
    public LevelScreen levelScreen;

    Label skipLabel;
    TextField textField;
    Button backButton;

    public LevelSkipTab(final LevelScreen levelScreen) {
        this.levelScreen = levelScreen;
        game = levelScreen.game;

        skipLabel = new Label(game, levelScreen.game.locale.get("Attention"));

        textField = new TextField(levelScreen.game, Gdx.files.internal("texts/" + game.userData.locale + "/skip_level.txt").readString());

        backButton = new Button("in_main_menu", game, new QuEvent() {
            @Override
            public void execute() {
                game.setScreen("level");
            }
        });
        backButton.setNinePatch(6).setLabel(levelScreen.game.locale.get("Back"));
    }

    @Override
    public void show() {
        Gdx.gl20.glClearColor(0, 0, 0, 1);
    }

    @Override
    public void render(float delta) {
        Gdx.gl20.glClear(Gdx.gl20.GL_COLOR_BUFFER_BIT);

        skipLabel.draw();
        textField.draw();
        backButton.draw();

        backButton.update();

        if (game.isBackButtonPressed) {
            game.isBackButtonPressed = false;
            game.setScreen("level");
        }
    }

    @Override
    public void resize(int width, int height) {
        skipLabel.resize(game.upperButtonCornerX, game.upperButtonCornerY, game.buttonW, game.buttonH, Align.center);

        int fontSize = (int) (levelScreen.game.HEIGHT * (1.0f / 32.0f));
        textField.resize(game.upperButtonCornerX, game.upperButtonCornerY - game.downMargin + game.buttonH, game.buttonW, fontSize);

        backButton.resize(game.upperButtonCornerX, game.upperButtonCornerY - game.downMargin * 5, game.buttonW, game.buttonH);
    }
}
