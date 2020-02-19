package com.introid.babyneed;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.introid.babyneed.Adapter.RecyclerViewAdapter;
import com.introid.babyneed.data.DatabaseHandler;
import com.introid.babyneed.model.Item;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {
    public RecyclerView recyclerView;
    public RecyclerViewAdapter recyclerViewAdapter;
    List<Item> itemList;
    public DatabaseHandler databaseHandler;
    public FloatingActionButton fab;
    public AlertDialog.Builder builder;
    public AlertDialog alertDialog;
    private Button saveButton;
    private EditText babyItem;
    private EditText itemQuantity;
    private EditText itemColor;
    private EditText itemSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        View view;
        fab= findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup();
            }
        });

        databaseHandler= new DatabaseHandler(this);
        recyclerView= findViewById(R.id.recyclerView);
        boolean b = true;
        recyclerView.setHasFixedSize(b);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemList= new ArrayList<>();
        itemList= databaseHandler.getAllItems();
        for (Item item : itemList){
            Log.d("main", "onCreate: "+ item.getItemName());
        }
        recyclerViewAdapter=new RecyclerViewAdapter(this,itemList);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();
    }

    private void showPopup() {
        Context context;
        builder= new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup,null);
        babyItem= view.findViewById(R.id.babyItem);
        itemQuantity= view.findViewById(R.id.itemQuantity);
        itemColor= view.findViewById(R.id.itemColor);
        itemSize= view.findViewById(R.id.itemSize);
        saveButton = view.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!babyItem.getText().toString().isEmpty() &&
                        !itemColor.getText().toString().isEmpty() && !itemQuantity.getText()
                        .toString().isEmpty() && !itemSize.getText().toString().isEmpty()){
                    saveItem(v);
                }else
                    Snackbar.make(v,"Please Fill All Fields",Snackbar.LENGTH_SHORT).show();
            }
        });

        builder.setView(view);
        alertDialog= builder.create();
        alertDialog.show();
    }

    private void saveItem(View v) {
        Item item=new Item();

        String newItem= babyItem.getText().toString().trim();
        String newColor= itemColor.getText().toString().trim();
        int size= Integer.parseInt(itemSize.getText().toString().trim());
        int quantity= Integer.parseInt(itemQuantity.getText().toString().trim());



        item.setItemName(newItem);
        item.setItemColor(newColor);
        item.setItemSize(size);
        item.setItemQuantity(quantity);

        databaseHandler.addItem(item);
        Snackbar.make(v,"item saved ", Snackbar.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                alertDialog.dismiss();
                startActivity(new Intent(ListActivity.this,ListActivity.class));
                finish();
            }
        },1200);
    }

}
