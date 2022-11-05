package com.quartyom.game_elements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.quartyom.LayThePath;

import java.util.HashMap;
import java.util.Map;

public class Locale {
    private final LayThePath game;
    private Map<String, String> tags;

    public Locale(LayThePath game){
        this.game = game;
        tags = new HashMap<>();
    }

    public void set(String language){
        game.userData.locale = language;
        game.save_user_data();
        tags = game.json.fromJson(tags.getClass(), Gdx.files.internal("texts/" + language + "/tags.json"));
    }

    public String get(String tag){
        if (tags.containsKey(tag)){
            return tags.get(tag);
        }
        else if (game.userData.locale.equals("en")){
            return tag;
        }
        else {
            //Gdx.files.local("raw_locale.json").writeString("\""+tag+"\" : \"\"\n", true);
            return "[RAW]: " + tag;
        }
    }
}
