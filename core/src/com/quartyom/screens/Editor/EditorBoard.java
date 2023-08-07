package com.quartyom.screens.Editor;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.quartyom.LayThePath;
import com.quartyom.game_elements.InputState;
import com.quartyom.game_elements.BoardDrawer;
import com.quartyom.game_elements.Gameplay;
import com.quartyom.screens.Level.LevelConfiguration;

import java.util.ArrayList;


public class EditorBoard {
    public boolean isActive = true;
    private boolean isErasing = false; // or drawing

    public final LayThePath game;
    public final EditorScreen editorScreen;

    private float boardX, boardY, boardW, boardH;
    private float squareW, squareH;
    private float wallOffsetX, wallOffsetY;

    private float actualSize;

    Gameplay gameplay;
    BoardDrawer boardDrawer;

    Vector2 touchPos;

    Sound putSound, unputSound;

    public static final String obstaclesToPut[] = {"vertical wall", "horizontal wall", "slash wall", "backslash wall", "box", "point", "crossroad", "eraser"};
    int cursorOnObstacles = 0;

    public EditorBoard(EditorScreen editorScreen) {
        this.editorScreen = editorScreen;
        this.game = editorScreen.game;

        gameplay = new Gameplay();
        boardDrawer = new BoardDrawer(game, gameplay);
        boardDrawer.isAbstractCursorVisible = false;

        touchPos = new Vector2();

        putSound = game.soundHolder.get("low_put");
        unputSound = game.soundHolder.get("low_put_1");

        LevelConfiguration levelConfiguration = new LevelConfiguration();
        levelConfiguration.field_size = 5;
        gameplay.setLevelConfiguration(levelConfiguration);

    }

    public void resize() {
        // выбираем наименьшее расстояние (граница экрана слева или панель сверху)
        actualSize = game.HALF_HEIGHT - editorScreen.editorTopPanel.getHeight();

        //System.out.println(actual_size + " " + levelScreen.game.HALF_WIDTH);
        if (game.HALF_WIDTH < actualSize) {
            actualSize = game.HALF_WIDTH;
        }

        boardX = -actualSize;
        boardY = -actualSize;
        boardW = actualSize * 2;
        boardH = actualSize * 2;

        squareW = boardW / gameplay.field_size;
        squareH = boardH / gameplay.field_size;

        // толщина стены 2 / 8 px
        wallOffsetX = squareW / 16.0f;
        wallOffsetY = squareH / 16.0f;

        boardDrawer.boardX = boardX;
        boardDrawer.boardY = boardY;
        boardDrawer.boardW = boardW;
        boardDrawer.boardH = boardH;

        boardDrawer.squareW = squareW;
        boardDrawer.squareH = squareH;

        boardDrawer.wallOffsetX = wallOffsetX;
        boardDrawer.wallOffsetY = wallOffsetY;

    }

    public void draw() {
        if (!isActive) {
            return;
        }
        boardDrawer.draw();
    }

    private void justTouchedChangeCell(ArrayList<Vector2> arr) {
        if (arr.contains(touchPos)) {
            isErasing = true;
            arr.remove(touchPos);
            unputSound.play(game.userData.volume);
        } else {
            isErasing = false;
            arr.add(new Vector2(touchPos));
            putSound.play(game.userData.volume);
        }
    }

    private void touchedChangeCell(ArrayList<Vector2> arr) {
        if (isErasing) {
            if (arr.contains(touchPos)) {
                arr.remove(touchPos);
                unputSound.play(game.userData.volume);
            }
        } else if (!arr.contains(touchPos)) {
            arr.add(new Vector2(touchPos));
            putSound.play(game.userData.volume);
        }

    }

    private void erase() {
        gameplay.vertical_walls.remove(touchPos);
        gameplay.horizontal_walls.remove(touchPos);
        gameplay.slash_walls.remove(touchPos);
        gameplay.backslash_walls.remove(touchPos);
        gameplay.boxes.remove(touchPos);
        gameplay.points.remove(touchPos);
        gameplay.crossroads.remove(touchPos);
    }

    public void update() {
        if (!isActive) {
            return;
        }

        if (game.isTouched()) {
            touchPos.x = game.touchPos.x;
            touchPos.y = game.touchPos.y;

            if (touchPos.x > boardX && touchPos.y > boardY && touchPos.x < boardX + boardW && touchPos.y < boardY + boardH) {

                touchPos.x = (int) ((touchPos.x - boardX) / squareW);
                touchPos.y = (int) ((touchPos.y - boardY) / squareH);

                if (game.inputState == InputState.JUST_TOUCHED) {
                    switch (cursorOnObstacles) {
                        case 0: // vertical
                            justTouchedChangeCell(gameplay.vertical_walls);
                            break;
                        case 1: // horizontal
                            justTouchedChangeCell(gameplay.horizontal_walls);
                            break;
                        case 2: //slash
                            justTouchedChangeCell(gameplay.slash_walls);
                            break;
                        case 3: // backslash
                            justTouchedChangeCell(gameplay.backslash_walls);
                            break;
                        case 4: // box
                            justTouchedChangeCell(gameplay.boxes);
                            break;
                        case 5: // point
                            justTouchedChangeCell(gameplay.points);
                            break;
                        case 6: // crossroad
                            justTouchedChangeCell(gameplay.crossroads);
                            break;
                        case 7: // eraser
                            erase();
                            break;
                    }
                } else {
                    switch (cursorOnObstacles) {
                        case 0: // vertical
                            touchedChangeCell(gameplay.vertical_walls);
                            break;
                        case 1: // horizontal
                            touchedChangeCell(gameplay.horizontal_walls);
                            break;
                        case 2: //slash
                            touchedChangeCell(gameplay.slash_walls);
                            break;
                        case 3: // backslash
                            touchedChangeCell(gameplay.backslash_walls);
                            break;
                        case 4: // box
                            touchedChangeCell(gameplay.boxes);
                            break;
                        case 5: // point
                            touchedChangeCell(gameplay.points);
                            break;
                        case 6: // crossroad
                            touchedChangeCell(gameplay.crossroads);
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
