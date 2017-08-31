package com.savar_computer.breaker;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.savar_computer.breaker.Classes.Ball;
import com.savar_computer.breaker.Classes.Brick;
import com.savar_computer.breaker.Classes.DrawingView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main extends Activity {

    public enum Status {loosed, paused, shooting, readyToShot}

    public static Status gameStatue;

    public static float startX, startY;
    public static float newStartX = -1, newStartY = -1;

    public static Context context;

    public static List<Ball> balls;
    public static int ballsCount;
    public static List<Brick> bricks;
    public static int bricksCount;
    private static float ballStepX, ballStepY;

    private static int level = 0;

    private static int ScreenW, ScreenH;
    public static int inner_layout_width, inner_layout_height;
    private static float brickW, brickY;
    public static int ball_radius;
    public static int ballDelay = 100;

    public static RelativeLayout main_layout, inner_layout, DrawLine_layout;
    private RelativeLayout score_panel;

    private ImageView backBtn, settingBtn, pasuseBtn;
    private static TextView scoreTextView;

    private static SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        context = this.getApplicationContext();
        PreparingGraphics();

        //------------------------------First Logic
        gameStatue = Status.readyToShot;
        startX = inner_layout_width / 2;
        startY = inner_layout_height;
        //--------------------------------Set Level 1
        Main.level = 1;
        bricksCount = 0;
        ballsCount = 0;
        //Creating Lists
        balls = new ArrayList<>();
        bricks = new ArrayList<>();
        //-------------------------------------addFirstBall and brick
        addNewBall();
        setBallsLocation();
        addNextLevelBricks();
        updateScoreTextView();
        //Reset Graphics
        DrawLine_layout.invalidate();
        main_layout.invalidate();
        inner_layout.invalidate();

        sharedPreferences = getApplicationContext().getSharedPreferences("data", MODE_PRIVATE);

        //-----The code below is to have different delay time in every Fragmentation
        ballDelay = ScreenH / 8;
    }

    //--------------------------------------------------------Graphics
    private void PreparingGraphics() {
        main_layout = (RelativeLayout) findViewById(R.id.main_layout_main);
        main_layout.setBackgroundResource(R.drawable.background);
        inner_layout = (RelativeLayout) findViewById(R.id.main_layout_inner);
        DrawLine_layout = (RelativeLayout) findViewById(R.id.main_layout_DrawLine);

        //The code below was deleted from Version 1.1
        /* main_layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_UP && DrawingView.drawed){
                    Main.releaseBalls(event.getX(), event.getY());
                    DrawingView.drawed=false;
                }
                return false;
            }
        });*/

        //------------------------------Get Screen Size

        //DisplayMetrics displayMetrics = new DisplayMetrics();
        //getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        /* ScreenH = displayMetrics.heightPixels;
        ScreenW = displayMetrics.widthPixels;
        */
        Point size = new Point();
        WindowManager w = getWindowManager();
        w.getDefaultDisplay().getSize(size);
        ScreenW = size.x;
        ScreenH = size.y;

        int margin_left_right = ScreenW / 32;

        //-------------------------------------Calculate Brick  and Ball Sizes
        brickW = (ScreenW - 2 * margin_left_right) / 6;
        brickY = (float) (brickW / 1.6);
        ball_radius = (int) brickY / 3;

        //--------------------------Calculate inner layout and DrawLineLayoutSize and Set Size and Point
        inner_layout_width = (int) brickW * 6;
        inner_layout_height = (int) brickY * 9;
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(inner_layout_width, inner_layout_height);
        inner_layout.setLayoutParams(params);
        DrawLine_layout.setLayoutParams(params);
        inner_layout.setX(margin_left_right);
        inner_layout.setY((float) ((ScreenH - brickY * 9) / 2.6 * 1.6));
        DrawLine_layout.setX(margin_left_right);
        DrawLine_layout.setY((float) ((ScreenH - brickY * 9) / 2.6 * 1.6));

        //Setting of Score Panel
        score_panel = (RelativeLayout) findViewById(R.id.main_score_panel);
        RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(ScreenW - 6 * margin_left_right, (int) ((((ScreenH - brickY * 9) / 2.6 * 1.6)) / 2.5));
        score_panel.setLayoutParams(params2);
        score_panel.setX(margin_left_right * 3);
        score_panel.setY((int) (((ScreenH - brickY * 9) / 2.6 * 1.6)) / 3);

        //Draw Line or Aiming Part
        DrawingView drawingView = new DrawingView(getApplicationContext(), inner_layout_width, inner_layout_height);
        drawingView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        DrawLine_layout.addView(drawingView);

        scoreTextView = (TextView) findViewById(R.id.main_score_score);
    }

    //--------------------------------------------------------------------Logic
    public static void releaseBalls(float x, float y) {
        if (gameStatue == Status.readyToShot) {
            setBallSteps(x, y);
            gameStatue = Status.shooting;
            for (int i = 0; i < ballsCount; i++) {
                balls.get(i).releaseBall(ballStepX, ballStepY, (i + 1) * ballDelay);
                inner_layout.invalidate();
            }
        }
    }

    //---------------------Calculate speed,stepX and stepY
    private static void setBallSteps(float x, float y) {
        float Nesbat = Math.abs(startX - x) / Math.abs(startY - y);
        //-----------------------Code Bellow will produce speed for different Fragmentation
        ballStepY = -1 * ((ScreenH + level) / 100) / (Nesbat + 1);
        if (startX <= x)
            ballStepX = Math.abs(Nesbat * ballStepY);
        else
            ballStepX = Math.abs(Nesbat * ballStepY) * -1;
    }

    public static void nextLevel() {
        Main.level++;
        if (Main.level > (sharedPreferences.getInt("score", 0))) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("score", Main.level);
            editor.commit();
            scoreTextView.setTextColor(Color.argb(100, 0, 0, 255));
        }
        if (gameStatue == Status.shooting) {
            updateScoreTextView();
            if (!(Main.startX == -1 && Main.startY == -1)) {
                Main.startX = Main.newStartX;
                Main.startY = Main.newStartY;
                Main.newStartX = -1;
                Main.newStartY = -1;
            }
            //Brings bricks one level Down
            for (int i = 0; i < bricksCount; i++) {
                if (bricks.get(i).amount <= 0) {
                    bricksRemove(bricks.get(i));
                } else
                    bricks.get(i).nextLevelAnimation();
            }
            //TODO:IN Loose situation
            if (gameStatue == Status.loosed) {
                Toast.makeText(context, "You LOOSED", Toast.LENGTH_LONG).show();
                return;
            }

            addNewBall();
            setBallsLocation();

            addNextLevelBricks();

            gameStatue = Status.readyToShot;
            Main.inner_layout.invalidate();
            inner_layout.invalidate();
            DrawLine_layout.invalidate();
        }
    }

    private static void updateScoreTextView() {
        scoreTextView.setText(level + "");
    }

    public static void setBallsLocation() {
        float x = startX;
        float y = startY;
        for (int i = 0; i < ballsCount; i++) {
            balls.get(i).setX(x - ball_radius / 2);
            balls.get(i).setY(y - ball_radius);
        }
        inner_layout.invalidate();
        DrawLine_layout.invalidate();
    }

    public static void addNewBall() {
        Ball ball = new Ball(context, Main.ball_radius);
        balls.add(ball);
        ballsCount++;
        //ball.setX(startX - ball_radius / 2);
        //ball.setY(startY - ball_radius);
        setBallsLocation();

        Main.inner_layout.addView(ball);
        Main.inner_layout.invalidate();
    }

    //TODO:not complete
    public static void addNextLevelBricks() {
        int newBallC = 0;
        if (Main.level <= 5) {
            newBallC = 1;
        } else if (Main.level <= 10) {
            newBallC = (int) ((Math.random() * 2) + 1);
        } else if (Main.level <= 15) {
            newBallC = (int) ((Math.random() * 3) + 1);
        } else if (Main.level <= 25) {
            newBallC = (int) ((Math.random() * 4) + 1);
        } else if (Main.level <= 30) {
            newBallC = (int) ((Math.random() * 5) + 1);
        } else if (Main.level <= 50) {
            newBallC = (int) ((Math.random() * 6) + 1);
        } else if (Main.level <= 80) {
            newBallC = (int) ((Math.random() * 5) + 2);
        }
        else if (Main.level <= 200) {
            newBallC = (int) ((Math.random() * 4) + 3);
        }
        else
            newBallC = (int) ((Math.random() * 3) + 4);
        if (newBallC > 6)
            newBallC = 6;

        Log.i("MAN", newBallC + "");
        float[] x = new float[newBallC];
        for (int i = 0; i < newBallC; i++) {
            Brick brick = new Brick(context, (int) brickW, (int) brickY, level);
            bricks.add(brick);
            bricksCount++;
            brick.setY(Brick.margin);

            float newX = 0;
            loop:
            while (true) {
                newX = randomXLocationBrick();
                for (int j = 0; j < i; j++) {
                    if (newX == x[j])
                        continue loop;
                }
                break;
            }

            x[i] = newX;
            brick.setX(newX);
            Main.inner_layout.addView(brick);
            Main.inner_layout.invalidate();
        }
    }

    public static float randomXLocationBrick() {
        Random r=new Random();
        return (((int) (r.nextDouble() * 100) % 6) * brickW);
    }

    public static void bricksRemove(Brick brick) {
        bricksCount--;
        inner_layout.removeView(brick);
        bricks.remove(brick);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onResume() {
        onCreate(Bundle.EMPTY);
    }
}