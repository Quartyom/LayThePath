package com.quartyom.screens.Editor;

import com.quartyom.game_elements.Button;
import com.quartyom.game_elements.GameBottomPanel;
import com.quartyom.game_elements.Slider;
import com.quartyom.game_elements.SwitchButton;
import com.quartyom.interfaces.QuEvent;

public class EditorBottomPanel extends GameBottomPanel {
    public boolean isActive = true;

    public final EditorScreen editorScreen;

    Button launchButton, transformButton;
    SwitchButton fieldSizeSwitch, obstacleSwitch;
    Slider slider;

    public EditorBottomPanel(final EditorScreen editorScreen) {
        super(editorScreen.game);
        this.editorScreen = editorScreen;

        fieldSizeSwitch = new SwitchButton(game);
        fieldSizeSwitch.add("field_size_normal").add("field_size_pressed");
        fieldSizeSwitch.setSound("click_1");

        transformButton = new Button("transform", game, new QuEvent() {
            @Override
            public void execute() {
                isActive = false;
                editorScreen.editorTransformBottomPanel.isActive = true;
            }
        });
        transformButton.setHint(game.locale.get("transform the field")).setSound("click_1");

        obstacleSwitch = new SwitchButton(game);
        obstacleSwitch.add("obstacle_normal").add("obstacle_pressed");
        obstacleSwitch.setSound("click_1");

        launchButton = new Button("launch", game, new QuEvent() {
            @Override
            public void execute() {
                editorScreen.editorBoard.isActive = false;
                editorScreen.editorLaunchedBoard.activate();

                editorScreen.editorBottomPanel.isActive = false;
                editorScreen.editorLaunchedBottomPanel.isActive = true;

                editorScreen.editorTopPanel.isActive = false;
                editorScreen.editorLaunchedTopPanel.isActive = true;

            }
        });
        launchButton.setHint(game.locale.get("launch")).setSound("click_1");

        slider = new Slider("regular", game);
    }

    @Override
    public void resize() {
        super.resize();

        fieldSizeSwitch.resize(firstButtonX, firstButtonY, buttonW, buttonH);
        transformButton.resize(firstButtonX + panelW / 4, firstButtonY, buttonW, buttonH);
        obstacleSwitch.resize(firstButtonX + panelW / 4 * 2, firstButtonY, buttonW, buttonH);
        launchButton.resize(firstButtonX + panelW / 4 * 3, firstButtonY, buttonW, buttonH);

        float slider_center_x = 0;

        float slider_h = panelH / 2;
        float slider_w = panelW * 0.8f;

        slider.resize(slider_center_x - slider_w / 2, panelY + panelH * 1.2f, slider_w, slider_h);
    }

    @Override
    public void draw() {
        if (!isActive) {
            return;
        }
        super.draw();
        fieldSizeSwitch.draw();
        transformButton.draw();
        obstacleSwitch.draw();
        launchButton.draw();

        if (editorScreen.isSliderActive) {
            slider.draw();
        }
    }

    public void update() {
        if (!isActive) {
            return;
        }
        fieldSizeSwitch.update();
        obstacleSwitch.update();
        transformButton.update();
        launchButton.update();

        // если попали по кнопке, остальные сбрасываются
        if (fieldSizeSwitch.recentlyChanged) {
            obstacleSwitch.state = 0;
        } else if (obstacleSwitch.recentlyChanged) {
            fieldSizeSwitch.state = 0;
        }


        if (fieldSizeSwitch.state == 1) {
            editorScreen.isSliderActive = true;

            slider.value = (float) (editorScreen.editorBoard.gameplay.field_size - editorScreen.MIN_FILED_SIZE) / (editorScreen.MAX_FIELD_SIZE - editorScreen.MIN_FILED_SIZE);
            slider.update();

            int value = Math.round(slider.value * (editorScreen.MAX_FIELD_SIZE - editorScreen.MIN_FILED_SIZE)) + editorScreen.MIN_FILED_SIZE;
            if (value != editorScreen.editorBoard.gameplay.field_size) {
                editorScreen.editorBoard.gameplay.field_size = value;
                editorScreen.editorBoard.resize();
                editorScreen.editorBoard.gameplay.normalizeObstacles();
            }

        } else if (obstacleSwitch.state == 1) {
            editorScreen.isSliderActive = true;

            slider.value = (float) (editorScreen.editorBoard.cursorOnObstacles) / (editorScreen.editorBoard.obstaclesToPut.length - 1);
            slider.update();

            int value = Math.round(slider.value * (editorScreen.editorBoard.obstaclesToPut.length - 1));

            if (value != editorScreen.editorBoard.cursorOnObstacles) {
                editorScreen.editorBoard.cursorOnObstacles = value;
            }

        } else {
            editorScreen.isSliderActive = false;
        }

    }

}
