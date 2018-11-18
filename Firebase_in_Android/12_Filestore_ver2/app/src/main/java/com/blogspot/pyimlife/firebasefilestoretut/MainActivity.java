package com.blogspot.pyimlife.firebasefilestoretut;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import javax.annotation.Nullable;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Volume24Bot";

    Context ctx;

    // Access a Cloud Firestore instance from your Activity
    FirebaseFirestore myRef;
    CollectionReference myColRef;

    Spinner spnSelectCoin;
    ArrayAdapter<String>  spnAdapter;
    String selectedCoin = "bitcoin";
    private final String SELECTED_COIN = "selected_coin";

    Button btnGetAll;
    TextView tvName;
    EditText edtSelectDate;
    Button btnFind, btnDelete;

    ListView lvItem;
    ArrayList<String> listCEntity;
    ArrayList<String> listKey;
    ArrayAdapter adapter;

    private Boolean searchMode = false;
    private Boolean itemSelected = false;
    private int selectedPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ctx = this;
        myRef = FirebaseFirestore.getInstance();
        myColRef = myRef.collection(selectedCoin);

        spnSelectCoin = (Spinner) findViewById(R.id.spnSelectCoin);
        spnAdapter = new ArrayAdapter<String>(ctx,
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.saSelectCoin));
        spnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnSelectCoin.setAdapter(spnAdapter);

        tvName = (TextView) findViewById(R.id.tvName);
        edtSelectDate = (EditText) findViewById(R.id.edtSelectDate);
        btnGetAll = (Button) findViewById(R.id.btnGetAll);
        btnFind = (Button) findViewById(R.id.btnFind);
        btnDelete = (Button) findViewById(R.id.btnDelete);
        //btnDelete.setEnabled(false);

        lvItem = (ListView) findViewById(R.id.lvItem);
        listCEntity = new ArrayList<String>();
        listKey = new ArrayList<String>();
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_single_choice, listCEntity);
        lvItem.setAdapter(adapter);
        lvItem.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        lvItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedPosition = i;
                itemSelected = true;
                //btnDelete.setEnabled(true);
            }
        });

        btnGetAll.setOnClickListener(new ServiceOnClickListener());

        btnDelete.setOnClickListener(new ServiceOnClickListener());

        btnFind.setOnClickListener(new ServiceOnClickListener());

        spnSelectCoin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                selectedCoin = (String) adapterView.getItemAtPosition(position);
                Toast.makeText(ctx, "Selected: " + selectedCoin, Toast.LENGTH_SHORT).show();
                myColRef = myRef.collection(selectedCoin);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    class ServiceOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btnGetAll:
                    getAllData();
                    break;
                case R.id.btnFind:
                    findData();
                    break;
                case R.id.btnDelete:
                    // Start DetailActivity class
                    Intent intent = new Intent(ctx, DetailActivity.class);
                    intent.putExtra(SELECTED_COIN, "ripple");
                    ctx.startActivity(intent);
                    //deleteData();
                    break;
                default:
                    break;
            }
        }
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
                //Log.d(TAG, "Listening Data.");
                adapter.clear();
                listCEntity.clear();
                listKey.clear();
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    CEntity ce = document.toObject(CEntity.class);
                    listCEntity.add(ce.updated_date + " - vol_24h: " + ce.volume_usd + " - price_btc: " + ce.price_btc + " - price_usd: " + ce.price_usd);
                    listKey.add(document.getId());
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    public void getAllData() {
        fetchData();
        btnFind.setText("Find");
        searchMode = false;
        // Clear previous selected data in listview
        if (itemSelected) {
            lvItem.setItemChecked(selectedPosition, false);
            itemSelected = false;
            //btnDelete.setEnabled(false);
        }
    }

    public void deleteData() {
        lvItem.setItemChecked(selectedPosition, false);
        myColRef.document(listKey.get(selectedPosition))
                .delete()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Delete document error");
                    }
                });
        //btnDelete.setEnabled(false);
    }

    public void findData() {
        Query query;
        if (!searchMode) {
            //String findStr = edtTimestamp.getText().toString();
            String findStr = edtSelectDate.getText().toString();
            if (findStr.equals("")) {
                findStr = "2018-11-06";
            }
            //Log.d(TAG, "find string = " + findStr);
            query = myColRef.whereEqualTo("updated_date", findStr);
            btnFind.setText("Clear");
            searchMode = true;
            query.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        Log.d(TAG, "Listen failed.", e);
                        return;
                    }
                    //Log.d(TAG, "Fetching find data.");
                    adapter.clear();
                    listCEntity.clear();
                    listKey.clear();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        CEntity ce = document.toObject(CEntity.class);
                        listCEntity.add(ce.updated_date + " - vol_24h: " + ce.volume_usd + " - price_btc: " + ce.price_btc + " - price_usd: " + ce.price_usd);
                        listKey.add(document.getId());
                    }
                    adapter.notifyDataSetChanged();
                }
            });
        } else {
            fetchData();
            btnFind.setText("Find");
            searchMode = false;
        }
        // Clear previous selected data in listview
        if (itemSelected) {
            lvItem.setItemChecked(selectedPosition, false);
            itemSelected = false;
            //btnDelete.setEnabled(false);
        }
    }

    public void fetchData() {
        // get data
        myColRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                //Log.d(TAG, "Getting Data");
                if (task.isSuccessful()) {
                    adapter.clear();
                    listCEntity.clear();
                    listKey.clear();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        CEntity ce = document.toObject(CEntity.class);
                        listCEntity.add(ce.updated_date + " - vol_24h: " + ce.volume_usd + " - price_btc: " + ce.price_btc + " - price_usd: " + ce.price_usd);
                        listKey.add(document.getId());
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

}
