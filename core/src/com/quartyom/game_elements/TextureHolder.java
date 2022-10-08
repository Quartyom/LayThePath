package com.quartyom.game_elements;

import com.badlogic.gdx.graphics.Texture;

import java.util.HashMap;
import java.util.Map;

public class TextureHolder {
    private Map<String, Texture> textures;
    public String path_prefix, path_suffix;

    public TextureHolder(){
        this(new String(), new String());
    }

    public TextureHolder(String path_prefix, String path_suffix){
        this.path_prefix = path_prefix;
        this.path_suffix = path_suffix;
        textures = new HashMap<String, Texture>();
    }

    /*public void load(String file_name){
        textures.put(file_name, new Texture(path_prefix + file_name + path_suffix));
    }*/

    public void load(String[] file_names){
        for (String file_name: file_names){
            if (!textures.containsKey(file_name)) {
                textures.put(file_name, new Texture(path_prefix + file_name + path_suffix));
            }
        }
    }

    public Texture get(String file_name){
        if (!textures.containsKey(file_name)){
            textures.put(file_name, new Texture(path_prefix + file_name + path_suffix));
        }
        return textures.get(file_name);
    }

    /*public Texture get(String name){
        return textures.get(name);
    }*/

    public void dispose(){
        for (Texture item: textures.values()){
            item.dispose();
        }
        textures.clear();
    }
}
