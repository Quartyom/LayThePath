package com.quartyom.game_elements;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class QuSpriteBatch extends SpriteBatch {

    public void draw(TextureRegion region, float x, float y, float width, float height, Color color) {
        super.setColor(color);
        super.draw(region, x, y, width, height);
        super.setColor(Color.WHITE);
    }
}
