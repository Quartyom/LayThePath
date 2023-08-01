package com.quartyom.game_elements;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.quartyom.LayThePath;
import com.quartyom.screens.ColorsTest.ColorsGameplay;
import com.quartyom.screens.ColorsTest.ColorsSource;
import com.quartyom.screens.Level.Gameplay;

import java.util.ArrayList;

// отрисовывает игровое поле
public class ColorsBoardDrawer {
    private LayThePath game;
    private ColorsGameplay colorsGameplay;

    public ColorHolder colorHolder;

    // ИХ БУДЕТ ИЗМЕНЯТЬ ВЫСШИЙ КЛАСС
    public float boardX, boardY, boardW, boardH;
    public float squareW, squareH;
    public float wallOffsetX, wallOffsetY;

    public boolean isHintShown = false;
    public boolean isAbstractCursorVisible = true;

    public ColorsBoardDrawer(LayThePath game, ColorsGameplay colorsGameplay) {
        this.game = game;
        this.colorsGameplay = colorsGameplay;
        colorHolder = new ColorHolder();
    }

    public void draw() {
        // красим задник
        for (int i_body = 0; i_body < colorsGameplay.all_bodies.size(); i_body++){
            if (colorsGameplay.all_bodies_color_id.get(i_body) == -1){
                continue;
            }
            ArrayList<Vector2> body = colorsGameplay.all_bodies.get(i_body);
            for (int i = 0; i < body.size(); i++) {
                game.batch.draw(game.fieldAtlas.findRegion("light_square"),
                        boardX + body.get(i).x * squareW,
                        boardY + body.get(i).y * squareH,
                        squareW,
                        squareH
                );

            }
        }

        if (colorsGameplay.body != null && colorsGameplay.body.size() > 0 && colorsGameplay.headIsCaptured) {
            game.batch.draw(game.fieldAtlas.findRegion("swamp_square"),
                    boardX + colorsGameplay.body.get(colorsGameplay.body.size() - 1).x * squareW,
                    boardY + colorsGameplay.body.get(colorsGameplay.body.size() - 1).y * squareH,
                    squareW,
                    squareH
            );
        }

        for (Vector2 item : colorsGameplay.boxes) {
            game.batch.draw(game.fieldAtlas.findRegion("light_square"),
                    boardX + item.x * squareW,
                    boardY + item.y * squareH,
                    squareW,
                    squareH
            );
        }

//        // hint
//        if (isHintShown) {
//            for (Vector2 item : colorsGameplay.hint) {
//                game.batch.draw(game.fieldAtlas.findRegion("bottom_panel"),
//                        boardX + item.x * squareW,
//                        boardY + item.y * squareH,
//                        squareW,
//                        squareH
//                );
//            }
//        }

        // false path
        if (!game.userData.abstract_input_is_on) {
            for (Vector2 item : colorsGameplay.falsePath) {
                game.batch.draw(game.fieldAtlas.findRegion("red_square"),
                        boardX + item.x * squareW,
                        boardY + item.y * squareH,
                        squareW,
                        squareH
                );
            }
        }

        // colors sources
        for (ColorsSource item : colorsGameplay.colors_sources) {
            game.batch.setColor(colorHolder.get(item.color_id));
            game.batch.draw(game.fieldAtlas.findRegion("color_source"),
                    boardX + item.pos.x * squareW,
                    boardY + item.pos.y * squareH,
                    squareW,
                    squareH
            );
        }
        game.batch.setColor(Color.WHITE);

        // рисуем сетку
        for (int y = 0; y < colorsGameplay.field_size; y++) {
            for (int x = 0; x < colorsGameplay.field_size; x++) {
                game.batch.draw(game.fieldAtlas.findRegion("full_square"),
                        boardX + x * squareW,
                        boardY + y * squareH,
                        squareW,
                        squareH
                );
            }
        }

        //вертикальные стенки
        for (Vector2 item : colorsGameplay.vertical_walls) {
            game.batch.draw(game.fieldAtlas.findRegion("vertical_wall"),
                    boardX + wallOffsetX + item.x * squareW,
                    boardY + item.y * squareH,
                    squareW,
                    squareH
            );
        }

        // горизонтальные стенки
        for (Vector2 item : colorsGameplay.horizontal_walls) {
            game.batch.draw(game.fieldAtlas.findRegion("horizontal_wall"),
                    boardX + item.x * squareW,
                    boardY + wallOffsetY + item.y * squareH,
                    squareW,
                    squareH
            );
        }

        // препятствия внутри клеток
        drawObstacleInSquare(colorsGameplay.slash_walls, game.fieldAtlas.findRegion("slash_wall"));
        drawObstacleInSquare(colorsGameplay.backslash_walls, game.fieldAtlas.findRegion("backslash_wall"));
        drawObstacleInSquare(colorsGameplay.boxes, game.fieldAtlas.findRegion("box"));
        drawObstacleInSquare(colorsGameplay.points, game.fieldAtlas.findRegion("point"));
        drawObstacleInSquare(colorsGameplay.crossroads, game.fieldAtlas.findRegion("crossroad"));

        // внешнее обрамление
        game.batch.draw(game.fieldAtlas.findRegion("full_square"),
                boardX,
                boardY,
                boardW,
                boardH
        );


        for (int i = 0; i < colorsGameplay.all_bodies.size(); i++){
            drawBody(colorsGameplay.all_bodies.get(i), colorsGameplay.all_bodies_io.get(i), colorsGameplay.all_bodies_color_id.get(i));
        }

        // абстрактный курсор
        if (game.userData.abstract_input_is_on && isAbstractCursorVisible) {
            // небольшой костыль, чтобы не модифицировать игровую логику
            // если false_path не пуст, значит был совершён неправильный ход
            if (colorsGameplay.falsePath.isEmpty()) {
                game.batch.draw(game.fieldAtlas.findRegion("cursor_white"),
                        boardX + colorsGameplay.abstractInputCursor.x * squareW,
                        boardY + colorsGameplay.abstractInputCursor.y * squareH,
                        squareW,
                        squareH
                );
            } else {
                game.batch.draw(game.fieldAtlas.findRegion("cursor_red"),
                        boardX + colorsGameplay.abstractInputCursor.x * squareW,
                        boardY + colorsGameplay.abstractInputCursor.y * squareH,
                        squareW,
                        squareH
                );
            }
        }
    }

