package com.example.adminapplication;


import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Objects;

import adapters_custom.Attend_adapter;
import cards.Attend_Item;


public class Attendance extends AppCompatActivity {

    RecyclerView cRecyclerView;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference cr_ref = db.collection("Attendance");
    private Attend_adapter adapter;
    FirestoreRecyclerOptions<Attend_Item> options;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        cRecyclerView = findViewById(R.id.circular_recycle);
        cRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        setCirculars();

    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    private void setCirculars() {
        Query query = cr_ref.orderBy("name",Query.Direction.ASCENDING);

        options = new FirestoreRecyclerOptions.Builder<Attend_Item>()
                .setQuery(query,Attend_Item.class)
                .build();

        adapter = new Attend_adapter(options);
        cRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        cRecyclerView.setAdapter(adapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                Objects.requireNonNull(cRecyclerView.getAdapter()).notifyItemRemoved(i);
            }
        }).attachToRecyclerView(cRecyclerView);

        adapter.setOnItemClickListener(new Attend_adapter.OnItemClickListener() {

            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position, Boolean checkcb) {
                Attend_Item circ = documentSnapshot.toObject(Attend_Item.class);
                String id = documentSnapshot.getId();
                String path = documentSnapshot.getReference().getPath();
                if(checkcb) {
                    cr_ref.document(id).update("status","present");
                } else {
                    cr_ref.document(id).update("status","absent");
                }
                Toast.makeText(Attendance.this,
                        "Attendance for " + id + " Taken Successfully " , Toast.LENGTH_SHORT).show();
              /*  Intent i = new Intent(Circulars_activity.this,ViewCircular.class);
                i.putExtra("id",id);
                Toast.makeText(Circulars_activity.this,
                        "Position: " + position + " ID: " + id, Toast.LENGTH_SHORT).show();
                startActivity(i);
                */
            }
        });
    }

}
