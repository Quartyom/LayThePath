package com.quartyom.screens.Level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.quartyom.UserData;
import com.quartyom.game_elements.GameBoard;
import com.quartyom.game_elements.InputState;
import com.quartyom.game_elements.PressTimer;
import com.quartyom.game_elements.Scroller;
import com.quartyom.game_elements.Slider;
import com.quartyom.game_elements.Vibrator;

import java.util.Random;

public class LevelBoard extends GameBoard {
    public final LevelScreen levelScreen;

    LevelsData levelsData;
    UserData userData;

    int how_many_levels, current_level;

    public LevelBoard(LevelScreen levelScreen){
        super(levelScreen.game);

        this.levelScreen = levelScreen;

        String a = Gdx.files.internal("levels/levels_data.json").readString();
        levelsData = game.json.fromJson(LevelsData.class, a);
        how_many_levels = levelsData.how_many_levels;

        userData = game.userData;
        current_level = userData.current_level;

        load_level();

    }

    public void resize(){
        super.resize(levelScreen.levelTopPanel.getHeight());
    }

    void draw(){
        boardDrawer.draw();
    }

    @Override
    public void victory_action() {
        current_level++;
        userData.current_level = current_level;
        game.save_user_data();

        load_level();   // осторожно, уровень может не загрузиться
    }


    public void load_level(){
        //System.out.println("Loaded level " + current_level);
        if (current_level > 100 && !userData.zen_is_available){
            userData.zen_is_available = true;
            game.save_user_data();
        }

        if (current_level > 200 && !userData.editor_is_available){
            userData.editor_is_available = true;
            game.save_user_data();
        }

        if (current_level > how_many_levels){
            game.setScreen("levels_are_over");
            return;
        }

        if (current_level > userData.max_level_achieved){
            Random random = new Random();
            if (random.nextInt(3) == 0){
                userData.hints_amount++;
            }
            userData.max_level_achieved = current_level;
            game.save_user_data();
        }

        String a = Gdx.files.internal("levels/" + current_level + ".json").readString();
        LevelConfiguration levelConfiguration = game.json.fromJson(LevelConfiguration.class, a);
        gameplay.set_level_configuration(levelConfiguration);

        userData.when_to_skip_level = TimeUtils.millis() + (levelConfiguration.field_size - 2) * 60_000L;    // now + N minutes
        game.save_user_data();

        resize(); // чтобы если размер уровня другой, адаптировать экран
        gameplay.normalize_cursor(); // чтобы курсор не выпрыгнул за поле
        boardDrawer.is_hint_shown = false;
    }

}
