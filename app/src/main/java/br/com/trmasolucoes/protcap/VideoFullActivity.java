package br.com.trmasolucoes.protcap;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;


public class VideoFullActivity extends Activity {

    private VideoView videoViewFull;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_full);

        final Intent intent = getIntent();
        if (intent != null){
            Bundle bundle = intent.getExtras();
            final Uri srcVideo = Uri.parse(bundle.getString("srcVideo"));
            final int time = bundle.getInt("time");

            if (bundle != null){
                videoViewFull = (VideoView)findViewById(R.id.videoViewFull);

                videoViewFull.setVisibility(View.VISIBLE);
                videoViewFull.setVideoURI(srcVideo);
                //add os controles do video
                videoViewFull.setMediaController(new MediaController(VideoFullActivity.this));
                videoViewFull.seekTo(time);
                videoViewFull.start();

                //Repetir video
                videoViewFull.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        videoViewFull.seekTo(0);
                        videoViewFull.start();
                    }
                });

            }else{
                Intent intent1 = new Intent(VideoFullActivity.this,MainActivity.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent1);
            }
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent(VideoFullActivity.this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
