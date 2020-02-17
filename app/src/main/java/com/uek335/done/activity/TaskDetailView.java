package com.uek335.done.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.uek335.done.MainActivity;
import com.uek335.done.R;
import com.uek335.done.activity.helper.DatePickerUtil;
import com.uek335.done.model.AppDatabase;
import com.uek335.done.model.Task;

import java.util.Date;

public class TaskDetailView extends AppCompatActivity {

    FloatingActionButton btn;
    int currentTaskId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_view_task);

        /* Get Task ID */
        currentTaskId = getIntent().getIntExtra("TaskId", 1);

        /* Populate view with Data */
        populateView(currentTaskId);

        /* Add action to Fab */
        btn = findViewById(R.id.btnEditTask);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent showEditTaskView = new Intent(TaskDetailView.this, EditTaskView.class);
                showEditTaskView.putExtra("TaskId", currentTaskId);
                TaskDetailView.this.startActivity(showEditTaskView);
            }
        });
    }

    /**
     * Populate View with Data from DB
     *
     * @param taskId Id of task to get from DB
     */
    private void populateView(int taskId) {
        Task actualTask = AppDatabase.getAppDatabase(getApplicationContext()).taskDao().getTask(taskId);

        /* Add Back button to ActionBar */
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView txtCategory = (TextView) findViewById(R.id.txtCategory);
        TextView txtDescription = (TextView) findViewById(R.id.txtDescription);
        TextView txtTitle = (TextView) findViewById(R.id.txtTitle);
        TextView datepicker = (TextView) findViewById(R.id.datepicker);

        txtCategory.setText(actualTask.getCategoryString());
        txtDescription.setText(actualTask.getContent());
        txtTitle.setText(actualTask.getTitle());

        Date endDate = actualTask.getEndDate();
        if (endDate != null) {
            datepicker.setText(DatePickerUtil.getSdf().format(endDate));
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Intent returnIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(returnIntent, 0);
        return true;
    }
}
