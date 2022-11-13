package com.quartyom.screens.Level;

import com.badlogic.gdx.math.Vector2;
import java.util.ArrayList;
import java.util.Collections;

// реализует чистую игровую логику
public class Gameplay {
    public ArrayList<Vector2> vertical_walls, horizontal_walls, slash_walls, backslash_walls, boxes, points, crossroads;

    public ArrayList<Vector2> body, body_io;
    public ArrayList<Vector2> false_path;
    public ArrayList<Vector2> hint;

    public Vector2 abstract_input_cursor;

    public boolean head_is_captured;    // держит ли палец на голове
    // когда только поставили голову - false, при ходе назад - false, если держит палец на голове или хвосте - true
    public boolean is_tending_to_destroy_the_head;


    public int how_many_visited;
    public int how_many_should_be_visited;

    public int field_size;

    public Gameplay(){
        body = new ArrayList<>();
        body_io = new ArrayList<>();
        false_path = new ArrayList<>();

        abstract_input_cursor = new Vector2();
    }

    public void set_level_configuration(LevelConfiguration levelConfiguration){
        vertical_walls = levelConfiguration.vertical_walls;
        horizontal_walls = levelConfiguration.horizontal_walls;
        slash_walls = levelConfiguration.slash_walls;
        backslash_walls = levelConfiguration.backslash_walls;
        boxes = levelConfiguration.boxes;
        points = levelConfiguration.points;
        crossroads = levelConfiguration.crossroads;
        hint = levelConfiguration.hint;
        field_size = levelConfiguration.field_size;

        reset_body();
        how_many_visited = 0;
        how_many_should_be_visited = field_size * field_size - boxes.size();

        head_is_captured = false;
        is_tending_to_destroy_the_head = false;
    }

    public void set_hint(){
        if (body != null && body.size()>0){
            if (hint == null){
                hint = new ArrayList<Vector2>();
            }
            if (!hint.isEmpty()){
                hint.clear();
            }
            hint.add(body.get(0));
            hint.add(body.get(body.size()-1));
        }
    }


    public LevelConfiguration get_level_configuration(){
        LevelConfiguration levelConfiguration = new LevelConfiguration();
        levelConfiguration.vertical_walls = vertical_walls;
        levelConfiguration.horizontal_walls = horizontal_walls;
        levelConfiguration.slash_walls = slash_walls;
        levelConfiguration.backslash_walls = backslash_walls;
        levelConfiguration.boxes = boxes;
        levelConfiguration.points = points;
        levelConfiguration.crossroads = crossroads;
        levelConfiguration.hint = hint;

        levelConfiguration.field_size = field_size;

        return levelConfiguration;
    }

    // сегменты dot dot_n... tail_n ... vertical, horizontal, turn ne, turn es...
    public final int segment_by_io[][] = {
            {1, 11, 9, 14},
            {11, 2, 12, 10},
            {9, 12, 3, 13},
            {14, 10, 13, 4}
    };

    // [move_direction][visited_segment]
    private int entrance_to_body[][] = {
            {0,0,0,0,0,0,0,0,0,0,0,     1,0,0,1},
            {0,0,0,0,0,0,0,0,0,0,0,     1,1,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,     0,1,1,0},
            {0,0,0,0,0,0,0,0,0,0,0,     0,0,1,1}
    };

    // [input][output] = ответ на вопрос можно ли так походить
    private int slash_io[][] = {
            {0,0,0,1},
            {0,0,1,0},
            {0,1,0,0},
            {1,0,0,0}
    };
    private int backslash_io[][] = {
            {0,1,0,0},
            {1,0,0,0},
            {0,0,0,1},
            {0,0,1,0}
    };
    private int point_io[][] = {
            {0,1,0,1},
            {1,0,1,0},
            {0,1,0,1},
            {1,0,1,0}
    };
    private int crossroad_io[][] = {
            {0,0,1,0},
            {0,0,0,1},
            {1,0,0,0},
            {0,1,0,0}
    };

    private int negate_direction[] = {2,3,0,1};

    private int direction_to(int x_from, int y_from, int x_to, int y_to){

        int x_diff = x_to - x_from;
        int y_diff = y_to - y_from;

        if (x_diff == 0 && y_diff == 1){
            return 0;
        }
        else if (x_diff == 1 && y_diff == 0){
            return 1;
        }
        else if (x_diff == 0 && y_diff == -1){
            return 2;
        }
        else if (x_diff == -1 && y_diff == 0){
            return 3;
        }
        else {
            return -1;
        }
    }

