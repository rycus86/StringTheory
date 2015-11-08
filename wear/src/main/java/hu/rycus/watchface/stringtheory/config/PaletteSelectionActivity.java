package hu.rycus.watchface.stringtheory.config;

import android.view.View;

import java.util.Arrays;
import java.util.List;

import hu.rycus.watchface.stringtheory.R;
import hu.rycus.watchface.stringtheory.commons.config.BaseWearableSubConfigurationActivity;
import hu.rycus.watchface.stringtheory.commons.config.Configuration;
import hu.rycus.watchface.stringtheory.commons.config.Palette;
import hu.rycus.watchface.stringtheory.commons.ui.PaletteView;

public class PaletteSelectionActivity extends BaseWearableSubConfigurationActivity<Palette> {

    @Override
    protected List<Palette> getItems(final Configuration parent) {
        return Arrays.asList(Palette.values());
    }

    @Override
    protected int getItemViewResource() {
        return R.layout.item_config_palette_wear;
    }

    @Override
    protected void onUpdateItemView(final View itemView, final Palette item) {
        ((PaletteView) itemView).setPalette(item);
    }

}
