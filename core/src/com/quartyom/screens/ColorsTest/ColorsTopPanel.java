package com.quartyom.screens.ColorsTest;

import com.badlogic.gdx.utils.Align;
import com.quartyom.game_elements.GameTopPanel;
import com.quartyom.game_elements.InputState;
import com.quartyom.game_elements.Label;
import com.quartyom.game_elements.Scroller;
import com.quartyom.screens.Level.LevelBoard;

public class ColorsTopPanel extends GameTopPanel {
    public final ColorsScreen colorsScreen;
    InputState inputState;

    Label levelLabel, progressLabel;
    Scroller scroller;

    String levelString, progressString;

    public ColorsTopPanel(final ColorsScreen colorsScreen) {
        super(colorsScreen.game);
        this.colorsScreen = colorsScreen;

        inputState = InputState.UNTOUCHED;

        levelLabel = new Label(game);
        levelString = game.locale.get("Level ");
        progressString = game.locale.get("Progress ");
        levelLabel.targetString = levelString + "1000";
        progressLabel = new Label(game);
        progressLabel.targetString = progressString + "100 / 100";

        scroller = new Scroller(game);
    }

    @Override
    public void resize() {
        super.resize();

        scroller.resize(panelX + panelW * 0.025f, panelY, panelW * 0.75f, panelH * 0.75f);
        levelLabel.resize(panelX + panelW * 0.025f, panelY + panelH / 2, panelW * 0.75f, panelH / 2, Align.left);
        progressLabel.resize(panelX + panelW * 0.025f, panelY, panelW * 0.75f, panelH / 2, Align.left);
    }

    @Override
    public void draw() {
        super.draw();

        levelLabel.string = levelString + colorsScreen.colorsBoard.currentColorsLevel;
        levelLabel.draw();

        ColorsGameplay colorsGameplay = colorsScreen.colorsBoard.colorsGameplay; // просто для сокращения пути
        progressLabel.string = progressString + colorsGameplay.howManyVisited + " / " + colorsGameplay.howManyShouldBeVisited;
        progressLabel.draw();
    }

    @Override
    public void update() {
        super.update();

        scroller.update();

        ColorsBoard colorsBoard = colorsScreen.colorsBoard; // для сокращения кода снизу
        float sensitivity = 50; // количество пикселей, которые нужно пройти, чтобы переключить 1 уровень

        if (scroller.inputState == InputState.JUST_TOUCHED) {
            scroller.value.x = colorsBoard.currentColorsLevel * sensitivity;
        } else if (scroller.inputState == InputState.TOUCHED) {
            if ((Math.round(scroller.value.x / sensitivity)) != colorsBoard.currentColorsLevel) {
                colorsBoard.currentColorsLevel = (Math.round(scroller.value.x / sensitivity));

                boolean to_change_scroller = true;

                if (colorsBoard.currentColorsLevel < 1) {
                    colorsBoard.currentColorsLevel += game.userData.max_colors_level_achieved;
                    if (colorsBoard.currentColorsLevel > colorsBoard.howManyColorsLevels) {
                        colorsBoard.currentColorsLevel = colorsBoard.howManyColorsLevels;
                    }
                } else if (colorsBoard.currentColorsLevel > colorsBoard.howManyColorsLevels) {
                    colorsBoard.currentColorsLevel = 1;
                } else if (colorsBoard.currentColorsLevel > game.userData.max_colors_level_achieved) {
                    colorsBoard.currentColorsLevel -= game.userData.max_colors_level_achieved;
                } else {
                    to_change_scroller = false;
                }
                if (to_change_scroller) {
                    scroller.value.x = colorsBoard.currentColorsLevel * sensitivity;
                }

                colorsBoard.userData.current_colors_level = colorsBoard.currentColorsLevel;
                game.saveUserData();
                colorsBoard.loadLevel();
            }
        }
    }

}
