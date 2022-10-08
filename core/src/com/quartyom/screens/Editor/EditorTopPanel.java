package com.quartyom.screens.Editor;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Align;
import com.quartyom.game_elements.Button;
import com.quartyom.interfaces.EventHandler;
import com.quartyom.game_elements.InputState;
import com.quartyom.game_elements.Label;

public class EditorTopPanel {
    public boolean is_active = true;

    public final EditorScreen editorScreen;
    InputState inputState;
    TextureRegion texture;

    private float panel_x, panel_y, panel_w, panel_h;

    Button menu_button;
    Label level_label, tool_label;

    public EditorTopPanel(final EditorScreen editorScreen){
        this.editorScreen = editorScreen;

        texture = editorScreen.game.field_atlas.findRegion("top_panel");
        inputState = InputState.UNTOUCHED;

        level_label = new Label(editorScreen.game);
        level_label.target_string = "Level 1000";
        tool_label = new Label(editorScreen.game);
        tool_label.target_string = "Current: backslash wall ";

        menu_button = new Button("menu", this.editorScreen.game, new EventHandler() {
            @Override
            public void execute() {
                editorScreen.game.setScreen("menu");
            }
        });
    }

    public void resize(){
        panel_x = -editorScreen.game.HALF_WIDTH;
        panel_h = (0.5f / 4) * editorScreen.game.HEIGHT;
        panel_y = editorScreen.game.HALF_HEIGHT - panel_h;
        panel_w = editorScreen.game.WIDTH;

        float button_actual_size = panel_h  / 2;


        float first_button_center_x = panel_x + panel_w - button_actual_size;
        float first_button_center_y = panel_y + panel_h / 2;

        button_actual_size *= 0.9f; // Отступ кнопки от краёв

        float first_button_x = first_button_center_x - button_actual_size;
        float first_button_y = first_button_center_y - button_actual_size;

        float button_w = button_actual_size * 2;
        float button_h = button_actual_size * 2;

        menu_button.resize(first_button_x, first_button_y, button_w, button_h);

        //tool_label.resize(panel_x + panel_w * 0.025f, panel_y + panel_h / 2, panel_w * 0.75f, panel_h / 2, Align.left);

        level_label.resize(panel_x + panel_w * 0.025f, panel_y + panel_h / 2, panel_w * 0.75f, panel_h / 2, Align.left);
        tool_label.resize(panel_x + panel_w * 0.025f, panel_y, panel_w * 0.75f, panel_h / 2, Align.left);

    }

    public void draw(){
        if (!is_active){return;}

        editorScreen.game.batch.draw(texture, panel_x, panel_y, panel_w, panel_h);

        level_label.string = editorScreen.game.locale.get("Editor");
        level_label.draw();

        tool_label.string = editorScreen.game.locale.get("Current: ") + editorScreen.editorBoard.obstacles_to_put[editorScreen.editorBoard.cursor_on_obstacles];
        tool_label.draw();

        menu_button.draw();
    }

    public void update(){
        if (!is_active){return;}
        menu_button.update();
    }

    public float getHeight(){
        return panel_h;
    }

}
