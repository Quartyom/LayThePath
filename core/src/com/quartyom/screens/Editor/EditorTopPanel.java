package com.quartyom.screens.Editor;

import com.badlogic.gdx.utils.Align;
import com.quartyom.game_elements.FontType;
import com.quartyom.game_elements.GameTopPanel;
import com.quartyom.game_elements.Label;

public class EditorTopPanel extends GameTopPanel {
    public boolean is_active = true;

    public final EditorScreen editorScreen;

    Label editor_label, tool_label;
    String tool_string;

    public EditorTopPanel(final EditorScreen editorScreen){
        super(editorScreen.game);
        this.editorScreen = editorScreen;

        editor_label = new Label(game);
        editor_label.string = game.locale.get("Editor");

        tool_label = new Label(game);
        tool_label.fontType = FontType.LOCALIZED_WITH_LATIN;
        tool_string = game.locale.get("Current: ");
        tool_label.target_string = tool_string + "backslash wall ";

    }

    @Override
    public void resize(){
        super.resize();

        editor_label.resize(panel_x + panel_w * 0.025f, panel_y + panel_h / 2, panel_w * 0.75f, panel_h / 2, Align.left);
        tool_label.resize(panel_x + panel_w * 0.025f, panel_y, panel_w * 0.75f, panel_h / 2, Align.left);
    }

    @Override
    public void draw(){
        if (!is_active){return;}

        super.draw();
        editor_label.draw();
        tool_label.string = tool_string + editorScreen.editorBoard.obstacles_to_put[editorScreen.editorBoard.cursor_on_obstacles];
        tool_label.draw();
    }

    @Override
    public void update(){
        if (!is_active){return;}
        super.update();
    }

}
