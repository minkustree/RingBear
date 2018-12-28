package home.westering56.ringbear;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.media.AudioManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;

public class VolumeInterceptorAccessibilityService extends AccessibilityService {
    private static final String TAG = "VolumeIntAccI11ySvc";
    /** From AudioManager: Adjusting the volume due to a hardware key press. (hidden) */
    private static final int HIDDEN_FLAG_FROM_KEY = 1 << 12;
    private AudioManager audioManager;

    public VolumeInterceptorAccessibilityService() {
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Log.d(TAG, "onServiceConnected");
        audioManager = getSystemService(AudioManager.class);
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
        final int keycode = event.getKeyCode();
        final int direction;
        if (keycode == KeyEvent.KEYCODE_VOLUME_UP) {
            direction = AudioManager.ADJUST_RAISE;
        } else if (keycode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            direction = AudioManager.ADJUST_LOWER;
        } else {
            Log.w(TAG, "Unable handle non-volume key event: " + event);
            return false; // Allow other handlers to process this event
        }
        adjustVolume(direction);
        return true; // swallow the event, we handled it
    }

    private void adjustVolume(final int direction) {
        Log.d(TAG, "Adjusting volume: direction=" + direction);
        audioManager.adjustStreamVolume(AudioManager.STREAM_RING, direction,
                AudioManager.FLAG_SHOW_UI | HIDDEN_FLAG_FROM_KEY);
    }
}
