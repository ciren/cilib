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
        byte[] addr = null;

        try {
            addr = InetAddress.getLocalHost().getAddress();
        } catch (UnknownHostException ex) {
            System.out.println("Warning: localhost not found directly. Proceeding.");
        }

        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                Enumeration<InetAddress> addresses = interfaces.nextElement().getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress a = addresses.nextElement();
                    if (!a.isLoopbackAddress()) {
                        addr = a.getAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            System.out.println("Warning: localhost not found through interface list. Proceeding.");
        }

        if (addr == null) {
            return 0;
        } else {
            return ((int) addr[0]) << 24 | ((int) addr[1]) << 16 | ((int) addr[2]) << 8 | (int) addr[3];
        }
    }
}
