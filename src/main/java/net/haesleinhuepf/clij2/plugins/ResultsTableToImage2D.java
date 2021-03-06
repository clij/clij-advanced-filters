package net.haesleinhuepf.clij2.plugins;

import ij.measure.ResultsTable;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import org.scijava.plugin.Plugin;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.FloatBuffer;

/**
 * Author: @haesleinhuepf
 *         September 2019
 */
@Deprecated
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_resultsTableToImage2D")
public class ResultsTableToImage2D extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        ClearCLBuffer buffer = (ClearCLBuffer)( args[0]);
        ResultsTable table = ResultsTable.getResultsTable();
        getCLIJ2().resultsTableToImage2D(buffer, table);
        return true;
    }

    @Deprecated
    public static boolean resultsTableToImage2D(CLIJ2 clij2, ClearCLBuffer buffer, ResultsTable table) {

        int rows = table.getCounter();
        String[] headings = table.getHeadings();
        int cols = headings.length;
        int c = 0;

        if (buffer.getNativeType() == NativeTypeEnum.UnsignedByte) {
            byte[] array = new byte[(int) (buffer.getWidth() * buffer.getHeight())];

            for (int y = 0; y < rows; y++) {
                for (int x = 0; x < cols; x++ ) {
                    int realX = table.getColumnIndex(headings[x]);
                    array[c] = (byte) table.getValueAsDouble(realX, y);
                    c++;
                }
            }
            ByteBuffer arrayBuffer = ByteBuffer.wrap(array);
            buffer.readFrom(arrayBuffer, true);
        } else if (buffer.getNativeType() == NativeTypeEnum.UnsignedShort) {
            char[] array = new char[(int) (buffer.getWidth() * buffer.getHeight())];

            for (int y = 0; y < rows; y++) {
                for (int x = 0; x < cols; x++ ) {
                    int realX = table.getColumnIndex(headings[x]);
                    array[c] = (char) table.getValueAsDouble(realX, y);
                    c++;
                }
            }
            CharBuffer arrayBuffer = CharBuffer.wrap(array);
            buffer.readFrom(arrayBuffer, true);
        } else {
            float[] array = new float[(int) (buffer.getWidth() * buffer.getHeight())];

            for (int y = 0; y < rows; y++) {
                for (int x = 0; x < cols; x++ ) {
                    int realX = table.getColumnIndex(headings[x]);
                    array[c] = (float) table.getValueAsDouble(realX, y);
                    c++;
                }
            }
            FloatBuffer arrayBuffer = FloatBuffer.wrap(array);
            buffer.readFrom(arrayBuffer, true);

        }
        return true;
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input) {
        ResultsTable table = ResultsTable.getResultsTable();
        return clij.create(new long[]{table.getHeadings().length, table.getCounter()}, NativeTypeEnum.Float);
    }

    @Override
    public String getParameterHelpText() {
        return "ByRef Image destination";
    }

    @Override
    public String getDescription() {
        return "Converts a table to an image. \n\nRows stay rows, columns stay columns.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D";
    }

}
