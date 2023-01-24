package com.quartyom.screens.Menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Align;
import com.quartyom.LayThePath;
import com.quartyom.game_elements.Button;
import com.quartyom.game_elements.Label;
import com.quartyom.game_elements.QuScreen;
import com.quartyom.game_elements.TextField;
import com.quartyom.interfaces.QuEvent;

public class PremiumIsActivated extends QuScreen {

    final LayThePath game;

    Label zenLabel;
    TextField infoField;
    Button deactivateButton, backButton;


    public PremiumIsActivated(final LayThePath game) {
        this.game = game;

        zenLabel = new Label(game, game.locale.get("Attention"));

        infoField = new TextField(game, game.locale.get("Premium is activated already."));

        deactivateButton = new Button("in_main_menu", game, new QuEvent() {
            @Override
            public void execute() {
                game.userData.premium_is_on = false;
                game.saveUserData();
                game.setScreen("menu_settings");
            }
        });
        deactivateButton.setNinePatch(6).setLabel(game.locale.get("Deactivate"));

        backButton = new Button("in_main_menu", game, new QuEvent() {
            @Override
            public void execute() {
                game.setScreen("menu_settings");
            }
        });
        backButton.setNinePatch(6).setLabel(game.locale.get("Back"));

    }

    @Override
    public void resize(int width, int height) {
        zenLabel.resize(game.upperButtonCornerX, game.upperButtonCornerY, game.buttonW,
                game.buttonH, Align.center);

        int font_size = (int) (game.HEIGHT * (1.0f / 32.0f));
        infoField.resize(game.upperButtonCornerX,
                game.upperButtonCornerY - game.downMargin + game.buttonH, game.buttonW, font_size);
        // gap
        deactivateButton.resize(game.upperButtonCornerX,
                game.upperButtonCornerY - game.downMargin * 4, game.buttonW, game.buttonH);
        backButton.resize(game.upperButtonCornerX,
                game.upperButtonCornerY - game.downMargin * 5, game.buttonW, game.buttonH);
    }

    @Override
    public void render(float delta) {
        Gdx.gl20.glClear(Gdx.gl20.GL_COLOR_BUFFER_BIT);

        zenLabel.draw();
        infoField.draw();
        deactivateButton.draw();
        backButton.draw();

        deactivateButton.update();
        backButton.update();

        if (game.isBackButtonPressed) {
            game.isBackButtonPressed = false;
            game.setScreen("menu_settings");
        }
    }

}
