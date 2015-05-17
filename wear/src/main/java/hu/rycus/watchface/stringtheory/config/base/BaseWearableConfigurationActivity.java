package hu.rycus.watchface.stringtheory.config.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.WearableListView;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Wearable;

import hu.rycus.watchface.commons.config.ConfigurationHelper;
import hu.rycus.watchface.commons.config.ConfigurationHelper.OnConfigurationDataReadCallback;
import hu.rycus.watchface.stringtheory.R;

public abstract class BaseWearableConfigurationActivity extends Activity
        implements
            GoogleApiClient.ConnectionCallbacks,
            GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "ConfigActivity";

    private GoogleApiClient apiClient;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_wear_default);

        apiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Wearable.API)
                .build();

        onBeforeListViewSetup(apiClient);

        final WearableListView listView = (WearableListView) findViewById(R.id.list_config);
        listView.setAdapter(getListAdapter());
        listView.setClickListener(getListClickListener());
    }

    protected void onBeforeListViewSetup(final GoogleApiClient apiClient) {
    }

    protected abstract String getConfigurationPath();

    protected abstract OnConfigurationDataReadCallback getConfigurationCallbackListener();

    protected abstract WearableListView.Adapter getListAdapter();

    protected abstract WearableListView.ClickListener getListClickListener();

    @Override
    protected void onStart() {
        super.onStart();
        apiClient.connect();
    }

    @Override
    protected void onStop() {
        if (apiClient != null && apiClient.isConnected()) {
            apiClient.disconnect();
        }
        super.onStop();
    }

    @Override
    public void onConnected(final Bundle bundle) {
        Log.d(TAG, "GoolgeApiClient connected");
        ConfigurationHelper.loadLocalConfiguration(
                apiClient, getConfigurationPath(), getConfigurationCallbackListener());
    }

    @Override
    public void onConnectionSuspended(final int i) {
        Log.w(TAG, "GoolgeApiClient connection suspended");
    }

    @Override
    public void onConnectionFailed(final ConnectionResult connectionResult) {
        Log.d(TAG, "GoolgeApiClient connection failed: ");
    }

}
