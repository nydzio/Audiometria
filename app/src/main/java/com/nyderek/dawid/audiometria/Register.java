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

public class Register extends AppCompatActivity {

    RequestQueue requestQueue;
    EditText loginRegisterBox, passwordRegisterBox, confirmPasswordBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        requestQueue = Volley.newRequestQueue(this);
        loginRegisterBox = (EditText) findViewById(R.id.loginRegisterBox);
        passwordRegisterBox = (EditText) findViewById(R.id.passwordRegisterBox);
        confirmPasswordBox = (EditText) findViewById(R.id.passwordBox);
    }

    public void showTextLoginActivity (View view){
        Intent intent = new Intent(this, TextLogin.class);
        startActivity(intent);
    }

    public void registerNewUser (View view) {
        if (loginRegisterBox.getText() == null || loginRegisterBox.getText().toString().equals("")) {
            Toast.makeText(Register.this, "Wpisz login", Toast.LENGTH_LONG).show();
            return;
        }

        String login = loginRegisterBox.getText().toString();
        if (login.contains(" ")) {
            Toast.makeText(Register.this, "Spacje nie są dozwolone w loginie", Toast.LENGTH_LONG).show();
            return;
        }
        if (login.length() < 6) {
            Toast.makeText(Register.this, "Login musi mieć conajmniej 6 znaków", Toast.LENGTH_LONG).show();
            return;
        }

        if (passwordRegisterBox.getText() == null || passwordRegisterBox.getText().toString().equals("")) {
            Toast.makeText(Register.this, "Wpisz hasło", Toast.LENGTH_LONG).show();
            return;
        }

        String password = passwordRegisterBox.getText().toString();
        if (password.contains(" ")) {
            Toast.makeText(Register.this, "Spacje nie są dozwolone w haśle", Toast.LENGTH_LONG).show();
            return;
        }
        if (password.length() < 6) {
            Toast.makeText(Register.this, "Hasło musi mieć conajmniej 6 znaków", Toast.LENGTH_LONG).show();
            return;
        }

        if (confirmPasswordBox.getText() == null || confirmPasswordBox.getText().toString().equals("")) {
            Toast.makeText(Register.this, "Potwierdź hasło", Toast.LENGTH_LONG).show();
            return;
        }

        String confirmPassword = passwordRegisterBox.getText().toString();
        if (!confirmPassword.equals(password)) {
            Toast.makeText(Register.this, "Hasła nie są takie same", Toast.LENGTH_LONG).show();
            return;
        }

        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("username", login);
        requestMap.put("password", password);

        String URL = "https://biometryauth.azurewebsites.net/api/User/add";
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, URL, new JSONObject(requestMap), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(Register.this, "Zarejestrowano pomyślnie", Toast.LENGTH_LONG).show();
                loadLoginActivity();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Register.this, "Błąd podczas rejestracji", Toast.LENGTH_LONG).show();
            }
        });

        requestQueue.add(objectRequest);
    }

    public void loadLoginActivity() {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }
}