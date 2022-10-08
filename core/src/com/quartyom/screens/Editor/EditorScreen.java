package com.quartyom.screens.Editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.quartyom.MakeTheWay;

/*
по умолчанию открывается EditorBoard, EditorBottomPanel, они могут вызывать слайдэр, расставлять препятствия, менять размер поля
при нажатии на Transform, открывается TransformBottomPanel, может поворачивать поле
при нажатии на запуск открывается EditorLaunchedBoard, EditorLaunchedBottomPanel с новой конфигурацией
при нажатии на Transform, открывается TransformBottomPanel

 */
public class EditorScreen implements Screen {
    final MakeTheWay game;

    EditorTopPanel editorTopPanel;
    EditorLaunchedTopPanel editorLaunchedTopPanel;
    EditorBottomPanel editorBottomPanel;
    EditorTransformBottomPanel editorTransformBottomPanel;
    EditorLaunchedBottomPanel editorLaunchedBottomPanel;
    EditorBoard editorBoard;
    EditorLaunchedBoard editorLaunchedBoard;

    final int MAX_FIELD_SIZE = 10;
    final int MIN_FILED_SIZE = 1;

    boolean is_slider_active = false;
    int which_button_control_the_slider;

    public EditorScreen(final MakeTheWay game){
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
    }

    @Override
    public void render(float delta) {
        Gdx.gl20.glClear(Gdx.gl20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();

        editorBoard.draw();
        editorLaunchedBoard.draw();
        editorTopPanel.draw();
        editorLaunchedTopPanel.draw();
        editorBottomPanel.draw();
        editorTransformBottomPanel.draw();
        editorLaunchedBottomPanel.draw();

        game.batch.end();

        editorLaunchedBoard.update();
        editorTransformBottomPanel.update();
        editorLaunchedBottomPanel.update();
        editorTopPanel.update();
        editorLaunchedTopPanel.update();
        editorBottomPanel.update();
        editorBoard.update();   // обновляется после нижней панели

        if (game.is_back_button_pressed){
            game.is_back_button_pressed = false;
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
    public void dispose() {}
}
