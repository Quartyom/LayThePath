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

    public LevelTopPanel(final LevelScreen levelScreen){
        super(levelScreen.game);
        this.levelScreen = levelScreen;

        inputState = InputState.UNTOUCHED;

        level_label = new Label(game);
        level_label.target_string = "Level 1000";
        progress_label = new Label(game);
        progress_label.target_string = "Progress 100 / 100";

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

        level_label.string = game.locale.get("Level ") + levelScreen.levelBoard.current_level;
        level_label.draw();

        Gameplay gameplay = levelScreen.levelBoard.gameplay; // просто для сокращения пути
        progress_label.string = game.locale.get("Progress ") + gameplay.how_many_visited + " / " + gameplay.how_many_should_be_visited;
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
            if ((int)(Math.round(scroller.value.x / sensitivity)) != levelScreen.levelBoard.current_level){
                levelScreen.levelBoard.current_level = (int)(Math.round(scroller.value.x / sensitivity));

                // Нормализуем (изменить)
                if (levelScreen.levelBoard.current_level < 1){
                    levelScreen.levelBoard.current_level += game.userData.max_level_achieved;
                }
                else if (levelScreen.levelBoard.current_level > game.userData.max_level_achieved){
                    levelScreen.levelBoard.current_level -= game.userData.max_level_achieved;
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
