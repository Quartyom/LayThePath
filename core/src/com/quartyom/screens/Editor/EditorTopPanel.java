package com.quartyom.screens.Editor;

import com.badlogic.gdx.utils.Align;
import com.quartyom.game_elements.FontType;
import com.quartyom.game_elements.GameTopPanel;
import com.quartyom.game_elements.Label;

public class EditorTopPanel extends GameTopPanel {
    public boolean isActive = true;

    public final EditorScreen editorScreen;

    Label editorLabel, toolLabel;
    String toolString;

    public EditorTopPanel(final EditorScreen editorScreen) {
        super(editorScreen.game, "menu_classic");
        this.editorScreen = editorScreen;

        editorLabel = new Label(game);
        editorLabel.string = game.locale.get("Editor");

        toolLabel = new Label(game);
        toolLabel.fontType = FontType.LOCALIZED_WITH_LATIN;
        toolString = game.locale.get("Current: ");
        toolLabel.targetString = toolString + "backslash wall ";

    }

    @Override
    public void resize() {
        super.resize();

        editorLabel.resize(panelX + panelW * 0.025f, panelY + panelH / 2, panelW * 0.75f, panelH / 2, Align.left);
        toolLabel.resize(panelX + panelW * 0.025f, panelY, panelW * 0.75f, panelH / 2, Align.left);
    }

    @Override
    public void draw() {
        if (!isActive) {
            return;
        }

        super.draw();
        editorLabel.draw();
        toolLabel.string = toolString + editorScreen.editorBoard.obstaclesToPut[editorScreen.editorBoard.cursorOnObstacles];
        toolLabel.draw();
    }

    @Override
    public void update() {
        if (!isActive) {
            return;
        }
        super.update();
    }

}
