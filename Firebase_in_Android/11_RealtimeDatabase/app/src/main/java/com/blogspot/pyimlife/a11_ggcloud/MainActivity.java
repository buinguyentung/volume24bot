package com.blogspot.pyimlife.a11_ggcloud;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
//import com.google.cloud.datastore.Datastore;
//import com.google.cloud.datastore.DatastoreOptions;

public class MainActivity extends Activity {

    private static final String TAG = "Volume24Bot";

    Context ctx;
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

    // For Firebase database
    DatabaseReference myRef;

    // For GG cloud datastore - not implement yet
    //Datastore datastore = DatastoreOptions.getDefaultInstance().getService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");
        setContentView(R.layout.activity_main);

        ctx = this;
        myRef = FirebaseDatabase.getInstance().getReference();

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

                myRef.child("HoTen").setValue("BNTung");
                myRef.child("CongTy").child("SVMC").setValue("2014-2017");
                myRef.child("CongTy").child("LGDCV").setValue("2017-2018");
                myRef.child("CongTy").child("PRDCV").setValue("2018-");

                CEntity ce = new CEntity("20181106", "0.0080", "51");
                myRef.child("Ripple").push().setValue(ce);
                CEntity ce2 = new CEntity("20181105", "0.0071", "45");
                myRef.child("Ripple").push().setValue(ce2);
                CEntity ce3 = new CEntity("20181104", "0.0072", "46");
                myRef.child("Ripple").push().setValue(ce3);

                Map<String, Integer> myMap = new HashMap<>();
                myMap.put("XeMay", 2);
                myMap.put("Oto", 4);
                myMap.put("XichLo", 3);
                // Lesson 8: CompletionListener
                myRef.child("Vehicle").setValue(myMap, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                        if (databaseError == null) {
                            Toast.makeText(ctx, "Save successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ctx, "Save failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                btnGen.setEnabled(false);
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sTimestamp = edtTimestamp.getText().toString();
                String sBValue = edtBValue.getText().toString();
                String iUValue = edtUValue.getText().toString();

                CEntity ce = new CEntity(sTimestamp, sBValue, iUValue);
                myRef.child("Ripple").push().setValue(ce);
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lvItem.setItemChecked(selectedPosition, false);
                myRef.child("Ripple").child(listKey.get(selectedPosition)).removeValue();
                btnDelete.setEnabled(false);
            }
        });

        btnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Query query;
                if (!searchMode) {
                    btnFind.setText("Clear");
                    query = myRef.child("Ripple").orderByChild("Timestamp").equalTo(edtTimestamp.getText().toString());
                    searchMode = true;
                } else {
                    btnFind.setText("Find");
                    query = myRef.child("Ripple").orderByKey();
                    searchMode = false;
                }
                // Clear previous selected data in listview
                if (itemSelected) {
                    lvItem.setItemChecked(selectedPosition, false);
                    itemSelected = false;
                    btnDelete.setEnabled(false);
                }
                query.addListenerForSingleValueEvent(queryValueListener);
            }
        });

        // Lesson 9: addValue - receive data Realtime database
        // Change on 'HoTen' value will be updated automatically
        myRef.child("HoTen").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tvName.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // Lesson 10: Receive data using Push String

        // Lesson 11: Receive data using Push Object
        myRef.child("Ripple").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                CEntity ce = dataSnapshot.getValue(CEntity.class);
                listCEntity.add(ce.Timestamp + " - " + ce.bValue + " - " + ce.uValue);
                listKey.add(dataSnapshot.getKey());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                String key = dataSnapshot.getKey();
                int index = listKey.indexOf(key);
                Log.d(TAG, "onChildRemoved: " + key);
                if (index != -1) {
                    listCEntity.remove(index);
                    listKey.remove(index);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    ValueEventListener queryValueListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            Iterable<DataSnapshot> snapshots = dataSnapshot.getChildren();
            Iterator<DataSnapshot> iterator = snapshots.iterator();

            adapter.clear();
            listKey.clear();
            while (iterator.hasNext()) {
                DataSnapshot next = (DataSnapshot) iterator.next();
                CEntity ce = next.getValue(CEntity.class);
                listCEntity.add(ce.Timestamp + " - " + ce.bValue + " - " + ce.uValue);
                listKey.add(next.getKey());
                adapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };
}
