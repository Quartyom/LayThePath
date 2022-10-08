package com.quartyom.game_elements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.quartyom.MakeTheWay;

import java.util.HashMap;
import java.util.Map;

public class Locale {
    private MakeTheWay game;
    private Json json;
    private Map<String, String> tags;

    public Locale(MakeTheWay game){
        this.game = game;
        json = new Json();
        tags = new HashMap<String, String>();
    }

    public void set(String language){
        FileHandle a = Gdx.files.internal("texts/" + language + "/tags.json");
        tags.clear();
        tags = json.fromJson(tags.getClass(), a);

    }

    public String get(String tag){

        if (tags.containsKey(tag)){
            return tags.get(tag);
        }

        else if (game.userData.locale.equals("en")){
            return tag;
        }

        else {
            Gdx.files.local("localeee.json").writeString("\""+tag+"\" : \"\"\n", true);
            return "[RAW]: " + tag;
        }
    }
}
