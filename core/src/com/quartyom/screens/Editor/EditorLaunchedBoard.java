package com.quartyom.screens.Editor;

import com.badlogic.gdx.math.Vector2;
import com.quartyom.game_elements.GameBoard;
import com.quartyom.game_elements.InputState;
import com.quartyom.game_elements.PressTimer;
import com.quartyom.game_elements.Scroller;
import com.quartyom.screens.Level.BoardDrawer;
import com.quartyom.screens.Level.Gameplay;

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
