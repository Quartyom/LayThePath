package com.quartyom.screens.Zen;

import com.badlogic.gdx.utils.TimeUtils;
import com.quartyom.game_elements.Button;
import com.quartyom.game_elements.GameBottomPanel;
import com.quartyom.interfaces.QuEvent;


public class ZenBottomPanel extends GameBottomPanel {

    public boolean isActive = true;

    public final ZenScreen zenScreen;

    Button resetButton, transformButton, hintButton, skipButton;

    public ZenBottomPanel(final ZenScreen zenScreen) {
        super(zenScreen.game);
        this.zenScreen = zenScreen;

        resetButton = new Button("reset", game, new QuEvent() {
            @Override
            public void execute() {
                zenScreen.zenBoard.gameplay.resetBody();
            }
        });
        resetButton.setHint(game.locale.get("reset level")).setSound("click_1");

        transformButton = new Button("transform", game, new QuEvent() {
            @Override
            public void execute() {
                isActive = false;
                zenScreen.zenTransformBottomPanel.isActive = true;
            }
        });
        transformButton.setHint(game.locale.get("transform the field")).setSound("click_1");

        hintButton = new Button("hint", game, new QuEvent() {
            @Override
            public void execute() {
                ZenBoard zenBoard = zenScreen.zenBoard;
                if (zenBoard.boardDrawer.isHintShown) {              // уже показана, выключаем
                    zenBoard.boardDrawer.isHintShown = false;
                    zenBoard.wasHintUsed = true;
                } else if (zenBoard.wasHintUsed) {                     // не показана, но была использована
                    zenBoard.boardDrawer.isHintShown = true;
                } else if (game.userData.hints_amount
                        > 0) {               // значит, если они есть, то уменьшаем на 1
                    game.userData.hints_amount--;
                    zenBoard.boardDrawer.isHintShown = true;
                } else {
                    game.setScreen("zen_hint");
                }
            }
        });
        hintButton.setHint(game.locale.get("show hint")).addNotification().setSound("click_1");

        skipButton = new Button("next", game, new QuEvent() {
            @Override
            public void execute() {
                if (TimeUtils.millis() >= game.userData.when_to_skip_zen_level
                        || game.userData.premium_is_on) {

                    zenScreen.zenBoard.currentLevel++;
                    game.userData.current_zen_level = zenScreen.zenBoard.currentLevel;
                    game.saveUserData();

                    zenScreen.zenBoard.nextLevel();
                    game.setScreen("zen");
                } else {
                    game.setScreen("zen_skip");
                }
            }
        });
        skipButton.setHint(game.locale.get("skip level")).addNotification().setSound("click_1");

    }

    @Override
    public void resize() {
        super.resize();
        resetButton.resize(firstButtonX, firstButtonY, buttonW, buttonH);
        transformButton.resize(firstButtonX + panelW / 4, firstButtonY, buttonW, buttonH);
        hintButton.resize(firstButtonX + panelW / 4 * 2, firstButtonY, buttonW, buttonH);
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
        hintButton.draw();
        skipButton.draw();
    }

    public void update() {
        if (!isActive) {
            return;
        }

        resetButton.update();
        transformButton.update();
        hintButton.update();
        skipButton.update();

        if (game.userData.hints_amount < 1_000) {
            hintButton.setNotification(String.valueOf(game.userData.hints_amount));
        } else {
            hintButton.setNotification("1k+");
        }

        long minutesLeft = (game.userData.when_to_skip_zen_level - TimeUtils.millis()) / 60_000L;

        if (game.userData.premium_is_on || minutesLeft < 0) {
            skipButton.setNotification(null);
        } else {
            skipButton.setNotification("<" + (minutesLeft + 1L) + "m");
        }
    }

}
