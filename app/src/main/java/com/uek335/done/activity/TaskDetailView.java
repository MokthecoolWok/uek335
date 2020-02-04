package com.uek335.done.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.uek335.done.R;

public class TaskDetailView extends AppCompatActivity {

    TextView category;
    FloatingActionButton btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_view_task);

        category = findViewById(R.id.txt_category);
        btn = findViewById(R.id.btnEditTask);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent showEditTaskView = new Intent(TaskDetailView.this, EditTaskView.class);
                // parameters ==> showCreateTaskView.putExtra("key", param);
                TaskDetailView.this.startActivity(showEditTaskView);
            }
        });
    }
}
