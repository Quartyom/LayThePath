package com.quartyom.game_elements;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.quartyom.LayThePath;
import com.quartyom.screens.ColorsTest.ColorsGameplay;
import com.quartyom.screens.Level.MoveResult;

public abstract class ColorsGameBoard {
    public final LayThePath game;

    public ColorsBoardDrawer colorsBoardDrawer;

    protected float boardX, boardY, boardW, boardH;
    protected float squareW, squareH;
    protected float wallOffsetX, wallOffsetY;

    protected float actualSize;

    public ColorsGameplay colorsGameplay;

    protected Scroller scroller;
    protected PressTimer pressTimer;

    protected InputState inputState;
    protected Vector2 touchPos;

    protected Sound moveSound, moveBackSound, bodyShortenedSound, victorySound;

    protected Vibrator vibrator;

    protected ColorsGameBoard(LayThePath game) {
        this.game = game;

        colorsGameplay = new ColorsGameplay();
        colorsBoardDrawer = new ColorsBoardDrawer(game, colorsGameplay);

        scroller = new Scroller(game);
        pressTimer = new PressTimer(game);

        inputState = InputState.UNTOUCHED;
        touchPos = new Vector2();

        vibrator = game.vibrator;

        moveSound = game.soundHolder.get("low_put");
        moveBackSound = game.soundHolder.get("low_put_1");
        bodyShortenedSound = game.soundHolder.get("body_shortened");
        victorySound = game.soundHolder.get("victory");
    }

    public void resize(float topPanelHeight) {
        // выбираем наименьшее расстояние (граница экрана слева или панель сверху)
        actualSize = game.HALF_HEIGHT - topPanelHeight;

        scroller.resize(-game.HALF_WIDTH, -actualSize, game.WIDTH, 2 * actualSize);
        pressTimer.resize(-game.HALF_WIDTH, -actualSize, game.WIDTH, 2 * actualSize);

        if (game.HALF_WIDTH < actualSize) {
            actualSize = game.HALF_WIDTH;
        }

        boardX = -actualSize;
        boardY = -actualSize;
        boardW = actualSize * 2;
        boardH = actualSize * 2;

        squareW = boardW / colorsGameplay.field_size;
        squareH = boardH / colorsGameplay.field_size;

        // толщина стены 2 / 8 px
        wallOffsetX = squareW / 16.0f;
        wallOffsetY = squareH / 16.0f;

        colorsBoardDrawer.boardX = boardX;
        colorsBoardDrawer.boardY = boardY;
        colorsBoardDrawer.boardW = boardW;
        colorsBoardDrawer.boardH = boardH;

        colorsBoardDrawer.squareW = squareW;
        colorsBoardDrawer.squareH = squareH;

        colorsBoardDrawer.wallOffsetX = wallOffsetX;
        colorsBoardDrawer.wallOffsetY = wallOffsetY;
    }

    public void show() {
        colorsGameplay.headIsCaptured = false;
    }

    public void update() {
        if (game.userData.abstract_input_is_on) {
            abstractUpdate();
        } else {
            touchUpdate();
        }
    }

    public abstract void victoryAction();

    public void touchUpdate() {
        if (game.isTouched()) {
            touchPos.x = game.touchPos.x;
            touchPos.y = game.touchPos.y;

            if (touchPos.x > boardX && touchPos.y > boardY && touchPos.x < boardX + boardW && touchPos.y < boardY + boardH) {
                inputState = InputState.TOUCHED;
                touchPos.x = (touchPos.x - boardX) / squareW;
                touchPos.y = (touchPos.y - boardY) / squareH;

                MoveResult result;
                // если нажатие произошло только что, нужно захватить голову
                if (game.inputState == InputState.JUST_TOUCHED) {
                    result = colorsGameplay.justTouchedMakeMove((int) touchPos.x, (int) touchPos.y);
                } else {
                    result = colorsGameplay.touchedMakeMove((int) touchPos.x, (int) touchPos.y);
                }

                switch (result) {
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
                        colorsGameplay.falsePath.clear();
                        moveSound.play(game.userData.volume);
                        break;
                    case MOVE_BACK:
                        colorsGameplay.falsePath.clear();
                        moveBackSound.play(game.userData.volume);
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
                        if (colorsGameplay.falsePath.isEmpty()) {
                            vibrator.vibrate(150);
                        }
                        colorsGameplay.falsePath.add(new Vector2((int) touchPos.x, (int) touchPos.y));
                        break;
                    case OUT_OF_BOUNDS:
                        if (colorsGameplay.falsePath.isEmpty()) {
                            vibrator.vibrate(150);
                        }
                        break;
                }
            }
        }
        // палец только убрали, НО палец до этого касался доски
        else if (inputState == InputState.TOUCHED) {
            inputState = InputState.UNTOUCHED;

            MoveResult result = colorsGameplay.justUntouchedMakeMove((int) touchPos.x, (int) touchPos.y);
            if (result == MoveResult.VICTORY) {
                victorySound.play(game.userData.volume);   // потому что действие при победе более ресурсозатратное
                victoryAction();
            } else if (result == MoveResult.BODY_IS_SHORTENED) {
                colorsGameplay.falsePath.clear();
                bodyShortenedSound.play(game.userData.volume);
            }
        }

    }

