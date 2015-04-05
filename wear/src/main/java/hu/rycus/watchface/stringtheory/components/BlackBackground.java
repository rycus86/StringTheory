package hu.rycus.watchface.stringtheory.components;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.format.Time;

import hu.rycus.watchface.commons.Component;

public class BlackBackground extends Component {

    @Override
    protected void onSetupPaint(final Paint paint) {
        paint.setColor(Color.BLACK);
    }

    @Override
    protected void onDraw(final Canvas canvas, final Time time) {
        canvas.drawPaint(paint);
    }

}
