package com.quartyom.screens.Menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Align;
import com.quartyom.LayThePath;
import com.quartyom.game_elements.Button;
import com.quartyom.game_elements.Label;
import com.quartyom.game_elements.QuScreen;
import com.quartyom.game_elements.TextField;
import com.quartyom.interfaces.QuEvent;

public class ZenAfterAds extends QuScreen {

    final LayThePath game;

    Label zenLabel;
    TextField infoField;
    Button backButton, acceptButton;


    public ZenAfterAds(final LayThePath game) {
        this.game = game;

        zenLabel = new Label(game, game.locale.get("Attention"));

        infoField = new TextField(game,
                Gdx.files.internal("texts/" + game.userData.locale + "/zen_after_ads.txt").readString());

        backButton = new Button("in_main_menu", game, new QuEvent() {
            @Override
            public void execute() {
                game.setScreen("menu");
            }
        });
        backButton.setNinePatch(6).setLabel(game.locale.get("Back"));

        acceptButton = new Button("in_main_menu", game, new QuEvent() {
            @Override
            public void execute() {
                game.setScreen("zen");
            }
        });
        acceptButton.setNinePatch(6).setLabel(game.locale.get("Accept"));

    }

    @Override
    public void resize(int width, int height) {
        zenLabel.resize(game.upperButtonCornerX, game.upperButtonCornerY, game.buttonW,
                game.buttonH, Align.center);

        int font_size = (int) (game.HEIGHT * (1.0f / 32.0f));
        infoField.resize(game.upperButtonCornerX,
                game.upperButtonCornerY - game.downMargin + game.buttonH, game.buttonW, font_size);
        // gap
        acceptButton.resize(game.upperButtonCornerX,
                game.upperButtonCornerY - game.downMargin * 4, game.buttonW, game.buttonH);
        backButton.resize(game.upperButtonCornerX,
                game.upperButtonCornerY - game.downMargin * 5, game.buttonW, game.buttonH);
    }

    @Override
    public void render(float delta) {
        Gdx.gl20.glClear(Gdx.gl20.GL_COLOR_BUFFER_BIT);

        zenLabel.draw();
        infoField.draw();
        backButton.draw();
        acceptButton.draw();

        backButton.update();
        acceptButton.update();

        if (game.isBackButtonPressed) {
            game.isBackButtonPressed = false;
            game.setScreen("menu");
        }
    }

}
