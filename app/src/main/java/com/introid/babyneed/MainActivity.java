package com.introid.babyneed;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.introid.babyneed.data.DatabaseHandler;
import com.introid.babyneed.model.Item;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;


public class MainActivity extends AppCompatActivity {
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private Button saveButton;
    private EditText babyItem;
    private EditText itemQuantity;
    private EditText itemColor;
    private EditText itemSize;
    DatabaseHandler databaseHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        databaseHandler = new DatabaseHandler(MainActivity.this);
        byPassActivity();

        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPopupDialog();
            }


//            public void onClick(View view) { Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
        });
    }

    private void byPassActivity() {
        if (databaseHandler.getItemCount() > 0){
            startActivity(new Intent(MainActivity.this,ListActivity.class));
            finish();
        }
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

                dialog.dismiss();
                startActivity(new Intent(MainActivity.this,ListActivity.class));
            }
        },1200);
    }

    public void createPopupDialog(){
        Builder builder= new Builder(this);
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
        dialog= builder.create();
        dialog.show();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
