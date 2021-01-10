package com.dts.bookies.adapters.recycler;


import android.content.Context;
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

import java.util.List;

public class RecentSwapsAdapter extends RecyclerView.Adapter<RecentSwapsAdapter.ViewHolder> {
    private List<Book> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    public RecentSwapsAdapter(Context context, List<Book> books){
        this.mInflater = LayoutInflater.from(context);
        mData = books;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.holder_swap_item, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Book book = mData.get(position);
//        Log.d("aaa", "from adapter: " + book.toString()); // for debugging use...

        if(book.getImageLinks() == null) {
            Glide.with(mInflater.getContext())
                    .load(R.drawable.ic_launcher_background)
                    .centerCrop()
                    .into(holder.itemImageHolder);
        } else if(book.getImageLinks().getThumbnail() == null || book.getImageLinks().getThumbnail().equals("")) {
            Glide.with(mInflater.getContext())
                    .load(R.drawable.ic_launcher_background)
                    .centerCrop()
                    .into(holder.itemImageHolder);
        } else {
            Glide.with(mInflater.getContext())
                    .load(book.getImageLinks().getThumbnail())
                    .centerCrop()
                    .into(holder.itemImageHolder);
        }

        String itemName = "" + book.getTitle();
        // TODO: if(swap.getSwapStatus == "recieved") set image background to blue; else set to green;

        holder.itemNameHolder.setText(itemName);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView itemImageHolder;
        TextView itemNameHolder;
        ImageView itemSwapStateHolder;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemNameHolder = itemView.findViewById(R.id.list_item_TXT_bookTitle);
            itemImageHolder = itemView.findViewById(R.id.list_item_IMG_imageBook);
            itemSwapStateHolder = itemView.findViewById(R.id.list_item_IMG_swapState);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    public Book getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

}
