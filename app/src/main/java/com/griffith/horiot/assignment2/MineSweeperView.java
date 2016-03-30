package com.griffith.horiot.assignment2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by horiot_b_to_chage on 30/03/2016 for Code and Learn
 */
public class MineSweeperView extends View {

    private Paint black, grey;
    private Rect square; // the square itself
    private int gameSquare[][];
    private int statusSquare[][];
    private int size;

    /*
    Legend gameSquare
    -1 = uncovered
    0 = void
    1 - 9 = nbr mines proxy

    Legend statusSquare
    -1 = mine
    0 = uncovered
    1 = discovered
    2 = marked
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

        // initialise the rectangle
        square = new Rect(-100, -100, 100, 100);

        black = new Paint();
        grey = new Paint();

        black.setColor(getResources().getColor(R.color.black));
        grey.setColor(getResources().getColor(R.color.grey));

        gameSquare = new int[10][10];
        statusSquare = new int[10][10];

        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                gameSquare[x][y] = -1;
                statusSquare[x][y] = 0;
            }
        }

    }

    // public method that needs to be overridden to draw the contents of this
    // widget
    public void onDraw(Canvas canvas) {
        // draw the rest of the squares in green to indicate multitouch
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                if (gameSquare[x][y] == -1) {
                    //draw black box
                    canvas.save();
                    canvas.drawRect(x * (size / 10), y * (size / 10),
                            (x + 1) * (size / 10) - 5, (y + 1) * (size / 10) - 5,
                            black);
                    //canvas.drawRect(square, black);
                    canvas.restore();

                } else if (gameSquare[x][y] == 0) {
                    //draw black box
                    canvas.save();
                    canvas.drawRect(x * (size / 10), y * (size / 10),
                            (x + 1) * (size / 10) - 5, (y + 1) * (size / 10) - 5,
                            grey);
                    //canvas.drawRect(square, black);
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
    }

    public boolean onTouchEvent(MotionEvent event) {

        int x, y;

        if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
            // this indicates that the user has placed the first finger on the
            // screen what we will do here is enable the pointer, track its location
            // and indicate that the user is touching the screen right now
            // we also take a copy of the pointer id as the initial pointer for this
            // touch
            int pointer_id = event.getPointerId(event.getActionIndex());

            x = (int)event.getX() / (size / 10);
            y = (int)event.getY() / (size / 10);

            this.gameSquare[x][y] = 0;

            invalidate();
            return true;
        } else if (event.getActionMasked() == MotionEvent.ACTION_UP) {
            // this indicates that the user has removed the last finger from the
            // screen and has ended all touch events. here we just disable the
            // last touch.
            int pointer_id = event.getPointerId(event.getActionIndex());

            /*x = (int)event.getRawX() / (size / 10);
            y = (int)event.getRawY() / (size / 10);*/

            invalidate();
            return true;
        }

        return super.onTouchEvent(event);
    }

}
