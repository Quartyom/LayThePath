package com.quartyom.game_elements;

import com.badlogic.gdx.math.Vector2;

public class ColorsSource {
    public Vector2 pos;
    public int color_id;

    public ColorsSource(){}

    public ColorsSource(ColorsSource other){
        this.pos = new Vector2(other.pos);
        this.color_id = other.color_id;
    }
}
