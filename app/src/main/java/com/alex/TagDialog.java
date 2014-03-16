package com.alex;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by alex on 08.03.14.
 */
public class TagDialog extends DialogFragment implements DialogInterface.OnClickListener {

    private final String LOG = "logTagDialog";

    EditText etTag;
    View v;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d(LOG, "onCreateDialog()");

        AlertDialog.Builder builder = null;

        try {

            builder = new AlertDialog.Builder(getActivity());

            // Подключаемся к dialog_new_tagml
            LayoutInflater inflater = getActivity().getLayoutInflater();
            v = inflater.inflate(R.layout.dialog_new_tag, null);

            etTag = (EditText) v.findViewById(R.id.et_new_tag);

            builder.setView(v)
                    .setTitle("New tag")
                    .setPositiveButton("OK", this)
                    .setNegativeButton("Cancel", this);

        } catch (Exception e) {
            Log.d(LOG, "!!!!!onCreteDialog " + e.toString());
        }

        return builder.create();
    }

    /**
     * Метод обрабатывает нажатие кнопки "Задать"
     */
    @Override
    public void onClick(DialogInterface dialog, int which) {
        Log.d(LOG, "onClick()");

        String tag = etTag.getText().toString();

        if (which == Dialog.BUTTON_POSITIVE && !tag.equals("")) {

            Data data = new Data(new SQLite(v.getContext()));
            if(data.addTag(tag)){
                ((ShowTagActivity) getActivity()).onRestart();
                Toast.makeText(v.getContext(), "Added tag '" + tag + "'", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(v.getContext(), "Do not create tag '" + tag + "'", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

