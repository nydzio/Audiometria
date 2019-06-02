package com.nyderek.dawid.audiometria;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class TextLogin extends AppCompatActivity {

    RequestQueue requestQueue;
    EditText loginBox, passwordBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_login);
        requestQueue = Volley.newRequestQueue(this);

        loginBox = (EditText) findViewById(R.id.loginBox);
        passwordBox = (EditText) findViewById(R.id.passwordBox);
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

        if (loginBox.getText() == null || loginBox.getText().toString().equals("")) {
            Toast.makeText(TextLogin.this, "Wpisz login", Toast.LENGTH_LONG).show();
            return;
        }

        String login = loginBox.getText().toString();
        if (passwordBox.getText() == null || passwordBox.getText().toString().equals("")) {
            Toast.makeText(TextLogin.this, "Wpisz hasło", Toast.LENGTH_LONG).show();
            return;
        }

        String password = passwordBox.getText().toString();

        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("username", login);
        requestMap.put("password", password);
        String URL = "https://biometryauth.azurewebsites.net/api/Auth/login";
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, URL, new JSONObject(requestMap), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String token = response.getString("token");
                    if (token != null && !token.isEmpty()) {
                        Toast.makeText(TextLogin.this, "Zalogowano pomyślnie", Toast.LENGTH_LONG).show();
                        loadMainActivity();
                    } else {
                        Toast.makeText(TextLogin.this, "Błąd podczas logowania", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(TextLogin.this, "Błąd podczas logowania", Toast.LENGTH_LONG).show();
            }
        });

        requestQueue.add(objectRequest);
    }

    public void loadMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}