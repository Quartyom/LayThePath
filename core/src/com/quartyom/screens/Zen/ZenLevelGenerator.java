package com.quartyom.screens.Zen;

import com.badlogic.gdx.math.Vector2;
import com.quartyom.screens.Level.Gameplay;
import com.quartyom.screens.Level.LevelConfiguration;
import com.quartyom.screens.Level.MoveResult;

import java.util.ArrayList;
import java.util.Random;

public class ZenLevelGenerator {
    float QUALITY_COEFFICIENT = 0.75f;

    Gameplay gameplay;
    LevelConfiguration levelConfiguration;

    Random random;

    public ZenLevelGenerator(){
        random = new Random();

        levelConfiguration = new LevelConfiguration();
        gameplay = new Gameplay();
    }

    private int free_direction(boolean arr[], int k){
        int K = k;

        for (int i = 0; i<arr.length;i++){
            if (arr[i]){
                K++;
            }
            else if (i == K){
                return i;
            }
        }
        return -1;
    }

    private void generate_path(int field_size){
        levelConfiguration.set_empty();
        levelConfiguration.field_size = field_size;

        gameplay.set_level_configuration(levelConfiguration);


        // определяем позицию головы
        int head_x = random.nextInt(field_size);
        int head_y = random.nextInt(field_size);

        //System.out.println(head_x + " " + head_y);

        boolean already_won = false;
        // внешний цикл
        for (int gg = 0; gg < 2; gg++){

            // первый раз голова поставится, второй раз переключится
            gameplay.just_touched_make_move(head_x, head_y);

            int tail_x = head_x;
            int tail_y = head_y;

            boolean able_to_move = true;
            // цикл ходов
            while (able_to_move){

                boolean attempt_direction[] = {false, false, false, false};
                boolean move_is_done = false;
                // цикл попыток
                for (int i = 4; i > 0; i--){

                    int rand = random.nextInt(i);
                    int direction = free_direction(attempt_direction, rand);



                    if (direction == -1){
                        able_to_move = false;

                        break;
                    }

                    //int new_tail_x = new_x_by_direction(tail_x, direction);
                    //int new_tail_y = new_y_by_direction(tail_y, direction);
                    int new_tail_x = tail_x + x_shift_by_direction[direction];
                    int new_tail_y = tail_y + y_shift_by_direction[direction];

                    MoveResult result = gameplay.touched_make_move(new_tail_x, new_tail_y);

                    move_is_done = false;

                    switch (result){

                        case HEAD_IS_SET:
                        case BODY_VISITED:
                        case SIMPLE_MOVEMENT:
                        case HEAD_IS_DESTROYED:
                        case NO_MOVEMENT:
                        case OTHER_GOOD:
                            tail_x = new_tail_x;
                            tail_y = new_tail_y;
                            move_is_done = true;
                            break;


                        case MOVE_BACK:
                            gameplay.touched_make_move(tail_x, tail_y);
                            attempt_direction[direction] = true;
                            break;


                        case VICTORY:
                            break;



                        case HEAD_IS_NOT_SET:
                        case OUT_OF_BOUNDS:
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
                        case HEAD_IS_NOT_CAPTURED:
                        case BODY_IS_SHORTENED:
                            attempt_direction[direction] = true;
                            break;
                    }

                    if (move_is_done){
                        break;
                    }


                }

                if (!move_is_done){
                    able_to_move = false;
                }


            }
            gameplay.just_untouched_make_move((int)gameplay.body.get(gameplay.body.size()-1).x, (int)gameplay.body.get(gameplay.body.size()-1).y);
        }

        gameplay.set_hint();
    }

