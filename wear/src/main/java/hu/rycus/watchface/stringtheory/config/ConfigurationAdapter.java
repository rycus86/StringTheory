package hu.rycus.watchface.stringtheory.config;

import android.content.Context;
import android.support.wearable.view.WearableListView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataMap;

import hu.rycus.watchface.commons.config.ConfigurationHelper;
import hu.rycus.watchface.stringtheory.R;
import hu.rycus.watchface.stringtheory.config.base.ConfigurationItem;

public class ConfigurationAdapter extends WearableListView.Adapter
        implements
            WearableListView.ClickListener,
            ConfigurationHelper.OnConfigurationDataReadCallback {

    private final Context context;
    private final LayoutInflater inflater;
    private final GoogleApiClient apiClient;

    private DataMap configuration;
    private DataMap pendingConfiguration;

    public ConfigurationAdapter(final Context context, final GoogleApiClient apiClient) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.apiClient = apiClient;
    }

    @Override
    public void onConfigurationDataRead(final DataMap configuration) {
        this.configuration = configuration;
        if (pendingConfiguration != null) {
            this.configuration.putAll(pendingConfiguration);
            ConfigurationHelper.storeConfiguration(
                    apiClient, Configuration.PATH, this.configuration);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return Configuration.count();
    }

    @Override
    public WearableListView.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final CheckBox itemView = (CheckBox) inflater.inflate(R.layout.item_config_checkbox, null);
        return new ConfigurationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final WearableListView.ViewHolder holder, final int position) {
        final ConfigurationViewHolder viewHolder = (ConfigurationViewHolder) holder;
        viewHolder.update(position);
    }

    @Override
    public void onClick(final WearableListView.ViewHolder holder) {
        final ConfigurationViewHolder viewHolder = (ConfigurationViewHolder) holder;
        viewHolder.onClick();
    }

    @Override
    public void onTopEmptyRegionClick() {
    }

    private class ConfigurationViewHolder extends WearableListView.ViewHolder {

        private boolean isUpdating = false;

        public ConfigurationViewHolder(final CheckBox itemView) {
            super(itemView);
            itemView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {
                    onClick();
                }
            });
        }

        public CheckBox asCheckBox() {
            return (CheckBox) itemView;
        }

        public void update(final int position) {
            isUpdating = true;
            try {
                final Configuration item = Configuration.at(position);
                if (item.getType().equals(ConfigurationItem.Type.Binary)) {
                    final CheckBox box = asCheckBox();
                    box.setText(item.getDisplayResource());
                    box.setChecked(item.getBoolean(configuration));
                }
            } finally {
                isUpdating = false;
            }
        }

        public void onClick() {
            if (isUpdating) {
                return;
            }

            final int position = getPosition();
            final Configuration item = Configuration.at(position);

            if (item.getType().equals(ConfigurationItem.Type.Binary)) {
                final boolean value = asCheckBox().isChecked();

                if (apiClient.isConnected() && configuration != null) {
                    configuration.putBoolean(item.getKey(), value);
                    ConfigurationHelper.storeConfiguration(
                            apiClient, Configuration.PATH, configuration);
                } else {
                    if (pendingConfiguration == null) {
                        pendingConfiguration = new DataMap();
                    }

                    pendingConfiguration.putBoolean(item.getKey(), value);
                }
            }
        }

    }

}
