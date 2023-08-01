package com.quartyom.screens.ColorsTest;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.TimeUtils;
import com.quartyom.UserData;
import com.quartyom.game_elements.ColorsGameBoard;
import com.quartyom.screens.Level.LevelConfiguration;

public class ColorsBoard extends ColorsGameBoard {
    public final ColorsScreen colorsScreen;

    LevelsColorsData levelsColorsData;
    UserData userData;

    int howManyColorsLevels, currentColorsLevel;
    boolean wasHintUsed;

    public ColorsBoard(ColorsScreen colorsScreen) {
        super(colorsScreen.game);

        this.colorsScreen = colorsScreen;

        String a = Gdx.files.internal("colors_levels/levels_data.json").readString();
        levelsColorsData = game.json.fromJson(LevelsColorsData.class, a);
        howManyColorsLevels = levelsColorsData.how_many_levels;

        userData = game.userData;
        currentColorsLevel = userData.current_colors_level;
        if (currentColorsLevel < 1) { currentColorsLevel = 1; }     // для тех, у кого обновление, и поле по умолчанию не установлено

        loadLevel();

    }

    public void resize() {
        super.resize(colorsScreen.colorsTopPanel.getHeight());
    }

    void draw() {
        colorsBoardDrawer.draw();
    }

    @Override
    public void victoryAction() {
        currentColorsLevel++;
        userData.current_colors_level = currentColorsLevel;
        game.saveUserData();

        loadLevel();   // осторожно, уровень может не загрузиться
    }

    public void loadLevel() {
        if (currentColorsLevel > userData.max_colors_level_achieved) {
            if (game.random.nextInt(3) == 0) {
                userData.hints_amount++;
            }
            userData.max_colors_level_achieved = currentColorsLevel;
            game.saveUserData();
        }

        if (currentColorsLevel > howManyColorsLevels) {
            game.setScreen("colors_levels_are_over");
            return;
        }

        String a = Gdx.files.internal("colors_levels/" + currentColorsLevel + ".json").readString();
        LevelColorsConfiguration levelColorsConfiguration = game.json.fromJson(LevelColorsConfiguration.class, a);
        colorsGameplay.setLevelConfiguration(levelColorsConfiguration);

        userData.when_to_skip_colors_level = TimeUtils.millis() + (levelColorsConfiguration.field_size - 2) * 60_000L - 1L;    // now + N minutes
        game.saveUserData();

        resize(); // чтобы если размер уровня другой, адаптировать экран
        colorsGameplay.normalizeCursor(); // чтобы курсор не выпрыгнул за поле
        colorsBoardDrawer.isHintShown = false;
        wasHintUsed = false;

        colorsBoardDrawer.colorHolder.refresh();    // поменять цвета источников

    }

}
