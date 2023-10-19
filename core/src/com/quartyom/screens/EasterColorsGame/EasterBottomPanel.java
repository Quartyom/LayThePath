package com.quartyom.screens.EasterColorsGame;

import com.quartyom.game_elements.GameBottomPanel;

public class EasterBottomPanel extends GameBottomPanel {

    public final EasterScreen easterScreen;

    public EasterBottomPanel(final EasterScreen easterScreen) {
        super(easterScreen.game);
        this.easterScreen = easterScreen;
    }
}
