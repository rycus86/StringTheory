package hu.rycus.watchface.stringtheory.components;

import android.graphics.Paint;
import android.graphics.Rect;
import android.text.format.Time;

public abstract class TextUI {

    protected final Paint paint;

    protected int value = -1;
    protected String text;

    protected final Rect bounds = new Rect();

    public TextUI(final Paint paint) {
        this.paint = paint;
    }

    public void update(final Time time) {
        if (hasChanged(time)) {
            onUpdate(time);
            onPrepareMeasurement(paint);
            measureText(paint);
        }
    }

    protected abstract boolean hasChanged(final Time time);

    protected abstract void onUpdate(final Time time);

    protected void onPrepareMeasurement(final Paint paint) { }

    protected void measureText(final Paint paint) {
        paint.getTextBounds(text, 0, text.length(), bounds);
    }

    public int width() {
        return bounds.width();
    }

    public int height() {
        return bounds.height();
    }

}