    boolean can_stay_here(Vector2 xy_to){
        return !slash_walls.contains(xy_to) && !backslash_walls.contains(xy_to) && !boxes.contains(xy_to) && !points.contains(xy_to) && !crossroads.contains(xy_to) && body.indexOf(xy_to) == (body.size() - 1);
    }

    public void update_how_many_visited(){

        how_many_visited = 0;

        for (int x=0; x<field_size; x++){
            for (int y=0; y<field_size; y++){
                if (body.contains(new Vector2(x,y))){
                    how_many_visited++;
                }
            }
        }
    }

    private void cut_tail(){
        body.remove(body.size()-1);
        body_io.remove(body_io.size()-1);
        body_io.get(body_io.size()-1).y = -1;

        update_how_many_visited();
    }

    public void reset_body(){
        how_many_visited = 0;
        body.clear();
        body_io.clear();
        false_path.clear();
        head_is_captured = false; /// new one
    }

    public void normalize_cursor(){
        if (abstract_input_cursor.x < 0){ abstract_input_cursor.x = 0; }
        else if (abstract_input_cursor.x >= field_size){ abstract_input_cursor.x = field_size - 1; }
        if (abstract_input_cursor.y < 0){ abstract_input_cursor.y = 0; }
        else if (abstract_input_cursor.y >= field_size){ abstract_input_cursor.y = field_size - 1; }
    }

    private void normalize_obstacle(ArrayList<Vector2> arr){
        ArrayList<Vector2> items_to_delete = new ArrayList<>();
        for (Vector2 item: arr){
            if (item.x < 0 || item.y < 0 || item.x >=field_size || item.y >= field_size){
                items_to_delete.add(item);
            }
        }
        arr.removeAll(items_to_delete);
    }

    public void normalize_obstacles(){
        normalize_obstacle(vertical_walls);
        normalize_obstacle(horizontal_walls);
        normalize_obstacle(slash_walls);
        normalize_obstacle(backslash_walls);
        normalize_obstacle(boxes);
        normalize_obstacle(points);
        normalize_obstacle(crossroads);
    }

    public MoveResult double_tap_make_move(){
        Vector2 xy_to = new Vector2(abstract_input_cursor);

        // либо тела нет, тогда нужно поставить голову
        if (body.size() == 0) {
            // можно ли ставить
            if (can_stay_here(xy_to)){
                body.add(xy_to);
                body_io.add(new Vector2(-1,-1));
                //System.out.println("Head is set on " + xy_to);
                how_many_visited = 1;
                head_is_captured = true;
                is_tending_to_destroy_the_head = false;
                return MoveResult.HEAD_IS_SET;
            }
            else {
                return MoveResult.HEAD_IS_NOT_SET;
            }
        }
        // если тело есть
        else {
            Vector2 tail = body.get(body.size() - 1);
            //System.out.println("Tail: " + tail);

            // курсор уже на хвосте
            if (xy_to.equals(tail)) {
                // курсор уже на хвосте, длина тела == 1, тогда разрушаем
                if (body.size() == 1) {
                    body.clear();
                    body_io.clear();
                    how_many_visited = 0;
                    //is_tending_to_destroy_the_head = false;
                    head_is_captured = false;
                    return MoveResult.HEAD_IS_DESTROYED;
                }
                // курсор уже на хвосте, тогда переключаем на голову
                else {
                    abstract_input_cursor = new Vector2(body.get(0));

                    boolean body_shortened = false;
                    while (body.size() > 0){
                        if (can_stay_here(body.get(body.size() - 1) )){
                            break;
                        }
                        else {
                            body_shortened = true;
                            cut_tail();
                        }
                    }

                    Collections.reverse(body);
                    Collections.reverse(body_io);
                    for (Vector2 item : body_io) {
                        float tmp = item.x;
                        item.x = item.y;
                        item.y = tmp;
                    }
                    //System.out.println(body);
                    //System.out.println(body_io);
                    //is_tending_to_destroy_the_head = true;
                    head_is_captured = true;
                    false_path.clear(); // чтобы стереть красную дорожку за курсором

                    if (body_shortened){
                        return MoveResult.BODY_IS_SHORTENED;
                    }

                    return MoveResult.OTHER_GOOD;
                }

            }
            // курсор не на хвосте, тогда переключаем на хвост
            else {
                abstract_input_cursor = new Vector2(body.get(body.size() - 1));
                head_is_captured = true;
                false_path.clear(); // чтобы стереть красную дорожку за курсором
                return MoveResult.OTHER_GOOD;
            }
        }
    }

