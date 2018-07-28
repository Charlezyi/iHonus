package com.example.joeytayyiqin.orbital;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditAnnounce extends AppCompatActivity {
    private EditText mEditTextTitle;
    private EditText mEditTextMessage;
    private String noteId;
    private Button mBtnDel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_announce);

        // Initialise Widgets
        mEditTextTitle = findViewById(R.id.editTextTitle);
        mEditTextMessage = findViewById(R.id.editTextMessage);
        mBtnDel = findViewById(R.id.buttonDelete);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Get Note from bundle
        Bundle b = this.getIntent().getExtras();
        if (b != null) {
            Note note = b.getParcelable("note");
            mEditTextTitle.setText(note.getTitle());
            mEditTextMessage.setText(note.getMessage());
            noteId = note.getId();
        } else {
            new NullPointerException();
        }

     mBtnDel.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            deleteNote();
        }
    });
}
    private void deleteNote() {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("announcements");
        mDatabase.child(noteId).removeValue();
        Toast.makeText(this, "Announcement deleted", Toast.LENGTH_SHORT).show();
        finish();
        backToMainActivity();
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
        String message = mEditTextMessage.getText().toString().trim();
        // Write a message to the database
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("announcements");
        String noteId = mDatabase.push().getKey();
        // Note object to store title and message
        Note note = new Note();
        note.setTitle(title);
        note.setMessage(message);
        note.setId(noteId);
        mDatabase.child(noteId).setValue(note);
        Toast.makeText(EditAnnounce.this, "Announcements added!", Toast.LENGTH_SHORT).show();
    }

    private void backToMainActivity() {
        Intent intent = new Intent(EditAnnounce.this, HallMain.class);
        startActivity(intent);
        finish();
    }

}

