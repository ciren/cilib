/**
 * Computational Intelligence Library (CIlib)
 * Copyright (C) 2003 - 2010
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.cilib.problem.dataset;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MockClusteringStringDataSet extends LocalDataSet {
    private static final long serialVersionUID = 5346632651777290824L;

    private String data = null;

    public MockClusteringStringDataSet() {
        this.identifier = "mock-data-set";
        this.delimiter = ", ";
        this.beginIndex = 0;
        this.endIndex = 1;
        this.classIndex = 2;

        this.data = "-1, 2, Class1\n";
        this.data += "-1, 2, Class1\n";
        this.data += "-1, 2, Class1\n";

        this.data += "-1, -1, Class2\n";
        this.data += "-2, -1, Class2\n";
        this.data += "-2, -1, Class2\n";

        this.data += "2, -1, Class3\n";
        this.data += "1, -1, Class3\n";
        this.data += "1, -2, Class3\n";
    }

    public MockClusteringStringDataSet(MockClusteringStringDataSet rhs) {
        this.data = new String(rhs.data);
        this.delimiter = new String(rhs.delimiter);
        this.beginIndex = rhs.beginIndex;
        this.endIndex = rhs.endIndex;
        this.classIndex = rhs.classIndex;
    }

    @Override
    public MockClusteringStringDataSet getClone() {
        return new MockClusteringStringDataSet(this);
    }

    @Override
    public byte[] getData() {
        try {
            InputStream is = getInputStream();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = is.read(buffer, 0, buffer.length)) != -1) {
                bos.write(buffer, 0, len);
            }
            bos.close();

            return bos.toByteArray();
        }
        catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    @Override
    public InputStream getInputStream() {
        InputStream is = new ByteArrayInputStream(data.getBytes());
        return is;
    }
}
