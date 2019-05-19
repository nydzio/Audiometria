package com.nyderek.dawid.audiometria;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Charsets.UTF_8;

public class ExaminationHistory extends AppCompatActivity {

    private ListView listView;
    DBHelper dbHelper = new DBHelper(this);
    private List<ResultModel> allResultData = null;
    private List<String> dateList = new ArrayList<>();
    private List<String> firstNameList = new ArrayList<>();
    private List<String> lastNameList = new ArrayList<>();
    private List<String> hashedFirstNameList = new ArrayList<>();
    private List<String> hashedLastNameList = new ArrayList<>();
    private ImageView imageView;
    private Bitmap bmp;
    private byte[] currentImage;
    private boolean showData = false;
    private boolean firstChange = true;
    String[] date = null;
    String[] firstName = null;
    String[] lastName = null;
    String[] hashedFirstName = null;
    String[] hashedLastName = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_examination_history);
        imageView = (ImageView) findViewById(R.id.imageView);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        allResultData = dbHelper.selectData();

        for(ResultModel result : allResultData) {
            dateList.add(result.getDate());
            firstNameList.add(result.getFirstName());
            lastNameList.add(result.getLastName());
            hashedFirstNameList.add(Hashing.sha256().hashString(result.getFirstName(), UTF_8).toString());
            hashedLastNameList.add(Hashing.sha256().hashString(result.getLastName(), UTF_8).toString());
        }
        date = dateList.toArray(new String[dateList.size()]);
        firstName = firstNameList.toArray(new String[firstNameList.size()]);
        lastName = lastNameList.toArray(new String[lastNameList.size()]);
        hashedFirstName = hashedFirstNameList.toArray(new String[hashedFirstNameList.size()]);
        hashedLastName = hashedLastNameList.toArray(new String[hashedLastNameList.size()]);

        NoImageAdapter adapter = new NoImageAdapter(this, date, hashedFirstName, hashedLastName);
        listView = (ListView) findViewById(R.id.listViewHistory);
        listView.setAdapter(adapter);
        listView.setOverscrollFooter(new ColorDrawable(Color.TRANSPARENT));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                currentImage = allResultData.get(position).getAudiogram();
                bmp = BitmapFactory.decodeByteArray(currentImage, 0, currentImage.length);
                imageView.setImageBitmap(bmp);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.itemChangeDisplay: {
                NoImageAdapter adapter;
                if (firstChange) {
                    adapter = new NoImageAdapter(this, date, firstName, lastName);
                    firstChange = false;
                } else {
                    if (showData) {
                        adapter = new NoImageAdapter(this, date, firstName, lastName);
                        showData = false;
                    } else {
                        adapter = new NoImageAdapter(this, date, hashedFirstName, hashedLastName);
                        showData = true;
                    }
                }

                listView = (ListView) findViewById(R.id.listViewHistory);
                listView.setAdapter(adapter);
                listView.setOverscrollFooter(new ColorDrawable(Color.TRANSPARENT));
            }
        }
        return true;
    }
}
