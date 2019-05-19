package com.nyderek.dawid.audiometria;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidplot.util.PixelUtils;
import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.androidplot.xy.XYStepMode;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;

public class ExamResult extends AppCompatActivity {

    private Button goToHistory, backToMenu;
    private EditText firstNameText, lastNameText;
    public XYPlot audiogram;
    List<Number> xList = new LinkedList<>();
    List<Integer> yListRight = new ArrayList<>();
    List<Integer> yListLeft = new ArrayList<>();
    DBHelper dbHelper;
    ResultModel resultModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_result);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        goToHistory = (Button) findViewById(R.id.toHistoryBtn);
        backToMenu = (Button) findViewById(R.id.backToMenuBtn);
        audiogram = (XYPlot) findViewById(R.id.audiogram);
        firstNameText = (EditText) findViewById(R.id.firstNameText);
        lastNameText = (EditText) findViewById(R.id.lastNameText);

        yListRight = getIntent().getIntegerArrayListExtra("rightEarList");
        yListLeft = getIntent().getIntegerArrayListExtra("leftEarList");


        dbHelper = new DBHelper(this);

        goToHistory.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!firstNameText.getText().toString().equals("") && !lastNameText.getText().toString().equals("")) {
                    saveResult(firstNameText.getText().toString(), lastNameText.getText().toString());
                    Intent examHistory = new Intent(getApplicationContext(), ExaminationHistory.class);
                    startActivity(examHistory);
                } else {
                    Toast.makeText(ExamResult.this, "Wprowadź imię i nazwisko", Toast.LENGTH_LONG).show();
                }
            }
        });

        backToMenu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!firstNameText.getText().toString().equals("") && !lastNameText.getText().toString().equals("")) {
                    saveResult(firstNameText.getText().toString(), lastNameText.getText().toString());
                    Intent menu = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(menu);
                } else {
                    Toast.makeText(ExamResult.this, "Wprowadź imię i nazwisko", Toast.LENGTH_LONG).show();
                }
            }
        });
        fillFreqList();
        graphSettings();
        prepareAudiogram();

    }

    void fillFreqList() {
        xList.add(250);
        xList.add(500);
        xList.add(1000);
        xList.add(2000);
        xList.add(4000);
        xList.add(6000);
        xList.add(8000);
    }

    void prepareAudiogram() {
        XYSeries rightEar = new SimpleXYSeries(xList, yListRight, "Ucho prawe");

        LineAndPointFormatter series2bFormat = new LineAndPointFormatter(Color.RED, Color.RED, null, null);
        series2bFormat.getVertexPaint().setStrokeWidth(PixelUtils.dpToPix(7));
//        audiogram.addSeries(rightEar, new LineAndPointFormatter(Color.RED, Color.RED, null, null));
        audiogram.addSeries(rightEar, series2bFormat);

        XYSeries leftEar = new SimpleXYSeries(xList, yListLeft, "Ucho lewe");
        audiogram.addSeries(leftEar, new LineAndPointFormatter(Color.BLUE, Color.BLUE, null, null));
    }

    void saveResult(String firstName, String lastName) {
        audiogram.setDrawingCacheEnabled(true);
        int width = audiogram.getWidth();
        int height = audiogram.getHeight();
        audiogram.measure(width, height);
        Bitmap bmp = Bitmap.createBitmap(audiogram.getDrawingCache());
        audiogram.setDrawingCacheEnabled(false);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        resultModel = new ResultModel(firstName, lastName, stream.toByteArray());
        dbHelper.insertData(resultModel);
    }

    void graphSettings() {
        audiogram.setTitle("Audiogram");
        audiogram.setRangeLabel("Amplituda [dB HL]");
        audiogram.setDomainLabel("Częstotliwość [Hz]");

        audiogram.setDomainBoundaries(0, 9000, BoundaryMode.FIXED);
        audiogram.setRangeBoundaries(80, -10, BoundaryMode.FIXED);
        audiogram.setDomainValueFormat(new DecimalFormat("#"));
        audiogram.setRangeValueFormat(new DecimalFormat("#"));
        audiogram.setDomainStep(XYStepMode.INCREMENT_BY_VAL, 1000);
        audiogram.getBackgroundPaint().setColor(Color.WHITE);
        audiogram.getGraphWidget().getBackgroundPaint().setColor(Color.WHITE);
        audiogram.getGraphWidget().getGridBackgroundPaint().setColor(Color.WHITE);
        audiogram.getGraphWidget().getDomainLabelPaint().setColor(Color.BLACK);
        audiogram.getGraphWidget().getRangeLabelPaint().setColor(Color.BLACK);
        audiogram.getGraphWidget().getDomainOriginLabelPaint().setColor(Color.BLACK);
        audiogram.getGraphWidget().getDomainOriginLinePaint().setColor(Color.BLACK);
        audiogram.getGraphWidget().setPaddingTop(8);
        audiogram.getGraphWidget().setPaddingBottom(12);
        audiogram.getGraphWidget().setPaddingLeft(-8);
        audiogram.getGraphWidget().setPaddingRight(18);

        audiogram.getTitleWidget().getLabelPaint().setColor(Color.BLACK);
        audiogram.getTitleWidget().getLabelPaint().setTextSize(15);
        audiogram.getTitleWidget().setHeight(30);
        audiogram.getTitleWidget().setWidth(400);

        audiogram.getDomainLabelWidget().getLabelPaint().setTextSize(13);
        audiogram.getDomainLabelWidget().setWidth(150);
        audiogram.getDomainLabelWidget().setHeight(18);
        audiogram.getDomainLabelWidget().getLabelPaint().setColor(Color.BLACK);

        audiogram.getRangeLabelWidget().getLabelPaint().setTextSize(13);
        audiogram.getRangeLabelWidget().setWidth(23);
        audiogram.getRangeLabelWidget().setHeight(500);
        audiogram.getRangeLabelWidget().getLabelPaint().setColor(Color.BLACK);
    }

    @Override
    public void onBackPressed() {
    }
}