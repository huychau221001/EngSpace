package com.example.engspace.adapter.setfragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.engspace.R;
import com.example.engspace.model.FolderResponse;
import com.example.engspace.model.UserResponse;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

public class FolderRecyclerViewAdapter extends RecyclerView.Adapter<FolderRecyclerViewAdapter.FolderViewHolder> {
    private Context context;
    private ArrayList<FolderResponse> listFolder;
    private ArrayList<Boolean> listSelected;
    OnItemClickCallback onItemClickCallback;

    public FolderRecyclerViewAdapter(Context context, OnItemClickCallback onItemClickCallback) {
        this.context = context;
        this.onItemClickCallback = onItemClickCallback;
        this.listSelected = new ArrayList<Boolean>();
    }

    public void setData(ArrayList<FolderResponse> list) {
        this.listFolder = list;
        for (int i = 0; i < this.listFolder.size(); i++) {
            this.listSelected.add(false);
        }
        notifyDataSetChanged();
    }

    public void setBorder(int position) {
        this.listSelected.set(position, !this.listSelected.get(position));
        notifyDataSetChanged();
    }

    public void setBorderByArrayId(ArrayList<Integer> set_folders) {
        for (int i = 0; i < this.listSelected.size(); i++) {
            if (set_folders.contains(this.listFolder.get(i).getId())) {
                this.listSelected.set(i, true);
            }
        }
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
        final float scale = context.getResources().getDisplayMetrics().density;
        int pixels = (int) (2 * scale + 0.5f);
        if (listSelected.get(position)) {
            holder.folderCard.setStrokeColor(ContextCompat.getColor(context, R.color.es_main_blue));
            holder.folderCard.setStrokeWidth(pixels);
        } else {
            holder.folderCard.setStrokeWidth(0);
        }
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
        private MaterialCardView folderCard;

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
