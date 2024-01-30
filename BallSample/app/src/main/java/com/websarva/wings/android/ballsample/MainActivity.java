package com.websarva.wings.android.ballsample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private MyView view;
    private float touch_x;
    private int w, h;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        view = new MyView(this);
        setContentView(view);

        WindowManager wm = (WindowManager)
                getSystemService(WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        w = point.x;
        h = point.y;

        //setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            touch_x = event.getX();
            return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        view.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        view.onPause();
    }

    public class MyView extends View {
        private Paint paint = new Paint();
        //private int x = 0, y = 0,dx = 1, dy = 1;
        private int x, y, px, py, dx = 1, dy = 1, point = 0;
        private ScheduledExecutorService ses = null;
        private ScheduledFuture future = null;
        private Context c;

        public MyView(Context context) {
            super(context);
            paint.setColor(Color.RED);
            c = context;
        }

        private final Runnable task = new Runnable() {
            @Override
            public void run() {
                if (x > w - 200 || x < 0) {
                    dx = -dx;
                }
                if ((y + 200 == py && x + 100 > py && x + 100 < px + 400) || y < 0) {
                //if (y > h - 400 || y < 0) {
                    dy = -dy;
                }

                x += dx;
                y += dy;

                if(touch_x < w /2){
                    px = px -1;
                    if(px < 0){
                        px = 0;
                    }
                }else{
                    px = px + 1;
                    if(px > w - 400){
                        px = w - 400;
                    }
                }

                if(y > h -400){
                    goal();
                }

                postInvalidate();//ビュー更新
            }
        };

        public void onResume() {
            px = w / 2 -200;
            py = h - 350;
            x = w / 2 - 100;
            y = h / 2 - 350;
            //2mm秒ごとにタスクを実行
            ses = Executors.newSingleThreadScheduledExecutor();
            //ses.scheduleAtFixedRate(task, 0, 2000, TimeUnit.MICROSECONDS);
            future = ses.scheduleAtFixedRate(task, 0, 2000, TimeUnit.MICROSECONDS);
        }

        public void onPause() {
            //ses開放
            if (ses != null) {
                ses.shutdown();
                ses = null;
            }
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawCircle(100 + x, 100 + y, 100, paint);
            canvas.drawRect(px, py, px + 400, py + 50, paint);
        }

        private void goal(){
            point++;
            future.cancel(true);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast t = Toast.makeText(c,Integer.toString(point), Toast.LENGTH_SHORT);
                    t.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL,0,0);
                    t.show();
                }
            });
            if(point < 3){
                x = px + 300;
                y = py - 200;
                future = ses.scheduleAtFixedRate(task,2000000,2000,TimeUnit.MICROSECONDS);
                //if (paint == null) {
                //    paint = new Paint();
                //}
            }else{
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(c,"gameover",Toast.LENGTH_LONG).show();
                        point = 0;
                    }
                });
            }

        }

    }
}