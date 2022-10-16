package com.quartyom.screens.Editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.quartyom.game_elements.InputState;
import com.quartyom.screens.Level.Gameplay;
import com.quartyom.screens.Level.LevelConfiguration;
import com.quartyom.screens.Level.BoardDrawer;


public class EditorBoard {
    public boolean is_active = true;

    public final EditorScreen editorScreen;

    private float board_x, board_y, board_w, board_h;
    private float square_w, square_h;
    private float wall_offset_x, wall_offset_y;

    private float actual_size;

    Gameplay gameplay;
    BoardDrawer boardDrawer;
    InputState inputState;

    Vector2 touch_pos;
    Vector2 prev_touch_pos;

    Sound put_sound, unput_sound;

    String obstacles_to_put[] = {"vertical wall", "horizontal wall", "slash wall", "backslash wall", "box", "point", "crossroad", "eraser"};
    int cursor_on_obstacles = 0;

    public EditorBoard(EditorScreen editorScreen){
        this.editorScreen = editorScreen;

        gameplay = new Gameplay();
        boardDrawer = new BoardDrawer(editorScreen.game, gameplay);


        inputState = InputState.UNTOUCHED;

        touch_pos = new Vector2();
        prev_touch_pos = new Vector2();

        put_sound = editorScreen.game.soundHolder.get("low_put");
        unput_sound = editorScreen.game.soundHolder.get("low_put_1");

        LevelConfiguration levelConfiguration = new LevelConfiguration();
        levelConfiguration.set_empty();
        levelConfiguration.field_size = 5;
        gameplay.set_level_configuration(levelConfiguration);

    }

    void resize(){
        // выбираем наименьшее расстояние (граница экрана слева или панель сверху)
        actual_size = editorScreen.game.HALF_HEIGHT - editorScreen.editorTopPanel.getHeight();

        //System.out.println(actual_size + " " + levelScreen.game.HALF_WIDTH);
        if (editorScreen.game.HALF_WIDTH < actual_size){
            actual_size = editorScreen.game.HALF_WIDTH;
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

    void draw(){
        if (!is_active) { return;}
        boardDrawer.draw();
    }


    public void update() {
        if (!is_active) {
            return;
        }
        if (editorScreen.editorBottomPanel.slider.inputState == InputState.TOUCHED) {
            return;
        }

        if (Gdx.input.isTouched()) {
            touch_pos.x = Gdx.input.getX() - editorScreen.game.HALF_WIDTH;
            touch_pos.y = editorScreen.game.HALF_HEIGHT - Gdx.input.getY();

            if (touch_pos.x >= board_x && touch_pos.y >= board_y && touch_pos.x <= board_x + board_w && touch_pos.y <= board_y + board_h) {
                //System.out.println("cat");
                touch_pos.x = (int) ((touch_pos.x - board_x) / square_w);
                touch_pos.y = (int) ((touch_pos.y - board_y) / square_h);
                //System.out.println(touch_pos);

                if (touch_pos.equals(prev_touch_pos)) {
                    // do nothing
                }
                else {
                    inputState = InputState.TOUCHED;
                    prev_touch_pos.x = touch_pos.x;
                    prev_touch_pos.y = touch_pos.y;
                    switch (cursor_on_obstacles) {
                        // vertical
                        case 0:
                            if (gameplay.vertical_walls.contains(touch_pos)) {
                                gameplay.vertical_walls.remove(touch_pos);
                                unput_sound.play(editorScreen.game.userData.volume);
                            } else {
                                gameplay.vertical_walls.add(new Vector2(touch_pos));
                                put_sound.play(editorScreen.game.userData.volume);
                            }
                            break;
                        // horizontal
                        case 1:
                            if (gameplay.horizontal_walls.contains(touch_pos)) {
                                gameplay.horizontal_walls.remove(touch_pos);
                                unput_sound.play(editorScreen.game.userData.volume);
                            } else {
                                gameplay.horizontal_walls.add(new Vector2(touch_pos));
                                put_sound.play(editorScreen.game.userData.volume);
                            }
                            break;
                        //slash
                        case 2:
                            if (gameplay.slash_walls.contains(touch_pos)) {
                                gameplay.slash_walls.remove(touch_pos);
                                unput_sound.play(editorScreen.game.userData.volume);
                            } else {
                                gameplay.slash_walls.add(new Vector2(touch_pos));
                                put_sound.play(editorScreen.game.userData.volume);
                            }
                            break;
                        // backslash
                        case 3:
                            if (gameplay.backslash_walls.contains(touch_pos)) {
                                gameplay.backslash_walls.remove(touch_pos);
                                unput_sound.play(editorScreen.game.userData.volume);
                            } else {
                                gameplay.backslash_walls.add(new Vector2(touch_pos));
                                put_sound.play(editorScreen.game.userData.volume);
                            }
                            break;
                        // box
                        case 4:
                            if (gameplay.boxes.contains(touch_pos)) {
                                gameplay.boxes.remove(touch_pos);
                                unput_sound.play(editorScreen.game.userData.volume);
                            } else {
                                gameplay.boxes.add(new Vector2(touch_pos));
                                put_sound.play(editorScreen.game.userData.volume);
                            }
                            break;
                        // point
                        case 5:
                            if (gameplay.points.contains(touch_pos)) {
                                gameplay.points.remove(touch_pos);
                                unput_sound.play(editorScreen.game.userData.volume);
                            } else {
                                gameplay.points.add(new Vector2(touch_pos));
                                put_sound.play(editorScreen.game.userData.volume);
                            }
                            break;
                        case 6:
                            if (gameplay.crossroads.contains(touch_pos)) {
                                gameplay.crossroads.remove(touch_pos);
                                unput_sound.play(editorScreen.game.userData.volume);
                            } else {
                                gameplay.crossroads.add(new Vector2(touch_pos));
                                put_sound.play(editorScreen.game.userData.volume);
                            }
                            break;
                        case 7:
                            gameplay.vertical_walls.remove(touch_pos);
                            gameplay.horizontal_walls.remove(touch_pos);
                            gameplay.slash_walls.remove(touch_pos);
                            gameplay.backslash_walls.remove(touch_pos);
                            gameplay.boxes.remove(touch_pos);
                            gameplay.points.remove(touch_pos);
                            gameplay.crossroads.remove(touch_pos);
                            break;
                    }

                }
            }
        }
        // палец только убрали
        else if (inputState == InputState.TOUCHED || inputState == InputState.JUST_TOUCHED){
            inputState = InputState.JUST_UNTOUCHED;
            prev_touch_pos.x = -1000; // чтобы не было совпадения
        }
        // палец убрали давно либо не ставили вовсе
        else {
            inputState = InputState.UNTOUCHED;
        }
    }

}
