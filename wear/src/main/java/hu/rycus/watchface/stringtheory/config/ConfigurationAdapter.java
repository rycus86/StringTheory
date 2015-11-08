package hu.rycus.watchface.stringtheory.config;

import android.support.wearable.view.WearableListView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataMap;

import hu.rycus.watchface.commons.config.ConfigurationHelper;
import hu.rycus.watchface.stringtheory.R;
import hu.rycus.watchface.stringtheory.commons.config.Configuration;
import hu.rycus.watchface.stringtheory.commons.config.ConfigurationItem;
import hu.rycus.watchface.stringtheory.commons.config.Palette;
import hu.rycus.watchface.stringtheory.commons.ui.PaletteView;

public class ConfigurationAdapter extends WearableListView.Adapter
        implements
            WearableListView.ClickListener,
            ConfigurationHelper.OnConfigurationDataReadCallback {

    private final WearableConfigurationActivity activity;
    private final LayoutInflater inflater;
    private final GoogleApiClient apiClient;

    private DataMap configuration;

    private boolean hasPendingConfiguration = false;
    private final DataMap pendingConfiguration = new DataMap();

    public ConfigurationAdapter(final WearableConfigurationActivity activity,
                                final GoogleApiClient apiClient) {
        this.activity = activity;
        this.inflater = LayoutInflater.from(activity);
        this.apiClient = apiClient;
    }

    @Override
    public void onConfigurationDataRead(final DataMap configuration) {
        this.configuration = configuration;
        if (hasPendingConfiguration) {
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
        final View itemView = inflater.inflate(R.layout.item_config_wear, null);
        return new ConfigurationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final WearableListView.ViewHolder holder, final int position) {
        final ConfigurationViewHolder viewHolder = (ConfigurationViewHolder) holder;
        viewHolder.update(position);
    }

    public void onGroupSelectionResult(final Configuration group, final Configuration selection) {
        if (apiClient.isConnected() && configuration != null) {
            configuration.putString(group.getKey(), selection.getKey());
            ConfigurationHelper.storeConfiguration(
                    apiClient, Configuration.PATH, configuration);
            notifyDataSetChanged();
        } else {
            pendingConfiguration.putString(group.getKey(), selection.getKey());
            hasPendingConfiguration = true;
        }
    }

    public void onPaletteSelectionResult(final Configuration item, final Palette selection) {
        if (apiClient.isConnected() && configuration != null) {
            configuration.putString(item.getKey(), selection.name());
            ConfigurationHelper.storeConfiguration(
                    apiClient, Configuration.PATH, configuration);
            notifyDataSetChanged();
        } else {
            pendingConfiguration.putString(item.getKey(), selection.name());
            hasPendingConfiguration = true;
        }
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

        private final CheckBox checkBox;
        private final TextView txtTitle;
        private final TextView txtDescription;
        private final PaletteView paletteView;
        private final TextView txtSingle;

        public ConfigurationViewHolder(final View itemView) {
            super(itemView);

            checkBox = (CheckBox) itemView.findViewById(R.id.btn_config_binary);
            txtTitle = (TextView) itemView.findViewById(R.id.txt_config_title);
            txtDescription = (TextView) itemView.findViewById(R.id.txt_config_description);
            paletteView = (PaletteView) itemView.findViewById(R.id.v_config_palette);
            txtSingle = (TextView) itemView.findViewById(R.id.txt_config_single_title);

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {
                    onClick();
                }
            });
        }

        public void update(final int position) {
            isUpdating = true;
            try {
                final Configuration item = Configuration.at(position);
                switch (item.getType()) {
                    case Binary:
                        checkBox.setText(item.getDisplayResource());
                        checkBox.setChecked(item.getBoolean(configuration));
                        break;
                    case Group:
                        final Configuration selected = item.getGroupSelection(configuration);
                        txtTitle.setText(item.getDisplayResource());
                        txtDescription.setText(selected.getDisplayResource());
                        break;
                    case Palette:
                        txtSingle.setText(item.getDisplayResource());
                        break;
                }

                checkBox.setVisibility(
                        is(item, ConfigurationItem.Type.Binary) ?
                                View.VISIBLE :
                                View.GONE);
                txtTitle.setVisibility(
                        is(item, ConfigurationItem.Type.Group) ?
                                View.VISIBLE :
                                View.GONE);
                txtDescription.setVisibility(
                        is(item, ConfigurationItem.Type.Group) ?
                                View.VISIBLE :
                                View.GONE);
                paletteView.setVisibility(
                        is(item, ConfigurationItem.Type.Palette) ?
                                View.VISIBLE :
                                View.GONE);
            } finally {
                isUpdating = false;
            }
        }

        private boolean is(final Configuration item, final Configuration.Type type) {
            return item.getType().equals(type);
        }

        public void onClick() {
            if (isUpdating) {
                return;
            }

            final int position = getPosition();
            final Configuration item = Configuration.at(position);

            if (item.getType().equals(ConfigurationItem.Type.Binary)) {
                final boolean value = checkBox.isChecked();

                if (apiClient.isConnected() && configuration != null) {
                    configuration.putBoolean(item.getKey(), value);
                    ConfigurationHelper.storeConfiguration(
                            apiClient, Configuration.PATH, configuration);
                } else {
                    pendingConfiguration.putBoolean(item.getKey(), value);
                    hasPendingConfiguration = true;
                }
            } else if (item.getType().equals(ConfigurationItem.Type.Group)) {
                final Configuration selection = item.getGroupSelection(configuration);
                activity.onStartGroupSelection(item, selection);
            } else if (item.getType().equals(ConfigurationItem.Type.Palette)) {
                final Palette selection = item.getPalette(configuration);
                activity.onStartPaletteSelection(item, selection);
            }
        }

    }

}
