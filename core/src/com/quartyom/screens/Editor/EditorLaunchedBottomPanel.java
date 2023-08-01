package com.quartyom.screens.Editor;

import com.badlogic.gdx.Gdx;
import com.quartyom.game_elements.Button;
import com.quartyom.game_elements.GameBottomPanel;
import com.quartyom.interfaces.QuEvent;
import com.quartyom.screens.Level.LevelConfiguration;

public class EditorLaunchedBottomPanel extends GameBottomPanel {
    public boolean isActive = false;

    public final EditorScreen editorScreen;

    Button backButton, resetButton, transformButton, saveButton;

    public EditorLaunchedBottomPanel(final EditorScreen editorScreen) {
        super(editorScreen.game);
        this.editorScreen = editorScreen;

        backButton = new Button("back", game, new QuEvent() {
            @Override
            public void execute() {
                isActive = false;
                editorScreen.editorLaunchedBoard.isActive = false;
                editorScreen.editorLaunchedTopPanel.isActive = false;

                editorScreen.editorBottomPanel.isActive = true;
                editorScreen.editorBoard.isActive = true;
                editorScreen.editorTopPanel.isActive = true;
            }
        });
        backButton.setHint(game.locale.get("go back")).setSound("click_1");

        resetButton = new Button("reset", game, new QuEvent() {
            @Override
            public void execute() {
                editorScreen.editorLaunchedBoard.gameplay.resetBody();
            }
        });
        resetButton.setHint(game.locale.get("reset level")).setSound("click_1");

        transformButton = new Button("transform", game, new QuEvent() {
            @Override
            public void execute() {
                isActive = false;
                editorScreen.editorTransformBottomPanel.isActive = true;
            }
        });
        transformButton.setHint(game.locale.get("transform the field")).setSound("click_1");

        saveButton = new Button("save", game, new QuEvent() {
            @Override
            public void execute() {
                LevelConfiguration levelConfiguration = editorScreen.editorBoard.gameplay.getLevelConfiguration();
                Gdx.files.local("user_levels/" + editorScreen.editorLaunchedBoard.gameplay.field_size + "/" + System.currentTimeMillis() + ".json").writeString(editorScreen.game.json.prettyPrint(levelConfiguration), false);
                if (!game.userData.is_developer) {
                    return;
                }
                if (!editorScreen.editorLaunchedBoard.isLevelCompleted) {
                    return;
                }

                // чтобы нельзя было дважды нажать на кнопку
                if (editorScreen.editorLaunchedBoard.isLevelSaved) {
                    return;
                }
                editorScreen.editorLaunchedBoard.isLevelSaved = true;
                //System.out.println("pressed save button");

                editorScreen.editorLaunchedBoard.gameplay.setHint();
                //LevelConfiguration levelConfiguration = editorScreen.editorBoard.gameplay.getLevelConfiguration();
                //Gdx.files.local("user_levels/" + editorScreen.editorLaunchedBoard.gameplay.field_size + "/" + System.currentTimeMillis() + ".json").writeString(editorScreen.game.json.prettyPrint(levelConfiguration), false);
            }
        });
        saveButton.setHint(game.locale.get("save")).setSound("click_1");
    }

    @Override
    public void resize() {
        super.resize();
        backButton.resize(firstButtonX, firstButtonY, buttonW, buttonH);
        resetButton.resize(firstButtonX + panelW / 4, firstButtonY, buttonW, buttonH);
        transformButton.resize(firstButtonX + panelW / 4 * 2, firstButtonY, buttonW, buttonH);
        saveButton.resize(firstButtonX + panelW / 4 * 3, firstButtonY, buttonW, buttonH);
    }

    @Override
    public void draw() {
        if (!isActive) {
            return;
        }
        super.draw();
        backButton.draw();
        resetButton.draw();
        transformButton.draw();
        saveButton.draw();
    }

    public void update() {
        if (!isActive) {
            return;
        }
        backButton.update();
        resetButton.update();
        transformButton.update();
        if (game.userData.is_developer) {
            saveButton.update();
        }
    }

}
