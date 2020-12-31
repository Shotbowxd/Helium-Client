package rip.helium.utils.property.abs;

/**
 * @author antja03
 */
public abstract class Property<T> {

    public T value;
    public T defaultValue;
    private final String id;
    private final String description;
    private final rip.helium.utils.Dependency dependency;

    protected Property(String id, String description, rip.helium.utils.Dependency dependency) {
        this.id = id;
        this.description = description;
        this.dependency = dependency;
    }

    public void setDefault() {
        value = defaultValue;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return this.description;
    }

    public T getValue() {
        return value;
    }

    public abstract void setValue(String input);

    public void setValue(T input) {
        value = input;
    }

    public String getValueAsString() {
        return String.valueOf(value);
    }

    public boolean checkDependency() {
        if (dependency == null) {
            return true;
        } else {
            return dependency.check();
        }
    }

    public interface Dependency {
        boolean check();
    }
}
