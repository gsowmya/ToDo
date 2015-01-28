package aasan.com.todo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class ListDetails extends ActionBarActivity {

    EditText txtDetails;
    Button save, delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_details);
        txtDetails = (EditText) findViewById(R.id.txtEdit);
        save = (Button) findViewById(R.id.btnSave);
        delete = (Button) findViewById(R.id.btnDelete);
        Intent i = getIntent();
        final String details = i.getStringExtra("Details");
        final int position = i.getIntExtra("Position", 0);
        txtDetails.setText(details);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newString = txtDetails.getText().toString();

                done("save", newString, position);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                done("delete", details, position);
            }
        });

    }

    private void done(String operation, String updatedString, int position) {
        Intent intent = getIntent();
        intent.putExtra("operation", operation);
        intent.putExtra("string", updatedString);
        intent.putExtra("position", position);
        setResult(100, intent);
        finish();
    }

    private List<String> getDataFromSharedPreferences() {
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("todo", Context.MODE_PRIVATE);
        Set<String> stringSet = prefs.getStringSet("data", null);
        List<String> stringList = new ArrayList<String>();
        for (String s : stringSet) {
            stringList.add(s);
        }
        return stringList;
    }

    private void updateSharedPreferences(List<String> stringList) {
        Set<String> stringSet = new HashSet<String>();
        for (String s : stringList) {
            stringSet.add(s);
        }
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("todo", Context.MODE_PRIVATE);
        SharedPreferences.Editor e = prefs.edit();
        e.putStringSet("data", stringSet);
        e.commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list_details, menu);
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
