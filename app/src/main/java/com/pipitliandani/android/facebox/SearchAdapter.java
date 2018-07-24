package com.pipitliandani.android.facebox;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by User on 04/06/2018.
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> implements Filterable {
    private Context context;
    private List<FaceBoxModel> Searchlist;
    private List<FaceBoxModel> SearchlistFiltered;
    private SearchAdapterlistener adapterlistener;
    List<FaceBoxModel> filteredList;
    TextView notFound;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final FaceBoxModel faceBoxModel = SearchlistFiltered.get(position);
        final String key = faceBoxModel.getKey();
        holder.name1.setText(faceBoxModel.getName());
        holder.unit1.setText(faceBoxModel.getUnit());
        holder.functionTitle.setText(faceBoxModel.getFunctionTitle());
        if (!faceBoxModel.image_url.equals(""))
            Picasso.with(context).load(faceBoxModel.image_url).into(holder.photo);
        else
            Picasso.with(context).load(R.drawable.default_image).placeholder(R.color.grey).into(holder.photo);
        if (faceBoxModel.workUnit.equals("Dewan Komisaris")){
            holder.star1.setVisibility(View.VISIBLE);
            holder.star2.setVisibility(View.VISIBLE);
            holder.star3.setVisibility(View.VISIBLE);
        }else if (faceBoxModel.officials.equals("Eselon I")){
            holder.star1.setVisibility(View.VISIBLE);
            holder.star2.setVisibility(View.VISIBLE);
            holder.star3.setVisibility(View.INVISIBLE);
        }else if (faceBoxModel.officials.equals("Eselon II")){
            holder.star1.setVisibility(View.VISIBLE);
            holder.star2.setVisibility(View.INVISIBLE);
            holder.star3.setVisibility(View.INVISIBLE);
        }else if (faceBoxModel.isHead){
            holder.star1.setVisibility(View.VISIBLE);
            holder.star2.setVisibility(View.INVISIBLE);
            holder.star3.setVisibility(View.INVISIBLE);
        } else {
            holder.star1.setVisibility(View.INVISIBLE);
            holder.star2.setVisibility(View.INVISIBLE);
            holder.star3.setVisibility(View.INVISIBLE);
        }
//        if(filteredList.size() == 0){
//            holder.notFound.setVisibility(View.VISIBLE);
//
//        }
//        if(!faceBoxModel.isHead) {
//            holder.star1.setVisibility(View.INVISIBLE);
//        } else {
//            holder.star1.setVisibility(View.VISIBLE);
//        }
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Profile.class);
                intent.putExtra("key", key);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return SearchlistFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String cons = constraint.toString();
                if (cons.isEmpty()) {
                    SearchlistFiltered = Searchlist;
                } else {
                    filteredList = new ArrayList<>();
                    for (FaceBoxModel row : Searchlist) {
                        if (row.getName().toLowerCase().contains(cons.toLowerCase()) ||
                                row.getUnit().toLowerCase().contains(cons.toLowerCase()) ||
                                row.getPlaceOfBirth().toLowerCase().contains(cons.toLowerCase()) ||
                                row.getEduLevel().toLowerCase().contains(cons.toLowerCase()) ||
                                row.getMajor().toLowerCase().contains(cons.toLowerCase()) ||
                                row.getFunctionTitle().toLowerCase().contains(cons)) {
                            filteredList.add(row);
                        }
                    }
                    adapterlistener.onSearchCompleted(filteredList.size());

                    SearchlistFiltered = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = SearchlistFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                SearchlistFiltered = (ArrayList<FaceBoxModel>) results.values;
                notifyDataSetChanged();

            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name1, unit1, functionTitle;
        public CircleImageView photo;
        public LinearLayout layout;
        ImageView star1, star2,star3;

        public ViewHolder(View itemView) {
            super(itemView);
            name1 = itemView.findViewById(R.id.nameListItem);
            unit1 = itemView.findViewById(R.id.unitListItem);
            photo = itemView.findViewById(R.id.photo);
            layout = itemView.findViewById(R.id.layoutItemView);
            star1 = itemView.findViewById(R.id.star1);
            star2 = itemView.findViewById(R.id.star2);
            star3 = itemView.findViewById(R.id.star3);
            functionTitle = itemView.findViewById(R.id.functionTitleList);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    adapterlistener.onSearchSelected(SearchlistFiltered.get(getAdapterPosition()));

                }
            });
        }
    }

    public SearchAdapter(Context context, List<FaceBoxModel> searchlist, SearchAdapterlistener adapterlistener, TextView notFound) {
        this.context = context;
        this.adapterlistener = adapterlistener;
        this.Searchlist = searchlist;
        this.SearchlistFiltered = searchlist;
        this.notFound = (TextView) ((Activity) this.context).findViewById(R.id.notFound);
    }

    public interface SearchAdapterlistener {
        void onSearchSelected(FaceBoxModel model);
        void onSearchCompleted(int total);
    }
}
