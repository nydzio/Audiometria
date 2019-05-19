package com.nyderek.dawid.audiometria;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TextLogin extends AppCompatActivity {

    private Button login, fingerPrint, register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_login);
        login = (Button) findViewById(R.id.loginBtn);
        fingerPrint = (Button) findViewById(R.id.fingerPrintBtn);
        register = (Button) findViewById(R.id.registerBtn);
    }

    public void showRegisterActivity(View view) {
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }

    public void showFingerLoginActivity(View view){
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }

    public void showMainActivity(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
