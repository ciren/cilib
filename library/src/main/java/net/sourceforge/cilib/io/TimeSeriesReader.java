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
package net.sourceforge.cilib.io;


import java.util.ArrayList;
import java.util.List;
import net.sourceforge.cilib.io.exception.CIlibIOException;
import net.sourceforge.cilib.type.types.Type;

public class TimeSeriesReader implements DataReader<List<Type>> {
    private DataReader<List<Type>> delegate;
    private int embedding = 2; // default value: predict the next step from the previous step, thus, embedding = two steps in the time series
    private int tau = 1; // specifies the time lag between observations
    private boolean hasNextRow = true;
    private int step = 1; // specifies the distance between patterns
    private List<Type> embeddedRow;
    private List<Type> dataSample;


    public TimeSeriesReader() {
        super();
        this.embeddedRow = new ArrayList<Type>();
        this.dataSample = new ArrayList<Type>();
    }


    @Override
    public void close() throws CIlibIOException {
       delegate.close();
    }

    @Override
    public List<String> getColumnNames() {
        return delegate.getColumnNames();
    }

    @Override
    public String getSourceURL() {
        return delegate.getSourceURL();
    }

    @Override
    public boolean hasNextRow() throws CIlibIOException {
        return hasNextRow && delegate.hasNextRow();
    }

    /**
     * Creates the next row based on the specified embedding and tau. Source is expected to be a single column of time-series values.
     * @return the next row
     * @obsolete
     */
    public List<Type> nextRowObsolete(){
      for(int i = 0; i < this.embedding; i++) {
          try {
              if(delegate.hasNextRow()) {
                  List<Type> nextInputRow = delegate.nextRow();
                  for(Type nextRowItem : nextInputRow) {
                      embeddedRow.add(nextRowItem);
                  }
                  if(embeddedRow.size() > embedding) {
                      while(embeddedRow.size() != embedding) {
                          embeddedRow.remove(0);
                      }
                      return embeddedRow;
                  }
              }
              else { // can't construct another embedded row, so just return the previous one and set hasNextRow flag to false
                this.hasNextRow = false;
                return embeddedRow;
              }
          } catch (CIlibIOException e) {
            e.printStackTrace();
          }
      }
      return embeddedRow;
    }

    /**
     * Creates the next row based on the specified embedding and tau. Source is expected to be a single column of time-series values.
     * @return the next row
     */
    @Override
    public List<Type> nextRow(){
          try {
              if(dataSample.size() > 0) { // not the first pattern
                  for(int j = 0; j < step; j++) {
                      dataSample.remove(0); // remove n previous entries
                      if(delegate.hasNextRow()) {
                          dataSample.add(delegate.nextRow().get(0)); // we only operate with index 0 because we assume univariate time series
                      } else {
                          hasNextRow = false;
                          return embeddedRow; // previous pattern
                      }
                  }
              } else { // construct the first dataSample vector
                  int sampleSize = tau * (embedding - 1) + 1;
                  for(int c = 0; c < sampleSize; c++) {
                      if(delegate.hasNextRow()) {
                          dataSample.add(delegate.nextRow().get(0)); // we only operate with index 0 because we assume univariate time series
                      } else {
                          hasNextRow = false;
                          return embeddedRow; // this will be empty
                      }
                  }
              } // Now that dataSample is constructed, sample the embedded row
              embeddedRow.clear();
              for(int j = 0; j < dataSample.size(); j+=tau) {
                  embeddedRow.add(dataSample.get(j));
              }
              return embeddedRow;
          } catch (CIlibIOException e) {
            e.printStackTrace();
            return null;
          }
    }

    @Override
    public void open() throws CIlibIOException {
        delegate.open();
    }

    @Override
    public void setSourceURL(String sourceURL) {
        delegate.setSourceURL(sourceURL);
    }

    public DataReader<List<Type>> getDelegate() {
        return delegate;
    }

    public void setDelegate(DataReader<List<Type>> delegate) {
        this.delegate = delegate;
    }

    public int getEmbedding() {
        return embedding;
    }

    public void setEmbedding(int embedding) {
        this.embedding = embedding;
    }

    public int getTau() {
        return tau;
    }

    public void setTau(int tau) {
        this.tau = tau;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }
}