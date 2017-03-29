package com.androidbelieve.drawerwithswipetabs;

/**
 * Created by pandu on 25/03/17.
 */


import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;

import static java.security.AccessController.getContext;

/**
 * Created by galan on 23/03/2017.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder> {
    private static final int TYPE_HEAD = 0;
    private static final int TYPE_LIST = 1;
    ArrayList<isiMateri> arrayList = new ArrayList<>();

    public RecyclerAdapter(ArrayList<isiMateri> arrayList) {
        this.arrayList = arrayList;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view);
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        isiMateri isiMateris = arrayList.get(position);
        holder.title.setText(isiMateris.getTitle());
        holder.description.setText(isiMateris.getDescription());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return TYPE_HEAD;
        return TYPE_LIST;

    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView title, description;
        FragmentManager mFragmentManager;
        FragmentTransaction mFragmentTransaction;

        public RecyclerViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.item_title);
            description = (TextView) view.findViewById(R.id.item_detail);
            mFragmentTransaction = mFragmentManager.beginTransaction();
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentTransaction fragmentTransaction = mFragmentTransaction;
                    fragmentTransaction.replace(R.id.containerView, new materiClicked()).commit();
                }
            });
        }
    }
}

