package com.example.adminapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegistrationActivity extends AppCompatActivity {
    EditText name,email,pass1,pass2,phone;
    Button reg;
    Boolean ipOk;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        name = findViewById(R.id.reg_name);
        email = findViewById(R.id.reg_email);
        pass1 = findViewById(R.id.reg_pass1);
        pass2 = findViewById(R.id.reg_pass2);
        phone = findViewById(R.id.reg_phone);
        reg = findViewById(R.id.reg_bt);
        mAuth = FirebaseAuth.getInstance();
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkAllFields()) {
                    mAuth.createUserWithEmailAndPassword(email.getText().toString(), pass1.getText().toString())
                            .addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Toast.makeText(getApplicationContext(), "User account Created Successfully", Toast.LENGTH_SHORT).show();
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        //updateUI(user);
                                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                .setDisplayName(name.getText().toString()).build();
                                        assert user != null;
                                        user.updateProfile(profileUpdates);
                                        Intent i = new Intent(RegistrationActivity.this,MainActivity.class);
                                        i.putExtra("userEmail",user.getEmail());
                                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(i);

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(RegistrationActivity.this, "Failed to Create User account" + task.getException(), Toast.LENGTH_SHORT).show();
                                        //updateUI(null);
                                    }

                                }
                            });
                } else {
                    Toast.makeText(getApplicationContext(), "Please Fill all the Fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private Boolean checkAllFields() {
        if(!name.getText().toString().isEmpty() && !email.getText().toString().isEmpty() &&  !pass1.getText().toString().isEmpty() && !pass2.getText().toString().isEmpty()  &&  !phone.getText().toString().isEmpty()) {
            if(pass1.getText().toString().equals(pass2.getText().toString())) {
                ipOk =  true;
            } else {
                Toast.makeText(this, "Passwords not matching", Toast.LENGTH_SHORT).show();
            }
        } else {
            ipOk = false;
        }
        return ipOk;
    }

    public void LoginActivity(View view) {
        Intent i = new Intent(this,MainActivity.class);
        startActivity(i);
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        //FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }
}
