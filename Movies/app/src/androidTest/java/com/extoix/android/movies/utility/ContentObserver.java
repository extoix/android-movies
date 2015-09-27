package com.extoix.android.movies.utility;

import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;

public class ContentObserver extends android.database.ContentObserver {
    final HandlerThread mHT;
    boolean mContentChanged;

    public static ContentObserver getTestContentObserver() {
        HandlerThread ht = new HandlerThread("ContentObserverThread");
        ht.start();
        return new ContentObserver(ht);
    }

    private ContentObserver(HandlerThread ht) {
        super(new Handler(ht.getLooper()));
        mHT = ht;
    }

    // On earlier versions of Android, this onChange method is called
    @Override
    public void onChange(boolean selfChange) {
        onChange(selfChange, null);
    }

    @Override
    public void onChange(boolean selfChange, Uri uri) {
        mContentChanged = true;
    }

    public void waitForNotificationOrFail() {
        // Note: The PollingCheck class is taken from the Android CTS (Compatibility Test Suite).
        // It's useful to look at the Android CTS source for ideas on how to test your Android
        // applications.  The reason that PollingCheck works is that, by default, the JUnit
        // testing framework is not running on the main Android application thread.
        new PollingCheck(5000) {
            @Override
            protected boolean check() {
                return mContentChanged;
            }
        }.run();
        mHT.quit();
    }
}

