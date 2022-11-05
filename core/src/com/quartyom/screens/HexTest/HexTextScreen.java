package com.quartyom.screens.HexTest;

import static java.lang.Math.abs;
import static java.lang.Math.sqrt;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.quartyom.LayThePath;

public class HexTextScreen implements Screen {


    final LayThePath game;

    Vector2 touch_pos;

    TextureRegion texture;

    int grid_size = 3;
    float hex_size;

    public HexTextScreen(final LayThePath game) {
        this.game = game;

        touch_pos = new Vector2();

        texture = game.field_atlas.findRegion("full_hex");


    }

    @Override
    public void show() {
        Gdx.gl20.glClearColor(0.25f, 0.25f, 0.25f, 1);
    }

    @Override
    public void render(float delta) {
        Gdx.gl20.glClear(Gdx.gl20.GL_COLOR_BUFFER_BIT);

        for (int x = -grid_size; x <= grid_size; x++){
            for (int y = -grid_size; y<=grid_size; y++){
                if (dist(new Vector2(x,y)) >= grid_size){continue;}
                Vector2 pix = hex_to_pixel(new Vector2(x,y));
                game.batch.draw(texture, (float)(pix.x-hex_size), (float)(pix.y-sqrt(3)/2*hex_size),2 * hex_size, (float)sqrt(3)*hex_size);
            }
        }

        if (Gdx.input.justTouched()){
            touch_pos.x = Gdx.input.getX() - game.HALF_WIDTH;
            touch_pos.y = game.HALF_HEIGHT - Gdx.input.getY();
            System.out.println(pixel_to_hex(touch_pos));
        }

    }

    @Override
    public void resize(int width, int height) {
        game.WIDTH = width;
        game.HALF_WIDTH = game.WIDTH / 2;
        game.HEIGHT = height;
        game.HALF_HEIGHT = game.HEIGHT / 2;

        game.batch.getProjectionMatrix().setToOrtho2D(-game.HALF_WIDTH, -game.HALF_HEIGHT, game.WIDTH, game.HEIGHT);

        //hex_size = game.WIDTH / (grid_size * 2 + 1) / (float)sqrt(3);
        hex_size = game.WIDTH / ((grid_size - 1) * 3 + 2);

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

    Vector2 hex_to_pixel(Vector2 hex){
        float x = hex_size * 3/2 * hex.x;
        float y = (float) (hex_size * sqrt(3) * (hex.y + hex.x/2));
        return new Vector2(x,y);
    }

    Vector2 pixel_to_hex(Vector2 pixel){
        int q = (int)Math.round(pixel.x / 3*2 / hex_size);
        int r = (int)Math.round((-pixel.x / 3 + sqrt(3)/3 * pixel.y) / hex_size);
        //int q = (int)Math.floor(pixel.x / 3*2 / hex_size);
        //int r = (int)Math.floor((-pixel.x / 3 + sqrt(3)/3 * pixel.y) / hex_size);
        return new Vector2(q, r);
    }

    float dist(Vector2 hex){
        return (abs(hex.x) + abs(hex.y) + abs(0 - hex.x - hex.y)) / 2;
    }



}
