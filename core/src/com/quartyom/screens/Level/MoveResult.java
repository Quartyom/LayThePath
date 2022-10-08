package com.quartyom.screens.Level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import java.util.Collections;

public enum MoveResult {
    HEAD_IS_SET,
    HEAD_IS_NOT_SET,

    OUT_OF_BOUNDS,
    MOVE_INTO_BOX,
    NO_MOVEMENT,
    MOVE_BACK,
    NOT_A_NEIGHBOR,
    MOVE_THROUGH_SLASH_WALL,
    MOVE_THROUGH_BACKSLASH_WALL,
    MOVE_THROUGH_POINT,
    MOVE_THROUGH_CROSSROAD,
    MOVE_THROUGH_VERTICAL_WALL,
    MOVE_THROUGH_HORIZONTAL_WALL,
    BODY_NOT_VISITED,
    BODY_VISITED,
    SIMPLE_MOVEMENT,

    HEAD_IS_DESTROYED,
    VICTORY,

    OTHER_GOOD,
    OTHER_BAD,
    HEAD_IS_NOT_CAPTURED,

    BODY_IS_SHORTENED
}



