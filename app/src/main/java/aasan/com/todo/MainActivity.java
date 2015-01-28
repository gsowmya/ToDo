package aasan.com.todo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class MainActivity extends ActionBarActivity {

    ListView lstTodo;
    Button add;
    EditText txtAddNew;
    List<String> listItems = new ArrayList<String>();
    ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lstTodo = (ListView) findViewById(R.id.lstTodo);
        add = (Button) findViewById(R.id.btnAdd);
        txtAddNew = (EditText) findViewById(R.id.txtNewItem);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItems);
        lstTodo.setAdapter(adapter);

        txtAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtAddNew.setText("");
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtAddNew.getText() != null) {
                    String item = txtAddNew.getText().toString();
                    txtAddNew.setText("");
                    listItems.add(item);
                    updateSharedPreferences(listItems);
                    adapter.notifyDataSetChanged();
                }
            }
        });
        lstTodo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object obj = parent.getItemAtPosition(position);
                String s = (String) obj;
                int requestCode = 100;
                Intent i = new Intent(MainActivity.this, ListDetails.class);
                i.putExtra("Details", s);
                i.putExtra("Position", position);
                startActivityForResult(i, requestCode);
            }
        });
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            String operation = data.getStringExtra("operation");
            String newString = data.getStringExtra("string");
            int position = data.getIntExtra("position", 0);

            if ("save".equalsIgnoreCase(operation)) {
                listItems.set(position, newString);
            } else {
                listItems.remove(position);
            }
            updateSharedPreferences(listItems);
            adapter.notifyDataSetChanged();
        }
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
