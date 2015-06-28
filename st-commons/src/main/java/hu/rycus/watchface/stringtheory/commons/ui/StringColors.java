package hu.rycus.watchface.stringtheory.commons.ui;

public class StringColors {

    private final int firstColor;
    private final int lastColor;
    private final int defaultColor;

    public StringColors(final int firstColor, final int lastColor, final int defaultColor) {
        this.firstColor = firstColor;
        this.lastColor = lastColor;
        this.defaultColor = defaultColor;
    }

    public int getFirstColor() {
        return firstColor;
    }

    public int getLastColor() {
        return lastColor;
    }

    public int getDefaultColor() {
        return defaultColor;
    }

    public boolean isDefaultColor(final int color) {
        return color == defaultColor;
    }

}
