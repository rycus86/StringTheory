package hu.rycus.watchface.stringtheory.components;

import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.format.Time;

import hu.rycus.watchface.commons.Component;

public class HourAndMinute extends Component {

    final AssetManager assetManager;
    final Typeface normalTypeface;
    final Typeface boldTypeface;

    public HourAndMinute(final AssetManager assetManager) {
        this.assetManager = assetManager;

        this.normalTypeface = Typeface.create("sans-serif-condensed-light", Typeface.NORMAL);
        this.boldTypeface = Typeface.create("sans-serif-condensed-light", Typeface.BOLD);
    }

    @Override
    protected void onSetupPaint(final Paint paint) {
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);
        paint.setShadowLayer(2, 0, 2, Color.BLACK);
    }

    @Override
    protected void onSizeSet(final int width, final int height, final boolean round) {
        super.onSizeSet(width, height, round);
        if (round) {
            paint.setTextSize(64f);
        } else {
            paint.setTextSize(80f);
        }
    }

    final Rect textBounds = new Rect();

    @Override
    protected void onDraw(final Canvas canvas, final Time time) {
        final String hour = time.format("%H");
        paint.setTypeface(boldTypeface);
        paint.getTextBounds(hour, 0, hour.length(), textBounds);
        final int boldWidth = textBounds.width();
        final int boldHeight = textBounds.height();

        final String minute = time.format("%M");
        paint.setTypeface(normalTypeface);
        paint.getTextBounds(minute, 0, minute.length(), textBounds);
        final int normalHeight = textBounds.height();

        final int height = Math.max(boldHeight, normalHeight);
        final int top = isRound ? 50 : 10;

        final float textCenter = isRound ? canvasWidth * 0.35f : canvasWidth / 2f;

        paint.setTypeface(boldTypeface);
        canvas.drawText(hour, textCenter - boldWidth - 4, top + height, paint);

        paint.setTypeface(normalTypeface);
        canvas.drawText(minute, textCenter + 4, top + height, paint);
    }

}
