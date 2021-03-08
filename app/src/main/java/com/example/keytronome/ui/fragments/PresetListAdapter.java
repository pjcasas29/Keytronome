package com.example.keytronome.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.keytronome.R;
import com.example.keytronome.db.Keytronome;

import java.util.List;

public class PresetListAdapter extends RecyclerView.Adapter<PresetListAdapter.PresetViewHolder> {



    private final LayoutInflater layoutInflater;
    private Context context;
    private List<Keytronome> presets;
    final private ListItemClickListener mOnClickListener;


    public PresetListAdapter(Activity activity, ListItemClickListener onClickListener) {
        this.mOnClickListener = onClickListener;
        layoutInflater = LayoutInflater.from(activity);
        context = activity;
    }

    @NonNull
    @Override
    public PresetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.preset_list_item, parent, false);
        PresetViewHolder viewHolder = new PresetViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PresetViewHolder holder, int position) {
        if(presets != null){
            Keytronome preset = presets.get(position);
            holder.setData(preset.getPresetName(), position);
        }
        else{
            //Data not ready yet
            holder.presetView.setText(R.string.no_preset);
        }
    }

    public void setPresets(List<Keytronome> presets){
        this.presets = presets;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        if(presets != null){
            return presets.size();
        }
        else return 0;
    }

    public class PresetViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView presetView;
        private int mPosition;

        public PresetViewHolder(View itemView){
            super(itemView);
            presetView = itemView.findViewById(R.id.presetTxv);
            itemView.setOnClickListener(this);
        }

        public void setData(String name, int position){
            presetView.setText(name);
            mPosition = position;
        }


        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            mOnClickListener.onListItemClick(position);
        }
    }
}
