package hu.rycus.watchface.stringtheory.components;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.format.Time;

import hu.rycus.watchface.commons.Component;
import hu.rycus.watchface.commons.DateTimeUI;
import hu.rycus.watchface.commons.TimeField;

public class Date extends Component {

    private static final String FONT_FAMILY = "sans-serif-condensed-light";
    private static final float TEXT_HEIGHT = 18f;

    private final DateTimeUI weekDayAndDate =
            new DateTimeUI.Builder()
                    .field(TimeField.DATE)
                    .format("%a %d")
                    .build();

    private final DateTimeUI month =
            new DateTimeUI.Builder()
                    .field(TimeField.DATE)
                    .format("%B")
                    .build();

    @Override
    protected void onSetupPaint(final Paint paint) {
        paint.setAntiAlias(true);
        paint.setElegantTextHeight(true);
        paint.setTypeface(Typeface.create(FONT_FAMILY, Typeface.NORMAL));
        paint.setColor(Color.WHITE);
        paint.setTextSize(TEXT_HEIGHT);
    }

    @Override
    protected void onDraw(final Canvas canvas, final Time time) {
        final float left, top;

        if (isRound) {
            left = canvasWidth * 0.6f;
            top = canvasHeight * 0.15f;
        } else {
            left = canvasWidth * 0.6f;
            top = 50f;
        }

        weekDayAndDate.update(time, paint);
        month.update(time, paint);

        final float line1Bottom = top + TEXT_HEIGHT;
        final float line2Bottom = line1Bottom + TEXT_HEIGHT;

        canvas.drawText(weekDayAndDate.text(), left, line1Bottom, paint);
        canvas.drawText(month.text(), left, line2Bottom, paint);
    }

}
