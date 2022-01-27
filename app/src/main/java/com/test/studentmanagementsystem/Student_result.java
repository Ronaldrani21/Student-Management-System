package com.test.studentmanagementsystem;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Student_result extends AppCompatActivity {

    String[] courseUnitItems = {"Artificial Intelligence","CyberSecurity","Software Project Management","Mobile Application Development","Cloud computing","Data Communication"};
    AutoCompleteTextView autoCompleteTxtCourseRes;

    ArrayAdapter<String> adapterCourseItems;

    SharedPreferences sp;

    EditText resultReg;
    EditText resultCourseU;
    EditText resultM1;
    EditText resultM2;
    EditText resultM3;
    EditText resultFinal;
    String theLecturerCourseUnit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_details);


//        Showing the Logged in User from LoginMain Activity
        sp = getSharedPreferences("MyLoginUserSharePref", Context.MODE_PRIVATE);

        String TheFetchedLoggedUser = sp.getString("loggedUser", "defaultValue");
        String LoggedUserName = sp.getString("LoginUserName", "defaultValue");

        Toast.makeText(Student_result.this, "Saved User Info:" + TheFetchedLoggedUser, Toast.LENGTH_SHORT).show();
        Toast.makeText(Student_result.this, "And the UserName is:" + LoggedUserName, Toast.LENGTH_SHORT).show();


        // Gets the data repository in write mode
        if(TheFetchedLoggedUser.equals("Lecturer")){
            //          Database query to fetch that specific lecturer's course unit they teach

            RegisterUser.UserDetailsDbHelper dbHelper = new RegisterUser.UserDetailsDbHelper(getApplicationContext());

            String select_query= "SELECT courseunit FROM users WHERE name='"+LoggedUserName+"'";


            SQLiteDatabase db = dbHelper.getWritableDatabase();

            Cursor cursor = db.rawQuery(select_query, null);
//
//        cursor.getString(1);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    theLecturerCourseUnit = cursor.getString(0);
//                Toast.makeText(ResultStudent.this, "From Cursor :"+theUser , Toast.LENGTH_SHORT).show();

                }while (cursor.moveToNext());
            }

//        End database query
        }
        else{
            Toast.makeText(Student_result.this, "This might be Admin :", Toast.LENGTH_SHORT).show();

        }

        resultReg = findViewById(R.id.ResultReg);
        resultCourseU = findViewById(R.id.auto_complete_txt_ResultCourse);
        resultM1 = findViewById(R.id.assi1);
        resultM2 = findViewById(R.id.assi2);
        resultM3 = findViewById(R.id.assi3);
        resultFinal = findViewById(R.id.ResExam);


//       String LecturerCourse = theLecturerCourseUnit;

        autoCompleteTxtCourseRes = findViewById(R.id.auto_complete_txt_ResultCourse);

        adapterCourseItems = new ArrayAdapter<String>(this, R.layout.list, courseUnitItems);

        autoCompleteTxtCourseRes.setAdapter(adapterCourseItems);

        if(TheFetchedLoggedUser.equals("Lecturer")){
//            int spinPos = adapterCourseItems.getPosition(LecturerCourse);
//            autoCompleteTxtCourseRes.setSelection(spinPos);
        }
        else{
            Toast.makeText(getApplicationContext(), " NOT LECTURER ", Toast.LENGTH_LONG).show();

        }


        autoCompleteTxtCourseRes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String Course = parent.getItemAtPosition(position).toString();

                if(TheFetchedLoggedUser.equals("Lecturer")){
                    Toast.makeText(getApplicationContext(), LoggedUserName + " inserting marks for : " + theLecturerCourseUnit, Toast.LENGTH_LONG).show();

                }
                else if(TheFetchedLoggedUser.equals("HOD")){
                    Toast.makeText(getApplicationContext(), LoggedUserName + " inserting marks for : " + Course, Toast.LENGTH_LONG).show();

                }
                else{
                    Toast.makeText(getApplicationContext(), LoggedUserName + " Not Authorized to Alter student marks : ", Toast.LENGTH_LONG).show();
                }
            }
        });

//        Submit button code
        Button ResultBtnSubmitt  = findViewById(R.id.ResultBtnSubmit);

        ResultBtnSubmitt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ResultRno, ResultCourseUnit, ResultMark1, ResultMark2, ResultMark3, ResultFinalMark;

