package com.quartyom.screens.Zen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.quartyom.MakeTheWay;

public class ZenScreen implements Screen {
    final MakeTheWay game;

    ZenTopPanel zenTopPanel;
    ZenBottomPanel zenBottomPanel;
    ZenTransformBottomPanel zenTransformBottomPanel;
    ZenBoard zenBoard;


    public ZenScreen(final MakeTheWay game){
        this.game = game;

        game.add("zen_hint", new ZenHintTab(this));
        game.add("zen_skip", new ZenSkipTab(this));

        zenTopPanel = new ZenTopPanel(this);
        zenBottomPanel = new ZenBottomPanel(this);
        zenTransformBottomPanel = new ZenTransformBottomPanel(this);
        zenBoard = new ZenBoard(this);
    }

    @Override
    public void show() {
        Gdx.gl20.glClearColor(0.25f, 0.25f, 0.25f, 1);
    }

    @Override
    public void render(float delta) {
        Gdx.gl20.glClear(Gdx.gl20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();

        zenBoard.draw();
        zenTopPanel.draw();
        zenBottomPanel.draw();
        zenTransformBottomPanel.draw();

        game.batch.end();

        zenBoard.update();
        zenTopPanel.update();
        zenBottomPanel.update();
        zenTransformBottomPanel.update();

        if (game.is_back_button_pressed){
            game.is_back_button_pressed = false;
            game.setScreen("menu");
        }
    }

    @Override
    public void resize(int width, int height) {
        zenTopPanel.resize();
        zenBottomPanel.resize();
        zenTransformBottomPanel.resize();
        zenBoard.resize();

        /*int size = (int)((0.5f / 4 / 3) * game.HEIGHT);

        font = game.fontHolder.get(size);

        int HOW_MANY_BUTTONS = 6;

        float padding = 0.8f;

        float down_margin = game.HEIGHT / HOW_MANY_BUTTONS;


        float button_actual_size_x = game.HALF_WIDTH * padding;
        float button_actual_size_y = down_margin / 2 * padding;

        float upper_button_center_x = 0;
        float upper_button_center_y = game.HALF_HEIGHT - down_margin / 2;


        float upper_button_corner_x = upper_button_center_x - button_actual_size_x;
        float upper_button_corner_y = upper_button_center_y - button_actual_size_y;

        float button_w = button_actual_size_x * 2;
        float button_h = button_actual_size_y * 2;*/

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
