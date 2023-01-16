package com.quartyom.game_elements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Queue;

public class QuFPS {

    Queue<Integer> fps_queue;

    public QuFPS(){
        fps_queue = new Queue<>();
    }

    public String update(){

        fps_queue.addLast(Gdx.graphics.getFramesPerSecond());

        if (fps_queue.size > 60) {
            fps_queue.removeFirst();
        }

        int sum = 0;
        int min = Integer.MAX_VALUE;

        for (int item : fps_queue){
            sum += item;
            if (item < min) {
                min = item;
            }
        }

        return min + " / " + Math.round((float)sum / fps_queue.size);

    }
}
