package com.quartyom.screens.ColorsTest;

import com.badlogic.gdx.math.Vector2;
import com.quartyom.game_elements.ColorsSource;

import java.util.ArrayList;

// хранит состояние уровня, используется для json
public class LevelColorsConfiguration {
    public int field_size;
    public ArrayList<Vector2> vertical_walls, horizontal_walls, slash_walls, backslash_walls, boxes, points, crossroads;
    public ArrayList<ColorsSource> colors_sources;

    public LevelColorsConfiguration() {
        vertical_walls = new ArrayList<>();
        horizontal_walls = new ArrayList<>();
        slash_walls = new ArrayList<>();
        backslash_walls = new ArrayList<>();
        boxes = new ArrayList<>();
        points = new ArrayList<>();
        crossroads = new ArrayList<>();
        colors_sources = new ArrayList<>();
    }

    public LevelColorsConfiguration(LevelColorsConfiguration other){
        field_size = other.field_size;
        vertical_walls = copyArrayListOfVector(other.vertical_walls);
        horizontal_walls = copyArrayListOfVector(other.horizontal_walls);
        slash_walls = copyArrayListOfVector(other.slash_walls);
        backslash_walls = copyArrayListOfVector(other.backslash_walls);
        boxes = copyArrayListOfVector(other.boxes);
        points = copyArrayListOfVector(other.points);
        crossroads = copyArrayListOfVector(other.crossroads);
        colors_sources = copyArrayListOfColors(other.colors_sources);
    }

    // лучше сделать обобщение
    private ArrayList<Vector2> copyArrayListOfVector(ArrayList<Vector2> source){
        ArrayList<Vector2> result = new ArrayList<>();
        for (Vector2 item : source){
            result.add(new Vector2(item));
        }
        return result;
    }

    private ArrayList<ColorsSource> copyArrayListOfColors(ArrayList<ColorsSource> source){
        ArrayList<ColorsSource> result = new ArrayList<>();
        for (ColorsSource item : source){
            result.add(new ColorsSource(item));
        }
        return result;
    }


    public void setEmpty() {
        vertical_walls.clear();
        horizontal_walls.clear();
        slash_walls.clear();
        backslash_walls.clear();
        boxes.clear();
        points.clear();
        crossroads.clear();
        colors_sources.clear();
    }

}
