package com.quartyom.screens.Menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Align;
import com.quartyom.LayThePath;
import com.quartyom.game_elements.Button;
import com.quartyom.game_elements.Label;
import com.quartyom.game_elements.QuScreen;
import com.quartyom.interfaces.QuEvent;

public class ClassicTab extends QuScreen {

    final LayThePath game;

    Label classicLabel;
    Button playButton, zenButton, editorButton, backButton;

    public ClassicTab(final LayThePath game) {
        this.game = game;

        classicLabel = new Label(game, game.locale.get("Classic"));

        playButton = new Button("in_main_menu", game, new QuEvent() {
            @Override
            public void execute() {
                game.setScreen("level");
            }
        });
        playButton.setNinePatch(6).setLabel(game.locale.get("Play"));

        zenButton = new Button("in_main_menu", game, new QuEvent() {
            @Override
            public void execute() {
                if (game.userData.zen_is_available) {
                    /*if (game.userData.editor_is_available){
                        game.setScreen("zen");
                    }
                    else {
                        game.setScreen("menu_zen_after_ads");
                    }*/
                    game.setScreen("zen");
                } else {
                    game.setScreen("menu_zen_is_unavailable");
                }
            }
        });
        zenButton.setNinePatch(6).setLabel(game.locale.get("Zen"));

        editorButton = new Button("in_main_menu", game, new QuEvent() {
            @Override
            public void execute() {
                if (game.userData.editor_is_available) {
                    game.setScreen("editor");
                } else {
                    game.setScreen("menu_editor_is_unavailable");
                }
            }
        });
        editorButton.setNinePatch(6).setLabel(game.locale.get("Editor"));

        backButton = new Button("in_main_menu", game, new QuEvent() {
            @Override
            public void execute() {
                game.setScreen("menu");
            }
        });
        backButton.setNinePatch(6).setLabel(game.locale.get("Back"));

    }

    @Override
    public void resize(int width, int height) {
        classicLabel.resize(game.upperButtonCornerX, game.upperButtonCornerY, game.buttonW,
                game.buttonH, Align.center);
        playButton.resize(game.upperButtonCornerX, game.upperButtonCornerY - game.downMargin,
                game.buttonW, game.buttonH);
        zenButton.resize(game.upperButtonCornerX, game.upperButtonCornerY - game.downMargin * 2,
                game.buttonW, game.buttonH);
        editorButton.resize(game.upperButtonCornerX,
                game.upperButtonCornerY - game.downMargin * 3, game.buttonW, game.buttonH);
        // gap
        backButton.resize(game.upperButtonCornerX,
                game.upperButtonCornerY - game.downMargin * 5, game.buttonW, game.buttonH);
    }

    @Override
    public void render(float delta) {
        Gdx.gl20.glClear(Gdx.gl20.GL_COLOR_BUFFER_BIT);

        classicLabel.draw();
        playButton.draw();
        zenButton.draw();
        editorButton.draw();
        backButton.draw();

        playButton.update();
        zenButton.update();
        editorButton.update();
        backButton.update();

        if (game.isBackButtonPressed) {
            game.isBackButtonPressed = false;
            game.setScreen("menu");
        }
    }

}
