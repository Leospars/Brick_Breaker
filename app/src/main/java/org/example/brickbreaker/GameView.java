package org.example.brickbreaker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;

import org.example.brickbreaker.activity.Account;
import org.example.brickbreaker.activity.GameOverActivity;
import org.example.brickbreaker.classes.Brick;
import org.example.brickbreaker.classes.Velocity;

import java.util.Random;

public class GameView extends View {

    static int dWidth, dHeight;
    final long UPDATE_MILLIS = 30;

    Context context;
    float ballX, ballY;
    Velocity velocity = new Velocity(30, 30);
    Handler handler;
    Runnable runnable;
    Paint textPaint = new Paint();
    Paint livesPaint = new Paint();
    Paint brickPaint = new Paint();
    Paint loginPaint = new Paint();
    final float TEXT_SIZE = 80;
    MediaPlayer mpHitPaddle, mpMiss, mpBreak, mpGameOver, mpWin;

    Random random;
    float paddleX, paddleY;
    float oldPaddleX, oldX;

    int points = 0;
    public static boolean userSignedIn = false;

    int life = 3;
    Bitmap ball, paddle;
    int ballWidth, ballHeight;
    final int brickRows = 4;
    final int brickColumns = 6;
    Brick[] bricks = new Brick[brickRows * brickColumns];
    int numBricks = 0;
    int destroyedBricks = 0;
    boolean gameOver = false;
    Account account;

    public GameView(Context context, Account account) {
        this(context);
        this.account = account;
    }

    private GameView(Context context) {
        super(context);
        this.context = context;

        // Get display size
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        dHeight = displayMetrics.heightPixels;
        dWidth = displayMetrics.widthPixels;

        //Get resources from res folder
        Resources res = getContext().getResources();
        ball = BitmapFactory.decodeResource(res, R.drawable.ball);
        paddle = BitmapFactory.decodeResource(res, R.drawable.paddle);
        handler = new Handler();
        runnable = this::invalidate;

        //scale ball based and paddle based on block size
        ball = Bitmap.createScaledBitmap(ball, (int) (((dWidth / (float) brickColumns) / 3)), dWidth / 20, false);
        paddle = Bitmap.createScaledBitmap(paddle, dWidth / 6, dWidth / 20, false);

        // Load the sounds
        mpHitPaddle = MediaPlayer.create(context, R.raw.thud);
        mpBreak = MediaPlayer.create(context, R.raw.brickbreak);
        mpMiss = MediaPlayer.create(context, R.raw.swipe_thud);
        mpGameOver = MediaPlayer.create(context, R.raw.game_fail);
        mpWin = MediaPlayer.create(context, R.raw.clapping);

        //Set Colors
        textPaint.setColor(Color.RED);
        textPaint.setTextSize(TEXT_SIZE);
        livesPaint.setColor(Color.GREEN);
        livesPaint.setTextSize(TEXT_SIZE / 1.5f);
        brickPaint.setColor(Color.argb(255, 200, 250, 200));

        //Draw circle for login status
        loginPaint.setStyle(Paint.Style.FILL);
        loginPaint.setColor(Color.GREEN);
        loginPaint.setStrokeWidth(10);

        ballWidth = ball.getWidth();
        ballHeight = ball.getHeight();

        //Position ball and paddle
        random = new Random();
        ballX = random.nextInt(dWidth - ballWidth);
        ballY = dHeight / 3.0f;

        paddleX = dWidth / 2.0f - paddle.getWidth() / 2.0f;
        paddleY = dHeight * 4.0f / 5;

        createBricks();
    }

    private void createBricks() {
        int brickWidth = dWidth / (brickColumns);
        int brickHeight = dHeight / 20;
        numBricks = 0;
        for (int row = 0; row < brickRows; row++) {
            for (int col = 0; col < brickColumns; col++) {
                bricks[numBricks] = new Brick(row, col, brickWidth, brickHeight);
                numBricks++;
            }
        }
    }

    //    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.BLACK);

        // Move the ball / Set Velocities
        ballX += velocity.getX();
        ballY += velocity.getY();

        // Check if ball hits the wall
        if (ballX + ballWidth >= dWidth || ballX <= 0) {
            velocity.setX(-velocity.getX());
        }
        if (ballY <= 0) {
            velocity.setY(-velocity.getY());
        }
        if (ballY + ballHeight >= paddleY && ballY + ballHeight <= paddleY + paddle.getHeight()
                && ballX + ballWidth >= paddleX && ballX <= paddleX + paddle.getWidth()) {
            if (mpHitPaddle != null) {
                mpHitPaddle.start();
            }
            //If ball hits the paddle change Y direction and increase speed by 1
            velocity.setX(Math.min(velocity.getX() + 2, 70));
            velocity.setY(-(Math.min(velocity.getY() + 2, 70)));
        }

        if (ballY >= paddleY + paddle.getHeight()) {
            //If ball is below the paddle play miss sound
            if (mpMiss != null) {
                mpMiss.start();
            }
        }

        if (ballY + ballHeight >= dHeight) {
            //If ball hits the ground
            life--;
            ballX = 1 + random.nextInt(Math.abs(dWidth - ballWidth - 1));
            ballY = dHeight / 3.0f;
            velocity.setX(xVelocity());
            velocity.setY(25);
            if (life == 0) {
                gameOver = true;
                if (mpGameOver != null) {
                    mpGameOver.start();
                }
                launchGameOver();
            }
        }

