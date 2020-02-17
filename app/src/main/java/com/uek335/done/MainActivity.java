package com.uek335.done;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.uek335.done.activity.CreateTaskView;
import com.uek335.done.activity.TaskDetailView;
import com.uek335.done.model.AppDatabase;
import com.uek335.done.model.Task;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Toolbar and Fab to add Task */
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton addTaskButton = findViewById(R.id.addTask);
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent showCreateTaskView = new Intent(MainActivity.this, CreateTaskView.class);
                MainActivity.this.startActivity(showCreateTaskView);
            }
        });

        /* Populate Start Screen with data */
        showAllTasks();
    }

    /**
     * Get and show all tasks from DB
     */
    private void showAllTasks() {
        ListView listView = (ListView) findViewById(R.id.listv);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LinearLayout tV = (LinearLayout) view;
                int taskId = tV.getId();
                Intent showEditTaskView = new Intent(MainActivity.this, TaskDetailView.class);
                showEditTaskView.putExtra("TaskId", taskId);
                MainActivity.this.startActivity(showEditTaskView);
            }
        });
        Task[] tasks = AppDatabase.getAppDatabase(getApplicationContext()).taskDao().getAllTasks();
        String[] titel = AppDatabase.getAppDatabase(getApplicationContext()).taskDao().getTitelFromTasks();
        LstViewAdapter adapter = new LstViewAdapter(this, R.layout.list_item, R.id.txt, tasks, titel);
        listView.setAdapter(adapter);
    }

    @Override
    public void onDestroy() {
        AppDatabase.destroyInstance();
        super.onDestroy();
    }

    /**
     * Function to delete Task
     *
     * @param view which Task-View to delete
     */
    public void deleteTask(View view) {
        Button bt = (Button) view;
        int taskId = bt.getId();
        Task[] tasks = AppDatabase.getAppDatabase(getApplicationContext()).taskDao().getAllTasks();
        for (int x = 0; x < tasks.length; x++) {
            if (tasks[x].getId() == taskId) {
                AppDatabase.getAppDatabase(getApplicationContext()).taskDao().deleteTask(tasks[x]);
            }
        }
        finish();
        startActivity(getIntent());
    }
}
