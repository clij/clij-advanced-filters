package net.haesleinhuepf.clijx.advancedmath;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clijx.CLIJx;
import net.haesleinhuepf.clijx.utilities.AbstractCLIJxPlugin;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

import static net.haesleinhuepf.clij.utilities.CLIJUtilities.assertDifferent;
import static net.haesleinhuepf.clijx.utilities.CLIJUtilities.checkDimensions;

/**
 * Author: @haesleinhuepf
 * 12 2018
 */

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_minimumImageAndScalar")
public class MinimumImageAndScalar extends AbstractCLIJxPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        return minimumImageAndScalar(getCLIJx(), (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), asFloat(args[2]));
    }

    public static boolean minimumImageAndScalar(CLIJx clijx, ClearCLImageInterface src, ClearCLImageInterface dst, Float valueB) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("valueB", valueB);
        parameters.put("dst", dst);

        if (!checkDimensions(src.getDimension(), src.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (minimumImageAndScalar)");
        }
        clijx.execute(MinimumImageAndScalar.class, "math" + src.getDimension() + "D.cl", "minPixelwiseScalar_" + src.getDimension() + "d", dst.getDimensions(), dst.getDimensions(), parameters);
        return true;
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, Image destination, Number scalar";
    }

    @Override
    public String getDescription() {
        return "Computes the minimum of a constant scalar s and each pixel value x in a given image X.\n\n<pre>f(x, s) = min(x, s)</pre>";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }

}