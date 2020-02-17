package com.uek335.done.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.uek335.done.R;
import com.uek335.done.model.AppDatabase;
import com.uek335.done.model.Task;

public class EditTaskView extends AppCompatActivity {

    AppDatabase database;
    FloatingActionButton createTaskInDb;
    int taskId = getTaskId();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task_view);



        // add Database context
        database = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "task_databasa")
                .allowMainThreadQueries()
                .build();

        Task actualTask = database.taskDao().getTask(taskId);

        // get Back button
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView txtCategory = (TextView)findViewById(R.id.txtCategory);
        TextView txtDescription = (TextView)findViewById(R.id.txtDescription);
        TextView txtTitle = (TextView)findViewById(R.id.txtTitle);
        TextView datepicker = (TextView)findViewById(R.id.datepicker);

        txtCategory.setText(actualTask.getCategory());
        txtDescription.setText(actualTask.getContent());
        txtTitle.setText(actualTask.getTitle());




        // get Back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_create_task_view);

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

    private void updateTask(){

        Task taskToUpdate;

        taskToUpdate = database.taskDao().getTask(taskId);

        taskToUpdate.setContent();
        taskToUpdate.setCategory();
        taskToUpdate.setTitle();
    }



    private void createTask() {
        EditText titleView = findViewById(R.id.txtTitle);

        // check if title is set
        if(titleView.getText().toString() != ""){
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
        Intent returnIntent = new Intent(getApplicationContext(), TaskDetailView.class);
        startActivityForResult(returnIntent, 0);
        return true;
    }
}
