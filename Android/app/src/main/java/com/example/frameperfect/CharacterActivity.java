package com.example.frameperfect;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.http.OkHttpClientFactory;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.table.query.Query;
import com.microsoft.windowsazure.mobileservices.table.query.QueryOperations;
import com.microsoft.windowsazure.mobileservices.table.query.QueryOrder;
import com.microsoft.windowsazure.mobileservices.table.sync.MobileServiceSyncContext;
import com.microsoft.windowsazure.mobileservices.table.sync.MobileServiceSyncTable;
import com.microsoft.windowsazure.mobileservices.table.sync.localstore.ColumnDataType;
import com.microsoft.windowsazure.mobileservices.table.sync.localstore.MobileServiceLocalStoreException;
import com.microsoft.windowsazure.mobileservices.table.sync.localstore.SQLiteLocalStore;
import com.microsoft.windowsazure.mobileservices.table.sync.synchandler.SimpleSyncHandler;
import com.squareup.okhttp.OkHttpClient;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class CharacterActivity extends Activity {

    private MobileServiceClient mClient;
    //private MobileServiceTable<CharacterItem> mCharactersTable;
    private MobileServiceSyncTable<CharacterItem> mCharactersTable;
    private CharacterItemAdapter mAdapter;
    private String mGameId;
    private ProgressBar mProgressBar;
    private EditText mTextCharacterSearch;
    private String mSearchString;

    public CharacterActivity() {
    }

    public CharacterActivity(MobileServiceClient client, String gameId) {
        mClient = client;
        mGameId = gameId;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_characters);

        mProgressBar = (ProgressBar) findViewById(R.id.loadingCharactersProgressBar);
        mProgressBar.setVisibility(ProgressBar.GONE);
        mTextCharacterSearch = (EditText) findViewById(R.id.textCharacterSearch);
        mSearchString = "";

        try {
            // Create the Mobile Service Client instance, using the provided

            // Mobile Service URL and key
            mClient = new MobileServiceClient(
                    "https://frameperfect.azurewebsites.net",
                    this).withFilter(new ProgressFilter(mProgressBar));

            // Extend timeout from default of 10s to 20s
            mClient.setAndroidHttpClientFactory(new OkHttpClientFactory() {
                @Override
                public OkHttpClient createOkHttpClient() {
                    OkHttpClient client = new OkHttpClient();
                    client.setReadTimeout(20, TimeUnit.SECONDS);
                    client.setWriteTimeout(20, TimeUnit.SECONDS);
                    return client;
                }
            });
            //mCharactersTable = mClient.getTable(CharacterItem.class);
            mCharactersTable = mClient.getSyncTable(CharacterItem.class);
            mGameId = getIntent().getExtras().getString("gameId");

            // Offline Sync
            //mToDoTable = mClient.getSyncTable("ToDoItem", ToDoItem.class);

            //Init local storage
            initLocalStore().get();

            // Create an adapter to bind the items with the view
            mAdapter = new CharacterItemAdapter(this, R.layout.row_list_character);
            final ListView listViewToDo = (ListView) findViewById(R.id.listViewCharacter);
            listViewToDo.setAdapter(mAdapter);

            listViewToDo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg, View view,
                                        int position, long id) {
                    //createAndShowDialog("You selected : " + (listViewToDo.getItemAtPosition(position).toString()), "SELECTION *dab*");
                    openMoveActivity(((CharacterItem) listViewToDo.getItemAtPosition(position)).getId());
                }
            });

            // Load the items from the Mobile Service
            refreshItemsFromTable();

        } catch (Exception e){
            createAndShowDialog(e, "Error");
        }
        Log.d("OnCreateAfter", "END OF ONCREATE, NO ERRORS ???");
    }

    /** Called when the user taps the Send button */
    public void openMoveActivity(String characterId) {
        Intent intent = new Intent(this, MoveActivity.class);
        intent.putExtra("characterId", characterId);
        startActivity(intent);
    }

    private void refreshItemsFromTable() {
        Log.d("refreshItemsFromTable", "REFRESHING...");

        // Get the items that weren't marked as completed and add them in the
        // adapter

        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params) {

                try {
                    //final List<CharacterItem> results = refreshItemsFromMobileServiceTable();

                    //Offline Sync
                    final List<CharacterItem> results = sortItemsWithSearch(refreshItemsFromMobileServiceTableSyncTable());
                    Log.d("refreshItemsFromTable", ("SIZE OF RESULT : " + results.size()));

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.clear();

                            for (CharacterItem item : results) {
                                mAdapter.add(item);
                                Log.d("refreshItemsFromTable", "ITEM ADDED");
                            }
                        }
                    });
                } catch (final Exception e){
                    createAndShowDialogFromTask(e, "Error");
                }

                return null;
            }
        };

        runAsyncTask(task);
    }

    /*private List<CharacterItem> refreshItemsFromMobileServiceTable() throws ExecutionException, InterruptedException {
        Log.d("REFRESH TABLE", "MY GAME ID : " + mGameId);
        return mCharactersTable
                .where()
                .field("gameId").eq(mGameId)
                .orderBy("name", QueryOrder.Ascending)
                .execute().get();
    }*/
    private List<CharacterItem> refreshItemsFromMobileServiceTableSyncTable() throws ExecutionException, InterruptedException {
        //sync the data
        sync().get();
        Query query = QueryOperations.field("gameId").eq(mGameId);
        return mCharactersTable.read(query).get();
    }

    public List<CharacterItem> sortItemsWithSearch(final List<CharacterItem> items) {
        List<CharacterItem> toReturn = new ArrayList<CharacterItem>();

        for (CharacterItem item : items) {
            if (item.getName().toLowerCase().contains(mSearchString)) {
                toReturn.add(item);
            }
        }

        return (toReturn);
    }

    public void searchItems(View view) {
        if (mTextCharacterSearch != null) {
            mSearchString = mTextCharacterSearch.getText().toString();
        }
        else {
            mSearchString = "";
        }
        refreshItemsFromTable();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_refresh) {
            refreshItemsFromTable();
        }

        return true;
    }

    private void createAndShowDialog(Exception exception, String title) {
        Throwable ex = exception;
        if(exception.getCause() != null){
            ex = exception.getCause();
        }
        createAndShowDialog(ex.getMessage(), title);
    }

    private void createAndShowDialog(final String message, final String title) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(message);
        builder.setTitle(title);
        builder.create().show();
    }

    private void createAndShowDialogFromTask(final Exception exception, String title) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                createAndShowDialog(exception, "Error");
            }
        });
    }

    private AsyncTask<Void, Void, Void> runAsyncTask(AsyncTask<Void, Void, Void> task) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            return task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            return task.execute();
        }
    }

    private AsyncTask<Void, Void, Void> initLocalStore() throws MobileServiceLocalStoreException, ExecutionException, InterruptedException {

        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {

                    MobileServiceSyncContext syncContext = mClient.getSyncContext();

                    if (syncContext.isInitialized())
                        return null;

                    SQLiteLocalStore localStore = new SQLiteLocalStore(mClient.getContext(), "OfflineCharacters", null, 1);

                    Map<String, ColumnDataType> tableDefinition = new HashMap<String, ColumnDataType>();
                    tableDefinition.put("id", ColumnDataType.String);
                    tableDefinition.put("name", ColumnDataType.String);
                    tableDefinition.put("gameId", ColumnDataType.String);
                    tableDefinition.put("imgUrl", ColumnDataType.String);

                    localStore.defineTable("CharacterItem", tableDefinition);

                    SimpleSyncHandler handler = new SimpleSyncHandler();

                    syncContext.initialize(localStore, handler).get();

                } catch (final Exception e) {
                    createAndShowDialogFromTask(e, "Error");
                }

                return null;
            }
        };

        return runAsyncTask(task);
    }

    private AsyncTask<Void, Void, Void> sync() {
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    MobileServiceSyncContext syncContext = mClient.getSyncContext();
                    syncContext.push().get();
                    Query query = QueryOperations.field("gameId").eq(mGameId);
                    mCharactersTable.pull(query).get();
                } catch (final Exception e) {
                    createAndShowDialogFromTask(e, "Error");
                }
                return null;
            }
        };
        return runAsyncTask(task);
    }
}
