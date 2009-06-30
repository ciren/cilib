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
package net.sourceforge.cilib.problem.dataset;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MockClusteringStringDataSet extends LocalDataSet {
    private static final long serialVersionUID = 5346632651777290824L;

    private String data = null;

    public MockClusteringStringDataSet() {
        delimiter = ",\\s";
        beginIndex = 0;
        endIndex = 1;
        classIndex = 2;

        data = "5, 1, Class0\n";

        data += "32, 1, Class1\n";
        data += "33, 1, Class1\n";
        data += "34, 1, Class1\n";
        data += "35, 1, Class1\n";
        data += "36, 1, Class1\n";
        data += "32, 2, Class1\n";
        data += "33, 2, Class1\n";
        data += "34, 2, Class1\n";
        data += "35, 2, Class1\n";
        data += "36, 2, Class1\n";
        data += "32, 3, Class1\n";
        data += "33, 3, Class1\n";
        data += "34, 3, Class1\n";
        data += "35, 3, Class1\n";
        data += "36, 3, Class1\n";
        data += "32, 4, Class1\n";
        data += "33, 4, Class1\n";
        data += "34, 4, Class1\n";
        data += "35, 4, Class1\n";
        data += "36, 4, Class1\n";
        data += "32, 5, Class1\n";
        data += "33, 5, Class1\n";
        data += "34, 5, Class1\n";
        data += "35, 5, Class1\n";
        data += "36, 5, Class1\n";

        data += "23, 9, Class2\n";
        data += "23, 10, Class2\n";
        data += "22, 11, Class2\n";
        data += "23, 11, Class2\n";
        data += "24, 11, Class2\n";
        data += "22, 12, Class2\n";
        data += "23, 12, Class2\n";
        data += "24, 12, Class2\n";
        data += "22, 13, Class2\n";
        data += "23, 13, Class2\n";
        data += "24, 13, Class2\n";
        data += "23, 14, Class2\n";
        data += "23, 15, Class2\n";

        data += "16, 18, Class3\n";
        data += "17, 18, Class3\n";
        data += "14, 19, Class3\n";
        data += "15, 19, Class3\n";
        data += "16, 19, Class3\n";
        data += "17, 19, Class3\n";

        data += "22, 19, Class4\n";
        data += "24, 19, Class4\n";
        data += "26, 19, Class4\n";
        data += "28, 19, Class4\n";

        data += "13, 20, Class3\n";
        data += "14, 20, Class3\n";
        data += "15, 20, Class3\n";
        data += "16, 20, Class3\n";
        data += "12, 21, Class3\n";
        data += "13, 21, Class3\n";
        data += "14, 21, Class3\n";
        data += "15, 21, Class3\n";
        data += "16, 21, Class3\n";

        data += "22, 21, Class4\n";
        data += "24, 21, Class4\n";
        data += "26, 21, Class4\n";
        data += "28, 21, Class4\n";

        data += "12, 22, Class3\n";
        data += "13, 22, Class3\n";
        data += "14, 22, Class3\n";
        data += "15, 22, Class3\n";
        data += "11, 23, Class3\n";
        data += "12, 23, Class3\n";
        data += "13, 23, Class3\n";
        data += "14, 23, Class3\n";

        data += "22, 23, Class4\n";
        data += "24, 23, Class4\n";
        data += "26, 23, Class4\n";
        data += "28, 23, Class4\n";

        data += "11, 24, Class3\n";
        data += "12, 24, Class3\n";

        data += "22, 25, Class4\n";
        data += "24, 25, Class4\n";
        data += "26, 25, Class4\n";
        data += "28, 25, Class4\n";

        data += "20, 30, Class5\n";

        data += "17, 38, Class6\n";
        data += "19, 38, Class6\n";
        data += "21, 38, Class6\n";
        data += "18, 39, Class6\n";
        data += "20, 39, Class6\n";
        data += "17, 40, Class6\n";
        data += "21, 40, Class6\n";
        data += "18, 41, Class6\n";
        data += "20, 41, Class6\n";
        data += "17, 42, Class6\n";
        data += "19, 42, Class6\n";
        data += "21, 42, Class6\n";
    }

    public MockClusteringStringDataSet(MockClusteringStringDataSet rhs) {
        data = new String(rhs.data);
        delimiter = new String(rhs.delimiter);
        beginIndex = rhs.beginIndex;
        endIndex = rhs.endIndex;
        classIndex = rhs.classIndex;
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
