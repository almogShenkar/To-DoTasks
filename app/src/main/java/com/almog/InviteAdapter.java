package com.almog;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * Created by almog on 3/12/16.
 */
public class InviteAdapter extends RecyclerView.Adapter<InviteAdapter.CustomViewHolder> {

    private ArrayList<String> mDataset;
    private LayoutInflater inflater;

    // Provide a suitable constructor (depends on the kind of dataset)
    public InviteAdapter(ArrayList<String> myDataset) {
        this.mDataset = myDataset;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        inflater=LayoutInflater.from(parent.getContext());
        View v=inflater.inflate(R.layout.listrow,parent,false);
        return new CustomViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        String mail=mDataset.get(position);
        holder.mail.setText(mail);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        private TextView mail;

        public CustomViewHolder(View view) {
            super(view);
            mail = (TextView)view.findViewById(R.id.itemEmail);
        }
    }
}

