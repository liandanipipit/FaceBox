package com.pipitliandani.android.facebox;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by User on 28/06/2018.
 */

public class BirthdayAdapter extends RecyclerView.Adapter<BirthdayAdapter.FaceViewHolder> {
    public Context cntx;
    List<FaceBoxModel> list;
    private int visibleThresHold = 10;
    private int LastVisibleItem, TotalItemCount;
    private boolean loading;
    private com.pipitliandani.android.facebox.onLoadMore LoadListener;

    public BirthdayAdapter(Context context, List<FaceBoxModel> list, RecyclerView recyclerView){
        this.cntx = context;
        this.list = list;
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager){
            final LinearLayoutManager llm = (LinearLayoutManager)recyclerView.getLayoutManager();
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    TotalItemCount = llm.getItemCount();
                    LastVisibleItem = llm.findLastVisibleItemPosition();
                    if (!loading && dy>0 && !recyclerView.canScrollVertically(RecyclerView.FOCUS_DOWN)){
//                        if (LoadListener != null){
//                            LoadListener.LoadMore();
//                        }
                        loading = true;
                    }
                }
            });
        }
    }
    @Override
    public FaceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FaceViewHolder holder = null;

        if (viewType ==1 ){
            holder = new FaceViewHolder(LayoutInflater.from(cntx).inflate(R.layout.birthday_list, parent, false));
        }else {
            holder = new LoadHolder(LayoutInflater.from(cntx).inflate(R.layout.load_item, parent, false));
        }
        return holder;

    }

    @Override
    public void onBindViewHolder(FaceViewHolder holder, int position) {
        if (holder instanceof FaceViewHolder){
            final FaceBoxModel currentModel = list.get(position);
            final String key = currentModel.key;
            if (key == null){
                Log.d("Badapter", "null");
            }else {
                Log.d("Badapter", key);
            }

            holder.name.setText(currentModel.name);
            holder.unit.setText(currentModel.unit);
            Picasso.with(cntx).load(currentModel.image_url).into(holder.photo);
            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(cntx, Profile.class);
                    intent.putExtra("key", key);
                    cntx.startActivity(intent);
                }
            });

        }else {
            ((LoadHolder)holder).progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position)!= null ?1:0;
    }



    public void setLoaded(){
        loading = false;
    }
    public void setLoad(){
        loading = true;
    }
    public void setLoadMore(com.pipitliandani.android.facebox.onLoadMore LoadListenet){
        this.LoadListener = LoadListenet;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public static class FaceViewHolder extends RecyclerView.ViewHolder {
        TextView name, unit;
        CircleImageView photo;
        LinearLayout layout;

        public FaceViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.nameBirthdayItem);
            unit = itemView.findViewById(R.id.unitBirthdayItem);
            photo = itemView.findViewById(R.id.photoBirthdayItem);
            layout = itemView.findViewById(R.id.layoutItemViewBirthday);

        }
    }
    public static class LoadHolder extends FaceViewHolder {
        public ProgressBar progressBar;
        public LoadHolder(View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.Progress_Item);
        }

    }
}
