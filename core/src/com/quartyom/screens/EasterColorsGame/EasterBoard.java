package com.quartyom.screens.EasterColorsGame;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.quartyom.LayThePath;
import com.quartyom.game_elements.BoardDrawer;
import com.quartyom.game_elements.Gameplay;
import com.quartyom.game_elements.InputState;
import com.quartyom.game_elements.PressTimer;
import com.quartyom.game_elements.Scroller;
import com.quartyom.game_elements.Vibrator;
import com.quartyom.screens.Level.MoveResult;

import java.util.Random;

public class EasterBoard {
    public final LayThePath game;

    public final int FIELD_SIZE = 3;
    private Vector2 current_level;
    private Vector2 false_tap;
    private Color main_level_color, victory_level_color;
    private Random random;

    protected float boardX, boardY, boardW, boardH;
    protected float squareW, squareH;
    protected float wallOffsetX, wallOffsetY;

    protected float actualSize;

    protected Vector2 touchPos;

    protected Sound victorySound;

    protected Vibrator vibrator;

    protected EasterBoard(LayThePath game) {
        this.game = game;
        this.random = game.random;

        touchPos = new Vector2();
        current_level = new Vector2();

        vibrator = game.vibrator;

        victorySound = game.soundHolder.get("victory");

        makeLevel();
    }

    public void resize(float topPanelHeight) {
        // выбираем наименьшее расстояние (граница экрана слева или панель сверху)
        actualSize = game.HALF_HEIGHT - topPanelHeight;

        if (game.HALF_WIDTH < actualSize) {
            actualSize = game.HALF_WIDTH;
        }

        boardX = -actualSize;
        boardY = -actualSize;
        boardW = actualSize * 2;
        boardH = actualSize * 2;

        squareW = boardW / FIELD_SIZE;
        squareH = boardH / FIELD_SIZE;

        // толщина стены 2 / 8 px
        wallOffsetX = squareW / 16.0f;
        wallOffsetY = squareH / 16.0f;
    }

    public void draw() {
        // задник
        for (int y = 0; y < FIELD_SIZE; y++) {
            for (int x = 0; x < FIELD_SIZE; x++) {
                if (current_level.x == x && current_level.y == y) {
                    game.batch.setColor(victory_level_color);
                    game.batch.draw(game.fieldAtlas.findRegion("white_square"),
                            boardX + x * squareW,
                            boardY + y * squareH,
                            squareW,
                            squareH
                    );
                }
                else {
                    game.batch.setColor(main_level_color);
                    game.batch.draw(game.fieldAtlas.findRegion("white_square"),
                            boardX + x * squareW,
                            boardY + y * squareH,
                            squareW,
                            squareH
                    );
                }
            }
        }
        game.batch.setColor(Color.WHITE);

        // false tap
        if (false_tap != null) {
            game.batch.draw(game.fieldAtlas.findRegion("red_square"),
                    boardX + false_tap.x * squareW,
                    boardY + false_tap.y * squareH,
                    squareW,
                    squareH
            );

        }

        // рисуем сетку
        for (int y = 0; y < FIELD_SIZE; y++) {
            for (int x = 0; x < FIELD_SIZE; x++) {
                game.batch.draw(game.fieldAtlas.findRegion("full_square"),
                        boardX + x * squareW,
                        boardY + y * squareH,
                        squareW,
                        squareH
                );
            }
        }

        // внешнее обрамление
        game.batch.draw(game.fieldAtlas.findRegion("full_square"),
                boardX,
                boardY,
                boardW,
                boardH
        );
    }

    public void update() {
        if (game.inputState == InputState.JUST_TOUCHED) {
            touchPos.x = game.touchPos.x;
            touchPos.y = game.touchPos.y;

            if (touchPos.x > boardX && touchPos.y > boardY && touchPos.x < boardX + boardW && touchPos.y < boardY + boardH) {
                touchPos.x = (int) ((touchPos.x - boardX) / squareW);
                touchPos.y = (int) ((touchPos.y - boardY) / squareH);

                //System.out.println(touchPos + " " + current_level);

                if (touchPos.equals(current_level)){
                    victorySound.play(game.userData.volume);
                    makeLevel();
                }
                else {
                    vibrator.vibrate(150);
                    false_tap = touchPos;
                }

            }
        }
        else if (game.inputState == InputState.JUST_UNTOUCHED) {
            false_tap = null;
        }
    }

    private void makeLevel(){
        current_level.x = random.nextInt(FIELD_SIZE);
        current_level.y = random.nextInt(FIELD_SIZE);

        int main_r = random.nextInt(256);
        int main_g = random.nextInt(256);
        int main_b = random.nextInt(256);

        int range = 50;
        int shift_range = range * 2 + 1;

        int shift_r = random.nextInt(shift_range);
        int shift_g = random.nextInt(shift_range);
        int shift_b = random.nextInt(shift_range);

        main_level_color = new Color(main_r / 255f, main_g / 255f, main_b / 255f, 1);

        victory_level_color = new Color((main_r - range + shift_r) / 255f,
                (main_g - range + shift_g) / 255f,
                (main_b - range + shift_b) / 255f,
                1);

    }

}
