package com.uek335.done.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.Date;
import java.util.List;

@Dao
public interface TaskDao {
    @Query("SELECT * FROM tasks")
    Task[] getAllTasks();

    @Query("Select title FROM tasks")
    String[] getTitelFromTasks();

    @Query("SELECT * FROM tasks WHERE taskId = :taskId" )
    Task getTask(int taskId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTask(Task tasks);

    @Update
    void updateTask(Task task);

    @Delete
    void deleteTask(Task task);
}
