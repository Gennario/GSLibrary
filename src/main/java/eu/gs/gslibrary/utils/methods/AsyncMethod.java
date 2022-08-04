package eu.gs.gslibrary.utils.methods;

import java.util.concurrent.CompletableFuture;

public abstract class AsyncMethod {

    public abstract void action();

    public AsyncMethod() {
        CompletableFuture.runAsync(this::action);
    }

}
