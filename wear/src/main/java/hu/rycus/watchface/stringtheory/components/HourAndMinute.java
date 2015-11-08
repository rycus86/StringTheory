package hu.rycus.watchface.stringtheory.components;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.format.Time;

import com.google.android.gms.wearable.DataMap;

import hu.rycus.watchface.commons.Component;
import hu.rycus.watchface.commons.DateTimeUI;
import hu.rycus.watchface.commons.TimeField;
import hu.rycus.watchface.stringtheory.commons.config.Configuration;

public class HourAndMinute extends Component {

    private static final String FONT_FAMILY = "sans-serif-condensed-light";

    private Typeface normalTypeface;
    private Typeface boldTypeface;

    private final DateTimeUI hour =
            new DateTimeUI.Builder()
                    .field(TimeField.HOUR)
                    .format("%H")
                    .build();

    private final DateTimeUI minute =
            new DateTimeUI.Builder()
                    .field(TimeField.MINUTE)
                    .format("%M")
                    .build();

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

        final boolean display24hours = Configuration.SHOW_24_HOURS.getBoolean(configuration);
        hour.changeFormat(display24hours ? "%H" : "%I");
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

        hour.update(time, paint);

        canvas.drawText(hour.text(), left, top + hour.height(), paint);

        final float minuteTop = top + hour.height() * 0.35f;
        final float minuteLeft = left + hour.width() * 1.1f;

        paint.setTextSize(baseTextSize - 16f);
        paint.setTypeface(normalTypeface);

        minute.update(time, paint);

        canvas.drawText(minute.text(), minuteLeft, minuteTop + minute.height(), paint);
    }

}
