package com.quartyom.screens.Zen;

import com.badlogic.gdx.math.Vector2;
import com.quartyom.LayThePath;
import com.quartyom.screens.Level.Gameplay;
import com.quartyom.screens.Level.LevelConfiguration;
import com.quartyom.screens.Level.MoveResult;

import java.util.Random;

public class ZenLevelGenerator {

    float QUALITY_COEFFICIENT = 0.75f;

    final LayThePath game;
    Gameplay gameplay;
    LevelConfiguration levelConfiguration;

    Random random;

    public ZenLevelGenerator(final LayThePath game) {
        this.game = game;
        random = game.random;

        levelConfiguration = new LevelConfiguration();
        gameplay = new Gameplay();
    }

    private boolean generatePath(int field_size) {
        boolean success = false;

        //levelConfiguration.setEmpty();
        levelConfiguration = new LevelConfiguration();
        levelConfiguration.field_size = field_size;
        gameplay.setLevelConfiguration(levelConfiguration);

        // определяем позицию головы
        int headX = random.nextInt(field_size);
        int headY = random.nextInt(field_size);

        int maxCellsOccupied = 0;
        while (true) {

            // рисуем путь с обоих концов
            for (int gg = 0; gg < 2; gg++) {

                // первый раз голова поставится, второй раз переключится
                gameplay.justTouchedMakeMove(headX, headY);

                int tail_x = headX;
                int tail_y = headY;

                boolean ableToMove = true;
                // цикл ходов
                while (ableToMove) {

                    boolean attempt_direction[] = {false, false, false, false};
                    boolean move_is_done = false;
                    // цикл попыток
                    for (int i = 4; i > 0; i--) {

                        int direction = freeDirection(attempt_direction, random.nextInt(i));

                        if (direction == -1) {
                            ableToMove = false;
                            break;
                        }

                        int new_tail_x = tail_x + X_SHIFT_BY_DIRECTION[direction];
                        int new_tail_y = tail_y + Y_SHIFT_BY_DIRECTION[direction];

                        MoveResult result = gameplay.touchedMakeMove(new_tail_x, new_tail_y);

                        switch (result) {

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
                                gameplay.touchedMakeMove(tail_x, tail_y);
                                break;

                            case VICTORY:
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
                                break;
                        }

                        if (move_is_done) {
                            break;
                        }

                    }

                    if (!move_is_done) {
                        ableToMove = false;
                    }

                }
                gameplay.justUntouchedMakeMove((int) gameplay.body.get(gameplay.body.size() - 1).x,
                        (int) gameplay.body.get(gameplay.body.size() - 1).y);
                if (gg == 0) {
                    headX = (int) gameplay.body.get(0).x;
                    headY = (int) gameplay.body.get(0).y;
                }
            }

            // проверка качества
            int cells_occupied = 0;      // количество клеток, где есть тело
            boolean not_empty_columns[] = new boolean[field_size];
            boolean not_empty_rows[] = new boolean[field_size];

            for (int y = 0; y < field_size; y++) {
                for (int x = 0; x < field_size; x++) {
                    if (gameplay.body.contains(new Vector2(x, y))) {
                        cells_occupied++;
                        not_empty_columns[x] = true;
                        not_empty_rows[y] = true;
                    }
                }
            }

            if (cells_occupied >= field_size * field_size * QUALITY_COEFFICIENT) {
                success = true;
                break;
            } else {

                // есть ли продвижение по заполнению поля
                if (cells_occupied <= maxCellsOccupied) {
                    break;
                } else {
                    maxCellsOccupied = cells_occupied;
                }

                boolean to_continue = false;

                // смещение тела по полю
                if (not_empty_columns[0] && !not_empty_columns[field_size - 1]) {  // 1...0
                    for (int i = 1; i < field_size; i++) {
                        if (!not_empty_columns[i]) {
                            for (Vector2 item : gameplay.body) {
                                item.x += field_size - i;
                            }
                            to_continue = true;
                            break;
                        }
                    }
                } else if (not_empty_columns[field_size - 1] && !not_empty_columns[0]) { // 0...1
                    for (int i = 1; i < field_size; i++) {
                        if (not_empty_columns[i]) {
                            for (Vector2 item : gameplay.body) {
                                item.x -= i;
                            }
                            to_continue = true;
                            break;
                        }
                    }

                }

                if (not_empty_rows[0] && !not_empty_rows[field_size - 1]) {    // 1...0
                    for (int i = 1; i < field_size; i++) {
                        if (!not_empty_rows[i]) {
                            for (Vector2 item : gameplay.body) {
                                item.y += field_size - i;
                            }
                            to_continue = true;
                            break;
                        }
                    }
                } else if (not_empty_rows[field_size - 1] && !not_empty_rows[0]) {   // 0...1
                    for (int i = 1; i < field_size; i++) {
                        if (not_empty_rows[i]) {
                            for (Vector2 item : gameplay.body) {
                                item.y -= i;
                            }
                            to_continue = true;
                            break;
                        }
                    }
                }

                if (!to_continue) {
                    break;
                } else {
                    headX = (int) gameplay.body.get(0).x;
                    headY = (int) gameplay.body.get(0).y;
                }

            }

        }

        gameplay.setHint();
        return success;
    }

