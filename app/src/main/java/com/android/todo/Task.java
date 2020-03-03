package com.android.todo;

import java.util.Date;
import java.util.UUID;

public class Task {
    private UUID mId;
    private String mTitle;
    private boolean mSolved;
    private Date mDate;


    public Task(){
        this(UUID.randomUUID());
    }

    public Task(UUID id){
        mId = id;
        mDate = new Date();
    }

    public UUID getId() {
        return mId;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }
}
