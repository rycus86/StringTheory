package hu.rycus.watchface.stringtheory.config;

import com.google.android.gms.wearable.DataMap;

import hu.rycus.watchface.stringtheory.R;
import hu.rycus.watchface.stringtheory.config.base.ConfigurationItem;

import static hu.rycus.watchface.stringtheory.config.ConfigurationItemBuilder.binary;

public enum Configuration implements ConfigurationItem {

    SHOW_24_HOURS(binary()
            .key("24hour")
            .defaultValue(true)
            .stringResource(R.string.config_24_hours)),

    CONSTANT_ANIMATION(binary()
            .key("constantAnimation")
            .defaultValue(false)
            .stringResource(R.string.config_constant_animation));

    public static final String PATH = "/string-theory";

    private static final int LENGTH = values().length;

    public static int count() {
        return LENGTH;
    }

    public static Configuration at(final int index) {
        return values()[index];
    }

    private final Type type;
    private final String key;
    private final Object defaultValue;
    private final int stringResource;

    Configuration(final ConfigurationItemBuilder<?> builder) {
        assert builder.type != null;
        assert builder.key != null;
        assert builder.defaultValue != null;

        this.type = builder.type;
        this.key = builder.key;
        this.defaultValue = builder.defaultValue;
        this.stringResource = builder.stringResource;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public int getDisplayResource() {
        return stringResource;
    }

    @Override
    public Object getDefaultValue() {
        return defaultValue;
    }

    public boolean getBoolean(final DataMap configuration) {
        if (configuration != null) {
            return configuration.getBoolean(key, (Boolean) defaultValue);
        } else {
            return (Boolean) defaultValue;
        }
    }

}
