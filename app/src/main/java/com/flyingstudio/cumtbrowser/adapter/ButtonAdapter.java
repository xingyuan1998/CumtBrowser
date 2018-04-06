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

import java.util.List;

/**
 * Created by MEzzsy on 2018/3/25.
 * 用于适配底部导航栏的设置按钮的RecyclerView
 */

public class ButtonAdapter extends RecyclerView.Adapter<ButtonAdapter.ViewHolder> {
    private List<MyButton> buttonList;
    private Context context;

    static class ViewHolder extends RecyclerView.ViewHolder{
        View buttonView;
        ImageView imageView;
        TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            buttonView=itemView;
            imageView=(ImageView)itemView.findViewById(R.id.image);
            textView=(TextView)itemView.findViewById(R.id.text);
        }
    }

    public ButtonAdapter(List<MyButton> buttonList,Context context) {
        this.buttonList = buttonList;
        this.context=context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.button_item,
                parent,false);
        final ViewHolder holder=new ViewHolder(view);
        holder.buttonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i=holder.getAdapterPosition();
                String content=buttonList.get(i).getText().trim();
                if (content.equals("关于我们")){
                    Intent intent=new Intent(context,AboutUs.class);
                    context.startActivity(intent);
                }else if (content.equals("UI测试")) {
                    Toast.makeText(v.getContext(), content, Toast.LENGTH_SHORT).show();
                }
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MyButton button=buttonList.get(position);
        holder.imageView.setImageResource(button.getImageId());
        holder.textView.setText(button.getText());
    }

    @Override
    public int getItemCount() {
        return buttonList.size();
    }
}
