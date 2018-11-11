package com.blogspot.pyimlife.firebasefilestoretut;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
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

    Button btnGen;
    TextView tvName;
    EditText edtTimestamp, edtBValue, edtUValue;
    Button btnAdd, btnFind, btnDelete;

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
        myColRef = myRef.collection("xrp_ex");

        btnGen = (Button) findViewById(R.id.btnGen);
        btnGen.setEnabled(true);
        tvName = (TextView) findViewById(R.id.tvName);
        edtTimestamp = (EditText) findViewById(R.id.edtTimestamp);
        edtBValue = (EditText) findViewById(R.id.edtBValue);
        edtUValue = (EditText) findViewById(R.id.edtUValue);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnFind = (Button) findViewById(R.id.btnFind);
        btnDelete = (Button) findViewById(R.id.btnDelete);
        btnDelete.setEnabled(false);

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
                btnDelete.setEnabled(true);
            }
        });

        btnGen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generateData();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addData();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteData();
            }
        });

        btnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findData();
            }
        });

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
                    listCEntity.add(ce.Timestamp + " - " + ce.bValue + " - " + ce.uValue);
                    listKey.add(document.getId());
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    public void generateData() {
        Log.d(TAG, "Generating Data");
        CEntity ce = new CEntity("20181106", "0.0080", "51");
        myColRef.document().set(ce);
        CEntity ce2 = new CEntity("20181105", "0.0071", "45");
        myColRef.document().set(ce2);
        CEntity ce3 = new CEntity("20181104", "0.0072", "46");
        myColRef.document().set(ce3).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "Object saved successfully!");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Object saved failed!");
            }
        });
        btnGen.setEnabled(false);
    }

    public void addData() {
        String sTimestamp = edtTimestamp.getText().toString();
        String sBValue = edtBValue.getText().toString();
        String iUValue = edtUValue.getText().toString();

        CEntity ce = new CEntity(sTimestamp, sBValue, iUValue);
        myColRef.document().set(ce);
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
        btnDelete.setEnabled(false);
    }

    public void findData() {
        Query query;
        if (!searchMode) {
            String findStr = edtTimestamp.getText().toString();
            Log.d(TAG, "find string = " + findStr);
            query = myColRef.whereEqualTo("Timestamp", findStr);
            btnFind.setText("Clear");
            searchMode = true;
            query.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        Log.d(TAG, "Listen failed.", e);
                        return;
                    }
                    Log.d(TAG, "Fetching find data.");
                    adapter.clear();
                    listCEntity.clear();
                    listKey.clear();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        CEntity ce = document.toObject(CEntity.class);
                        listCEntity.add(ce.Timestamp + " - " + ce.bValue + " - " + ce.uValue);
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
            btnDelete.setEnabled(false);
        }
    }

    public void fetchData() {
        // get data
        myColRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                Log.d(TAG, "Getting Data");
                if (task.isSuccessful()) {
                    adapter.clear();
                    listCEntity.clear();
                    listKey.clear();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        CEntity ce = document.toObject(CEntity.class);
                        listCEntity.add(ce.Timestamp + " - " + ce.bValue + " - " + ce.uValue);
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
