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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.http.OkHttpClientFactory;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.table.query.QueryOrder;
import com.microsoft.windowsazure.mobileservices.table.sync.MobileServiceSyncContext;
import com.microsoft.windowsazure.mobileservices.table.sync.localstore.ColumnDataType;
import com.microsoft.windowsazure.mobileservices.table.sync.localstore.MobileServiceLocalStoreException;
import com.microsoft.windowsazure.mobileservices.table.sync.localstore.SQLiteLocalStore;
import com.microsoft.windowsazure.mobileservices.table.sync.synchandler.SimpleSyncHandler;
import com.squareup.okhttp.OkHttpClient;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class MainActivity extends Activity {

    private MobileServiceClient mClient;
    private MobileServiceTable<AccountItem> mAccountsTable;
    private List<AccountItem> mAccounts;

    private Button loginButton, createAccountButton, offlineButton;
    private EditText ed1,ed2;
    private ProgressBar mProgressBar;

    int counter = 3;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginButton = (Button) findViewById(R.id.login_button);
        createAccountButton = (Button) findViewById(R.id.create_account_button);
        offlineButton = (Button) findViewById(R.id.offline_button);
        ed1 = (EditText) findViewById(R.id.name_textbox);
        ed2 = (EditText) findViewById(R.id.password_textbox);
        mProgressBar = (ProgressBar) findViewById(R.id.loadingAccountsProgressBar);
        mProgressBar.setVisibility(ProgressBar.GONE);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkLogin(ed1.getText().toString(), ed2.getText().toString())) {
                    counter--;
                    if (counter == 0) {
                        createAndShowDialog("Wrong credentials for too much time, exiting", "Error");
                        finishAndRemoveTask();
                    }
                    createAndShowDialog("Wrong credentials", "Error");
                }
                else {
                    openGameActivity();
                }
            }
        });
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ed1.getText().toString().isEmpty() || ed2.getText().toString().isEmpty()) {
                    createAndShowDialog("Empty credentials", "Error");
                    return;
                }
                refreshItemsFromTable();

                for (int i=0; i<mAccounts.size(); i++) {
                    if (ed1.getText().toString().equals(mAccounts.get(i).getName())) {
                        createAndShowDialog("Account already exists", "Error");
                        return;
                    }
                }
                AccountItem newAccount = new AccountItem();
                newAccount.setName(ed1.getText().toString());
                newAccount.setPassword(ed2.getText().toString());
                try {
                    Log.d("Création de compte", "NOM : " + newAccount.getName());
                    Log.d("Création de compte", "PWD : " + newAccount.getPassword());
                    addItem(v, newAccount);
                }
                catch (Exception e) {
                    createAndShowDialog(e, "Error");
                    return;
                }
                createAndShowDialog("Account successfully created", "Success");
            }
        });

        offlineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGameActivity();
            }
        });

        try {
            mClient = new MobileServiceClient(
                    "https://frameperfect.azurewebsites.net",
                    this).withFilter(new ProgressFilter(mProgressBar));

            mClient.setAndroidHttpClientFactory(new OkHttpClientFactory() {
                @Override
                public OkHttpClient createOkHttpClient() {
                    OkHttpClient client = new OkHttpClient();
                    client.setReadTimeout(20, TimeUnit.SECONDS);
                    client.setWriteTimeout(20, TimeUnit.SECONDS);
                    return client;
                }
            });

            mAccountsTable = mClient.getTable("Accounts", AccountItem.class);

            refreshItemsFromTable();

        } catch (MalformedURLException e) {
            createAndShowDialog(new Exception("There was an error creating the Mobile Service. Verify the URL"), "Error");
        } catch (Exception e){
            createAndShowDialog(e, "Error");
        }
    }

    private void openGameActivity() {
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }

    private boolean checkLogin(final String name, final String password) {
        Log.d("checkLogin", "Coucou");
        refreshItemsFromTable();
        if (mAccounts == null) {
            Log.d("checkLogin", "Pardon?");
            return (false);
        }
        for (int i=0; i<mAccounts.size(); i++) {
            Log.d("checkLogin", "i : " + i);
            Log.d("checkLogin", "MY NOM : " + name);
            Log.d("checkLogin", "MY PWD : " + password);
            Log.d("checkLogin", "NOM : " + mAccounts.get(i).getName());
            Log.d("checkLogin", "PWD : " + mAccounts.get(i).getPassword());
            if (name.equals(mAccounts.get(i).getName()) && password.equals(mAccounts.get(i).getPassword())) {
                return (true);
            }
        }
        return (false);
    }

    private void refreshItemsFromTable() {
        Log.d("refreshItemsFromTable", "REFRESHING...");

        // Get the items that weren't marked as completed and add them in the
        // adapter

        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params) {

                try {
                    mAccounts = refreshItemsFromMobileServiceTable();
                    Log.d("refreshItemsFromTable", ("SIZE OF RESULT : " + mAccounts.size()));

                    //Offline Sync
                    //final List<ToDoItem> results = refreshItemsFromMobileServiceTableSyncTable();
                } catch (final Exception e){
                    createAndShowDialogFromTask(e, "Error");
                }

                return null;
            }
        };

        runAsyncTask(task);
    }

    private List<AccountItem> refreshItemsFromMobileServiceTable() throws ExecutionException, InterruptedException {
        return mAccountsTable
                .where()
                .execute().get();
    }

    public void addItem(View view, final AccountItem item) {
        if (mClient == null) {
            return;
        }

        // Insert the new item
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    final AccountItem entity = addItemInTable(item);

                } catch (final Exception e) {
                    createAndShowDialogFromTask(e, "Error");
                }
                return null;
            }
        };

        runAsyncTask(task);
    }

    public AccountItem addItemInTable(AccountItem item) throws ExecutionException, InterruptedException {
        AccountItem entity = mAccountsTable.insert(item).get();
        return entity;
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

                    SQLiteLocalStore localStore = new SQLiteLocalStore(mClient.getContext(), "OfflineAccounts", null, 1);

                    Map<String, ColumnDataType> tableDefinition = new HashMap<String, ColumnDataType>();
                    tableDefinition.put("id", ColumnDataType.String);
                    tableDefinition.put("name", ColumnDataType.String);
                    tableDefinition.put("password", ColumnDataType.String);

                    localStore.defineTable("Accounts", tableDefinition);

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

}
