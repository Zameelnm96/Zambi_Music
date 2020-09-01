package com.example.zambimusic.observer;


import android.content.ContentResolver;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

public class UriObserver extends ContentObserver {
    private static final String TAG = "UriObserver";
    OnChangeListener onChangeListener;
    public UriObserver(Handler handler,OnChangeListener onChangeListener) {

        super(handler);
        this.onChangeListener = onChangeListener;
    }

    @Override
    public void onChange(boolean selfChange) {
        this.onChange(selfChange, null);
    }

    @Override
    public void onChange(boolean selfChange, Uri uri) {

        onChangeListener.onChange(uri);
    }
    public interface OnChangeListener
    {
        public void onChange(Uri uri);
    }
    /*public static class InnerUriObserver
    {
        private final Cursor mCursor;
        private final ContentObserver mObserver;
        private boolean mRunning = true;

        private class ObserverWithListener extends ContentObserver
        {
            private final OnChangeListener mListener;
            public ObserverWithListener(OnChangeListener listener)
            {
                super(new Handler());

                mListener = listener;
            }

            @Override
            public void onChange(boolean selfChange)
            {
                if (mRunning)
                {
                    Log.d(TAG, "onChange: Inner");
                    mListener.onChange();
                }
            }
        };

        public static InnerUriObserver getInstance(ContentResolver contentResolver, Uri uri, OnChangeListener listener)
        {
            Cursor c = contentResolver.query(uri, new String[] { "*" }, null, null, null);

            if (!c.moveToFirst())
            {
                Log.d(TAG, "getInstance inner: " + uri);
                return null;
            }

            return new InnerUriObserver(c, listener);
        }

        public InnerUriObserver(Cursor c, final OnChangeListener listener)
        {
            mCursor = c;
            mObserver = new ObserverWithListener(listener);
            mCursor.registerContentObserver(mObserver);
        }

        public void stop()
        {
            mCursor.unregisterContentObserver(mObserver);
            mCursor.close();
            mRunning = false;
        }

        public interface OnChangeListener
        {
            public void onChange();
        }
    }*/
}

