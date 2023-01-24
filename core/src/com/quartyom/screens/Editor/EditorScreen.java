package com.quartyom.screens.Editor;

import com.badlogic.gdx.Gdx;
import com.quartyom.LayThePath;
import com.quartyom.game_elements.QuScreen;

/*
по умолчанию открывается EditorBoard, EditorBottomPanel, они могут вызывать слайдэр, расставлять препятствия, менять размер поля
при нажатии на Transform, открывается TransformBottomPanel, может поворачивать поле
при нажатии на запуск открывается EditorLaunchedBoard, EditorLaunchedBottomPanel с новой конфигурацией
при нажатии на Transform, открывается TransformBottomPanel
 */

public class EditorScreen extends QuScreen {
    final LayThePath game;

    EditorTopPanel editorTopPanel;
    EditorLaunchedTopPanel editorLaunchedTopPanel;
    EditorBottomPanel editorBottomPanel;
    EditorTransformBottomPanel editorTransformBottomPanel;
    EditorLaunchedBottomPanel editorLaunchedBottomPanel;
    EditorBoard editorBoard;
    EditorLaunchedBoard editorLaunchedBoard;

    final int MAX_FIELD_SIZE = 10;
    final int MIN_FILED_SIZE = 1;

    boolean isSliderActive = false;

    public EditorScreen(final LayThePath game) {
        this.game = game;

        editorTopPanel = new EditorTopPanel(this);
        editorLaunchedTopPanel = new EditorLaunchedTopPanel(this);
        editorBottomPanel = new EditorBottomPanel(this);
        editorTransformBottomPanel = new EditorTransformBottomPanel(this);
        editorLaunchedBottomPanel = new EditorLaunchedBottomPanel(this);
        editorBoard = new EditorBoard(this);
        editorLaunchedBoard = new EditorLaunchedBoard(this);

    }

    @Override
    public void show() {
        Gdx.gl20.glClearColor(0.25f, 0.25f, 0.25f, 1);
        editorLaunchedBoard.show();
    }

    @Override
    public void render(float delta) {
        Gdx.gl20.glClear(Gdx.gl20.GL_COLOR_BUFFER_BIT);

        editorBoard.draw();
        editorLaunchedBoard.draw();
        editorTopPanel.draw();
        editorLaunchedTopPanel.draw();
        editorBottomPanel.draw();
        editorTransformBottomPanel.draw();
        editorLaunchedBottomPanel.draw();

        editorLaunchedBoard.update();
        editorTransformBottomPanel.update();
        editorLaunchedBottomPanel.update();
        editorTopPanel.update();
        editorLaunchedTopPanel.update();
        editorBottomPanel.update();
        editorBoard.update();   // обновляется после нижней панели

        if (game.isBackButtonPressed) {
            game.isBackButtonPressed = false;
            game.setScreen("menu");
        }
    }

    @Override
    public void resize(int width, int height) {
        editorTopPanel.resize();
        editorLaunchedTopPanel.resize();
        editorBottomPanel.resize();
        editorTransformBottomPanel.resize();
        editorLaunchedBottomPanel.resize();
        editorBoard.resize();
        editorLaunchedBoard.resize();

    }

}
