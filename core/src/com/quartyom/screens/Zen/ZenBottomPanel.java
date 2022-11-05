package com.quartyom.screens.Zen;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.TimeUtils;
import com.quartyom.game_elements.Button;
import com.quartyom.interfaces.QuEvent;


public class ZenBottomPanel {
    public boolean is_active = true;

    public final ZenScreen zenScreen;
    TextureRegion texture;

    //Button reset_button, turn_clockwise_button, turn_counterclockwise_button, mirror_button;
    Button reset_button, transform_button, hint_button, /*save_button*/ skip_button;

    private float panel_x, panel_y, panel_w, panel_h;

    public ZenBottomPanel(final ZenScreen zenScreen){
        this.zenScreen = zenScreen;

        texture = zenScreen.game.field_atlas.findRegion("bottom_panel");

        reset_button = new Button("reset", zenScreen.game, new QuEvent() {
            @Override
            public void execute() {
                zenScreen.zenBoard.gameplay.reset_body();
            }
        });
        reset_button.setHint(zenScreen.game.locale.get("reset level")).setSound("click_1");

        transform_button = new Button("transform", zenScreen.game, new QuEvent() {
            @Override
            public void execute() {
                is_active = false;
                zenScreen.zenTransformBottomPanel.is_active = true;
            }
        });
        transform_button.setHint(zenScreen.game.locale.get("transform the field")).setSound("click_1");

        hint_button = new Button("hint", zenScreen.game, new QuEvent() {
            @Override
            public void execute() {
                if (zenScreen.zenBoard.boardDrawer.is_hint_shown){
                    zenScreen.zenBoard.boardDrawer.is_hint_shown = false;
                    return;
                }
                if (zenScreen.game.userData.hints_amount > 0){
                    zenScreen.game.userData.hints_amount--;
                    zenScreen.zenBoard.boardDrawer.is_hint_shown = true;
                }
                else {
                    zenScreen.game.setScreen("zen_hint");
                }
            }
        });
        hint_button.setHint(zenScreen.game.locale.get("show hint")).setSound("click_1");

        /*save_button = new Button("save", zenScreen.game, new EventHandler() {
            @Override
            public void execute() {
                //System.out.println("pressed save button");
                LevelConfiguration levelConfiguration = zenScreen.zenBoard.gameplay.get_level_configuration();
                Gdx.files.local(String.valueOf("user_levels/" + System.currentTimeMillis() + ".json")).writeString(zenScreen.game.json.prettyPrint(levelConfiguration), false);
            }
        });
        save_button.setSound("click_1");*/

        skip_button = new Button("next", zenScreen.game, new QuEvent() {
            @Override
            public void execute() {
                if (TimeUtils.millis() >= zenScreen.game.userData.when_to_skip_zen_level ||zenScreen.game.userData.premium_is_on){

                    zenScreen.zenBoard.current_level++;
                    zenScreen.game.userData.current_zen_level = zenScreen.zenBoard.current_level;
                    zenScreen.game.save_user_data();

                    zenScreen.zenBoard.next_level();
                    zenScreen.game.setScreen("zen");
                }
                else {
                    zenScreen.game.setScreen("zen_skip");
                }
            }
        });
    }

    public void resize(){

        panel_x = -zenScreen.game.HALF_WIDTH;
        panel_y = -zenScreen.game.HALF_HEIGHT;
        panel_w = zenScreen.game.WIDTH;
        panel_h = (0.5f / 4) * zenScreen.game.HEIGHT;

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
        transform_button.resize(first_button_x + panel_w / 4, first_button_y, button_w, button_h);
        hint_button.resize(first_button_x + panel_w / 4 * 2, first_button_y, button_w, button_h);
        //save_button.resize(first_button_x + panel_w / 4 * 3, first_button_y, button_w, button_h);
        skip_button.resize(first_button_x + panel_w / 4 * 3, first_button_y, button_w, button_h);
    }

    public void draw(){
        if (!is_active){return;}

        zenScreen.game.batch.draw(texture, panel_x, panel_y, panel_w, panel_h);

        reset_button.draw();
        transform_button.draw();
        hint_button.draw();
        //save_button.draw();
        skip_button.draw();
    }

    public void update(){
        if (!is_active){return;}

        reset_button.update();
        transform_button.update();
        hint_button.update();
        //save_button.update();
        skip_button.update();
    }

}
