package pl.android.androidpaint;

import android.graphics.Path;
import android.graphics.Paint;

public class FigureToDraw {
    private Paint paint;
    private Path path;

    public FigureToDraw(Path path, Paint paint) {
        this.path = path;
        this.paint = paint;
    }

    public Path getPath() {
        return path;
    }

    public Paint getPaint() {
        return paint;
    }
}