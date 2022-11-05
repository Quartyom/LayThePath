package com.quartyom.screens.Menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.Align;
import com.quartyom.LayThePath;
import com.quartyom.game_elements.Button;
import com.quartyom.game_elements.QuScreen;
import com.quartyom.interfaces.QuEvent;
import com.quartyom.game_elements.Label;

public class MenuTab extends QuScreen {
    final LayThePath game;

    Button easter_button, play_button, zen_button, editor_button, info_button, settings_button;
    Label menu_label;

    private int times_easter_button_clicked;
    private boolean back_pressed_before = false;

    public MenuTab(final LayThePath game){
        this.game = game;

        game.add("menu_about", new AboutTab(game));
        game.add("menu_editor_is_unavailable", new EditorIsUnavailable(game));
        game.add("menu_how_can_i_help", new HowCanIHelpTab(game));
        game.add("menu_how_to_play", new HowToPlayTab(game));
        game.add("menu_info", new InfoTab(game));
        game.add("menu_settings", new SettingsTab(game));
        game.add("menu_premium", new PremiumTab(game));
        game.add("menu_premium_is_activated", new PremiumIsActivated(game));
        game.add("menu_locale", new LocaleTab(game));
        game.add("menu_controls", new ControlsTab(game));
        game.add("menu_stats", new StatsTab(game));
        //game.add("menu_zen_after_ads", new ZenAfterAds(game));
        game.add("menu_zen_is_unavailable", new ZenIsUnavailable(game));

        menu_label = new Label(game, game.locale.get("Menu"));

        easter_button = new Button(game, new QuEvent() {
            @Override
            public void execute() {
                times_easter_button_clicked++;

                if (times_easter_button_clicked >= 30){
                    menu_label.set_string(game.locale.get("Ughhhh"));
                }
                else if (times_easter_button_clicked >= 22){
                    menu_label.set_string(game.locale.get("stop?"));
                }
                else if (times_easter_button_clicked >= 20){
                    menu_label.set_string(game.locale.get("you please"));
                }
                else if (times_easter_button_clicked >= 18){
                    menu_label.set_string(game.locale.get("Would"));
                }
                else if (times_easter_button_clicked >= 10){
                    menu_label.set_string(game.locale.get("It's annoying"));
                }
                else if (times_easter_button_clicked >= 3){
                    menu_label.set_string(game.locale.get("Auch!"));
                }
            }
        });

        play_button = new Button("in_main_menu", game, new QuEvent() {
            @Override
            public void execute() {
                game.setScreen("level");
                //game.setScreen("hex");
            }
        });
        play_button.setNinePatch(6).setLabel(game.locale.get("Play"));

        zen_button = new Button("in_main_menu", game, new QuEvent() {
            @Override
            public void execute() {
                if (game.userData.zen_is_available){
                    /*if (game.userData.editor_is_available){
                        game.setScreen("zen");
                    }
                    else {
                        game.setScreen("menu_zen_after_ads");
                    }*/
                    game.setScreen("zen");
                }
                else {
                    game.setScreen("menu_zen_is_unavailable");
                }
            }
        });
        zen_button.setNinePatch(6).setLabel(game.locale.get("Zen"));

        editor_button = new Button("in_main_menu", game, new QuEvent() {
            @Override
            public void execute() {
                if (game.userData.editor_is_available){
                    game.setScreen("editor");
                }
                else {
                    game.setScreen("menu_editor_is_unavailable");
                }
            }
        });
        editor_button.setNinePatch(6).setLabel(game.locale.get("Editor"));

        info_button = new Button("in_main_menu", game, new QuEvent() {
            @Override
            public void execute() {
                game.setScreen("menu_info");
            }
        });
        info_button.setNinePatch(6).setLabel(game.locale.get("Info"));

        settings_button = new Button("in_main_menu", game, new QuEvent() {
            @Override
            public void execute() {
                game.setScreen("menu_settings");
            }
        });
        settings_button.setNinePatch(6).setLabel(game.locale.get("Settings"));

    }

    @Override
    public void resize(int width, int height) {
        menu_label.resize(game.upper_button_corner_x, game.upper_button_corner_y, game.button_w, game.button_h, Align.center);
        easter_button.resize(game.upper_button_corner_x, game.upper_button_corner_y, game.button_w, game.button_h);
        play_button.resize(game.upper_button_corner_x, game.upper_button_corner_y - game.down_margin, game.button_w, game.button_h);
        zen_button.resize(game.upper_button_corner_x, game.upper_button_corner_y - game.down_margin * 2, game.button_w, game.button_h);
        editor_button.resize(game.upper_button_corner_x, game.upper_button_corner_y - game.down_margin * 3, game.button_w, game.button_h);
        info_button.resize(game.upper_button_corner_x, game.upper_button_corner_y - game.down_margin * 4, game.button_w, game.button_h);
        settings_button.resize(game.upper_button_corner_x, game.upper_button_corner_y - game.down_margin * 5, game.button_w, game.button_h);
    }

    @Override
    public void show() {
        Gdx.gl20.glClearColor(0, 0, 0, 1);
        menu_label.set_string(game.locale.get("Menu"));
        times_easter_button_clicked = 0;
        back_pressed_before = false;
    }

    @Override
    public void render(float delta) {
        Gdx.gl20.glClear(Gdx.gl20.GL_COLOR_BUFFER_BIT);

        menu_label.draw();
        play_button.draw();
        zen_button.draw();
        editor_button.draw();
        info_button.draw();
        settings_button.draw();

        easter_button.update();
        play_button.update();
        zen_button.update();
        editor_button.update();
        info_button.update();
        settings_button.update();

        if (game.is_back_button_pressed){
            if (back_pressed_before){
                Gdx.app.exit();
            }
            else {
                back_pressed_before = true;
                game.is_back_button_pressed = false;
            }
        }
    }

}
