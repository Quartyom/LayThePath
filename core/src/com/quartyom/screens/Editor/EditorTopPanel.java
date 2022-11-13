package com.quartyom.screens.Editor;

import com.badlogic.gdx.utils.Align;
import com.quartyom.game_elements.Button;
import com.quartyom.game_elements.GameTopPanel;
import com.quartyom.interfaces.QuEvent;
import com.quartyom.game_elements.Label;

public class EditorTopPanel extends GameTopPanel {
    public boolean is_active = true;

    public final EditorScreen editorScreen;

    Label level_label, tool_label;

    public EditorTopPanel(final EditorScreen editorScreen){
        super(editorScreen.game);
        this.editorScreen = editorScreen;

        level_label = new Label(game);
        level_label.target_string = "Level 1000";
        tool_label = new Label(game);
        tool_label.target_string = "Current: backslash wall ";
    }

    @Override
    public void resize(){
        super.resize();

        level_label.resize(panel_x + panel_w * 0.025f, panel_y + panel_h / 2, panel_w * 0.75f, panel_h / 2, Align.left);
        tool_label.resize(panel_x + panel_w * 0.025f, panel_y, panel_w * 0.75f, panel_h / 2, Align.left);
    }

    @Override
    public void draw(){
        if (!is_active){return;}

        super.draw();
        level_label.string = editorScreen.game.locale.get("Editor");
        level_label.draw();
        tool_label.string = editorScreen.game.locale.get("Current: ") + editorScreen.editorBoard.obstacles_to_put[editorScreen.editorBoard.cursor_on_obstacles];
        tool_label.draw();
    }

    @Override
    public void update(){
        if (!is_active){return;}
        super.update();
    }

}
