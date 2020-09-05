package com.example.zambimusic.ui.main.Button;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatImageButton;


import com.example.zambimusic.R;
import com.example.zambimusic.data.enums.TWO_STATE;

public class CustomToggleImageButton extends AppCompatImageButton {
    private static final String TAG = "PlayButton";
    private final int MAX_STATES=2;
    int state;
    Drawable srcPlay;
    Drawable srcPause;
    int btn_state;
    Context context;


    public CustomToggleImageButton(Context context) {
        super(context);
        this.context=context;

    }

    public CustomToggleImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.CustomToggleImageButton);

        try {

            btn_state = a
                    .getInteger(R.styleable.CustomToggleImageButton_state, 0);
            srcPlay = a
                    .getDrawable(R.styleable.CustomToggleImageButton_src_state1);
            srcPause = a
                    .getDrawable(R.styleable.CustomToggleImageButton_src_state2);


        } catch (Exception e) {

        } finally {
            a.recycle();
        }

        switch (btn_state) {
            case 0:
                this.setBackground(srcPlay);
                break;
            case 1:
                this.setBackground(srcPause);
                break;

            default:
                this.setBackground(srcPlay);
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
                this.setBackground(srcPlay);
                showButtonText("PLAY");
                break;
            case 1:
                this.setBackground(srcPause);
                showButtonText("PAUSE");
                break;

            default:
                break;

        }
    }
    public void showButtonText(String text) {

        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();

    }
    public TWO_STATE getBtn_state() {

        switch (state) {
            case 0:
                Log.d(TAG, "getRepeatState: case " + 0 );
                return TWO_STATE.STATE1;
            case 1:
                Log.d(TAG, "getRepeatState: case " + 1 );
                return TWO_STATE.STATE2;

            default:
                Log.d(TAG, "getRepeatState: case default"  );
                return TWO_STATE.STATE1;

        }

    }

    public void setBtn_state(TWO_STATE btn_TWO_state) {

        switch (btn_TWO_state) {
            case STATE1:
                state=0;

                break;
            case STATE2:
                state=1;
                break;

            default:
                break;
        }

        setStateBackground();
    }
}