//                Checking if its HOD inserting or Lecturer inserting
                if(TheFetchedLoggedUser.equals("Lecturer")){
                    ResultRno = resultReg.getText().toString();
                    ResultCourseUnit = theLecturerCourseUnit;
                    ResultMark1 = resultM1.getText().toString();
                    ResultMark2 = resultM2.getText().toString();
                    ResultMark3 = resultM3.getText().toString();
                    ResultFinalMark = resultFinal.getText().toString();


                    if (resultReg.getText().toString().trim().length() <= 0
                            || resultCourseU.getText().toString().length() <= 0
                            || resultM1.getText().toString().length() <= 0
                            || resultM2.getText().toString().length() <= 0
                            || resultM3.getText().toString().length() <= 0
                            || resultFinal.getText().toString().length() <= 0) {

                        Toast.makeText(Student_result.this, "All Fields for StudentResults are Required!!!",
                                Toast.LENGTH_LONG).show();
                    }
                    else {
                        //--------------------inserting the data to the database--------------------------

                        //getting access to the database by instantiating the subclass of SQLiteOpenHelper
                        RegisterUser.UserDetailsDbHelper dbHelper = new RegisterUser.UserDetailsDbHelper(getApplicationContext());

                        // Gets the data repository in write mode
                        SQLiteDatabase db = dbHelper.getWritableDatabase();

                        // Create a new map of values, where column names are the keys
                        ContentValues values = new ContentValues();
                        values.put(RegisterUser.UserDetailsContract.ResultsEntry.COLUMN_REGNO, ResultRno);
                        values.put(RegisterUser.UserDetailsContract.ResultsEntry.COLUMN_COURSEUNIT, ResultCourseUnit);
                        values.put(RegisterUser.UserDetailsContract.ResultsEntry.COLUMN_MARK1, ResultMark1);
                        values.put(RegisterUser.UserDetailsContract.ResultsEntry.COLUMN_MARK2, ResultMark2);
                        values.put(RegisterUser.UserDetailsContract.ResultsEntry.COLUMN_MARK3, ResultMark3);
                        values.put(RegisterUser.UserDetailsContract.ResultsEntry.COLUMN_FINALMARK, ResultFinalMark);



                        // Insert the new row, returning the primary key value of the new row
                        long newRowId = db.insert(RegisterUser.UserDetailsContract.ResultsEntry.TABLE_NAME, null, values);

                        Log.d("StudentData In my DB", "Student Mark: " + newRowId);

                        //show message of Lecturer Added
                        Toast.makeText(Student_result.this, "Student Results Added Successfully", Toast.LENGTH_LONG).show();

                    }
                }
                else if(TheFetchedLoggedUser.equals("HOD")){
                    ResultRno = resultReg.getText().toString();
                    ResultCourseUnit = resultCourseU.getText().toString();
                    ResultMark1 = resultM1.getText().toString();
                    ResultMark2 = resultM2.getText().toString();
                    ResultMark3 = resultM3.getText().toString();
                    ResultFinalMark = resultFinal.getText().toString();


                    if (resultReg.getText().toString().trim().length() <= 0
                            || resultCourseU.getText().toString().length() <= 0
                            || resultM1.getText().toString().length() <= 0
                            || resultM2.getText().toString().length() <= 0
                            || resultM3.getText().toString().length() <= 0
                            || resultFinal.getText().toString().length() <= 0) {

                        Toast.makeText(Student_result.this, "All Fields for StudentResults are Required!!!",
                                Toast.LENGTH_LONG).show();
                    }
                    else {
                        //--------------------inserting the data to the database--------------------------

                        //getting access to the database by instantiating the subclass of SQLiteOpenHelper
                        RegisterUser.UserDetailsDbHelper dbHelper = new RegisterUser.UserDetailsDbHelper(getApplicationContext());

                        // Gets the data repository in write mode
                        SQLiteDatabase db = dbHelper.getWritableDatabase();

                        // Create a new map of values, where column names are the keys
                        ContentValues values = new ContentValues();
                        values.put(RegisterUser.UserDetailsContract.ResultsEntry.COLUMN_REGNO, ResultRno);
                        values.put(RegisterUser.UserDetailsContract.ResultsEntry.COLUMN_COURSEUNIT, ResultCourseUnit);
                        values.put(RegisterUser.UserDetailsContract.ResultsEntry.COLUMN_MARK1, ResultMark1);
                        values.put(RegisterUser.UserDetailsContract.ResultsEntry.COLUMN_MARK2, ResultMark2);
                        values.put(RegisterUser.UserDetailsContract.ResultsEntry.COLUMN_MARK3, ResultMark3);
                        values.put(RegisterUser.UserDetailsContract.ResultsEntry.COLUMN_FINALMARK, ResultFinalMark);



                        // Insert the new row, returning the primary key value of the new row
                        long newRowId = db.insert(RegisterUser.UserDetailsContract.ResultsEntry.TABLE_NAME, null, values);

                        Log.d("StudentData In my DB", "Student Mark: " + newRowId);

                        //show message of Lecturer Added
                        Toast.makeText(Student_result.this, "Student Results Added Successfully", Toast.LENGTH_LONG).show();

                    }
                }
                else{
                    Toast.makeText(Student_result.this, "You Are not Authorized to alter Student data", Toast.LENGTH_LONG).show();

                }
