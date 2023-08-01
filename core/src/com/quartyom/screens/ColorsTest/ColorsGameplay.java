package com.quartyom.screens.ColorsTest;

import com.badlogic.gdx.math.Vector2;
import com.quartyom.screens.Level.MoveResult;

import java.util.ArrayList;
import java.util.Collections;

// реализует чистую игровую логику
public class ColorsGameplay {
    public ArrayList<Vector2> vertical_walls, horizontal_walls, slash_walls, backslash_walls, boxes, points, crossroads;

    public ArrayList<ArrayList<Vector2>> all_bodies, all_bodies_io;
    public ArrayList<Integer> all_bodies_color_id;

    public ArrayList<Vector2> body, body_io;
    public Integer body_color_id;
    public ArrayList<Vector2> falsePath;
    //public ArrayList<Vector2> hint;
    public ArrayList<ColorsSource> colors_sources;

    public Vector2 abstractInputCursor;

    public boolean headIsCaptured;    // держит ли палец на голове
    // когда только поставили голову - false, при ходе назад - false, если держит палец на голове или хвосте - true
    public boolean isTendingToDestroyTheHead;
    public boolean isTendingToPaintBody;


    public int howManyVisited;
    public int howManyShouldBeVisited;

    public int field_size;

    public ColorsGameplay() {
        all_bodies = new ArrayList<>();
        all_bodies_io = new ArrayList<>();
        all_bodies_color_id = new ArrayList<>();

        falsePath = new ArrayList<>();

        abstractInputCursor = new Vector2();
    }

    public void setLevelConfiguration(LevelColorsConfiguration levelColorsConfiguration) {
        vertical_walls = levelColorsConfiguration.vertical_walls;
        horizontal_walls = levelColorsConfiguration.horizontal_walls;
        slash_walls = levelColorsConfiguration.slash_walls;
        backslash_walls = levelColorsConfiguration.backslash_walls;
        boxes = levelColorsConfiguration.boxes;
        points = levelColorsConfiguration.points;
        crossroads = levelColorsConfiguration.crossroads;
        colors_sources = levelColorsConfiguration.colors_sources;
        field_size = levelColorsConfiguration.field_size;

        resetAllBodies();
        howManyShouldBeVisited = field_size * field_size - boxes.size();
        isTendingToDestroyTheHead = false;
    }

//    public void setHint() {
//        if (body != null && body.size() > 0) {
//            if (hint == null) {
//                hint = new ArrayList<>();
//            }
//            if (!hint.isEmpty()) {
//                hint.clear();
//            }
//            hint.add(body.get(0));
//            hint.add(body.get(body.size() - 1));
//        }
//    }

    public LevelColorsConfiguration getLevelConfiguration() {
        LevelColorsConfiguration levelColorsConfiguration = new LevelColorsConfiguration();
        levelColorsConfiguration.vertical_walls = vertical_walls;
        levelColorsConfiguration.horizontal_walls = horizontal_walls;
        levelColorsConfiguration.slash_walls = slash_walls;
        levelColorsConfiguration.backslash_walls = backslash_walls;
        levelColorsConfiguration.boxes = boxes;
        levelColorsConfiguration.points = points;
        levelColorsConfiguration.crossroads = crossroads;
        levelColorsConfiguration.colors_sources = colors_sources;
        levelColorsConfiguration.field_size = field_size;

        return levelColorsConfiguration;
    }

    // сегменты dot dot_n... tail_n ... vertical, horizontal, turn ne, turn es...
    public static final int SEGMENT_BY_IO[][] = {
            {1, 11, 9, 14},
            {11, 2, 12, 10},
            {9, 12, 3, 13},
            {14, 10, 13, 4}
    };