    public MoveResult just_touched_make_move(int x_to, int y_to){
        Vector2 xy_to = new Vector2(x_to, y_to);

        // либо тела нет, тогда нужно поставить голову
        if (body.size() == 0) {
            // можно ли ставить
            if (can_stay_here(xy_to)){
                body.add(xy_to);
                body_io.add(new Vector2(-1,-1));
                //System.out.println("Head is set on " + xy_to);
                how_many_visited = 1;
                head_is_captured = true;
                is_tending_to_destroy_the_head = false;
                return MoveResult.HEAD_IS_SET;
            }
            else {
                //System.out.println("Cant set the head here");
                return MoveResult.HEAD_IS_NOT_SET;
            }
        }
        // если тело есть
        else {
            Vector2 tail = body.get(body.size()-1);
            //System.out.println("Tail: " + tail);

            // выбрал хвост
            if (xy_to.equals(tail)){
                is_tending_to_destroy_the_head = true;
                head_is_captured = true;

                return MoveResult.OTHER_GOOD;
            }
            // выбрал голову
            if (xy_to.equals(body.get(0))){

                boolean body_shortened = false;
                while (body.size() > 0){
                    if (can_stay_here(body.get(body.size() - 1) )){
                        break;
                    }
                    else {
                        body_shortened = true;
                        cut_tail();
                    }
                }

                // надо всё сделать задом наперёд
                //System.out.println("You chose head");
                Collections.reverse(body);
                Collections.reverse(body_io);
                for (Vector2 item : body_io){
                    float tmp = item.x;
                    item.x = item.y;
                    item.y = tmp;
                }
                //System.out.println(body);
                //System.out.println(body_io);
                is_tending_to_destroy_the_head = true;
                head_is_captured = true;

                return MoveResult.OTHER_GOOD;

            }
            // иначе просто тап вникуда
            return MoveResult.OTHER_BAD;

        }
    }

    public MoveResult slide_just_untouched_make_move(){
        if (how_many_visited == how_many_should_be_visited && can_stay_here(body.get(body.size() - 1) )){
            return MoveResult.VICTORY;
        }

        return MoveResult.OTHER_GOOD;
    }

    public MoveResult just_untouched_make_move(int x_to, int y_to){
        head_is_captured = false;
        if (is_tending_to_destroy_the_head) {
            if (body.size() == 1 && body.get(body.size() - 1).equals(new Vector2(x_to, y_to))) {
                // убрать тело
                if (body.size() == 1) {
                    //System.out.println("The head is destroyed");
                    body.clear();
                    body_io.clear();
                    how_many_visited = 0;
                    is_tending_to_destroy_the_head = false;

                    return MoveResult.HEAD_IS_DESTROYED;
                }
            }
        }

        false_path.clear();

        boolean body_shortened = false;
        while (body.size() > 0){
            if (can_stay_here(body.get(body.size() - 1) )){
                break;
            }
            else {
                body_shortened = true;
                cut_tail();
            }
        }

        if (how_many_visited == how_many_should_be_visited){
            return MoveResult.VICTORY;
        }

        // сначала идёт проверка на победу на случай, если палец завёл тело слишком далеко и укорочение привело к победе
        if (body_shortened){
            return MoveResult.BODY_IS_SHORTENED;
        }

        return MoveResult.OTHER_GOOD;

    }

    public MoveResult slide_touched_make_move(){
        return touched_make_move((int)abstract_input_cursor.x, (int)abstract_input_cursor.y);
    }

