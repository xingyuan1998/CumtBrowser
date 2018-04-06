package com.flyingstudio.cumtbrowser.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.flyingstudio.cumtbrowser.R;

import java.util.List;

/**
 * Created by MEzzsy on 2018/4/3.
 */

public class PagesAdapter extends RecyclerView.Adapter<PagesAdapter.ViewHolder> {
    private List<String> pagesList;
    private JumpListener listener;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView page_content;
        ImageView img_delete_page;

        public ViewHolder(View itemView) {
            super(itemView);
            page_content = (TextView) itemView.findViewById(R.id.history_content);
            img_delete_page = (ImageView) itemView.findViewById(R.id.img_delete_history);
        }
    }

    public PagesAdapter() {
    }

    public void setPagesList(List<String> pagesList) {
        this.pagesList = pagesList;
    }

    @Override
    public PagesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_listview_item, parent, false);
        final PagesAdapter.ViewHolder viewHolder = new PagesAdapter.ViewHolder(view);
        viewHolder.page_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                listener.jump(position);
                listener.hidePopupWindow();
            }
        });
        viewHolder.img_delete_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pagesList.size() == 1) {
                    Toast.makeText(v.getContext(),
                            "必须保留一个页面", Toast.LENGTH_SHORT).show();
                } else {
                    int position = viewHolder.getAdapterPosition();
                    pagesList.remove(position);
                    listener.showNumbers(pagesList.size());
                    notifyDataSetChanged();
                }
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String content = pagesList.get(position);
        holder.page_content.setText(content);
    }

    @Override
    public int getItemCount() {
        return pagesList.size();
    }

    public void setJumpListener(PagesAdapter.JumpListener listener) {
        this.listener = listener;
    }

    public interface JumpListener {
        void jump(int position);
        void hidePopupWindow();
        void showNumbers(int number);
    }
}