        // Draw the ball and paddle
        canvas.drawBitmap(ball, ballX, ballY, null);
        canvas.drawBitmap(paddle, paddleX, paddleY, null);

        // Draw the bricks
        for (int i = 0; i < numBricks; i++) {
            Brick brick = bricks[i];
            if (!brick.isDestroyed()) {
                int border = 2;
                int xpos = brick.getColumn() * brick.getWidth() + border,
                        ypos = brick.getRow() * brick.getHeight() + border,
                        right = xpos + brick.getWidth() - border,
                        bottom = ypos + brick.getHeight() - border;
                canvas.drawRect(xpos, ypos, right, bottom, brickPaint);
            }
        }

        canvas.drawText("Points: " + points, 30, TEXT_SIZE, textPaint);
        if (life == 2) {
            livesPaint.setColor(Color.YELLOW);
        } else if (life == 1) {
            livesPaint.setColor(Color.RED);
        }

        canvas.drawRect(dWidth - 220 + (70 * (3 - life)), 30, dWidth - 10, 80, livesPaint);
        canvas.drawText("Lives: " + life, dWidth - 220, 80 + TEXT_SIZE / 1.5f, livesPaint);

        // Draw bricks
        for (int i = 0; i < numBricks; i++) {
            Brick brick = bricks[i];
            if (!brick.isDestroyed()) {
                int brickXpos = brick.getColumn() * brick.getWidth();
                int brickYpos = brick.getRow() * brick.getHeight();

                // Check if ball hits the brick
                boolean ballHitBrickBottom = ((ballX + ballWidth > brickXpos + ballWidth/2.0) && ballX < brickXpos + brick.getWidth() - ballWidth/2.0)
                        && ballY >= brickYpos && ballY <= brickYpos + brick.getHeight();
                boolean ballHitBrickTop = (ballX + ballWidth > brickXpos + ballWidth/2.0 && ballX < brickXpos + brick.getWidth() - ballWidth/2.0)
                        && (ballY + ballHeight > brickYpos && ballY + ballHeight <= brickYpos + brick.getHeight());
                boolean ballHitBrickSide = ((ballX + ballWidth > brickXpos && ballX + ballWidth < brickXpos + 10) ||
                        (ballX <= brickXpos + brick.getHeight() && ballX > brickXpos + brick.getHeight() - 10))
                        && (ballY >= brickYpos && ballY <= brickYpos + brick.getHeight());
                boolean perfectHitBrickEdge = (ballX == brickXpos + brick.getHeight() || ballX + ballWidth == brickXpos)
                        && (ballY == brickYpos || ballY == brickYpos + brick.getHeight());

                boolean ballHitsBrick = ballHitBrickBottom || ballHitBrickTop || ballHitBrickSide;
                if (ballHitsBrick) {
                    System.out.println("Hit bottom: " + ballHitBrickBottom + " Hit top: " + ballHitBrickTop + " Hit side: " + ballHitBrickSide);
                    breakBrick(brick);

                    if(perfectHitBrickEdge){
                        velocity.setX(-velocity.getX());
                        velocity.setY(-velocity.getY());
                        System.out.println("Brick hit perfect edge");
                    } else if (ballHitBrickSide) {
                        velocity.setX(-velocity.getX());
                        System.out.println("Brick hit side");
                    } else {
                        velocity.setY(-velocity.getY());
                        System.out.println("Brick hit top or bottom");
                    }

//                    breakAdjacentBrickIfHit();
                }
            }
        }

        if (destroyedBricks == numBricks) {
            launchGameOver();
        }

        if (!gameOver) {
            handler.postDelayed(runnable, UPDATE_MILLIS);// Makes the game run every UPDATE_MILLIS milliseconds
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchx = event.getX();
        float touchy = event.getY();
        if (touchy >= paddleY) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    oldX = event.getX();
                    oldPaddleX = paddleX;
                    break;
                case MotionEvent.ACTION_MOVE:
                    float shift = oldX - touchx;
                    float newPaddleX = oldPaddleX - shift;

                    if (newPaddleX <= 0) {
                        paddleX = 0;
                    } else if (newPaddleX + paddle.getWidth() >= dWidth) {
                        paddleX = dWidth - paddle.getWidth();
                    } else {
                        paddleX = newPaddleX;
                    }
                    break;
            }
        }
        return true;
    }

    private void launchGameOver() {
        handler.removeCallbacksAndMessages(null);
        Intent intent = new Intent(context, GameOverActivity.class);
        intent.putExtra("org.example.brickbreaker.points", points);
        intent.putExtra("org.example.brickbreaker.gameWon", destroyedBricks == numBricks);
        intent.putExtra("org.example.brickbreaker.account", account);

        context.startActivity(intent);
        ((Activity) context).finish();
    }

    private int xVelocity() {
        int[] values = {-25, 25};
        return values[random.nextInt(2)];
    }

    private void breakBrick(Brick brick) {
        brick.setDestroyed(true);
        points += 10;
        destroyedBricks++;

        if (mpBreak != null) {
            mpBreak.start();
        }
        if (destroyedBricks == numBricks) {
            if (mpWin != null) {
                mpWin.start();
            }
            launchGameOver();
        }
    }
}
