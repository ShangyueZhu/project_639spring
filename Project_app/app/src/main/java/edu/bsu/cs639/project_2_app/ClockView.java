package edu.bsu.cs639.project_2_app;

/**
 * Created by Brandon on 12/10/2016.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Calendar;

public class ClockView extends View {

    public int width = 1000;
    public int height = 1500;
    public float clock_radius = 450;

    private Bitmap mBitmap;
//    private Canvas mCanvas;
    private Path cPath;

    Context context;
    private Paint cPaint;

    private ArrayList<Path> pointPaths;
    private ArrayList<Paint> pointPaints;


    public ClockView(Context c, AttributeSet attrs) {
        super(c, attrs);
        context = c;

        cPath = new Path();

        cPaint = new Paint();
        cPaint.setAntiAlias(true);
        cPaint.setColor(Color.BLACK);
        cPaint.setStyle(Paint.Style.STROKE);
        cPaint.setStrokeJoin(Paint.Join.ROUND);
        cPaint.setStrokeWidth(4f);

        pointPaths = new ArrayList<Path>();
        pointPaints = new ArrayList<Paint>();

        drawClock();

    }


    protected void drawClock(){
        // draw clock face
        cPath.addCircle(width/2 ,height/2 , 25, Path.Direction.CW);
        cPath.addCircle(width/2 ,height/2 , clock_radius, Path.Direction.CW);
        cPath.addCircle(width/2 ,height/2 , clock_radius+10, Path.Direction.CW);

        // draw tick marks
        for(double angle = 0; angle < Math.toRadians(360); angle += Math.toRadians(15)){
            float fromX = width/2 + (float)(Math.sin(angle)*clock_radius*3/4);
            float fromY = height/2 - (float)(Math.cos(angle)*clock_radius*3/4) ;

            float xOffset = (float) Math.sin(angle)*clock_radius/8;
            float yOffset = (float) -Math.cos(angle)*clock_radius/8;

            cPath.moveTo(fromX, fromY);
            cPath.rLineTo(xOffset, yOffset);
        }

        // draw current time indicator
        Calendar c = Calendar.getInstance();
        float time_angle = (float)(
                c.get(Calendar.HOUR_OF_DAY)/24.0*360
                +c.get(Calendar.MINUTE)/60.0/24*360
        );
        cPath.moveTo(width/2, height/2);
        cPath.rLineTo(
                (float)(Math.sin(Math.toRadians(time_angle))*clock_radius*11/16)
                ,-(float)(Math.cos(Math.toRadians(time_angle))*clock_radius*11/16)
        );

        invalidate();

    }


    protected void markInterval(int fHr, int fMin, int tHr, int tMin, int color){

        Path newPath = new Path();
        Paint newPaint = new Paint();
        newPaint.setAntiAlias(true);
        newPaint.setColor(color);
        newPaint.setStyle(Paint.Style.STROKE);
        newPaint.setStrokeJoin(Paint.Join.ROUND);
        newPaint.setStrokeWidth(4f);

        pointPaths.add(newPath);
        pointPaints.add(newPaint);

        // draw the arc
        float radius = (float) (clock_radius*15.0/16);
        float stAngle = (float)(fHr/24.0*360 + fMin/60.0/24*360 - 90);
        float endAngle = (float)(tHr/24.0*360 + tMin/60.0/24*360 - 90);
        float sweepAngle = endAngle-stAngle;

        final RectF circle = new RectF();
        circle.set(width/2 - radius, height/2 - radius, width/2 + radius, height/2 + radius);
        newPath.arcTo(circle, stAngle, sweepAngle, true);


        // draw the endpoints
        markTime(fHr, fMin, color);
        markTime(tHr, tMin, color);


        invalidate();
    }

    protected void markTime(int hr, int min, int color){

        Path newPath = new Path();
        Paint newPaint = new Paint();
        newPaint.setAntiAlias(true);
        newPaint.setColor(color);
        newPaint.setStyle(Paint.Style.STROKE);
        newPaint.setStrokeJoin(Paint.Join.ROUND);
        newPaint.setStrokeWidth(10f);

        pointPaths.add(newPath);
        pointPaints.add(newPaint);

        float time_angle = (float)(hr/24.0*360 + min/60.0/24*360);

        float x = (float)(width/2.0 + Math.sin(Math.toRadians(time_angle))*clock_radius*15/16);
        float y = (float)(height/2.0 - Math.cos(Math.toRadians(time_angle))*clock_radius*15/16);

        newPath.addCircle(x, y, 4, Path.Direction.CW);

        invalidate();

    }


    public void clearCanvas() {
        cPath.reset();
        pointPaths = new ArrayList<Path>();
        pointPaints = new ArrayList<Paint>();
        invalidate();
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh){
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        clock_radius = (float)Math.min(height*.45, width*.45);

        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
//        mCanvas = new Canvas(mBitmap);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(cPath, cPaint);
        for(int i = 0; i < pointPaths.size(); i++) {
            canvas.drawPath(pointPaths.get(i), pointPaints.get(i));
        }
        
    }

}
