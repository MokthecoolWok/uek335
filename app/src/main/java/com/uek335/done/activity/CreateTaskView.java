package com.uek335.done.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.uek335.done.MainActivity;
import com.uek335.done.R;
import com.uek335.done.model.AppDatabase;
import com.uek335.done.model.Task;

public class CreateTaskView extends AppCompatActivity {

    AppDatabase database;
    FloatingActionButton createTaskInDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task_view);


        // get Back button
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // add Database context
        database = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "task_databasa")
                .allowMainThreadQueries()
                .build();

        // add action to Fab
        createTaskInDb = findViewById(R.id.createNewTask);
        createTaskInDb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createTask();
            }
        });
    }

    private void createTask() {
        EditText titleView = findViewById(R.id.txtTitle);

        // check if title is set
        if(titleView.getText().toString() == ""){
            // post new entry to db
            database.taskDao().insertTask(new Task() {
                {
                    setTitle("Title");
                    setContent("Content");
                    setCategory(1);
                }
            });
        } else {
            // throw alert dialog here
            
        }
    }

    public boolean onOptionsItemSelected(MenuItem item){
        Intent returnIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(returnIntent, 0);
        return true;
    }
}
