package hu.rycus.watchface.stringtheory.commons.ui;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.util.SparseArray;

import java.util.Random;

public class Strings {

    private final SparseArray<Shader> shaderByColor = new SparseArray<>();
    private final Path path = new Path();
    private final Random random = new Random();

    private final StringUI[] items;

    private float width;
    private float halfWidth;
    private float thirdWidth;
    private float twoThirdWidth;
    private float halfHeight;

    private int xLimit;
    private int yLimit;

    public static class Builder {

        private int stringCount;
        private int firstColor;
        private int lastColor;
        private int defaultColor;

        public Builder count(final int stringCount) {
            this.stringCount = stringCount;
            return this;
        }

        public Builder firstColor(final int firstColor) {
            this.firstColor = firstColor;
            return this;
        }

        public Builder lastColor(final int lastColor) {
            this.lastColor = lastColor;
            return this;
        }

        public Builder defaultColor(final int defaultColor) {
            this.defaultColor = defaultColor;
            return this;
        }

        public Strings build() {
            return new Strings(stringCount, new StringColors(firstColor, lastColor, defaultColor));
        }

    }

    private Strings(final int count, final StringColors colors) {
        this.items = new StringUI[count];
        this.addStrings(count, colors);
    }

    private void addStrings(final int count, final StringColors colors) {
        final float strokeWidthDiff = 0.05f;
        float strokeWidth = 1.25f;

        for (int idx = 0; idx < count; idx++) {
            int color = colors.getDefaultColor();
            float sWidth = strokeWidth;

            if (idx == count - 3) {
                color = colors.getFirstColor();
                sWidth = 3f;
            } else if (idx == count - 4) {
                color = colors.getLastColor();
                sWidth = 2f;
            }

            final StringUI item = new StringUI(color, sWidth);
            items[idx] = item;

            if (colors.isDefaultColor(color)) {
                strokeWidth += strokeWidthDiff;
            }
        }
    }

    private Shader getShader(final int color) {
        Shader shader = shaderByColor.get(color);
        if (shader == null) {
            final int opaque = color | 0xFF000000;
            final int transparent = color & 0x00FFFFFF;

            shader = new RadialGradient(
                    halfWidth, halfHeight, Math.min(halfWidth, halfHeight),
                    opaque, transparent, Shader.TileMode.CLAMP);
            shaderByColor.put(color, shader);
        }

        return shader;
    }

    public void setCanvasSize(final int width, final int height) {
        this.width = width;
        this.halfWidth = width / 2f;
        this.thirdWidth = width / 3f;
        this.twoThirdWidth = this.thirdWidth * 2f;
        this.halfHeight = height / 2f;

        this.xLimit = width / 5;
        this.yLimit = height;

        this.skipToNextState();
    }

    public void draw(final Canvas canvas, final Paint paint, final boolean colored) {
        for (final StringUI item : items) {
            final int color = colored ? item.color : Color.WHITE;
            paint.setShader(getShader(color));
            paint.setStrokeWidth(item.strokeWidth);

            path.reset();
            path.moveTo(0, halfHeight);
            path.cubicTo(
                    thirdWidth + item.currentPt1.x, halfHeight + item.currentPt1.y,
                    twoThirdWidth + item.currentPt2.x, halfHeight + item.currentPt2.y,
                    width, halfHeight);

            canvas.drawPath(path, paint);
        }

        paint.setShader(null);
    }

    public void prepareMove() {
        for (final StringUI item : items) {
            item.prepare(random, xLimit, yLimit);
        }
    }

    public void animate(final float progress) {
        for (final StringUI item : items) {
            item.move(progress);
        }
    }

    public void finishMove() {
        for (final StringUI item : items) {
            item.settle();
        }
    }

    public void skipToNextState() {
        prepareMove();
        finishMove();
    }

}