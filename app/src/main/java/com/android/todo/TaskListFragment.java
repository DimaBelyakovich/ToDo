package com.android.todo;

import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TaskListFragment extends Fragment {
    private RecyclerView mTaskRecyclerView;
    private TaskAdapter mAdapter;
    private TextView mTextView;
    private Date today = new Date();

    private Callbacks mCallbacks;

    public interface Callbacks{
        void onTaskSelected(Task task);
        void onTaskRemoved(Task task);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    private class TaskHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private Task mTask;

        public TaskHolder(LayoutInflater inflater, ViewGroup parent){

            super(inflater.inflate(R.layout.list_item_task, parent,false));

            itemView.setOnClickListener(this);
            mTitleTextView = (TextView) itemView.findViewById(R.id.task_title);
            mDateTextView = (TextView) itemView.findViewById(R.id.task_date);
        }

        public void bind(Task task){
            mTask = task;
            mTitleTextView.setText(mTask.getTitle());
            mDateTextView.setText(DateFormat.getDateInstance(DateFormat.FULL, Locale.ENGLISH).format(mTask.getDate()));


            if(mTask.isSolved()){
                mTitleTextView.setTextColor(Color.parseColor("#8CBA51"));
                mDateTextView.setTextColor(Color.parseColor("#8CBA51"));
                itemView.setBackgroundColor(Color.parseColor("#F1F6FB"));
            }

            if((today.compareTo(mTask.getDate()) == 1) && !mTask.isSolved()){
                mDateTextView.setTextColor(Color.parseColor("#F6522E"));
            }

            if((today.compareTo(mTask.getDate()) == 0) && !mTask.isSolved()){
                mDateTextView.setTextColor(Color.parseColor("#A400FF"));
            }
        }

        @Override
        public void onClick(View view) {
            mCallbacks.onTaskSelected(mTask);
        }
    }

    private class TaskAdapter extends RecyclerView.Adapter<TaskHolder>{
        private List<Task> mTasks;
        public TaskAdapter(List<Task> tasks){
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

        public List<Task> getTasks(){
            return mTasks;
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


        ItemTouchHelper mIth = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    public boolean onMove(RecyclerView recyclerView,
                                          RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                        final int fromPos = viewHolder.getAdapterPosition();
                        final int toPos = target.getAdapterPosition();
                        return false;
                    }
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                        final int pos = viewHolder.getAdapterPosition();
                        Task task = mAdapter.getTasks().get(pos);
                        if (direction==ItemTouchHelper.LEFT) {
                            mCallbacks.onTaskRemoved(task);
                            updateUI();
                        } else if (direction== ItemTouchHelper.RIGHT) {
                            mCallbacks.onTaskSelected(task);
                            updateUI();
                        }
                    }
                });
        mIth.attachToRecyclerView(mTaskRecyclerView);

        ///
        AnimationSet set = new AnimationSet(true);
        Animation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(50);
        set.addAnimation(animation);

        animation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF, 0.0f
        );
        animation.setDuration(50);
        set.addAnimation(animation);

        mTaskRecyclerView.setLayoutAnimation(new LayoutAnimationController(set, 0.5f));

        ////////
        mTextView = view.findViewById(R.id.no_tasks);

        if(TaskLab.get(getActivity()).getTasks().size() <= 0) {
            mTextView.setVisibility(View.VISIBLE);
            updateUI();
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
    public void onPause() {
        super.onPause();
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

                updateUI();
                mCallbacks.onTaskSelected(task);

                return true;

                default:
                    return super.onOptionsItemSelected(item);
        }
    }

    public void updateUI(){
        TaskLab taskLab = TaskLab.get(getActivity());
        List<Task> tasksAll = taskLab.getTasks();

        List<Task> tasksSolved = new ArrayList<>();
        List<Task> tasks = new ArrayList<>();


        for (int i = 0; i < tasksAll.size(); i++) {
            Task taskTmp = tasksAll.get(i);
            if(!taskTmp.isSolved()){
                tasks.add(taskTmp);
            }else {
                tasksSolved.add(taskTmp);
            }
        }

        Collections.reverse(tasks);

        for (int i = 0; i < tasksSolved.size(); i++) {
            Task taskTmp = tasksSolved.get(i);
            tasks.add(taskTmp);
        }

        if(mAdapter == null){
            mAdapter = new TaskAdapter(tasks);
            mTaskRecyclerView.setAdapter(mAdapter);
        }
        else {
            mAdapter.setTasks(tasks);
            mAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }
}
