package com.nyderek.dawid.audiometria;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    public void showTextLoginActivity (View view){
        Intent intent = new Intent(this, TextLogin.class);
        startActivity(intent);

    }
}
