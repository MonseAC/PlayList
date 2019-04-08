package com.example.playlist;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static java.util.Arrays.asList;

public class MainActivity extends AppCompatActivity {

    MediaPlayer mediaPlayer;
    AudioManager audioManager;
    boolean isActivePlaying = false;

    public void btnState(View view){
        if (isActivePlaying == false){
            playClick(view);
            isActivePlaying = true;
        }
        else{
            pauseClick(view);
            isActivePlaying = false;
        }
    }
    public void pauseClick(View view){
        mediaPlayer.pause();
    }

    public void playClick(View view){
        mediaPlayer.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mediaPlayer = MediaPlayer.create(this, R.raw.who_is_it);

        //Volumen-----------------------------------------------------------------------------------

        audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);

        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);


        SeekBar volumeSeekBar = findViewById(R.id.seekV);

        volumeSeekBar.setMax(maxVolume);
        volumeSeekBar.setProgress(currentVolume);

        volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                Log.d("volume:", Integer.toString(progress));
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        //------------------------------------------------------------------------------------------

        //Lista con las canciones-------------------------------------------------------------------
        ListView listView = findViewById(R.id.list_Canciones);
        final ArrayList<String> elementos = new ArrayList<String>(asList("Ben", "Billie Jean", "Dancing Machine", "Remember the Time",
                "Don't Stop till You Get Enough", "I Just Can´t Stop Loving You", "I Want You Back", "One More Chance", "Pretty Young Thing",
                "Rock With You", "Stranger in Moscow", "The Girl is Mine", "Wanna be Startin' Sometin'", "Who Is It"));

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, elementos);

        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView textview = findViewById(R.id.txtCancion);
                textview.setText(elementos.get(i));
                //Toast.makeText(getApplicationContext(), elementos.get(i), Toast.LENGTH_SHORT).show();
            }
        });
        //------------------------------------------------------------------------------------------

        //Progreso de la cancion--------------------------------------------------------------------
        final SeekBar advanceSeekbar = findViewById(R.id.seekC);
        int duration = mediaPlayer.getDuration();
        int progress = mediaPlayer.getCurrentPosition();
        advanceSeekbar.setMax(duration/1000);
        advanceSeekbar.setProgress(progress/1000);

        advanceSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(mediaPlayer != null && b){
                    mediaPlayer.seekTo(i * 1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if(mediaPlayer != null){
                    int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                    advanceSeekbar.setProgress(mCurrentPosition);
                }
            }
            }, 0, 1000
        );
        //------------------------------------------------------------------------------------------

    }
}
