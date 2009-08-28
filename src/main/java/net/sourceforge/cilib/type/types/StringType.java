/**
 * Copyright (C) 2003 - 2009
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
 */
package net.sourceforge.cilib.type.types;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 *
 * @author Gary Pampara
 *
 */
public class StringType implements Type {
    private static final long serialVersionUID = 2946972552546398657L;
    private String string;

    /**
     * Create an {@linkplain StringType} instance, which is empty and uninitialised.
     */
    public StringType() {
        string = null;
    }

    /**
     * Create an instance with the given string as the contents.
     * @param string The string value to have.
     */
    public StringType(String string) {
        this.string = string;
    }

    /**
     * Copy constructor. Copy the given instance.
     * @param copy The instance to copy.
     */
    public StringType(StringType copy) {
        this.string = copy.string;
    }

    /**
     * {@inheritDoc}
     */
    public StringType getClone() {
        return new StringType(this);
    }

    /**
     * Get the contained string.
     * @return the contained {@linkplain String}.
     */
    public String getString() {
        return this.string;
    }

    /**
     * Set the contained {@linkplain String} value.
     * @param newString The value to be contained by the {@linkplain StringType}.
     */
    public void setString(String newString) {
        this.string = newString;
    }

    /**
     * {@inheritDoc}
     */
    public boolean equals(Object other) {
        if (this == other)
            return true;

        if ((other == null) || (this.getClass() != other.getClass()))
            return false;

        StringType stringType = (StringType) other;
        return this.string.equals(stringType.string);
    }

    /**
     * {@inheritDoc}
     */
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + this.string.hashCode();
        return hash;
    }

    /**
     * {@inheritDoc}
     */
    public String toString() {
        return string;
    }

    /**
     * Write the contents of the {@linkplain StringType} to an {@linkplain ObjectOutput}.
     * @param oos the {@linkplain ObjectOutput} to write to.
     * @throws IOException If an exception occurs.
     */
    public void writeExternal(ObjectOutput oos) throws IOException {
        oos.writeUTF(this.string);
    }

    /**
     * Read the contents from a given {@linkplain ObjectInput}.
     * @param ois The {@linkplain ObjectInput} to use.
     * @throws IOException If an IO problem occurs.
     * @throws ClassNotFoundException If the desired class is not found.
     */
    public void readExternal(ObjectInput ois) throws IOException, ClassNotFoundException {
        this.string = ois.readUTF();
    }

}
