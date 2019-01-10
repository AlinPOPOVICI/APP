package com.example.alin.app1;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;



public class LoreActivity extends Activity {

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        String word=getIntent().getStringExtra(WidgetProvider.EXTRA_WORD);

        if (word==null) {
            word="We did not get a word!";
        }

        Toast.makeText(this, word, Toast.LENGTH_LONG).show();
        finish();
    }
}