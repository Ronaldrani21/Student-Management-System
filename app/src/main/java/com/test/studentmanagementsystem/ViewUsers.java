package com.test.studentmanagementsystem;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ViewUsers extends AppCompatActivity {
    private TableLayout tableLayout;
    Cursor cursor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_users);

        tableLayout = findViewById(R.id.tableLayoutUsers);

        RegisterUser.UserDetailsDbHelper dbHelper = new RegisterUser.UserDetailsDbHelper(getApplicationContext());

        //Fetching my data from my database

        SQLiteDatabase dbbb = dbHelper.getReadableDatabase();

        cursor = dbbb.query(
                RegisterUser.UserDetailsContract.UserEntry.TABLE_NAME,   // The table to query
                null,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The s+ort order
        );

        while (cursor.moveToNext()) {
            View tableRow = LayoutInflater.from(this).inflate(R.layout.user_table, null, false);

            TextView tttuser = (TextView) tableRow.findViewById(R.id.UTuser);
            TextView tttregno = (TextView) tableRow.findViewById(R.id.UTregno);
            TextView tttname = (TextView) tableRow.findViewById(R.id.UTname);
            TextView tttemail = (TextView) tableRow.findViewById(R.id.UTemail);
            TextView tttcourseunit = (TextView) tableRow.findViewById(R.id.UTcourseunit);


            tttuser.setText(cursor.getString(1));
            tttregno.setText(cursor.getString(2));
            tttname.setText(cursor.getString(3));
            tttemail.setText(cursor.getString(4));
            tttcourseunit.setText(cursor.getString(6));
            tableLayout.addView(tableRow);

        }
        cursor.close();
    }
}
