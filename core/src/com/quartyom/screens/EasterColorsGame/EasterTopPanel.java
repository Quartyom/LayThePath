package com.quartyom.screens.EasterColorsGame;

import com.badlogic.gdx.utils.Align;
import com.quartyom.game_elements.ColorsGameplay;
import com.quartyom.game_elements.FontType;
import com.quartyom.game_elements.GameTopPanel;
import com.quartyom.game_elements.InputState;
import com.quartyom.game_elements.Label;

public class EasterTopPanel extends GameTopPanel {
    public final EasterScreen easterScreen;
    Label levelLabel;

    public EasterTopPanel(final EasterScreen easterScreen) {
        super(easterScreen.game);
        this.easterScreen = easterScreen;

        levelLabel = new Label(game);
        levelLabel.fontType = FontType.LOCALIZED_WITH_LATIN;
    }

    @Override
    public void resize() {
        super.resize();
        levelLabel.resize(panelX + panelW * 0.025f, panelY + panelH / 2, panelW * 0.75f, panelH / 2, Align.left);
    }

    @Override
    public void draw() {
        super.draw();

        levelLabel.string = "XD";
        levelLabel.draw();
    }


}
