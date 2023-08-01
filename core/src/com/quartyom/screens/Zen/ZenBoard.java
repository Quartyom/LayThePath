package com.quartyom.screens.Zen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.TimeUtils;
import com.quartyom.game_elements.GameBoard;
import com.quartyom.screens.Level.LevelConfiguration;


public class ZenBoard extends GameBoard {

    public final ZenScreen zenScreen;

    public int currentLevel;   // Влияет только на визуал верхней панэли
    boolean wasHintUsed;

    public ZenBoard(ZenScreen zenScreen) {
        super(zenScreen.game);
        this.zenScreen = zenScreen;

        currentLevel = game.userData.current_zen_level;
        loadLevel(true);
    }

    private LevelConfiguration levelConfiguration;

    public void newLevel() {
        //levelConfiguration = zenScreen.zenLevelGenerator.generateLevel();
        levelConfiguration = zenScreen.zenLevelGenerator.getNewLevel();
        Gdx.files.local("zen_level.json").writeString(game.json.prettyPrint(levelConfiguration), false);
    }

    public void loadLevel(boolean toReadFromFile) {
        if (toReadFromFile) {
            if (!Gdx.files.local("zen_level.json").exists()) {
                newLevel();
            }
            String a = Gdx.files.local("zen_level.json").readString();
            levelConfiguration = game.json.fromJson(LevelConfiguration.class, a);
        }
        // else levelConfiguration is already defined

        gameplay.setLevelConfiguration(levelConfiguration);

        game.userData.when_to_skip_zen_level =
                TimeUtils.millis() + (levelConfiguration.field_size - 2) * 60_000L - 1L;    // now + N-2 minutes
        game.saveUserData();

        resize();
        gameplay.normalizeCursor(); // чтобы курсор не выпрыгнул за поле
        boardDrawer.isHintShown = false;
        wasHintUsed = false;
    }

    public void nextLevel() {
        newLevel();
        loadLevel(false);
    }

    public void resize() {
        super.resize(zenScreen.zenTopPanel.getHeight());
    }

    void draw() {
        boardDrawer.draw();
    }

    @Override
    public void victoryAction() {
        currentLevel++;
        game.userData.current_zen_level = currentLevel;
        //game.save_user_data();  // not necessary bcs called in next_level

        if (game.random.nextInt(3) == 0) {
            game.userData.hints_amount++;
        }

        game.userData.zen_levels_passed++;     // статистика
        //game.save_user_data();  // not necessary bcs called in next_level

        nextLevel();
    }

}
