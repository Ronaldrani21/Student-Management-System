package com.test.studentmanagementsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterUser extends AppCompatActivity {

    String[] registerItems = {"Student","Lecturer"};
    String RegAs;
    AutoCompleteTextView autoCompleteTxtReg2;

    ArrayAdapter<String> adapterRegItems;

    //My Database
    public final class UserDetailsContract {
        // To prevent someone from accidentally instantiating the contract class,
        // make the constructor private.
        public UserDetailsContract() {}

        /* Inner class that defines the table contents */
        public class UserEntry implements BaseColumns {
            public static final String TABLE_NAME = "users";
            public static final String COLUMN_USER = "user";
            public static final String COLUMN_REGNO = "regno";
            public static final String COLUMN_NAME = "name";
            public static final String COLUMN_EMAIL = "email";
            public static final String COLUMN_PASSWORD = "password";
            public static final String COLUMN_COURSEUNIT = "courseunit";

        }
        public class ResultsEntry implements BaseColumns {
            public static final String TABLE_NAME = "studentResults";
            public static final String COLUMN_REGNO = "resultregno";
            public static final String COLUMN_COURSEUNIT = "resultcourseunit";
            public static final String COLUMN_MARK1 = "resultmark1";
            public static final String COLUMN_MARK2 = "resultmark2";
            public static final String COLUMN_MARK3 = "resultmark3";
            public static final String COLUMN_FINALMARK = "resultfinalmark";

        }
    }
    //Data for table 2 for Student results
    private static final String SQL_CREATE_ENTRIES2 =
            "CREATE TABLE " + UserDetailsContract.ResultsEntry.TABLE_NAME + " (" +
                    UserDetailsContract.ResultsEntry._ID + " INTEGER PRIMARY KEY," +
                    UserDetailsContract.ResultsEntry.COLUMN_REGNO + " TEXT," +
                    UserDetailsContract.ResultsEntry.COLUMN_COURSEUNIT + " TEXT," +
                    UserDetailsContract.ResultsEntry.COLUMN_MARK1 + " TEXT," +
                    UserDetailsContract.ResultsEntry.COLUMN_MARK2 + " TEXT," +
                    UserDetailsContract.ResultsEntry.COLUMN_MARK3 + " TEXT," +
                    UserDetailsContract.ResultsEntry.COLUMN_FINALMARK + " TEXT)";

    private static final String SQL_DELETE_ENTRIES2 =
            "DROP TABLE IF EXISTS " + UserDetailsContract.ResultsEntry.TABLE_NAME;
//end of Student results data

    //    Start user details table
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + UserDetailsContract.UserEntry.TABLE_NAME + " (" +
                    UserDetailsContract.UserEntry._ID + " INTEGER PRIMARY KEY," +
                    UserDetailsContract.UserEntry.COLUMN_USER + " TEXT," +
                    UserDetailsContract.UserEntry.COLUMN_REGNO + " TEXT," +
                    UserDetailsContract.UserEntry.COLUMN_NAME + " TEXT," +
                    UserDetailsContract.UserEntry.COLUMN_EMAIL + " TEXT," +
                    UserDetailsContract.UserEntry.COLUMN_PASSWORD + " TEXT," +
                    UserDetailsContract.UserEntry.COLUMN_COURSEUNIT + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + UserDetailsContract.UserEntry.TABLE_NAME;
//end of user details table

    public static class UserDetailsDbHelper extends SQLiteOpenHelper {
        // If you change the database schema, you must increment the database version.
        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "UserDetails.db";

        public UserDetailsDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        public void onCreate(SQLiteDatabase db) {

            db.execSQL(SQL_CREATE_ENTRIES);
            db.execSQL(SQL_CREATE_ENTRIES2);
        }
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // This database is only a cache for online data, so its upgrade policy is
            // to simply to discard the data and start over
            db.execSQL(SQL_DELETE_ENTRIES);
            db.execSQL(SQL_DELETE_ENTRIES2);

            onCreate(db);
        }

    }

    EditText regno;
    EditText name ;
    EditText email;
    EditText password;
    EditText confirmpassword;
    EditText courseunit;

