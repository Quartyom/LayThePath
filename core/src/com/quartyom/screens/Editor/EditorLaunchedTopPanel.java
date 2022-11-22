package com.quartyom.screens.Editor;

import com.badlogic.gdx.utils.Align;
import com.quartyom.game_elements.GameTopPanel;
import com.quartyom.game_elements.Label;
import com.quartyom.screens.Level.Gameplay;

public class EditorLaunchedTopPanel extends GameTopPanel {
    public boolean is_active = false;

    public final EditorScreen editorScreen;

    Label editor_label, progress_label;
    String progress_string;

    public EditorLaunchedTopPanel(final EditorScreen editorScreen){
        super(editorScreen.game);
        this.editorScreen = editorScreen;

        editor_label = new Label(game);
        editor_label.string = game.locale.get("Editor");

        progress_label = new Label(game);
        progress_string = game.locale.get("Progress ");
        progress_label.target_string = progress_string + "100 / 100";
    }

    @Override
    public void resize(){
        super.resize();

        editor_label.resize(panel_x + panel_w * 0.025f, panel_y + panel_h / 2, panel_w * 0.75f, panel_h / 2, Align.left);
        progress_label.resize(panel_x + panel_w * 0.025f, panel_y, panel_w * 0.75f, panel_h / 2, Align.left);
    }

    @Override
    public void draw(){
        if (!is_active){return;}
        super.draw();

        editor_label.draw();

        Gameplay gameplay = editorScreen.editorLaunchedBoard.gameplay; // просто для сокращения пути
        progress_label.string = progress_string + gameplay.how_many_visited + " / " + gameplay.how_many_should_be_visited;
        progress_label.draw();
    }

    @Override
    public void update(){
        if (!is_active){return;}
        super.update();
    }

}
