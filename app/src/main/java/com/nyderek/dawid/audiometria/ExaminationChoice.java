package com.nyderek.dawid.audiometria;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class ExaminationChoice extends AppCompatActivity {

    ListView listView;
    String[] choices = {"Audiometria tonalna", "Audiometria mowy"};
    String[] descriptions = {"Kliknij, aby rozpocząć nowe badanie audiometrii tonalnej", "Kliknij, aby rozpocząć nowe badanie audiometrii mowy"};
    Integer[] imgID = {R.mipmap.ton, R.mipmap.usta};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_examination_choice);

        CustomAdapter adapter = new CustomAdapter(this, choices, descriptions, imgID);
        listView = (ListView) findViewById(R.id.listViewExam);
        listView.setAdapter(adapter);
        listView.setOverscrollFooter(new ColorDrawable(Color.TRANSPARENT));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch(position)
                {
                    case 0:
                        try {
                            Intent toneExam = new Intent(getApplicationContext(), ToneExam.class);
                            startActivity(toneExam);
                        } catch (Exception e) {
                            Toast.makeText(ExaminationChoice.this, "Nastąpił nieoczekiwany błąd. Proszę spróbować ponownie", Toast.LENGTH_LONG).show();
                        }
                        break;
                    case 1:
                        Toast.makeText(ExaminationChoice.this, "Dostępne wkrótce", Toast.LENGTH_LONG).show();
//                        try {
//                            Intent speechExam = new Intent(getApplicationContext(), SpeechExam.class);
//                            startActivity(speechExam);
//                        } catch (Exception e) {
//                            Toast.makeText(ExaminationChoice.this, "Nastąpił nieoczekiwany błąd. Proszę spróbować ponownie", Toast.LENGTH_LONG).show();
//                        }
                        break;
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        try {
            Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(mainActivity);
        } catch (Exception e) {
            Toast.makeText(ExaminationChoice.this, "Nastąpił nieoczekiwany błąd. Proszę spróbować ponownie", Toast.LENGTH_LONG).show();
        }
    }
}
