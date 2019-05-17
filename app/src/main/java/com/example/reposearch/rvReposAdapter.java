package com.example.reposearch;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class rvReposAdapter extends RecyclerView.Adapter<rvReposAdapter.reposViewHolder> {

    private ArrayList<repoPair<String, Intent>> repositories;
    private Context context;
    private OnNoteListner mOnNoteListner;

    public rvReposAdapter(ArrayList<repoPair<String, Intent>> mRepositories, OnNoteListner onNoteListner)
    {
        mOnNoteListner = onNoteListner;
        repositories = mRepositories;
    }

    @NonNull
    @Override
    public reposViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        View view = LayoutInflater.from(context)
                .inflate(R.layout.recycle_view_repos, viewGroup, false);
        reposViewHolder holder = new reposViewHolder(view, mOnNoteListner);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull reposViewHolder reposViewHolder, int i) {

        repoPair<String, Intent> repo = repositories.get(i);

        TextView repoNameView = reposViewHolder.repoName;
        repoNameView.setText(repo.getName());
    }

    @Override
    public int getItemCount() {
        return repositories.size();
    }

    public class reposViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener
    {
        public TextView repoName;
        OnNoteListner onNoteListner;

        public reposViewHolder(@NonNull View itemView, OnNoteListner onNoteListner) {
            super(itemView);

            this.onNoteListner = onNoteListner;
            repoName = (TextView) itemView.findViewById(R.id.repo_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onNoteListner.onNoteClick(getAdapterPosition());
        }
    }

    public interface OnNoteListner{
        void onNoteClick(int position);
    }
}
