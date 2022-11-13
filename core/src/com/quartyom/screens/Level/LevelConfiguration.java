package com.quartyom.screens.Level;

import com.badlogic.gdx.math.Vector2;
import java.util.ArrayList;
import java.util.Arrays;

// хранит состояние уровня
public class LevelConfiguration {
    public int field_size;
    public ArrayList<Vector2> vertical_walls, horizontal_walls, slash_walls, backslash_walls, boxes, points, crossroads;
    public ArrayList<Vector2> hint;

    public LevelConfiguration() { set_empty(); }

    public void set_empty(){
        vertical_walls = new ArrayList<>();
        horizontal_walls = new ArrayList<>();
        slash_walls = new ArrayList<>();
        backslash_walls = new ArrayList<>();
        boxes = new ArrayList<>();
        points = new ArrayList<>();
        crossroads = new ArrayList<>();
        hint = new ArrayList<>();
    }

}
