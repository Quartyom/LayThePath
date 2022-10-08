package com.quartyom.screens.Editor;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.quartyom.game_elements.Button;
import com.quartyom.interfaces.EventHandler;
import com.quartyom.game_elements.Slider;
import com.quartyom.game_elements.SwitchButton;

public class EditorBottomPanel {
    public boolean is_active = true;

    public final EditorScreen editorScreen;
    TextureRegion texture;

    Button launch_button, transform_button;
    SwitchButton field_size_switch, obstacle_switch;

    Slider slider;

    private float panel_x, panel_y, panel_w, panel_h;

    public EditorBottomPanel(final EditorScreen editorScreen){
        this.editorScreen = editorScreen;

        texture = editorScreen.game.field_atlas.findRegion("bottom_panel");


        field_size_switch = new SwitchButton(editorScreen.game);
        field_size_switch.add("field_size_normal").add("field_size_pressed");
        field_size_switch.setSound("click_1");

        transform_button = new Button("transform", editorScreen.game, new EventHandler() {
            @Override
            public void execute() {
                is_active = false;
                editorScreen.editorTransformBottomPanel.is_active = true;
            }
        });
        transform_button.setSound("click_1");

        obstacle_switch = new SwitchButton(editorScreen.game);
        obstacle_switch.add("obstacle_normal").add("obstacle_pressed");
        obstacle_switch.setSound("click_1");

        launch_button = new Button("launch", editorScreen.game, new EventHandler() {
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
        launch_button.setSound("click_1");

        slider = new Slider("regular", editorScreen.game);
    }

    public void resize(){

        panel_x = -editorScreen.game.HALF_WIDTH;
        panel_y = -editorScreen.game.HALF_HEIGHT;
        panel_w = editorScreen.game.WIDTH;
        panel_h = (0.5f / 4) * editorScreen.game.HEIGHT;

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

        field_size_switch.resize(first_button_x, first_button_y, button_w, button_h);
        transform_button.resize(first_button_x + panel_w / 4, first_button_y, button_w, button_h);
        obstacle_switch.resize(first_button_x + panel_w / 4 * 2, first_button_y, button_w, button_h);
        launch_button.resize(first_button_x + panel_w / 4 * 3, first_button_y, button_w, button_h);

        float slider_center_x = 0;

        float slider_h = panel_h / 2;
        float slider_w = panel_w * 0.8f;

        slider.resize(slider_center_x - slider_w / 2, panel_y + panel_h * 1.2f, slider_w, slider_h);
    }

    public void draw(){
        if (!is_active){return;}
        editorScreen.game.batch.draw(texture, panel_x, panel_y, panel_w, panel_h);

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
