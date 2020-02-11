package com.uek335.done.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;


@Entity(tableName = "tasks")
public class Task {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "taskId")
    private int id;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "content")
    private String content;

    @ColumnInfo(name = "category")
    private int category;

    @ColumnInfo(name = "endDate")
    @TypeConverters({DateConverter.class})
    private Date endDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getCategory() {
        return this.category;
    }

    public void setCategory(int categoryId) {
        this.category = categoryId;
    }
    public String toString(){
        return this.title + this.category;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getEndDateString(){
        if(this.endDate == null){
            return "No Date";
        }else {
            return "" + this.endDate;
        }
    }

    public String getCategoryString(){
        String val = "none";
        switch(this.category){
            case 0:
                val = "Work";
                break;
            case 1:
                val = "School";
                break;
            case 2:
                val = "Private";
        }
        return val;
    }
}
