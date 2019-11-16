package com.ksp.donut.uca.dm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ksp.donut.uca.R;

import java.util.List;


public class DMAdapter extends RecyclerView.Adapter<DMAdapter.DMViewHolder> {

    private static final String TAG = DMAdapter.class.getSimpleName() + "Logs";

    private LayoutInflater mInflater;
    private Context mContext;

    private List<DMDetails> mDMCards;

    DMAdapter(Context ct, List<DMDetails> dmCards) {
        mContext = ct;
        mInflater = LayoutInflater.from(ct);
        mDMCards = dmCards;
    }

    void setCards(List<DMDetails> eventCards)
    {
        mDMCards = eventCards;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DMViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.dm_rv, parent, false);
        return new DMViewHolder(mItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final DMViewHolder holder, final int position) {
        if (mDMCards != null) {
            final DMDetails current = mDMCards.get(position);
            holder.senderName.setText(current.getSender());

        }
    }

    @Override
    public int getItemCount() {
        return mDMCards == null ? 0 : mDMCards.size();
    }

    class DMViewHolder extends RecyclerView.ViewHolder {
        final TextView senderName;

        DMViewHolder(View itemView) {
            super(itemView);
            senderName = itemView.findViewById(R.id.dm_rv_sendername);
        }

    }

}
