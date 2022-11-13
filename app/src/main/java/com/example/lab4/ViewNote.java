package com.example.lab4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

public class ViewNote extends AppCompatActivity {

    Toolbar toolbarView;
    EditText nTitle, nDetails;
    Calendar c;
    String todaysDate;
    String currentTime;
    String noteId;
    NoteDatabase db = new NoteDatabase(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Note note;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_note);
        toolbarView = findViewById(R.id.toolbarView);
        toolbarView.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbarView);

        nTitle = findViewById(R.id.viewTitle);
        nDetails = findViewById(R.id.viewDetails);
        noteId = getIntent().getStringExtra("id");
        note = db.getNote(Long.valueOf(noteId));

        nTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() != 0) {
                    getSupportActionBar().setTitle(s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        nTitle.setText(note.getTitle());
        nDetails.setText(note.getContent());

        getSupportActionBar().setTitle(note.getTitle());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        c = Calendar.getInstance();
        todaysDate = c.get(Calendar.YEAR)+"/"+(c.get(Calendar.MONTH)+1)+"/"+c.get(Calendar.DAY_OF_MONTH);
        currentTime = pad(c.get(Calendar.HOUR))+":"+pad(c.get(Calendar.MINUTE));
    }

    private String pad(int i) {
        if (i<10)
            return "0"+i;
        return String.valueOf(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.delete) {
            db.deleteNote(Long.valueOf(noteId));
            gotToMain();
        }
        if(item.getItemId() == R.id.save) {
            String title, details;
            title = nTitle.getText().toString();
            details = nDetails.getText().toString();

            if (db.updateNote(Long.valueOf(noteId), title, details, todaysDate, currentTime) != -1){
                gotToMain();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void gotToMain() {
        Intent i = new Intent(this,MainActivity.class);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}