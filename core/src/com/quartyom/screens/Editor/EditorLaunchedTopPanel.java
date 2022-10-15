package com.quartyom.screens.Editor;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Align;
import com.quartyom.game_elements.Button;
import com.quartyom.interfaces.QuEvent;
import com.quartyom.game_elements.InputState;
import com.quartyom.game_elements.Label;
import com.quartyom.screens.Level.Gameplay;

public class EditorLaunchedTopPanel {
    public boolean is_active = false;

    public final EditorScreen editorScreen;
    InputState inputState;
    TextureRegion texture;

    private float panel_x, panel_y, panel_w, panel_h;

    Button menu_button;
    Label level_label, progress_label;

    public EditorLaunchedTopPanel(final EditorScreen editorScreen){
        this.editorScreen = editorScreen;

        texture = editorScreen.game.field_atlas.findRegion("top_panel");
        inputState = InputState.UNTOUCHED;

        level_label = new Label(editorScreen.game);
        level_label.target_string = "Level 1000";
        progress_label = new Label(editorScreen.game);
        progress_label.target_string = "Progress 100 / 100";

        menu_button = new Button("menu", editorScreen.game, new QuEvent() {
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

        level_label.resize(panel_x + panel_w * 0.025f, panel_y + panel_h / 2, panel_w * 0.75f, panel_h / 2, Align.left);
        progress_label.resize(panel_x + panel_w * 0.025f, panel_y, panel_w * 0.75f, panel_h / 2, Align.left);

    }

    public void draw(){
        if (!is_active){return;}

        Gameplay gameplay = editorScreen.editorLaunchedBoard.gameplay; // просто для сокращения пути

        editorScreen.game.batch.draw(texture, panel_x, panel_y, panel_w, panel_h);

        level_label.string = editorScreen.game.locale.get("Editor");
        level_label.draw();

        progress_label.string = editorScreen.game.locale.get("Progress ") + gameplay.how_many_visited + " / " + gameplay.how_many_should_be_visited;
        progress_label.draw();

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
