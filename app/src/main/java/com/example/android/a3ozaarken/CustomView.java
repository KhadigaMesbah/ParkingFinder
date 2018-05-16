package com.example.android.a3ozaarken;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

//import com.example.android.a3ozaarken.MainActivity;
//import com.example.android.a3ozaarken.ParkInfo;


public class CustomView extends View {
    private Paint red_paint,green_paint;
    private Rect rec_frame_red ;
    private Rect rec_frame_green;
    ArrayList<String[]> park_data;

    public CustomView(Context context) {
        super(context);
        init(null);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

//    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//        init(attrs);
//    }


    private void init (@Nullable AttributeSet set){
        setBackgroundResource(R.drawable.lot2);
        red_paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        green_paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        rec_frame_green = new Rect();
        rec_frame_red = new Rect();
    }


    public void setValue(ArrayList<String[]> park_data){
        this.park_data=park_data;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        red_paint.setColor(Color.RED);
        red_paint.setStyle(Paint.Style.FILL);
        green_paint.setColor(Color.GREEN);
        green_paint.setStyle(Paint.Style.FILL);
        if(park_data!=null && park_data.size()>0) {
            if (park_data.get(0)[1].equals("1")) {
//                rec_frame_red.set(370, 225, 490, 575);
                rec_frame_red.set(230, 150, 280, 400);
                canvas.drawRect(rec_frame_red, red_paint);
            }
        }
    }


}