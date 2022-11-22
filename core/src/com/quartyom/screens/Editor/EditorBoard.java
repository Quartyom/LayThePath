package com.quartyom.screens.Editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.quartyom.LayThePath;
import com.quartyom.game_elements.InputState;
import com.quartyom.screens.Level.Gameplay;
import com.quartyom.screens.Level.LevelConfiguration;
import com.quartyom.screens.Level.BoardDrawer;

import java.util.ArrayList;


public class EditorBoard {
    public boolean is_active = true;
    private boolean is_erasing = false; // or drawing

    public final LayThePath game;
    public final EditorScreen editorScreen;

    private float board_x, board_y, board_w, board_h;
    private float square_w, square_h;
    private float wall_offset_x, wall_offset_y;

    private float actual_size;

    Gameplay gameplay;
    BoardDrawer boardDrawer;

    Vector2 touch_pos;

    Sound put_sound, unput_sound;

    String obstacles_to_put[] = {"vertical wall", "horizontal wall", "slash wall", "backslash wall", "box", "point", "crossroad", "eraser"};
    int cursor_on_obstacles = 0;

    public EditorBoard(EditorScreen editorScreen){
        this.editorScreen = editorScreen;
        this.game = editorScreen.game;

        gameplay = new Gameplay();
        boardDrawer = new BoardDrawer(game, gameplay);
        boardDrawer.is_abstract_cursor_visible = false;

        touch_pos = new Vector2();

        put_sound = game.soundHolder.get("low_put");
        unput_sound = game.soundHolder.get("low_put_1");

        LevelConfiguration levelConfiguration = new LevelConfiguration();
        levelConfiguration.field_size = 5;
        gameplay.set_level_configuration(levelConfiguration);

    }

    public void resize(){
        // выбираем наименьшее расстояние (граница экрана слева или панель сверху)
        actual_size = game.HALF_HEIGHT - editorScreen.editorTopPanel.getHeight();

        //System.out.println(actual_size + " " + levelScreen.game.HALF_WIDTH);
        if (game.HALF_WIDTH < actual_size){
            actual_size = game.HALF_WIDTH;
        }

        board_x = -actual_size;
        board_y = -actual_size;
        board_w = actual_size * 2;
        board_h = actual_size * 2;

        square_w = board_w / gameplay.field_size;
        square_h = board_h / gameplay.field_size;

        // толщина стены 2 / 8 px
        wall_offset_x = square_w / 16.0f;
        wall_offset_y = square_h / 16.0f;

        boardDrawer.board_x = board_x;
        boardDrawer.board_y = board_y;
        boardDrawer.board_w = board_w;
        boardDrawer.board_h = board_h;

        boardDrawer.square_w = square_w;
        boardDrawer.square_h = square_h;

        boardDrawer.wall_offset_x = wall_offset_x;
        boardDrawer.wall_offset_y = wall_offset_y;

    }

    public void draw(){
        if (!is_active) { return;}
        boardDrawer.draw();
    }

    private void just_touched_change_cell(ArrayList<Vector2> arr) {
        if (arr.contains(touch_pos)) {
            is_erasing = true;
            arr.remove(touch_pos);
            unput_sound.play(game.userData.volume);
        }
        else {
            is_erasing = false;
            arr.add(new Vector2(touch_pos));
            put_sound.play(game.userData.volume);
        }
    }

    private void touched_change_cell(ArrayList<Vector2> arr) {
        if (is_erasing) {
            if (arr.contains(touch_pos)) {
                arr.remove(touch_pos);
                unput_sound.play(game.userData.volume);
            }
        }
        else if (!arr.contains(touch_pos)) {
            arr.add(new Vector2(touch_pos));
            put_sound.play(game.userData.volume);
        }

    }

    private void erase() {
        gameplay.vertical_walls.remove(touch_pos);
        gameplay.horizontal_walls.remove(touch_pos);
        gameplay.slash_walls.remove(touch_pos);
        gameplay.backslash_walls.remove(touch_pos);
        gameplay.boxes.remove(touch_pos);
        gameplay.points.remove(touch_pos);
        gameplay.crossroads.remove(touch_pos);
    }

    public void update() {
        if (!is_active) {
            return;
        }

        if (game.isTouched()) {
            touch_pos.x = game.touch_pos.x;
            touch_pos.y = game.touch_pos.y;

            if (touch_pos.x > board_x && touch_pos.y > board_y && touch_pos.x < board_x + board_w && touch_pos.y < board_y + board_h) {

                touch_pos.x = (int) ((touch_pos.x - board_x) / square_w);
                touch_pos.y = (int) ((touch_pos.y - board_y) / square_h);

                if (game.inputState == InputState.JUST_TOUCHED) {
                    switch (cursor_on_obstacles) {
                        case 0: // vertical
                            just_touched_change_cell(gameplay.vertical_walls);
                            break;
                        case 1: // horizontal
                            just_touched_change_cell(gameplay.horizontal_walls);
                            break;
                        case 2: //slash
                            just_touched_change_cell(gameplay.slash_walls);
                            break;
                        case 3: // backslash
                            just_touched_change_cell(gameplay.backslash_walls);
                            break;
                        case 4: // box
                            just_touched_change_cell(gameplay.boxes);
                            break;
                        case 5: // point
                            just_touched_change_cell(gameplay.points);
                            break;
                        case 6: // crossroad
                            just_touched_change_cell(gameplay.crossroads);
                            break;
                        case 7: // eraser
                            erase();
                            break;
                    }
                }
                else {
                    switch (cursor_on_obstacles) {
                        case 0: // vertical
                            touched_change_cell(gameplay.vertical_walls);
                            break;
                        case 1: // horizontal
                            touched_change_cell(gameplay.horizontal_walls);
                            break;
                        case 2: //slash
                            touched_change_cell(gameplay.slash_walls);
                            break;
                        case 3: // backslash
                            touched_change_cell(gameplay.backslash_walls);
                            break;
                        case 4: // box
                            touched_change_cell(gameplay.boxes);
                            break;
                        case 5: // point
                            touched_change_cell(gameplay.points);
                            break;
                        case 6: // crossroad
                            touched_change_cell(gameplay.crossroads);
                            break;
                        case 7: // eraser
                            erase();
                            break;
                    }

                }

            }
        }
    }

}
