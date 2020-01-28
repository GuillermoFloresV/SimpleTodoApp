package com.example.simpletodo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FileUtils;

public class MainActivity extends AppCompatActivity {


   List<String> items;

    Button btnAdd;
    EditText editItem;
    RecyclerView rvItems;
    itemsAdapter itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btnAdd = findViewById(R.id.btnAdd);
        editItem = findViewById(R.id.editItem);
        rvItems = findViewById(R.id.rvItem);

        loadItems();

        itemsAdapter.OnLongClickListener onLongClickListener = new itemsAdapter.OnLongClickListener()
        {
            @Override
            public void onItemLongClicked(int position) {
                //delete the item from the list
                items.remove(position);
                //notify the adapter
                itemsAdapter.notifyItemRemoved(position);
                Toast.makeText(getApplicationContext(), "Item was removed!", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        };

        itemsAdapter = new itemsAdapter(items, onLongClickListener);
        rvItems.setAdapter(itemsAdapter);
        rvItems.setLayoutManager(new LinearLayoutManager(this));

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String todoText = editItem.getText().toString();
                //add item to the view
                if(todoText.length() > 1)
                {
                    items.add(todoText);
                    //notify the adapter that an item has been added
                    itemsAdapter.notifyItemInserted(items.size()-1);
                    editItem.setText("");
                    Toast.makeText(getApplicationContext(), "Item was added", Toast.LENGTH_SHORT).show();
                    saveItems();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Can't add an item with less than 1 character!", Toast.LENGTH_SHORT). show();
                }
            }
        });
    }

    private File getDataFile()
    {
        return new File(getFilesDir(), "data.txt");
    }
    //this func will load items by reading the lines in out data.txt file
    private void loadItems()
    {
        try
        {
            items = new ArrayList<>(org.apache.commons.io.FileUtils.readLines(getDataFile(),Charset.defaultCharset()));
        }
        catch (IOException e)
        {
            Log.e("MainActivity", "Error reading items", e);
            items = new ArrayList<>();
        }
    }
    //this func saves items written by the user into the data.txt file
    private void saveItems() {
        try
        {
            FileUtils.writeLines(getDataFile(), items);
        }
        catch (IOException e)
        {
            Log.e("MainActivity", "Error writing items", e);
        }
    }
}
