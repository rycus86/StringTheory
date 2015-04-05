package hu.rycus.watchface.stringtheory;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.test.ApplicationTestCase;
import android.text.format.Time;

import java.io.FileOutputStream;
import java.io.IOException;

import hu.rycus.watchface.stringtheory.components.StringsBackground;


/** Test class to generate image assets. */
public class LauncherDrawer extends ApplicationTestCase<Application> {

    private static final Point[] sizes = {
            new Point(1024, 1024), new Point(500, 500), new Point(320, 320), new Point(280, 280)
    };
    private static final String filenamePattern = "ic_launcher_%dpx_%d_%s.%s";

    public LauncherDrawer() {
        super(Application.class);
    }

    public void testDrawImage() throws IOException {
        for (final Point size : sizes) {
            final TestBackground background = new TestBackground();

            for (int idx = 0; idx < 5; idx++) {
                final Bitmap bitmap = Bitmap.createBitmap(size.x, size.y, Bitmap.Config.ARGB_8888);
                final Canvas canvas = new Canvas(bitmap);
                final Time time = new Time();
                time.setToNow();

                background.setSize(size.x, size.y);

                String filename;

                background.prepare(canvas, true);
                background.onDraw(canvas, time);

                filename = String.format(filenamePattern, size.x, idx + 1, "round", "png");
                try (final FileOutputStream output = getContext()
                        .openFileOutput(filename, Context.MODE_PRIVATE)) {
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
                }

                background.prepare(canvas, false);
                background.onDraw(canvas, time);

                filename = String.format(filenamePattern, size.x, idx + 1, "rect", "png");
                try (final FileOutputStream output = getContext()
                        .openFileOutput(filename, Context.MODE_PRIVATE)) {
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
                }
            }
        }

        assertTrue(true);
    }

    private class TestBackground extends StringsBackground {

        public void setSize(final int width, final int height) {
            onSizeSet(width, height, true);
        }

        public void prepare(final Canvas canvas, final boolean roundBackground) {
            paint.setAntiAlias(true);
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
            if (roundBackground) {
                canvas.drawRoundRect(
                        0, 0, canvasWidth, canvasHeight,
                        canvasWidth / 10f, canvasHeight / 10f, paint);
            } else {
                canvas.drawPaint(paint);
            }
        }

        @Override
        public void onDraw(final Canvas canvas, final Time time) {
            onSetupPaint(paint);
            super.onDraw(canvas, time);
        }

        @Override
        public void onTimeTick(final Time time) {
            super.onTimeTick(time);
        }
    }

}