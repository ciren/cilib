package net.cilib.main;

import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * @since 0.8
 * @author gpampara
 */
public final class Main {

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new CIlibCoreModule());

        Main main = injector.getInstance(Main.class);
        // Something here?
    }
}
