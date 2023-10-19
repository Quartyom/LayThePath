package com.quartyom.screens.EasterColorsGame;

import com.badlogic.gdx.Gdx;
import com.quartyom.LayThePath;
import com.quartyom.game_elements.QuScreen;

public class EasterScreen extends QuScreen {
    final LayThePath game;

    EasterTopPanel easterTopPanel;
    EasterBottomPanel easterBottomPanel;
    EasterBoard easterBoard;

    public EasterScreen(final LayThePath game) {
        this.game = game;

        easterTopPanel = new EasterTopPanel(this);
        easterBottomPanel = new EasterBottomPanel(this);
        easterBoard = new EasterBoard(game);

    }

    @Override
    public void show() {
        Gdx.gl20.glClearColor(0.25f, 0.25f, 0.25f, 1);
    }

    @Override
    public void render(float delta) {
        Gdx.gl20.glClear(Gdx.gl20.GL_COLOR_BUFFER_BIT);

        easterBoard.draw();
        easterTopPanel.draw();
        easterBottomPanel.draw();

        easterBoard.update();
        easterTopPanel.update();

        if (game.isBackButtonPressed) {
            game.isBackButtonPressed = false;
            game.setScreen("menu");
        }

    }

    @Override
    public void resize(int width, int height) {
        easterTopPanel.resize();
        easterBottomPanel.resize();
        easterBoard.resize(easterTopPanel.getHeight());
    }

}
