package com.example.musicapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

public class CreateAlbum extends AppCompatActivity implements View.OnClickListener {
    private EditText textTitle , textCategory;
    private Button buttonSave;
    private AlbumInfo album;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_album);
        textTitle = findViewById(R.id.text_title);
        textCategory = findViewById(R.id.text_category);
        buttonSave = findViewById(R.id.button_save);
        buttonSave.setOnClickListener(this);
        album = getIntent().getParcelableExtra("album");
        if(album != null) {
            textTitle.setText(album.getTitle());
            textCategory.setText(album.getCategory());
            setTitle("Edit album");
        }else {
            setTitle("Create album");
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.button_save) {
            saveAlbum();
        }
    }

    private void saveAlbum() {
        String name = textTitle.getText().toString();
        String category = textCategory.getText().toString();
        Intent intent = new Intent();
        if(album == null) {
            album = new AlbumInfo(name , category);
        }else {
            album.setTitle(name);
            album.setCategory(category);
        }
        intent.putExtra("album" , album);
        setResult(RESULT_OK , intent);
        finish();
    }
}