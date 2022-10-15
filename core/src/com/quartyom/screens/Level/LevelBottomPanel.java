package com.quartyom.screens.Level;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.quartyom.game_elements.Button;
import com.quartyom.interfaces.QuEvent;

public class LevelBottomPanel {
    boolean is_active = true;

    public final LevelScreen levelScreen;
    TextureRegion texture;

    Button reset_button, transform_button,  hint_button, skip_button;

    private float panel_x, panel_y, panel_w, panel_h;

    public LevelBottomPanel(final LevelScreen levelScreen){
        this.levelScreen = levelScreen;

        texture = levelScreen.game.field_atlas.findRegion("bottom_panel");

        reset_button = new Button("reset", levelScreen.game, new QuEvent() {
            @Override
            public void execute() {
                levelScreen.levelBoard.gameplay.reset_body();
            }
        });
        reset_button.setHint(levelScreen.game.locale.get("reset level")).setSound("click_1");

        transform_button = new Button("transform", levelScreen.game, new QuEvent() {
            @Override
            public void execute() {
                is_active = false;
                levelScreen.levelTransformBottomPanel.is_active = true;
            }
        });
        transform_button.setHint(levelScreen.game.locale.get("transform the field")).setSound("click_1");

        hint_button = new Button("hint", levelScreen.game, new QuEvent() {
            @Override
            public void execute() {
                if (levelScreen.levelBoard.boardDrawer.is_hint_shown){
                    levelScreen.levelBoard.boardDrawer.is_hint_shown = false;
                    return;
                }
                if (levelScreen.game.userData.hints_amount > 0){
                    levelScreen.game.userData.hints_amount--;
                    levelScreen.levelBoard.boardDrawer.is_hint_shown = true;
                }
                else {
                    //levelScreen.levelHintTab.is_active = true;
                    levelScreen.game.setScreen("level_hint");
                }
            }
        });
        hint_button.setHint(levelScreen.game.locale.get("show hint")).setSound("click_1");

        skip_button = new Button("next", levelScreen.game, new QuEvent() {
            @Override
            public void execute() {
                if (levelScreen.game.userData.premium_is_on){
                    levelScreen.levelBoard.current_level++;
                    levelScreen.levelBoard.userData.current_level = levelScreen.levelBoard.current_level;
                    levelScreen.game.save_user_data();
                    levelScreen.levelBoard.load_level();

                    levelScreen.game.setScreen("level");
                }
                else {
                    levelScreen.game.setScreen("level_skip");
                }
            }
        });
        skip_button.setHint(levelScreen.game.locale.get("skip level")).setSound("click_1");


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

        reset_button.resize(first_button_x, first_button_y, button_w, button_h);
        transform_button.resize(first_button_x + panel_w / 4 * 1, first_button_y, button_w, button_h);
        hint_button.resize(first_button_x + panel_w / 4 * 2, first_button_y, button_w, button_h);
        skip_button.resize(first_button_x + panel_w / 4 * 3, first_button_y, button_w, button_h);

    }

    public void draw(){
        if (!is_active){return;}
        levelScreen.game.batch.draw(texture, panel_x, panel_y, panel_w, panel_h);

        reset_button.draw();
        transform_button.draw();
        hint_button.draw();
        skip_button.draw();
    }

    public void update(){
        if (!is_active){return;}
        reset_button.update();
        transform_button.update();
        hint_button.update();
        skip_button.update();
    }
}
