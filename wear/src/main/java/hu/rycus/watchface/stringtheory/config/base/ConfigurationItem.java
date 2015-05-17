package hu.rycus.watchface.stringtheory.config.base;

public interface ConfigurationItem {

    String getKey();

    Type getType();

    int getDisplayResource();

    Object getDefaultValue();

    enum Type {
        Binary
    }

}
