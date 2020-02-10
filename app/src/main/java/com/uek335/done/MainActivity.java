package com.uek335.done;

import android.content.Context;
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
import com.uek335.done.model.AppDatabase;
import com.uek335.done.model.Task;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    AppDatabase database;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        //Toolbar and Floatingbar to add Task
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //FloatingActionButton addTaskButton = findViewById(R.id.addTask);
        /*addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent showCreateTaskView = new Intent(MainActivity.this, CreateTaskView.class);
                // parameters ==> showCreateTaskView.putExtra("key", param);
                MainActivity.this.startActivity(showCreateTaskView);
            }
        });*/
        ListView lstview=(ListView)findViewById(R.id.listv);
        lstview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(context, "An item of the ListView is clicked", Toast.LENGTH_LONG).show();
            }
        });
        //Initialize Database
        database = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "task_databasa")
                .allowMainThreadQueries()
                .build();

        // Get all tasks from db
        String[] items = database.taskDao().getTitelFromTasks();
        LstViewAdapter adapter = new LstViewAdapter(this,R.layout.list_item, R.id.txt, items);
        lstview.setAdapter(adapter);
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

    public void clickMe(View view){
        Button bt=(Button)view;
        Toast.makeText(this,"Clicked" + bt.getText().toString(), Toast.LENGTH_LONG).show();
    }
}
