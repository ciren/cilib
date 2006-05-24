package net.sourceforge.cilib.problem.dataset;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MockStringDataSet extends DataSet {
	
	private String data;
	
	public MockStringDataSet() {
		data = "CGTGGTAATCAC\n";
		data += "AGCAGTGTATCC\n";
		data += "CGGGCTATGCCG";
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
