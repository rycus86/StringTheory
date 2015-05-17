package hu.rycus.watchface.stringtheory.components;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.format.Time;

import com.google.android.gms.wearable.DataMap;

import hu.rycus.watchface.commons.Component;
import hu.rycus.watchface.stringtheory.config.Configuration;

public class HourAndMinute extends Component {

    private static final String FONT_FAMILY = "sans-serif-condensed-light";

    private final Rect hourBounds = new Rect();
    private final Rect minuteBounds = new Rect();

    private String hour;
    private String minute;

    private Typeface normalTypeface;
    private Typeface boldTypeface;

    private boolean display24hours = true;

    @Override
    protected void onSetupPaint(final Paint paint) {
        this.normalTypeface = Typeface.create(FONT_FAMILY, Typeface.NORMAL);
        this.boldTypeface = Typeface.create(FONT_FAMILY, Typeface.BOLD);

        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);
    }

    @Override
    protected void onApplyConfiguration(final DataMap configuration) {
        super.onApplyConfiguration(configuration);

        display24hours = Configuration.SHOW_24_HOURS.getBoolean(configuration);

        final Time currentTime = new Time();
        currentTime.setToNow();
        prepareValues(currentTime);
    }

    @Override
    protected void onSizeSet(final int width, final int height, final boolean round) {
        super.onSizeSet(width, height, round);

        final Time currentTime = new Time();
        currentTime.setToNow();
        prepareValues(currentTime);
    }

    @Override
    protected void onTimeTick(final Time time) {
        prepareValues(time);
    }

    private void prepareValues(final Time time) {
        final float baseTextSize = isRound ? 64f : 80f;

        if (display24hours) {
            hour = time.format("%H");
        } else {
            hour = time.format("%I");
        }

        paint.setTextSize(baseTextSize);
        paint.setTypeface(boldTypeface);
        paint.getTextBounds(hour, 0, hour.length(), hourBounds);

        minute = time.format("%M");
        paint.setTextSize(baseTextSize - 16f);
        paint.setTypeface(normalTypeface);
        paint.getTextBounds(minute, 0, minute.length(), minuteBounds);
    }

    @Override
    protected void onDraw(final Canvas canvas, final Time time) {
        final float baseTextSize, left, top;

        if (isRound) {
            baseTextSize = 64f;
            left = canvasWidth * 0.2f;
            top = canvasHeight * 0.15f;
        } else {
            baseTextSize = 80f;
            left = 20f;
            top = 20f;
        }

        paint.setTextSize(baseTextSize);
        paint.setTypeface(boldTypeface);
        canvas.drawText(hour, left, top + hourBounds.height(), paint);

        final float minuteTop = top + hourBounds.height() * 0.35f;
        final float minuteLeft = left + hourBounds.width() * 1.1f;

        paint.setTextSize(baseTextSize - 16f);
        paint.setTypeface(normalTypeface);
        canvas.drawText(minute, minuteLeft, minuteTop + minuteBounds.height(), paint);
    }

}
