package com.quartyom.screens.Level;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Align;
import com.quartyom.game_elements.Button;
import com.quartyom.interfaces.EventHandler;
import com.quartyom.game_elements.InputState;
import com.quartyom.game_elements.Label;
import com.quartyom.game_elements.Scroller;

public class LevelTopPanel {
    public final LevelScreen levelScreen;
    InputState inputState;
    TextureRegion texture;

    private float panel_x, panel_y, panel_w, panel_h;

    Button menu_button;
    Label level_label, progress_label;
    Scroller scroller;

    public LevelTopPanel(final LevelScreen levelScreen){
        this.levelScreen = levelScreen;
        texture = levelScreen.game.field_atlas.findRegion("top_panel");
        inputState = InputState.UNTOUCHED;

        level_label = new Label(levelScreen.game);
        level_label.target_string = "Level 1000";
        progress_label = new Label(levelScreen.game);
        progress_label.target_string = "Progress 100 / 100";

        menu_button = new Button("menu", levelScreen.game, new EventHandler() {
            @Override
            public void execute() {
                //levelScreen.game.setScreen(new MenuScreen(levelScreen.game));
                //levelScreen.dispose();
                levelScreen.game.setScreen("menu");
            }
        });

        scroller = new Scroller(levelScreen.game);

    }

    public void resize(){
        panel_x = -levelScreen.game.HALF_WIDTH;
        panel_h = (0.5f / 4) * levelScreen.game.HEIGHT;
        panel_y = levelScreen.game.HALF_HEIGHT - panel_h;
        panel_w = levelScreen.game.WIDTH;

        float button_actual_size = panel_h  / 2;


        float first_button_center_x = panel_x + panel_w - button_actual_size;
        float first_button_center_y = panel_y + panel_h / 2;

        button_actual_size *= 0.9f; // Отступ кнопки от краёв

        float first_button_x = first_button_center_x - button_actual_size;
        float first_button_y = first_button_center_y - button_actual_size;

        float button_w = button_actual_size * 2;
        float button_h = button_actual_size * 2;

        menu_button.resize(first_button_x, first_button_y, button_w, button_h);

        scroller.resize(panel_x + panel_w * 0.025f, panel_y, panel_w * 0.75f, panel_h);

        level_label.resize(panel_x + panel_w * 0.025f, panel_y + panel_h / 2, panel_w * 0.75f, panel_h / 2, Align.left);
        progress_label.resize(panel_x + panel_w * 0.025f, panel_y, panel_w * 0.75f, panel_h / 2, Align.left);

    }

    public void draw(){
        Gameplay gameplay = levelScreen.levelBoard.gameplay; // просто для сокращения пути

        levelScreen.game.batch.draw(texture, panel_x, panel_y, panel_w, panel_h);

        level_label.string = levelScreen.game.locale.get("Level ") + levelScreen.levelBoard.current_level;
        level_label.draw();

        progress_label.string = levelScreen.game.locale.get("Progress ") + gameplay.how_many_visited + " / " + gameplay.how_many_should_be_visited;
        progress_label.draw();

        menu_button.draw();
    }

    public void update(){
        menu_button.update();
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
                    levelScreen.levelBoard.current_level += levelScreen.game.userData.max_level_achieved;
                }
                else if (levelScreen.levelBoard.current_level > levelScreen.game.userData.max_level_achieved){
                    levelScreen.levelBoard.current_level -= levelScreen.game.userData.max_level_achieved;
                }

                levelScreen.levelBoard.userData.current_level = levelScreen.levelBoard.current_level;
                levelScreen.game.save_user_data();
                levelScreen.levelBoard.load_level();
            }
        }
        else if (scroller.inputState == InputState.JUST_UNTOUCHED){

        }
    }


    public float getHeight(){
        return panel_h;
    }
}
