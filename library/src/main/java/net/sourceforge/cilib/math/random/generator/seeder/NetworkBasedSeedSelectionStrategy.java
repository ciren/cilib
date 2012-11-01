/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.math.random.generator.seeder;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.SecureRandom;
import java.util.Enumeration;
import java.util.Random;

/**
 * Determine the initial seed value by manipulating the current network address.
 */
public class NetworkBasedSeedSelectionStrategy implements SeedSelectionStrategy {
    private Random random;
    private int address;

    public NetworkBasedSeedSelectionStrategy() {
        random = new SecureRandom();
        address = getNetworkAddress();
    }

    @Override
    public long getSeed() {
        long seed = random.nextLong();
        seed ^= System.currentTimeMillis();
        seed ^= address;
        seed ^= ((long) System.identityHashCode(new Object())) << 32;

        return seed;
    }

    private int getNetworkAddress() {
        byte[] address = null;

        try {
            address = InetAddress.getLocalHost().getAddress();
        }
        catch (UnknownHostException ex) {
//            log.warn("localhost not found directly. Proceeding.");
        }

        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                Enumeration<InetAddress> addresses = interfaces.nextElement().getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();
                    if (!addr.isLoopbackAddress()) {
                        address = addr.getAddress();
                    }
                }
            }
        }
        catch (SocketException ex) {
//            log.warn("localhost not found through interfce list. Proceeding.");
        }

        if (address == null) {
            return 0;
        }
        else {
            return ((int) address[0]) << 24 | ((int) address[1]) << 16 | ((int) address[2]) << 8 | (int) address[3];
        }
    }
}
