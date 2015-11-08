package hu.rycus.watchface.stringtheory.commons.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import hu.rycus.watchface.stringtheory.commons.config.Palette;

public class PaletteView extends View {

    private final Paint paint = new Paint();

    private Strings strings;

    public PaletteView(final Context context) {
        super(context);
        onInitPaint();
    }

    public PaletteView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        onInitPaint();
    }

    public PaletteView(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        onInitPaint();
    }

    public void setPalette(final Palette palette) {
        strings = new Strings.Builder()
                .count(5)
                .palette(palette)
                .build();

        invalidate();
    }

    private void onInitPaint() {
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStrokeCap(Paint.Cap.ROUND);
        setPalette(Palette.ORIGINAL);
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        paint.setStyle(Paint.Style.STROKE);
        strings.setCanvasSize(getWidth(), getHeight());
        strings.skipToNextState();
        strings.draw(canvas, paint, true, true);
    }

}
