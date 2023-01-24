package com.quartyom.screens.Menu;

import com.badlogic.gdx.Gdx;
import com.quartyom.LayThePath;
import com.quartyom.game_elements.Button;
import com.quartyom.game_elements.Label;
import com.quartyom.game_elements.QuScreen;
import com.quartyom.game_elements.TextField;
import com.quartyom.interfaces.QuEvent;

public class StatsTab extends QuScreen {

    boolean isActive = false;

    final LayThePath game;

    Button backButton;
    Label howToPlayLabel;
    TextField informationField;

    public StatsTab(final LayThePath game) {
        this.game = game;

        howToPlayLabel = new Label(game, game.locale.get("Stats"));

        informationField = new TextField(game, new String());

        backButton = new Button("in_main_menu", game, new QuEvent() {
            @Override
            public void execute() {
                game.setScreen("menu_info");
            }
        });
        backButton.setNinePatch(6).setLabel(game.locale.get("Back"));

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
    }

    @Override
    public void show() {
        Gdx.gl20.glClearColor(0, 0, 0, 1);

        isActive = true;
        game.userData.stats_views++;
        game.saveUserData();
        informationField.string = String.format(
                Gdx.files.internal("texts/" + game.userData.locale + "/stats.txt").readString(),
                game.userData.launches, game.userData.zen_levels_passed, game.userData.stats_views);
    }

    @Override
    public void render(float delta) {
        Gdx.gl20.glClear(Gdx.gl20.GL_COLOR_BUFFER_BIT);

        howToPlayLabel.draw();
        informationField.draw();
        backButton.draw();

        backButton.update();

        if (game.isBackButtonPressed) {
            game.isBackButtonPressed = false;
            game.setScreen("menu_info");
        }
    }

}
