/*
 * Measurement.java
 * 
 * Created on Jul 5, 2004
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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Random;

/**
 * @author espeer
 */
public class Measurement extends DomainComponent {

    public static final String PREFIX = "M";

    public Measurement(net.sourceforge.cilib.Measurement.Measurement measurement) {
    	this.measurement = measurement;
    	domain = ComponentFactory.instance().newComponent(measurement.getDomain());
    }
    
    public Measurement(String representation) {
        if (! representation.startsWith(PREFIX)) {
            throw new IllegalArgumentException();
        }
        
        int openParenIndex = representation.indexOf('(');
        int closeParenIndex = representation.indexOf(')');
        
        if (openParenIndex > 0 && closeParenIndex > openParenIndex + 1) {
        	try {
        		measurement = (net.sourceforge.cilib.Measurement.Measurement) Class.forName(representation.substring(openParenIndex + 1, closeParenIndex).trim()).newInstance();
        		domain = ComponentFactory.instance().newComponent(measurement.getDomain());
        	}
        	catch (Exception ex) {
        		throw new IllegalArgumentException("Unknown measurement");
        	}
        }
        else {
        	throw new IllegalArgumentException("Format error");
        }
    }
    
    public String getRepresentation() {
        StringBuffer representation = new StringBuffer(PREFIX);
        representation.append("(");
        representation.append(measurement.getClass().getName());
        representation.append(")");
        
        return representation.toString();
    }

    public boolean isInside(Object item, int offset) {
    	return domain.isInside(item, offset);
    }
    
    protected void serialise(ObjectOutputStream oos, Object item, int offset) throws IOException {
    	domain.serialise(oos, item, offset);
    }
    
    public Object deserialise(ObjectInputStream ois) throws IOException {
    	return domain.deserialise(ois);
    }
    
    protected Object deserialiseGroup(ObjectInputStream ois) throws IOException {
    	return domain.deserialiseGroup(ois);
    }

    protected Object getRandomGroup(Random randomiser)  {
    	throw new UnsupportedOperationException();
    }
    
	private net.sourceforge.cilib.Measurement.Measurement measurement;
	private DomainComponent domain;

	/**
	 * Throws an exception.  Should never actually get called.
	 *
	 * @author jkroon
	 */
	public void acceptDomainVisitor(DomainVisitor dv, Object []instances) {
		domain.acceptDomainVisitor(dv, instances);
	}
}
