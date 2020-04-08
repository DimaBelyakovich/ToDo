package com.android.todo.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.android.todo.Task;
import com.android.todo.database.TaskDbSchema.TaskTable;

import java.util.Date;
import java.util.UUID;

public class TaskCursorWrapper extends CursorWrapper {
    public TaskCursorWrapper(Cursor cursor){
        super(cursor);
    }

    public Task getTask(){
        String uuidString = getString(getColumnIndex(TaskTable.Cols.UUID));
        String title = getString(getColumnIndex(TaskTable.Cols.TITLE));
        String description = getString(getColumnIndex(TaskTable.Cols.DESCRIPTION));
        long date = getLong(getColumnIndex(TaskTable.Cols.DATE));
        int isSolved = getInt(getColumnIndex(TaskTable.Cols.SOLVED));

        Task task = new Task(UUID.fromString(uuidString));

        task.setTitle(title);
        task.setDate(new Date(date));
        task.setSolved(isSolved != 0);
        task.setDescription(description);

        return task;
    }
}