    public MoveResult touched_make_move(int x_to, int y_to){
        if (!head_is_captured){
            //System.out.println("Head is not captured");
            return MoveResult.HEAD_IS_NOT_CAPTURED;
        }
        //System.out.println("Make move to " + x_to + ", " + y_to);

        Vector2 xy_to = new Vector2(x_to, y_to);
        // граница карты
        if (x_to < 0 || y_to < 0 || x_to >= field_size || y_to >= field_size){
            //System.out.println("Out of bounds");
            return MoveResult.OUT_OF_BOUNDS;
        }

        // коробка
        if (boxes.contains(xy_to)){
            //System.out.println("Move on the box");
            return MoveResult.MOVE_INTO_BOX;
        }

        // если нет тела
        if (body.size() == 0){
            return MoveResult.OTHER_BAD;
        }
        else {
            Vector2 tail = body.get(body.size()-1);
            //System.out.println("Tail: " + tail);

            // остался ли на месте
            if (xy_to.equals(tail)){
                //System.out.println("No movement");
                return MoveResult.NO_MOVEMENT;
            }

            // ход назад
            if (body.size() >= 2 && xy_to.equals(body.get(body.size()-2))){
                //System.out.println("Move back");
                cut_tail();
                is_tending_to_destroy_the_head = false;
                return MoveResult.MOVE_BACK;
            }

            // направление
            int move_direction = direction_to((int)tail.x, (int)tail.y, x_to, y_to);
            //System.out.println("Move direction: " + move_direction);


            // сосед ли
            if (move_direction == -1){
                //System.out.println("Not a neighbor");
                return MoveResult.NOT_A_NEIGHBOR;
            }

            // диагональные стенки
            if (slash_walls.contains(tail)){
                int can_visit = slash_io[(int)body_io.get(body_io.size()-1).x][move_direction];
                if (can_visit == 0){
                    //System.out.println("Movement through the slash wall");
                    return MoveResult.MOVE_THROUGH_SLASH_WALL;
                }

            }
            if (backslash_walls.contains(tail)){
                int can_visit = backslash_io[(int)body_io.get(body_io.size()-1).x][move_direction];
                if (can_visit == 0){
                    //System.out.println("Movement through the backslash wall");
                    return MoveResult.MOVE_THROUGH_BACKSLASH_WALL;
                }

            }

            // точка
            if (points.contains(tail)){
                int can_visit = point_io[(int)body_io.get(body_io.size()-1).x][move_direction];
                if (can_visit == 0){
                    //System.out.println("Movement through the point");
                    return MoveResult.MOVE_THROUGH_POINT;
                }
            }

            // перекрёсток
            if (crossroads.contains(tail)){
                int can_visit = crossroad_io[(int)body_io.get(body_io.size()-1).x][move_direction];
                if (can_visit == 0){
                    //System.out.println("Movement through the crossroad");
                    return MoveResult.MOVE_THROUGH_CROSSROAD;
                }
            }

            if (move_direction == 0){
                if (horizontal_walls.contains(tail)){
                    //System.out.println("Movement through the horizontal_wall");
                    return MoveResult.MOVE_THROUGH_HORIZONTAL_WALL;
                }
            }
            if (move_direction == 2){
                if (horizontal_walls.contains(xy_to)){
                    //System.out.println("Movement through the horizontal_wall");
                    return MoveResult.MOVE_THROUGH_HORIZONTAL_WALL;
                }
            }
            if (move_direction == 1){
                if (vertical_walls.contains(tail)){
                    //System.out.println("Movement through the vertical_wall");
                    return MoveResult.MOVE_THROUGH_VERTICAL_WALL;
                }
            }
            if (move_direction == 3){
                if (vertical_walls.contains(xy_to)){
                    //System.out.println("Movement through the vertical_wall");
                    return MoveResult.MOVE_THROUGH_VERTICAL_WALL;
                }
            }

            int i = body.indexOf(xy_to);
            if (i != -1){
                //System.out.println("Segment " + i + ": " + body.get(i) + " is at the same position");

                if (i==0){
                    //System.out.println("This is my tail");
                    return MoveResult.BODY_NOT_VISITED;
                }
                if (body.lastIndexOf(xy_to) != i){
                    //System.out.println("Triple visited");
                    return MoveResult.BODY_NOT_VISITED;
                }

                int visited_segment = segment_by_io[(int)body_io.get(i).x][(int)body_io.get(i).y];
                //System.out.println("Visited segment: " + visited_segment);

                int can_visit = entrance_to_body[move_direction][visited_segment];

                if (can_visit > 0){
                    //System.out.println("Move is done");
                    body.add(xy_to);
                    body_io.get(body_io.size()-1).y = move_direction;
                    body_io.add(new Vector2(negate_direction[move_direction], -1));
                    return MoveResult.BODY_VISITED;
                }
                else {
                    //System.out.println("Can't visit by rules");
                    return MoveResult.BODY_NOT_VISITED;
                }

            }

            //System.out.println("Simple movement");
            body.add(xy_to);
            body_io.get(body_io.size()-1).y = move_direction;
            body_io.add(new Vector2(negate_direction[move_direction], -1));

            how_many_visited++; //update_how_many_visited();

            return MoveResult.SIMPLE_MOVEMENT;
        }
    }

