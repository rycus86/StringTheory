package hu.rycus.watchface.stringtheory.commons.config;

import android.view.View;
import android.widget.TextView;

import java.util.List;

import hu.rycus.watchface.stringtheory.commons.R;

public class GroupSelectionActivity extends BaseWearableSubConfigurationActivity<Configuration> {

    @Override
    protected List<Configuration> getItems(final Configuration parent) {
        return parent.getGroupValues();
    }

    @Override
    protected int getItemViewResource() {
        return R.layout.item_config_selection;
    }

    @Override
    protected void onUpdateItemView(final View itemView, final Configuration item) {
        ((TextView) itemView).setText(item.getDisplayResource());
    }

}
