package com.quartyom.screens.Editor;

import com.quartyom.game_elements.GameBoard;

public class EditorLaunchedBoard extends GameBoard {
    public boolean isActive = false;

    public final EditorScreen editorScreen;

    public boolean isLevelCompleted = false;
    public boolean isLevelSaved = false;


    public EditorLaunchedBoard(EditorScreen editorScreen) {
        super(editorScreen.game);
        this.editorScreen = editorScreen;
    }

    public void activate() {
        isActive = true;
        isLevelCompleted = false;
        isLevelSaved = false;
        gameplay.setLevelConfiguration(editorScreen.editorBoard.gameplay.getLevelConfiguration());
        resize();
        gameplay.normalizeCursor(); // чтобы курсор не выпрыгнул за поле
    }

    public void resize() {
        super.resize(editorScreen.editorTopPanel.getHeight());
    }

    void draw() {
        if (!isActive) {
            return;
        }
        boardDrawer.draw();
    }

    @Override
    public void update() {
        if (!isActive) {
            return;
        }
        super.update();
    }

    @Override
    public void victoryAction() {
        isLevelCompleted = true;
    }

}
