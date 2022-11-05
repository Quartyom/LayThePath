package com.quartyom.screens.Level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.quartyom.LayThePath;
import com.quartyom.game_elements.QuScreen;

public class LevelScreen extends QuScreen {

    final LayThePath game;

    LevelTopPanel levelTopPanel;
    LevelBottomPanel levelBottomPanel;
    LevelTransformBottomPanel levelTransformBottomPanel;
    LevelBoard levelBoard;

    public LevelScreen(final LayThePath game) {
        this.game = game;

        game.add("level_skip", new LevelSkipTab(this));
        game.add("level_hint", new LevelHintTab(this));
        game.add("levels_are_over", new LevelsAreOver(this));

        levelTopPanel = new LevelTopPanel(this);
        levelBottomPanel = new LevelBottomPanel(this);
        levelTransformBottomPanel = new LevelTransformBottomPanel(this);
        levelBoard = new LevelBoard(this);

    }

    @Override
    public void show() {
        Gdx.gl20.glClearColor(0.25f, 0.25f, 0.25f, 1);
        levelBoard.show();

        if (levelBoard.current_level > levelBoard.how_many_levels){
            game.setScreen("levels_are_over");
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl20.glClear(Gdx.gl20.GL_COLOR_BUFFER_BIT);

        levelBoard.draw();
        levelTopPanel.draw();
        levelBottomPanel.draw();
        levelTransformBottomPanel.draw();

        levelBoard.update();
        levelTopPanel.update();
        levelBottomPanel.update();
        levelTransformBottomPanel.update();

        if (game.is_back_button_pressed){
            game.is_back_button_pressed = false;
            game.setScreen("menu");
        }

    }

    @Override
    public void resize(int width, int height) {
        levelTopPanel.resize();
        levelBottomPanel.resize();
        levelTransformBottomPanel.resize();
        levelBoard.resize();
    }

}
