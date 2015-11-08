package hu.rycus.watchface.stringtheory.commons.config;

public interface ConfigurationItem {

    String getKey();

    Type getType();

    int getDisplayResource();

    Object getDefaultValue();

    enum Type {
        Binary, Group, Choice, Palette
    }

}
