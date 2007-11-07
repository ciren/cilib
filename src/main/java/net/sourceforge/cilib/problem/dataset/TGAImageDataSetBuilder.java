/*
 * TGAImageDataSetBuilder.java
 * 
 * Created on Jul 24, 2004
 *
 *
 * Copyright (C) 2004 - CIRG@UP 
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

import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import net.sourceforge.cilib.type.types.Int;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.EuclideanDistanceMeasure;

import org.apache.log4j.Logger;

/**
 * This class represents an uncompressed RGB(A) TGA (targa) image with origin at the bottom left corner as a DataSet.
 * @author Theuns Cloete
 * 
 * @TODO: This must be refactored to be a generic DataSetBuilder... the image format should be setable via a strategy
 */
public class TGAImageDataSetBuilder extends AssociatedPairDataSetBuilder {
	private static final long serialVersionUID = 5180881998608485523L;
	private static Logger log = Logger.getLogger(TGAImageDataSetBuilder.class);
	private String outputImage = null;

	protected char data[][][] = null;
	protected char key[][] = null;

	private final short BYTES_1 = 8;
	private final short BYTES_2 = 16;
	private final short BYTES_3 = 24;
	private int idlength = 0;
	private int colourmaptype = 0;
	private int datatypecode = 0;
	private int colourmaporigin = 0;
	private int colourmaplength = 0;
	private int colourmapdepth = 0;
	private int x_origin = 0;
	private int y_origin = 0;
	private int width = 0;
	private int height = 0;
	private int bitsperpixel = 0;
	private int depth = 0;
	private int imagedescriptor = 0;
	private byte[] idfield = null;
	private byte[] colourmapdata = null;

	public TGAImageDataSetBuilder() {
		distanceMeasure = new EuclideanDistanceMeasure();
		outputImage = "output.tga";
	}
	
	@Override
	public void initialise() {
		for(DataSet dataset : this) {
			DataInputStream dis = new DataInputStream(dataset.getInputStream());
			try {
				readHeader(dis);
				log.info("Reading image data...");
				data = new char[width][height][depth];
				key = new char[width][height];
				for(int x = 0; x < width; x++) {
					for(int y = 0; y < height; y++) {
						for(int z = 0; z < depth; z++) {
							data[x][y][z] = (char)dis.readUnsignedByte();
						}
						key[x][y] = (char)0;
					}
				}
				log.info("done");
			}
			catch (IOException io) {
				throw new RuntimeException(io);
			}
		}
	}
	
	public void uninitialise(Vector centroids) {
		try {
			FileOutputStream fos = new FileOutputStream(outputImage);
			writeHeader(fos);
			System.out.println(centroids);
			log.info("Writing image data...");
			for(int x = 0; x < width; x++) {
				for(int y = 0; y < height; y++) {
					Vector centroid = arrangedCentroids.get(key[x][y]);
					for(int z = 0; z < depth; z++) {
						fos.write(centroid.getInt(z));
					}
				}
			}
			for(int i = 0; i < numberOfClusters * 2; i++) {
				for(int j = 0; j < width; j++) {
					Vector centroid = arrangedCentroids.get(i / 2);
					for(int k = 0; k < depth; k++) {
						fos.write(centroid.getInt(k));
					}
				}
			}
			log.info("done");
		}
		catch (IOException iox) {
			throw new RuntimeException(iox);
		}
	}
	
	private void readHeader(DataInputStream dis) throws IOException{
		idlength = dis.readUnsignedByte();
		colourmaptype = dis.readUnsignedByte();
		datatypecode = dis.readUnsignedByte();
		colourmaporigin |= dis.readUnsignedByte();
		colourmaporigin |= dis.readUnsignedByte() << BYTES_1;
		colourmaplength |= dis.readUnsignedByte();
		colourmaplength |= dis.readUnsignedByte() << BYTES_1;
		colourmapdepth = dis.readUnsignedByte();
		x_origin |= dis.readUnsignedByte();
		x_origin |= dis.readUnsignedByte() << BYTES_1;
		y_origin |= dis.readUnsignedByte();
		y_origin |= dis.readUnsignedByte() << BYTES_1;
		width |= dis.readUnsignedByte();
		width |= dis.readUnsignedByte() << BYTES_1;
		height |= dis.readUnsignedByte();
		height |= dis.readUnsignedByte() << BYTES_1;
		bitsperpixel = dis.readUnsignedByte();
		depth = bitsperpixel / BYTES_1;
		imagedescriptor = dis.readUnsignedByte();
		idfield = new byte[idlength];
		if(dis.read(idfield, 0, idlength) != idlength)
			throw new IOException("Cannot read Image Identification field");
		colourmapdata = new byte[colourmaplength];
		if(dis.read(colourmapdata, 0, colourmaplength) != colourmaplength)
			throw new IOException("Cannot read Colour Map Data");
	}
	
	private void writeHeader(FileOutputStream fos) throws IOException {
		fos.write(idlength);
		fos.write(colourmaptype);
		fos.write(datatypecode);
		fos.write((colourmaporigin << BYTES_3) >> BYTES_3);
		fos.write((colourmaporigin << BYTES_2) >> BYTES_3);
		fos.write((colourmaplength << BYTES_3) >> BYTES_3);
		fos.write((colourmaplength << BYTES_2) >> BYTES_3);
		fos.write(colourmapdepth);
		fos.write((x_origin << BYTES_3) >> BYTES_3);
		fos.write((x_origin << BYTES_2) >> BYTES_3);
		fos.write((y_origin << BYTES_3) >> BYTES_3);
		fos.write((y_origin << BYTES_2) >> BYTES_3);
		fos.write((width << BYTES_3) >> BYTES_3);
		fos.write((width << BYTES_2) >> BYTES_3);
		height += numberOfClusters * 2;
		fos.write((height << BYTES_3) >> BYTES_3);
		fos.write((height << BYTES_2) >> BYTES_3);
		height -= numberOfClusters * 2;
		fos.write(bitsperpixel);
		fos.write(imagedescriptor);
		fos.write(idfield, 0, idlength);
		fos.write(colourmapdata, 0, colourmaplength);
	}
	
	@Override
	public Pattern getPattern(int index) {
		Vector tmp = new Vector();
		for(int z = 0; z < depth; z++)
			tmp.add(new Int((int)data[index % width][index / width][z]));
		int k = (int)key[index % width][index / width];
		return new Pattern(index, k, tmp);
	}

	@Override
	public int getNumberOfPatterns() {
		return width * height;
	}

	public String getOutputImage() {
		return outputImage;
	}

	public void setOutputImage(String oi) {
		outputImage = oi;
	}
}
