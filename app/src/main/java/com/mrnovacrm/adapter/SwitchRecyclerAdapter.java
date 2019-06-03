package com.mrnovacrm.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.mrnovacrm.R;
import com.mrnovacrm.model.SwitchItemDTO;

import java.util.List;

public class SwitchRecyclerAdapter extends RecyclerView.Adapter<SwitchRecyclerAdapter.ViewHolder> {

    private Context mContext = null;
    private List<SwitchItemDTO> mDataList = null;

    private OnItemClickedListener mItemClickListener = null;
    private OnCheckedChangeListener onItemCheckedListener;

    public interface OnItemClickedListener {
        void onItemClicked(SwitchItemDTO vo, int position);
    }

    public interface OnCheckedChangeListener {
        void onCheckedChanged(int position , boolean isChecked);
    }

    public SwitchRecyclerAdapter(Context context, List<SwitchItemDTO> items) {
        mContext = context;
        mDataList = items;
    }

    public void setOnClickListener(OnItemClickedListener listener) {
        mItemClickListener = listener;
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        onItemCheckedListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_switch, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SwitchItemDTO item = mDataList.get(position);

        holder.title.setText(item.getTitle());
        if (item.isSwitch()) {
            holder.switchBtn.setVisibility(View.VISIBLE);
        } else {
            holder.switchBtn.setVisibility(View.GONE);
        }
        holder.switchBtn.setTag(position);
        holder.switchBtn.setOnCheckedChangeListener(mOnCheckedChangeListener);

        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(mOnClickListener);
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        SwitchCompat switchBtn;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.textTitle);
            switchBtn = (SwitchCompat) itemView.findViewById(R.id.switchOnOff);
        }
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = (int) v.getTag();

            if (mItemClickListener != null) {
                mItemClickListener.onItemClicked(mDataList.get(position), position);
            }
        }
    };
    private CompoundButton.OnCheckedChangeListener mOnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            int position = (int) buttonView.getTag();

            if (onItemCheckedListener != null) {
                onItemCheckedListener.onCheckedChanged(position, isChecked);
            }
        }
    };
}