package com.android.todo;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.Annotation;
import java.text.DateFormat;
import java.util.List;
import java.util.Locale;

public class TaskListFragment extends Fragment {
    private RecyclerView mTaskRecyclerView;
    private TaskAdater mAdapter;
    private TextView mTextView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    private class TaskHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private CheckBox mSolved;
        private Task mTask;

        public TaskHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.list_item_task, parent,false));
            itemView.setOnClickListener(this);
            mTitleTextView = (TextView) itemView.findViewById(R.id.task_title);
            mDateTextView = (TextView) itemView.findViewById(R.id.task_date);
            mSolved = (CheckBox)itemView.findViewById(R.id.task_solved);
        }

        public void bind(Task task){
            mTask = task;
            mTitleTextView.setText(mTask.getTitle());
            mDateTextView.setText(DateFormat.getDateInstance(DateFormat.FULL, Locale.ENGLISH).format(mTask.getDate()));
            mSolved.setChecked(mTask.isSolved());

            mSolved.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mTask.setSolved(isChecked);
                }
            });
        }

        @Override
        public void onClick(View view) {
            Intent intent = TaskPagerActivity.newIntent(getActivity(), mTask.getId());
            startActivity(intent);
        }

    }

    private class TaskAdater extends RecyclerView.Adapter<TaskHolder>{
        private List<Task> mTasks;
        public TaskAdater(List<Task> tasks){
            mTasks = tasks;
        }

        @Override
        public TaskHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new TaskHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(TaskHolder holder, int position) {
            Task task = mTasks.get(position);
            holder.bind(task);

        }

        @Override
        public int getItemCount() {
            return mTasks.size();
        }

        public void setTasks(List<Task> tasks){
            mTasks = tasks;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frgment_task_list, container, false);
        mTaskRecyclerView = (RecyclerView) view.findViewById(R.id.task_recycler_view);
        mTaskRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ///
        AnimationSet set = new AnimationSet(true);
        Animation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(500);
        set.addAnimation(animation);

        animation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF, 0.0f
        );
        animation.setDuration(100);
        set.addAnimation(animation);

        mTaskRecyclerView.setLayoutAnimation(new LayoutAnimationController(set, 0.5f));

        ////////
        mTextView = view.findViewById(R.id.no_tasks);

        if(TaskLab.get(getActivity()).getTasks().size() <= 0) {
            mTextView.setVisibility(View.VISIBLE);
        } else {
            updateUI();
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_task_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.new_task:
                Task task = new Task();
                TaskLab.get(getActivity()).addTask(task);
                Intent intent = TaskPagerActivity.newIntent(getActivity(), task.getId());
                startActivity(intent);
                return true;

                default:
                    return super.onOptionsItemSelected(item);
        }
    }

    private void updateUI(){
        TaskLab taskLab = TaskLab.get(getActivity());
        List<Task> tasks = taskLab.getTasks();

        if(mAdapter == null){
            mAdapter = new TaskAdater(tasks);
            mTaskRecyclerView.setAdapter(mAdapter);
        }
        else {
            mAdapter.setTasks(tasks);
            mAdapter.notifyDataSetChanged();
        }

    }
}
