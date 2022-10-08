package com.quartyom.screens.Level;


import com.badlogic.gdx.math.Vector2;
import java.util.ArrayList;

// хранит состояние уровня
public class LevelConfiguration {
    public int field_size;
    public ArrayList<Vector2> vertical_walls, horizontal_walls, slash_walls, backslash_walls, boxes, points, crossroads;
    public ArrayList<Vector2> hint;

    public void set_empty(){
        vertical_walls = new ArrayList<Vector2>();
        horizontal_walls = new ArrayList<Vector2>();
        slash_walls = new ArrayList<Vector2>();
        backslash_walls = new ArrayList<Vector2>();
        boxes = new ArrayList<Vector2>();
        points = new ArrayList<Vector2>();
        crossroads = new ArrayList<Vector2>();
        hint = new ArrayList<Vector2>();
    }
}
