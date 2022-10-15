package com.quartyom.screens.Level;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.quartyom.game_elements.Button;
import com.quartyom.interfaces.QuEvent;

public class LevelTransformBottomPanel {
    public boolean is_active = false;

    public final LevelScreen levelScreen;
    TextureRegion texture;

    Button back_button, turn_clockwise_button, turn_counterclockwise_button, mirror_button;

    private float panel_x, panel_y, panel_w, panel_h;

    public LevelTransformBottomPanel(final LevelScreen levelScreen){
        this.levelScreen = levelScreen;

        texture = levelScreen.game.field_atlas.findRegion("bottom_panel");

        back_button = new Button("back", levelScreen.game, new QuEvent() {
            @Override
            public void execute() {
                is_active = false;
                levelScreen.levelBottomPanel.is_active = true;
            }
        });
        back_button.setHint(levelScreen.game.locale.get("back")).setSound("click_1");

        turn_counterclockwise_button = new Button("turn_counterclockwise", levelScreen.game, new QuEvent() {
            @Override
            public void execute() {
                levelScreen.levelBoard.gameplay.counterclockwise_turn();
            }
        });
        turn_counterclockwise_button.setHint(levelScreen.game.locale.get("counterclockwise turn")).setSound("click_1");

        turn_clockwise_button = new Button("turn_clockwise", levelScreen.game, new QuEvent() {
            @Override
            public void execute() {
                levelScreen.levelBoard.gameplay.clockwise_turn();
            }
        });
        turn_clockwise_button.setHint(levelScreen.game.locale.get("clockwise turn")).setSound("click_1");

        mirror_button = new Button("mirror", levelScreen.game, new QuEvent() {
            @Override
            public void execute() {
                levelScreen.levelBoard.gameplay.mirror_turn();
            }
        });
        mirror_button.setHint(levelScreen.game.locale.get("mirror")).setSound("click_1");

    }

    public void resize(){

        panel_x = -levelScreen.game.HALF_WIDTH;
        panel_y = -levelScreen.game.HALF_HEIGHT;
        panel_w = levelScreen.game.WIDTH;
        panel_h = (0.5f / 4) * levelScreen.game.HEIGHT;

        float first_button_center_x = panel_x + panel_w / 2 / 4; // тк 4 кнопки
        float first_button_center_y = panel_y + panel_h / 2;

        float button_actual_size = panel_h / 2;
        //System.out.println(button_actual_size);
        if (panel_w / 2 / 4 < button_actual_size){
            button_actual_size = panel_w / 2 / 4;
        }
        button_actual_size *= 0.9f; // Отступ кнопки от краёв

        float first_button_x = first_button_center_x - button_actual_size;
        float first_button_y = first_button_center_y - button_actual_size;

        //System.out.println(first_button_x + " " + first_button_y);

        float button_w = button_actual_size * 2;
        float button_h = button_actual_size * 2;

        //System.out.println(button_w + " " + button_h);

        back_button.resize(first_button_x, first_button_y, button_w, button_h);
        turn_counterclockwise_button.resize(first_button_x + panel_w / 4, first_button_y, button_w, button_h);
        turn_clockwise_button.resize(first_button_x + panel_w / 4 * 2, first_button_y, button_w, button_h);
        mirror_button.resize(first_button_x + panel_w / 4 * 3, first_button_y, button_w, button_h);

    }

    public void draw(){
        if (!is_active){return;}
        levelScreen.game.batch.draw(texture, panel_x, panel_y, panel_w, panel_h);

        back_button.draw();
        turn_counterclockwise_button.draw();
        turn_clockwise_button.draw();
        mirror_button.draw();

    }

    public void update(){
        if (!is_active){return;}
        back_button.update();
        turn_counterclockwise_button.update();
        turn_clockwise_button.update();
        mirror_button.update();

    }

}
