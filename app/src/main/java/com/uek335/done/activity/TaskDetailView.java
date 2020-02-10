package com.uek335.done.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.uek335.done.MainActivity;
import com.uek335.done.R;
import com.uek335.done.model.AppDatabase;
import com.uek335.done.model.Task;

public class TaskDetailView extends AppCompatActivity {

    FloatingActionButton btn;
    AppDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        int taskId = getTaskId();

        // add Database context
        database = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "task_databasa")
                .allowMainThreadQueries()
                .build();

        Task actualTask = database.taskDao().getTask(taskId);

        // get Back button
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_view_task);

        TextView txtCategory = (TextView)findViewById(R.id.txtCategory);
        TextView txtDescription = (TextView)findViewById(R.id.txtDescription);
        TextView txtTitle = (TextView)findViewById(R.id.txtTitle);
        //TextView datepicker = (TextView)findViewById(R.id.datepicker);

        txtCategory.setText(actualTask.getCategory());
        txtDescription.setText(actualTask.getContent());
        txtTitle.setText(actualTask.getTitle());
        // datepicker.setText(actualTask.);


        btn = findViewById(R.id.btnEditTask);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent showEditTaskView = new Intent(TaskDetailView.this, EditTaskView.class);
                int TaskID = getTaskId();
                showEditTaskView.putExtra("taskId", TaskID);
                TaskDetailView.this.startActivity(showEditTaskView);
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item){
        Intent returnIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(returnIntent, 0);
        return true;
    }
}
