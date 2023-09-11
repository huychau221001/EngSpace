package com.example.engspace.adapter.searchfragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.engspace.R;
import com.example.engspace.model.FolderResponse;
import com.example.engspace.model.UserResponse;

import java.util.ArrayList;

public class FolderResultRecyclerViewAdapter extends RecyclerView.Adapter<FolderResultRecyclerViewAdapter.FolderViewHolder> {
    private Context context;
    private ArrayList<FolderResponse> listFolder;
    OnItemClickCallback onItemClickCallback;

    public FolderResultRecyclerViewAdapter(Context context, OnItemClickCallback onItemClickCallback) {
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
        return new FolderResultRecyclerViewAdapter.FolderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FolderViewHolder holder, int position) {
        FolderResponse folder = listFolder.get(position);
        if (folder == null) {
            return;
        }
        holder.folderTitle.setText(folder.getName());
        UserResponse userResponse = folder.getUser();
        holder.folderUser.setText(userResponse.getUsername());
        holder.folderAmount.setText(folder.getAmount() + " học phần");
        holder.folderUserPhoto.setImageResource(R.drawable.d1);
        holder.folderCard.setOnClickListener(new View.OnClickListener() {
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
        private CardView folderCard;

        public FolderViewHolder(@NonNull View itemView) {
            super(itemView);

            folderTitle = itemView.findViewById(R.id.folder_title);
            folderUser = itemView.findViewById(R.id.folder_user);
            folderUserPhoto = itemView.findViewById(R.id.folder_user_photo);
            folderAmount = itemView.findViewById(R.id.folder_amount);
            folderCard = itemView.findViewById(R.id.folder);
            folderCard.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        }
    }
    public interface OnItemClickCallback {
        void itemClickAt(int position);
    }
}
