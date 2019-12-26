package net.haesleinhuepf.clijx.advancedfilters;


import ij.gui.Roi;
import ij.plugin.frame.RoiManager;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clijx.utilities.AbstractCLIJxPlugin;
import org.scijava.plugin.Plugin;

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_pullToROIManager")
public class PullToROIManager extends AbstractCLIJxPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public String getParameterHelpText() {
        return "Image binary_input";
    }

    @Override
    public boolean executeCL() {
        Roi result = PullAsROI.pullAsROI(getCLIJx(), (ClearCLBuffer) (args[0]));

        RoiManager rm = RoiManager.getInstance();
        if (rm == null) {
            rm = new RoiManager();
        }
        rm.addRoi(result);

        return true;
    }

    @Override
    public String getDescription() {
        return "Pulls a binary image from the GPU memory and puts it in the ROI Manager.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D";
    }
}
