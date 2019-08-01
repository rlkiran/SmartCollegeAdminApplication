package com.example.adminapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    public void openIt(View view) {
        Intent i = new Intent(this, InternalMarks.class);
        startActivity(i);
    }

    public void openTb(View view) {
        Intent i = new Intent(this, TimeTables.class);
        startActivity(i);
    }

    public void openCir(View view) {
        Intent i = new Intent(this, Circulars.class);
        startActivity(i);
    }

    public void openLib(View view) {
        Intent i = new Intent(this, LibraryManagement.class);
        startActivity(i);
    }

    public void openBusSharing(View view) {
        Intent i = new Intent(this, BusTracking.class);
        startActivity(i);
    }

    public void openAttendance(View view) {
        Intent i = new Intent(this, Attendance.class);
        startActivity(i);
    }


    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        HomeActivity.super.onBackPressed();
                    }
                }).create().show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.menus, menu);

        return true;

    }

    public boolean onOptionsItemSelected(MenuItem item) {


        if (item.getItemId() == R.id.logout) {

            new AlertDialog.Builder(this)
                    .setTitle("Logout")
                    .setMessage("Are you sure you want to logout?")
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface arg0, int arg1) {
                            FirebaseAuth.getInstance().signOut();
                            Intent i = new Intent(getApplicationContext(), MainActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                        }
                    }).create().show();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
