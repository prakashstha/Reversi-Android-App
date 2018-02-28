package com.example.godaibo.reversisimulator;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by godaibo on 4/2/2015.
 */
public class PuzzleSlidingPaneLayout  extends android.support.v4.widget.SlidingPaneLayout {

    private boolean swipeEnabled = false;

    public PuzzleSlidingPaneLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PuzzleSlidingPaneLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public PuzzleSlidingPaneLayout(Context context) {
        super(context);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return  super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        if(!  ( (ReversiActivity)getContext()).sPaneLy.isOpen()) {
            getChildAt(1).dispatchTouchEvent(ev);
        }
        else{

            getChildAt(0).dispatchTouchEvent(ev);

        }

        return true;


        // return super.onTouchEvent(ev);
    }
}
