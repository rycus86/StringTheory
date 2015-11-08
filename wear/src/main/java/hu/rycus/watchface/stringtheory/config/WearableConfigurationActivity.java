package hu.rycus.watchface.stringtheory.config;

import android.content.Intent;
import android.support.wearable.view.WearableListView;

import com.google.android.gms.common.api.GoogleApiClient;

import hu.rycus.watchface.commons.config.ConfigurationHelper.OnConfigurationDataReadCallback;
import hu.rycus.watchface.stringtheory.commons.config.BaseWearableConfigurationActivity;
import hu.rycus.watchface.stringtheory.commons.config.Configuration;
import hu.rycus.watchface.stringtheory.commons.config.GroupSelectionActivity;
import hu.rycus.watchface.stringtheory.commons.config.Palette;

public class WearableConfigurationActivity extends BaseWearableConfigurationActivity {

    private static final int REQUEST_SELECT_GROUP = 0x01;
    private static final int REQUEST_SELECT_PALETTE = 0x02;

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

    public void onStartGroupSelection(final Configuration group, final Configuration selection) {
        final int current = group.getGroupValues().indexOf(selection);

        startForResult(GroupSelectionActivity.class)
                .parent(group)
                .requestCode(REQUEST_SELECT_GROUP)
                .selected(current)
                .start();
    }

    public void onStartPaletteSelection(final Configuration group, final Palette selection) {
        final int current = selection.ordinal();

        startForResult(PaletteSelectionActivity.class)
                .parent(group)
                .requestCode(REQUEST_SELECT_PALETTE)
                .selected(current)
                .start();
    }

    @Override
    protected void onActivityResult(final int requestCode,
                                    final int resultCode, final Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_SELECT_GROUP) {
                final Configuration parent = (Configuration)
                        data.getSerializableExtra(GroupSelectionActivity.EXTRA_PARENT);
                final Configuration selected = (Configuration)
                        data.getSerializableExtra(GroupSelectionActivity.EXTRA_RESULT);
                adapter.onGroupSelectionResult(parent, selected);
            } else if (requestCode == REQUEST_SELECT_PALETTE) {
                final Configuration parent = (Configuration)
                        data.getSerializableExtra(GroupSelectionActivity.EXTRA_PARENT);
                final Palette selected = (Palette)
                        data.getSerializableExtra(GroupSelectionActivity.EXTRA_RESULT);
                adapter.onPaletteSelectionResult(parent, selected);
            }
        }
    }

}
