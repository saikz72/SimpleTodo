package com.example.simpletodo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<String> items;
    private Button btnAdd;
    private RecyclerView rvItems;
    private EditText edtItem;
    private ItemsAdapter itemsAdapter;
    public static final String KEY_ITEM_TEXT = "item_text";
    public static final String KEY_ITEM_POSITION = "item_position";
    public static final int EDIT_TEXT_CODE = 20;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        items = new ArrayList<>();
        btnAdd = findViewById(R.id.btnAdd);
        rvItems = findViewById(R.id.rvItems);
        edtItem = findViewById(R.id.edtItem);

        loadItems();

        //event when when item clicked for a long time
        ItemsAdapter.OnLongClickListener onLongClickListener = new ItemsAdapter.OnLongClickListener() {
            @Override
            public void onItemLongClicked(int position) {
                items.remove(position);//delete the item from the mode
                itemsAdapter.notifyItemRemoved(position);//notify the adapter
                Toast.makeText(getApplicationContext(), "Item was removed", Toast.LENGTH_SHORT).show(); //Feedback for when an item is removed
                saveItems();
            }
        };
        ItemsAdapter.OnClickListener onClickListener = new ItemsAdapter.OnClickListener() {
            @Override
            public void onItemClicked(int position) {
                //create the new activity
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                //pass the data being edited
                intent.putExtra(KEY_ITEM_TEXT, items.get(position));
                intent.putExtra(KEY_ITEM_POSITION, position);
                //display the activity
                startActivityForResult(intent, EDIT_TEXT_CODE);
            }
        };

        itemsAdapter = new ItemsAdapter(items, onLongClickListener, onClickListener);
        rvItems.setAdapter(itemsAdapter);
        rvItems.setLayoutManager(new LinearLayoutManager(this));

        //event when button is pressed
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String todItem = edtItem.getText().toString();
                items.add(todItem); //add item to the model
                itemsAdapter.notifyItemInserted(items.size() - 1); //notify adapter that an item is inserted at the last position
                edtItem.setText(""); //clear text once submitted
                Toast.makeText(getApplicationContext(), "item was added", Toast.LENGTH_SHORT).show(); //Feedback for when an item is added
                saveItems();
            }
        });
    }
    private File getDataFile(){
        return new File(getFilesDir(), "data.txt");
    }
    //this function will load items by reading every line of the data file
    private void loadItems(){
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("MainActivity", "Error reading items", e);
            items = new ArrayList<>();
        }
    }
    //this function saves items by writing them into the data file
    private void saveItems(){
        try {
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            Log.e("MainActivity", "Error writing items", e);
        }
    }

    //handle the result of the edit activity

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == EDIT_TEXT_CODE){
            //retrieve the updated text value
            String itemText = data.getStringExtra(KEY_ITEM_TEXT);
            //extract the original position of the edited item from the position key
            int position = data.getExtras().getInt((KEY_ITEM_POSITION));
            //update the model at the right position with new item text
            items.set(position, itemText);
            //notify the adapter
            itemsAdapter.notifyDataSetChanged();
            saveItems();
            Toast.makeText(getApplicationContext(), "Item updated successfully!!", Toast.LENGTH_SHORT).show();

        }else{
            Log.w("MainActivity", "Unknown call to onActivityResult");
        }
    }
}