    public void abstractUpdate() {
        scroller.update();
        pressTimer.update();

        float sensitivity = actualSize * 2 / 5;

        if (pressTimer.handle_double_tap()) {
            pressTimer.reset();
            MoveResult result = colorsGameplay.doubleTapMakeMove();
            if (result == MoveResult.HEAD_IS_NOT_SET) {
                if (colorsGameplay.falsePath.isEmpty()) {
                    vibrator.vibrate(150);
                }
                colorsGameplay.falsePath.add(new Vector2((int) colorsGameplay.abstractInputCursor.x, (int) colorsGameplay.abstractInputCursor.y));
            } else if (result == MoveResult.BODY_IS_SHORTENED) {
                colorsGameplay.falsePath.clear();
                bodyShortenedSound.play(game.userData.volume);
            }
            scroller.inputState = InputState.UNTOUCHED; // чтобы не было одновременно слайда и тапа
            return;
        }

        if (scroller.inputState == InputState.JUST_TOUCHED) {
            scroller.value.x = colorsGameplay.abstractInputCursor.x * sensitivity;
            scroller.value.y = colorsGameplay.abstractInputCursor.y * sensitivity;
        } else if (scroller.inputState == InputState.TOUCHED) {

            if (scroller.value.x < 0) {
                scroller.value.x = 0;
            } else if (Math.round(scroller.value.x / sensitivity) >= colorsGameplay.field_size) {
                scroller.value.x = (colorsGameplay.field_size - 1) * sensitivity;
            }

            if (scroller.value.y < 0) {
                scroller.value.y = 0;
            } else if (Math.round(scroller.value.y / sensitivity) >= colorsGameplay.field_size) {
                scroller.value.y = (colorsGameplay.field_size - 1) * sensitivity;
            }

            // выравнивание вдоль осей
            if (colorsGameplay.abstractInputCursor.x != Math.round(scroller.value.x / sensitivity)) {
                colorsGameplay.abstractInputCursor.x = Math.round(scroller.value.x / sensitivity);
                scroller.value.y = colorsGameplay.abstractInputCursor.y * sensitivity;
            } else if (colorsGameplay.abstractInputCursor.y != Math.round(scroller.value.y / sensitivity)) {
                scroller.value.x = colorsGameplay.abstractInputCursor.x * sensitivity;
                colorsGameplay.abstractInputCursor.y = Math.round(scroller.value.y / sensitivity);
            }

            MoveResult result = colorsGameplay.slideTouchedMakeMove();

            switch (result) {
                // нейтральные исходы
                case NO_MOVEMENT:
                case HEAD_IS_NOT_CAPTURED:
                case OTHER_GOOD:
                    colorsGameplay.falsePath.clear();
                    break;
                // хорошие исходы
                case HEAD_IS_SET:
                case BODY_VISITED:
                case SIMPLE_MOVEMENT:
                case HEAD_IS_DESTROYED:
                case VICTORY:
                case BODY_IS_SHORTENED:
                    colorsGameplay.falsePath.clear();
                    moveSound.play(game.userData.volume);
                    break;
                case MOVE_BACK:
                    colorsGameplay.falsePath.clear();
                    moveBackSound.play(game.userData.volume);
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
                    if (colorsGameplay.falsePath.isEmpty()) {
                        vibrator.vibrate(150);
                    }
                    colorsGameplay.falsePath.add(new Vector2((int) colorsGameplay.abstractInputCursor.x, (int) colorsGameplay.abstractInputCursor.y));
                    break;
                case OUT_OF_BOUNDS:
                    if (colorsGameplay.falsePath.isEmpty()) {
                        vibrator.vibrate(150);
                    }
                    break;
            }
        } else if (scroller.inputState == InputState.JUST_UNTOUCHED) {
            MoveResult result = colorsGameplay.slideJustUntouchedMakeMove();
            if (result == MoveResult.VICTORY) {
                victorySound.play(game.userData.volume);   // потому что действие при победе более ресурсозатратное
                victoryAction();
            } else if (result == MoveResult.BODY_IS_SHORTENED) {
                colorsGameplay.falsePath.clear();
                bodyShortenedSound.play(game.userData.volume);
            }

        }
    }
}
