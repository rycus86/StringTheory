package hu.rycus.watchface.stringtheory.config;

import android.support.wearable.view.WearableListView;

import com.google.android.gms.common.api.GoogleApiClient;

import hu.rycus.watchface.commons.config.ConfigurationHelper.OnConfigurationDataReadCallback;
import hu.rycus.watchface.stringtheory.config.base.BaseWearableConfigurationActivity;

public class WearableConfigurationActivity extends BaseWearableConfigurationActivity {

    private ConfigurationAdapter adapter;

    @Override
    protected void onBeforeListViewSetup(final GoogleApiClient apiClient) {
        super.onBeforeListViewSetup(apiClient);
        adapter = new ConfigurationAdapter(this, apiClient);
    }

    @Override
    protected String getConfigurationPath() {
        return Configuration.PATH;
    }

    @Override
    protected OnConfigurationDataReadCallback getConfigurationCallbackListener() {
        return adapter;
    }

    @Override
    protected WearableListView.Adapter getListAdapter() {
        return adapter;
    }

    @Override
    protected WearableListView.ClickListener getListClickListener() {
        return adapter;
    }
}
