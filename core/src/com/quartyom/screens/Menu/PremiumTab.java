package com.quartyom.screens.Menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.Align;
import com.quartyom.MakeTheWay;
import com.quartyom.game_elements.Button;
import com.quartyom.game_elements.Label;
import com.quartyom.interfaces.EventHandler;

import java.util.Random;

public class PremiumTab implements Screen {
    final MakeTheWay game;

    Label info_label, code_label;
    Button button_0, button_1, button_2, button_3, button_4, button_5, button_6, button_7, button_8, button_9, button_clean, button_accept, back_button;

    //private String private_code;
    private String public_code;
    private String current_input;

    public PremiumTab(final MakeTheWay game){
        this.game = game;

        info_label = new Label(game, game.locale.get("Get premium"));
        code_label = new Label(game);
        code_label.target_string = "000000: 00000000";

        button_0 = new Button("in_main_menu", game, new EventHandler() {
            @Override
            public void execute() {
                current_input += 0;
            }
        });
        button_1 = new Button("in_main_menu", game, new EventHandler() {
            @Override
            public void execute() {
                current_input += 1;
            }
        });
        button_2 = new Button("in_main_menu", game, new EventHandler() {
            @Override
            public void execute() {
                current_input += 2;
            }
        });
        button_3 = new Button("in_main_menu", game, new EventHandler() {
            @Override
            public void execute() {
                current_input += 3;
            }
        });
        button_4 = new Button("in_main_menu", game, new EventHandler() {
            @Override
            public void execute() {
                current_input += 4;
            }
        });
        button_5 = new Button("in_main_menu", game, new EventHandler() {
            @Override
            public void execute() {
                current_input += 5;
            }
        });
        button_6 = new Button("in_main_menu", game, new EventHandler() {
            @Override
            public void execute() {
                current_input += 6;
            }
        });
        button_7 = new Button("in_main_menu", game, new EventHandler() {
            @Override
            public void execute() {
                current_input += 7;
            }
        });
        button_8 = new Button("in_main_menu", game, new EventHandler() {
            @Override
            public void execute() {
                current_input += 8;
            }
        });
        button_9 = new Button("in_main_menu", game, new EventHandler() {
            @Override
            public void execute() {
                current_input += 9;
            }
        });

        button_0.setNinePatch(6).setSound("click_1").setLabel("0");
        button_1.setNinePatch(6).setSound("click_1").setLabel("1");
        button_2.setNinePatch(6).setSound("click_1").setLabel("2");
        button_3.setNinePatch(6).setSound("click_1").setLabel("3");
        button_4.setNinePatch(6).setSound("click_1").setLabel("4");
        button_5.setNinePatch(6).setSound("click_1").setLabel("5");
        button_6.setNinePatch(6).setSound("click_1").setLabel("6");
        button_7.setNinePatch(6).setSound("click_1").setLabel("7");
        button_8.setNinePatch(6).setSound("click_1").setLabel("8");
        button_9.setNinePatch(6).setSound("click_1").setLabel("9");

        button_clean = new Button("in_main_menu", game, new EventHandler() {
            @Override
            public void execute() {
                if (current_input.length() > 0) {
                    current_input = current_input.substring(0, current_input.length() - 1);
                }
            }
        });
        button_clean.setNinePatch(6).setSound("click_1").setLabel("<");

        button_accept = new Button("in_main_menu", game, new EventHandler() {
            @Override
            public void execute() {
                if (encrypt(current_input).equals(public_code)){
                    game.userData.premium_is_on = true;
                    game.save_user_data();
                    game.setScreen("menu_settings");
                }
                else {
                    update_code();
                }
                current_input = "";
            }
        });
        button_accept.setNinePatch(6).setSound("click_1").setLabel(">");

        back_button = new Button("in_main_menu", game, new EventHandler() {
            @Override
            public void execute() {
                game.setScreen("menu_settings");
            }
        });
        back_button.setNinePatch(6).setLabel(game.locale.get("Back"));

    }

