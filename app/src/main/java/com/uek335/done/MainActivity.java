package com.uek335.done;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.room.Room;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.uek335.done.activity.CreateTaskView;
import com.uek335.done.activity.TaskDetailView;
import com.uek335.done.model.AppDatabase;
import com.uek335.done.model.Task;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    AppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton testBtnforDeitail;

        // Create CreateView
        FloatingActionButton addTaskButton = findViewById(R.id.addTask);
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent showCreateTaskView = new Intent(MainActivity.this, CreateTaskView.class);
                // parameters ==> showCreateTaskView.putExtra("key", param);
                MainActivity.this.startActivity(showCreateTaskView);
            }
        });

        // Create DetailView
        FloatingActionButton addTaskTaskDetailView = findViewById(R.id.addTask3);
        addTaskTaskDetailView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent showTaskDetailView = new Intent(MainActivity.this, TaskDetailView.class);
                int TaskID = getTaskId();
                showTaskDetailView.putExtra("taskId", TaskID);
                MainActivity.this.startActivity(showTaskDetailView);
            }
        });

        /**
         * Initialize Database
         */
        database = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "task_databasa")
                .allowMainThreadQueries()
                .build();

        /* Get all tasks from db */
        List<Task> tasks = database.taskDao().getAllTasks();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        AppDatabase.destroyInstance();
        super.onDestroy();
    }
}
