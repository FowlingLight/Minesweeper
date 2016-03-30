package com.griffith.horiot.assignment2;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by horiot_b_to_chage on 30/03/2016 for Code and Learn
 */
public class MineSweeperView extends View {
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
    }

    // public method that needs to be overridden to draw the contents of this
    // widget
    public void onDraw(Canvas canvas) {
        // call the superclass method
        super.onDraw(canvas);
    }

    // public method that needs to be overridden to handle the touches from a
    // user
    public boolean onTouchEvent(MotionEvent event) {
        // if we get to this point they we have not handled the touch
        // ask the system to handle it instead
        return super.onTouchEvent(event);
    }
}
