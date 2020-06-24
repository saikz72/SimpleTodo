package com.example.simpletodo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

//responsible for displaying data from the model into a row in the recycler view
public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder>{

    public interface OnLongClickListener{
        void onItemLongClicked(int position);
    }

    public interface OnClickListener{
        void onItemClicked(int position);
    }

    private List<String> items;
    private OnLongClickListener longClickListener;
    private OnClickListener clickListener;

    public ItemsAdapter(List<String> items, OnLongClickListener longClickListener, OnClickListener clickListener) {
        this.items = items;
        this.longClickListener = longClickListener;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //use layout inflater to inflate a view
        View todoView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        //wrap it inside a view holder and return it
        return new ViewHolder(todoView);
    }

    //responsible for binding data to a particular view holder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //grab the item at the position
        String item = items.get(position);
        //bind the item into the specified view holder
        holder.bind(item);
    }

    //tells the RV how many items are in the list
    @Override
    public int getItemCount() {
        return items.size();
    }

    //container to provide easy access to views that represent each row of the list
    class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItem = itemView.findViewById(android.R.id.text1);
        }
        //update the view inside of the view holder with this data
        public void bind(String item) {
            tvItem.setText(item);
            //event for when an item is pressed a long time
            tvItem.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    //notify the listener which is long pressed
                    longClickListener.onItemLongClicked(getAdapterPosition());
                    return true;
                }
            });
            tvItem.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    clickListener.onItemClicked(getAdapterPosition());
                }
            });

        }
    }
}
