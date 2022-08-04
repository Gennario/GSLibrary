package eu.gs.gslibrary.utils.cooldowns;

public interface CooldownDoneResponse {

    void start();
    void tick();
    void stop();

}
