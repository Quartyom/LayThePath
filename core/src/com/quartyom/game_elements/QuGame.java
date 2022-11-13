package com.quartyom.game_elements;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

public abstract class QuGame implements ApplicationListener, InputProcessor {

    protected QuScreen screen;
    protected QuScreen default_screen;
    private Map<String, QuScreen> screens;

    public QuGame(){
        screens = new HashMap<>();
    }

    @Override
    public void dispose () {
        for (Screen item: screens.values()){
            item.dispose();
        }
        screens.clear();
    }

    public void setScreen(String name){
        QuScreen screen = screens.get(name);
        if (this.screen != null){ this.screen.hide(); }
        if (screen != null) {
            this.screen = screen;
        }
        else if (default_screen != null) {
            this.screen = default_screen;
            System.out.println("Screen " + name + " is not found: set default Screen");
        }
        else {
            System.out.println("Screen " + name + " is not found, default Screen is not set");
            return;
        }
        this.screen.show();
        this.screen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public Screen getScreen () {
        return screen;
    }

    public void add(String name, QuScreen screen){
        if (screens.containsKey(name)){
            screens.get(name).dispose();
            System.out.println("rewritten: Screen " + name);
        }

        screens.put(name, screen);
    }

    public void add_default(String name, QuScreen screen){
        add(name, screen);
        default_screen = screen;
    }

    @Override
    public void pause () {
        if (screen != null) screen.pause();
    }

    @Override
    public void resume () {
        if (screen != null) screen.resume();
    }

    @Override
    public void render () {
        if (screen != null) screen.render(Gdx.graphics.getDeltaTime());
    }

    @Override
    public void resize (int width, int height) {
        if (screen != null) screen.resize(width, height);
    }

    @Override
    public boolean keyDown(int i) { return false; }

    @Override
    public boolean keyUp(int i) {
        return false;
    }

    @Override
    public boolean keyTyped(char c) {
        return false;
    }

    @Override
    public boolean touchDown(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchUp(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchDragged(int i, int i1, int i2) {
        return false;
    }

    @Override
    public boolean mouseMoved(int i, int i1) {
        return false;
    }

    @Override
    public boolean scrolled(float v, float v1) {
        return false;
    }

}
