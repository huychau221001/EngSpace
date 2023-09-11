package com.example.engspace.adapter.setfragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.engspace.R;
import com.example.engspace.model.SetDetailResponse;
import com.example.engspace.model.SetDetailResponseError;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class SetDetailRecyclerViewAdapter extends RecyclerView.Adapter<SetDetailRecyclerViewAdapter.SetDetailViewHolder> {
    TextChangeCallback callback;
    TextChangeCallback callback1;
    private Context context;
    private ArrayList<SetDetailResponse> listSetDetail;
    public SetDetailRecyclerViewAdapter(Context context, ArrayList<SetDetailResponse> list, TextChangeCallback callback, TextChangeCallback callback1) {
        this.context = context;
        this.listSetDetail = list;
        this.callback = callback;
        this.callback1 = callback1;
    }

    public ArrayList<SetDetailResponse> getData() {
        return listSetDetail;
    }

    @NonNull
    @Override
    public SetDetailRecyclerViewAdapter.SetDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_create_set, parent, false);
        return new SetDetailRecyclerViewAdapter.SetDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SetDetailRecyclerViewAdapter.SetDetailViewHolder holder, @SuppressLint("RecyclerView") int position) {
        SetDetailResponse set = listSetDetail.get(position);
        if (set == null) {
            return;
        }
        holder.termInput.setText(set.getTerm());
        holder.definitionInput.setText(set.getDefinition());

        if (set.getSetDetailResponseError() != null) {
            if (set.getSetDetailResponseError().getTerm() != null && !set.getSetDetailResponseError().getTerm().isEmpty()) {
                holder.termInputLayout.setError(set.getSetDetailResponseError().getTerm().get(0));
            } else {
                holder.termInputLayout.setError(null);
            }
            if (set.getSetDetailResponseError().getDefinition() != null && !set.getSetDetailResponseError().getDefinition().isEmpty()) {
                holder.definitionInputLayout.setError(set.getSetDetailResponseError().getDefinition().get(0));
            } else {
                holder.definitionInputLayout.setError(null);
            }
            set.setSetDetailResponseError(null);
        }

        holder.termInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.equals("")) {
                    return;
                }
                callback.textChangedAt(position, String.valueOf(s));

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        holder.definitionInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.equals("")) {
                    return;
                }
                callback1.textChangedAt(position, String.valueOf(s));

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public int getItemCount() {
        if (listSetDetail != null) {
            return listSetDetail.size();
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void removeItem(int position) {
        listSetDetail.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(SetDetailResponse item, int position) {
        listSetDetail.add(position, item);
        notifyItemInserted(position);
    }

    public void setError(SetDetailResponseError item, int position) {
        listSetDetail.get(position).setSetDetailResponseError(item);
        notifyDataSetChanged();
    }

    public interface TextChangeCallback {
        void textChangedAt(int position, String text);
    }

    public class SetDetailViewHolder extends RecyclerView.ViewHolder {
        private TextInputLayout termInputLayout;
        private TextInputLayout definitionInputLayout;
        private TextInputEditText termInput;
        private TextInputEditText definitionInput;

        public SetDetailViewHolder(@NonNull View itemView) {
            super(itemView);

            termInputLayout = (TextInputLayout) itemView.findViewById(R.id.term_input_layout);
            termInput = (TextInputEditText) itemView.findViewById(R.id.term_input);
            definitionInputLayout = (TextInputLayout) itemView.findViewById(R.id.definition_input_layout);
            definitionInput = (TextInputEditText) itemView.findViewById(R.id.definition_input);
        }
    }
}
