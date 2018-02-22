package com.example.lenovo.noteapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    static ArrayList<String> notes = new ArrayList<>();
    static ArrayAdapter arrayAdapter;
    static Set<String> set;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listview = (ListView) findViewById(R.id.listview);
        SharedPreferences sharedPreferences = this.getSharedPreferences("com.example.lenovo.noteapp" , Context.MODE_PRIVATE);
        set = sharedPreferences.getStringSet("notes" , null);
        notes.clear();

        if(set != null)
        {
            notes.addAll(set);
        }
        else
        {
            notes.add("Example Note");
            set = new HashSet<>();
            set.addAll(notes);
            sharedPreferences.edit().putStringSet("notes" , set).apply();
        }


        arrayAdapter = new ArrayAdapter(this , android.R.layout.simple_list_item_1 , notes);
        listview.setAdapter(arrayAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getApplicationContext() , EditYourNote.class);
                i.putExtra("noteid" , position);
                startActivity(i);
            }
        });


        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Are You Sure?")
                        .setMessage("Do You Want To Delete This Note?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                notes.remove(position);
                                SharedPreferences sharedPreferences = MainActivity.this.getSharedPreferences("com.example.lenovo.noteapp" , Context.MODE_PRIVATE);
                                if (set == null)
                                {
                                    set = new HashSet<String>();
                                }
                                else
                                {
                                    set.clear();
                                }
                                set.addAll(notes);
                                sharedPreferences.edit().remove("notes").apply();
                                sharedPreferences.edit().putStringSet("notes" , set).apply();
                                arrayAdapter.notifyDataSetChanged();

                            }
                        }).setNegativeButton("No" , null)
                        .show();

                return true;
            }
        });


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
        if (id == R.id.add) {

            notes.add("");
            SharedPreferences sharedPreferences = this.getSharedPreferences("com.example.lenovo.noteapp" , Context.MODE_PRIVATE);

            if (set == null)
            {
                set = new HashSet<String>();
            }
            else
            {
                set.clear();
            }

            set.addAll(notes);
            sharedPreferences.edit().remove("notes").apply();
            sharedPreferences.edit().putStringSet("notes" , set).apply();
            arrayAdapter.notifyDataSetChanged();

            Intent i = new Intent(getApplicationContext() , EditYourNote.class);
            i.putExtra("noteid" , notes.size() -1);
            startActivity(i);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
