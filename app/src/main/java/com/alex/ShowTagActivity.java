package com.alex;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ShowTagActivity extends FragmentActivity implements AdapterView.OnItemClickListener {

    private static final String LOG = "myLogShowTagActivity";
    private ListView lvTag;
    private ArrayList<String> alTag;
    private ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG, "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_tag);

        alTag = new Data(new SQLite(this)).getColumn(SqlVariables.TABLE_TAGS, SqlVariables.COLUMN_TAG);
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, alTag);

        lvTag = (ListView) findViewById(R.id.lv_tag);
        lvTag.setAdapter(arrayAdapter);
        lvTag.setOnItemClickListener(this);

        new Data(new SQLite(this)).outTables();
    }

    @Override
    protected void onRestart() {
        Log.d(LOG, "onRestart()");
        super.onRestart();

        Data data = new Data(new SQLite(this));

        if(data.getCount(SqlVariables.TABLE_TAGS) != arrayAdapter.getCount()) {
            alTag = data.getColumn(SqlVariables.TABLE_TAGS, SqlVariables.COLUMN_TAG);
            arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, alTag);
            lvTag.setAdapter(arrayAdapter);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tags, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()){
            case R.id.im_new_note:
                Log.d(LOG, "R.id.im_new_note");
                startActivity(new Intent(this, NewNoteActivity.class));
                break;
            case R.id.im_show_notes:
                Log.d(LOG, "R.id.im_show_notes");
                startActivity(new Intent(this, ShowNotesActivity.class));
                break;
            case R.id.im_new_tag:
                Log.d(LOG, "R.id.im_new_tag");
                new TagDialog().show(getSupportFragmentManager(), null);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Log.d(LOG, "onItemClick()");

        TextView tvSelectTag = (TextView) view.findViewById(android.R.id.text1);
        Intent intent = new Intent(this,ShowNoteOfTagActivity.class);
        intent.putExtra("tag", tvSelectTag.getText().toString());
        startActivity(intent);

    }
}