    private void drawObstacleInSquare(ArrayList<Vector2> list, TextureRegion texture) {
        for (Vector2 item : list) {
            game.batch.draw(texture,
                    boardX + item.x * squareW,
                    boardY + item.y * squareH,
                    squareW,
                    squareH
            );
        }
    }

    private void drawBody(ArrayList<Vector2> body, ArrayList<Vector2> body_io, Integer color_id) {
        if (color_id != -1) {   // не нейтральный цвет
            game.batch.setColor(colorHolder.get(color_id));
        }
        if (body.size() == 0) {
        }
        else if (body.size() == 1) {
            game.batch.draw(game.fieldAtlas.findRegion("body", 0),
                    boardX + body.get(0).x * squareW,
                    boardY + body.get(0).y * squareH,
                    squareW,
                    squareH
            );
        }
        else {
            int head_segment = Gameplay.SEGMENT_BY_IO[(int) body_io.get(0).y][(int) body_io.get(0).y];
            game.batch.draw(game.fieldAtlas.findRegion("body", head_segment),
                    boardX + body.get(0).x * squareW,
                    boardY + body.get(0).y * squareH,
                    squareW,
                    squareH
            );
            int tail_segment = Gameplay.SEGMENT_BY_IO[(int) body_io.get(body_io.size() - 1).x][(int) body_io.get(body_io.size() - 1).x];
            if (body == colorsGameplay.body && colorsGameplay.headIsCaptured) {
                tail_segment += 4;
            }
            game.batch.draw(game.fieldAtlas.findRegion("body", tail_segment),
                    boardX + body.get(body.size() - 1).x * squareW,
                    boardY + body.get(body.size() - 1).y * squareH,
                    squareW,
                    squareH
            );

            for (int i = 1; i < (body.size() - 1); i++) {
                int body_segment = Gameplay.SEGMENT_BY_IO[(int) body_io.get(i).x][(int) body_io.get(i).y];
                game.batch.draw(game.fieldAtlas.findRegion("body", body_segment),
                        boardX + body.get(i).x * squareW,
                        boardY + body.get(i).y * squareH,
                        squareW,
                        squareH
                );
            }
        }
        game.batch.setColor(Color.WHITE);
    }

}
