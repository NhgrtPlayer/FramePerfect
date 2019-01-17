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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TableLayout;

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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class MoveActivity extends Activity {

    private MobileServiceClient mClient;
    //private MobileServiceTable<MoveItem> mMovesTable;
    private MobileServiceSyncTable<MoveItem> mMovesTable;
    private MoveItemAdapter mAdapter;
    private String mCharacterId;
    private ProgressBar mProgressBar;

    public MoveActivity() {
    }

    public MoveActivity(MobileServiceClient client, String gameId) {
        mClient = client;
        mCharacterId = gameId;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moves);

        mProgressBar = (ProgressBar) findViewById(R.id.loadingMovesProgressBar);
        mProgressBar.setVisibility(ProgressBar.GONE);
        try {
            // Create the Mobile Service Client instance, using the provided

            // Mobile Service URL and key
            mClient = new MobileServiceClient(
                    "https://frameperfect.azurewebsites.net",
                    this).withFilter(new ProgressFilter(mProgressBar));;

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
            //mMovesTable = mClient.getTable(MoveItem.class);
            mCharacterId = getIntent().getExtras().getString("characterId");

            // Offline Sync
            mMovesTable = mClient.getSyncTable(MoveItem.class);

            //Init local storage
            initLocalStore().get();

            // Create an adapter to bind the items with the view
            mAdapter = new MoveItemAdapter(this, R.layout.row_list_move);
            final ListView listViewToDo = (ListView) findViewById(R.id.listViewMove);
            listViewToDo.setAdapter(mAdapter);

            // Load the items from the Mobile Service
            refreshItemsFromTable();


            listViewToDo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg, View view,
                                        int position, long id) {
                    openMoveDetailsActivity((MoveItem) listViewToDo.getItemAtPosition(position));
                }
            });
        } catch (Exception e){
            createAndShowDialog(e, "Error");
        }
        Log.d("OnCreateAfter", "END OF ONCREATE, NO ERRORS ???");
    }

    private void openMoveDetailsActivity(MoveItem moveItem) {
        Intent intent = new Intent(this, MoveDetailsActivity.class);
        intent.putExtra("moveItem", moveItem);
        startActivity(intent);
    }

    private void refreshItemsFromTable() {
        Log.d("refreshItemsFromTable", "REFRESHING...");

        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params) {

                try {
                    //final List<MoveItem> results = refreshItemsFromMobileServiceTable();

                    //Offline Sync
                    final List<MoveItem> results = refreshItemsFromMobileServiceTableSyncTable();
                    Log.d("refreshItemsFromTable", ("SIZE OF RESULT : " + results.size()));

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.clear();

                            for (MoveItem item : results) {
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

    /*private List<MoveItem> refreshItemsFromMobileServiceTable() throws ExecutionException, InterruptedException {
        Log.d("REFRESH TABLE", "MY GAME ID : " + mCharacterId);
        return mMovesTable
                .where()
                .field("characterId").eq(mCharacterId)
                .orderBy("name", QueryOrder.Ascending)
                .execute().get();
    }*/
    private List<MoveItem> refreshItemsFromMobileServiceTableSyncTable() throws ExecutionException, InterruptedException {
        //sync the data
        sync().get();
        Query query = QueryOperations.field("characterId").eq(mCharacterId);
        return mMovesTable.read(query).get();
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

                    SQLiteLocalStore localStore = new SQLiteLocalStore(mClient.getContext(), "OfflineMoves", null, 1);

                    Map<String, ColumnDataType> tableDefinition = new HashMap<String, ColumnDataType>();
                    tableDefinition.put("id", ColumnDataType.String);
                    tableDefinition.put("name", ColumnDataType.String);
                    tableDefinition.put("characterId", ColumnDataType.String);
                    tableDefinition.put("moveType", ColumnDataType.String);
                    tableDefinition.put("damage", ColumnDataType.String);
                    tableDefinition.put("startUp", ColumnDataType.String);
                    tableDefinition.put("active", ColumnDataType.String);
                    tableDefinition.put("recovery", ColumnDataType.String);
                    tableDefinition.put("frameAdvantage", ColumnDataType.String);
                    tableDefinition.put("guard", ColumnDataType.String);

                    localStore.defineTable("MoveItem", tableDefinition);

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
                    Query query = QueryOperations.field("characterId").eq(mCharacterId);
                    mMovesTable.pull(query).get();
                } catch (final Exception e) {
                    createAndShowDialogFromTask(e, "Error");
                }
                return null;
            }
        };
        return runAsyncTask(task);
    }

}
