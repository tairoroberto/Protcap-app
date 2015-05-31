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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_video_full, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
