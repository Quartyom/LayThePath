package com.quartyom.screens.Menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Align;
import com.quartyom.LayThePath;
import com.quartyom.game_elements.Button;
import com.quartyom.game_elements.Label;
import com.quartyom.game_elements.QuScreen;
import com.quartyom.game_elements.Scroller;
import com.quartyom.game_elements.TextField;
import com.quartyom.interfaces.QuEvent;

public class AboutTab extends QuScreen {

    final LayThePath game;

    Button backButton, howCanIHelpButton;
    Label aboutLabel;
    TextField informationField;
    Scroller scroller;

    public AboutTab(final LayThePath game) {
        this.game = game;

        aboutLabel = new Label(game, game.locale.get("About"));

        informationField = new TextField(game,
                Gdx.files.internal("texts/" + game.userData.locale + "/about.txt").readString());

        backButton = new Button("in_main_menu", game, new QuEvent() {
            @Override
            public void execute() {
                game.setScreen("menu_info");
            }
        });
        backButton.setNinePatch(6).setLabel(game.locale.get("Back"));

        howCanIHelpButton = new Button("in_main_menu", game, new QuEvent() {
            @Override
            public void execute() {
                game.setScreen("menu_how_can_i_help");
            }
        });
        howCanIHelpButton.setNinePatch(6).setLabel(game.locale.get("How can I help?"));

        scroller = new Scroller(game);
        scroller.physicsOn = true;

    }

    public void update() {
        backButton.update();
        howCanIHelpButton.update();
        scroller.update();

        if (scroller.value.y < 0) {
            scroller.value.y = 0;
        }  // нельзя листать вверх
        else if (scroller.value.y
                > informationField.getHeight()) {    // вниз нельзя листать дальше, чем высота текста
            scroller.value.y = informationField.getHeight();
        }

        aboutLabel.offset.y = scroller.value.y;
        informationField.offset.y = scroller.value.y;
        howCanIHelpButton.offset.y = scroller.value.y;

    }

    @Override
    public void resize(int width, int height) {
        aboutLabel.resize(game.upperButtonCornerX, game.upperButtonCornerY, game.buttonW,
                game.buttonH, Align.center);

        int font_size = (int) (game.HEIGHT * (1.0f / 32.0f));
        informationField.resize(game.upperButtonCornerX,
                game.upperButtonCornerY - game.downMargin + game.buttonH, game.buttonW, font_size);

        howCanIHelpButton.resize(game.upperButtonCornerX,
                game.upperButtonCornerY - game.downMargin * 2 - informationField.getHeight(),
                game.buttonW, game.buttonH);

        backButton.resize(game.upperButtonCornerX,
                game.upperButtonCornerY - game.downMargin * 5, game.buttonW, game.buttonH);

        scroller.resizeFull();
    }

    @Override
    public void render(float delta) {
        Gdx.gl20.glClear(Gdx.gl20.GL_COLOR_BUFFER_BIT);

        aboutLabel.draw();
        informationField.draw();
        howCanIHelpButton.draw();
        backButton.draw();

        update();

        if (game.isBackButtonPressed) {
            game.isBackButtonPressed = false;
            game.setScreen("menu_info");
        }
    }

}
