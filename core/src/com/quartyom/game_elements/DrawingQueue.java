package com.quartyom.game_elements;

import com.badlogic.gdx.utils.Queue;
import com.quartyom.MakeTheWay;
import com.quartyom.interfaces.Drawable;

public class DrawingQueue {
    private Queue<Drawable> elements;

    private MakeTheWay game;

    public DrawingQueue(MakeTheWay game){
        this.game = game;
        elements = new Queue<Drawable>();
    }

    public void add(Drawable element){
        elements.addLast(element);
    }

    public void draw(){
        game.batch.begin();
        for (Drawable item: elements){
            item.draw();
        }
        elements.clear();
        game.batch.end();
    }
}
