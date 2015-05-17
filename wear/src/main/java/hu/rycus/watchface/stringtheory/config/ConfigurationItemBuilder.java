package hu.rycus.watchface.stringtheory.config;

import hu.rycus.watchface.stringtheory.config.base.ConfigurationItem;

public class ConfigurationItemBuilder<T> {

    final ConfigurationItem.Type type;
    String key;
    T defaultValue;
    int stringResource;
    Configuration[] dependencies = new Configuration[0];

    private ConfigurationItemBuilder(final ConfigurationItem.Type type) {
        this.type = type;
    }

    public ConfigurationItemBuilder<T> key(final String key) {
        this.key = key;
        return this;
    }

    public ConfigurationItemBuilder<T> defaultValue(final T defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    public ConfigurationItemBuilder<T> stringResource(final int stringResource) {
        this.stringResource = stringResource;
        return this;
    }

    public ConfigurationItemBuilder<T> dependencies(final Configuration... dependencies) {
        this.dependencies = dependencies;
        return this;
    }

    public static ConfigurationItemBuilder<Boolean> binary() {
        return new ConfigurationItemBuilder<>(ConfigurationItem.Type.Binary);
    }

//    public static ConfigurationBuilder<String> group() {
//        return new ConfigurationBuilder<>(Type.Group);
//    }

//    public static ConfigurationBuilder<Boolean> choice(final Configuration group) {
//        return new ConfigurationBuilder<Boolean>(Type.Choice).dependencies(group);
//    }

//    public static ConfigurationBuilder<Palette> palette() {
//        return new ConfigurationBuilder<>(Type.Palette);
//    }

}