    @Override
    public void resize(int width, int height) {
        info_label.resize(game.upper_button_corner_x, game.upper_button_corner_y, game.button_w, game.button_h, 1);

        float num_pad_x = game.upper_button_corner_x;
        float num_pad_y = game.upper_button_corner_y - game.down_margin * 4;
        float num_pad_w = game.button_w;
        float num_pad_h = game.upper_button_corner_y - game.down_margin + game.button_h - num_pad_y;

        float code_label_h = game.button_h * 0.3f;

        code_label.resize(num_pad_x, num_pad_y + num_pad_h - code_label_h, num_pad_w, code_label_h, Align.left);

        num_pad_h -= code_label_h;

        float num_pad_button_margin_x = num_pad_w / 3;
        float num_pad_button_margin_y = num_pad_h / 4;

        float num_pad_button_center_x = num_pad_x + num_pad_button_margin_x / 2;
        float num_pad_button_center_y = num_pad_y + num_pad_button_margin_y / 2;

        float num_pad_button_w = num_pad_button_margin_x * game.padding;
        float num_pad_button_h = num_pad_button_margin_y * game.padding;

        float num_pad_button_corner_x = num_pad_button_center_x - num_pad_button_w / 2;
        float num_pad_button_corner_y = num_pad_button_center_y - num_pad_button_h / 2;

        button_clean.resize(num_pad_button_corner_x, num_pad_button_corner_y, num_pad_button_w, num_pad_button_h);
        button_0.resize(num_pad_button_corner_x + num_pad_button_margin_x, num_pad_button_corner_y, num_pad_button_w, num_pad_button_h);
        button_accept.resize(num_pad_button_corner_x + num_pad_button_margin_x * 2, num_pad_button_corner_y, num_pad_button_w, num_pad_button_h);

        button_7.resize(num_pad_button_corner_x, num_pad_button_corner_y + num_pad_button_margin_y, num_pad_button_w, num_pad_button_h);
        button_8.resize(num_pad_button_corner_x + num_pad_button_margin_x, num_pad_button_corner_y + num_pad_button_margin_y, num_pad_button_w, num_pad_button_h);
        button_9.resize(num_pad_button_corner_x + num_pad_button_margin_x * 2, num_pad_button_corner_y + num_pad_button_margin_y, num_pad_button_w, num_pad_button_h);

        button_4.resize(num_pad_button_corner_x, num_pad_button_corner_y + num_pad_button_margin_y * 2, num_pad_button_w, num_pad_button_h);
        button_5.resize(num_pad_button_corner_x + num_pad_button_margin_x, num_pad_button_corner_y + num_pad_button_margin_y * 2, num_pad_button_w, num_pad_button_h);
        button_6.resize(num_pad_button_corner_x + num_pad_button_margin_x * 2, num_pad_button_corner_y + num_pad_button_margin_y * 2, num_pad_button_w, num_pad_button_h);

        button_1.resize(num_pad_button_corner_x, num_pad_button_corner_y + num_pad_button_margin_y * 3, num_pad_button_w, num_pad_button_h);
        button_2.resize(num_pad_button_corner_x + num_pad_button_margin_x, num_pad_button_corner_y + num_pad_button_margin_y * 3, num_pad_button_w, num_pad_button_h);
        button_3.resize(num_pad_button_corner_x + num_pad_button_margin_x * 2, num_pad_button_corner_y + num_pad_button_margin_y * 3, num_pad_button_w, num_pad_button_h);

        // gap
        back_button.resize(game.upper_button_corner_x, game.upper_button_corner_y - game.down_margin * 5, game.button_w, game.button_h);
    }

    private void update_code(){
        Random random = new Random();

        //int code = random.nextInt(1_000_000) + 1_000_000;
        //private_code = String.valueOf(code).substring(1, 7);
        public_code = encrypt(String.valueOf(random.nextInt(1_000_000) + 1_000_000).substring(1, 7));

        code_label.string = public_code + ": ";
    }

    @Override
    public void show() {
        Gdx.gl20.glClearColor(0, 0, 0, 1);

        if (public_code == null) {
            update_code();
        }

        current_input = "";

    }

    @Override
    public void render(float delta) {
        Gdx.gl20.glClear(Gdx.gl20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();

        code_label.string = public_code + ": " + current_input;

        info_label.draw();
        code_label.draw();

        button_0.draw();
        button_1.draw();
        button_2.draw();
        button_3.draw();
        button_4.draw();
        button_5.draw();
        button_6.draw();
        button_7.draw();
        button_8.draw();
        button_9.draw();
        button_accept.draw();
        button_clean.draw();

        back_button.draw();

        game.batch.end();

        button_0.update();
        button_1.update();
        button_2.update();
        button_3.update();
        button_4.update();
        button_5.update();
        button_6.update();
        button_7.update();
        button_8.update();
        button_9.update();
        button_accept.update();
        button_clean.update();

        if (current_input.length() > 8){
            current_input = "";
            update_code();
        }

        back_button.update();

        if (game.is_back_button_pressed){
            game.is_back_button_pressed = false;
            game.setScreen("menu_settings");
        }
    }


    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    private String encrypt(String n){
        long k = Long.valueOf("1" + n) + 39L;
        long a = 6_180_339_887L;
        long d = 10_000_000_000L;
        String tmp = String.valueOf(k * a % d);
        return tmp.substring(tmp.length() - 6, tmp.length());
    }
}
