package com.quartyom.screens.Editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.quartyom.game_elements.Button;
import com.quartyom.game_elements.GameBottomPanel;
import com.quartyom.interfaces.QuEvent;
import com.quartyom.screens.Level.LevelConfiguration;

public class EditorLaunchedBottomPanel extends GameBottomPanel {
    public boolean is_active = false;

    public final EditorScreen editorScreen;

    Button back_button, reset_button, transform_button, save_button;

    public EditorLaunchedBottomPanel(final EditorScreen editorScreen){
        super(editorScreen.game);
        this.editorScreen = editorScreen;

        back_button = new Button("back", game, new QuEvent() {
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
        back_button.setHint(game.locale.get("go back")).setSound("click_1");

        reset_button = new Button("reset", game, new QuEvent() {
            @Override
            public void execute() {
                editorScreen.editorLaunchedBoard.gameplay.reset_body();
            }
        });
        reset_button.setHint(game.locale.get("reset level")).setSound("click_1");

        transform_button = new Button("transform", game, new QuEvent() {
            @Override
            public void execute() {
                is_active = false;
                editorScreen.editorTransformBottomPanel.is_active = true;
            }
        });
        transform_button.setHint(game.locale.get("transform the field")).setSound("click_1");

        save_button = new Button("save", game, new QuEvent() {
            @Override
            public void execute() {
                if (!game.userData.is_developer){return;}
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
        save_button.setHint(game.locale.get("only for developers")).setSound("click_1");
    }

    @Override
    public void resize(){
        super.resize();
        back_button.resize(first_button_x, first_button_y, button_w, button_h);
        reset_button.resize(first_button_x + panel_w / 4, first_button_y, button_w, button_h);
        transform_button.resize(first_button_x + panel_w / 4 * 2, first_button_y, button_w, button_h);
        save_button.resize(first_button_x + panel_w / 4 * 3, first_button_y, button_w, button_h);
    }

    @Override
    public void draw(){
        if (!is_active){return;}
        super.draw();
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
