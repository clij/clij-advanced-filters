package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clij2.utilities.HasClassifiedInputOutput;
import net.haesleinhuepf.clij2.utilities.IsCategorized;
import org.scijava.plugin.Plugin;

/**
 * Author: @haesleinhuepf
 * 12 2018
 */

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_invert")
public class Invert extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, IsCategorized, HasClassifiedInputOutput {
    @Override
    public String getInputType() {
        return "Image";
    }

    @Override
    public String getOutputType() {
        return "Image";
    }

    @Override
    public String getCategories() {
        return "Math";
    }

    @Override
    public boolean executeCL() {

        return getCLIJ2().invert((ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]));
    }

    public static boolean invert(CLIJ2 clij2, ClearCLImageInterface src, ClearCLImageInterface dst) {
        return clij2.multiplyImageAndScalar(src, dst, -1f);
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input) {
        return getCLIJ2().create(input.getDimensions(), NativeTypeEnum.Float);
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, ByRef Image destination";
    }

    @Override
    public String getDescription() {
        return "Computes the negative value of all pixels in a given image. \n\nIt is recommended to convert images to \n" +
                "32-bit float before applying this operation.\n\n" +
                "<pre>f(x) = - x</pre>\n\n" +
                "For binary images, use binaryNot.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
