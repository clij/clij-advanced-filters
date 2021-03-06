package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clij2.utilities.HasClassifiedInputOutput;
import net.haesleinhuepf.clij2.utilities.IsCategorized;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

import static net.haesleinhuepf.clij.utilities.CLIJUtilities.assertDifferent;
import static net.haesleinhuepf.clij2.utilities.CLIJUtilities.checkDimensions;

/**
 * Author: @haesleinhuepf
 * December 2018
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_detectMaximaSliceBySliceBox")
public class DetectMaximaSliceBySliceBox extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, IsCategorized, HasClassifiedInputOutput {
    @Override
    public String getInputType() {
        return "Image";
    }

    @Override
    public String getOutputType() {
        return "Binary Image";
    }


    @Override
    public boolean executeCL() {
        return getCLIJ2().detectMaximaSliceBySliceBox((ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), asInteger(args[2]), asInteger(args[3]));
    }


    public static boolean detectMaximaSliceBySliceBox(CLIJ2 clij2, ClearCLBuffer src, ClearCLBuffer dst, Integer radiusX, Integer radiusY) {
        assertDifferent(src, dst);
        if (!checkDimensions(src.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (detectOptima)");
        }

        ClearCLBuffer temp = clij2.create(dst.getDimensions(), clij2.Float);
        clij2.meanBox(src, temp, radiusX, radiusY, 0);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", temp);
        parameters.put("dst", dst);

        clij2.execute(DetectMaximaSliceBySliceBox.class, "detect_maxima_3d_slice_by_slice_x.cl", "detect_maxima_3d_slice_by_slice", dst.getDimensions(), dst.getDimensions(), parameters);
        temp.close();
        return true;
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, ByRef Image destination, Number radiusX, Number radiusY";
    }

    @Override
    public String getDescription() {
        return "Detects local maxima in a given square neighborhood of an input image stack. \n\n" +
                "The input image stack is processed slice by slice. Pixels in the resulting image are set to 1 if \n" +
                "there is no other pixel in a given radius which has a higher intensity, and to 0 otherwise.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "3D";
    }

    @Override
    public String getCategories() {
        return "Binary, Detection";
    }
}