//    TextView loggeduser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registeruser);

        regno = findViewById(R.id.regnoUser);
        name = findViewById(R.id.RegName);
        email = findViewById(R.id.RegEmail);
        password = findViewById(R.id.RegPass1);
        confirmpassword = findViewById(R.id.RegPass);
        courseunit = findViewById(R.id.RegCourseUnit);

//        Printing our shared Preference
        SharedPreferences sp = getSharedPreferences("MyLoginUserSharePref", Context.MODE_PRIVATE);
        String TheFetchedLoggedUser = sp.getString("loggedUser", "defaultValue");

        Toast.makeText(RegisterUser.this, "Shared Pref User is :" + TheFetchedLoggedUser, Toast.LENGTH_SHORT).show();
//          End of shared Preference

        autoCompleteTxtReg2 = findViewById(R.id.auto_complete_txt_register);

        adapterRegItems = new ArrayAdapter<String>(this, R.layout.list, registerItems);

        autoCompleteTxtReg2.setAdapter(adapterRegItems);

        autoCompleteTxtReg2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RegAs = parent.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(), "Registering : " + RegAs, Toast.LENGTH_LONG).show();
            }
        });

//Button to Register
        Button RegisterBtn = findViewById(R.id.RegisterBtn);

        RegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String RRno, RRname, RRemail, RRpass, RRpass2, RRCU;

                RRno = regno.getText().toString();
                RRname = name.getText().toString();
                RRemail = email.getText().toString();
                RRpass = password.getText().toString();
                RRpass2 = confirmpassword.getText().toString();
                RRCU = courseunit.getText().toString();


                if (RegAs == "Student") {
                    if (regno.getText().toString().trim().length() <= 0
                            || name.getText().toString().length() <= 0
                            || email.getText().toString().length() <= 0
                            || password.getText().toString().length() <= 0
                            || confirmpassword.getText().toString().length() <= 0) {
                        //validate password and confirm password
                        Toast.makeText(RegisterUser.this, "Enter all student Fields!!!",
                                Toast.LENGTH_LONG).show();
                    } else {
                        //--------------------inserting the data to the database--------------------------

                        //getting access to the database by instantiating the subclass of SQLiteOpenHelper
                        UserDetailsDbHelper dbHelper = new UserDetailsDbHelper(getApplicationContext());

                        // Gets the data repository in write mode
                        SQLiteDatabase db = dbHelper.getWritableDatabase();

                        // Create a new map of values, where column names are the keys
                        ContentValues values = new ContentValues();
                        values.put(UserDetailsContract.UserEntry.COLUMN_USER, RegAs);
                        values.put(UserDetailsContract.UserEntry.COLUMN_REGNO, RRno);
                        values.put(UserDetailsContract.UserEntry.COLUMN_NAME, RRname);
                        values.put(UserDetailsContract.UserEntry.COLUMN_EMAIL, RRemail);
                        values.put(UserDetailsContract.UserEntry.COLUMN_PASSWORD, RRpass);


                        // Insert the new row, returning the primary key value of the new row
                        long newRowId = db.insert(UserDetailsContract.UserEntry.TABLE_NAME, null, values);

                        Log.d("Data In my Database", "UserStudent: " + newRowId);

                        //show message of Lecturer Added
                        Toast.makeText(RegisterUser.this, "Student Added!", Toast.LENGTH_LONG).show();

                    }
                } else if (RegAs == "Lecturer") {
                    if (name.getText().toString().length() <= 0
                            || email.getText().toString().length() <= 0
                            || password.getText().toString().length() <= 0
                            || confirmpassword.getText().toString().length() <= 0
                            || courseunit.getText().toString().length() <= 0) {
                        //validation
                        Toast.makeText(RegisterUser.this, "Enter all Lecturer Fields!!!",
                                Toast.LENGTH_LONG).show();
                    } else {
//--------------------inserting the data to the database--------------------------

                        //getting access to the database by instantiating the subclass of SQLiteOpenHelper
                        UserDetailsDbHelper dbHelper = new UserDetailsDbHelper(getApplicationContext());

                        // Gets the data repository in write mode
                        SQLiteDatabase db = dbHelper.getWritableDatabase();

                        // Create a new map of values, where column names are the keys
                        ContentValues values = new ContentValues();
                        values.put(UserDetailsContract.UserEntry.COLUMN_USER, RegAs);
                        values.put(UserDetailsContract.UserEntry.COLUMN_NAME, RRname);
                        values.put(UserDetailsContract.UserEntry.COLUMN_EMAIL, RRemail);
                        values.put(UserDetailsContract.UserEntry.COLUMN_PASSWORD, RRpass);
                        values.put(UserDetailsContract.UserEntry.COLUMN_COURSEUNIT, RRCU);


                        // Insert the new row, returning the primary key value of the new row
                        long newRowId = db.insert(UserDetailsContract.UserEntry.TABLE_NAME, null, values);

                        Log.d("Data In my Database", "UserLecturer: " + newRowId);

                        //show message of Lecturer Added
                        Toast.makeText(RegisterUser.this, "Lecturer Added Successfully", Toast.LENGTH_LONG).show();

                    }
                } else {
                    Toast.makeText(RegisterUser.this, "Select user!",
                            Toast.LENGTH_LONG).show();
                }


            }
        });
