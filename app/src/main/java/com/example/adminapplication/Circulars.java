package com.example.adminapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class Circulars extends AppCompatActivity {
    public static final int PICK_IMAGE_REQUEST = 1;
    StorageReference tt_storageReference;
    EditText e1,e2;
    Button b,post_cir;
    Uri ImageUri;
    ImageView img_Cir;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String dlink;
    ProgressBar pb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circulars);
        e1 = findViewById(R.id.ed_title);
        e2 = findViewById(R.id.ed_desc);
        b = findViewById(R.id.bt_file_circular);
        img_Cir = findViewById(R.id.img_circulars);
        post_cir = findViewById(R.id.bt_postCir);
        pb = findViewById(R.id.circular_pb);


        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!e1.getText().toString().isEmpty() && !e2.getText().toString().isEmpty()) {
                    openFileSelector();
                } else {
                    Toast.makeText(Circulars.this, "Please Fill all the required fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        post_cir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ImageUri != null)
                {
                    pb.setVisibility(View.VISIBLE);
                    tt_storageReference  = FirebaseStorage.getInstance().getReference("Circulars").child(e1.getText().toString());
                    tt_storageReference.putFile(ImageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>()
                    {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task)
                        {
                            return tt_storageReference.getDownloadUrl();

                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>()
                    {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void onComplete(@NonNull Task<Uri> task)
                        {
                            if (task.isSuccessful()) {
                                Uri downloadUri = task.getResult();
                                assert downloadUri != null;
                                dlink = downloadUri.toString();
                                Map<String, Object> circular_data = new HashMap<>();
                                circular_data.put("description", e2.getText().toString());
                                circular_data.put("title", e1.getText().toString());
                                circular_data.put("url", dlink);

                                db.collection("Circulars").document(e1.getText().toString())
                                        .set(circular_data)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(Circulars.this, "Posted Successfully", Toast.LENGTH_SHORT).show();
                                                pb.setVisibility(View.GONE);
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        pb.setVisibility(View.GONE);
                                        Toast.makeText(Circulars.this, "Not Posted", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pb.setVisibility(View.GONE);
                            Toast.makeText(Circulars.this, "Image upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
                else {
                    pb.setVisibility(View.GONE);
                    Toast.makeText(Circulars.this, "Please select an Image", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void openFileSelector() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(i,PICK_IMAGE_REQUEST);
    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK &&
                data != null && data.getData() != null) {
            ImageUri = data.getData();
            Picasso.get()
                    .load(ImageUri)
                    .fit()
                    .into(img_Cir);
        }
    }
}
