package com.quartyom.screens.Editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.quartyom.game_elements.Button;
import com.quartyom.interfaces.QuEvent;
import com.quartyom.screens.Level.LevelConfiguration;

public class EditorLaunchedBottomPanel {
    public boolean is_active = false;

    public final EditorScreen editorScreen;
    TextureRegion texture;

    Button back_button, reset_button, transform_button, save_button;

    private float panel_x, panel_y, panel_w, panel_h;

    public EditorLaunchedBottomPanel(final EditorScreen editorScreen){
        this.editorScreen = editorScreen;
        texture = editorScreen.game.field_atlas.findRegion("bottom_panel");


        back_button = new Button("back", editorScreen.game, new QuEvent() {
            @Override
            public void execute() {
                is_active = false;
                editorScreen.editorLaunchedBoard.is_active = false;
                editorScreen.editorLaunchedTopPanel.is_active = false;

                editorScreen.editorBottomPanel.is_active = true;
                editorScreen.editorBoard.is_active = true;
                editorScreen.editorTopPanel.is_active = true;
            }
        });
        back_button.setHint(editorScreen.game.locale.get("go back")).setSound("click_1");

        reset_button = new Button("reset", editorScreen.game, new QuEvent() {
            @Override
            public void execute() {
                editorScreen.editorLaunchedBoard.gameplay.reset_body();
            }
        });
        reset_button.setHint(editorScreen.game.locale.get("reset level")).setSound("click_1");

        transform_button = new Button("transform", editorScreen.game, new QuEvent() {
            @Override
            public void execute() {
                is_active = false;
                editorScreen.editorTransformBottomPanel.is_active = true;
            }
        });
        transform_button.setHint(editorScreen.game.locale.get("transform the field")).setSound("click_1");

        save_button = new Button("save", editorScreen.game, new QuEvent() {
            @Override
            public void execute() {
                if (!editorScreen.game.userData.is_developer){return;}
                if (!editorScreen.editorLaunchedBoard.is_level_completed){return;}

                // чтобы нельзя было дважды нажать на кнопку
                if (editorScreen.editorLaunchedBoard.is_level_saved){return;}
                editorScreen.editorLaunchedBoard.is_level_saved = true;
                //System.out.println("pressed save button");

                editorScreen.editorLaunchedBoard.gameplay.set_hint();
                LevelConfiguration levelConfiguration = editorScreen.editorBoard.gameplay.get_level_configuration();
                Gdx.files.local("user_levels/" + editorScreen.editorLaunchedBoard.gameplay.field_size + "/" + System.currentTimeMillis() + ".json").writeString(editorScreen.game.json.prettyPrint(levelConfiguration), false);
            }
        });
        save_button.setHint(editorScreen.game.locale.get("only for developers")).setSound("click_1");
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

        back_button.resize(first_button_x, first_button_y, button_w, button_h);
        reset_button.resize(first_button_x + panel_w / 4, first_button_y, button_w, button_h);
        transform_button.resize(first_button_x + panel_w / 4 * 2, first_button_y, button_w, button_h);
        save_button.resize(first_button_x + panel_w / 4 * 3, first_button_y, button_w, button_h);

    }

    public void draw(){
        if (!is_active){return;}
        editorScreen.game.batch.draw(texture, panel_x, panel_y, panel_w, panel_h);

        back_button.draw();
        reset_button.draw();
        transform_button.draw();
        save_button.draw();

    }

    public void update(){
        if (!is_active){return;}

        back_button.update();
        reset_button.update();
        transform_button.update();
        save_button.update();

    }

}