    private void clockwise_turn_item(Vector2 item){
        float new_x = item.y;
        float new_y = -item.x + (field_size - 1);
        item.x = new_x;
        item.y = new_y;
    }
    private void clockwise_turn_array(ArrayList<Vector2> arr){
        for (Vector2 item: arr){
            clockwise_turn_item(item);
        }
    }
    public void clockwise_turn(){
        ArrayList<Vector2> tmp;

        clockwise_turn_array(vertical_walls);
        for (Vector2 item: vertical_walls){
            item.y--;
        }
        clockwise_turn_array(horizontal_walls);
        // тк горизонтальные <--> вертикальные
        tmp = vertical_walls;
        vertical_walls = horizontal_walls;
        horizontal_walls = tmp;


        clockwise_turn_array(slash_walls);
        clockwise_turn_array(backslash_walls);
        // тк slash <--> backslash
        tmp = slash_walls;
        slash_walls = backslash_walls;
        backslash_walls = tmp;

        clockwise_turn_array(boxes);
        clockwise_turn_array(points);
        clockwise_turn_array(crossroads);
        clockwise_turn_array(hint);
        clockwise_turn_item(abstract_input_cursor);

        clockwise_turn_array(body);
        for (Vector2 item: body_io){
            item.x = (item.x + 1) % 4;
            item.y = (item.y + 1) % 4;
        }
    }

    private void counterclockwise_turn_item(Vector2 item){
        float new_x = -item.y + (field_size - 1);
        float new_y = item.x;
        item.x = new_x;
        item.y = new_y;
    }
    private void counterclockwise_turn_array(ArrayList<Vector2> arr){
        for (Vector2 item: arr){
            counterclockwise_turn_item(item);
        }
    }
    public void counterclockwise_turn(){
        ArrayList<Vector2> tmp;

        counterclockwise_turn_array(vertical_walls);
        counterclockwise_turn_array(horizontal_walls);
        for (Vector2 item: horizontal_walls){
            item.x-=1;
        }
        tmp = vertical_walls;
        vertical_walls = horizontal_walls;
        horizontal_walls = tmp;

        counterclockwise_turn_array(slash_walls);
        counterclockwise_turn_array(backslash_walls);
        tmp = slash_walls;
        slash_walls = backslash_walls;
        backslash_walls = tmp;

        counterclockwise_turn_array(boxes);
        counterclockwise_turn_array(points);
        counterclockwise_turn_array(crossroads);
        counterclockwise_turn_array(hint);
        counterclockwise_turn_item(abstract_input_cursor);

        counterclockwise_turn_array(body);
        for (Vector2 item: body_io){
            item.x = (item.x + 3) % 4;  // +3 по модулю 4 равно -1
            item.y = (item.y + 3) % 4;
        }
    }

    private void mirror_turn_item(Vector2 item){
        item.x = field_size - 1 - item.x;
    }
    private void mirror_turn_array(ArrayList<Vector2> arr){
        for (Vector2 item: arr){
            mirror_turn_item(item);
        }
    }
    public void mirror_turn(){
        ArrayList<Vector2> tmp;
        mirror_turn_array(vertical_walls);
        for (Vector2 item: vertical_walls){
            item.x--;
        }
        mirror_turn_array(horizontal_walls);
        mirror_turn_array(slash_walls);
        mirror_turn_array(backslash_walls);

        tmp = slash_walls;
        slash_walls = backslash_walls;
        backslash_walls = tmp;

        mirror_turn_array(boxes);
        mirror_turn_array(points);
        mirror_turn_array(crossroads);
        mirror_turn_array(hint);
        mirror_turn_item(abstract_input_cursor);

        mirror_turn_array(body);
        for (Vector2 item: body_io){
            if (item.x % 2 != 0){
                item.x = (item.x + 2) % 4;  // равносильно двум поворотам по часовой
            }
            if (item.y % 2 != 0){
                item.y = (item.y + 2) % 4;
            }
        }
    }
}
