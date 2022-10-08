package com.quartyom.game_elements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

import java.util.HashMap;
import java.util.Map;

public class SoundHolder {
    private Map<String, Sound> sounds;

    public SoundHolder(){
        sounds = new HashMap<String, Sound>();
    }

    public void load(String name){
        sounds.put(name, Gdx.audio.newSound(Gdx.files.internal("sounds/" + name + ".wav")));
    }

    public Sound get(String name){
        if (!sounds.containsKey(name)) {
            sounds.put(name, Gdx.audio.newSound(Gdx.files.internal("sounds/" + name + ".wav")));
        }
        return sounds.get(name);
    }


    public void dispose(){
        for (Sound item: sounds.values()){
            item.dispose();
        }
        sounds.clear();
    }
}
