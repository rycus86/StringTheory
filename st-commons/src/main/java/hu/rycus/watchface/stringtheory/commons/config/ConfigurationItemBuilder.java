package hu.rycus.watchface.stringtheory.commons.config;

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

    public static ConfigurationItemBuilder<String> group() {
        return new ConfigurationItemBuilder<>(ConfigurationItem.Type.Group);
    }

    public static <T> ConfigurationItemBuilder<T> choice(final Configuration group) {
        return new ConfigurationItemBuilder<T>(ConfigurationItem.Type.Choice)
                .dependencies(group);
    }

    public static ConfigurationItemBuilder<Enum<? extends PaletteItem>> palette() {
        return new ConfigurationItemBuilder<>(ConfigurationItem.Type.Palette);
    }

}
