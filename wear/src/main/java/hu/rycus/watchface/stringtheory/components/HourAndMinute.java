package hu.rycus.watchface.stringtheory.components;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.format.Time;

import com.google.android.gms.wearable.DataMap;

import hu.rycus.watchface.commons.Component;
import hu.rycus.watchface.stringtheory.config.Configuration;

public class HourAndMinute extends Component {

    private static final String FONT_FAMILY = "sans-serif-condensed-light";

    private Typeface normalTypeface;
    private Typeface boldTypeface;

    private boolean display24hours = true;

    private final HourUI hour = new HourUI(paint);
    private final MinuteUI minute = new MinuteUI(paint);

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

        hour.update(time);
        minute.update(time);

        paint.setTextSize(baseTextSize);
        paint.setTypeface(boldTypeface);
        canvas.drawText(hour.text, left, top + hour.height(), paint);

        final float minuteTop = top + hour.height() * 0.35f;
        final float minuteLeft = left + hour.width() * 1.1f;

        paint.setTextSize(baseTextSize - 16f);
        paint.setTypeface(normalTypeface);
        canvas.drawText(minute.text, minuteLeft, minuteTop + minute.height(), paint);
    }

    private class HourUI extends TextUI {

        public HourUI(final Paint paint) {
            super(paint);
        }

        @Override
        protected boolean hasChanged(final Time time) {
            return time.hour != value;
        }

        @Override
        protected void onUpdate(final Time time) {
            value = time.hour;
            text = display24hours ? time.format("%H") : time.format("%I");
        }

        @Override
        protected void onPrepareMeasurement(final Paint paint) {
            final float baseTextSize = isRound ? 64f : 80f;
            paint.setTextSize(baseTextSize);
            paint.setTypeface(boldTypeface);
        }
    }

    private class MinuteUI extends TextUI {

        public MinuteUI(final Paint paint) {
            super(paint);
        }

        @Override
        protected boolean hasChanged(final Time time) {
            return time.minute != value;
        }

        @Override
        protected void onUpdate(final Time time) {
            value = time.minute;
            text = time.format("%M");
        }

        @Override
        protected void onPrepareMeasurement(final Paint paint) {
            final float baseTextSize = isRound ? 64f : 80f;
            paint.setTextSize(baseTextSize - 16f);
            paint.setTypeface(normalTypeface);
        }

    }

}
