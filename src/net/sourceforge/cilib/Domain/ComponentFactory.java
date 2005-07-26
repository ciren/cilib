/*
 * ComponentFactory.java
 * 
 * Created on Apr 14, 2004
 *
 * Copyright (C)  2004 - CIRG@UP 
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science 
 * University of Pretoria
 * South Africa
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA 
 *
 */
package net.sourceforge.cilib.Domain;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;


/**
 * This class can be used to create a composite component structure given a string representation for
 * the domain.
 * <p />
 * This class is a singleton.
 * 
 * @author espeer
 */
public class ComponentFactory {
    
    private ComponentFactory() {
        prefixes = new HashMap<String, Constructor>();

        Class[] stringParameter = { String.class };
        
        try {
            prefixes.put(Int.PREFIX, Int.class.getConstructor(stringParameter));
            prefixes.put(Real.PREFIX, Real.class.getConstructor(stringParameter));
            prefixes.put(Bit.PREFIX, Bit.class.getConstructor(stringParameter));
            prefixes.put(Text.PREFIX, Text.class.getConstructor(stringParameter));
            prefixes.put(Set.PREFIX, Set.class.getConstructor(stringParameter));
            prefixes.put(Measurement.PREFIX, Measurement.class.getConstructor(stringParameter));
            prefixes.put(Unknown.PREFIX, Unknown.class.getConstructor(stringParameter));
        }
        catch (NoSuchMethodException ex) { }   
    }
    
    /**
     * Create a new domain component structure for the given string representation.
     * 
     * @param representation the string representation for the domain.
     * @return the composite component structure representing the given domain. 
     */
    public DomainComponent newComponent(String representation) {
        representation = representation.trim();
        char lastChar = representation.charAt(representation.length() - 1);
        if (Character.isDigit(lastChar) || lastChar == 'N') {
            // Special case for compound components which are recognised by their suffix
            return new Compound(representation);
        }
        else if (representation.charAt(0) == '[') {
            // Special case for composite components which are recognised by the opening square bracket
            return new Composite(representation);
        }
        else {
            // Look for a suitable prefix
            StringBuffer prefix = new StringBuffer();
            for (int i = 0; i < representation.length(); ++i) {
                char tmp = representation.charAt(i);
                if (! (Character.isLetterOrDigit(tmp) || tmp == '?')) {
                	break;
                }
                prefix.append(tmp);
            }
            Constructor constructor = (Constructor) prefixes.get(prefix.toString());
            if (constructor == null) {
                throw new IllegalArgumentException();
            }
            try {
                return (DomainComponent) constructor.newInstance(new Object[] { representation });
            }
            catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }
    
    /**
     * Obtain a reference to the <code>ComponentFactory</code> instance.
     * 
     * @return the <code>ComponentFactory</code> instance.
     */
    public static ComponentFactory instance() {
        return instance;
    }

    private static ComponentFactory instance = new ComponentFactory();

    private Map<String, Constructor> prefixes;
}
