package com.example.adminapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import adapters_custom.BookAdapter;
import cards.Book;

public class LibraryManagement extends AppCompatActivity {

    RecyclerView recyclerView;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference bookref = db.collection("Library");
    private BookAdapter adapter;
    String search_text = null;
    EditText e1;
    FirestoreRecyclerOptions<Book> options,options2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_management);
        e1 =findViewById(R.id.ed_search);
        recyclerView = findViewById(R.id.recycler1);
        allBookList();
    }
    private void allBookList() {
        Query query = bookref.orderBy("title",Query.Direction.ASCENDING);

        options = new FirestoreRecyclerOptions.Builder<Book>()
                .setQuery(query,Book.class)
                .build();

        adapter = new BookAdapter(options);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
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


    public void changeData(View v) {
        search_text = e1.getText().toString();

        if(search_text.equals("")) {
            allBookList();
            adapter.startListening();
        }
        else {
            Query query2 = bookref.whereEqualTo("title",search_text);
            options2 = new FirestoreRecyclerOptions.Builder<Book>()
                    .setQuery(query2,Book.class)
                    .build();
            adapter = new BookAdapter(options2);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            adapter.startListening();

        }

    }
}


/*
 com.example.adminapplication;
LibraryManagement
 setContentView(R.layout.activity_library_management);
 */