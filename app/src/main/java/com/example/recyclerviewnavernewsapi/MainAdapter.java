package com.example.recyclerviewnavernewsapi;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import static android.text.Html.fromHtml;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.CustomViewHolder> {

    private ArrayList<ResultData> arrayList;

    public MainAdapter(ArrayList<ResultData> arrayList) {
        this.arrayList = arrayList;
    }

    // 리스트뷰 메뉴가 처음으로 생성될 때
    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.result_list, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        // 실제 추가될 때
        holder.tv_rank.setText(arrayList.get(position).getTv_rank());
        holder.tv_title.setText(arrayList.get(position).getTv_title());
        holder.tv_content.setText(arrayList.get(position).getTv_content());
        holder.tv_link.setText(arrayList.get(position).getTv_link());

        // 리스트뷰 클릭했을 때
        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 링크로 넘어가야 함.
                Context context = view.getContext();
                String url = holder.tv_link.getText().toString();
//                Log.e("url", url);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        protected TextView tv_rank;
        protected TextView tv_title;
        protected TextView tv_content;
        protected TextView tv_link;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tv_rank = (TextView)itemView.findViewById(R.id.tv_rank);
            this.tv_title = (TextView)itemView.findViewById(R.id.tv_title);
            this.tv_content = (TextView)itemView.findViewById(R.id.tv_content);
            this.tv_link = (TextView)itemView.findViewById(R.id.tv_link);
        }
    }
}
