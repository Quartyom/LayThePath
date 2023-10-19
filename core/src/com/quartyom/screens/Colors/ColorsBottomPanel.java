package com.quartyom.screens.Colors;

import com.badlogic.gdx.utils.TimeUtils;
import com.quartyom.game_elements.Button;
import com.quartyom.game_elements.GameBottomPanel;
import com.quartyom.interfaces.QuEvent;

public class ColorsBottomPanel extends GameBottomPanel {
    boolean isActive = true;

    public final ColorsScreen colorsScreen;
    Button resetButton, transformButton, skipButton;

    public ColorsBottomPanel(final ColorsScreen colorsScreen) {
        super(colorsScreen.game);
        this.colorsScreen = colorsScreen;

        resetButton = new Button("reset", game, new QuEvent() {
            @Override
            public void execute() {
                colorsScreen.colorsBoard.colorsGameplay.resetAllBodies();
            }
        });
        resetButton.setHint(game.locale.get("reset level")).setSound("click_1");

        transformButton = new Button("transform", game, new QuEvent() {
            @Override
            public void execute() {
                 isActive = false;
                 colorsScreen.colorsTransformBottomPanel.isActive = true;
            }
        });
        transformButton.setHint(game.locale.get("transform the field")).setSound("click_1");

        skipButton = new Button("next", game, new QuEvent() {
            @Override
            public void execute() {
                ColorsBoard colorsBoard = colorsScreen.colorsBoard;
                if (colorsBoard.currentColorsLevel < game.userData.max_colors_level_achieved ||
                        TimeUtils.millis() >= game.userData.when_to_skip_level ||
                        game.userData.premium_is_on) {

                    colorsBoard.currentColorsLevel++;
                    colorsBoard.userData.current_colors_level = colorsBoard.currentColorsLevel;
                    game.saveUserData();
                    colorsBoard.loadLevel();

                    game.setScreen("colors_level");
                } else {
                    game.setScreen("colors_level_skip");
                }
            }
        });
        skipButton.setHint(game.locale.get("skip level")).addNotification().setSound("click_1");
    }

    @Override
    public void resize() {
        super.resize();

        resetButton.resize(firstButtonX, firstButtonY, buttonW, buttonH);
        transformButton.resize(firstButtonX + panelW / 4 * 1, firstButtonY, buttonW, buttonH);
        skipButton.resize(firstButtonX + panelW / 4 * 3, firstButtonY, buttonW, buttonH);
    }

    @Override
    public void draw() {
        if (!isActive) {
            return;
        }
        super.draw();
        resetButton.draw();
        transformButton.draw();
        skipButton.draw();
    }

    public void update() {
        if (!isActive) {
            return;
        }
        resetButton.update();
        transformButton.update();
        skipButton.update();

        long minutes_left = (game.userData.when_to_skip_level - TimeUtils.millis()) / 60_000L;

        if (colorsScreen.colorsBoard.currentColorsLevel < game.userData.max_colors_level_achieved || minutes_left < 0 || game.userData.premium_is_on) {
            skipButton.setNotification(null);
        } else {
            skipButton.setNotification("<" + (minutes_left + 1L) + "m");
        }
    }
}
