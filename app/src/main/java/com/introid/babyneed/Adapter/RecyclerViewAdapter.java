package com.introid.babyneed.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.introid.babyneed.R;
import com.introid.babyneed.data.DatabaseHandler;
import com.introid.babyneed.model.Item;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    public Context context;
    public List<Item> itemList;
    AlertDialog.Builder builder;
    AlertDialog dialog;
    LayoutInflater inflater;

    public RecyclerViewAdapter(Context context, List<Item> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row,parent,false);

        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Item item =itemList.get(position);
        viewHolder.itemName.setText("Item: "+item.getItemName());
        viewHolder.itemColor.setText("Color: "+ item.getItemColor());
        viewHolder.itemsize.setText("Size: "+(String.valueOf(item.getItemSize())));
        viewHolder.quantity.setText("Quantity: "+(String.valueOf(item.getItemQuantity())));
        viewHolder.dateAdded.setText("Time: "+item.getDateItemAdded());


    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView itemName;
        public TextView itemColor;
        public TextView quantity;
        public TextView itemsize;
        public TextView dateAdded;
        public Button edit;
        public Button delete;
        public int id;
        public ViewHolder(@NonNull View itemView ,Context c) {
            super(itemView);
            context=c;
            itemName=itemView.findViewById(R.id.item_name);
            itemColor= itemView.findViewById(R.id.item_color);
            quantity= itemView.findViewById(R.id.item_quantity);
            itemsize= itemView.findViewById(R.id.item_size);
            dateAdded=itemView.findViewById(R.id.item_date);
            edit= itemView.findViewById(R.id.edit);
            delete=itemView.findViewById(R.id.delete);

            edit.setOnClickListener(this);
            delete.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            switch(v.getId()){
                case R.id.edit:
                    editItem();
                    break;
                case R.id.delete:
                 int position= getAdapterPosition();
                    Item item= itemList.get(position);
                    deleteItem(item.getId());
                    break;
            }
        }
        private void deleteItem(final int id){
            builder=new AlertDialog.Builder(context);
            inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.cinfirmation_pop,null);
            Button noButton= view.findViewById(R.id.conf_no_btn);
            Button yesButton= view.findViewById(R.id.conf_yes_btn);

            builder.setView(view);
            dialog= builder.create();
            dialog.show();

            yesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseHandler db= new DatabaseHandler(context);
                    db.deleteItem(id);
                    itemList.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());

                    dialog.dismiss();
                }
            });
            noButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

        }
    }

    private void editItem() {

        builder= new AlertDialog.Builder(context);
        inflater= LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.popup,null);


        Button saveButton;
        EditText babyItem;
        EditText itemQuantity;
        EditText itemColor;
        EditText itemSize;
        TextView title;
        babyItem= view.findViewById(R.id.babyItem);
        itemQuantity= view.findViewById(R.id.itemQuantity);
        itemColor= view.findViewById(R.id.itemColor);
        itemSize= view.findViewById(R.id.itemSize);
        saveButton = view.findViewById(R.id.saveButton);
        title= view.findViewById(R.id.title);
        title.setText(R.string.edit_item);

        builder.setView(view);
        dialog= builder.create();
        dialog.show();
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
