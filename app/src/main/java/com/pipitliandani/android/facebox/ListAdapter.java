package com.pipitliandani.android.facebox;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by User on 28/05/2018.
 */

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.FaceViewHolder> {
    public Context cntx;
    List<FaceBoxModel> list;
    private int visibleThresHold = 10;
    private int LastVisibleItem, TotalItemCount;
    private boolean loading;
    private com.pipitliandani.android.facebox.onLoadMore LoadListener;

    public ListAdapter(Context context, List<FaceBoxModel> list, RecyclerView recyclerView) {
        this.cntx = context;
        this.list = list;
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            final LinearLayoutManager llm = (LinearLayoutManager) recyclerView.getLayoutManager();
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    TotalItemCount = llm.getItemCount();
                    LastVisibleItem = llm.findLastVisibleItemPosition();
                    if (!loading && dy > 0 && !recyclerView.canScrollVertically(RecyclerView.FOCUS_DOWN)) {
                        if (LoadListener != null) {
                            LoadListener.LoadMore();
                        }
                        loading = true;
                    }
                }
            });
        }
    }

    @Override
    public FaceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FaceViewHolder holder = null;

        if (viewType == 1) {
            holder = new FaceViewHolder(LayoutInflater.from(cntx).inflate(R.layout.list_item, parent, false));
        } else {
            holder = new LoadHolder(LayoutInflater.from(cntx).inflate(R.layout.load_item, parent, false));
        }
        return holder;
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position) != null ? 1 : 0;
    }

    @Override
    public void onBindViewHolder(final FaceViewHolder holder, int position) {
        if (holder instanceof FaceViewHolder) {
            final FaceBoxModel currentModel = list.get(position);
            final String key = currentModel.key;

            holder.name.setText(currentModel.name);
            holder.unit.setText(currentModel.unit);
            holder.functionTitlelist.setText(currentModel.functionTitle);

            Picasso.with(cntx).load(currentModel.image_url).into(holder.photo);
            if (currentModel.unit.equals("Dewan Komisaris")) {
                holder.star1.setVisibility(View.VISIBLE);
                holder.star2.setVisibility(View.VISIBLE);
                holder.star3.setVisibility(View.VISIBLE);
            } else if (currentModel.unit.equals("Direksi")) {
                holder.star1.setVisibility(View.VISIBLE);
                holder.star2.setVisibility(View.VISIBLE);
                holder.star3.setVisibility(View.VISIBLE);
            } else if (currentModel.officials.equals("Eselon I")) {
                holder.star1.setVisibility(View.VISIBLE);
                holder.star2.setVisibility(View.VISIBLE);
                holder.star3.setVisibility(View.GONE);
            } else if (currentModel.officials.equals("Eselon II")) {
                holder.star1.setVisibility(View.VISIBLE);
                holder.star2.setVisibility(View.GONE);
                holder.star3.setVisibility(View.GONE);
            } else if (
                    currentModel.unit.contains("PT.") &&
                            (currentModel.functionTitle.contains("Direktur Utama")
                                    || currentModel.functionTitle.contains("Vice President"))) {
                holder.star1.setVisibility(View.VISIBLE);
                holder.star2.setVisibility(View.VISIBLE);
                holder.star3.setVisibility(View.GONE);

            } else if (currentModel.unit.contains("PT.") && currentModel.functionTitle.contains("Direktur")) {
                holder.star1.setVisibility(View.VISIBLE);
                holder.star2.setVisibility(View.GONE);
                holder.star3.setVisibility(View.GONE);
            } else if (currentModel.functionTitle.contains("Director")){
                holder.star1.setVisibility(View.VISIBLE);
                holder.star2.setVisibility(View.GONE);
                holder.star3.setVisibility(View.GONE);
            }else if (currentModel.isHead) {
                holder.star1.setVisibility(View.VISIBLE);
                holder.star2.setVisibility(View.GONE);
                holder.star3.setVisibility(View.GONE);
            } else {
                holder.star1.setVisibility(View.INVISIBLE);
                holder.star2.setVisibility(View.INVISIBLE);
                holder.star3.setVisibility(View.INVISIBLE);
            }
//            if(!currentModel.isHead) {
//                holder.star.setVisibility(View.INVISIBLE);
//            } else {
//                holder.star.setVisibility(View.VISIBLE);
//            }
            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(cntx, Profile.class);
                    Log.d("PROFILE_CLICK", key);
                    intent.putExtra("key", key);
                    cntx.startActivity(intent);
                }
            });

        } else {
            ((LoadHolder) holder).progressBar.setIndeterminate(true);
        }
    }

    public void setLoaded() {
        loading = false;
    }

    public void setLoad() {
        loading = true;
    }

    public void setLoadMore(com.pipitliandani.android.facebox.onLoadMore LoadListenet) {
        this.LoadListener = LoadListenet;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class FaceViewHolder extends RecyclerView.ViewHolder {
        TextView name, unit, functionTitlelist;
        CircleImageView photo;
        LinearLayout layout;
        ImageView star1, star2, star3;

        public FaceViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.nameListItem);
            unit = itemView.findViewById(R.id.unitListItem);
            photo = itemView.findViewById(R.id.photo);
            layout = itemView.findViewById(R.id.layoutItemView);
            functionTitlelist = itemView.findViewById(R.id.functionTitleList);

            star1 = itemView.findViewById(R.id.star1);
            star2 = itemView.findViewById(R.id.star2);
            star3 = itemView.findViewById(R.id.star3);

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
