package com.quartyom.screens.Editor;

import com.quartyom.game_elements.Button;
import com.quartyom.game_elements.GameBottomPanel;
import com.quartyom.interfaces.QuEvent;
import com.quartyom.game_elements.Slider;
import com.quartyom.game_elements.SwitchButton;

public class EditorBottomPanel extends GameBottomPanel {
    public boolean is_active = true;

    public final EditorScreen editorScreen;

    Button launch_button, transform_button;
    SwitchButton field_size_switch, obstacle_switch;
    Slider slider;

    public EditorBottomPanel(final EditorScreen editorScreen){
        super(editorScreen.game);
        this.editorScreen = editorScreen;

        field_size_switch = new SwitchButton(game);
        field_size_switch.add("field_size_normal").add("field_size_pressed");
        field_size_switch.setSound("click_1");

        transform_button = new Button("transform", game, new QuEvent() {
            @Override
            public void execute() {
                is_active = false;
                editorScreen.editorTransformBottomPanel.is_active = true;
            }
        });
        transform_button.setHint(game.locale.get("transform the field")).setSound("click_1");

        obstacle_switch = new SwitchButton(game);
        obstacle_switch.add("obstacle_normal").add("obstacle_pressed");
        obstacle_switch.setSound("click_1");

        launch_button = new Button("launch", game, new QuEvent() {
            @Override
            public void execute() {
                editorScreen.editorBoard.is_active = false;
                editorScreen.editorLaunchedBoard.activate();

                editorScreen.editorBottomPanel.is_active = false;
                editorScreen.editorLaunchedBottomPanel.is_active = true;

                editorScreen.editorTopPanel.is_active = false;
                editorScreen.editorLaunchedTopPanel.is_active = true;

            }
        });
        launch_button.setHint(game.locale.get("launch")).setSound("click_1");

        slider = new Slider("regular", game);
    }

    @Override
    public void resize(){
        super.resize();

        field_size_switch.resize(first_button_x, first_button_y, button_w, button_h);
        transform_button.resize(first_button_x + panel_w / 4, first_button_y, button_w, button_h);
        obstacle_switch.resize(first_button_x + panel_w / 4 * 2, first_button_y, button_w, button_h);
        launch_button.resize(first_button_x + panel_w / 4 * 3, first_button_y, button_w, button_h);

        float slider_center_x = 0;

        float slider_h = panel_h / 2;
        float slider_w = panel_w * 0.8f;

        slider.resize(slider_center_x - slider_w / 2, panel_y + panel_h * 1.2f, slider_w, slider_h);
    }

    @Override
    public void draw(){
        if (!is_active){return;}
        super.draw();
        field_size_switch.draw();
        transform_button.draw();
        obstacle_switch.draw();
        launch_button.draw();

        if (editorScreen.is_slider_active) {
            slider.draw();
        }
    }

    public void update(){
        if (!is_active){return;}
        field_size_switch.update();
        obstacle_switch.update();
        transform_button.update();
        launch_button.update();

        // если попали по кнопке, остальные сбрасываются
        if (field_size_switch.recently_changed){
            obstacle_switch.state = 0;
        }
        else if (obstacle_switch.recently_changed){
            field_size_switch.state = 0;
        }


        if (field_size_switch.state == 1){
            editorScreen.is_slider_active = true;

            slider.value = (float)(editorScreen.editorBoard.gameplay.field_size - editorScreen.MIN_FILED_SIZE) / (editorScreen.MAX_FIELD_SIZE - editorScreen.MIN_FILED_SIZE);
            slider.update();

            int value = Math.round(slider.value * (editorScreen.MAX_FIELD_SIZE - editorScreen.MIN_FILED_SIZE)) + editorScreen.MIN_FILED_SIZE;
            if (value != editorScreen.editorBoard.gameplay.field_size) {
                editorScreen.editorBoard.gameplay.field_size = value;
                editorScreen.editorBoard.resize();
                editorScreen.editorBoard.gameplay.normalize_obstacles();
            }

        }
        else if (obstacle_switch.state == 1){
            editorScreen.is_slider_active = true;

            slider.value = (float)(editorScreen.editorBoard.cursor_on_obstacles)/(editorScreen.editorBoard.obstacles_to_put.length - 1);
            slider.update();

            int value = Math.round(slider.value * (editorScreen.editorBoard.obstacles_to_put.length - 1));

            if (value != editorScreen.editorBoard.cursor_on_obstacles){
                editorScreen.editorBoard.cursor_on_obstacles = value;
            }

        }

        else {
            editorScreen.is_slider_active = false;
        }

    }

}
