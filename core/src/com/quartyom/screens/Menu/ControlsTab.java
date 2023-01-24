package com.quartyom.screens.Menu;

import com.badlogic.gdx.Gdx;
import com.quartyom.LayThePath;
import com.quartyom.game_elements.Button;
import com.quartyom.game_elements.Label;
import com.quartyom.game_elements.QuScreen;
import com.quartyom.game_elements.Scroller;
import com.quartyom.game_elements.TextField;
import com.quartyom.interfaces.QuEvent;

public class ControlsTab extends QuScreen {

    final LayThePath game;

    Button activateButton, backButton;
    Label controlsLabel;
    TextField informationField;
    Scroller scroller;

    public ControlsTab(final LayThePath game) {
        this.game = game;

        controlsLabel = new Label(game, game.locale.get("Controls"));

        informationField = new TextField(game,
                Gdx.files.internal("texts/" + game.userData.locale + "/controls.txt").readString());

        activateButton = new Button("in_main_menu", game, new QuEvent() {
            @Override
            public void execute() {
                if (game.userData.abstract_input_is_on) {
                    game.userData.abstract_input_is_on = false;
                    activateButton.setLabel(game.locale.get("Activate"));
                } else {
                    game.userData.abstract_input_is_on = true;
                    activateButton.setLabel(game.locale.get("Deactivate"));
                }
                game.saveUserData();
            }
        });
        if (game.userData.abstract_input_is_on) {
            activateButton.setNinePatch(6).setLabel(game.locale.get("Deactivate"));
        } else {
            activateButton.setNinePatch(6).setLabel(game.locale.get("Activate"));
        }

        backButton = new Button("in_main_menu", game, new QuEvent() {
            @Override
            public void execute() {
                game.setScreen("menu_settings");
            }
        });
        backButton.setNinePatch(6).setLabel(game.locale.get("Back"));

        scroller = new Scroller(game);
        scroller.physicsOn = true;

    }

    @Override
    public void resize(int width, int height) {
        controlsLabel.resize(game.upperButtonCornerX, game.upperButtonCornerY, game.buttonW,
                game.buttonH, 1);

        int font_size = (int) (game.HEIGHT * (1.0f / 32.0f));
        informationField.resize(game.upperButtonCornerX,
                game.upperButtonCornerY - game.downMargin + game.buttonH, game.buttonW, font_size);

        activateButton.resize(game.upperButtonCornerX,
                game.upperButtonCornerY - game.downMargin - informationField.getHeight(),
                game.buttonW, game.buttonH);

        backButton.resize(game.upperButtonCornerX,
                game.upperButtonCornerY - game.downMargin * 5, game.buttonW, game.buttonH);

        scroller.resizeFull();
    }

    public void update() {
        backButton.update();
        activateButton.update();
        scroller.update();

        if (scroller.value.y < 0) {
            scroller.value.y = 0;
        }  // нельзя листать вверх
        else if (scroller.value.y
                > informationField.getHeight()) {    // вниз нельзя листать дальше, чем высота текста
            scroller.value.y = informationField.getHeight();
        }

        controlsLabel.offset.y = scroller.value.y;
        informationField.offset.y = scroller.value.y;
        activateButton.offset.y = scroller.value.y;
    }

    @Override
    public void render(float delta) {
        Gdx.gl20.glClear(Gdx.gl20.GL_COLOR_BUFFER_BIT);

        controlsLabel.draw();
        informationField.draw();
        activateButton.draw();
        backButton.draw();

        update();

        if (game.isBackButtonPressed) {
            game.isBackButtonPressed = false;
            game.setScreen("menu_settings");
        }
    }

}
