package edu.uoc.monuments.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import edu.uoc.library.LibraryManager;
import edu.uoc.library.calback.GetCallback;
import edu.uoc.library.model.Monument;
import edu.uoc.library.utils.LibraryConstants;
import edu.uoc.monuments.R;
import edu.uoc.monuments.ui.adapters.MonumentAdapter;
import edu.uoc.monuments.utils.ApplicationUtils;

/**
 * Created by UOC on 28/09/2016.
 */
public class ListMonumentActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    int request_code = 1;  // The request code

    // Views
    private ListView mListView;
    private ProgressBar mProgressBar;

    // List items
    private MonumentAdapter monumentAdapter;
    private ArrayList<Monument> monumentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_monuments);

        // Set views
        mListView = (ListView) findViewById(R.id.listView);
        mProgressBar = (ProgressBar) findViewById(R.id.load_progress);

        // Set listeners
        mListView.setOnItemClickListener(this);

        // UOC-BEGIN-CODE1
        LibraryManager.getInstance(getApplicationContext()).getAllMonuments(new GetCallback<List<Monument>>() {
            @Override
            public void onSuccess(List<Monument> result) {
                mProgressBar.setVisibility(View.GONE);
                Log.d(LibraryConstants.TAG, "Set breakpoint 1");
                monumentList.addAll(result);
                monumentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Throwable e) {
                mProgressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        // UOC-END-CODE1

        // UOC-BEGIN-CODE2
//        monumentList = LibraryManager.getInstance(getApplicationContext()).getAllMonuments();
//        Log.d(LibraryConstants.TAG, "Set breakpoint 2");
//        mProgressBar.setVisibility(View.GONE);
        // UOC-END-CODE2
        
        monumentAdapter = new MonumentAdapter(this, monumentList);
        mListView.setAdapter(monumentAdapter);
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
        if (id == R.id.action_logout) {
            ApplicationUtils.setUserLoginState(getApplicationContext(), false);
            startActivity(new Intent(this, LoginActivity.class));
            return true;
        }

        if (id == R.id.action_add) {
            ApplicationUtils.setUserLoginState(getApplicationContext(), false);

            Intent AddMonumentIntent = new Intent(this, AddMonumentActivity.class);
            startActivityForResult(AddMonumentIntent,request_code);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        startActivity(DetailMonumentActivity.makeIntent(this, monumentList.get(position).getId()));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == request_code) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                Log.d(LibraryConstants.TAG,"Resultado devuelto OK");
                monumentAdapter.notifyDataSetChanged(); // We use notifyDataSetChanged method to inform that the list data is modified and views must be refreshed

            } else {
                Log.d(LibraryConstants.TAG,"Resultado devuelto KO");
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        LibraryManager.getInstance(getApplicationContext()).getAllMonuments(new GetCallback<List<Monument>>() {
            @Override
            public void onSuccess(List<Monument> result) {
                mProgressBar.setVisibility(View.GONE);
                Log.d(LibraryConstants.TAG, "Set breakpoint 1");
                monumentList.clear();
                monumentList.addAll(result);
                monumentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Throwable e) {
                mProgressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
