/*
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
package net.sourceforge.cilib.io.exception;

/**
 * Class represent an Exception that occurs during data I/O in CIlib. As the data
 * source is unknown, exceptions might range from simple IOExceptions to SAXExceptions
 * or SQL Exceptions, therefore the super class is simply Exception.
 * @author andrich
 */
public class CIlibIOException extends Exception {

    public CIlibIOException(Throwable cause) {
        super(cause);
    }

    public CIlibIOException(String message, Throwable cause) {
        super(message, cause);
    }

    public CIlibIOException(String message) {
        super(message);
    }

    public CIlibIOException() {
    }
}
