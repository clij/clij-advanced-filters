package net.haesleinhuepf.clijx.plugins;

import net.haesleinhuepf.clij2.plugins.EqualConstant;
import net.haesleinhuepf.clijx.CLIJx;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clijx.utilities.AbstractCLIJxPlugin;
import org.scijava.plugin.Plugin;

/**
 * Author: @haesleinhuepf
 *         August 2019
 */

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_labelToMask")
public class LabelToMask extends AbstractCLIJxPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        boolean result = labelToMask(getCLIJx(), (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), asFloat(args[2]));
        return result;
    }

    public static boolean labelToMask(CLIJx clijx, ClearCLBuffer labelMap, ClearCLBuffer maskOutput, Float index) {
        return EqualConstant.equalConstant(clijx, labelMap, maskOutput, index);
    }

    @Override
    public String getParameterHelpText() {
        return "Image label_map_source, Image mask_destination, Number label_index";
    }

    @Override
    public String getDescription() {
        return "Masks a single label in a label map: Sets all pixels in the target image to 1, where the given label" +
                " index was present in the label map. Other pixels are set to 0.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