//button View
        Button Regbtn1 = findViewById(R.id.RegView);

        Regbtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toCourseUnitPage = new Intent(RegisterUser.this, ViewUsers.class);
                startActivity(toCourseUnitPage);
            }
        });

        //button Edit

        Button EditBtn = findViewById(R.id.RegEdit);

        EditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TheFetchedLoggedUser.equals("HOD")){
                    String RRno, RRname, RRemail, RRpass, RRpass2, RRCU;
                    RRno = regno.getText().toString();
                    RRname = name.getText().toString();
                    RRemail = email.getText().toString();
                    RRpass = password.getText().toString();
                    RRpass2 = confirmpassword.getText().toString();
                    RRCU = courseunit.getText().toString();
                    if (regno.getText().toString().trim().length() <= 0) {
                        Toast.makeText(RegisterUser.this, "RegNo is required for studentResult Edit Record!!",
                                Toast.LENGTH_LONG).show();
                    }
                    else{
                        //Editing Student logic

                        RegisterUser.UserDetailsDbHelper dbHelper = new RegisterUser.UserDetailsDbHelper(getApplicationContext());

                        // Gets the data repository in write mode
                        SQLiteDatabase db = dbHelper.getWritableDatabase();

// New value for Multiple columns

                        ContentValues values = new ContentValues();
                        values.put(UserDetailsContract.UserEntry.COLUMN_USER, RegAs);
                        values.put(UserDetailsContract.UserEntry.COLUMN_REGNO, RRno);
                        values.put(UserDetailsContract.UserEntry.COLUMN_NAME, RRname);
                        values.put(UserDetailsContract.UserEntry.COLUMN_EMAIL, RRemail);
                        values.put(UserDetailsContract.UserEntry.COLUMN_PASSWORD, RRpass);

// Which row to update, based on the title
                        String selection = RegisterUser.UserDetailsContract.UserEntry.COLUMN_REGNO + " LIKE ?";
                        String[] selectionArgs = { RRno };
                        if (regno.getText().toString().trim().length() <= 0
                                || name.getText().toString().length() <= 0
                                || email.getText().toString().length() <= 0
                                || password.getText().toString().length() <= 0
                                || confirmpassword.getText().toString().length() <= 0) {

                            Toast.makeText(RegisterUser.this, "All Fields Are Required For Editing!!!",
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
                                    Toast.makeText(RegisterUser.this, RRno+" Edited Successfully",
                                            Toast.LENGTH_LONG).show();
                                }
                                else{
                                    Toast.makeText(RegisterUser.this, RRno+" StudentResults Not Edited, May Not Be Found",
                                            Toast.LENGTH_LONG).show();
                                }

                            }
                            catch (Exception e){
                                Toast.makeText(RegisterUser.this, e.getMessage().toString()+"---Error Editing Student Results",
                                        Toast.LENGTH_LONG).show();
                            }

                        }
                    }
                }
                else if(TheFetchedLoggedUser.equals("Lecturer")){
                    String RRno, RRname, RRemail, RRpass, RRpass2, RRCU;
                    RRno = regno.getText().toString();
                    RRname = name.getText().toString();
                    RRemail = email.getText().toString();
                    RRpass = password.getText().toString();
                    RRpass2 = confirmpassword.getText().toString();
                    RRCU = courseunit.getText().toString();
                    if (regno.getText().toString().trim().length() <= 0) {
                        Toast.makeText(RegisterUser.this, "RegNo is required for studentResult Edit Record!!",
                                Toast.LENGTH_LONG).show();
                    }
                    else{
                        //Editing Student logic

                        RegisterUser.UserDetailsDbHelper dbHelper = new RegisterUser.UserDetailsDbHelper(getApplicationContext());

                        // Gets the data repository in write mode
                        SQLiteDatabase db = dbHelper.getWritableDatabase();

// New value for Multiple columns
                        ContentValues values = new ContentValues();
                        values.put(UserDetailsContract.UserEntry.COLUMN_USER, RegAs);
                        values.put(UserDetailsContract.UserEntry.COLUMN_NAME, RRname);
                        values.put(UserDetailsContract.UserEntry.COLUMN_EMAIL, RRemail);
                        values.put(UserDetailsContract.UserEntry.COLUMN_PASSWORD, RRpass);
                        values.put(UserDetailsContract.UserEntry.COLUMN_COURSEUNIT, RRCU);

// Which row to update, based on the title
                        String selection = RegisterUser.UserDetailsContract.UserEntry.COLUMN_REGNO + " LIKE ?";
                        String[] selectionArgs = { RRno };
                        if (regno.getText().toString().trim().length() <= 0
                                || name.getText().toString().length() <= 0
                                || email.getText().toString().length() <= 0
                                || password.getText().toString().length() <= 0
                                || confirmpassword.getText().toString().length() <= 0) {

                            Toast.makeText(RegisterUser.this, "All Fields Are Required For Editing!!!",
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
                                    Toast.makeText(RegisterUser.this, RRno+" Edited Successfully",
                                            Toast.LENGTH_LONG).show();
                                }
                                else{
                                    Toast.makeText(RegisterUser.this, RRno+" StudentResults Not Edited, May Not Be Found",
                                            Toast.LENGTH_LONG).show();
                                }

                            }
                            catch (Exception e){
                                Toast.makeText(RegisterUser.this, e.getMessage().toString()+"---Error Editing Student Results",
                                        Toast.LENGTH_LONG).show();
                            }

                        }
                    }
                }
                else{
                    Toast.makeText(RegisterUser.this, "Editing Other Staff",
                            Toast.LENGTH_LONG).show();
                }

            }
        });

