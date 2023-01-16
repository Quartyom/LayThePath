package com.quartyom.screens.Level;

import com.badlogic.gdx.utils.TimeUtils;
import com.quartyom.game_elements.Button;
import com.quartyom.game_elements.GameBottomPanel;
import com.quartyom.interfaces.QuEvent;

public class LevelBottomPanel extends GameBottomPanel {
    boolean is_active = true;

    public final LevelScreen levelScreen;
    Button reset_button, transform_button,  hint_button, skip_button;

    public LevelBottomPanel(final LevelScreen levelScreen){
        super(levelScreen.game);
        this.levelScreen = levelScreen;

        reset_button = new Button("reset", game, new QuEvent() {
            @Override
            public void execute() {
                levelScreen.levelBoard.gameplay.reset_body();
            }
        });
        reset_button.setHint(game.locale.get("reset level")).setSound("click_1");

        transform_button = new Button("transform", game, new QuEvent() {
            @Override
            public void execute() {
                is_active = false;
                levelScreen.levelTransformBottomPanel.is_active = true;
            }
        });
        transform_button.setHint(game.locale.get("transform the field")).setSound("click_1");

        hint_button = new Button("hint", game, new QuEvent() {
            @Override
            public void execute() {
                LevelBoard levelBoard = levelScreen.levelBoard;
                if (levelBoard.boardDrawer.is_hint_shown){              // уже показана, выключаем
                    levelBoard.boardDrawer.is_hint_shown = false;
                    levelBoard.was_hint_used = true;
                }
                else if (levelBoard.was_hint_used){                     // не показана, но была использована
                    levelBoard.boardDrawer.is_hint_shown = true;
                }
                else if (game.userData.hints_amount > 0){               // значит, если они есть, то уменьшаем на 1
                    game.userData.hints_amount--;
                    levelBoard.boardDrawer.is_hint_shown = true;
                }
                else {
                    game.setScreen("level_hint");
                }
            }
        });
        hint_button.setHint(game.locale.get("show hint")).addNotification().setSound("click_1");

        skip_button = new Button("next", game, new QuEvent() {
            @Override
            public void execute() {
                LevelBoard levelBoard = levelScreen.levelBoard;
                if (levelBoard.current_level < game.userData.max_level_achieved ||
                        TimeUtils.millis() >= game.userData.when_to_skip_level ||
                        game.userData.premium_is_on){

                    levelBoard.current_level++;
                    levelBoard.userData.current_level = levelBoard.current_level;
                    game.save_user_data();
                    levelBoard.load_level();

                    game.setScreen("level");
                }
                else {
                    game.setScreen("level_skip");
                }
            }
        });
        skip_button.setHint(game.locale.get("skip level")).addNotification().setSound("click_1");
    }

    @Override
    public void resize(){
        super.resize();

        reset_button.resize(first_button_x, first_button_y, button_w, button_h);
        transform_button.resize(first_button_x + panel_w / 4 * 1, first_button_y, button_w, button_h);
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

        long minutes_left = (game.userData.when_to_skip_level - TimeUtils.millis()) / 60_000L;

        if (levelScreen.levelBoard.current_level < game.userData.max_level_achieved || minutes_left < 0 || game.userData.premium_is_on){
            skip_button.setNotification(null);
        }
        else {
            skip_button.setNotification("<" + (minutes_left + 1L) + "m");
        }
    }
}
