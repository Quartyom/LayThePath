package com.quartyom.screens.Zen;

import com.badlogic.gdx.utils.Align;
import com.quartyom.game_elements.GameTopPanel;
import com.quartyom.game_elements.Label;
import com.quartyom.screens.Level.Gameplay;

public class ZenTopPanel extends GameTopPanel {

    public final ZenScreen zenScreen;

    Label levelLabel, progressLabel;

    String levelString, progressString;

    public ZenTopPanel(final ZenScreen zenScreen) {
        super(zenScreen.game, "menu_classic");
        this.zenScreen = zenScreen;

        levelLabel = new Label(game);
        levelString = game.locale.get("Level ");
        progressString = game.locale.get("Progress ");
        levelLabel.targetString = levelString + "1000";
        progressLabel = new Label(game);
        progressLabel.targetString = progressString + "100 / 100";
    }

    @Override
    public void resize() {
        super.resize();

        levelLabel.resize(panelX + panelW * 0.025f, panelY + panelH / 2, panelW * 0.75f,
                panelH / 2, Align.left);
        progressLabel.resize(panelX + panelW * 0.025f, panelY, panelW * 0.75f, panelH / 2,
                Align.left);
    }

    @Override
    public void draw() {
        super.draw();

        levelLabel.string = levelString + zenScreen.zenBoard.currentLevel;
        levelLabel.draw();

        Gameplay gameplay = zenScreen.zenBoard.gameplay;
        progressLabel.string =
                progressString + gameplay.howManyVisited + " / " + gameplay.howManyShouldBeVisited;
        progressLabel.draw();
    }

}