//button Delete

        Button DelBtn = findViewById(R.id.RegDelete);

        DelBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String  RegName;


                RegName = name.getText().toString();
                if (RegName.trim().length() <= 0){
                    Toast.makeText(RegisterUser.this, "Name field is Required to Delete!!",
                            Toast.LENGTH_LONG).show();
                }
                else{
                    //getting access to the database by instantiating the subclass of SQLiteOpenHelper
                    RegisterUser.UserDetailsDbHelper dbHelper = new RegisterUser.UserDetailsDbHelper(getApplicationContext());

                    // Gets the data repository in write mode
                    SQLiteDatabase db = dbHelper.getWritableDatabase();

                    // Define 'where' part of query.
                    String selection = UserDetailsContract.UserEntry.COLUMN_NAME + " LIKE ?";
                    // Specify arguments in placeholder order.
                    String[] selectionArgs = { RegName };
                    // Issue SQL statement.
                    try {
                        int deletedRows = db.delete(RegisterUser.UserDetailsContract.UserEntry.TABLE_NAME, selection, selectionArgs);
                        if (deletedRows > 0 ){
                            Toast.makeText(RegisterUser.this, RegName+" Deleted Successfully",
                                    Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(RegisterUser.this, RegName+" User Not Deleted May Not Be Found",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                    catch (Exception e){
                        Toast.makeText(RegisterUser.this, e.getMessage().toString()+"---Error Deleting User",
                                Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

    }
}
