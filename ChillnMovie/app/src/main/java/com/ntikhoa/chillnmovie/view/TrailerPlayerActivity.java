package com.ntikhoa.chillnmovie.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PersistableBundle;

import com.ntikhoa.chillnmovie.R;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerFullScreenListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerTracker;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class TrailerPlayerActivity extends AppCompatActivity {
    public static final String EXTRA_TRAILER_KEY = "trailer key";
    private static final String SAVED_START_TIME = "start time";
    private static final String SAVED_FIRST_LOAD = "first load";

    private YouTubePlayerView trailerView;

    private String videoId;
    private float startTime = 0f;
    private boolean firstLoad = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trailer_player);

        trailerView = findViewById(R.id.trailerPlayer);
        getLifecycle().addObserver(trailerView);

        videoId = getIntent().getStringExtra(EXTRA_TRAILER_KEY);
        if (savedInstanceState != null) {
            startTime = savedInstanceState.getFloat(SAVED_START_TIME);
            firstLoad = savedInstanceState.getBoolean(SAVED_FIRST_LOAD);
        }

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            trailerView.enterFullScreen();
        } else trailerView.exitFullScreen();

        YouTubePlayerTracker tracker = new YouTubePlayerTracker();
        trailerView.getYouTubePlayerWhenReady(new YouTubePlayerCallback() {
            @Override
            public void onYouTubePlayer(YouTubePlayer youTubePlayer) {
                if (firstLoad) {
                    youTubePlayer.cueVideo(videoId, startTime);
                } else youTubePlayer.loadVideo(videoId, startTime);
                youTubePlayer.addListener(tracker);
            }
        });

        trailerView.addFullScreenListener(new YouTubePlayerFullScreenListener() {
            @Override
            public void onYouTubePlayerEnterFullScreen() {
                firstLoad = false;
                startTime = tracker.getCurrentSecond();
                trailerView.enterFullScreen();
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }

            @Override
            public void onYouTubePlayerExitFullScreen() {
                firstLoad = false;
                startTime = tracker.getCurrentSecond();
                trailerView.exitFullScreen();
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_FIRST_LOAD, firstLoad);
        outState.putFloat(SAVED_START_TIME, startTime);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        trailerView.release();
    }
}