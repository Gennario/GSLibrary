package eu.gs.gslibrary.utils.methods;

import java.util.concurrent.CompletableFuture;

/**
 * "It's a class that runs a method asynchronously and prints any exceptions that occur."
 *
 * The class is abstract because it doesn't make sense to instantiate it. It's only useful as a base class
 */
public abstract class AsyncMethod {

    public abstract void action();

    public AsyncMethod() {
        CompletableFuture.runAsync(this::action).whenComplete(((unused, throwable) -> {
            if (throwable != null) {
                throwable.printStackTrace();
            }
        }));
    }

}
