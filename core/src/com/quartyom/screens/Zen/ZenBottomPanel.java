package com.quartyom.screens.Zen;

import com.badlogic.gdx.utils.TimeUtils;
import com.quartyom.game_elements.Button;
import com.quartyom.game_elements.GameBottomPanel;
import com.quartyom.interfaces.QuEvent;


public class ZenBottomPanel extends GameBottomPanel {
    public boolean is_active = true;

    public final ZenScreen zenScreen;

    Button reset_button, transform_button, hint_button, /*save_button*/ skip_button;

    public ZenBottomPanel(final ZenScreen zenScreen){
        super(zenScreen.game);
        this.zenScreen = zenScreen;

        reset_button = new Button("reset", game, new QuEvent() {
            @Override
            public void execute() {
                zenScreen.zenBoard.gameplay.reset_body();
            }
        });
        reset_button.setHint(game.locale.get("reset level")).setSound("click_1");

        transform_button = new Button("transform", game, new QuEvent() {
            @Override
            public void execute() {
                is_active = false;
                zenScreen.zenTransformBottomPanel.is_active = true;
            }
        });
        transform_button.setHint(game.locale.get("transform the field")).setSound("click_1");

        hint_button = new Button("hint", game, new QuEvent() {
            @Override
            public void execute() {
                if (zenScreen.zenBoard.boardDrawer.is_hint_shown){
                    zenScreen.zenBoard.boardDrawer.is_hint_shown = false;
                    return;
                }
                if (game.userData.hints_amount > 0){
                    game.userData.hints_amount--;
                    zenScreen.zenBoard.boardDrawer.is_hint_shown = true;
                }
                else {
                    game.setScreen("zen_hint");
                }
            }
        });
        hint_button.setHint(game.locale.get("show hint")).setSound("click_1");

        /*save_button = new Button("save", zenScreen.game, new EventHandler() {
            @Override
            public void execute() {
                //System.out.println("pressed save button");
                LevelConfiguration levelConfiguration = zenScreen.zenBoard.gameplay.get_level_configuration();
                Gdx.files.local(String.valueOf("user_levels/" + System.currentTimeMillis() + ".json")).writeString(zenScreen.game.json.prettyPrint(levelConfiguration), false);
            }
        });
        save_button.setSound("click_1");*/

        skip_button = new Button("next", game, new QuEvent() {
            @Override
            public void execute() {
                if (TimeUtils.millis() >= game.userData.when_to_skip_zen_level || game.userData.premium_is_on){

                    zenScreen.zenBoard.current_level++;
                    game.userData.current_zen_level = zenScreen.zenBoard.current_level;
                    game.save_user_data();

                    zenScreen.zenBoard.next_level();
                    game.setScreen("zen");
                }
                else {
                    game.setScreen("zen_skip");
                }
            }
        });
        skip_button.setHint(game.locale.get("skip level")).setSound("click_1");

    }

    @Override
    public void resize(){
        super.resize();
        reset_button.resize(first_button_x, first_button_y, button_w, button_h);
        transform_button.resize(first_button_x + panel_w / 4, first_button_y, button_w, button_h);
        hint_button.resize(first_button_x + panel_w / 4 * 2, first_button_y, button_w, button_h);
        //save_button.resize(first_button_x + panel_w / 4 * 3, first_button_y, button_w, button_h);
        skip_button.resize(first_button_x + panel_w / 4 * 3, first_button_y, button_w, button_h);
    }

    @Override
    public void draw(){
        if (!is_active){return;}
        super.draw();

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
