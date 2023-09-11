package com.example.engspace.adapter.profileactivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.engspace.R;
import com.example.engspace.model.FolderResponse;
import com.example.engspace.model.UserResponse;

import java.util.ArrayList;

public class FolderRecyclerViewAdapter extends RecyclerView.Adapter<FolderRecyclerViewAdapter.FolderViewHolder> {
    private Context context;
    private ArrayList<FolderResponse> listFolder;
    OnItemClickCallback onItemClickCallback;

    public FolderRecyclerViewAdapter(Context context, OnItemClickCallback onItemClickCallback) {
        this.context = context;
        this.onItemClickCallback = onItemClickCallback;
    }

    public void setData(ArrayList<FolderResponse> list) {
        this.listFolder = list;
        notifyDataSetChanged();
    }


    public ArrayList<FolderResponse> getData() {
        return listFolder;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public FolderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_folder, parent, false);
        return new FolderRecyclerViewAdapter.FolderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FolderViewHolder holder, @SuppressLint("RecyclerView") int position) {
        FolderResponse folder = listFolder.get(position);
        if (folder == null) {
            return;
        }
        holder.folderTitle.setText(folder.getName());
        UserResponse userResponse = folder.getUser();
        holder.folderUser.setText(userResponse.getUsername());
        holder.folderAmount.setText(folder.getSets().size() + " học phần");
        holder.folderUserPhoto.setImageResource(R.drawable.d1);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickCallback.itemClickAt(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (listFolder != null) {
            return listFolder.size();
        }
        return 0;
    }

    public class FolderViewHolder extends RecyclerView.ViewHolder {
        private TextView folderTitle;
        private TextView folderUser;
        private ImageView folderUserPhoto;
        private TextView folderAmount;
        private CardView cardView;

        public FolderViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.folder);
            folderTitle = itemView.findViewById(R.id.folder_title);
            folderUser = itemView.findViewById(R.id.folder_user);
            folderUserPhoto = itemView.findViewById(R.id.folder_user_photo);
            folderAmount = itemView.findViewById(R.id.folder_amount);
            cardView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
    }

    public interface OnItemClickCallback {
        void itemClickAt(int position);
    }
}
