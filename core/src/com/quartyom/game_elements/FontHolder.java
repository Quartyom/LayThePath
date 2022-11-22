package com.quartyom.game_elements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.quartyom.LayThePath;

import java.util.HashMap;
import java.util.Map;


public class FontHolder {
    LayThePath game;

    private Map<Integer, BitmapFont> fonts;
    private FreeTypeFontGenerator generator;
    private FreeTypeFontGenerator.FreeTypeFontParameter parameter;

    public FontHolder(LayThePath game, String path){
        this.game = game;

        fonts = new HashMap<>();

        FreeTypeFontGenerator.setMaxTextureSize(2048);  // очень важная строчка, без неё часть смволов не отображается
        generator = new FreeTypeFontGenerator(Gdx.files.internal(path));

        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.characters = " !\"#$%&\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz[\\]^_`{|}~";
        for (String key : game.locale.folders.keySet()) {
            parameter.characters += Gdx.files.internal("texts/" + key + "/alphabet.txt").readString();
        }
        parameter.characters = parameter.characters.replaceAll("(.)\\1{1,}", "$1"); // повторяющиеся символы удалит регулярное выражение
        System.out.println(parameter.characters);
        parameter.color = Color.WHITE;
    }

    public BitmapFont get(Integer font_size){
        if (!fonts.containsKey(font_size)) {
            parameter.size = font_size;
            fonts.put(font_size, generator.generateFont(parameter));
        }
        return fonts.get(font_size);
    }

    public int size(){
        return fonts.size();
    }

    public void dispose(){
        for (BitmapFont item: fonts.values()){
            item.dispose();
        }
        fonts.clear();
    }
}
