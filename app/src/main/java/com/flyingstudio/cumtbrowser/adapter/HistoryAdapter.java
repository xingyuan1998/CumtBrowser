package com.flyingstudio.cumtbrowser.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.flyingstudio.cumtbrowser.R;

import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by MEzzsy on 2018/3/29.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    private List<String> historyList;
    private JumpListener listener;

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView history_content;
        ImageView img_delete_history;

        public ViewHolder(View itemView) {
            super(itemView);
            history_content=(TextView)itemView.findViewById(R.id.history_content);
            img_delete_history=(ImageView)itemView.findViewById(R.id.img_delete_history);
        }
    }

    public HistoryAdapter(List<String> historyList) {
        this.historyList = historyList;
    }

    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_listview_item,parent,false);
        final ViewHolder viewHolder=new ViewHolder(view);
        viewHolder.history_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position =viewHolder.getAdapterPosition();
                String content= historyList.get(position);
                listener.jump(content);
                Log.d(TAG, "历史记录内容为"+content);
            }
        });
        viewHolder.img_delete_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position =viewHolder.getAdapterPosition();
                historyList.remove(position);
                notifyDataSetChanged();
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(HistoryAdapter.ViewHolder holder, int position) {
        String content=historyList.get(position);
        holder.history_content.setText(content);
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public void setJumpListener(JumpListener listener){
        this.listener=listener;
    }

    public interface JumpListener{
        void jump(String content);
    }

}
