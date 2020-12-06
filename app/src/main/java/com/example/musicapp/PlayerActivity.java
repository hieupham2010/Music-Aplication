package com.example.musicapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class PlayerActivity extends AppCompatActivity
        implements View.OnClickListener
        , SeekBar.OnSeekBarChangeListener, MediaPlayer.OnCompletionListener {
    private TextView textSong,startTime,endTime;
    private SeekBar seekBar;
    private Button buttonPlay_Pause,buttonNext,buttonPrevious,buttonRandom,buttonRepeat;
    private MediaPlayer mediaPlayer;
    private int position;
    private List<File> songFiles;
    private Timer timer;
    private boolean buttonRandomBool = false,buttonRepeatBool = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        textSong = findViewById(R.id.txtSong_Name);
        seekBar = findViewById(R.id.seekBar);
        buttonPlay_Pause = findViewById(R.id.button_Play_Pause);
        buttonNext = findViewById(R.id.button_next);
        buttonPrevious = findViewById(R.id.button_previous);
        buttonRandom = findViewById(R.id.button_random);
        buttonRepeat = findViewById(R.id.button_repeat);
        startTime = findViewById(R.id.startTime);
        endTime = findViewById(R.id.endTime);
        buttonPlay_Pause.setOnClickListener(this);
        buttonPrevious.setOnClickListener(this);
        buttonNext.setOnClickListener(this);
        buttonRandom.setOnClickListener(this);
        buttonRepeat.setOnClickListener(this);
        preparePlayer();
    }

    public void countTime() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if(mediaPlayer.isPlaying()) {
                                startTime.setText(formatTime(mediaPlayer.getCurrentPosition()));
                                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                            }
                            mediaPlayer.setOnCompletionListener(PlayerActivity.this);
                        }catch (Exception e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                });
            }
        },100 , 1000);
    }

    private void preparePlayer() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String songs = intent.getStringExtra("songs");
        position = intent.getIntExtra("position" , 0);
        songFiles = (ArrayList) bundle.getParcelableArrayList("songFiles");
        textSong.setText(songs);
        textSong.setSelected(true);
        Uri uri = Uri.parse(songFiles.get(position).toString());
        mediaPlayer = MediaPlayer.create(this , uri);
        mediaPlayer.start();
        seekBar.setProgress(0);
        int duration = mediaPlayer.getDuration();
        seekBar.setMax(duration);
        countTime();
        endTime.setText(formatTime(duration));
        seekBar.setOnSeekBarChangeListener(this);
    }

    public String formatTime(int time) {
        int min = (int)TimeUnit.MILLISECONDS.toMinutes(time);
        time -= min * 60 * 1000;
        int secs = (int)TimeUnit.MILLISECONDS.toSeconds(time);
        return String.format("%02d:%02d" , min , secs);
    }

    private String getSongName(String song) {
        String[] temp = song.split("-");
        String SongName , SingerName;
        if(temp.length >= 2) {
            SongName = temp[0];
            SingerName = temp[1];
        }else {
            SongName = song.replace(".mp3" , "");
            SingerName = "";
        }
        return SongName + "-" + SingerName;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mediaPlayer.stop();
        mediaPlayer.release();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.button_Play_Pause) {
            playSong();
        }else if(view.getId() == R.id.button_next) {
            nextSong();
        }else if(view.getId() == R.id.button_previous) {
            previousSong();
        }else if(view.getId() == R.id.button_random) {
            if(buttonRandomBool) {
                buttonRandomBool = false;
                buttonRandom.setBackgroundResource(R.drawable.ic_random);
            }else{
                buttonRandomBool = true;
                buttonRandom.setBackgroundResource(R.drawable.ic_random_on);
            }
        }else if(view.getId() == R.id.button_repeat) {
            if(buttonRepeatBool) {
                buttonRepeatBool = false;
                buttonRepeat.setBackgroundResource(R.drawable.ic_repeat);
            }else {
                buttonRepeatBool = true;
                buttonRepeat.setBackgroundResource(R.drawable.ic_repeat_on);
            }
        }
    }

    private void previousSong() {
        mediaPlayer.stop();
        mediaPlayer.release();
        if(buttonRandomBool && !buttonRepeatBool) {
            position = getRandomPosition(songFiles.size());
        }else if(!buttonRandomBool && !buttonRepeatBool) {
            position = (position - 1) < 0 ? (songFiles.size() - 1) : (position - 1);
        }
        Uri uri = Uri.parse(songFiles.get(position).toString());
        mediaPlayer = MediaPlayer.create(getApplicationContext() , uri);
        String songName = getSongName(songFiles.get(position).getName());
        textSong.setText(songName);
        mediaPlayer.start();
        seekBar.setProgress(0);
        int duration = mediaPlayer.getDuration();
        seekBar.setMax(duration);
        endTime.setText(formatTime(duration));
        buttonPlay_Pause.setBackgroundResource(R.drawable.ic_pause);
    }

    private int getRandomPosition(int i) {
        Random random = new Random();
        return random.nextInt(i);
    }

    private void nextSong() {
        mediaPlayer.stop();
        mediaPlayer.release();
        if(buttonRandomBool && !buttonRepeatBool) {
            position = getRandomPosition(songFiles.size());
        }else if(!buttonRandomBool && !buttonRepeatBool) {
            position = (position + 1) % songFiles.size();
        }
        Uri uri = Uri.parse(songFiles.get(position).toString());
        mediaPlayer = MediaPlayer.create(getApplicationContext() , uri);
        String songName = getSongName(songFiles.get(position).getName());
        textSong.setText(songName);
        mediaPlayer.start();
        seekBar.setProgress(0);
        int duration = mediaPlayer.getDuration();
        seekBar.setMax(duration);
        endTime.setText(formatTime(duration));
        buttonPlay_Pause.setBackgroundResource(R.drawable.ic_pause);
    }

    private void playSong() {
        seekBar.setMax(mediaPlayer.getDuration());
        if(mediaPlayer.isPlaying()) {
            buttonPlay_Pause.setBackgroundResource(R.drawable.ic_play);
            mediaPlayer.pause();
        }else {
            buttonPlay_Pause.setBackgroundResource(R.drawable.ic_pause);
            mediaPlayer.start();
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mediaPlayer.seekTo(seekBar.getProgress());
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        mediaPlayer.pause();
        nextSong();
    }
}