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

public class MockStringDataSet extends DataSet {
    private static final long serialVersionUID = 8253706217084102158L;

    protected String data = null;

    public MockStringDataSet() {
        super();
        data = "CGTGGTAATCAC\n";
        data += "AGCAGTGTATCC\n";
        data += "CGGGCTATGCCG";
    }

    public MockStringDataSet(MockStringDataSet rhs) {
        super();
        data = new String(rhs.data);
    }

    @Override
    public MockStringDataSet getClone() {
        return new MockStringDataSet(this);
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
        catch (IOException ioException) {
            throw new RuntimeException(ioException);
        }
    }

    @Override
    public InputStream getInputStream() {
        InputStream is = new ByteArrayInputStream(data.getBytes());
        return is;
    }
}
