package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

import static net.haesleinhuepf.clij.utilities.CLIJUtilities.assertDifferent;

/**
 * Author: @haesleinhuepf
 *         June 2020
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_minimumYProjection")
public class MinimumYProjection extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        return minimumYProjection(getCLIJ2(), (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]));
    }

    public static boolean minimumYProjection(CLIJ2 clij2, ClearCLImageInterface src, ClearCLImageInterface dst_min) {
        assertDifferent(src, dst_min);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst_min", dst_min);

        clij2.execute(MinimumYProjection.class, "minimum_y_projection_x.cl", "minimum_y_projection", dst_min.getDimensions(), dst_min.getDimensions(), parameters);

        return true;
    }


    @Override
    public String getParameterHelpText() {
        return "Image source, ByRef Image destination_sum";
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input)
    {
        return getCLIJ2().create(new long[]{input.getWidth(), input.getDepth()}, input.getNativeType());
    }

    @Override
    public String getDescription() {
        return "Determines the minimum intensity projection of an image along Y.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "3D -> 2D";
    }

}
