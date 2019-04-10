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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static java.lang.String.valueOf;
import static java.util.Arrays.asList;

public class MainActivity extends AppCompatActivity {

    MediaPlayer mediaPlayer;
    AudioManager audioManager;
    //boolean isActivePlaying = false;
    int canciones[];
    int indice = 0;

    public void btnState(View view){
        if (mediaPlayer.isPlaying() == false){
            playClick(view);
            //isActivePlaying = true;
        }
        else{
            pauseClick(view);
            //isActivePlaying = false;
        }
    }
    public void pauseClick(View view){
        mediaPlayer.pause();
    }

    public void playClick(View view){

        mediaPlayer.start();
    }

    public void cargarC() {
        Field[] fields = R.raw.class.getFields();
        int[] resArray = new int[fields.length];

        for (int i = 0; i < fields.length; i++) {
            try {
                resArray[i] = fields[i].getInt(null);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        canciones = resArray;
    }

    public void repCanciones(int idC){
        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
            mediaPlayer.release();

        }
        mediaPlayer = MediaPlayer.create(this, canciones[idC]);
        mediaPlayer.start();
        final SeekBar advanceSeekbar = findViewById(R.id.seekC);
        int duration = mediaPlayer.getDuration();
        int progress = mediaPlayer.getCurrentPosition();
        advanceSeekbar.setMax(duration/1000);
        advanceSeekbar.setProgress(progress/1000);
    }

    public void adelante(View view){
        mediaPlayer.pause();
        indice +=1;
        System.out.println(indice);
        if (indice >= canciones.length){
            indice = 0;
            repCanciones(indice);

        }
        else {
            repCanciones(indice);
        }
        Toast.makeText(getApplicationContext(),String.valueOf(indice), Toast.LENGTH_SHORT).show();
    }

    public void atras(View view){
        mediaPlayer.stop();
        indice -=1;
        System.out.println(indice);
        if (indice < 0){
            indice = 0;
            repCanciones(indice);

        }
        else {
            repCanciones(indice);
        }
        Toast.makeText(getApplicationContext(),String.valueOf(indice), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView textview = findViewById(R.id.txtCancion);

        cargarC();
        mediaPlayer = MediaPlayer.create(this, canciones[0]);

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
        textview.setText(elementos.get(0));
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, elementos);

        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                textview.setText(elementos.get(i));
                indice = i;
                if (mediaPlayer.isPlaying()){
                    //isActivePlaying = false;
                    mediaPlayer.pause();
                    //mediaPlayer.release();
                    repCanciones(i);
                    //textview.setText(elementos.get(indice));

                }
                else{

                    //textview.setText(elementos.get(i));
                    //isActivePlaying = true;
                    repCanciones(i);

                }
                Toast.makeText(getApplicationContext(),String.valueOf(indice), Toast.LENGTH_SHORT).show();
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
