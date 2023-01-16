package com.quartyom.screens.Zen;

import com.badlogic.gdx.utils.TimeUtils;
import com.quartyom.game_elements.Button;
import com.quartyom.game_elements.GameBottomPanel;
import com.quartyom.interfaces.QuEvent;


public class ZenBottomPanel extends GameBottomPanel {
    public boolean is_active = true;

    public final ZenScreen zenScreen;

    Button reset_button, transform_button, hint_button, skip_button;

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
                ZenBoard zenBoard = zenScreen.zenBoard;
                if (zenBoard.boardDrawer.is_hint_shown){              // уже показана, выключаем
                    zenBoard.boardDrawer.is_hint_shown = false;
                    zenBoard.was_hint_used = true;
                }
                else if (zenBoard.was_hint_used){                     // не показана, но была использована
                    zenBoard.boardDrawer.is_hint_shown = true;
                }
                else if (game.userData.hints_amount > 0){               // значит, если они есть, то уменьшаем на 1
                    game.userData.hints_amount--;
                    zenBoard.boardDrawer.is_hint_shown = true;
                }
                else {
                    game.setScreen("zen_hint");
                }
            }
        });
        hint_button.setHint(game.locale.get("show hint")).addNotification().setSound("click_1");

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
        skip_button.setHint(game.locale.get("skip level")).addNotification().setSound("click_1");

    }

    @Override
    public void resize(){
        super.resize();
        reset_button.resize(first_button_x, first_button_y, button_w, button_h);
        transform_button.resize(first_button_x + panel_w / 4, first_button_y, button_w, button_h);
        hint_button.resize(first_button_x + panel_w / 4 * 2, first_button_y, button_w, button_h);
        skip_button.resize(first_button_x + panel_w / 4 * 3, first_button_y, button_w, button_h);
    }

    @Override
    public void draw(){
        if (!is_active){return;}
        super.draw();

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

        if (game.userData.hints_amount < 1_000) {
            hint_button.setNotification(String.valueOf(game.userData.hints_amount));
        }
        else {
            hint_button.setNotification("1k+");
        }

        long minutes_left = (game.userData.when_to_skip_zen_level - TimeUtils.millis()) / 60_000L;

        if (game.userData.premium_is_on || minutes_left < 0) {
            skip_button.setNotification(null);
        }
        else {
            skip_button.setNotification("<" + (minutes_left + 1L) + "m");
        }
    }

}
