package com.dts.bookies.adapters.recycler;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dts.bookies.R;
import com.dts.bookies.booksAPI.entities.Book;
import com.dts.bookies.booksAPI.entities.ImagesUri;
import com.dts.bookies.logic.boundaries.ItemBoundary;
import com.dts.bookies.logic.boundaries.subboundaries.UserIdBoundary;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private ItemBoundary [] itemArr;
    private LayoutInflater layoutInflater;
    private Context context;
    private String address;


    public ItemAdapter(ItemBoundary[] _stam){
    itemArr = _stam;
    }
    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    layoutInflater = LayoutInflater.from(parent.getContext());
    context = parent.getContext();

    //this view is passed to the view holder
    //and getItemCount item is used to create us the given array
    View view = layoutInflater.inflate(R.layout.list_item,parent,false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        ItemBoundary item = itemArr[position];
        Book bookDetails = (Book) new Gson().fromJson(new Gson().toJsonTree
                (item.getItemAttributes().get("googleBook")).getAsJsonObject(), Book.class);

        String itemName = "Name: " + bookDetails.getTitle();
        String itemAuthor = "Author: " + bookDetails.getAuthors();
        String itemGenre = "Genre: " + bookDetails.getCategories();
        String itemOwner = "Owner: " + ((UserIdBoundary) new Gson().fromJson(new Gson().toJsonTree
                (itemArr[position].getItemAttributes().get("owner")).getAsJsonObject(), UserIdBoundary.class)).getEmail();
        convertToAddress(itemArr[position].getLocation().getLat(), itemArr[position].getLocation().getLng());
        String BookImage = bookDetails.getImageLinks().getThumbnail();

        Glide.with(context)
                .load(BookImage)
                .centerCrop()
                .into(holder.itemImageHolder);

        holder.itemNameHolder.setText(itemName);
        holder.itemAuthorHolder.setText(itemAuthor);
        holder.itemGenreHolder.setText(itemGenre);
        holder.itemOwnerHolder.setText(itemOwner);
        holder.itemAddressHolder.setText(address);

    }
    private void convertToAddress(double lat, double lng){
        try {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            if(!addresses.isEmpty()) {
                address = addresses.get(0).getAddressLine(0);
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {
        return itemArr.length;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder{
        ImageView itemImageHolder;
        TextView itemNameHolder;
        TextView itemAuthorHolder;
        TextView itemOwnerHolder;
        TextView itemGenreHolder;
        TextView itemAddressHolder;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            itemNameHolder = itemView.findViewById(R.id.list_item_TXT_bookTitle);
            itemImageHolder = itemView.findViewById(R.id.list_item_IMG_imageBook);
            itemAuthorHolder = itemView.findViewById(R.id.list_item_TXT_bookAuthor);
            itemOwnerHolder = itemView.findViewById(R.id.list_item_TXT_owner);
            itemGenreHolder = itemView.findViewById(R.id.list_item_TXT_genre);
            itemAddressHolder = itemView.findViewById(R.id.list_item_TXT_Address);


        }
    }
}
