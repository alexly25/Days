package com.alex;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ShowNotesActivity extends FragmentActivity implements View.OnClickListener {

    private static final String LOG = "myLogShowNotesActivity";
    private ListView lvNotes;
    private Button btnDelete;
    private ArrayList<String> alNotes;
    private ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_notes);

        btnDelete = (Button) findViewById(R.id.btn_delete_note);
        btnDelete.setOnClickListener(this);

        lvNotes = (ListView) findViewById(R.id.lv_notes);

        Data data = new Data(new SQLite(this));

        String tag = getIntent().getStringExtra("tag");
        if (tag != null) {

            alNotes = data.getNotes(tag);

        } else {

            alNotes = data.getColumn(SqlVariables.TABLE_NOTES, SqlVariables.COLUMN_NOTE);

        }
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, alNotes);
        lvNotes.setAdapter(arrayAdapter);
        lvNotes.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

    }

    @Override
    protected void onRestart() {
        Log.d(LOG, "onRestart()");
        super.onRestart();

        Data data = new Data(new SQLite(this));

        if (data.getCount(SqlVariables.TABLE_NOTES) != arrayAdapter.getCount()) {
            alNotes = data.getColumn(SqlVariables.TABLE_NOTES, SqlVariables.COLUMN_NOTE);
            arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, alNotes);
            lvNotes.setAdapter(arrayAdapter);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_notes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.im_new_note:
                Log.d(LOG, "R.id.im_new_note");
                finish();
                startActivity(new Intent(this, NewNoteActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        Log.d(LOG, "onClick()");

        try {
            switch (view.getId()) {
                case R.id.btn_delete_note:

                    Data data = new Data(new SQLite(this));
                    SparseBooleanArray sbArray = lvNotes.getCheckedItemPositions();

                    boolean restart = false;
                    for (int i = 0; i < sbArray.size(); i++) {

                        int key = sbArray.keyAt(i);

                        if (lvNotes.isItemChecked(key)) {

                            String note = alNotes.get(key);

                            if (data.deleteNote(note)) {
                                restart = true;
                                Toast.makeText(this, "Deleted '" + note + "'", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(this, "Do not deleted '" + note + "'", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    if (restart) {
                        onRestart();
                    }

                    break;
            }

        } catch (Exception e) {
            Log.d(LOG, "onClick() " + e.toString());
        }
    }
}
