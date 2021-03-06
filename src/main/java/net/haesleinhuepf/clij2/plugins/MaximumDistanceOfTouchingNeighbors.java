package net.haesleinhuepf.clij2.plugins;


import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clij2.utilities.HasClassifiedInputOutput;
import net.haesleinhuepf.clij2.utilities.IsCategorized;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_maximumDistanceOfTouchingNeighbors")
public class MaximumDistanceOfTouchingNeighbors extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, IsCategorized, HasClassifiedInputOutput {
    @Override
    public String getInputType() {
        return "Image, Matrix";
    }

    @Override
    public String getOutputType() {
        return "Image";
    }


    @Override
    public String getParameterHelpText() {
        return "Image distance_matrix, Image touch_matrix, ByRef Image distancelist_destination";
    }

    @Override
    public boolean executeCL() {
        boolean result = maximumDistanceOfTouchingNeighbors(getCLIJ2(), (ClearCLBuffer) (args[0]), (ClearCLBuffer) (args[1]),  (ClearCLBuffer) (args[2]));
        return result;
    }

    public static boolean maximumDistanceOfTouchingNeighbors(CLIJ2 clij2, ClearCLBuffer distance_matrix, ClearCLBuffer touch_matrix, ClearCLBuffer minimum_distancelist_destination) {

        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("src_distance_matrix", distance_matrix);
        parameters.put("src_touch_matrix", touch_matrix);
        parameters.put("dst_distance_list", minimum_distancelist_destination);

        long[] globalSizes = new long[]{distance_matrix.getWidth()};

        clij2.activateSizeIndependentKernelCompilation();
        clij2.execute(MaximumDistanceOfTouchingNeighbors.class, "maximum_distance_of_touching_neighbors_x.cl", "maximum_distance_of_touching_neighbors", globalSizes, globalSizes, parameters);

        return true;
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input) {
        return clij.create(new long[]{input.getWidth(), 1, 1}, NativeTypeEnum.Float);
    }

    @Override
    public String getDescription() {
        return "Takes a touch matrix and a distance matrix to determine the maximum distance of touching neighbors for every object.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D";
    }

    @Override
    public String getCategories() {
        return "Graph, Measurements";
    }
}
