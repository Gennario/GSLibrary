package eu.gs.gslibrary.nms;

/**
 * This class provides an implementation of {@link NMSAdapter}.
 *
 * @author d0by
 */
public class NMSProvider {

    private final NMSAdapter adapter;

    /**
     * Create a new instance of {@link NMSProvider}.
     * <p>
     * This constructor also initializes the adapter instance
     * for your current server version.
     */
    public NMSProvider() {
        try {
            Class<?> clazz = Class.forName("eu.gs.gslibrary.nms." + Version.CURRENT.name() + ".NMSAdapterImpl");
            this.adapter = (NMSAdapter) clazz.newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("You server version " + Version.CURRENT.name() + " is not supported.");
        }
    }

    /**
     * Get the NMSAdapter for your current server version.
     *
     * @return The NMSAdapter for your current server version or null the version is not supported.
     * @see NMSAdapter
     */
    public NMSAdapter getAdapter() {
        return adapter;
    }

}
