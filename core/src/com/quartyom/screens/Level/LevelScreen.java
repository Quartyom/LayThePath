package com.quartyom.screens.Level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.quartyom.MakeTheWay;

public class LevelScreen implements Screen {

    final MakeTheWay game;

    LevelTopPanel levelTopPanel;
    LevelBottomPanel levelBottomPanel;
    LevelTransformBottomPanel levelTransformBottomPanel;
    LevelBoard levelBoard;

    public LevelScreen(final MakeTheWay game) {
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

        if (levelBoard.current_level > levelBoard.how_many_levels){
            game.setScreen("levels_are_over");
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl20.glClear(Gdx.gl20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();

        levelBoard.draw();
        levelTopPanel.draw();
        levelBottomPanel.draw();
        levelTransformBottomPanel.draw();

        game.batch.end();

        levelBoard.update();
        //levelBoard.abstract_update();
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

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
