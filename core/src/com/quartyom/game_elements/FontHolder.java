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

    private FreeTypeFontGenerator generator;
    private Map<Integer, BitmapFont> fonts_light, fonts_with_latin, international_fonts;
    private FreeTypeFontGenerator.FreeTypeFontParameter parameter;
    private String characters_light, characters_with_latin, international_characters;
    private boolean latin_is_light = false;

    // " !\"#$%&\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz[\\]^_`{|}~";
    public static final String default_characters = " !\"#$%&\'()*+,-./0123456789:;<=>?@[\\]^_`{|}~";
    public static final String latin_characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    public FontHolder(LayThePath game, String path){
        this.game = game;

        FreeTypeFontGenerator.setMaxTextureSize(2048);  // очень важная строчка, без неё часть символов не отображается
        generator = new FreeTypeFontGenerator(Gdx.files.internal(path));
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.color = Color.WHITE;

        fonts_light = new HashMap<>();
        fonts_with_latin = new HashMap<>();

        update_locale();

        international_fonts = new HashMap<>();
        international_characters = default_characters + latin_characters;
        for (String key : game.locale.folders.keySet()) {
            international_characters += Gdx.files.internal("texts/" + key + "/alphabet.txt").readString();
        }
        international_characters = international_characters.replaceAll("(.)\\1{1,}", "$1");
    }

    public void update_locale(){
        clear_fonts(fonts_light);
        clear_fonts(fonts_with_latin);

        String local_characters = Gdx.files.internal("texts/" + game.userData.locale + "/alphabet.txt").readString().replace("*", latin_characters);
        if (local_characters.contains("*")){
            latin_is_light = true;
            local_characters = local_characters.replace("*", latin_characters);
        }
        else {
            characters_with_latin = default_characters + latin_characters + local_characters;
            characters_with_latin = characters_with_latin.replaceAll("(.)\\1{1,}", "$1");
        }
        characters_light = default_characters + local_characters;
        characters_light = characters_light.replaceAll("(.)\\1{1,}", "$1"); // повторяющиеся символы удалит регулярное выражение
    }

    public BitmapFont get(Integer font_size, FontType fontType){

        switch (fontType){  // ищем в лёгких, затем латинице, затем международых
            case LOCALIZED_LIGHT:
                if (fonts_light.containsKey(font_size)){
                    return fonts_light.get(font_size);
                }
            case LOCALIZED_WITH_LATIN:
                if (fonts_with_latin.containsKey(font_size)){
                    return fonts_with_latin.get(font_size);
                }
            case INTERNATIONAL:
                if (international_fonts.containsKey(font_size)){
                    return international_fonts.get(font_size);
                }
        }

        Map<Integer, BitmapFont> fonts;

        if (fontType == FontType.LOCALIZED_LIGHT || fontType == FontType.LOCALIZED_WITH_LATIN && latin_is_light){
            fonts = fonts_light;
            parameter.characters = characters_light;
        }
        else if (fontType == FontType.LOCALIZED_WITH_LATIN){
            fonts = fonts_with_latin;
            parameter.characters = characters_with_latin;
        }
        else {
            fonts = international_fonts;
            parameter.characters = international_characters;
        }

        parameter.size = font_size;
        BitmapFont font = generator.generateFont(parameter);
        fonts.put(font_size, font);
        return font;
    }

    public int size(){
        return fonts_light.size();
    }

    public void dispose(){
        clear_fonts(fonts_light);
        clear_fonts(fonts_with_latin);
        clear_fonts(international_fonts);
        generator.dispose();
    }

    public void clear_fonts(Map<Integer, BitmapFont> fonts){
        for (BitmapFont item: fonts.values()){
            item.dispose();
        }
        fonts.clear();
    }
}
