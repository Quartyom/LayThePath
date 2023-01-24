package com.quartyom.screens.Editor;

import com.quartyom.game_elements.Button;
import com.quartyom.game_elements.GameBottomPanel;
import com.quartyom.interfaces.QuEvent;

public class EditorTransformBottomPanel extends GameBottomPanel {
    public boolean isActive = false;

    public final EditorScreen editorScreen;

    Button backButton, turnClockwiseButton, turnCounterclockwiseButton, mirrorButton;

    public EditorTransformBottomPanel(final EditorScreen editorScreen) {
        super(editorScreen.game);
        this.editorScreen = editorScreen;

        backButton = new Button("back", game, new QuEvent() {
            @Override
            public void execute() {
                isActive = false;
                if (editorScreen.editorBoard.isActive) {
                    editorScreen.editorBottomPanel.isActive = true;
                } else if (editorScreen.editorLaunchedBoard.isActive) {
                    editorScreen.editorLaunchedBottomPanel.isActive = true;
                }
            }
        });
        backButton.setHint(game.locale.get("back")).setSound("click_1");

        turnCounterclockwiseButton = new Button("turn_counterclockwise", game, new QuEvent() {
            @Override
            public void execute() {
                if (editorScreen.editorBoard.isActive) {
                    editorScreen.editorBoard.gameplay.counterclockwiseTurn();
                } else if (editorScreen.editorLaunchedBoard.isActive) {
                    editorScreen.editorLaunchedBoard.gameplay.counterclockwiseTurn();
                }
            }
        });
        turnCounterclockwiseButton.setHint(game.locale.get("counterclockwise turn")).setSound("click_1");

        turnClockwiseButton = new Button("turn_clockwise", game, new QuEvent() {
            @Override
            public void execute() {
                if (editorScreen.editorBoard.isActive) {
                    editorScreen.editorBoard.gameplay.clockwiseTurn();
                } else if (editorScreen.editorLaunchedBoard.isActive) {
                    editorScreen.editorLaunchedBoard.gameplay.clockwiseTurn();
                }
            }
        });
        turnClockwiseButton.setHint(game.locale.get("clockwise turn")).setSound("click_1");

        mirrorButton = new Button("mirror", game, new QuEvent() {
            @Override
            public void execute() {
                if (editorScreen.editorBoard.isActive) {
                    editorScreen.editorBoard.gameplay.mirrorTurn();
                } else if (editorScreen.editorLaunchedBoard.isActive) {
                    editorScreen.editorLaunchedBoard.gameplay.mirrorTurn();
                }
            }
        });
        mirrorButton.setHint(game.locale.get("mirror")).setSound("click_1");

    }

    @Override
    public void resize() {
        super.resize();
        backButton.resize(firstButtonX, firstButtonY, buttonW, buttonH);
        turnCounterclockwiseButton.resize(firstButtonX + panelW / 4, firstButtonY, buttonW, buttonH);
        turnClockwiseButton.resize(firstButtonX + panelW / 4 * 2, firstButtonY, buttonW, buttonH);
        mirrorButton.resize(firstButtonX + panelW / 4 * 3, firstButtonY, buttonW, buttonH);
    }

    @Override
    public void draw() {
        if (!isActive) {
            return;
        }
        super.draw();
        backButton.draw();
        turnCounterclockwiseButton.draw();
        turnClockwiseButton.draw();
        mirrorButton.draw();
    }

    public void update() {
        if (!isActive) {
            return;
        }
        backButton.update();
        turnCounterclockwiseButton.update();
        turnClockwiseButton.update();
        mirrorButton.update();
    }
}
