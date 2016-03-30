package com.griffith.horiot.assignment2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;

/**
 * Created by horiot_b_to_chage on 30/03/2016 for Code and Learn
 */
public class MineSweeperView extends View {

    private Paint black, grey, red, green, blue, yellow;
    private int gameSquare[][];
    private int statusSquare[][];
    private int size;
    private RectF rect;
    private boolean isRunning;

    /*
    Legend gameSquare
    0 = void
    1 - 8 = nbr mines proxy
    >= 9 = mine

    Legend statusSquare
    -1 = discovered
    0 = covered
    1 = marked
     */


    // default constructor for the class that takes in a context
    public MineSweeperView(Context c) {
        super(c);
        init();
    }

    // constructor that takes in a context and also a list of attributes
    // that were set through XML
    public MineSweeperView(Context c, AttributeSet as) {
        super(c, as);
        init();
    }

    // constructor that take in a context, attribute set and also a default
    // style in case the view is to be styled in a certian way
    public MineSweeperView(Context c, AttributeSet as, int default_style) {
        super(c, as, default_style);
        init();
    }

    // refactored init method as most of this code is shared by all the
    // constructors
    private void init() {

        black = new Paint();
        grey = new Paint();
        red = new Paint();
        green = new Paint();
        blue = new Paint();
        yellow = new Paint();

        black.setColor(getResources().getColor(R.color.black));
        grey.setColor(getResources().getColor(R.color.grey));
        red.setColor(getResources().getColor(R.color.red));
        green.setColor(getResources().getColor(R.color.green));
        blue.setColor(getResources().getColor(R.color.blue));
        yellow.setColor(getResources().getColor(R.color.yellow));

        gameSquare = new int[10][10];
        statusSquare = new int[10][10];

        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                gameSquare[x][y] = 0;
                statusSquare[x][y] = 0;
            }
        }

        //placing the mines
        int nbrMinesPlaces = 0, x, y;
        Random random = new Random();
        while (nbrMinesPlaces < 20) {
            x = random.nextInt(10);
            y = random.nextInt(10);

            if (gameSquare[x][y] < 9) {
                gameSquare[x][y] = 9;

                if (x > 0) {
                    gameSquare[x - 1][y] += 1;
                    if (y > 0) gameSquare[x - 1][y - 1] += 1;
                    if (y < 9) gameSquare[x - 1][y + 1] += 1;
                }
                if (x < 9) {
                    gameSquare[x + 1][y] += 1;
                    if (y > 0) gameSquare[x + 1][y - 1] += 1;
                    if (y < 9) gameSquare[x + 1][y + 1] += 1;
                }

                if (y > 0) gameSquare[x][y - 1] += 1;
                if (y < 9) gameSquare[x][y + 1] += 1;

                nbrMinesPlaces++;
            }
        }
        isRunning = true;

        rect = new RectF();

    }

    public void resetGame() {
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                gameSquare[x][y] = 0;
                statusSquare[x][y] = 0;
            }
        }

        //placing the mines
        int nbrMinesPlaces = 0, x, y;
        Random random = new Random();
        while (nbrMinesPlaces < 20) {
            x = random.nextInt(10);
            y = random.nextInt(10);

            if (gameSquare[x][y] < 9) {
                gameSquare[x][y] = 9;

                if (x > 0) {
                    gameSquare[x - 1][y] += 1;
                    if (y > 0) gameSquare[x - 1][y - 1] += 1;
                    if (y < 9) gameSquare[x - 1][y + 1] += 1;
                }
                if (x < 9) {
                    gameSquare[x + 1][y] += 1;
                    if (y > 0) gameSquare[x + 1][y - 1] += 1;
                    if (y < 9) gameSquare[x + 1][y + 1] += 1;
                }

                if (y > 0) gameSquare[x][y - 1] += 1;
                if (y < 9) gameSquare[x][y + 1] += 1;

                nbrMinesPlaces++;
            }
        }
        isRunning = true;
        invalidate();
    }

    // public method that needs to be overridden to draw the contents of this
    // widget
    public void onDraw(Canvas canvas) {
        // draw the rest of the squares in green to indicate multitouch
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                rect.set(x * (size / 10),  y * (size / 10),
                        (x + 1) * (size / 10) - 5, (y + 1) * (size / 10) - 5);

                if (statusSquare[x][y] == 0) {
                    canvas.save();
                    canvas.drawRect(rect, black);
                    canvas.restore();

                } else {
                    canvas.save();
                    if (gameSquare[x][y] >= 9) {
                        canvas.drawRect(rect, red);
                        canvas.drawText("M", x * (size / 10) + 5, (y + 1) * (size / 10) - 15, black);
                    } else {
                        canvas.drawRect(rect, grey);
                        if (gameSquare[x][y] == 1) {
                            canvas.drawText("1", x * (size / 10) + 5, (y + 1) * (size / 10) - 15, blue);
                        } else if (gameSquare[x][y] == 2) {
                            canvas.drawText("2", x * (size / 10) + 5, (y + 1) * (size / 10) - 15, green);
                        } else if (gameSquare[x][y] == 3) {
                            canvas.drawText("3", x * (size / 10) + 5, (y + 1) * (size / 10) - 15, yellow);
                        } else if (gameSquare[x][y] > 3) {
                            canvas.drawText(Integer.toString(gameSquare[x][y]), x * (size / 10) + 5, (y + 1) * (size / 10) - 15, red);
                        }
                    }
                    canvas.restore();

                }
            }
        }

        // call the superclass method
        super.onDraw(canvas);

    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        size = width > height ? height : width;
        setMeasuredDimension(size, size);

        black.setTextSize(size / 10);
        red.setTextSize(size / 10);
        green.setTextSize(size / 10);
        blue.setTextSize(size / 10);
        yellow.setTextSize(size / 10);
    }

    public boolean onTouchEvent(MotionEvent event) {
        int x, y;

        if (isRunning) {
            if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                // this indicates that the user has placed the first finger on the
                // screen what we will do here is enable the pointer, track its location
                // and indicate that the user is touching the screen right now
                // we also take a copy of the pointer id as the initial pointer for this
                // touch
                x = (int) event.getX() / (size / 10);
                y = (int) event.getY() / (size / 10);

                statusSquare[x][y] = -1;

                if (gameSquare[x][y] >= 9) isRunning = false;
                invalidate();
                return true;
            }
        }

        return super.onTouchEvent(event);
    }

}
