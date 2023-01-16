package com.quartyom.game_elements;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.quartyom.LayThePath;
import com.quartyom.screens.Level.BoardDrawer;
import com.quartyom.screens.Level.Gameplay;
import com.quartyom.screens.Level.MoveResult;

public abstract class GameBoard {
    public final LayThePath game;

    public BoardDrawer boardDrawer;

    protected float board_x, board_y, board_w, board_h;
    protected float square_w, square_h;
    protected float wall_offset_x, wall_offset_y;

    protected float actual_size;

    public Gameplay gameplay;

    protected Scroller scroller;
    protected PressTimer pressTimer;

    protected InputState inputState;
    protected Vector2 touch_pos;

    protected Sound move_sound, move_back_sound, body_shortened_sound, victory_sound;

    protected Vibrator vibrator;

    protected GameBoard(LayThePath game){
        this.game = game;

        gameplay = new Gameplay();
        boardDrawer = new BoardDrawer(game, gameplay);

        scroller = new Scroller(game);
        pressTimer = new PressTimer(game);

        inputState = InputState.UNTOUCHED;
        touch_pos = new Vector2();

        vibrator = game.vibrator;

        move_sound = game.soundHolder.get("low_put");
        move_back_sound = game.soundHolder.get("low_put_1");
        body_shortened_sound = game.soundHolder.get("body_shortened");
        victory_sound = game.soundHolder.get("victory");
    }

    public void resize(float topPanelHeight){
        // выбираем наименьшее расстояние (граница экрана слева или панель сверху)
        actual_size = game.HALF_HEIGHT - topPanelHeight;

        scroller.resize(-game.HALF_WIDTH, -actual_size , game.WIDTH, 2 * actual_size);
        pressTimer.resize(-game.HALF_WIDTH, -actual_size , game.WIDTH, 2 * actual_size);

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

    public void show(){
        gameplay.head_is_captured = false;
    }

    public void update(){
        if (game.userData.abstract_input_is_on){
            abstract_update();
        }
        else {
            touch_update();
        }
    }

    public abstract void victory_action();

    public void touch_update() {
        if (game.isTouched()) {
            touch_pos.x = game.touch_pos.x;
            touch_pos.y = game.touch_pos.y;

            if (touch_pos.x > board_x && touch_pos.y > board_y && touch_pos.x < board_x + board_w && touch_pos.y < board_y + board_h) {
                inputState = InputState.TOUCHED;
                touch_pos.x = (touch_pos.x - board_x) / square_w;
                touch_pos.y = (touch_pos.y - board_y) / square_h;

                MoveResult result;
                // если нажатие произошло только что, нужно захватить голову
                if (game.inputState == InputState.JUST_TOUCHED) {
                    result = gameplay.just_touched_make_move((int) touch_pos.x, (int) touch_pos.y);
                } else {
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
                        move_sound.play(game.userData.volume);
                        break;
                    case MOVE_BACK:
                        gameplay.false_path.clear();
                        move_back_sound.play(game.userData.volume);
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
        // палец только убрали, НО палец до этого касался доски
        else if (inputState == InputState.TOUCHED){
            inputState = InputState.UNTOUCHED;

            MoveResult result = gameplay.just_untouched_make_move((int) touch_pos.x, (int) touch_pos.y);
            if (result == MoveResult.VICTORY){
                victory_sound.play(game.userData.volume);   // потому что действие при победе более ресурсозатратное
                victory_action();
            }
            else if (result == MoveResult.BODY_IS_SHORTENED){
                gameplay.false_path.clear();
                body_shortened_sound.play(game.userData.volume);
            }
        }

    }

    public void abstract_update(){
        scroller.update();
        pressTimer.update();

        float sensitivity = actual_size * 2 / 5;

        if (pressTimer.handle_double_tap()){
            pressTimer.reset();
            MoveResult result = gameplay.double_tap_make_move();
            if (result == MoveResult.HEAD_IS_NOT_SET){
                if (gameplay.false_path.isEmpty()){
                    vibrator.vibrate(150);
                }
                gameplay.false_path.add(new Vector2((int) gameplay.abstract_input_cursor.x, (int) gameplay.abstract_input_cursor.y));
            }
            else if (result == MoveResult.BODY_IS_SHORTENED){
                gameplay.false_path.clear();
                body_shortened_sound.play(game.userData.volume);
            }
            scroller.inputState = InputState.UNTOUCHED; // чтобы не было одновременно слайда и тапа
            return;
        }

        if (scroller.inputState == InputState.JUST_TOUCHED){
            scroller.value.x = gameplay.abstract_input_cursor.x * sensitivity;
            scroller.value.y = gameplay.abstract_input_cursor.y * sensitivity;
        }

        else if (scroller.inputState == InputState.TOUCHED){

            if (scroller.value.x < 0){
                scroller.value.x = 0;
            }
            else if (Math.round(scroller.value.x / sensitivity) >= gameplay.field_size){
                scroller.value.x = (gameplay.field_size - 1) * sensitivity;
            }

            if (scroller.value.y < 0){
                scroller.value.y = 0;
            }
            else if (Math.round(scroller.value.y / sensitivity) >= gameplay.field_size){
                scroller.value.y = (gameplay.field_size - 1) * sensitivity;
            }

            // выравнивание вдоль осей
            if (gameplay.abstract_input_cursor.x != Math.round(scroller.value.x / sensitivity)){
                gameplay.abstract_input_cursor.x = Math.round(scroller.value.x / sensitivity);
                scroller.value.y = gameplay.abstract_input_cursor.y * sensitivity;
            }
            else if (gameplay.abstract_input_cursor.y != Math.round(scroller.value.y / sensitivity)){
                scroller.value.x = gameplay.abstract_input_cursor.x * sensitivity;
                gameplay.abstract_input_cursor.y = Math.round(scroller.value.y / sensitivity);
            }

            MoveResult result = gameplay.slide_touched_make_move();

            switch (result){
                // нейтральные исходы
                case NO_MOVEMENT:
                case HEAD_IS_NOT_CAPTURED:
                case OTHER_GOOD:
                    gameplay.false_path.clear();
                    break;
                // хорошие исходы
                case HEAD_IS_SET:
                case BODY_VISITED:
                case SIMPLE_MOVEMENT:
                case HEAD_IS_DESTROYED:
                case VICTORY:
                case BODY_IS_SHORTENED:
                    gameplay.false_path.clear();
                    move_sound.play(game.userData.volume);
                    break;
                case MOVE_BACK:
                    gameplay.false_path.clear();
                    move_back_sound.play(game.userData.volume);
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
                    gameplay.false_path.add(new Vector2((int) gameplay.abstract_input_cursor.x, (int) gameplay.abstract_input_cursor.y));
                    break;
                case OUT_OF_BOUNDS:
                    if (gameplay.false_path.isEmpty()){
                        vibrator.vibrate(150);
                    }
                    break;
            }
        }

        else if (scroller.inputState == InputState.JUST_UNTOUCHED){
            MoveResult result = gameplay.slide_just_untouched_make_move();
            if (result == MoveResult.VICTORY){
                victory_sound.play(game.userData.volume);   // потому что действие при победе более ресурсозатратное
                victory_action();
            }
            else if (result == MoveResult.BODY_IS_SHORTENED){
                gameplay.false_path.clear();
                body_shortened_sound.play(game.userData.volume);
            }

        }
    }
}
