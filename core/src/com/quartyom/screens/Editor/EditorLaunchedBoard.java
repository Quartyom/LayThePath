package com.quartyom.screens.Editor;

import com.quartyom.game_elements.GameBoard;

public class EditorLaunchedBoard extends GameBoard {
    public boolean is_active = false;

    public final EditorScreen editorScreen;

    public boolean is_level_completed = false;
    public boolean is_level_saved = false;


    public EditorLaunchedBoard(EditorScreen editorScreen){
        super(editorScreen.game);
        this.editorScreen = editorScreen;
    }

    public void activate(){
        is_active = true;
        is_level_completed = false;
        is_level_saved = false;
        gameplay.set_level_configuration(editorScreen.editorBoard.gameplay.get_level_configuration());
        resize();
        gameplay.normalize_cursor(); // чтобы курсор не выпрыгнул за поле
    }

    public void resize(){
        super.resize(editorScreen.editorTopPanel.getHeight());
    }

    void draw(){
        if (!is_active){return;}
        boardDrawer.draw();
    }

    @Override
    public void update(){
        if (!is_active){return;}
        super.update();
    }

    @Override
    public void victory_action() {
        is_level_completed = true;
    }

}
