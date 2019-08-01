package com.example.adminapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
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

public class MainActivity extends AppCompatActivity {

    EditText email,pass;
    Button signIn;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        email = findViewById(R.id.sign_email);
        pass = findViewById(R.id.sign_pass);
        signIn = findViewById(R.id.sign_bt);

        mAuth = FirebaseAuth.getInstance();
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!email.getText().toString().isEmpty() && !pass.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Validating User data", Toast.LENGTH_LONG).show();
                    mAuth.signInWithEmailAndPassword(email.getText().toString(), pass.getText().toString())
                            .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Toast.makeText(MainActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        Intent i = new Intent(getApplicationContext(),HomeActivity.class);
                                        assert user != null;
                                        i.putExtra("username",user.getDisplayName());
                                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(i);
                                        //updateUI(user);

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(MainActivity.this, "signInWithEmail:failure" + task.getException(), Toast.LENGTH_SHORT).show();
                                        Toast.makeText(MainActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                        //updateUI(null);
                                    }

                                }
                            });
                } else {
                    Toast.makeText(MainActivity.this, "Please Enter Email And Password", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }



    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("Do you want to Exit?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user pressed "yes", then he is allowed to exit from application
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user select "No", just cancel this dialog and continue with app
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void RegActivity(View view) {
        Intent i = new Intent(this,RegistrationActivity.class);
        startActivity(i);
    }

    public void resetEmail(View view) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String emailAddress = email.getText().toString();
        if(!emailAddress.isEmpty()) {
            auth.sendPasswordResetEmail(emailAddress)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(MainActivity.this, "Check Your Email for Password Reset", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(this, "Please Enter Your Email Address", Toast.LENGTH_SHORT).show();
        }

    }
}
