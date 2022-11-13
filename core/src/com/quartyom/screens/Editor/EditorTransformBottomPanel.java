package com.quartyom.screens.Editor;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.quartyom.game_elements.Button;
import com.quartyom.game_elements.GameBottomPanel;
import com.quartyom.interfaces.QuEvent;

public class EditorTransformBottomPanel extends GameBottomPanel {
    public boolean is_active = false;

    public final EditorScreen editorScreen;

    Button back_button, turn_clockwise_button, turn_counterclockwise_button, mirror_button;

    public EditorTransformBottomPanel(final EditorScreen editorScreen){
        super(editorScreen.game);
        this.editorScreen = editorScreen;

        back_button = new Button("back", game, new QuEvent() {
            @Override
            public void execute() {
                is_active = false;
                if (editorScreen.editorBoard.is_active) {
                    editorScreen.editorBottomPanel.is_active = true;
                }
                else if (editorScreen.editorLaunchedBoard.is_active){
                    editorScreen.editorLaunchedBottomPanel.is_active = true;
                }
            }
        });
        back_button.setHint(game.locale.get("back")).setSound("click_1");

        turn_counterclockwise_button = new Button("turn_counterclockwise", game, new QuEvent() {
            @Override
            public void execute() {
                if (editorScreen.editorBoard.is_active) {
                    editorScreen.editorBoard.gameplay.counterclockwise_turn();
                }
                else if (editorScreen.editorLaunchedBoard.is_active){
                    editorScreen.editorLaunchedBoard.gameplay.counterclockwise_turn();
                }
            }
        });
        turn_counterclockwise_button.setHint(game.locale.get("counterclockwise turn")).setSound("click_1");

        turn_clockwise_button = new Button("turn_clockwise", game, new QuEvent() {
            @Override
            public void execute() {
                if (editorScreen.editorBoard.is_active) {
                    editorScreen.editorBoard.gameplay.clockwise_turn();
                }
                else if (editorScreen.editorLaunchedBoard.is_active){
                    editorScreen.editorLaunchedBoard.gameplay.clockwise_turn();
                }
            }
        });
        turn_clockwise_button.setHint(game.locale.get("clockwise turn")).setSound("click_1");

        mirror_button = new Button("mirror", game, new QuEvent() {
            @Override
            public void execute() {
                if (editorScreen.editorBoard.is_active) {
                    editorScreen.editorBoard.gameplay.mirror_turn();
                }
                else if (editorScreen.editorLaunchedBoard.is_active){
                    editorScreen.editorLaunchedBoard.gameplay.mirror_turn();
                }
            }
        });
        mirror_button.setHint(game.locale.get("mirror")).setSound("click_1");

    }

    @Override
    public void resize(){
        super.resize();
        back_button.resize(first_button_x, first_button_y, button_w, button_h);
        turn_counterclockwise_button.resize(first_button_x + panel_w / 4, first_button_y, button_w, button_h);
        turn_clockwise_button.resize(first_button_x + panel_w / 4 * 2, first_button_y, button_w, button_h);
        mirror_button.resize(first_button_x + panel_w / 4 * 3, first_button_y, button_w, button_h);
    }

    @Override
    public void draw(){
        if (!is_active){return;}
        super.draw();
        back_button.draw();
        turn_counterclockwise_button.draw();
        turn_clockwise_button.draw();
        mirror_button.draw();
    }

    public void update(){
        if (!is_active){return;}
        back_button.update();
        turn_counterclockwise_button.update();
        turn_clockwise_button.update();
        mirror_button.update();
    }
}
