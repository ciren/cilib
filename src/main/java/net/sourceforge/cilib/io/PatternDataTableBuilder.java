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

/**
 *
 * @author gpampara
 */
public class PatternDataTableBuilder{}// implements DataTableBuilder {
//
//    private Vector dataDomain;
//    private Type targetDomain;
//    private DataReader reader;
//    private int beginIndex;
//    private int endIndex;
//    private int targetIndex;
//
//    public PatternDataTableBuilder() {
//        reader = new DelimitedTextFileReader();
//    }
//
//    @Override
//    public DataTable buildDataTable() throws IOException {
//        StandardPatternDataTable table = new StandardPatternDataTable();
//        for (Object object : reader) {
//            Iterable<StringType> stringTypes = (Iterable<StringType>) object;
//            List<StringType> intermediate = Lists.newArrayList(stringTypes);
//            Vector data = new Vector();
//
//            // Conversion for the data domain
//            Preconditions.checkState(beginIndex >= 0);
//            Preconditions.checkState(beginIndex <= endIndex);
//            Preconditions.checkState(endIndex < intermediate.size());
//
//            for (int i = beginIndex; i < endIndex; ++i) {
//                Numeric n = dataDomain.get(i).getClone();
//                n.set(intermediate.get(i).toString());
//
//                Preconditions.checkState(Types.isInsideBounds(n));
//                data.add(n);
//            }
//
//            // Converstion for the target
//            Preconditions.checkState(targetIndex >= 0 && targetIndex < intermediate.size());
//            StringType target = intermediate.get(targetIndex);
//            table.addRow(new StandardPattern(data, target));
//        }
//        table.setColumnNames(reader.getColumnNames());
//        reader.close();
//        return table;
//    }
//
//    @Override
//    public DataReader getDataReader() {
//        return reader;
//    }
//
//    @Override
//    public String getSourceURL() {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public void setDataReader(DataReader dataReader) {
//        this.reader = dataReader;
//    }
//
//    @Override
//    public void setSourceURL(String sourceURL) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    public void setDataDomain(String domain) {
//        dataDomain = Parser.parse(domain);
//    }
//
//    public void setTargetDomain(String domain) {
//        targetDomain = Parser.parse(domain);
//    }
//
//    public int getBeginIndex() {
//        return beginIndex;
//    }
//
//    public void setBeginIndex(int beginIndex) {
//        this.beginIndex = beginIndex;
//    }
//
//    public int getEndIndex() {
//        return endIndex;
//    }
//
//    public void setEndIndex(int endIndex) {
//        this.endIndex = endIndex;
//    }
//
//    public int getTargetIndex() {
//        return targetIndex;
//    }
//
//    public void setTargetIndex(int targetIndex) {
//        this.targetIndex = targetIndex;
//    }
//}
