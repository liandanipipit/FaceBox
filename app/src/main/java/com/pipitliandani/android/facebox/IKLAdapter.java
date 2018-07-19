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

import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by User on 07/07/2018.
 */

public class IKLAdapter extends RecyclerView.Adapter<IKLAdapter.FaceViewHolder> {
    public Context cntx;
    List<FaceBoxModel> list;
    private int visibleThresHold = 10;
    private int LastVisibleItem, TotalItemCount;
    public String type = "";
    private boolean loading;
    private com.pipitliandani.android.facebox.onLoadMore LoadListener;


    public IKLAdapter(Context context, List<FaceBoxModel> list, RecyclerView recyclerView, String type){
        this.cntx = context;
        this.list = list;
        this.type = type;
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
    public IKLAdapter.FaceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        IKLAdapter.FaceViewHolder holder = null;

        if (viewType ==1 ){
            holder = new IKLAdapter.FaceViewHolder(LayoutInflater.from(cntx).inflate(R.layout.list_pengurus_ikl, parent, false));
        }else {
            holder = new IKLAdapter.LoadHolder(LayoutInflater.from(cntx).inflate(R.layout.load_item, parent, false));
        }
        return holder;
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position)!= null ?1:0;
    }

    @Override
    public void onBindViewHolder(final IKLAdapter.FaceViewHolder holder, int position) {
        if (holder instanceof IKLAdapter.FaceViewHolder){
            final FaceBoxModel currentModel = list.get(position);
            final String key = currentModel.key;
            if (currentModel.workUnit.equals("Dewan Komisaris")){
                holder.starIKl1.setVisibility(View.VISIBLE);
                holder.starIKL2.setVisibility(View.VISIBLE);
                holder.starIKl3.setVisibility(View.VISIBLE);
            }else if (currentModel.officials.equals("Eselon I")){
                holder.starIKl1.setVisibility(View.VISIBLE);
                holder.starIKL2.setVisibility(View.VISIBLE);
                holder.starIKl3.setVisibility(View.INVISIBLE);
            }else if (currentModel.officials.equals("Eselon II")){
                holder.starIKl1.setVisibility(View.VISIBLE);
                holder.starIKL2.setVisibility(View.INVISIBLE);
                holder.starIKl3.setVisibility(View.INVISIBLE);
            }else if (currentModel.isHead){
                holder.starIKl1.setVisibility(View.VISIBLE);
                holder.starIKL2.setVisibility(View.INVISIBLE);
                holder.starIKl3.setVisibility(View.INVISIBLE);
            } else {
                holder.starIKl1.setVisibility(View.INVISIBLE);
                holder.starIKL2.setVisibility(View.INVISIBLE);
                holder.starIKl3.setVisibility(View.INVISIBLE);
            }
//            if (!currentModel.isHead){
//                holder.starIKl.setVisibility(View.INVISIBLE);
//            } else {
//                holder.starIKl.setVisibility(View.VISIBLE);
//            }

            holder.name.setText(currentModel.name);
            holder.unit.setText(currentModel.unit);
            if (type.equals("IKL")){
                Log.d("IKL", currentModel.ikl);
                holder.jabatan.setText(currentModel.ikl);

            } else {
                holder.jabatan.setText(currentModel.pensionBudget);
            }
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
            ((IKLAdapter.LoadHolder)holder).progressBar.setIndeterminate(true);
        }
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
        TextView name, unit, jabatan;
        CircleImageView photo;
        LinearLayout layout;
        ImageView starIKl1, starIKL2,starIKl3;

        public FaceViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.nameListItemIKL);
            unit = itemView.findViewById(R.id.unitListItemIKL);
            photo = itemView.findViewById(R.id.photoIKL);
            jabatan = itemView.findViewById(R.id.jabatanIKL);
            layout = itemView.findViewById(R.id.layoutItemViewIKL);
            starIKl1 = itemView.findViewById(R.id.starIKL1);
            starIKL2 = itemView.findViewById(R.id.starIKL2);
            starIKl3 = itemView.findViewById(R.id.starIKL3);

        }
    }
    public static class LoadHolder extends IKLAdapter.FaceViewHolder {
        public ProgressBar progressBar;
        public LoadHolder(View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.Progress_Item);
        }

    }
}
