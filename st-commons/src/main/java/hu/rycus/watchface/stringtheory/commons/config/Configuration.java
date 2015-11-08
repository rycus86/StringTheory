package hu.rycus.watchface.stringtheory.commons.config;

import android.util.Log;

import com.google.android.gms.wearable.DataMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import hu.rycus.watchface.stringtheory.commons.R;

import static hu.rycus.watchface.stringtheory.commons.config.ConfigurationItemBuilder.binary;
import static hu.rycus.watchface.stringtheory.commons.config.ConfigurationItemBuilder.choice;
import static hu.rycus.watchface.stringtheory.commons.config.ConfigurationItemBuilder.group;
import static hu.rycus.watchface.stringtheory.commons.config.ConfigurationItemBuilder.palette;

public enum Configuration implements ConfigurationItem {

    SHOW_24_HOURS(binary()
            .key("24hour")
            .defaultValue(true)
            .stringResource(R.string.config_24_hours)),

    CONSTANT_ANIMATION(binary()
            .key("constantAnimation")
            .defaultValue(false)
            .stringResource(R.string.config_constant_animation)),

    NUMBER_OF_STRINGS(group()
            .key("stringCount")
            .defaultValue("count:10")
            .stringResource(R.string.config_number_of_string)),

    NUMBER_OF_STRINGS_07(choice(NUMBER_OF_STRINGS)
            .key("count:7")
            .defaultValue(7)
            .stringResource(R.string.config_number_of_string_07)),

    NUMBER_OF_STRINGS_10(choice(NUMBER_OF_STRINGS)
            .key("count:10")
            .defaultValue(10)
            .stringResource(R.string.config_number_of_string_10)),

    NUMBER_OF_STRINGS_12(choice(NUMBER_OF_STRINGS)
            .key("count:12")
            .defaultValue(12)
            .stringResource(R.string.config_number_of_string_12)),

    NUMBER_OF_STRINGS_15(choice(NUMBER_OF_STRINGS)
            .key("count:15")
            .defaultValue(15)
            .stringResource(R.string.config_number_of_string_15)),

    PALETTE(palette()
            .key("palette")
            .defaultValue(Palette.ORIGINAL)
            .stringResource(R.string.config_palette));

    public static final String PATH = "/string-theory";

    private static final Configuration[] ROOT_ITEMS;
    static {
        final ArrayList<Configuration> items = new ArrayList<>(values().length);
        for (Configuration item : values()) {
            if (item.getType().equals(Type.Palette)) {
                // TODO Palette is not quite ready yet
                continue;
            }

            if (!item.getType().equals(Type.Choice)) {
                items.add(item);
            }
        }

        ROOT_ITEMS = items.toArray(new Configuration[items.size()]);
    }

    private static final int LENGTH = ROOT_ITEMS.length;

    public static int count() {
        return LENGTH;
    }

    public static Configuration at(final int index) {
        return ROOT_ITEMS[index];
    }

    private final Type type;
    private final String key;
    private final Object defaultValue;
    private final int stringResource;
    private final List<Configuration> dependencies;

    Configuration(final ConfigurationItemBuilder<?> builder) {
        assert builder.type != null;
        assert builder.key != null;
        assert builder.defaultValue != null;

        this.type = builder.type;
        this.key = builder.key;
        this.defaultValue = builder.defaultValue;
        this.stringResource = builder.stringResource;
        this.dependencies = Arrays.asList(builder.dependencies);
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

    public Configuration getGroupSelection(final DataMap configuration) {
        final String selected = getStringValue(configuration);
        for (final Configuration child : Configuration.values()) {
            if (child.getKey().equals(selected) && child.dependencies.contains(this)) {
                return child;
            }
        }

        return null;
    }

    public List<Configuration> getGroupValues() {
        final List<Configuration> items = new LinkedList<>();
        for (final Configuration child : Configuration.values()) {
            if (child.dependencies.contains(this)) {
                items.add(child);
            }
        }
        return items;
    }

    private String getStringValue(final DataMap configuration) {
        if (configuration != null) {
            return configuration.getString(key, (String) defaultValue);
        } else {
            return (String) defaultValue;
        }
    }

    public Integer getInteger(final DataMap configuration) {
        if (configuration != null) {
            return configuration.getInt(key, (Integer) defaultValue);
        } else {
            return (Integer) defaultValue;
        }
    }

    public Palette getPalette(final DataMap configuration) {
        final Palette defaultValue = (Palette) this.defaultValue;
        if (configuration != null) {
            final String selected = configuration.getString(key, defaultValue.name());
            try {
                return Palette.valueOf(selected);
            } catch (IllegalArgumentException ex) {
                Log.w("Configuration", "Illegal palette name: " + selected);
            }
        }

        return defaultValue;
    }

    public boolean isAvailable(final DataMap configuration) {
        for (final Configuration parent : dependencies) {
            if (!parent.getBoolean(configuration)) {
                return false;
            }
        }

        return true;
    }

}
