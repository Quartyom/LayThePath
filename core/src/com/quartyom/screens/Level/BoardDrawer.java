package com.quartyom.screens.Level;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.quartyom.MakeTheWay;

import java.util.ArrayList;

public class BoardDrawer {
    private MakeTheWay game;
    private Gameplay gameplay;

    // ИХ БУДЕТ ИЗМЕНЯТЬ ВЫСШИЙ КЛАСС
    public float board_x, board_y, board_w, board_h;
    public float square_w, square_h;
    public float wall_offset_x, wall_offset_y;

    public boolean is_hint_shown = false;


    public BoardDrawer(MakeTheWay game, Gameplay gameplay){
        this.game = game;
        this.gameplay = gameplay;
    }

    public void draw(){
        // красим задник
        for (int i = 0; i < gameplay.body.size(); i++){
            game.batch.draw(game.field_atlas.findRegion("light_square"),
                    board_x + gameplay.body.get(i).x * square_w,
                    board_y + gameplay.body.get(i).y * square_h,
                    square_w,
                    square_h
            );

        }
        if (gameplay.body.size()>0 && gameplay.head_is_captured) {
            game.batch.draw(game.field_atlas.findRegion("swamp_square"),
                    board_x + gameplay.body.get(gameplay.body.size() - 1).x * square_w,
                    board_y + gameplay.body.get(gameplay.body.size() - 1).y * square_h,
                    square_w,
                    square_h
            );
        }

        for (Vector2 item: gameplay.boxes){
            game.batch.draw(game.field_atlas.findRegion("light_square"),
                    board_x + item.x * square_w,
                    board_y + item.y * square_h,
                    square_w,
                    square_h
            );
        }

        // hint
        if (is_hint_shown) {
            for (Vector2 item : gameplay.hint) {
                game.batch.draw(game.field_atlas.findRegion("bottom_panel"),
                        board_x + item.x * square_w,
                        board_y + item.y * square_h,
                        square_w,
                        square_h
                );
            }
        }

        // false path
        for (Vector2 item: gameplay.false_path){
            game.batch.draw(game.field_atlas.findRegion("red_square"),
                    board_x + item.x * square_w,
                    board_y + item.y * square_h,
                    square_w,
                    square_h
            );
        }

        // рисуем сетку
        for (int y = 0; y < gameplay.field_size; y++){
            for (int x = 0; x < gameplay.field_size; x++){
                game.batch.draw(game.field_atlas.findRegion("full_square"),
                        board_x + x * square_w,
                        board_y + y * square_h,
                        square_w,
                        square_h
                );
            }
        }

        //вертикальные стенки
        for (Vector2 item : gameplay.vertical_walls) {
            game.batch.draw(game.field_atlas.findRegion("vertical_wall"),
                    board_x + wall_offset_x + item.x * square_w,
                    board_y + item.y * square_h,
                    square_w,
                    square_h
            );
        }

        // горизонтальные стенки
        for (Vector2 item : gameplay.horizontal_walls) {
            game.batch.draw(game.field_atlas.findRegion("horizontal_wall"),
                    board_x + item.x * square_w,
                    board_y + wall_offset_y + item.y * square_h,
                    square_w,
                    square_h
            );
        }

        // препятствия внутри клеток
        draw_obstacle_in_square(gameplay.slash_walls, game.field_atlas.findRegion("slash_wall"));
        draw_obstacle_in_square(gameplay.backslash_walls, game.field_atlas.findRegion("backslash_wall"));
        draw_obstacle_in_square(gameplay.boxes, game.field_atlas.findRegion("box"));
        draw_obstacle_in_square(gameplay.points, game.field_atlas.findRegion("point"));
        draw_obstacle_in_square(gameplay.crossroads, game.field_atlas.findRegion("crossroad"));

        // внешнее обрамление
        game.batch.draw(game.field_atlas.findRegion("full_square"),
                board_x,
                board_y,
                board_w,
                board_h
        );

        draw_body();
    }

    private void draw_obstacle_in_square(ArrayList<Vector2> list, TextureRegion texture){
        for (Vector2 item : list) {
            game.batch.draw(texture,
                    board_x + item.x * square_w,
                    board_y + item.y * square_h,
                    square_w,
                    square_h
            );
        }
    }

    private void draw_body(){
        if (gameplay.body.size() == 0){}
        else if (gameplay.body.size() == 1){
            game.batch.draw(game.field_atlas.findRegion("body", 0),
                    board_x + gameplay.body.get(0).x * square_w,
                    board_y + gameplay.body.get(0).y * square_h,
                    square_w,
                    square_h
            );
        }
        else {
            int head_segment = gameplay.segment_by_io[(int) gameplay.body_io.get(0).y][(int) gameplay.body_io.get(0).y];
            game.batch.draw(game.field_atlas.findRegion("body", head_segment),
                    board_x + gameplay.body.get(0).x * square_w,
                    board_y + gameplay.body.get(0).y * square_h,
                    square_w,
                    square_h
            );
            int tail_segment = gameplay.segment_by_io[(int) gameplay.body_io.get(gameplay.body_io.size() - 1).x][(int) gameplay.body_io.get(gameplay.body_io.size() - 1).x];
            if (gameplay.head_is_captured){tail_segment += 4;}
            game.batch.draw(game.field_atlas.findRegion("body", tail_segment),
                    board_x + gameplay.body.get(gameplay.body.size()-1).x * square_w,
                    board_y + gameplay.body.get(gameplay.body.size()-1).y * square_h,
                    square_w,
                    square_h
            );

            for (int i = 1; i < (gameplay.body.size() - 1); i++) {
                int body_segment = gameplay.segment_by_io[(int) gameplay.body_io.get(i).x][(int) gameplay.body_io.get(i).y];
                game.batch.draw(game.field_atlas.findRegion("body", body_segment),
                        board_x + gameplay.body.get(i).x * square_w,
                        board_y + gameplay.body.get(i).y * square_h,
                        square_w,
                        square_h
                );
            }
        }
    }

}
