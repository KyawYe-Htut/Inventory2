package com.example.inventory;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.util.Objects;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        boolean noIntent =  getIntent() == null ;
        if(noIntent){
            Objects.requireNonNull(getSupportActionBar()).hide();
        }
    }
}