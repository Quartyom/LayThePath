package com.quartyom.screens.Zen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.quartyom.game_elements.InputState;
import com.quartyom.game_elements.PressTimer;
import com.quartyom.game_elements.Scroller;
import com.quartyom.screens.Level.BoardDrawer;
import com.quartyom.game_elements.GameBoard;
import com.quartyom.screens.Level.Gameplay;
import com.quartyom.screens.Level.LevelConfiguration;

import java.util.Random;


public class ZenBoard extends GameBoard {

    public final ZenScreen zenScreen;
    ZenLevelGenerator zenLevelGenerator;

    public int current_level;   // Влияет только на визуал верхней панэли


    public ZenBoard(ZenScreen zenScreen) {
        super(zenScreen.game);
        this.zenScreen = zenScreen;

        zenLevelGenerator = new ZenLevelGenerator();

        current_level = game.userData.current_zen_level;
        load_level();

    }

    public void new_level() {
        Gdx.files.local("zen_level.json").writeString(game.json.prettyPrint(zenLevelGenerator.generate_level()), false);
    }

    public void load_level() {
        if ( !Gdx.files.local("zen_level.json").exists() ) { new_level(); }

        String a = Gdx.files.local("zen_level.json").readString();
        LevelConfiguration levelConfiguration = game.json.fromJson(LevelConfiguration.class, a);
        gameplay.set_level_configuration(levelConfiguration);

        game.userData.when_to_skip_zen_level = TimeUtils.millis() + (levelConfiguration.field_size - 2) * 60_000L;    // now + N-2 minutes
        game.save_user_data();

        resize();
        gameplay.normalize_cursor(); // чтобы курсор не выпрыгнул за поле
        boardDrawer.is_hint_shown = false;
    }

    public void next_level(){
        new_level();
        load_level();
    }

    public void resize() {
        super.resize(zenScreen.zenTopPanel.getHeight());
    }

    void draw() {
        boardDrawer.draw();
    }

    @Override
    public void victory_action() {
        current_level++;
        game.userData.current_zen_level = current_level;
        game.save_user_data();

        Random random = new Random();
        if (random.nextInt(3) == 0){
            game.userData.hints_amount++;
        }

        game.userData.zen_levels_passed++;     // статистика
        game.save_user_data();

        next_level();
    }

}
