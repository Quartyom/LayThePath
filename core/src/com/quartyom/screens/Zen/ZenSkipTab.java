package com.quartyom.screens.Zen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Align;
import com.quartyom.LayThePath;
import com.quartyom.game_elements.Button;
import com.quartyom.game_elements.Label;
import com.quartyom.game_elements.QuScreen;
import com.quartyom.game_elements.TextField;
import com.quartyom.interfaces.QuEvent;

public class ZenSkipTab extends QuScreen {

    final LayThePath game;
    ZenScreen zenScreen;

    Label skipLabel;
    TextField infoField;
    Button backButton;

    public ZenSkipTab(final ZenScreen zenScreen) {
        this.zenScreen = zenScreen;
        game = zenScreen.game;

        skipLabel = new Label(game, zenScreen.game.locale.get("Attention"));

        infoField = new TextField(zenScreen.game,
                Gdx.files.internal("texts/" + game.userData.locale + "/skip_level.txt").readString());

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
        skipLabel.resize(game.upperButtonCornerX, game.upperButtonCornerY, game.buttonW,
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

        skipLabel.draw();
        infoField.draw();
        backButton.draw();

        backButton.update();

        if (game.isBackButtonPressed) {
            game.isBackButtonPressed = false;
            game.setScreen("zen");
        }

    }

}
