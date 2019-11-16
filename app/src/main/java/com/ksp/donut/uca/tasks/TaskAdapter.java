package com.ksp.donut.uca.tasks;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ksp.donut.uca.R;

import java.util.List;


public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private static final String TAG = TaskAdapter.class.getSimpleName() + "Logs";

    private LayoutInflater mInflater;
    private Context mContext;

    private List<TaskDetails> mDMCards;

    TaskAdapter(Context ct, List<TaskDetails> dmCards) {
        mContext = ct;
        mInflater = LayoutInflater.from(ct);
        mDMCards = dmCards;
    }

    void setCards(List<TaskDetails> eventCards)
    {
        mDMCards = eventCards;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.task_rv, parent, false);

        return new TaskViewHolder(mItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final TaskViewHolder holder, final int position) {
        if (mDMCards != null) {
            final TaskDetails current = mDMCards.get(position);
            holder.taskName.setText(current.getTaskName());
            holder.deadLine.setText(current.getTaskDeadline());

        }
    }

    @Override
    public int getItemCount() {
        return mDMCards == null ? 0 : mDMCards.size();
    }

    class TaskViewHolder extends RecyclerView.ViewHolder {
        final TextView taskName;
        final TextView deadLine;

        TaskViewHolder(View itemView) {
            super(itemView);
            taskName = itemView.findViewById(R.id.task_name_rv);
            deadLine = itemView.findViewById(R.id.task_deadline_rv);
        }

    }

}
