package com.blogspot.pyimlife.firebasefilestoretut;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;

import javax.annotation.Nullable;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = "Volume24Bot";

    Context ctx;

    String selectedCoin = "bitcoin";
    private final String SELECTED_COIN = "selected_coin";

    // Access a Cloud Firestore instance from your Activity
    FirebaseFirestore myRef;
    CollectionReference myColRef;

    ArrayList<CEntity> listCEntity;
    ArrayList<String> listKey;

    // Graph
    GraphView graphView;
    double x, y;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ctx = this;
        Intent intent = getIntent();
        selectedCoin = intent.getStringExtra(SELECTED_COIN);

        myRef = FirebaseFirestore.getInstance();
        myColRef = myRef.collection(selectedCoin);
        listCEntity = new ArrayList<CEntity>();
        listKey = new ArrayList<String>();

        // Graph
        graphView = (GraphView) findViewById(R.id.mainGraph);

    }

    @Override
    protected void onStart() {
        super.onStart();

        // Listen to multiple documents in a collection
        myColRef.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.d(TAG, "Listen failed.", e);
                    return;
                }
                Log.d(TAG, "Query Data.");
                listCEntity.clear();
                listKey.clear();
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    CEntity ce = document.toObject(CEntity.class);
                    listCEntity.add(ce);
                    listKey.add(document.getId());
                }
                // Draw graph
                DataPoint[] dataPoints = new DataPoint[listCEntity.size()];
                x = -1;
                int i = -1;
                for (CEntity ce : listCEntity) {
                    i = i + 1;
                    x = x + 1;
                    try {
                        y = Double.parseDouble(ce.volume_usd.toString());
                    } catch (NumberFormatException err) {
                        y = 0;
                    }
                    //Log.d(TAG, x + " ; " + y);
                    dataPoints[i] = new DataPoint(x, y);
                }
                BarGraphSeries<DataPoint> series = new BarGraphSeries<DataPoint>(dataPoints);
                graphView.addSeries(series);
                // styling
                series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
                    @Override
                    public int get(DataPoint data) {
                        return Color.rgb((int) data.getX()*255/4, (int) Math.abs(data.getY()*255/6), 100);
                    }
                });

                series.setSpacing(50);

                // draw values on top
                series.setDrawValuesOnTop(true);
                series.setValuesOnTopColor(Color.RED);
                //series.setValuesOnTopSize(50);
            }
        });
    }
}