//                end of checking HOD or Lecturer

            }
        });
        //button View
        Button btn4 = findViewById(R.id.button4);

        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toCourseUnitPage = new Intent(Student_result.this, ViewResults.class);
                startActivity(toCourseUnitPage);
            }
        });

        //button Edit

        Button button2 = findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TheFetchedLoggedUser.equals("HOD")){
                    String ResultRno, ResultCourseUnit, ResultMark1, ResultMark2, ResultMark3, ResultFinalMark;
                    ResultRno = resultReg.getText().toString();
                    ResultCourseUnit = resultCourseU.getText().toString();
                    ResultMark1 = resultM1.getText().toString();
                    ResultMark2 = resultM2.getText().toString();
                    ResultMark3 = resultM3.getText().toString();
                    ResultFinalMark = resultFinal.getText().toString();
                    if (resultReg.getText().toString().trim().length() <= 0) {
                        Toast.makeText(Student_result.this, "RegNo is required for studentResult Edit Record!!",
                                Toast.LENGTH_LONG).show();
                    }
                    else{
                        //Editing Student logic

                        RegisterUser.UserDetailsDbHelper dbHelper = new RegisterUser.UserDetailsDbHelper(getApplicationContext());

                        // Gets the data repository in write mode
                        SQLiteDatabase db = dbHelper.getWritableDatabase();

// New value for Multiple columns

                        ContentValues values = new ContentValues();
                        values.put(RegisterUser.UserDetailsContract.ResultsEntry.COLUMN_REGNO, ResultRno);
                        values.put(RegisterUser.UserDetailsContract.ResultsEntry.COLUMN_COURSEUNIT, ResultCourseUnit);
                        values.put(RegisterUser.UserDetailsContract.ResultsEntry.COLUMN_MARK1, ResultMark1);
                        values.put(RegisterUser.UserDetailsContract.ResultsEntry.COLUMN_MARK2, ResultMark2);
                        values.put(RegisterUser.UserDetailsContract.ResultsEntry.COLUMN_MARK3, ResultMark3);
                        values.put(RegisterUser.UserDetailsContract.ResultsEntry.COLUMN_FINALMARK, ResultFinalMark);

// Which row to update, based on the title
                        String selection = RegisterUser.UserDetailsContract.ResultsEntry.COLUMN_REGNO + " LIKE ?";
                        String[] selectionArgs = { ResultRno };
                        if (ResultRno.trim().length() <= 0
                                || resultCourseU.getText().toString().length() <= 0
                                || resultM1.getText().toString().length() <= 0
                                || resultM2.getText().toString().length() <= 0
                                || resultM3.getText().toString().length() <= 0
                                || resultFinal.getText().toString().length() <= 0) {

                            Toast.makeText(Student_result.this, "All Fields Are Required For Editing!!!",
                                    Toast.LENGTH_LONG).show();
                        }
                        else {
                            try {

                                int count = db.update(
                                        RegisterUser.UserDetailsContract.ResultsEntry.TABLE_NAME,
                                        values,
                                        selection,
                                        selectionArgs);
                                if (count > 0 ){
                                    Toast.makeText(Student_result.this, ResultRno+" Edited Successfully",
                                            Toast.LENGTH_LONG).show();
                                }
                                else{
                                    Toast.makeText(Student_result.this, ResultRno+" StudentResults Not Edited, May Not Be Found",
                                            Toast.LENGTH_LONG).show();
                                }

                            }
                            catch (Exception e){
                                Toast.makeText(Student_result.this, e.getMessage().toString()+"---Error Editing Student Results",
                                        Toast.LENGTH_LONG).show();
                            }

                        }
                    }
                }
                else if(TheFetchedLoggedUser.equals("Lecturer")){

                    String ResultRno, ResultCourseUnit, ResultMark1, ResultMark2, ResultMark3, ResultFinalMark;
                    ResultRno = resultReg.getText().toString();
                    ResultCourseUnit = theLecturerCourseUnit;
                    ResultMark1 = resultM1.getText().toString();
                    ResultMark2 = resultM2.getText().toString();
                    ResultMark3 = resultM3.getText().toString();
                    ResultFinalMark = resultFinal.getText().toString();
                    if (resultReg.getText().toString().trim().length() <= 0) {
                        Toast.makeText(Student_result.this, "RegNo is required for studentResult Edit Record!!",
                                Toast.LENGTH_LONG).show();
                    }
                    else{
                        //Editing Student logic

                        RegisterUser.UserDetailsDbHelper dbHelper = new RegisterUser.UserDetailsDbHelper(getApplicationContext());

                        // Gets the data repository in write mode
                        SQLiteDatabase db = dbHelper.getWritableDatabase();

// New value for Multiple columns

                        ContentValues values = new ContentValues();
                        values.put(RegisterUser.UserDetailsContract.ResultsEntry.COLUMN_REGNO, ResultRno);
                        values.put(RegisterUser.UserDetailsContract.ResultsEntry.COLUMN_COURSEUNIT, ResultCourseUnit);
                        values.put(RegisterUser.UserDetailsContract.ResultsEntry.COLUMN_MARK1, ResultMark1);
                        values.put(RegisterUser.UserDetailsContract.ResultsEntry.COLUMN_MARK2, ResultMark2);
                        values.put(RegisterUser.UserDetailsContract.ResultsEntry.COLUMN_MARK3, ResultMark3);
                        values.put(RegisterUser.UserDetailsContract.ResultsEntry.COLUMN_FINALMARK, ResultFinalMark);

// Which row to update, based on the title
                        String selection = RegisterUser.UserDetailsContract.UserEntry.COLUMN_REGNO + " LIKE ?";
                        String[] selectionArgs = { ResultRno };
                        if (ResultRno.trim().length() <= 0
                                || resultCourseU.getText().toString().length() <= 0
                                || resultM1.getText().toString().length() <= 0
                                || resultM2.getText().toString().length() <= 0
                                || resultM3.getText().toString().length() <= 0
                                || resultFinal.getText().toString().length() <= 0) {

                            Toast.makeText(Student_result.this, "All Fields Are Required For Editing!!!",
                                    Toast.LENGTH_LONG).show();
                        }
                        else {
                            try {

                                int count = db.update(
                                        RegisterUser.UserDetailsContract.UserEntry.TABLE_NAME,
                                        values,
                                        selection,
                                        selectionArgs);
                                if (count > 0 ){
                                    Toast.makeText(Student_result.this, ResultRno+" Edited Successfully",
                                            Toast.LENGTH_LONG).show();
                                }
                                else{
                                    Toast.makeText(Student_result.this, ResultRno+" StudentResults Not Found",
                                            Toast.LENGTH_LONG).show();
                                }

                            }
                            catch (Exception e){
                                Toast.makeText(Student_result.this, e.getMessage().toString()+"Error Editing Student Results",
                                        Toast.LENGTH_LONG).show();
                            }

                        }
                    }
                }
                else{
                    Toast.makeText(Student_result.this, "Editing Other Staff",
                            Toast.LENGTH_LONG).show();
                }

            }
        });

