package com.quartyom.game_elements;

import com.badlogic.gdx.graphics.Color;

import java.util.Random;

public class ColorHolder {
    private Random random;
    private int start_index;

    private Color predefined_colors[] = {
            Color.CYAN, Color.BLUE, Color.MAGENTA, Color.MAROON,
            Color.CORAL, Color.YELLOW, Color.TAN, Color.ORANGE
    };

    public ColorHolder(){
        random = new Random();
    }

    public void refresh(){
        start_index = random.nextInt(predefined_colors.length);
    }

    public Color get(Integer color_id) {
        if (color_id >= predefined_colors.length) {
            return predefined_colors[-5]; // вызовет поломку, если цветов много
        }
        return predefined_colors[(color_id + start_index) % predefined_colors.length];
    }
}
