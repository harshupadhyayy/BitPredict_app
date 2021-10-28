package com.kekguy.bitcoinprice;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.DefaultAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationBarMenu;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;

@SuppressLint({"SimpleDateFormat", "SetTextI18n", "ResourceAsColor"})
public class MainActivity extends AppCompatActivity {
    private TextView currentPrice;
    private TextView lastUpdatedText;
    private TextView currentPorfolio;
    private TextView moneyText;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference bitcoinRef = db.collection("bitsmaptrimmed");
    private LineChart mChart;
    private NavigationView navigationView;
    private TextView predictedPrice;

    DBHandler dbHandler = new DBHandler(MainActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_activity_main);
        currentPrice = findViewById(R.id.currentPrice);
        lastUpdatedText = findViewById(R.id.lastUpdatedText);
        currentPorfolio = findViewById(R.id.currentPortfolio);
        moneyText = findViewById(R.id.moneyText);
        mChart = findViewById(R.id.lineChart);
        predictedPrice = findViewById(R.id.predictedPrice);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_home) {
                    DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                if (item.getItemId() == R.id.nav_gallery) {
                    Intent intent = new Intent(MainActivity.this, NewsActivity.class);
                    startActivity(intent);
                }
                if (item.getItemId() == R.id.nav_portfolio) {
                    Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                    startActivity(intent);
                }
                return true;
            }
        });
        initChart();
        setInitGraphValues();
        loadPrice();
        handler.postDelayed(runnable, 10000);
    }

    private Thread thread;

    @Override
    protected void onPause() {
        super.onPause();

        if (thread != null) {
            thread.interrupt();
        }
    }

    private final Handler handler = new Handler();
    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            loadPrice();
            handler.postDelayed(this, 10000);
        }
    };


    private void initChart() {
        mChart.getDescription().setEnabled(false);
        mChart.setTouchEnabled(true);
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setDrawGridBackground(false);
        mChart.setPinchZoom(true);
        LineData data = new LineData();
        data.setValueTextColor(Color.WHITE);
        mChart.setData(data);
        Legend l = mChart.getLegend();
        l.setForm(Legend.LegendForm.LINE);
        l.setTextColor(Color.WHITE);
        XAxis xl = mChart.getXAxis();
        //xl.setValueFormatter(new MyValueFormatter());
        //xl.setValueFormatter(new MyValueFormatter());
        //final String[] weekdays = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        //xl.setValueFormatter(new IndexAxisValueFormatter(weekdays));
        xl.setTextColor(Color.WHITE);
        xl.setDrawGridLines(false);
        xl.setAvoidFirstLastClipping(true);
        xl.setEnabled(true);
        xl.setGranularity(1f);
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        xl.setTextColor(ContextCompat.getColor(mChart.getContext(), R.color.off_white));
        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setTextColor(Color.WHITE);
        leftAxis.setAxisMaximum(60000f);
        leftAxis.setAxisMinimum(51000f);
        leftAxis.setDrawGridLines(false);
        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);

    }
    private void addEntry(float price, float predicted) {
        LineData data = mChart.getData();

        if (data != null) {
            ILineDataSet set = data.getDataSetByIndex(0);
            ILineDataSet set2 = data.getDataSetByIndex(1);
            if (set == null || set2 == null) {
                set = createSetCurrent();
                set2 = createSetPredicted();
                data.addDataSet(set);
                data.addDataSet(set2);
            }
            float asa = price;
            float asb = predicted;
            data.addEntry(new Entry(set.getEntryCount(), asa), 0);
            data.addEntry(new Entry(set2.getEntryCount(), asb), 1);
            data.notifyDataChanged();

            mChart.notifyDataSetChanged();

            // limit the number of visible entries
            mChart.setVisibleXRangeMaximum(10);
            // mChart.setVisibleYRange(30, AxisDependency.LEFT);

            // move to the latest entry
            mChart.moveViewToX(data.getEntryCount());

            // this automatically refreshes the chart (calls invalidate())
            // mChart.moveViewTo(data.getXValCount()-7, 55f,
            // AxisDependency.LEFT);
        }
    }

    private LineDataSet createSetCurrent() {

        LineDataSet set = new LineDataSet(null, "Current Price");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
//        set.setColor(ColorTemplate.getHoloBlue());
//        set.setLineWidth(2f);
//        set.setFillAlpha(65);
//        set.setFillColor(ColorTemplate.getHoloBlue());
//        set.setHighLightColor(Color.rgb(244, 117, 117));
//        set.setValueTextColor(Color.WHITE);
//        set.setValueTextSize(9f);
//        set.setDrawValues(false);
//        LineDataSet.Mode mode = LineDataSet.Mode.CUBIC_BEZIER;
//        set.setMode(mode);

        set.setColor(ContextCompat.getColor(this, R.color.colorDark));
        set.setValueTextColor(ContextCompat.getColor(this, R.color.black_75));
        set.setDrawValues(false);
        set.setLineWidth(3.0F);
        set.setHighlightEnabled(true);
        set.setDrawHighlightIndicators(false);
        set.setDrawCircles(false);
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setDrawFilled(true);
        set.setFillDrawable(ContextCompat.getDrawable(this, R.drawable.bg_spark_line));
        return set;
    }
    private LineDataSet createSetPredicted() {

        LineDataSet set = new LineDataSet(null, "Predicted Price");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
//        set.setColor(ColorTemplate.getHoloBlue());
//        set.setLineWidth(2f);
//        set.setFillAlpha(65);
//        set.setFillColor(ColorTemplate.getHoloBlue());
//        set.setHighLightColor(Color.rgb(244, 117, 117));
//        set.setValueTextColor(Color.WHITE);
//        set.setValueTextSize(9f);
//        set.setDrawValues(false);
//        LineDataSet.Mode mode = LineDataSet.Mode.CUBIC_BEZIER;
//        set.setMode(mode);

        set.setColor(ContextCompat.getColor(this, R.color.colorLight));
        set.setValueTextColor(ContextCompat.getColor(this, R.color.black_75));
        set.setDrawValues(false);
        set.setLineWidth(3.0F);
        set.setHighlightEnabled(true);
        set.setDrawHighlightIndicators(false);
        set.setDrawCircles(false);
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setDrawFilled(true);
        set.setFillDrawable(ContextCompat.getDrawable(this, R.drawable.bg_spark_line));
        return set;
    }


    public void loadPrice() {
        bitcoinRef.orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Bitcoin bitcoin = documentSnapshot.toObject(Bitcoin.class);
                            setCurrentPrice(bitcoin.getLast(), bitcoin.getPredicted());
                            setLastUpdatedTime(bitcoin.getTimestamp());
                            //addEntry();
                        }
                    }
                });
    }

    public void setInitGraphValues() {
        bitcoinRef.orderBy("timestamp", Query.Direction.ASCENDING)
                //.limit(24)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Bitcoin bitcoin = documentSnapshot.toObject(Bitcoin.class);
                            Log.d("timestamp", bitcoin.getTimestamp());
                            Log.d("price", Float.toString(bitcoin.getLast()));
                            addEntry(bitcoin.getLast(), bitcoin.getPredicted());
                        }
                    }
                });

    }

    public void setCurrentPrice(float price, float predicted) {
        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setAxisMaximum(price + 4000f);
        leftAxis.setAxisMinimum(price - 4000f);
        addEntry(price, predicted);
        Log.d("price", Float.toString(price));
        Log.d("predicted", Float.toString(predicted));
        currentPrice.setText("$" + price);
        predictedPrice.setText("$" + predicted);
        ArrayList<String> res = dbHandler.readBitcoinData();
        if(res.get(0).equals("0")) {
            currentPorfolio.setText("Not Set");
            moneyText.setText("Please enter data from settings");
        }
        else {
            Float value = Float.parseFloat(res.get(0));
            value = value * price;
            currentPorfolio.setText("$" + value.toString());
            Float money = Float.parseFloat(res.get(1));
            Float percentage = (price/money) * 100;
            moneyText.setText("Total money invested: $" + res.get(1) + "\nProfits: " + percentage.toString() + "%");
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setLastUpdatedTime(String timestamp) {
        Log.d("time", timestamp);
        timestamp += "000";
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        String currentDateandTime = sdf.format(new Date(Long.parseLong(timestamp)));
        lastUpdatedText.setText("Last Updated at: " + currentDateandTime);
    }


}
