package com.ksp.donut.uca.contact;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.ksp.donut.uca.R;

import java.util.List;


public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    private static final String TAG = ContactAdapter.class.getSimpleName() + "Logs";

    private LayoutInflater mInflater;
    private Context mContext;

    private List<ContactDetails> mContactCards;

    ContactAdapter(Context ct, List<ContactDetails> dmCards) {
        mContext = ct;
        mInflater = LayoutInflater.from(ct);
        mContactCards = dmCards;
    }

    void setCards(List<ContactDetails> eventCards)
    {
        mContactCards = eventCards;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.contact_rv, parent, false);
        return new ContactViewHolder(mItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ContactViewHolder holder, final int position) {
        if (mContactCards != null) {
            final ContactDetails current = mContactCards.get(position);
            holder.contactName.setText(current.getName());
            holder.contactNo.setText(current.getNumber());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NavController navController = Navigation.findNavController(v);
                    Bundle bundle = new Bundle();
                    bundle.putString("no",current.getNumber());
                    bundle.putString("name",current.getName());
                    navController.navigate(R.id.nav_chat,bundle);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mContactCards == null ? 0 : mContactCards.size();
    }

    class ContactViewHolder extends RecyclerView.ViewHolder {
        final TextView contactName;
        final TextView contactNo;

        ContactViewHolder(View itemView) {
            super(itemView);
            contactName = itemView.findViewById(R.id.contact_name);
            contactNo = itemView.findViewById(R.id.contact_no);
        }

    }

}
