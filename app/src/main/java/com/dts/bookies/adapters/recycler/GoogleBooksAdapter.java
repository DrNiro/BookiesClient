package com.dts.bookies.adapters.recycler;


import android.content.Context;
import android.util.Log;
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
import com.dts.bookies.booksAPI.entities.Result;

import java.util.List;

public class GoogleBooksAdapter extends RecyclerView.Adapter<GoogleBooksAdapter.ViewHolder> {
    private List<Result> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    public GoogleBooksAdapter(Context context, List<Result> books){
        this.mInflater = LayoutInflater.from(context);
        mData = books;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.add_book_item, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Book book = mData.get(position).getBook();
//        Log.d("aaa", "from adapter: " + book.toString()); // for debugging use...

        StringBuilder authors = new StringBuilder("");
        if(book.getAuthors() != null) {
            if(!book.getAuthors().get(0).equals("."))
                for (String author : book.getAuthors()) authors.append(author + ", ");
            else
                authors.append("No known authors.  ");
        } else {
            authors.append("No known authors.  ");
        }
        String itemName = "" + book.getTitle();
        String itemAuthor = "Authors: " + authors.substring(0, authors.length()-2);
        StringBuilder categories = new StringBuilder("");
        if(book.getCategories() != null)
            for (String category : book.getCategories()) categories.append(category + ", ");
        else
            categories.append("None  ");
        String itemGenre = "Genre: " + categories.substring(0, categories.length()-2);
        String itemLangAndDate = book.getLanguage() + ", " + book.getPublishedDate();

        if(book.getImageLinks() == null) {
            Glide.with(mInflater.getContext())
                    .load(R.drawable.ic_launcher_background)
                    .centerCrop()
                    .into(holder.ItemImageHolder);
        } else if(book.getImageLinks().getThumbnail() == null || book.getImageLinks().getThumbnail().equals("")) {
            Glide.with(mInflater.getContext())
                    .load(R.drawable.ic_launcher_background)
                    .centerCrop()
                    .into(holder.ItemImageHolder);
        } else {
            Glide.with(mInflater.getContext())
                    .load(book.getImageLinks().getThumbnail())
                    .centerCrop()
                    .into(holder.ItemImageHolder);
        }

        holder.itemNameHolder.setText(itemName);
        holder.itemAuthorHolder.setText(itemAuthor);
        holder.itemGenreHolder.setText(itemGenre);
        holder.itemLangAndDateHolder.setText(itemLangAndDate);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView ItemImageHolder;
        TextView itemNameHolder;
        TextView itemAuthorHolder;
        TextView itemLangAndDateHolder;
        TextView itemGenreHolder;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemNameHolder = itemView.findViewById(R.id.list_item_TXT_bookTitle);
            ItemImageHolder = itemView.findViewById(R.id.list_item_IMG_imageBook);
            itemAuthorHolder = itemView.findViewById(R.id.list_item_TXT_bookAuthor);
            itemLangAndDateHolder = itemView.findViewById(R.id.list_item_TXT_langAndDate);
            itemGenreHolder = itemView.findViewById(R.id.list_item_TXT_subject);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    public Result getItem(int id) {
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
