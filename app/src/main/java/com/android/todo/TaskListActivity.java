package com.android.todo;

import android.content.Intent;

import androidx.fragment.app.Fragment;

public class TaskListActivity extends SingleFragmentActivity
        implements TaskListFragment.Callbacks, TaskFragment.Callbacks {
    @Override
    protected Fragment createFragment() {
        return new TaskListFragment();
    }

    @Override
    public void onTaskSelected(Task task) {
        if(findViewById(R.id.detail_fragment_container) == null){
            Intent intent = TaskPagerActivity.newIntent(this,task.getId());
            startActivity(intent);
        }else{
            Fragment newDetail = TaskFragment.newInstance(task.getId());

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_fragment_container, newDetail)
                    .commit();
        }
    }

    @Override
    public void onTaskRemoved(Task task) {
        TaskListFragment listFragment = (TaskListFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        TaskLab.get(this).deleteTask(task);
        listFragment.updateUI();
    }

    @Override
    public void onTaskUpdated(Task task) {
        TaskListFragment listFragment = (TaskListFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        listFragment.updateUI();
    }
}
