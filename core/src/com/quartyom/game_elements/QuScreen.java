package com.quartyom.game_elements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

public abstract class QuScreen implements Screen {

    //@Override
    //public void render(float delta) {}

    @Override
    public void show() {
        Gdx.gl20.glClearColor(0, 0, 0, 1);
    }
    @Override
    public void resize(int width, int height) {}
    @Override
    public void pause() {}
    @Override
    public void resume() {}
    @Override
    public void hide() {}
    @Override
    public void dispose() {}
}
