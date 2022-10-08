package com.quartyom.screens.Zen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.quartyom.UserData;
import com.quartyom.game_elements.InputState;
import com.quartyom.game_elements.TextureHolder;
import com.quartyom.game_elements.Vibrator;
import com.quartyom.screens.Level.BoardDrawer;
import com.quartyom.screens.Level.Gameplay;
import com.quartyom.screens.Level.LevelsData;
import com.quartyom.screens.Level.MoveResult;

import java.util.Random;


public class ZenBoard {
    public final ZenScreen zenScreen;

    ZenLevelGenerator zenLevelGenerator;

    BoardDrawer boardDrawer;

    private float board_x, board_y, board_w, board_h;
    private float square_w, square_h;
    private float wall_offset_x, wall_offset_y;

    private float actual_size;

    Gameplay gameplay;
    InputState inputState;

    Vector2 touch_pos;

    Sound move_sound, move_back_sound, body_shortened_sound, victory_sound;

    Vibrator vibrator;

    public int current_level;   // Влияет только на визуал верхней панэли


    public ZenBoard(ZenScreen zenScreen) {
        this.zenScreen = zenScreen;

        zenLevelGenerator = new ZenLevelGenerator();

        gameplay = new Gameplay();
        boardDrawer = new BoardDrawer(zenScreen.game, gameplay);


        inputState = InputState.UNTOUCHED;

        touch_pos = new Vector2();

        move_sound = zenScreen.game.soundHolder.get("low_put");
        move_back_sound = zenScreen.game.soundHolder.get("low_put_1");
        body_shortened_sound = zenScreen.game.soundHolder.get("body_shortened");
        victory_sound = zenScreen.game.soundHolder.get("victory");

        vibrator = zenScreen.game.vibrator;

        current_level = 1;
        load_level();

    }

    public void load_level() {
        boardDrawer.is_hint_shown = false;
        gameplay.set_level_configuration(zenLevelGenerator.generate_level());
        resize();
    }

    void resize() {
        // выбираем наименьшее расстояние (граница экрана слева или панель сверху)
        actual_size = zenScreen.game.HALF_HEIGHT - zenScreen.zenTopPanel.getHeight();

        //System.out.println(actual_size + " " + levelScreen.game.HALF_WIDTH);
        if (zenScreen.game.HALF_WIDTH < actual_size) {
            actual_size = zenScreen.game.HALF_WIDTH;
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

    void draw() {

        boardDrawer.draw();
    }

    public void update() {
        if (Gdx.input.isTouched()) {
            touch_pos.x = Gdx.input.getX() - zenScreen.game.HALF_WIDTH;
            touch_pos.y = zenScreen.game.HALF_HEIGHT - Gdx.input.getY();

            if (touch_pos.x >= board_x && touch_pos.y >= board_y && touch_pos.x <= board_x + board_w && touch_pos.y <= board_y + board_h) {
                //System.out.println("cat");
                touch_pos.x = (touch_pos.x - board_x) / square_w;
                touch_pos.y = (touch_pos.y - board_y) / square_h;
                //System.out.println(touch_pos);

                MoveResult result;
                // если нажатие произошло только что, нужно захватить голову
                if (Gdx.input.justTouched()) {
                    inputState = InputState.JUST_TOUCHED;
                    result = gameplay.just_touched_make_move((int) touch_pos.x, (int) touch_pos.y);
                } else {
                    inputState = InputState.TOUCHED;
                    result = gameplay.touched_make_move((int) touch_pos.x, (int) touch_pos.y);
                }

                switch (result){
                    // нейтральные исходы
                    case NO_MOVEMENT:
                    case HEAD_IS_NOT_CAPTURED:
                    case OTHER_GOOD:
                        break;
                    // хорошие исходы
                    case HEAD_IS_SET:
                    case BODY_VISITED:
                    case SIMPLE_MOVEMENT:
                    case HEAD_IS_DESTROYED:
                    case VICTORY:
                    case BODY_IS_SHORTENED:
                        gameplay.false_path.clear();
                        move_sound.play(zenScreen.game.userData.volume);
                        break;
                    case MOVE_BACK:
                        gameplay.false_path.clear();
                        move_back_sound.play(zenScreen.game.userData.volume);
                        break;
                    // плохие исходы
                    case HEAD_IS_NOT_SET:
                    case MOVE_INTO_BOX:
                    case NOT_A_NEIGHBOR:
                    case MOVE_THROUGH_SLASH_WALL:
                    case MOVE_THROUGH_BACKSLASH_WALL:
                    case MOVE_THROUGH_POINT:
                    case MOVE_THROUGH_CROSSROAD:
                    case MOVE_THROUGH_VERTICAL_WALL:
                    case MOVE_THROUGH_HORIZONTAL_WALL:
                    case BODY_NOT_VISITED:
                    case OTHER_BAD:
                        if (gameplay.false_path.isEmpty()){
                            vibrator.vibrate(150);
                        }
                        gameplay.false_path.add(new Vector2((int) touch_pos.x, (int) touch_pos.y));
                        break;
                    case OUT_OF_BOUNDS:
                        if (gameplay.false_path.isEmpty()){
                            vibrator.vibrate(150);
                        }
                        break;
                }

            }
        }
        // палец только убрали
        else if (inputState == InputState.TOUCHED || inputState == InputState.JUST_TOUCHED){
            inputState = InputState.JUST_UNTOUCHED;
            MoveResult result = gameplay.just_untouched_make_move((int) touch_pos.x, (int) touch_pos.y);
            if (result == MoveResult.VICTORY){
                victory_sound.play(zenScreen.game.userData.volume);
                current_level++;

                Random random = new Random();
                if (random.nextInt(3) == 0){
                    zenScreen.game.userData.hints_amount++;
                }

                zenScreen.game.userData.zen_levels_completed++;     // статистика
                zenScreen.game.save_user_data();

                load_level();
            }
            else if (result == MoveResult.BODY_IS_SHORTENED){
                gameplay.false_path.clear();
                body_shortened_sound.play(zenScreen.game.userData.volume);
            }
        }
        // палец убрали давно либо не ставили вовсе
        else {
            inputState = InputState.UNTOUCHED;
        }

    }

}
