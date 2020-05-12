package com.android.todo;

import android.content.res.Resources;

import androidx.core.os.ConfigurationCompat;
import androidx.core.os.LocaleListCompat;

import java.text.DateFormat;
import java.util.Date;
import java.util.UUID;

public class Task {
    private UUID mId;
    private String mTitle;
    private boolean mSolved;
    private Date mDate;
    private String mDescription;


    public Task(){
        this(UUID.randomUUID());
    }

    public Task(UUID id){
        mId = id;
        mDate = new Date();
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String Description) {
        this.mDescription = Description;
    }

    public UUID getId() {
        return mId;
    }

    public Date getDate() {
        return mDate;
    }

    public String getFormattedDate() {

        LocaleListCompat localeListCompat = ConfigurationCompat.getLocales(Resources.getSystem().getConfiguration());

        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.LONG, localeListCompat.get(0));

        return dateFormat.format(mDate);
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