    public LevelConfiguration generateLevel() {
        int shift = (game.userData.current_zen_level / 500) + 3;
        if (shift > 6) {
            shift = 6;
        }

        int field_size = FIELD_SIZE_DISTRIBUTION[random.nextInt(FIELD_SIZE_DISTRIBUTION.length)] + shift;


        while (!generatePath(field_size));

        // в пустоты расставляются коробки
        // вынесено в отдельный цикл, потому что прежде чем расставлять стены требуется поставить ВСЕ коробки
        // стена должна проверить, не перекрывает ли она коробку
        for (int y = 0; y < field_size; y++) {
            for (int x = 0; x < field_size; x++) {
                Vector2 current = new Vector2(x, y);
                if (!gameplay.body.contains(current)) {
                    gameplay.boxes.add(current);
                }
            }
        }

        // остальные заполняются препятствиями
        for (int y = 0; y < field_size; y++) {
            for (int x = 0; x < field_size; x++) {

                Vector2 current = new Vector2(x, y);

                int i = gameplay.body.indexOf(current);

                //if (i == -1){ gameplay.boxes.add(current); }
                if (i != -1) {
                    // вероятность поставить преятствие в принципе
                    if (random.nextInt(5) < 2) {
                        continue;
                    } // p = 2 / 5

                    int inp = (int) gameplay.body_io.get(i).x;
                    int out = (int) gameplay.body_io.get(i).y;

                    int segment;
                    if (i == 0) {
                        segment = Gameplay.SEGMENT_BY_IO[out][out];
                    } else if (i == gameplay.body.size() - 1) {
                        segment = Gameplay.SEGMENT_BY_IO[inp][inp];
                    } else {
                        segment = Gameplay.SEGMENT_BY_IO[inp][out];
                    }

                    // Все исходы равновероятны
                    switch (segment) {
                        case 0: // по идее такого не бывает

                        case 1:
                        case 5:
                            // вертикальные и горизонтальные снизу
                            if (random.nextBoolean() && x > 0) {
                                // те же координаты либо х+1
                                if (gameplay.boxes.contains(new Vector2(x - 1, y)) || gameplay.boxes.contains(
                                        current)) {
                                    break;
                                }
                                gameplay.vertical_walls.add(new Vector2(x - 1, y));
                            }
                            if (random.nextBoolean() && x < field_size - 1) {
                                if (gameplay.boxes.contains(current) || gameplay.boxes.contains(
                                        new Vector2(x + 1, y))) {
                                    break;
                                }
                                gameplay.vertical_walls.add(current);
                            }
                            if (random.nextBoolean() && y > 0) {
                                // те же координаты либо y+1
                                if (gameplay.boxes.contains(new Vector2(x, y - 1)) || gameplay.boxes.contains(
                                        current)) {
                                    break;
                                }
                                gameplay.horizontal_walls.add(new Vector2(x, y - 1));
                            }
                            break;
                        case 2:
                        case 6:
                            // вертикальные слева и горизонтальные
                            if (random.nextBoolean() && x > 0) {
                                if (gameplay.boxes.contains(new Vector2(x - 1, y)) || gameplay.boxes.contains(
                                        current)) {
                                    break;
                                }
                                gameplay.vertical_walls.add(new Vector2(x - 1, y));
                            }
                            if (random.nextBoolean() && y > 0) {
                                if (gameplay.boxes.contains(new Vector2(x, y - 1)) || gameplay.boxes.contains(
                                        current)) {
                                    break;
                                }
                                gameplay.horizontal_walls.add(new Vector2(x, y - 1));
                            }
                            if (random.nextBoolean() && y < field_size - 1) {
                                if (gameplay.boxes.contains(current) || gameplay.boxes.contains(
                                        new Vector2(x, y + 1))) {
                                    break;
                                }
                                gameplay.horizontal_walls.add(current);
                            }
                            break;
                        case 3:
                        case 7:
                            // вертикальные и горизонтальные сверху
                            if (random.nextBoolean() && x > 0) {
                                if (gameplay.boxes.contains(new Vector2(x - 1, y)) || gameplay.boxes.contains(
                                        current)) {
                                    break;
                                }
                                gameplay.vertical_walls.add(new Vector2(x - 1, y));
                            }
                            if (random.nextBoolean() && x < field_size - 1) {
                                if (gameplay.boxes.contains(current) || gameplay.boxes.contains(
                                        new Vector2(x + 1, y))) {
                                    break;
                                }
                                gameplay.vertical_walls.add(current);
                            }
                            if (random.nextBoolean() && y < field_size - 1) {
                                if (gameplay.boxes.contains(current) || gameplay.boxes.contains(
                                        new Vector2(x, y + 1))) {
                                    break;
                                }
                                gameplay.horizontal_walls.add(current);
                            }
                            break;
                        case 4:
                        case 8:
                            // вертикальные справа и горизонтальные
                            if (random.nextBoolean() && x < field_size - 1) {
                                if (gameplay.boxes.contains(current) || gameplay.boxes.contains(
                                        new Vector2(x + 1, y))) {
                                    break;
                                }
                                gameplay.vertical_walls.add(current);
                            }
                            if (random.nextBoolean() && y > 0) {
                                if (gameplay.boxes.contains(new Vector2(x, y - 1)) || gameplay.boxes.contains(
                                        current)) {
                                    break;
                                }
                                gameplay.horizontal_walls.add(new Vector2(x, y - 1));
                            }
                            if (random.nextBoolean() && y < field_size - 1) {
                                if (gameplay.boxes.contains(current) || gameplay.boxes.contains(
                                        new Vector2(x, y + 1))) {
                                    break;
                                }
                                gameplay.horizontal_walls.add(current);
                            }
                            break;

                        case 9:
                            // только вертикальные или перекрёсток
                            if (random.nextBoolean() && x > 0) {
                                if (gameplay.boxes.contains(new Vector2(x - 1, y)) || gameplay.boxes.contains(
                                        current)) {
                                    break;
                                }
                                gameplay.vertical_walls.add(new Vector2(x - 1, y));
                            }
                            if (random.nextBoolean() && x < field_size - 1) {
                                if (gameplay.boxes.contains(current) || gameplay.boxes.contains(
                                        new Vector2(x + 1, y))) {
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
                            if (random.nextBoolean() && y > 0) {
                                if (gameplay.boxes.contains(new Vector2(x, y - 1)) || gameplay.boxes.contains(
                                        current)) {
                                    break;
                                }
                                gameplay.horizontal_walls.add(new Vector2(x, y - 1));
                            }
                            if (random.nextBoolean() && y < field_size - 1) {
                                if (gameplay.boxes.contains(current) || gameplay.boxes.contains(
                                        new Vector2(x, y + 1))) {
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
                            if (random.nextInt(3) != 0) {
                                if (random.nextBoolean()) {
                                    gameplay.backslash_walls.add(current);  // p = 1/3
                                } else {
                                    gameplay.points.add(current);       // p = 1/3
                                }
                            }
                            break;
                        case 12:
                        case 14:
                            // точка, slash
                            if (random.nextInt(3) != 0) {
                                if (random.nextBoolean()) {
                                    gameplay.slash_walls.add(current);  // p = 1/3
                                } else {
                                    gameplay.points.add(current);       // p = 1/3
                                }
                            }
                            break;

                    }
                }


            }
        }

        return gameplay.getLevelConfiguration();
    }

    private static final int X_SHIFT_BY_DIRECTION[] = {0, 1, 0, -1};
    private static final int Y_SHIFT_BY_DIRECTION[] = {1, 0, -1, 0};

    private int freeDirection(boolean arr[], int k) {
        int K = k;

        for (int i = 0; i < arr.length; i++) {
            if (arr[i]) {
                K++;
            } else if (i == K) {
                arr[i] = true; // added
                return i;
            }
        }
        return -1;
    }

    private static final int FIELD_SIZE_DISTRIBUTION[] = {
            0, 0,
            1, 1, 1,
            2, 2, 2, 2,
            3, 3, 3,
            4, 4
    };

    private LevelConfiguration newLevelConfiguration;

    public synchronized void generateNewLevel() throws InterruptedException {   // генерирует новый уровень, если требуется (иначе ждёт)
        while (newLevelConfiguration != null){
            wait();
        }
        newLevelConfiguration = new LevelConfiguration(generateLevel());
        notify();
    }

    public synchronized LevelConfiguration getNewLevel(){   // выдаёт новый уровень, если сгенерирован (иначе ждёт)
        while (newLevelConfiguration == null){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        notify();
        LevelConfiguration tmp = newLevelConfiguration;
        newLevelConfiguration = null;
        return tmp;
    }

}
