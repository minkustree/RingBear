package home.westering56.ringvolumesetter;

import android.accessibilityservice.AccessibilityService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;

public class VolumeInterceptorAccessibilityService extends AccessibilityService {
    public static final String TAG = "VolumeIntAccI11ySvc";

    public VolumeInterceptorAccessibilityService() {
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        // TODO: Determine if any initialization is needed, do it here if it is.
        Log.d(TAG, "OnServiceConnected");
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        Log.d(TAG, "Received accessibility event" + event.toString());
    }

    @Override
    public void onInterrupt() {
        Log.d(TAG, "onInterrupt called");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        // TODO: Determine if any teardown is needed, do it here if it is.
        Log.d(TAG, "onUnbind: Intent="+intent.toString());
        return super.onUnbind(intent);
    }

    @Override
    protected boolean onKeyEvent(KeyEvent event) {
        Log.d(TAG, "Key event" + event.toString());
        return super.onKeyEvent(event);
    }
}
