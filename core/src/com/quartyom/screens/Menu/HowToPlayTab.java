package com.quartyom.screens.Menu;

import com.badlogic.gdx.Gdx;
import com.quartyom.LayThePath;
import com.quartyom.game_elements.Button;
import com.quartyom.game_elements.Label;
import com.quartyom.game_elements.QuScreen;
import com.quartyom.game_elements.Scroller;
import com.quartyom.game_elements.TextField;
import com.quartyom.interfaces.QuEvent;

public class HowToPlayTab extends QuScreen {

    final LayThePath game;

    Button backButton;
    Label howToPlayLabel;
    TextField informationField;
    Scroller scroller;

    public HowToPlayTab(final LayThePath game) {
        this.game = game;

        howToPlayLabel = new Label(game, game.locale.get("How to play"));

        informationField = new TextField(game,
                Gdx.files.internal("texts/" + game.userData.locale + "/how_to_play.txt").readString());

        backButton = new Button("in_main_menu", game, new QuEvent() {
            @Override
            public void execute() {
                game.setScreen("menu_info");
            }
        });
        backButton.setNinePatch(6).setLabel(game.locale.get("Back"));

        scroller = new Scroller(game);
        scroller.physicsOn = true;

    }

    public void update() {
        backButton.update();
        scroller.update();

        if (scroller.value.y < 0) {
            scroller.value.y = 0;
        }  // нельзя листать вверх
        else if (scroller.value.y
                > informationField.getHeight()) {    // вниз нельзя листать дальше, чем высота текста
            scroller.value.y = informationField.getHeight();
        }

        howToPlayLabel.offset.y = scroller.value.y;
        informationField.offset.y = scroller.value.y;
    }

    public void resize(float upper_button_corner_x, float upper_button_corner_y, float button_w,
                       float button_h, float down_margin) {

        howToPlayLabel.resize(upper_button_corner_x, upper_button_corner_y, button_w, button_h, 1);

        int font_size = (int) (game.HEIGHT * (1.0f / 32.0f));
        informationField.resize(upper_button_corner_x, upper_button_corner_y + button_h - down_margin,
                button_w, font_size);

        backButton.resize(upper_button_corner_x, upper_button_corner_y - down_margin * 5, button_w,
                button_h);

        scroller.resizeFull();

    }

    @Override
    public void render(float delta) {
        Gdx.gl20.glClear(Gdx.gl20.GL_COLOR_BUFFER_BIT);

        howToPlayLabel.draw();
        informationField.draw();
        backButton.draw();

        update();

        if (game.isBackButtonPressed) {
            game.isBackButtonPressed = false;
            game.setScreen("menu_info");
        }
    }

    @Override
    public void resize(int width, int height) {
        howToPlayLabel.resize(game.upperButtonCornerX, game.upperButtonCornerY, game.buttonW,
                game.buttonH, 1);

        int font_size = (int) (game.HEIGHT * (1.0f / 32.0f));
        informationField.resize(game.upperButtonCornerX,
                game.upperButtonCornerY + game.buttonH - game.downMargin, game.buttonW, font_size);

        backButton.resize(game.upperButtonCornerX,
                game.upperButtonCornerY - game.downMargin * 5, game.buttonW, game.buttonH);

        scroller.resizeFull();
    }

}
