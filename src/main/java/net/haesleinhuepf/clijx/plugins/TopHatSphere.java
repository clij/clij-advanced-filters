package net.haesleinhuepf.clijx.plugins;


import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clijx.CLIJx;
import net.haesleinhuepf.clijx.utilities.AbstractCLIJxPlugin;
import org.scijava.plugin.Plugin;

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_topHatSphere")
public class TopHatSphere extends AbstractCLIJxPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public String getParameterHelpText() {
        return "Image input, Image destination, Number radiusX, Number radiusY, Number radiusZ";
    }

    @Override
    public boolean executeCL() {

        Object[] args = openCLBufferArgs();
        boolean result = topHatSphere(getCLIJx(), (ClearCLBuffer) (args[0]), (ClearCLBuffer) (args[1]), asInteger(args[2]), asInteger(args[3]), asInteger(args[4]));
        releaseBuffers(args);
        return result;
    }

    public static boolean topHatSphere(CLIJx clijx, ClearCLBuffer input, ClearCLBuffer output, Integer radiusX, Integer radiusY, Integer radiusZ) {

        ClearCLBuffer temp1 = clijx.create(input);
        ClearCLBuffer temp2 = clijx.create(input);

        if (input.getDimension() == 3 ) {
            clijx.minimum3DSphere(input, temp1, radiusX, radiusX, radiusZ);
            clijx.maximum3DSphere(temp1, temp2, radiusX, radiusY, radiusZ);
        } else {
            clijx.minimum2DSphere(input, temp1, radiusX, radiusX);
            clijx.maximum2DSphere(temp1, temp2, radiusX, radiusY);
        }
        clijx.subtractImages(input, temp2, output);

        clijx.release(temp1);
        clijx.release(temp2);
        return true;
    }

    @Override
    public String getDescription() {
        return "Applies a top-hat filter for background subtraction to the input image.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
