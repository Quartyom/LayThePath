package com.quartyom.screens.Editor;

import com.badlogic.gdx.utils.Align;
import com.quartyom.game_elements.GameTopPanel;
import com.quartyom.game_elements.Label;
import com.quartyom.screens.Level.Gameplay;

public class EditorLaunchedTopPanel extends GameTopPanel {
    public boolean isActive = false;

    public final EditorScreen editorScreen;

    Label editorLabel, progressLabel;
    String progressString;

    public EditorLaunchedTopPanel(final EditorScreen editorScreen) {
        super(editorScreen.game, "menu_classic");
        this.editorScreen = editorScreen;

        editorLabel = new Label(game);
        editorLabel.string = game.locale.get("Editor");

        progressLabel = new Label(game);
        progressString = game.locale.get("Progress ");
        progressLabel.targetString = progressString + "100 / 100";
    }

    @Override
    public void resize() {
        super.resize();

        editorLabel.resize(panelX + panelW * 0.025f, panelY + panelH / 2, panelW * 0.75f, panelH / 2, Align.left);
        progressLabel.resize(panelX + panelW * 0.025f, panelY, panelW * 0.75f, panelH / 2, Align.left);
    }

    @Override
    public void draw() {
        if (!isActive) {
            return;
        }
        super.draw();

        editorLabel.draw();

        Gameplay gameplay = editorScreen.editorLaunchedBoard.gameplay; // просто для сокращения пути
        progressLabel.string = progressString + gameplay.howManyVisited + " / " + gameplay.howManyShouldBeVisited;
        progressLabel.draw();
    }

    @Override
    public void update() {
        if (!isActive) {
            return;
        }
        super.update();
    }

}
