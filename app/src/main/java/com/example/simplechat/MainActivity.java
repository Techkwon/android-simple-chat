package com.example.simplechat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.simplechat.fragments.SignInFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState == null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(android.R.id.content, new SignInFragment(), this.toString())
                    .commit();
        }

    }//onCreate


}
