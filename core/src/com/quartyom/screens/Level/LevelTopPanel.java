package com.quartyom.screens.Level;

import com.badlogic.gdx.utils.Align;
import com.quartyom.game_elements.GameTopPanel;
import com.quartyom.game_elements.InputState;
import com.quartyom.game_elements.Label;
import com.quartyom.game_elements.Scroller;

public class LevelTopPanel extends GameTopPanel {
    public final LevelScreen levelScreen;
    InputState inputState;

    Label level_label, progress_label;
    Scroller scroller;

    String level_string, progress_string;

    public LevelTopPanel(final LevelScreen levelScreen){
        super(levelScreen.game);
        this.levelScreen = levelScreen;

        inputState = InputState.UNTOUCHED;

        level_label = new Label(game);
        level_string = game.locale.get("Level ");
        progress_string = game.locale.get("Progress ");
        level_label.target_string = level_string + "1000";
        progress_label = new Label(game);
        progress_label.target_string = progress_string + "100 / 100";

        scroller = new Scroller(game);
    }

    @Override
    public void resize(){
        super.resize();

        scroller.resize(panel_x + panel_w * 0.025f, panel_y, panel_w * 0.75f, panel_h);
        level_label.resize(panel_x + panel_w * 0.025f, panel_y + panel_h / 2, panel_w * 0.75f, panel_h / 2, Align.left);
        progress_label.resize(panel_x + panel_w * 0.025f, panel_y, panel_w * 0.75f, panel_h / 2, Align.left);
    }

    @Override
    public void draw(){
        super.draw();

        level_label.string = level_string + levelScreen.levelBoard.current_level;
        level_label.draw();

        Gameplay gameplay = levelScreen.levelBoard.gameplay; // просто для сокращения пути
        progress_label.string = progress_string + gameplay.how_many_visited + " / " + gameplay.how_many_should_be_visited;
        progress_label.draw();
    }

    @Override
    public void update(){
        super.update();

        scroller.update();

        float sensitivity = 50; // количество пикселей, которые нужно пройти, чтобы переключить 1 уровень

        if (scroller.inputState == InputState.JUST_TOUCHED){
            scroller.value.x = levelScreen.levelBoard.current_level * sensitivity;
        }

        else if (scroller.inputState == InputState.TOUCHED){
            if ((Math.round(scroller.value.x / sensitivity)) != levelScreen.levelBoard.current_level){
                levelScreen.levelBoard.current_level = (Math.round(scroller.value.x / sensitivity));

                boolean to_change_scroller = true;

                if (levelScreen.levelBoard.current_level < 1){
                    levelScreen.levelBoard.current_level += game.userData.max_level_achieved;
                    if (levelScreen.levelBoard.current_level > levelScreen.levelBoard.how_many_levels){
                        levelScreen.levelBoard.current_level = levelScreen.levelBoard.how_many_levels;
                    }
                }
                else if (levelScreen.levelBoard.current_level > levelScreen.levelBoard.how_many_levels){
                    levelScreen.levelBoard.current_level = 1;
                }
                else if (levelScreen.levelBoard.current_level > game.userData.max_level_achieved){
                    levelScreen.levelBoard.current_level -= game.userData.max_level_achieved;
                }
                else {
                    to_change_scroller = false;
                }
                if (to_change_scroller){
                    scroller.value.x = levelScreen.levelBoard.current_level * sensitivity;
                }

                levelScreen.levelBoard.userData.current_level = levelScreen.levelBoard.current_level;
                game.save_user_data();
                levelScreen.levelBoard.load_level();
            }
        }
        else if (scroller.inputState == InputState.JUST_UNTOUCHED){

        }
    }

}
