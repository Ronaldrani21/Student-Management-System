package com.test.studentmanagementsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class ViewResults extends AppCompatActivity {

    private TableLayout tableLayout;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.results);

        tableLayout = findViewById(R.id.tableLayout);

//        ArrayList<String> MyStudents = new ArrayList<>();

        RegisterUser.UserDetailsDbHelper dbHelper = new RegisterUser.UserDetailsDbHelper(getApplicationContext());

        //Fetching my data from my database

        SQLiteDatabase dbbb = dbHelper.getReadableDatabase();

        cursor = dbbb.query(
                RegisterUser.UserDetailsContract.ResultsEntry.TABLE_NAME,   // The table to query
                null,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The s+ort order
        );

        while (cursor.moveToNext()) {
            View tableRow = LayoutInflater.from(this).inflate(R.layout.table, null, false);

            TextView ttfinalle = (TextView) tableRow.findViewById(R.id.tRealFinal);
            TextView ttregno = (TextView) tableRow.findViewById(R.id.tregno);
            TextView ttcourseunit = (TextView) tableRow.findViewById(R.id.tcourseunit);
            TextView ttmark11 = (TextView) tableRow.findViewById(R.id.tmark1);
            TextView ttmark22 = (TextView) tableRow.findViewById(R.id.tmark2);
            TextView ttmark33 = (TextView) tableRow.findViewById(R.id.tmark3);
            TextView ttfinalmark4 = (TextView) tableRow.findViewById(R.id.tfinalmark);

            int FinalCompute, m1,m2,m3,FF;
            m1 = Integer.parseInt(cursor.getString(3));
            m2 = Integer.parseInt(cursor.getString(4));
            m3 = Integer.parseInt(cursor.getString(5));
            FF = Integer.parseInt(cursor.getString(6));

            FinalCompute = (((m1+m2+m3)/3)/2 + (FF/2));

            String FFC = String.valueOf(FinalCompute);


            ttfinalle.setText(FFC);
            ttregno.setText(cursor.getString(1));
            ttcourseunit.setText(cursor.getString(2));
            ttmark11.setText(cursor.getString(3));
            ttmark22.setText(cursor.getString(4));
            ttmark33.setText(cursor.getString(5));
            ttfinalmark4.setText(cursor.getString(6));
            tableLayout.addView(tableRow);
//
//            MyStudents.add("ID: "+cursor.getString(0));
//            MyStudents.add("First Name: "+cursor.getString(1));
//            MyStudents.add("Last Name: "+cursor.getString(2));
//            MyStudents.add("Course Unit: "+cursor.getString(4));
//            MyStudents.add("Program: "+cursor.getString(3));
//            MyStudents.add("Residence: "+cursor.getString(5));
//            MyStudents.add("Year of Study: "+cursor.getString(6));
        }
        cursor.close();
//        data.moveToFirst();
//        do {
//            View tableRow = LayoutInflater.from(this).inflate(R.layout.table_item,null,false);
//            TextView name  = (TextView) tableRow.findViewById(R.id.name);
//            TextView title  = (TextView) tableRow.findViewById(R.id.title);
//
//
//            name.setText(data.getString(1));
//            title.setText(data.getString(2));
//            tableLayout.addView(tableRow);
//
//        } while (data.moveToNext());
//        data.close();
    }
}

