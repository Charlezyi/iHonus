package com.example.joeytayyiqin.orbital;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class EditNoteActivity extends AppCompatActivity{
    private EditText mEditTextTitle;
    //private EditText mEditTextMessage;
    private String noteId;
    private Button mBtnDel;
    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);


        // Initialise Widgets
        mEditTextTitle = findViewById(R.id.editTextTitle);
       // mEditTextMessage = findViewById(R.id.editTextMessage);
        mDisplayDate = (TextView) findViewById(R.id.tvDate);
        mBtnDel = findViewById(R.id.buttonDelete);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        EditNoteActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month + 1;

                String date = month + "/" + day + "/" + year;
                mDisplayDate.setText(date);
            }
        };

        // Get Note from bundle
        Bundle b = this.getIntent().getExtras();
        if (b != null){
            Note note = b.getParcelable("note");
            mEditTextTitle.setText(note.getTitle());
            mDisplayDate.setText(note.getMessage());
            //mEditTextMessage.setText(note.getMessage());
            noteId = note.getId();



        } else {
            new NullPointerException();
        }

        mBtnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteNote();
                backToMainActivity();
            }
        });
    }



    private void deleteNote() {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("notes");
        mDatabase.child(noteId).removeValue();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.layout.menu_add_note, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onSupportNavigateUp() {
        backToMainActivity();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addnote:
                // Add Note to FireBase
                updateNote();
                // Back to MainActivity
                backToMainActivity();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void updateNote() {
        String title = mEditTextTitle.getText().toString().trim();
        String message = mDisplayDate.getText().toString().trim();
        // Write a message to the database
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("notes");
        String noteId = mDatabase.push().getKey();
        // Note object to store title and message
        Note note = new Note();
        note.setTitle(title);
        note.setMessage(message);
        note.setId(noteId);
        mDatabase.child(noteId).setValue(note);
        Toast.makeText(EditNoteActivity.this, "Note Added!", Toast.LENGTH_SHORT).show();
    }

    private void backToMainActivity() {
        Intent intent = new Intent(EditNoteActivity.this, HallEvent.class);
        startActivity(intent);
        finish();
    }

}


