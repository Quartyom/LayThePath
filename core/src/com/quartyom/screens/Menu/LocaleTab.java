package com.quartyom.screens.Menu;

import com.badlogic.gdx.Gdx;
import com.quartyom.LayThePath;
import com.quartyom.game_elements.Button;
import com.quartyom.game_elements.InputState;
import com.quartyom.game_elements.QuScreen;
import com.quartyom.game_elements.Scroller;
import com.quartyom.interfaces.QuEvent;

import java.util.ArrayList;

public class LocaleTab extends QuScreen {
    final LayThePath game;

    Button back_button;
    ArrayList<Button> locale_buttons;

    Scroller scroller;

    public LocaleTab(final LayThePath game){
        this.game = game;

        locale_buttons = new ArrayList<>();

        for (final String key: game.locale.folders.keySet()){
            Button button = new Button("in_main_menu", game, new QuEvent() {
                @Override
                public void execute() {
                    game.change_locale(key);
                    game.setScreen("menu_settings");
                }
            });
            button.setNinePatch(6).setLabel(game.locale.folders.get(key));
            locale_buttons.add(button);
        }

        back_button = new Button("in_main_menu", game, new QuEvent() {
            @Override
            public void execute() {
                game.setScreen("menu_settings");
            }
        });
        back_button.setNinePatch(6).setLabel(game.locale.get("Back"));

        scroller = new Scroller(game);
        scroller.physics_on = true;

    }

    public void update(){
        back_button.update();

        for (Button item: locale_buttons){ item.update();}

        scroller.update();

        if (scroller.value.y < 0){ scroller.value.y = 0; }
        else if (scroller.value.y > game.down_margin * locale_buttons.size()){
            scroller.value.y = game.down_margin * locale_buttons.size();
        }

        for (Button item: locale_buttons){ item.offset.y = scroller.value.y; }

    }

    @Override
    public void resize(int width, int height) {
        int i = 0;
        for (Button item: locale_buttons){
            item.resize(game.upper_button_corner_x, game.upper_button_corner_y - game.down_margin * 0.5f * (i++), game.button_w, game.button_h * 0.5f);
        }

        back_button.resize(game.upper_button_corner_x, game.upper_button_corner_y - game.down_margin * 5, game.button_w, game.button_h);

        scroller.resize_full();
    }

    @Override
    public void render(float delta) {
        Gdx.gl20.glClear(Gdx.gl20.GL_COLOR_BUFFER_BIT);

        for (Button item : locale_buttons){
            item.draw();
        }
        back_button.draw();

        update();

        if (game.is_back_button_pressed){
            game.is_back_button_pressed = false;
            game.setScreen("menu_settings");
        }
    }

}
