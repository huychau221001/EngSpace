package com.example.engspace.adapter.folderfragment;

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
import com.example.engspace.model.Folder;

import java.util.List;

public class FolderRecyclerViewAdapter extends RecyclerView.Adapter<FolderRecyclerViewAdapter.FolderViewHolder> {
    private Context context;
    private List<Folder> listFolder;

    public FolderRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<Folder> list) {
        this.listFolder = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FolderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_folder, parent, false);
        return new FolderRecyclerViewAdapter.FolderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FolderViewHolder holder, int position) {
        Folder folder = listFolder.get(position);
        if (folder == null) {
            return;
        }
        holder.folderTitle.setText(folder.getTitle());
        holder.folderUser.setText(folder.getUser());
        holder.folderAmount.setText(folder.getAmount() + " học phần");
        holder.folderUserPhoto.setImageResource(folder.getUser_img());
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
}
