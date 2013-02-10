/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.io;

import java.util.LinkedList;
import java.util.List;
import net.sourceforge.cilib.io.exception.CIlibIOException;
import net.sourceforge.cilib.io.transform.DataOperator;

/**
 * A class that builds a new instance of a DataTable object. By changing the type
 * of {@link #dataTable dataTable} and {@link #dataReader dataReader} that is used,
 * the behaviour changes. The default behaviour is to read a text file from the local
 * machine to a text datatable.
 */
public class DataTableBuilder {

    private DataTable dataTable;
    private DataReader dataReader;
    private List<DataOperator> operatorPipeline;

    /**
     * Default empty constructor
     */
    public DataTableBuilder() {
        dataReader = new DelimitedTextFileReader();
        dataTable = new StandardDataTable();
        operatorPipeline = new LinkedList<DataOperator>();
    }

    /**
     * Default constructor. Initialises dataTable to a new TextDataTable and
     * dataReader to be a new TextDataReader.
     * @param reader
     */
    public DataTableBuilder(DataReader reader) {
        dataReader = reader;
        dataTable = new StandardDataTable();
        operatorPipeline = new LinkedList<DataOperator>();
    }

    /**
     * This method reads all rows from the {@link #dataReader DataReader} object and
     * adds them into the {@link #dataTable DataTable} object. If the default
     * behaviour is not sufficient or desired, method should be overridden.
     * @return the constructed datatable.
     * @throws CIlibIOException wraps another Exception that might occur during IO
     */
    public DataTable buildDataTable() throws CIlibIOException {
        dataReader.open();
        while (dataReader.hasNextRow()) {
            dataTable.addRow(dataReader.nextRow());
        }
        dataTable.setColumnNames(dataReader.getColumnNames());
        dataReader.close();
        for (DataOperator operator : operatorPipeline) {
            this.setDataTable(operator.operate(this.getDataTable()));
        }
        return (DataTable) this.dataTable.getClone();
    }

    /**
     * Adds a DataOperator to the pipeline.
     * @param dataOperator a new DataOperator.
     */
    public void addDataOperator(DataOperator dataOperator) {
        operatorPipeline.add(dataOperator);
    }

    /**
     * Get the DataReader object.
     * @return the data reader.
     */
    public DataReader getDataReader() {
        return dataReader;
    }

    /**
     * Set the DataReader object.
     * @param dataReader the data reader object.
     */
    public void setDataReader(DataReader dataReader) {
        this.dataReader = dataReader;
    }

    /**
     * Get the DataTable object.
     * @return the data table.
     */
    public DataTable getDataTable() {
        return (DataTable) this.dataTable.getClone();
    }

    /**
     * Sets the DataTable object.
     * @param dataTable the data table.
     */
    public void setDataTable(DataTable dataTable) {
        this.dataTable = dataTable;
    }

    /**
     * Gets the operator pipeline.
     * @return the operator pipeline.
     */
    public List<DataOperator> getOperatorPipeline() {
        return operatorPipeline;
    }

    /**
     * Sets the operator pipeline.
     * @param operatorPipeline the new operator pipeline.
     */
    public void setOperatorPipeline(List<DataOperator> operatorPipeline) {
        this.operatorPipeline = operatorPipeline;
    }

    /**
     * Convenience method for getting the source URL that the datatable is built
     * from. Delegates to: {@link #dataReader dataReader} object.
     * @return the source URL.
     */
    public String getSourceURL() {
        return this.dataReader.getSourceURL();
    }

    /**
     * Convenience method for setting the source URL that the datatable is built
     * from. Delegates to: {@link #dataReader dataReader} object.
     * @param sourceURL the new source URL.
     */
    public void setSourceURL(String sourceURL) {
        this.dataReader.setSourceURL(sourceURL);
    }
}