////button Delete

        Button button3 = findViewById(R.id.manageResultBtn);

        button3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String RRNo;
                RRNo = resultReg.getText().toString();
                if (RRNo.trim().length() <= 0){
                    Toast.makeText(Student_result.this, "Registration Number is Required to Delete!!",
                            Toast.LENGTH_LONG).show();
                }
                else{
                    //getting access to the database by instantiating the subclass of SQLiteOpenHelper
                    RegisterUser.UserDetailsDbHelper dbHelper = new RegisterUser.UserDetailsDbHelper(getApplicationContext());

                    // Gets the data repository in write mode
                    SQLiteDatabase db = dbHelper.getWritableDatabase();

                    // Define 'where' part of query.
                    String selection = RegisterUser.UserDetailsContract.ResultsEntry.COLUMN_REGNO + " LIKE ?";
                    // Specify arguments in placeholder order.
                    String[] selectionArgs = { RRNo };
                    // Issue SQL statement.
                    try {
                        int deletedRows = db.delete(RegisterUser.UserDetailsContract.ResultsEntry.TABLE_NAME, selection, selectionArgs);
                        if (deletedRows > 0 ){
                            Toast.makeText(Student_result.this, RRNo+" Deleted Successfully",
                                    Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(Student_result.this, RRNo+" User Not Deleted",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                    catch (Exception e){
                        Toast.makeText(Student_result.this, e.getMessage().toString()+"---Error Deleting User",
                                Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

    }
}