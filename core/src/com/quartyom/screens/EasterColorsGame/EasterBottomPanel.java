package com.quartyom.screens.EasterColorsGame;

import com.badlogic.gdx.utils.TimeUtils;
import com.quartyom.game_elements.Button;
import com.quartyom.game_elements.GameBottomPanel;
import com.quartyom.interfaces.QuEvent;
import com.quartyom.screens.ColorsTest.ColorsBoard;

public class EasterBottomPanel extends GameBottomPanel {

    public final EasterScreen easterScreen;

    public EasterBottomPanel(final EasterScreen easterScreen) {
        super(easterScreen.game);
        this.easterScreen = easterScreen;
    }
}
