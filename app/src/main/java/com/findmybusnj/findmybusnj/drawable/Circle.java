package com.findmybusnj.findmybusnj.drawable;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

/**
 * Created by davidaghassi on 10/20/16.
 */

/**
 * View that will render a circle given when used in tandem with the proper XML fragment
 */
public class Circle extends View {
    private Paint drawPaint;

    public Circle(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFocusable(false);
        setFocusableInTouchMode(false);
        setupPaint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle(140, 140, 130, drawPaint);
    }

    private void setupPaint() {
        drawPaint = new Paint();
        drawPaint.setColor(Color.GRAY);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
    }
}
