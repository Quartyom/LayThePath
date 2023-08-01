package com.quartyom.screens.Zen;

import com.badlogic.gdx.Gdx;
import com.quartyom.LayThePath;
import com.quartyom.game_elements.AttentionScreenWithBackButton;
import com.quartyom.game_elements.QuScreen;

public class ZenScreen extends QuScreen {

    final LayThePath game;
    ZenLevelGenerator zenLevelGenerator;
    private ZenGeneratorThread zenGeneratorThread;  // оновной поток забирает уровни, этот генерирует

    ZenTopPanel zenTopPanel;
    ZenBottomPanel zenBottomPanel;
    ZenTransformBottomPanel zenTransformBottomPanel;
    ZenBoard zenBoard;


    public ZenScreen(final LayThePath game) {
        this.game = game;
        zenLevelGenerator = new ZenLevelGenerator(game);
        zenGeneratorThread = new ZenGeneratorThread(zenLevelGenerator);
        zenGeneratorThread.start();

        game.add("zen_hint", new AttentionScreenWithBackButton(game,
                "hints_are_over", "zen"));
        game.add("zen_skip", new AttentionScreenWithBackButton(game,
                "skip_level", "zen"));

        zenTopPanel = new ZenTopPanel(this);
        zenBottomPanel = new ZenBottomPanel(this);
        zenTransformBottomPanel = new ZenTransformBottomPanel(this);
        zenBoard = new ZenBoard(this);
    }

    @Override
    public void show() {
        Gdx.gl20.glClearColor(0.25f, 0.25f, 0.25f, 1);
        zenBoard.show();
    }

    @Override
    public void render(float delta) {
        Gdx.gl20.glClear(Gdx.gl20.GL_COLOR_BUFFER_BIT);

        zenBoard.draw();
        zenTopPanel.draw();
        zenBottomPanel.draw();
        zenTransformBottomPanel.draw();

        zenBoard.update();
        zenTopPanel.update();
        zenBottomPanel.update();
        zenTransformBottomPanel.update();

        if (game.isBackButtonPressed) {
            game.isBackButtonPressed = false;
            game.setScreen("menu_classic");
        }
    }

    @Override
    public void resize(int width, int height) {
        zenTopPanel.resize();
        zenBottomPanel.resize();
        zenTransformBottomPanel.resize();
        zenBoard.resize();
    }

    @Override
    public void dispose(){
        zenGeneratorThread.interrupt();
    }

}
