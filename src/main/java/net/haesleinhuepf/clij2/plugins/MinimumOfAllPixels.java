package net.haesleinhuepf.clij2.plugins;

import ij.measure.ResultsTable;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clij2.utilities.IsCategorized;
import net.imglib2.Cursor;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.RealType;
import net.imglib2.view.Views;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

/**
 * Author: @haesleinhuepf
 * December 2018
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_minimumOfAllPixels")
public class MinimumOfAllPixels extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, IsCategorized {
    @Override
    public String getCategories() {
        return "Measurements";
    }

    @Override
    public boolean executeCL() {
        double minimumGreyValue = getCLIJ2().minimumOfAllPixels((ClearCLBuffer)( args[0]));

        ResultsTable table = ResultsTable.getResultsTable();
        table.incrementCounter();
        table.addValue("Min", minimumGreyValue);
        table.show("Results");
        return true;
    }

    public static double minimumOfAllPixels(CLIJ2 clij2, ClearCLImageInterface clImage) {
        ClearCLImageInterface clReducedImage = clImage;
        if (clImage.getDimension() == 3) {
            clReducedImage = clij2.create(new long[]{clImage.getWidth(), clImage.getHeight()}, clImage.getNativeType());

            HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("src", clImage);
            parameters.put("dst", clReducedImage);
            clij2.execute(MinimumOfAllPixels.class, "minimum_projection_3d_2d_x.cl", "minimum_projection_3d_2d", clReducedImage.getDimensions(), clReducedImage.getDimensions(), parameters);
        }

        RandomAccessibleInterval rai = clij2.convert(clReducedImage, RandomAccessibleInterval.class);
        Cursor cursor = Views.iterable(rai).cursor();
        float minimumGreyValue = Float.MAX_VALUE;
        while (cursor.hasNext()) {
            float greyValue = ((RealType) cursor.next()).getRealFloat();
            if (minimumGreyValue > greyValue) {
                minimumGreyValue = greyValue;
            }
        }

        if (clImage != clReducedImage) {
            clij2.release(clReducedImage);
        }
        return minimumGreyValue;
    }

    @Override
    public String getParameterHelpText() {
        return "Image source";
    }

    @Override
    public String getDescription() {
        return "Determines the minimum of all pixels in a given image. \n\nIt will be stored in a new row of ImageJs\n" +
                "Results table in the column 'Min'.\n\n" +
                "Parameters\n" +
                "----------\n" +
                "source : Image\n" +
                "    The image of which the minimum of all pixels or voxels will be determined.\n";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }

}
