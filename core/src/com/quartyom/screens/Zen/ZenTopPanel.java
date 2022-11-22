package com.quartyom.screens.Zen;

import com.badlogic.gdx.utils.Align;
import com.quartyom.game_elements.GameTopPanel;
import com.quartyom.game_elements.Label;
import com.quartyom.screens.Level.Gameplay;

public class ZenTopPanel extends GameTopPanel {
    public final ZenScreen zenScreen;

    Label level_label, progress_label;

    String level_string, progress_string;

    public ZenTopPanel(final ZenScreen zenScreen){
        super(zenScreen.game);
        this.zenScreen = zenScreen;

        level_label = new Label(game);
        level_string = game.locale.get("Level ");
        progress_string = game.locale.get("Progress ");
        level_label.target_string = level_string + "1000";
        progress_label = new Label(game);
        progress_label.target_string = progress_string + "100 / 100";
    }

    @Override
    public void resize(){
        super.resize();

        level_label.resize(panel_x + panel_w * 0.025f, panel_y + panel_h / 2, panel_w * 0.75f, panel_h / 2, Align.left);
        progress_label.resize(panel_x + panel_w * 0.025f, panel_y, panel_w * 0.75f, panel_h / 2, Align.left);
    }

    @Override
    public void draw(){
        super.draw();

        level_label.string = level_string + zenScreen.zenBoard.current_level;
        level_label.draw();

        Gameplay gameplay = zenScreen.zenBoard.gameplay;
        progress_label.string = progress_string + gameplay.how_many_visited + " / " + gameplay.how_many_should_be_visited;
        progress_label.draw();
    }

}
