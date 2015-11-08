package hu.rycus.watchface.stringtheory.commons.config;

import android.graphics.Color;

public enum Palette implements PaletteItem {

    ORIGINAL(Color.RED, Color.GREEN, Color.WHITE),
    TEST(Color.BLUE, Color.YELLOW, Color.WHITE);

    private final int[] colors;

    Palette(final int... colors) {
        this.colors = colors;
    }

    @Override
    public int get(final int index) {
        return this.colors[index];
    }

}
