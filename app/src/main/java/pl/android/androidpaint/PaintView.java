package pl.android.androidpaint;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

public class PaintView extends View {

    private Paint paint;
    private Path path;
    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;
    private ArrayList<FigureToDraw> figuresToDraw;
    private ArrayList<FigureToDraw> figuresUndoed;
    private int color = Color.BLACK;
    private int size = 5;

    public PaintView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setFocusable(true);

        path = new Path();

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStyle(Style.STROKE);
        paint.setStrokeJoin(Join.ROUND);
        paint.setStrokeCap(Cap.ROUND);

        figuresToDraw = new ArrayList<FigureToDraw>();
        figuresUndoed = new ArrayList<FigureToDraw>();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        paint.setColor(color);
        paint.setStrokeWidth(size);

        for (FigureToDraw figure : figuresToDraw) {
            canvas.drawPath(figure.getPath(), figure.getPaint());
        }

        canvas.drawPath(path, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                path.reset();
                path.moveTo(x, y);
                path.lineTo(x + 1, y + 1);
                mX = x;
                mY = y;

                break;
            case MotionEvent.ACTION_MOVE:
                float dx = Math.abs(x - mX);
                float dy = Math.abs(y - mY);

                if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
                    path.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
                    mX = x;
                    mY = y;
                }

                break;
            case MotionEvent.ACTION_UP:
                path.lineTo(mX, mY);

                figuresUndoed.clear();
                figuresToDraw.add(new FigureToDraw(new Path(path), new Paint(paint)));

                path.reset();

                break;
        }

        invalidate();

        return true;
    }

    public void clear() {
        figuresUndoed.clear();
        figuresToDraw.clear();
        invalidate();
    }

    public void undo() {
        if (figuresToDraw.size() >= 1) {
            figuresUndoed.add(figuresToDraw.remove(figuresToDraw.size() - 1));
            invalidate();
        }
    }

    public void redo() {
        if (figuresUndoed.size() >= 1) {
            figuresToDraw.add(figuresUndoed.remove(figuresUndoed.size() - 1));
            invalidate();
        }
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setSize(int size) {
        this.size = size;
    }
}