package home.westering56.ringbear;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.media.AudioManager;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;

public class VolumeInterceptorAccessibilityService extends AccessibilityService {
    private static final String TAG = "VolumeIntAccI11ySvc";
    /** From AudioManager: Adjusting the volume due to a hardware key press. (hidden) */
    private static final int HIDDEN_FLAG_FROM_KEY = 1 << 12;
    /** Tracks the last action we saw of the volume keys we are interested in. a[keycode]=action. */
    private SparseIntArray lastKeyAction;
    private AudioManager audioManager;

    public VolumeInterceptorAccessibilityService() {
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Log.d(TAG, "onServiceConnected");
        audioManager = getSystemService(AudioManager.class);
        lastKeyAction = new SparseIntArray(2);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        Log.e(TAG, "Received unexpected accessibility: event" + event.toString());
    }

    @Override
    public void onInterrupt() {
        Log.d(TAG, "onInterrupt called. No action.");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind: intent="+intent.toString());
        audioManager = null;
        lastKeyAction = null;
        return super.onUnbind(intent);
    }

    @Override
    protected boolean onKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP ||
                event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN) {
            return handleVolUpDownEvent(event);
        }
        return super.onKeyEvent(event);
    }

    private boolean handleVolUpDownEvent(final KeyEvent event) {
        Log.d(TAG, "Vol up/down: event=" + event.toString());
        final int direction;
        if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP) {
            direction = AudioManager.ADJUST_RAISE;
        } else if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN) {
            direction = AudioManager.ADJUST_LOWER;
        } else {
            Log.w(TAG, "Unable handle unexpected non-volume key event: " + event);
            return false; // Allow other handlers to process this event
        }

        if (lastKeyAction.get(event.getKeyCode(), KeyEvent.ACTION_UP) == KeyEvent.ACTION_DOWN) {
            /* take action on this event, as it's either an ACTION_UP (events normally take effect
             * on up), or another in a series of ACTION_DOWN events which indicate key repeats. */
            // A18y service doesn't seem to tell us about press-and-hold for key repeats though.
            adjustVolume(direction);
        }
        lastKeyAction.put(event.getKeyCode(), event.getAction());
        return true; // swallow the event, we handled it
    }

    private void adjustVolume(final int direction) {
        Log.d(TAG, "Adjusting volume: direction=" + direction);
        audioManager.adjustSuggestedStreamVolume(direction, AudioManager.STREAM_RING,
                AudioManager.FLAG_SHOW_UI | HIDDEN_FLAG_FROM_KEY);
    }
}
