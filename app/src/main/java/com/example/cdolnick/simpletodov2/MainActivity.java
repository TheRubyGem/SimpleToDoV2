package com.example.cdolnick.simpletodov2;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {
    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;
    Integer itemPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvItems = (ListView) findViewById(R.id.lvItems);
        readItems();
        itemsAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);
        setupListViewListener();
        setupOnClickListener();
    }

    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapter, View item, int pos, long id) {
                        items.remove(pos);
                        itemsAdapter.notifyDataSetChanged();
                        writeItems();
                        return true;
                    }
                }
        );
    }

    public void onAddItem(View v){
       EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
       String itemText= etNewItem.getText().toString();
       itemsAdapter.add(itemText);
       etNewItem.setText("");
       writeItems();
    }

    private void readItems() {
        File  filesDir = getFilesDir();
        File  todoFile = new File(filesDir, "todo.text");
        try {
            items = new ArrayList<String>(FileUtils.readLines(todoFile));
        } catch (IOException e) {
            items = new ArrayList<String>();
        }
    }

    private void writeItems(){
       File filesDir = getFilesDir();
       File todoFile = new File(filesDir,"todo.txt");
       try {
           FileUtils.writeLines(todoFile, items);
       } catch (IOException e){
           e.printStackTrace();
       }}

    private final int REQUEST_CODE = 7;

    private void setupOnClickListener() {
        lvItems.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapter, View item, int pos, long id) {
                    String editMe = (String) lvItems.getItemAtPosition(pos);
                    itemPos = pos;
                    Intent i = new Intent(MainActivity.this, EditItemActivity.class);
                    i.putExtra("editText",editMe);
                    startActivityForResult(i, REQUEST_CODE);
                    }
                }
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      if (resultCode == RESULT_OK && requestCode == REQUEST_CODE){
      String newItem = data.getExtras().getString("updatedItem");
      Toast.makeText(this, "You have updated to: " + newItem, Toast.LENGTH_SHORT).show();
      itemsAdapter.remove(itemsAdapter.getItem(itemPos));
      itemsAdapter.insert(newItem,itemPos);
      }
    }
    public void onSubmit(View v) {
        // closes the activity and returns to first screen
        this.finish();
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
