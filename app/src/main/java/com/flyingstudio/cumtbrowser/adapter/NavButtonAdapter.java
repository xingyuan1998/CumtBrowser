package com.flyingstudio.cumtbrowser.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.flyingstudio.cumtbrowser.R;
import com.flyingstudio.cumtbrowser.activity.AboutUs;
import com.flyingstudio.cumtbrowser.bar.MyButton;
import com.flyingstudio.cumtbrowser.bar.SearchBar;

import java.util.List;

import static com.flyingstudio.cumtbrowser.BrowserActivity.searchBar;

/**
 * Created by MEzzsy on 2018/4/6.
 */

public class NavButtonAdapter extends RecyclerView.Adapter<NavButtonAdapter.ViewHolder> {
    private List<MyButton> buttonList;
    private Context context;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View buttonView;
        ImageView imageView;
        TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            buttonView = itemView;
            imageView = (ImageView) itemView.findViewById(R.id.image);
            textView = (TextView) itemView.findViewById(R.id.text);
        }
    }

    public NavButtonAdapter(List<MyButton> buttonList, Context context) {
        this.buttonList = buttonList;
        this.context = context;
    }

    @Override
    public NavButtonAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.button_item,
                parent, false);
        final NavButtonAdapter.ViewHolder holder = new NavButtonAdapter.ViewHolder(view);
        holder.buttonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = holder.getAdapterPosition();
                String url = buttonList.get(i).getUrl().trim();
                searchBar.onClickNavButton(url);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(NavButtonAdapter.ViewHolder holder, int position) {
        MyButton button = buttonList.get(position);
        holder.imageView.setImageResource(button.getImageId());
        holder.textView.setText(button.getText());
    }

    @Override
    public int getItemCount() {
        return buttonList.size();
    }
}
