package com.nyderek.dawid.audiometria;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;


public class MainActivity extends AppCompatActivity {

    ListView listView;
    String[] choices = {"Nowe badanie", "Historia badań"};
    String[] descriptions = {"Kliknij, aby wybrać rodzaj badania", "Kliknij, aby uzyskać dostęp do przeprowadzonych badań"};
    Integer[] imgID = {R.mipmap.lettern, R.mipmap.historia};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CustomAdapter adapter = new CustomAdapter(this, choices, descriptions, imgID);
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setOverscrollFooter(new ColorDrawable(Color.TRANSPARENT));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch(position)
                {
                    case 0:
                        Intent examinationChoice = new Intent(getApplicationContext(), ExaminationChoice.class);
                        startActivity(examinationChoice);
                        break;
                    case 1:
                        Intent examinationHistory = new Intent(getApplicationContext(), ExaminationHistory.class);
                        startActivity(examinationHistory);
                        break;
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
    }
}