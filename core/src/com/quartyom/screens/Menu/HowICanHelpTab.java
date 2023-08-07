package com.quartyom.screens.Menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Align;
import com.quartyom.LayThePath;
import com.quartyom.game_elements.Button;
import com.quartyom.game_elements.Label;
import com.quartyom.game_elements.QuScreen;
import com.quartyom.game_elements.Scroller;
import com.quartyom.game_elements.TextField;
import com.quartyom.game_elements.Timer;
import com.quartyom.interfaces.QuEvent;

public class HowICanHelpTab extends QuScreen {

    final LayThePath game;

    Button bitcoinButton, ethereumButton, backButton;
    Label aboutLabel, copiedLabel;
    TextField informationField;
    Scroller scroller;

    Timer timer;
    boolean toShowCopiedLabel;

    public HowICanHelpTab(final LayThePath game) {
        this.game = game;

        timer = new Timer(new QuEvent() {
            @Override
            public void execute() {
                toShowCopiedLabel = false;
            }
        });

        aboutLabel = new Label(game, game.locale.get("How I can help"));
        copiedLabel = new Label(game, game.locale.get("Copied to clipboard"));

        informationField = new TextField(game,
                Gdx.files.internal("texts/" + game.userData.locale + "/how_i_can_help.txt").readString());

        bitcoinButton = new Button("bitcoin", game, new QuEvent() {
            @Override
            public void execute() {
                Gdx.app.getClipboard().setContents("bc1qekxx6tgmk6hh8rqhugn20wvngkrgl5sw2rx9mu");
                toShowCopiedLabel = true;
                timer.set(2000);
            }
        });
        bitcoinButton.setSound("click_1").setHint(game.locale.get("copy address to clipboard"));

        ethereumButton = new Button("ethereum", game, new QuEvent() {
            @Override
            public void execute() {
                Gdx.app.getClipboard().setContents("0xF639A0c69E1E3FB3Eaf0092988404Ec99214E097");
                toShowCopiedLabel = true;
                timer.set(2000);
            }
        });
        ethereumButton.setSound("click_1").setHint(game.locale.get("copy address to clipboard"));

        backButton = new Button("in_main_menu", game, new QuEvent() {
            @Override
            public void execute() {
                game.setScreen("menu_about");
            }
        });
        backButton.setNinePatch(6).setLabel(game.locale.get("Back"));

        scroller = new Scroller(game);
        scroller.physicsOn = true;

    }

    public void update() {
        timer.update();
        backButton.update();
        bitcoinButton.update();
        ethereumButton.update();
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
        copiedLabel.offset.y = scroller.value.y;

        bitcoinButton.offset.y = scroller.value.y;
        ethereumButton.offset.y = scroller.value.y;
    }

    @Override
    public void resize(int width, int height) {
        aboutLabel.resize(game.upperButtonCornerX, game.upperButtonCornerY, game.buttonW,
                game.buttonH, Align.center);

        int font_size = (int) (game.HEIGHT * (1.0f / 32.0f));
        informationField.resize(game.upperButtonCornerX,
                game.upperButtonCornerY - game.downMargin + game.buttonH, game.buttonW, font_size);

        copiedLabel.resize(game.upperButtonCornerX, informationField.getLowerY() - font_size,
                game.buttonW, font_size, Align.center);

        bitcoinButton.resize(game.upperButtonCornerX, copiedLabel.getLowerY() - game.buttonH,
                game.buttonH, game.buttonH);
        float theme_button_corner_x = game.upperButtonCornerX + game.buttonW - game.buttonH;
        ethereumButton.resize(theme_button_corner_x, copiedLabel.getLowerY() - game.buttonH,
                game.buttonH, game.buttonH);

        backButton.resize(game.upperButtonCornerX,
                game.upperButtonCornerY - game.downMargin * 5, game.buttonW, game.buttonH);

        scroller.resizeFull();
    }

    @Override
    public void render(float delta) {
        Gdx.gl20.glClear(Gdx.gl20.GL_COLOR_BUFFER_BIT);

        aboutLabel.draw();
        informationField.draw();
        if (toShowCopiedLabel) {
            copiedLabel.draw();
        }
        bitcoinButton.draw();
        ethereumButton.draw();
        backButton.draw();

        update();

        if (game.isBackButtonPressed) {
            game.isBackButtonPressed = false;
            game.setScreen("menu_about");
        }
    }

}
