package com.alex;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by alex on 13.02.14.
 */
public class NewNoteActivity extends FragmentActivity implements View.OnClickListener {

    private static final String LOG = "myLogNewNodeActivity";
    private Button btnAdd;
    private ListView lvTag;
    private EditText etNote;
    private ArrayList<String> alTag;
    private ArrayAdapter<String> arrayAdapter;

    String selectedTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG, "onCreate()");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);

        btnAdd = (Button) findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(this);


        etNote = (EditText) findViewById(R.id.et_new_note);

        alTag = new Data(new SQLite(this)).getColumn(SqlVariables.TABLE_TAGS, SqlVariables.COLUMN_TAG);
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, alTag);

        lvTag = (ListView) findViewById(R.id.lv_select_tag);
        lvTag.setAdapter(arrayAdapter);
        lvTag.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        String tag = getIntent().getStringExtra("tag");
        if (tag != null) {
            int indexTag = alTag.indexOf(tag);
            lvTag.setItemChecked(indexTag, true);
        }

    }

    @Override
    protected void onRestart() {
        Log.d(LOG, "onRestart()");
        super.onRestart();

        Data data = new Data(new SQLite(this));

        if(data.getCount(SqlVariables.TABLE_TAGS) != arrayAdapter.getCount()) {
            alTag = data.getColumn(SqlVariables.TABLE_TAGS, SqlVariables.COLUMN_TAG);
            arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, alTag);
            lvTag.setAdapter(arrayAdapter);
        }
    }

    @Override
    public void onClick(View view) {
        Log.d(LOG, "onClick");
        try {
            switch (view.getId()) {
                case R.id.btn_add:
                    Log.d(LOG, "R.id.btnAdd");

                    SparseBooleanArray sbArray = lvTag.getCheckedItemPositions();
                    Data data = new Data(new SQLite(this));
                    String note = etNote.getText().toString();

                    if (!note.equals("")) {

                        for (int i = 0; i < sbArray.size(); i++) {

                            int key = sbArray.keyAt(i);

                            if(lvTag.isItemChecked(key)) {

                                String tag = alTag.get(key);
                                Note newNote = new Note(tag, note);

                                if (data.addNote(newNote)) {
                                    Toast.makeText(this, "Added note with teg '" + tag + "'", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(this, "Do not add", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }

                    break;
            }
        } catch (Exception e) {
            Log.d(LOG, "error: " + e);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_others, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (item.getItemId()) {
            case R.id.im_show_notes:
                Log.d(LOG, "R.id.im_show_notes");
                finish();
                startActivity(new Intent(this, ShowNotesActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
