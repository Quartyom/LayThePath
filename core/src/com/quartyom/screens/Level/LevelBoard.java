package com.quartyom.screens.Level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.quartyom.UserData;
import com.quartyom.game_elements.InputState;
import com.quartyom.game_elements.PressTimer;
import com.quartyom.game_elements.Scroller;
import com.quartyom.game_elements.Slider;
import com.quartyom.game_elements.Vibrator;

import java.util.Random;

public class LevelBoard {
    public final LevelScreen levelScreen;

    private float board_x, board_y, board_w, board_h;
    private float square_w, square_h;
    private float wall_offset_x, wall_offset_y; // ???

    private float actual_size;                  // реальный размер поля в пикселях (половина по факту)

    Gameplay gameplay;
    BoardDrawer boardDrawer;
    InputState inputState;

    Scroller scroller;
    PressTimer pressTimer;

    Vector2 touch_pos;

    LevelsData levelsData;
    UserData userData;

    Vibrator vibrator;
    Sound move_sound, move_back_sound, body_shortened_sound, victory_sound;

    int how_many_levels, current_level;


    public LevelBoard(LevelScreen levelScreen){
        this.levelScreen = levelScreen;

        gameplay = new Gameplay();
        boardDrawer = new BoardDrawer(this.levelScreen.game, gameplay);

        inputState = InputState.UNTOUCHED;

        scroller = new Scroller(levelScreen.game);
        pressTimer = new PressTimer(levelScreen.game);

        touch_pos = new Vector2();


        String a = Gdx.files.internal("levels/levels_data.json").readString();
        levelsData = levelScreen.game.json.fromJson(LevelsData.class, a);
        how_many_levels = levelsData.how_many_levels;


        userData = levelScreen.game.userData;
        current_level = userData.current_level;

        vibrator = levelScreen.game.vibrator;

        move_sound = levelScreen.game.soundHolder.get("low_put");
        move_back_sound = levelScreen.game.soundHolder.get("low_put_1");
        body_shortened_sound = levelScreen.game.soundHolder.get("body_shortened");
        victory_sound = levelScreen.game.soundHolder.get("victory");

        load_level();

    }

    void resize(){
        // выбираем наименьшее расстояние (граница экрана слева или панель сверху)
        actual_size = levelScreen.game.HALF_HEIGHT - levelScreen.levelTopPanel.getHeight();

        scroller.resize(-levelScreen.game.HALF_WIDTH, -actual_size , levelScreen.game.WIDTH, 2 * actual_size);
        pressTimer.resize(-levelScreen.game.HALF_WIDTH, -actual_size , levelScreen.game.WIDTH, 2 * actual_size);

        if (levelScreen.game.HALF_WIDTH < actual_size){
            actual_size = levelScreen.game.HALF_WIDTH;
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

    void show(){
        gameplay.head_is_captured = false;
    }

    void draw(){
        boardDrawer.draw();
    }

    public void update(){
        if (levelScreen.game.userData.abstract_input_is_on){
            abstract_update();
        }
        else {
            touch_update();
        }
    }

    public void touch_update() {
        if (Gdx.input.isTouched()) {
            touch_pos.x = Gdx.input.getX() - levelScreen.game.HALF_WIDTH;
            touch_pos.y = levelScreen.game.HALF_HEIGHT - Gdx.input.getY();

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
                        move_sound.play(levelScreen.game.userData.volume);
                        break;
                    case MOVE_BACK:
                        gameplay.false_path.clear();
                        move_back_sound.play(levelScreen.game.userData.volume);
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
                // hints ++
                current_level++;
                userData.current_level = current_level;
                levelScreen.game.save_user_data();

                victory_sound.play(levelScreen.game.userData.volume);

                load_level();   // осторожно, уровень может не загрузиться
            }
            else if (result == MoveResult.BODY_IS_SHORTENED){
                gameplay.false_path.clear();
                body_shortened_sound.play(levelScreen.game.userData.volume);
            }
        }
        // палец убрали давно либо не ставили вовсе
        else {
            inputState = InputState.UNTOUCHED;
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
                body_shortened_sound.play(levelScreen.game.userData.volume);
            }
            scroller.inputState = InputState.UNTOUCHED; // чтобы не было одновременно слайда и тапа
        }

        if (scroller.inputState == InputState.JUST_TOUCHED){
            scroller.value.x = gameplay.abstract_input_cursor.x * sensitivity;
            scroller.value.y = gameplay.abstract_input_cursor.y * sensitivity;
        }

        else if (scroller.inputState == InputState.TOUCHED){
            inputState = InputState.TOUCHED;

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
                    move_sound.play(levelScreen.game.userData.volume);
                    break;
                case MOVE_BACK:
                    gameplay.false_path.clear();
                    move_back_sound.play(levelScreen.game.userData.volume);
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
            inputState = InputState.JUST_UNTOUCHED;
            MoveResult result = gameplay.slide_just_untouched_make_move();
            if (result == MoveResult.VICTORY){
                current_level++;
                userData.current_level = current_level;
                levelScreen.game.save_user_data();

                victory_sound.play(levelScreen.game.userData.volume);

                load_level();   // осторожно, уровень может не загрузиться
            }
            else if (result == MoveResult.BODY_IS_SHORTENED){
                gameplay.false_path.clear();
                body_shortened_sound.play(levelScreen.game.userData.volume);
            }

        }
        else {
            inputState = InputState.UNTOUCHED;
        }
    }

    public void load_level(){
        //System.out.println("Loaded level " + current_level);
        if (current_level > 100 && !userData.zen_is_available){
            userData.zen_is_available = true;
            levelScreen.game.save_user_data();
        }

        if (current_level > 200 && !userData.editor_is_available){
            userData.editor_is_available = true;
            levelScreen.game.save_user_data();
        }

        if (current_level > how_many_levels){
            //levelScreen.levelsAreOver.is_active = true;
            levelScreen.game.setScreen("levels_are_over");
            return;
        }

        if (current_level > userData.max_level_achieved){
            Random random = new Random();
            if (random.nextInt(3) == 0){
                userData.hints_amount++;
            }
            userData.max_level_achieved = current_level;
            levelScreen.game.save_user_data();
        }

        String a = Gdx.files.internal("levels/" + current_level + ".json").readString();
        LevelConfiguration levelConfiguration = levelScreen.game.json.fromJson(LevelConfiguration.class, a);
        gameplay.set_level_configuration(levelConfiguration);

        resize(); // чтобы если размер уровня другой, адаптировать экран
        gameplay.normalize_cursor(); // чтобы курсор не выпрыгнул за поле
        boardDrawer.is_hint_shown = false;
    }

}
