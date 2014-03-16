package com.alex;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ShowNoteOfTagActivity extends FragmentActivity {

    private static final String LOG = "myLogShowNodeOfTag";

    private TextView tvNotes;
    private String tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG, "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_note_for_tag);

        tvNotes = (TextView) findViewById(R.id.tv_notes);
        tag = getIntent().getStringExtra("tag");
        setTitle(tag);
        setNotes(tag);

    }

    @Override
    protected void onRestart() {
        super.onRestart();

        setNotes(tag);
    }

    private void setNotes(String tag) {
        Log.d(LOG, "setNote()");

        tvNotes.setText("");
        ArrayList<String> alNotes = new Data(new SQLite(this)).getNotes(tag);

        if (alNotes != null) {
            for (String note : alNotes) {
                tvNotes.append(note + ", ");
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tag, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        Intent intent = null;

        switch (item.getItemId()) {
            case R.id.im_add_note:
                Log.d(LOG, "R.id.im_add_note");

                intent = new Intent(this, NewNoteActivity.class);
                intent.putExtra("tag", tag);
                startActivity(intent);

                break;
            case R.id.im_delete_tag:
                Log.d(LOG, "R.id.im_delete_tag");

                Data data = new Data(new SQLite(this));
                if(data.deleteNotes(tag) && data.deleteTag(tag)){
                    Toast.makeText(this, "Deleted tag " + tag, Toast.LENGTH_SHORT).show();

                    finish();
                } else {
                    Toast.makeText(this, "Do not deleted tag " + tag, Toast.LENGTH_SHORT).show();

                }

                break;
            case R.id.im_edit_tag:

                intent = new Intent(this, ShowNotesActivity.class);
                intent.putExtra("tag", tag);
                startActivity(intent);

                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
