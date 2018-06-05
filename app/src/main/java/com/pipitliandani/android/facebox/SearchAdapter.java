package com.pipitliandani.android.facebox;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

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

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final FaceBoxModel faceBoxModel = SearchlistFiltered.get(position);
        holder.name1.setText(faceBoxModel.getName());
        holder.unit1.setText(faceBoxModel.getUnit());
        Picasso.with(context).load(faceBoxModel.image_url).into(holder.photo);

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
                if (cons.isEmpty()){
                    SearchlistFiltered = Searchlist;
                } else {
                    List<FaceBoxModel> filteredList = new ArrayList<>();
                    for (FaceBoxModel row : Searchlist){
                        if (row.getName().toLowerCase().contains(cons.toLowerCase()) || row.getUnit().contains(constraint)){
                            filteredList.add(row);
                        }
                    }
                    SearchlistFiltered =filteredList;
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
        public TextView name1, unit1;
        public CircleImageView photo;
        public ViewHolder(View itemView) {
            super(itemView);
            name1 = itemView.findViewById(R.id.nameListItem);
            unit1 = itemView.findViewById(R.id.unitListItem);
            photo = itemView.findViewById(R.id.photo);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapterlistener.onSearchSelected(SearchlistFiltered.get(getAdapterPosition()));
                }
            });
        }
    }
    public SearchAdapter(Context context, List<FaceBoxModel> searchlist, SearchAdapterlistener adapterlistener){
        this.context = context;
        this.adapterlistener = adapterlistener;
        this.Searchlist = searchlist;
        this.SearchlistFiltered = searchlist;
    }
    public interface SearchAdapterlistener {
        void onSearchSelected(FaceBoxModel model);
    }
}
