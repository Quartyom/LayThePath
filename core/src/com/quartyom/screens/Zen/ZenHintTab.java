package com.quartyom.screens.Zen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Align;
import com.quartyom.LayThePath;
import com.quartyom.game_elements.Button;
import com.quartyom.game_elements.Label;
import com.quartyom.game_elements.QuScreen;
import com.quartyom.game_elements.TextField;
import com.quartyom.interfaces.QuEvent;

public class ZenHintTab extends QuScreen {

    final LayThePath game;

    public ZenScreen zenScreen;

    Label hintLabel;
    TextField infoField;
    Button backButton;

    public ZenHintTab(final ZenScreen zenScreen) {
        this.zenScreen = zenScreen;
        game = zenScreen.game;

        hintLabel = new Label(game, zenScreen.game.locale.get("Attention"));

        infoField = new TextField(zenScreen.game,
                Gdx.files.internal("texts/" + game.userData.locale + "/hints_are_over.txt").readString());

        backButton = new Button("in_main_menu", game, new QuEvent() {
            @Override
            public void execute() {
                game.setScreen("zen");
            }
        });
        backButton.setNinePatch(6).setLabel(zenScreen.game.locale.get("Back"));

    }

    @Override
    public void resize(int width, int height) {
        hintLabel.resize(game.upperButtonCornerX, game.upperButtonCornerY, game.buttonW,
                game.buttonH, Align.center);

        int font_size = (int) (zenScreen.game.HEIGHT * (1.0f / 32.0f));
        infoField.resize(game.upperButtonCornerX,
                game.upperButtonCornerY - game.downMargin + game.buttonH, game.buttonW, font_size);

        backButton.resize(game.upperButtonCornerX,
                game.upperButtonCornerY - game.downMargin * 5, game.buttonW, game.buttonH);
    }

    @Override
    public void render(float delta) {
        Gdx.gl20.glClear(Gdx.gl20.GL_COLOR_BUFFER_BIT);

        hintLabel.draw();
        infoField.draw();
        backButton.draw();

        backButton.update();

        if (game.isBackButtonPressed) {
            game.isBackButtonPressed = false;
            game.setScreen("zen");
        }
    }

}
