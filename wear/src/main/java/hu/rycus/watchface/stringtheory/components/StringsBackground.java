package hu.rycus.watchface.stringtheory.components;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.format.Time;
import android.view.animation.DecelerateInterpolator;

import hu.rycus.watchface.commons.Animation;
import hu.rycus.watchface.commons.Component;
import hu.rycus.watchface.stringtheory.ui.Strings;

public class StringsBackground extends Component {

    private static final int NUM_STRINGS = 10;

    private static final int MSG_TICK = 0x01;
    private static final long INTERVAL_TICK = 1000L;

    private static final long ANIMATION_LENGTH = 350L;

    private final Strings strings = new Strings(NUM_STRINGS);
    private final DecelerateInterpolator interpolator = new DecelerateInterpolator();

    @Override
    protected boolean needsScheduler() {
        return true;
    }

    @Override
    protected void onSetupPaint(final Paint paint) {
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onSizeSet(int width, int height, boolean round) {
        super.onSizeSet(width, height, round);
        strings.setCanvasSize(width, height);
    }

    @Override
    protected void onVisibilityChanged(final boolean visible) {
        super.onVisibilityChanged(visible);
        if (visible && !inAmbientMode) {
            schedule(MSG_TICK, INTERVAL_TICK);
        }
    }

    @Override
    protected void onAmbientModeChanged(final boolean inAmbientMode) {
        super.onAmbientModeChanged(inAmbientMode);
        if (inAmbientMode) {
            cancel(MSG_TICK);
        } else {
            schedule(MSG_TICK, INTERVAL_TICK);
        }
    }

    @Override
    protected void onDraw(final Canvas canvas, final Time time) {
        strings.draw(canvas, paint, !inAmbientMode);
    }

    @Override
    protected void onTimeTick(final Time time) {
        if (inAmbientMode) {
            strings.skipToNextState();
        }
    }

    @Override
    protected void onHandleMessage(final int what) {
        if (MSG_TICK == what) {
            strings.prepareMove();

            setAnimation(new Animation(ANIMATION_LENGTH) {
                @Override
                protected void apply(final float progress) {
                    final float interpolatedProgress = interpolator.getInterpolation(progress);
                    strings.animate(interpolatedProgress);
                }

                @Override
                protected void onFinished() {
                    strings.finishMove();
                }
            });
        }
    }

}
