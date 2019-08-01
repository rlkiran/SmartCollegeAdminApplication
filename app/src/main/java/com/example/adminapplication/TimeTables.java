package com.example.adminapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;


public class TimeTables extends AppCompatActivity {

    public static final int PICK_IMAGE_REQUEST = 1;
    LinearLayout up_lt,del_lt;
    Button up_bt,del_bt,upload_file,up_cancel,up_ok,del_ok,del_cancel;
    ImageView img_TT;
    ProgressBar pb;
    Uri ImageUri;
    Spinner sp1,sp2,sp3,sp4,sp5,sp6;
    StorageReference tt_storageReference;
    DatabaseReference tt_databaseReference;

    String[] year = {"YEAR" , "FIRST" , "SECOND" ,"THIRD" ,"FINAL"};
    String[] branch = {"BRANCH","CSE","ECE","CIVIL","EEE","MECH"};
    String[] section = {"SECTION","A","B"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_tables);
        up_lt = findViewById(R.id.layout_upTT);
        up_bt = findViewById(R.id.bt_upTT);
        del_lt = findViewById(R.id.layout_delTT);
        del_bt = findViewById(R.id.bt_delTT);
        upload_file = findViewById(R.id.bt_file_select);
        img_TT = findViewById(R.id.img_timeTable);
        pb = findViewById(R.id.pb);
        up_cancel = findViewById(R.id.bt_upCancel);
        up_ok = findViewById(R.id.bt_upOk);
        sp1 = findViewById(R.id.year_sp);
        sp2 = findViewById(R.id.branch_sp);
        sp3 = findViewById(R.id.section_sp);
        sp4 = findViewById(R.id.sp4);
        sp5 = findViewById(R.id.sp5);
        sp6 = findViewById(R.id.sp6);
        del_ok = findViewById(R.id.bt_del_ok);
        del_cancel = findViewById(R.id.bt_del_cancel);



        ArrayAdapter<String> adapter =  new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, year);
        ArrayAdapter<String> adapter2 =  new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, branch);
        ArrayAdapter<String> adapter3 =  new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, section);

        sp1.setAdapter(adapter);
        sp2.setAdapter(adapter2);
        sp3.setAdapter(adapter3);

        ArrayAdapter<String> adapter4 =  new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, year);
        ArrayAdapter<String> adapter5 =  new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, branch);
        ArrayAdapter<String> adapter6 =  new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, section);

        sp4.setAdapter(adapter4);
        sp5.setAdapter(adapter5);
        sp6.setAdapter(adapter6);

        up_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                up_lt.setVisibility(View.VISIBLE);
            }
        });
        del_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                del_lt.setVisibility(View.VISIBLE);
            }
        });
        del_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                del_lt.setVisibility(View.GONE);
            }
        });
        upload_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileSelector();
            }
        });
        up_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { up_lt.setVisibility(View.GONE);
            }
        });
        up_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadTimeTable();
            }
        });
        del_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String filename = getDataFromSecondSpinners();
                if(filename!= null) {
                    tt_storageReference = FirebaseStorage.getInstance().getReference("timetables").child(filename);
                    tt_databaseReference = FirebaseDatabase.getInstance().getReference("timetables").child(filename);
                    tt_storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("timetables").child(filename);
                            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot apSnapshot: dataSnapshot.getChildren()) {
                                        apSnapshot.getRef().removeValue();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                            Toast.makeText(TimeTables.this, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Uh-oh, an error occurred!
                            Toast.makeText(TimeTables.this, "Unable To delete file" +exception.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }else {
                    Toast.makeText(getApplicationContext(), "Please Select Class Details", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void uploadTimeTable() {
        if (ImageUri != null)
        {
            final String filename = getDataFromSpinners();
            if(filename!= null) {
                tt_storageReference  = FirebaseStorage.getInstance().getReference("timetables").child(filename);
                tt_databaseReference = FirebaseDatabase.getInstance().getReference("timetables");
                Toast.makeText(this, "File name " + filename, Toast.LENGTH_SHORT).show();
                pb.setVisibility(View.VISIBLE);
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
                            Toast.makeText(TimeTables.this, "File Uploaded Successfully", Toast.LENGTH_SHORT).show();
                            Uri downloadUri = task.getResult();
                            pb.setVisibility(View.INVISIBLE);
                            assert downloadUri != null;
                            tt_databaseReference.child(filename).child("url").setValue(downloadUri.toString());
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(TimeTables.this, "upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(this, "Please Select Class Details", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(this, "No file Selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void openFileSelector() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(i,PICK_IMAGE_REQUEST);
    }
    private String getDataFromSpinners() {
        String name = null;
        if (sp1.getSelectedItemPosition() > 0 && sp2.getSelectedItemPosition() > 0 && sp3.getSelectedItemPosition() > 0) {
            name = sp1.getSelectedItem().toString() +" " + sp2.getSelectedItem().toString()+ " " + sp3.getSelectedItem().toString();
        }
        return name;
    }

    private String getDataFromSecondSpinners() {
        String name = null;
        if (sp4.getSelectedItemPosition() > 0 && sp5.getSelectedItemPosition() > 0 && sp6.getSelectedItemPosition() > 0) {
            name = sp4.getSelectedItem().toString() +" " + sp5.getSelectedItem().toString()+ " " + sp6.getSelectedItem().toString();
        }
        return name;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK &&
                data != null && data.getData() != null) {
            ImageUri = data.getData();
            Picasso.get()
                    .load(ImageUri)
                    .fit()
                    .into(img_TT);
        }
    }
}
