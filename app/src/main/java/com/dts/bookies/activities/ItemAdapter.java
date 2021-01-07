package com.dts.bookies.activities;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dts.bookies.R;
import com.dts.bookies.logic.boundaries.ItemBoundary;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private ItemBoundary [] itemArr;
    private LayoutInflater layoutInflater;

    public ItemAdapter(ItemBoundary[] _stam){
    itemArr = _stam;
    }
    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    layoutInflater = LayoutInflater.from(parent.getContext());

    //this view is passed to the view holder
    //and getItemCount item is used to create us the given array
    View view = layoutInflater.inflate(R.layout.list_item,parent,false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        String itemName = " Name: " + itemArr[position].getName();
        String itemAuthor = " Author: " +(String) itemArr[position].getItemAttributes().get("author");
        String itemGenre = " Genre: " + (String) itemArr[position].getItemAttributes().get("genre");
        String itemOwner = " Owner: " + (String) itemArr[position].getItemAttributes().get("owner");

        holder.itemNameHolder.setText(itemName);
        holder.itemAuthorHolder.setText(itemAuthor);
        holder.itemAuthorHolder.setText(itemGenre);
        holder.itemOwnerHolder.setText(itemOwner);

    }

    @Override
    public int getItemCount() {
        return itemArr.length;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder{
        ImageView ItemImageHolder;
        TextView itemNameHolder;
        TextView itemAuthorHolder;
        TextView itemOwnerHolder;
        TextView itemGenreHolder;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            itemNameHolder = itemView.findViewById(R.id.list_item_TXT_itemBookName);
            ItemImageHolder = itemView.findViewById(R.id.list_item_IMG_imageBook);
            itemAuthorHolder = itemView.findViewById(R.id.list_item_TXT_itemBookAuthor);
            itemOwnerHolder = itemView.findViewById(R.id.list_item_TXT_owner);
            itemGenreHolder = itemView.findViewById(R.id.list_item_TXT_genre);


        }
    }
}
