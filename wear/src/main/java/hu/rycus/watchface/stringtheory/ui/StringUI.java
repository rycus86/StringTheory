package hu.rycus.watchface.stringtheory.ui;

import android.graphics.PointF;

import java.util.Random;

class StringUI {

    final int color;
    final float strokeWidth;

    final PointF previousPt1 = new PointF();
    final PointF previousPt2 = new PointF();
    
    final PointF currentPt1 = new PointF();
    final PointF currentPt2 = new PointF();

    final PointF nextPt1 = new PointF();
    final PointF nextPt2 = new PointF();
    
    StringUI(int color, float strokeWidth) {
        this.color = color;
        this.strokeWidth = strokeWidth;
    }
    
    void prepare(final Random random, final int xLimit, final int yLimit) {
        nextPt1.x = next(random, xLimit);
        nextPt1.y = next(random, yLimit);
        nextPt2.x = next(random, xLimit);
        nextPt2.y = next(random, yLimit);
        if (Math.signum(nextPt1.y) == Math.signum(nextPt2.y)) {
            nextPt2.y *= -1;
        }
    }

    private static float next(final Random random, final int base) {
        return random.nextInt(base) - base / 2f;
    }
    
    void move(final float progress) {
        currentPt1.x = previousPt1.x + (nextPt1.x - previousPt1.x) * progress;
        currentPt1.y = previousPt1.y + (nextPt1.y - previousPt1.y) * progress;
        currentPt2.x = previousPt2.x + (nextPt2.x - previousPt2.x) * progress;
        currentPt2.y = previousPt2.y + (nextPt2.y - previousPt2.y) * progress;
    }
    
    void settle() {
        currentPt1.set(nextPt1);
        currentPt2.set(nextPt2);
        previousPt1.set(nextPt1);
        previousPt2.set(nextPt2);
    }

}
