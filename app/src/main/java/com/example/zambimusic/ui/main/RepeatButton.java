package com.example.zambimusic.ui.main;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatImageButton;

import android.util.Log;
import android.widget.Toast;

import com.example.zambimusic.R;
import com.example.zambimusic.enums.REPEAT;

public class RepeatButton extends AppCompatImageButton {
    private static final String TAG = "RepeatButton";
    private final int MAX_STATES=3;
    int state;
    Drawable srcRepeatOff;
    Drawable srcRepeatOne;
    Drawable srcRepeatAll;
    int repeatState;
    Context context;


    public RepeatButton(Context context) {
        super(context);
        this.context=context;

    }

    public RepeatButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.RepeatButton);

        try {

            repeatState = a
                    .getInteger(R.styleable.RepeatButton_repeat_state, 0);
            srcRepeatOff = a
                    .getDrawable(R.styleable.RepeatButton_src_repeat_off);
            srcRepeatOne = a
                    .getDrawable(R.styleable.RepeatButton_src_repeat_one);
            srcRepeatAll = a
                    .getDrawable(R.styleable.RepeatButton_src_repeat_all);

        } catch (Exception e) {

        } finally {
            a.recycle();
        }

        switch (repeatState) {
            case 0:
                this.setBackground(srcRepeatOff);
                break;
            case 1:
                this.setBackground(srcRepeatOne);
                break;
            case 2:
                this.setBackground(srcRepeatAll);
                break;
            default:
                break;

        }
    }


    public void nextState() {
        Log.d(TAG, "nextState: called before increment " + state);
        state++;
        Log.d(TAG, "nextState: called after increment " + state);
        if (state == MAX_STATES) {
            state = 0;
        }
        setStateBackground();
    }

    private void setStateBackground() {

        switch (state) {
            case 0:
                this.setBackground(srcRepeatOff);
                showButtonText("REPEAT Off");
                break;
            case 1:
                this.setBackground(srcRepeatOne);
                showButtonText("REPEAT One");
                break;
            case 2:
                this.setBackground(srcRepeatAll);
                showButtonText("REPEAT All");

                break;
            default:
                break;

        }
    }
    public void showButtonText(String text) {

        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();

    }
    public REPEAT getRepeatState() {

        switch (state) {
            case 0:
                Log.d(TAG, "getRepeatState: case " + 0 );
                return REPEAT.NO_REPEAT;
            case 1:
                Log.d(TAG, "getRepeatState: case " + 1 );
                return REPEAT.REPEAT_ONE;
            case 2:
                Log.d(TAG, "getRepeatState: case " + 2 );
                return REPEAT.REPEAT_ALL;
            default:
                Log.d(TAG, "getRepeatState: case default"  );
                return REPEAT.NO_REPEAT;

        }

    }

    public void setRepeatState(REPEAT repeatState) {

        switch (repeatState) {
            case NO_REPEAT:
                state=0;

                break;
            case REPEAT_ONE:
                state=1;
                break;
            case REPEAT_ALL:
                state=2;
                break;
            default:
                break;
        }

        setStateBackground();
    }
}
