package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clij2.utilities.HasClassifiedInputOutput;
import net.haesleinhuepf.clij2.utilities.IsCategorized;
import org.scijava.plugin.Plugin;

//import net.haesleinhuepf.clijx.CLIJx;

/**
 * ConnectedComponentsLabeling
 * <p>
 * <p>
 * <p>
 * Author: @haesleinhuepf
 * 06 2019
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_connectedComponentsLabelingBox")
public class ConnectedComponentsLabelingBox extends ConnectedComponentsLabeling implements IsCategorized, HasClassifiedInputOutput {
    @Override
    public String getInputType() {
        return "Binary Image";
    }

    @Override
    public String getOutputType() {
        return "Label Image";
    }


    @Override
    public boolean executeCL() {
        Object[] args = openCLBufferArgs();
        ClearCLBuffer input = (ClearCLBuffer) args[0];
        ClearCLBuffer output = (ClearCLBuffer) args[1];

        boolean result = getCLIJ2().connectedComponentsLabelingBox(input, output);
        releaseBuffers(args);
        return result;
    }
    public static boolean connectedComponentsLabelingBox(CLIJ2 clij2, ClearCLImageInterface input, ClearCLImageInterface output) {
        return connectedComponentsLabeling_internal(clij2, input, output, true, true);
    }

    public static boolean connectedComponentsLabelingBox(CLIJ2 clij2, ClearCLImageInterface input, ClearCLImageInterface output, boolean forceContinousLabeling) {
        return connectedComponentsLabeling_internal(clij2, input, output, true, forceContinousLabeling);
    }

    @Override
    public String getDescription() {
        return "Performs connected components analysis inspecting the box neighborhood of every pixel to a binary image and generates a label map.";
    }

    @Override
    public String getCategories() {
        return "Label";
    }
}
