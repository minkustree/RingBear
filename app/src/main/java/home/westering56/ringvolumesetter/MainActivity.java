package home.westering56.ringvolumesetter;

import android.media.AudioManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private AudioManager audioManager;
    private int originalRingVolume;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        audioManager = (AudioManager)getSystemService(AUDIO_SERVICE);
        originalRingVolume = audioManager.getStreamVolume(AudioManager.STREAM_RING);
    }

    public void setRingLevelToOne(View v) {
        audioManager.setStreamVolume(AudioManager.STREAM_RING, 0, 0);
        audioManager.adjustStreamVolume(AudioManager.STREAM_RING, AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
    }

    public void restoreRingLevel(View v) {
        audioManager.setStreamVolume(AudioManager.STREAM_RING, originalRingVolume, AudioManager.FLAG_SHOW_UI);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN || keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            final int direction = keyCode == KeyEvent.KEYCODE_VOLUME_DOWN ? AudioManager.ADJUST_LOWER : AudioManager.ADJUST_RAISE;
            audioManager.adjustStreamVolume(AudioManager.STREAM_RING, direction, AudioManager.FLAG_SHOW_UI);
            return true; // do not propagate event
        }
//        return super.onKeyDown(keyCode, event);
        return false; // propagate event
    }
}
