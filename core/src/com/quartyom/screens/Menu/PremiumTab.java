package com.quartyom.screens.Menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Align;
import com.quartyom.LayThePath;
import com.quartyom.game_elements.Button;
import com.quartyom.game_elements.Label;
import com.quartyom.game_elements.QuScreen;
import com.quartyom.interfaces.QuEvent;

import java.util.Random;

public class PremiumTab extends QuScreen {

    final LayThePath game;

    Label infoLabel, codeLabel;
    Button button_0, button_1, button_2, button_3, button_4, button_5, button_6, button_7, button_8, button_9, buttonClean, buttonAccept, backButton;

    private String currentInput;

    public PremiumTab(final LayThePath game) {
        this.game = game;

        infoLabel = new Label(game, game.locale.get("Get premium"));
        codeLabel = new Label(game);
        codeLabel.targetString = "000000: 00000000";

        button_0 = new Button("in_main_menu", game, new QuEvent() {
            @Override
            public void execute() {
                currentInput += 0;
            }
        });
        button_1 = new Button("in_main_menu", game, new QuEvent() {
            @Override
            public void execute() {
                currentInput += 1;
            }
        });
        button_2 = new Button("in_main_menu", game, new QuEvent() {
            @Override
            public void execute() {
                currentInput += 2;
            }
        });
        button_3 = new Button("in_main_menu", game, new QuEvent() {
            @Override
            public void execute() {
                currentInput += 3;
            }
        });
        button_4 = new Button("in_main_menu", game, new QuEvent() {
            @Override
            public void execute() {
                currentInput += 4;
            }
        });
        button_5 = new Button("in_main_menu", game, new QuEvent() {
            @Override
            public void execute() {
                currentInput += 5;
            }
        });
        button_6 = new Button("in_main_menu", game, new QuEvent() {
            @Override
            public void execute() {
                currentInput += 6;
            }
        });
        button_7 = new Button("in_main_menu", game, new QuEvent() {
            @Override
            public void execute() {
                currentInput += 7;
            }
        });
        button_8 = new Button("in_main_menu", game, new QuEvent() {
            @Override
            public void execute() {
                currentInput += 8;
            }
        });
        button_9 = new Button("in_main_menu", game, new QuEvent() {
            @Override
            public void execute() {
                currentInput += 9;
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

        buttonClean = new Button("in_main_menu", game, new QuEvent() {
            @Override
            public void execute() {
                if (currentInput.length() > 0) {
                    currentInput = currentInput.substring(0, currentInput.length() - 1);
                }
            }
        });
        buttonClean.setNinePatch(6).setSound("click_1").setLabel("<");

        buttonAccept = new Button("in_main_menu", game, new QuEvent() {
            @Override
            public void execute() {
                if (encrypt(currentInput).equals(game.userData.premium_public_code)) {
                    game.userData.premium_is_on = true;
                    game.saveUserData();
                    game.setScreen("menu_settings");
                } else {
                    updateCode();
                }
                currentInput = "";
            }
        });
        buttonAccept.setNinePatch(6).setSound("click_1").setLabel(">");

        backButton = new Button("in_main_menu", game, new QuEvent() {
            @Override
            public void execute() {
                game.setScreen("menu_settings");
            }
        });
        backButton.setNinePatch(6).setLabel(game.locale.get("Back"));

    }

    @Override
    public void resize(int width, int height) {
        infoLabel.resize(game.upperButtonCornerX, game.upperButtonCornerY, game.buttonW,
                game.buttonH, 1);

        float num_pad_x = game.upperButtonCornerX;
        float num_pad_y = game.upperButtonCornerY - game.downMargin * 4;
        float num_pad_w = game.buttonW;
        float num_pad_h = game.upperButtonCornerY - game.downMargin + game.buttonH - num_pad_y;

        float code_label_h = game.buttonH * 0.3f;

        codeLabel.resize(num_pad_x, num_pad_y + num_pad_h - code_label_h, num_pad_w, code_label_h,
                Align.left);

        num_pad_h -= code_label_h;

        float num_pad_button_margin_x = num_pad_w / 3;
        float num_pad_button_margin_y = num_pad_h / 4;

        float num_pad_button_center_x = num_pad_x + num_pad_button_margin_x / 2;
        float num_pad_button_center_y = num_pad_y + num_pad_button_margin_y / 2;

        float num_pad_button_w = num_pad_button_margin_x * game.padding;
        float num_pad_button_h = num_pad_button_margin_y * game.padding;

        float num_pad_button_corner_x = num_pad_button_center_x - num_pad_button_w / 2;
        float num_pad_button_corner_y = num_pad_button_center_y - num_pad_button_h / 2;

        buttonClean.resize(num_pad_button_corner_x, num_pad_button_corner_y, num_pad_button_w,
                num_pad_button_h);
        button_0.resize(num_pad_button_corner_x + num_pad_button_margin_x, num_pad_button_corner_y,
                num_pad_button_w, num_pad_button_h);
        buttonAccept.resize(num_pad_button_corner_x + num_pad_button_margin_x * 2,
                num_pad_button_corner_y, num_pad_button_w, num_pad_button_h);

        button_7.resize(num_pad_button_corner_x, num_pad_button_corner_y + num_pad_button_margin_y,
                num_pad_button_w, num_pad_button_h);
        button_8.resize(num_pad_button_corner_x + num_pad_button_margin_x,
                num_pad_button_corner_y + num_pad_button_margin_y, num_pad_button_w, num_pad_button_h);
        button_9.resize(num_pad_button_corner_x + num_pad_button_margin_x * 2,
                num_pad_button_corner_y + num_pad_button_margin_y, num_pad_button_w, num_pad_button_h);

        button_4.resize(num_pad_button_corner_x, num_pad_button_corner_y + num_pad_button_margin_y * 2,
                num_pad_button_w, num_pad_button_h);
        button_5.resize(num_pad_button_corner_x + num_pad_button_margin_x,
                num_pad_button_corner_y + num_pad_button_margin_y * 2, num_pad_button_w, num_pad_button_h);
        button_6.resize(num_pad_button_corner_x + num_pad_button_margin_x * 2,
                num_pad_button_corner_y + num_pad_button_margin_y * 2, num_pad_button_w, num_pad_button_h);

        button_1.resize(num_pad_button_corner_x, num_pad_button_corner_y + num_pad_button_margin_y * 3,
                num_pad_button_w, num_pad_button_h);
        button_2.resize(num_pad_button_corner_x + num_pad_button_margin_x,
                num_pad_button_corner_y + num_pad_button_margin_y * 3, num_pad_button_w, num_pad_button_h);
        button_3.resize(num_pad_button_corner_x + num_pad_button_margin_x * 2,
                num_pad_button_corner_y + num_pad_button_margin_y * 3, num_pad_button_w, num_pad_button_h);

        // gap
        backButton.resize(game.upperButtonCornerX,
                game.upperButtonCornerY - game.downMargin * 5, game.buttonW, game.buttonH);
    }

    private void updateCode() {
        Random random = new Random();

        //int code = random.nextInt(1_000_000) + 1_000_000;
        //private_code = String.valueOf(code).substring(1, 7);
        game.userData.premium_public_code = encrypt(
                String.valueOf(random.nextInt(1_000_000) + 1_000_000).substring(1, 7));
        game.saveUserData();

        codeLabel.string = game.userData.premium_public_code + ": ";
    }

    @Override
    public void show() {
        Gdx.gl20.glClearColor(0, 0, 0, 1);

        if (game.userData.premium_public_code == null) {
            updateCode();
        }

        currentInput = "";

    }

    @Override
    public void render(float delta) {
        Gdx.gl20.glClear(Gdx.gl20.GL_COLOR_BUFFER_BIT);

        codeLabel.string = game.userData.premium_public_code + ": " + currentInput;

        infoLabel.draw();
        codeLabel.draw();

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
        buttonAccept.draw();
        buttonClean.draw();
        backButton.draw();

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
        buttonAccept.update();
        buttonClean.update();

        if (currentInput.length() > 8) {
            currentInput = "";
            updateCode();
        }

        backButton.update();

        if (game.isBackButtonPressed) {
            game.isBackButtonPressed = false;
            game.setScreen("menu_settings");
        }
    }

    private String encrypt(String n) {
        long k = Long.valueOf("1" + n) + 39L;
        long a = 6_180_339_887L;
        long d = 10_000_000_000L;
        String tmp = String.valueOf(k * a % d);
        return tmp.substring(tmp.length() - 6, tmp.length());
    }
}
