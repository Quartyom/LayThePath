package com.quartyom.screens.Colors;

import com.badlogic.gdx.Gdx;
import com.quartyom.LayThePath;
import com.quartyom.game_elements.AttentionScreenWithBackButton;
import com.quartyom.game_elements.QuScreen;

public class ColorsScreen extends QuScreen {
    final LayThePath game;

    ColorsTopPanel colorsTopPanel;
    ColorsBottomPanel colorsBottomPanel;
    ColorsTransformBottomPanel colorsTransformBottomPanel;
    ColorsBoard colorsBoard;

    public ColorsScreen(final LayThePath game) {
        this.game = game;

        game.add("colors_level_skip", new AttentionScreenWithBackButton(game,
                "skip_level", "colors_level"));
        game.add("colors_levels_are_over", new ColorsLevelsAreOver(this));

        colorsTopPanel = new ColorsTopPanel(this);
        colorsBottomPanel = new ColorsBottomPanel(this);
        colorsTransformBottomPanel = new ColorsTransformBottomPanel(this);
        colorsBoard = new ColorsBoard(this);

    }

    @Override
    public void show() {
        Gdx.gl20.glClearColor(0.25f, 0.25f, 0.25f, 1);
        colorsBoard.show();

        if (colorsBoard.currentColorsLevel > colorsBoard.howManyColorsLevels) {
            game.setScreen("colors_levels_are_over");
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl20.glClear(Gdx.gl20.GL_COLOR_BUFFER_BIT);

        colorsBoard.draw();
        colorsTopPanel.draw();
        colorsBottomPanel.draw();
        colorsTransformBottomPanel.draw();

        colorsBoard.update();
        colorsTopPanel.update();
        colorsBottomPanel.update();
        colorsTransformBottomPanel.update();

        if (game.isBackButtonPressed) {
            game.isBackButtonPressed = false;
            game.setScreen("menu");
        }

    }

    @Override
    public void resize(int width, int height) {
        colorsTopPanel.resize();
        colorsBottomPanel.resize();
        colorsTransformBottomPanel.resize();
        colorsBoard.resize();
    }

}
