package hu.rycus.watchface.stringtheory.commons.config;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.WearableListView;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Wearable;

import hu.rycus.watchface.commons.config.ConfigurationHelper;
import hu.rycus.watchface.commons.config.ConfigurationHelper.OnConfigurationDataReadCallback;
import hu.rycus.watchface.stringtheory.commons.R;

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

    public Builder startForResult(
            final Class<? extends BaseWearableSubConfigurationActivity> targetActivityClass) {

        return new Builder(this, targetActivityClass);
    }

    public static class Builder {

        private final Activity activity;
        private final Class<? extends Activity> activityClass;
        private Configuration parent;
        private int selectedIndex;
        private int requestCode;

        public Builder(final Activity activity, final Class<? extends Activity> activityClass) {
            this.activity = activity;
            this.activityClass = activityClass;
        }

        public Builder parent(final Configuration parent) {
            this.parent = parent;
            return this;
        }

        public Builder selected(final int index) {
            this.selectedIndex = index;
            return this;
        }

        public Builder requestCode(final int code) {
            this.requestCode = code;
            return this;
        }

        public void start() {
            final Intent intent = new Intent(activity, activityClass);
            intent.putExtra(BaseWearableSubConfigurationActivity.EXTRA_PARENT, parent);
            intent.putExtra(BaseWearableSubConfigurationActivity.EXTRA_SELECTED_INDEX, selectedIndex);
            activity.startActivityForResult(intent, requestCode);
        }

    }

}
