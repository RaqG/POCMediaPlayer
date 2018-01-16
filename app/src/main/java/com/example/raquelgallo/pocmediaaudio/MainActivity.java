package com.example.raquelgallo.pocmediaaudio;

import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    MediaPlayer mediaPlayer;
    float volume = 0.05f;

    @BindView(R.id.parent_view)
    public LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mediaPlayer = MediaPlayer.create(this, R.raw.revenge_spoon);

        if(savedInstanceState != null){
            mediaPlayer = MediaPlayer.create(this, R.raw.revenge_spoon);
            volume = savedInstanceState.getFloat("volume");
            mediaPlayer.seekTo(savedInstanceState.getInt("songPosition"));
            startAudio();
        }

        mediaPlayer.setVolume(volume, volume);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        pauseAudio();
        outState.putFloat("volume", volume);
        if(mediaPlayer == null)
            outState.putInt("songPosition", 0);
        else
            outState.putInt("songPosition", mediaPlayer.getCurrentPosition());
    }

    @OnClick(R.id.start_btn)
    public void startAudio() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, R.raw.revenge_spoon);
            mediaPlayer.setVolume(volume, volume);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mediaPlayer.setAudioAttributes(new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build());
        }
        mediaPlayer.start();
        audioEnded();

    }

    private void audioEnded() {
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                stopAudio();
            }
        });
    }

    @OnClick(R.id.pause_btn)
    public void pauseAudio() {
        if (mediaPlayer != null)
            mediaPlayer.pause();
    }

    @OnClick(R.id.stop_btn)
    public void stopAudio() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        volume = 0.05f;
    }

    @OnClick(R.id.up_volume_btn)
    public void upVolume() {
        if (mediaPlayer != null) {
            if (volume <= 1.0f) {
                volume += 0.01f;
                mediaPlayer.setVolume(volume, volume);
            } else {
                String volumeMsg = this.getResources().getString(R.string.max_volume);
                Snackbar.make(linearLayout, volumeMsg, Snackbar.LENGTH_LONG).show();
            }
        }
    }

    @OnClick(R.id.down_volume_btn)
    public void downVolume() {
        if (mediaPlayer != null) {
            if (volume >= 0.0f) {
                volume -= 0.01f;
                mediaPlayer.setVolume(volume, volume);
            } else {
                String volumeMsg = this.getResources().getString(R.string.min_volume);
                Snackbar.make(linearLayout, volumeMsg, Snackbar.LENGTH_LONG).show();
            }
        }
    }
}
