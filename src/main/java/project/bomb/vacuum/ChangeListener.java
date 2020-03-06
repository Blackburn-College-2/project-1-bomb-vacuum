package project.bomb.vacuum;

/**
 * An object to be notified when the listened value changes.
 *
 * @param <T> the type that is being listened to.
 */
public interface ChangeListener<T> {

    /**
     * SHOULD be called whenever the listened to value changes.
     *
     * @param oldValue the value before the change.
     * @param newValue the value after the change.
     */
    void onChange(T oldValue, T newValue);
}
