package com.ifsp.dmo.dotaapplication.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ifsp.dmo.dotaapplication.R;
import com.ifsp.dmo.dotaapplication.model.BaseEntity;

import java.util.List;

/**
 * @author vinicius.montouro
 */
public class ItemEntityAdapter extends RecyclerView.Adapter<ItemEntityAdapter.RepositoryViewHolder> {

    private List<BaseEntity> entities;

    private static RecyclerItemClickListener clickListener;

    public ItemEntityAdapter(@NonNull List<BaseEntity> entities){
        this.entities = entities;
    }

    @NonNull
    @Override
    public RepositoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_view, parent, false);
        RepositoryViewHolder holder = new RepositoryViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RepositoryViewHolder holder, int position) {
        holder.repositoryName.setText(entities.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return entities.size();
    }

    public void setClickListener(RecyclerItemClickListener clickListener) {
        ItemEntityAdapter.clickListener = clickListener;
    }

    public class RepositoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView repositoryName;

        public RepositoryViewHolder(@NonNull View itemView){
            super(itemView);
            repositoryName = itemView.findViewById(R.id.item_recicler_view_tv);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (clickListener != null)
                clickListener.onItemClick(getAdapterPosition());
        }
    }
}