    // [move_direction][visited_segment]
    private static final int ENTRANCE_TO_BODY[][] = {
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1}
    };

    // [input][output] = ответ на вопрос можно ли так походить
    private static final int SLASH_IO[][] = {
            {0, 0, 0, 1},
            {0, 0, 1, 0},
            {0, 1, 0, 0},
            {1, 0, 0, 0}
    };
    private static final int BACKSLASH_IO[][] = {
            {0, 1, 0, 0},
            {1, 0, 0, 0},
            {0, 0, 0, 1},
            {0, 0, 1, 0}
    };
    private static final int POINT_IO[][] = {
            {0, 1, 0, 1},
            {1, 0, 1, 0},
            {0, 1, 0, 1},
            {1, 0, 1, 0}
    };
    private static final int CROSSROAD_IO[][] = {
            {0, 0, 1, 0},
            {0, 0, 0, 1},
            {1, 0, 0, 0},
            {0, 1, 0, 0}
    };

    private static final int NEGATE_DIRECTION[] = {2, 3, 0, 1};

    private static int directionTo(int xFrom, int yFrom, int xTo, int yTo) {

        int xDiff = xTo - xFrom;
        int yDiff = yTo - yFrom;

        if (xDiff == 0 && yDiff == 1) {
            return 0;
        } else if (xDiff == 1 && yDiff == 0) {
            return 1;
        } else if (xDiff == 0 && yDiff == -1) {
            return 2;
        } else if (xDiff == -1 && yDiff == 0) {
            return 3;
        } else {
            return -1;
        }
    }

    boolean canStayHere(Vector2 xy_to) {
        for (ArrayList<Vector2> item: all_bodies){      // нельзя остаться в другом теле
            if (item != body && item.contains(xy_to)){
                return false;
            }
        }
        return !slash_walls.contains(xy_to) && !backslash_walls.contains(xy_to) && !boxes.contains(xy_to) && !points.contains(xy_to) && !crossroads.contains(xy_to) && body.indexOf(xy_to) == (body.size() - 1);
    }

    boolean canPutHeadHere(Vector2 xy_to) {
        if (!canStayHere(xy_to)){       // должно быть можно оставаться
            return false;
        }
        for (int i_body = 0; i_body < all_bodies.size(); i_body++) {
            if (all_bodies.get(i_body) != body      // это другое тело
                    && all_bodies_color_id.get(i_body).equals(body_color_id)){  // и у него тот же цвет
                return false;
            }
        }
        return true;
    }

    int getSourceColor(Vector2 xy){
        int color_from_source = -1;
        for (ColorsSource item : colors_sources){
            if (item.pos.equals(xy)){
                return item.color_id;
            }
        }
        return color_from_source;
    }

    boolean isBodyColorAllowedHere(Vector2 xy_to) {
        int color_here = getSourceColor(xy_to);
        isTendingToPaintBody = color_here != -1;    // то есть если клетка цветная, тут true
        // System.out.println(isTendingToPaintBody + " " + color_here + " " + body_color_id);
        // можно ходить в нейтральную клетку
        // нейтральное тело может зайти куда угодно
        // иначе цвета должны совпасть
        if (body_color_id == -1) {  // если тело нейтральное, заходит на цветную клетку, но этот цвет занят
            if (color_here != -1 && all_bodies_color_id.contains(color_here)){
                return false;
            }
            return true;
        }
        return color_here == -1 || color_here == body_color_id;
    }

    public void updateHowManyVisited() {
        howManyVisited = 0;
        for (int x = 0; x < field_size; x++) {
            for (int y = 0; y < field_size; y++) {
                for (int i_body = 0; i_body < all_bodies.size(); i_body++){
                    if (all_bodies.get(i_body).contains(new Vector2(x, y))  // если тело проходит клетку
                            && all_bodies_color_id.get(i_body) != -1) {      // и цвет не нейтральный
                        howManyVisited++;
                        break;
                    }
                }
            }
        }
    }

    public void updateBodyColor() {
        int index = all_bodies.indexOf(body);
        for (ColorsSource item : colors_sources){
            Vector2 color_pos = item.pos;
            if (body.contains(color_pos)){
                body_color_id = item.color_id;
                all_bodies_color_id.set(index, body_color_id);
                return;
            }
        }
        body_color_id = -1;
        all_bodies_color_id.set(index, body_color_id);
    }

    private void cutTail() {
        body.remove(body.size() - 1);
        body_io.remove(body_io.size() - 1);
        body_io.get(body_io.size() - 1).y = -1;
        updateBodyColor();
        updateHowManyVisited();
    }

    public void clearBody() {
        int index = all_bodies.indexOf(body);
        all_bodies.remove(index);
        all_bodies_io.remove(index);
        all_bodies_color_id.remove(index);
    }

    public void resetAllBodies() {
        howManyVisited = 0;
        all_bodies.clear();
        all_bodies_io.clear();
        all_bodies_color_id.clear();
        if (body != null) { body.clear(); }
        if (body_io != null) { body_io.clear(); }
        falsePath.clear();
        headIsCaptured = false; /// new one
    }

    public void normalizeCursor() {
        if (abstractInputCursor.x < 0) {
            abstractInputCursor.x = 0;
        } else if (abstractInputCursor.x >= field_size) {
            abstractInputCursor.x = field_size - 1;
        }
        if (abstractInputCursor.y < 0) {
            abstractInputCursor.y = 0;
        } else if (abstractInputCursor.y >= field_size) {
            abstractInputCursor.y = field_size - 1;
        }
    }

    private void normalizeObstacle(ArrayList<Vector2> arr) {
        if (field_size <= 1) {
            arr.clear();
        } else {
            ArrayList<Vector2> itemsToDelete = new ArrayList<>();
            for (Vector2 item : arr) {
                if (item.x < 0 || item.y < 0 || item.x >= field_size || item.y >= field_size) {
                    itemsToDelete.add(item);
                }
            }
            arr.removeAll(itemsToDelete);
        }
    }

    public void normalizeObstacles() {
        normalizeObstacle(vertical_walls);
        normalizeObstacle(horizontal_walls);
        normalizeObstacle(slash_walls);
        normalizeObstacle(backslash_walls);
        normalizeObstacle(boxes);
        normalizeObstacle(points);
        normalizeObstacle(crossroads);
    }

    public void catch_certain_body(Vector2 xy) {    // либо вернёт существующее тело, либо создаст новое с цветом null
            for (int i_body = 0; i_body < all_bodies.size(); i_body++){
                ArrayList<Vector2> item = all_bodies.get(i_body);
                if (item.get(0).equals(xy) || item.get(item.size()-1).equals(xy)) {  // голова или хвост
                    body = item;
                    body_io = all_bodies_io.get(i_body);
                    body_color_id = all_bodies_color_id.get(i_body);
                    return;
                }
            }
            // или создаём новое тело
            body = new ArrayList<>();
            body_io = new ArrayList<>();
            body_color_id = getSourceColor(xy);

            all_bodies.add(body);
            all_bodies_io.add(body_io);
            all_bodies_color_id.add(body_color_id);
    }

    public MoveResult doubleTapMakeMove() {
        Vector2 xy_to = new Vector2(abstractInputCursor);
        catch_certain_body(xy_to);

        // либо тела нет, тогда нужно поставить голову
        if (body.size() == 0) {
            // можно ли ставить
            if (canPutHeadHere(xy_to)) {
                body.add(xy_to);
                body_io.add(new Vector2(-1, -1));
                updateHowManyVisited();
                headIsCaptured = true;
                isTendingToDestroyTheHead = false;
                return MoveResult.HEAD_IS_SET;
            } else {
                clearBody();
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
                    clearBody();
                    updateHowManyVisited();
                    headIsCaptured = false;
                    return MoveResult.HEAD_IS_DESTROYED;
                }
                // курсор уже на хвосте, тогда переключаем на голову
                else {
                    abstractInputCursor = new Vector2(body.get(0));

                    boolean bodyShortened = false;
                    while (body.size() > 0) {
                        if (canStayHere(body.get(body.size() - 1))) {
                            break;
                        } else {
                            bodyShortened = true;
                            cutTail();
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
                    headIsCaptured = true;
                    falsePath.clear(); // чтобы стереть красную дорожку за курсором

                    if (bodyShortened) {
                        return MoveResult.BODY_IS_SHORTENED;
                    }

                    return MoveResult.OTHER_GOOD;
                }

            }
            // курсор не на хвосте, тогда переключаем на хвост
            else {
                abstractInputCursor = new Vector2(body.get(body.size() - 1));
                headIsCaptured = true;
                falsePath.clear(); // чтобы стереть красную дорожку за курсором
                return MoveResult.OTHER_GOOD;
            }
        }
    }

    public MoveResult justTouchedMakeMove(int x_to, int y_to) {
        Vector2 xy_to = new Vector2(x_to, y_to);
        catch_certain_body(xy_to);

        // либо тела нет, тогда нужно поставить голову
        if (body.size() == 0) {
            // можно ли ставить
            if (canPutHeadHere(xy_to)) {
                body.add(xy_to);
                body_io.add(new Vector2(-1, -1));
                updateHowManyVisited();
                headIsCaptured = true;
                isTendingToDestroyTheHead = false;
                return MoveResult.HEAD_IS_SET;
            } else {
                clearBody();
                //System.out.println("Cant set the head here");
                return MoveResult.HEAD_IS_NOT_SET;
            }
        }
        // если тело есть
        else {
            Vector2 tail = body.get(body.size() - 1);
            //System.out.println("Tail: " + tail);

            // выбрал хвост
            if (xy_to.equals(tail)) {
                isTendingToDestroyTheHead = true;
                headIsCaptured = true;

                return MoveResult.OTHER_GOOD;
            }
            // выбрал голову
            if (xy_to.equals(body.get(0))) {

                boolean bodyShortened = false;
                while (body.size() > 0) {
                    if (canStayHere(body.get(body.size() - 1))) {
                        break;
                    } else {
                        bodyShortened = true;
                        cutTail();
                    }
                }

                // надо всё сделать задом наперёд
                //System.out.println("You chose head");
                Collections.reverse(body);
                Collections.reverse(body_io);
                for (Vector2 item : body_io) {
                    float tmp = item.x;
                    item.x = item.y;
                    item.y = tmp;
                }
                //System.out.println(body);
                //System.out.println(body_io);
                isTendingToDestroyTheHead = true;
                headIsCaptured = true;

                return MoveResult.OTHER_GOOD;

            }
            // иначе просто тап вникуда
            return MoveResult.OTHER_BAD;

        }
    }

    public MoveResult slideJustUntouchedMakeMove() {
        if (howManyVisited == howManyShouldBeVisited && canStayHere(body.get(body.size() - 1))) {
            return MoveResult.VICTORY;
        }

        return MoveResult.OTHER_GOOD;
    }

    public MoveResult justUntouchedMakeMove(int x_to, int y_to) {
        headIsCaptured = false;
        if (isTendingToDestroyTheHead) {
            if (body.size() == 1 && body.get(body.size() - 1).equals(new Vector2(x_to, y_to))) {
                // убрать тело
                if (body.size() == 1) {
                    //System.out.println("The head is destroyed");
//                    body.clear();
//                    body_io.clear();
//                    howManyVisited = 0;
                    clearBody();
                    updateHowManyVisited();
                    isTendingToDestroyTheHead = false;

                    return MoveResult.HEAD_IS_DESTROYED;
                }
            }
        }

        falsePath.clear();

        boolean bodyShortened = false;
        while (body.size() > 0) {
            if (canStayHere(body.get(body.size() - 1))) {
                break;
            } else {
                bodyShortened = true;
                cutTail();
            }
        }

        if (howManyVisited == howManyShouldBeVisited) {
            return MoveResult.VICTORY;
        }

        // сначала идёт проверка на победу на случай, если палец завёл тело слишком далеко и укорочение привело к победе
        if (bodyShortened) {
            return MoveResult.BODY_IS_SHORTENED;
        }

        return MoveResult.OTHER_GOOD;

    }

    public MoveResult slideTouchedMakeMove() {
        return touchedMakeMove((int) abstractInputCursor.x, (int) abstractInputCursor.y);
    }

    public MoveResult touchedMakeMove(int x_to, int y_to) {
        if (!headIsCaptured) {
            //System.out.println("Head is not captured");
            return MoveResult.HEAD_IS_NOT_CAPTURED;
        }
        //System.out.println("Make move to " + x_to + ", " + y_to);

        Vector2 xy_to = new Vector2(x_to, y_to);
        // граница карты
        if (x_to < 0 || y_to < 0 || x_to >= field_size || y_to >= field_size) {
            //System.out.println("Out of bounds");
            return MoveResult.OUT_OF_BOUNDS;
        }

        // не тот цвет
        if (!isBodyColorAllowedHere(xy_to)){
            //System.out.println("Wrong color");
            return MoveResult.MOVE_INTO_BOX;
        }

        // коробка
        if (boxes.contains(xy_to)) {
            //System.out.println("Move on the box");
            return MoveResult.MOVE_INTO_BOX;
        }

        // если нет тела
        if (body.size() == 0) {
            return MoveResult.OTHER_BAD;
        } else {
            Vector2 tail = body.get(body.size() - 1);
            //System.out.println("Tail: " + tail);

            // остался ли на месте
            if (xy_to.equals(tail)) {
                //System.out.println("No movement");
                return MoveResult.NO_MOVEMENT;
            }

            // ход назад
            if (body.size() >= 2 && xy_to.equals(body.get(body.size() - 2))) {
                //System.out.println("Move back");
                cutTail();
                isTendingToDestroyTheHead = false;
                return MoveResult.MOVE_BACK;
            }

            // направление
            int moveDirection = directionTo((int) tail.x, (int) tail.y, x_to, y_to);
            //System.out.println("Move direction: " + move_direction);


            // сосед ли
            if (moveDirection == -1) {
                //System.out.println("Not a neighbor");
                return MoveResult.NOT_A_NEIGHBOR;
            }

            // диагональные стенки
            if (slash_walls.contains(tail)) {
                int canVisit = SLASH_IO[(int) body_io.get(body_io.size() - 1).x][moveDirection];
                if (canVisit == 0) {
                    //System.out.println("Movement through the slash wall");
                    return MoveResult.MOVE_THROUGH_SLASH_WALL;
                }

            }
            if (backslash_walls.contains(tail)) {
                int canVisit = BACKSLASH_IO[(int) body_io.get(body_io.size() - 1).x][moveDirection];
                if (canVisit == 0) {
                    //System.out.println("Movement through the backslash wall");
                    return MoveResult.MOVE_THROUGH_BACKSLASH_WALL;
                }

            }

            // точка
            if (points.contains(tail)) {
                int canVisit = POINT_IO[(int) body_io.get(body_io.size() - 1).x][moveDirection];
                if (canVisit == 0) {
                    //System.out.println("Movement through the point");
                    return MoveResult.MOVE_THROUGH_POINT;
                }
            }

            // перекрёсток
            if (crossroads.contains(tail)) {
                int canVisit = CROSSROAD_IO[(int) body_io.get(body_io.size() - 1).x][moveDirection];
                if (canVisit == 0) {
                    //System.out.println("Movement through the crossroad");
                    return MoveResult.MOVE_THROUGH_CROSSROAD;
                }
            }

            if (moveDirection == 0) {
                if (horizontal_walls.contains(tail)) {
                    //System.out.println("Movement through the horizontal_wall");
                    return MoveResult.MOVE_THROUGH_HORIZONTAL_WALL;
                }
            }
            if (moveDirection == 2) {
                if (horizontal_walls.contains(xy_to)) {
                    //System.out.println("Movement through the horizontal_wall");
                    return MoveResult.MOVE_THROUGH_HORIZONTAL_WALL;
                }
            }
            if (moveDirection == 1) {
                if (vertical_walls.contains(tail)) {
                    //System.out.println("Movement through the vertical_wall");
                    return MoveResult.MOVE_THROUGH_VERTICAL_WALL;
                }
            }
            if (moveDirection == 3) {
                if (vertical_walls.contains(xy_to)) {
                    //System.out.println("Movement through the vertical_wall");
                    return MoveResult.MOVE_THROUGH_VERTICAL_WALL;
                }
            }

            for (int i_body=0; i_body<all_bodies.size(); i_body++){
                ArrayList<Vector2> other_body = all_bodies.get(i_body);
                ArrayList<Vector2> other_body_io = all_bodies_io.get(i_body);
                int i = other_body.indexOf(xy_to);
                // то есть ход внутри тела
                if (i != -1) {
                    //System.out.println("Segment " + i + ": " + body.get(i) + " is at the same position");

                    if (i == 0 || i == other_body.size() - 1) {
                        //System.out.println("This is my tail");
                        return MoveResult.BODY_NOT_VISITED;
                    }
                    // ???
//                    if (body.lastIndexOf(xy_to) != i) {
//                        //System.out.println("Triple visited");
//                        return MoveResult.BODY_NOT_VISITED;
//                    }

                    int visitedSegment = SEGMENT_BY_IO[(int) other_body_io.get(i).x][(int) other_body_io.get(i).y];
                    //System.out.println("Visited segment: " + visited_segment);

                    int canVisit = ENTRANCE_TO_BODY[moveDirection][visitedSegment];

                    if (canVisit > 0) {
                        //System.out.println("Move is done");
                        body.add(xy_to);
                        body_io.get(body_io.size() - 1).y = moveDirection;
                        body_io.add(new Vector2(NEGATE_DIRECTION[moveDirection], -1));
                        if (isTendingToPaintBody) { updateBodyColor(); }
                        // updateHowManyVisited();
                        return MoveResult.BODY_VISITED;
                    } else {
                        //System.out.println("Can't visit by rules");
                        return MoveResult.BODY_NOT_VISITED;
                    }

                }
            }
            // int i = body.indexOf(xy_to);

            //System.out.println("Simple movement");
            body.add(xy_to);
            body_io.get(body_io.size() - 1).y = moveDirection;
            body_io.add(new Vector2(NEGATE_DIRECTION[moveDirection], -1));
            if (isTendingToPaintBody) { updateBodyColor(); }

            updateHowManyVisited();

            return MoveResult.SIMPLE_MOVEMENT;
        }
    }

    private void clockwiseTurnItem(Vector2 item) {
        float newX = item.y;
        float newY = -item.x + (field_size - 1);
        item.x = newX;
        item.y = newY;
    }

    private void clockwiseTurnArray(ArrayList<Vector2> arr) {
        for (Vector2 item : arr) {
            clockwiseTurnItem(item);
        }
    }

    public void clockwiseTurn() {
        ArrayList<Vector2> tmp;

        clockwiseTurnArray(vertical_walls);
        for (Vector2 item : vertical_walls) {
            item.y--;
        }
        clockwiseTurnArray(horizontal_walls);
        // тк горизонтальные <--> вертикальные
        tmp = vertical_walls;
        vertical_walls = horizontal_walls;
        horizontal_walls = tmp;


        clockwiseTurnArray(slash_walls);
        clockwiseTurnArray(backslash_walls);
        // тк slash <--> backslash
        tmp = slash_walls;
        slash_walls = backslash_walls;
        backslash_walls = tmp;

        clockwiseTurnArray(boxes);
        clockwiseTurnArray(points);
        clockwiseTurnArray(crossroads);
        // clockwiseTurnArray(hint);
        clockwiseTurnItem(abstractInputCursor);

        for (ArrayList<Vector2> item : all_bodies){
            clockwiseTurnArray(item);
        }
        for (ArrayList<Vector2> item_io : all_bodies_io) {
            for (Vector2 item : item_io) {
                item.x = (item.x + 1) % 4;
                item.y = (item.y + 1) % 4;
            }
        }
        for (ColorsSource item: colors_sources){
            clockwiseTurnItem(item.pos);
        }
    }

    private void counterclockwiseTurnItem(Vector2 item) {
        float newX = -item.y + (field_size - 1);
        float newY = item.x;
        item.x = newX;
        item.y = newY;
    }

    private void counterclockwiseTurnArray(ArrayList<Vector2> arr) {
        for (Vector2 item : arr) {
            counterclockwiseTurnItem(item);
        }
    }

    public void counterclockwiseTurn() {
        ArrayList<Vector2> tmp;

        counterclockwiseTurnArray(vertical_walls);
        counterclockwiseTurnArray(horizontal_walls);
        for (Vector2 item : horizontal_walls) {
            item.x -= 1;
        }
        tmp = vertical_walls;
        vertical_walls = horizontal_walls;
        horizontal_walls = tmp;

        counterclockwiseTurnArray(slash_walls);
        counterclockwiseTurnArray(backslash_walls);
        tmp = slash_walls;
        slash_walls = backslash_walls;
        backslash_walls = tmp;

        counterclockwiseTurnArray(boxes);
        counterclockwiseTurnArray(points);
        counterclockwiseTurnArray(crossroads);
        // counterclockwiseTurnArray(hint);
        counterclockwiseTurnItem(abstractInputCursor);

        for (ArrayList<Vector2> item : all_bodies) {
            counterclockwiseTurnArray(item);
        }
        for (ArrayList<Vector2> item_io : all_bodies_io) {
            for (Vector2 item : item_io) {
                item.x = (item.x + 3) % 4;  // +3 по модулю 4 равно -1
                item.y = (item.y + 3) % 4;
            }
        }
        for (ColorsSource item: colors_sources){
            counterclockwiseTurnItem(item.pos);
        }
    }

    private void mirrorTurnItem(Vector2 item) {
        item.x = field_size - 1 - item.x;
    }

    private void mirrorTurnArray(ArrayList<Vector2> arr) {
        for (Vector2 item : arr) {
            mirrorTurnItem(item);
        }
    }

    public void mirrorTurn() {
        ArrayList<Vector2> tmp;
        mirrorTurnArray(vertical_walls);
        for (Vector2 item : vertical_walls) {
            item.x--;
        }
        mirrorTurnArray(horizontal_walls);
        mirrorTurnArray(slash_walls);
        mirrorTurnArray(backslash_walls);

        tmp = slash_walls;
        slash_walls = backslash_walls;
        backslash_walls = tmp;

        mirrorTurnArray(boxes);
        mirrorTurnArray(points);
        mirrorTurnArray(crossroads);
        // mirrorTurnArray(hint);
        mirrorTurnItem(abstractInputCursor);

        for (ArrayList<Vector2> item : all_bodies) {
            mirrorTurnArray(item);
        }
        for (ArrayList<Vector2> item_io : all_bodies_io) {
            for (Vector2 item : item_io) {
                if (item.x % 2 != 0) {
                    item.x = (item.x + 2) % 4;  // равносильно двум поворотам по часовой
                }
                if (item.y % 2 != 0) {
                    item.y = (item.y + 2) % 4;
                }
            }
        }
        for (ColorsSource item: colors_sources){
            mirrorTurnItem(item.pos);
        }
    }
}
