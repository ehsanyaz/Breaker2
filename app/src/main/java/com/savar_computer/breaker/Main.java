package com.savar_computer.breaker;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.savar_computer.breaker.Classes.Ball;
import com.savar_computer.breaker.Classes.Brick;
import com.savar_computer.breaker.Classes.DrawingView;

import java.util.ArrayList;
import java.util.List;

//TODO:WE HAVE TO COMPLETE DODE BELOW
public class Main extends Activity {

    public enum Status {loosed, paused, shooting, readyToShot}
    public static Status gameStatue;

    public static float startX, startY;
    public static float newStartX = -1, newStartY = -1;

    public static Context context;

    public static List<Ball> balls;
    public static int ballsCount = 0;
    public static List<Brick> bricks;
    public static int bricksCount = 0;
    private static float ballStepX = -5, ballStepY = -1;

    private static int level = 0;

    public static RelativeLayout main_layout, inner_layout, DrawLine_layout;
    private RelativeLayout score_panel;

    private int ScreenW, ScreenH;
    public static int inner_layout_width, inner_layout_height;
    private static float brickW, brickY;
    public static int ball_radius;
    public static int ballDelay = 100;

    private ImageView backBtn, settingBtn, pasuseBtn;
    private static TextView scoreTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        context = this.getApplicationContext();
        PreparingGraphics();

        //Set Level 1
        level++;
        //Creating Lists
        balls = new ArrayList<>();
        bricks = new ArrayList<>();
        //addFirstBall and brick
        addNewBall();
        addNextLevelBricks();
        inner_layout.invalidate();
    }

    private void PreparingGraphics() {

        main_layout = (RelativeLayout) findViewById(R.id.main_layout_main);
        main_layout.setBackgroundResource(R.drawable.background);
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
        inner_layout = (RelativeLayout) findViewById(R.id.main_layout_inner);
        DrawLine_layout = (RelativeLayout) findViewById(R.id.main_layout_DrawLine);


        //Get Screen Size
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        ScreenH = displayMetrics.heightPixels;
        ScreenW = displayMetrics.widthPixels;
        int margin_left_right = ScreenW / 32;

        //Calculate Brick  and Ball Sizes
        brickW = (ScreenW - 2 * margin_left_right) / 6;
        brickY = (float) (brickW / 1.6);
        ball_radius = (int) brickY / 3;

        //Calculate inner layout and DrawLineLayoutSize and Set Size and Point
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

        gameStatue = Status.readyToShot;
        startX = inner_layout_width / 2;
        startY = inner_layout_height;

        //TODO:not complete
        scoreTextView = (TextView) findViewById(R.id.main_score_score);
    }

    public static void releaseBalls(float x, float y) {
        calculateBallSteps(x, y);
        gameStatue = Status.shooting;
        for (int i = 0; i < ballsCount; i++) {
            balls.get(i).releaseBall(ballStepX, ballStepY, (i + 1) * ballDelay);
        }
    }


    private static void calculateBallSteps(float x, float y) {
        float nesbat = Math.abs(startX - x) / Math.abs(startY - y);
        ballStepY = -1 * 5 / (nesbat + 1);
        if (startX <= x)
            ballStepX = Math.abs(nesbat * ballStepY);
        else
            ballStepX = Math.abs(nesbat * ballStepY) * -1;
    }

    public static void nextLevel() {
        updateScoreTextView();
        if (!(Main.startX == -1 && Main.startY == -1)) {
            Main.startX = Main.newStartX;
            Main.startY = Main.newStartY;
            Main.newStartX = -1;
            Main.newStartY = -1;
        }
        //Remove old bricks
        //Brings bricks one level Down
        for (int i = 0; i < bricksCount; i++) {
            if (bricks.get(i).amount <= 0) {
                bricksRemove(bricks.get(i));
            } else
                bricks.get(i).nextLevelAnimation();
        }


        addNewBall();
        setBallsLocation();


        Main.level++;
        addNextLevelBricks();
        gameStatue = Status.readyToShot;

        //Just for exam

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
    }

    public static void addNewBall() {
        Ball ball = new Ball(context, Main.ball_radius);
        balls.add(ball);
        ballsCount++;
        ball.setX(startX - ball_radius / 2);
        ball.setY(startY - ball_radius);
        Main.inner_layout.addView(ball);
    }

    public static void addNextLevelBricks() {
        Brick brick = new Brick(context, (int) brickW, (int) brickY, level);
        bricks.add(brick);
        bricksCount++;
        brick.setY(Brick.margin);
        brick.setX(randomXLocationBrick());
        Main.inner_layout.addView(brick);

    }

    public static float randomXLocationBrick() {
        return (((int) (Math.random() * 100) % 6) * brickW);
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
}