    public LevelConfiguration generate_level(){
        int field_size = random.nextInt(9 - 4) + 4; // от 4 до 8

        while (true) {
            generate_path(field_size);
            int k = 0;      // количество клеток, где есть тело
            for (int y = 0; y < field_size; y++) {
                for (int x = 0; x < field_size; x++) {
                    Vector2 current = new Vector2(x, y);
                    if (gameplay.body.contains(current)) {
                        k++;
                    }
                }
            }
            if (k >= field_size * field_size * QUALITY_COEFFICIENT) {
                break;
            }
        }

        //System.out.println("field size " + field_size);
        //System.out.println("body size " + gameplay.body.size());
        //System.out.println(gameplay.body);


        // в пустоты расставляются коробки
        // вынесено в отдельный цикл, потому что прежде чем расставлять стены требуется поставить ВСЕ коробки
        // стена должна проверить, не перекрывает ли она коробку
        for (int y=0; y<field_size; y++) {
            for (int x = 0; x < field_size; x++) {
                Vector2 current = new Vector2(x,y);
                if (!gameplay.body.contains(current)){
                    gameplay.boxes.add(current);
                }
            }
        }

        // остальные заполняются препятствиями
        for (int y=0; y<field_size; y++){
            for (int x=0; x<field_size; x++){

                Vector2 current = new Vector2(x,y);

                int i = gameplay.body.indexOf(current);

                //if (i == -1){ gameplay.boxes.add(current); }
                if (i != -1) {
                    // вероятность поставить преятствие в принципе
                    if (random.nextInt(5) < 2){ continue; } // p = 2 / 5

                    int inp = (int) gameplay.body_io.get(i).x;
                    int out = (int) gameplay.body_io.get(i).y;

                    int segment;
                    if (i==0){
                        segment = gameplay.segment_by_io[out][out];
                    }
                    else if (i == gameplay.body.size()-1){
                        segment = gameplay.segment_by_io[inp][inp];
                    }
                    else {
                        segment = gameplay.segment_by_io[inp][out];
                    }

                    // Все исходы равновероятны
                    switch (segment){
                        case 0: // по идее такого не бывает

                        case 1:
                        case 5:
                            // вертикальные и горизонтальные снизу
                            if (random.nextBoolean() && x > 0){
                                // те же координаты либо х+1
                                if (gameplay.boxes.contains(new Vector2(x-1, y)) || gameplay.boxes.contains(current)){
                                    break;
                                }
                                gameplay.vertical_walls.add(new Vector2(x - 1, y));
                            }
                            if (random.nextBoolean() && x < field_size - 1) {
                                if (gameplay.boxes.contains(current) || gameplay.boxes.contains(new Vector2(x + 1, y))) {
                                    break;
                                }
                                gameplay.vertical_walls.add(current);
                            }
                            if (random.nextBoolean() && y > 0) {
                                // те же координаты либо y+1
                                if (gameplay.boxes.contains(new Vector2(x, y - 1)) || gameplay.boxes.contains(current)) {
                                    break;
                                }
                                gameplay.horizontal_walls.add(new Vector2(x, y - 1));
                            }
                            break;
                        case 2:
                        case 6:
                            // вертикальные слева и горизонтальные
                            if (random.nextBoolean() && x > 0){
                                if (gameplay.boxes.contains(new Vector2(x-1, y)) || gameplay.boxes.contains(current)){
                                    break;
                                }
                                gameplay.vertical_walls.add(new Vector2(x - 1, y));
                            }
                            if (random.nextBoolean() && y > 0){
                                if (gameplay.boxes.contains(new Vector2(x, y-1)) || gameplay.boxes.contains(current)){
                                    break;
                                }
                                gameplay.horizontal_walls.add(new Vector2(x, y - 1));
                            }
                            if (random.nextBoolean() && y < field_size - 1){
                                if (gameplay.boxes.contains(current) || gameplay.boxes.contains(new Vector2(x, y+1))){
                                    break;
                                }
                                gameplay.horizontal_walls.add(current);
                            }
                            break;
                        case 3:
                        case 7:
                            // вертикальные и горизонтальные сверху
                            if (random.nextBoolean() && x > 0){
                                if (gameplay.boxes.contains(new Vector2(x-1, y)) || gameplay.boxes.contains(current)){
                                    break;
                                }
                                gameplay.vertical_walls.add(new Vector2(x - 1, y));
                            }
                            if (random.nextBoolean() && x < field_size - 1) {
                                if (gameplay.boxes.contains(current) || gameplay.boxes.contains(new Vector2(x+1, y))){
                                    break;
                                }
                                gameplay.vertical_walls.add(current);
                            }
                            if (random.nextBoolean() && y < field_size - 1) {
                                if (gameplay.boxes.contains(current) || gameplay.boxes.contains(new Vector2(x, y+1))){
                                    break;
                                }
                                gameplay.horizontal_walls.add(current);
                            }
                            break;
                        case 4:
                        case 8:
                            // вертикальные справа и горизонтальные
                            if (random.nextBoolean() && x < field_size - 1){
                                if (gameplay.boxes.contains(current) || gameplay.boxes.contains(new Vector2(x+1, y))){
                                    break;
                                }
                                gameplay.vertical_walls.add(current);
                            }
                            if (random.nextBoolean() && y > 0) {
                                if (gameplay.boxes.contains(new Vector2(x, y-1)) || gameplay.boxes.contains(current)){
                                    break;
                                }
                                gameplay.horizontal_walls.add(new Vector2(x, y - 1));
                            }
                            if (random.nextBoolean() && y < field_size - 1) {
                                if (gameplay.boxes.contains(current) || gameplay.boxes.contains(new Vector2(x, y+1))){
                                    break;
                                }
                                gameplay.horizontal_walls.add(current);
                            }
                            break;

                        case 9:
                            // только вертикальные или перекрёсток
                            if (random.nextBoolean() && x > 0){
                                if (gameplay.boxes.contains(new Vector2(x - 1, y)) || gameplay.boxes.contains(current)) {
                                    break;
                                }
                                gameplay.vertical_walls.add(new Vector2(x - 1, y));
                            }
                            if (random.nextBoolean() && x < field_size - 1) {
                                if (gameplay.boxes.contains(current) || gameplay.boxes.contains(new Vector2(x + 1, y))) {
                                    break;
                                }
                                gameplay.vertical_walls.add(current);
                            }
                            if (random.nextInt(4) == 0) {
                                gameplay.crossroads.add(current);
                            }
                            break;
                        case 10:
                            // только горизонтальные или перекёсток
                            if (random.nextBoolean() && y > 0){
                                if (gameplay.boxes.contains(new Vector2(x, y - 1)) || gameplay.boxes.contains(current)) {
                                    break;
                                }
                                gameplay.horizontal_walls.add(new Vector2(x, y - 1));
                            }
                            if (random.nextBoolean() && y < field_size - 1) {
                                if (gameplay.boxes.contains(current) || gameplay.boxes.contains(new Vector2(x, y + 1))) {
                                    break;
                                }
                                gameplay.horizontal_walls.add(current);
                            }
                            if (random.nextInt(4) == 0) {
                                gameplay.crossroads.add(current);
                            }
                            break;

                        case 11:
                        case 13:
                            // точка, backslash
                            if (random.nextInt(3) != 0){
                                if (random.nextBoolean()){
                                    gameplay.backslash_walls.add(current);  // p = 1/3
                                }
                                else {
                                    gameplay.points.add(current);       // p = 1/3
                                }
                            }
                            break;
                        case 12:
                        case 14:
                            // точка, slash
                            if (random.nextInt(3) != 0){
                                if (random.nextBoolean()){
                                    gameplay.slash_walls.add(current);  // p = 1/3
                                }
                                else {
                                    gameplay.points.add(current);       // p = 1/3
                                }
                            }
                            break;

                    }
                }


            }
        }

        return gameplay.get_level_configuration();
    }

    private int x_shift_by_direction[] = {0, 1, 0, -1};
    private int y_shift_by_direction[] = {1, 0, -1, 0};

    /*private int new_x_by_direction(int x, int direction){
        if (direction == 0 || direction == 2){
            return x;
        }
        else if (direction == 1){
            return x + 1;
        }
        else {
            return x - 1;
        }
    }*/

    /*private int new_y_by_direction(int y, int direction){
        if (direction == 0){
            return y + 1;
        }
        else if (direction == 1 || direction == 3){
            return y;
        }
        else {
            return y - 1;
        }
    }*/


}
