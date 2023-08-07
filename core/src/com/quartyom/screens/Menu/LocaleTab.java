package com.quartyom.screens.Menu;

import com.badlogic.gdx.Gdx;
import com.quartyom.LayThePath;
import com.quartyom.game_elements.Button;
import com.quartyom.game_elements.FontType;
import com.quartyom.game_elements.QuScreen;
import com.quartyom.game_elements.Scroller;
import com.quartyom.interfaces.QuEvent;

import java.util.ArrayList;

public class LocaleTab extends QuScreen {

    final LayThePath game;

    Button backButton;
    ArrayList<Button> localeButtons;

    Scroller scroller;

    public LocaleTab(final LayThePath game) {
        this.game = game;

        localeButtons = new ArrayList<>();

        for (final String key : game.locale.folders.keySet()) {
            Button button = new Button("in_main_menu", game, new QuEvent() {
                @Override
                public void execute() {
                    game.changeLocale(key);
                    game.setScreen("menu_settings");
                }
            });
            button.setNinePatch(6).setLabel(game.locale.folders.get(key), FontType.INTERNATIONAL);
            localeButtons.add(button);
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

    public void update() {
        backButton.update();

        for (Button item : localeButtons) {
            item.update();
        }

        scroller.update();

        if (scroller.value.y < 0) {
            scroller.value.y = 0;
        } else if (scroller.value.y > game.downMargin * 0.5f * localeButtons.size()) {
            scroller.value.y = game.downMargin * 0.5f * localeButtons.size();
        }

        for (Button item : localeButtons) {
            item.offset.y = scroller.value.y;
        }

    }

    @Override
    public void resize(int width, int height) {
        int i = 0;
        for (Button item : localeButtons) {
            item.resize(game.upperButtonCornerX,
                    game.upperButtonCornerY - game.downMargin * 0.5f * (i++), game.buttonW,
                    game.buttonH * 0.5f);
        }

        backButton.resize(game.upperButtonCornerX,
                game.upperButtonCornerY - game.downMargin * 5, game.buttonW, game.buttonH);

        scroller.resizeFull();
    }

    @Override
    public void render(float delta) {
        Gdx.gl20.glClear(Gdx.gl20.GL_COLOR_BUFFER_BIT);

        for (Button item : localeButtons) {
            item.draw();
        }
        backButton.draw();

        update();

        if (game.isBackButtonPressed) {
            game.isBackButtonPressed = false;
            game.setScreen("menu_settings");
        }
    }

}
