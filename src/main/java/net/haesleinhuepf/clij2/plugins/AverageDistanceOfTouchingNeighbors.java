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

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_averageDistanceOfTouchingNeighbors")
public class AverageDistanceOfTouchingNeighbors extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, IsCategorized, HasClassifiedInputOutput {
    @Override
    public String getInputType() {
        return "Matrix";
    }

    @Override
    public String getOutputType() {
        return "Vector";
    }

    @Override
    public String getCategories() {
        return "Measurements, Graph";
    }

    @Override
    public String getParameterHelpText() {
        return "Image distance_matrix, Image touch_matrix, ByRef Image average_distancelist_destination";
    }

    @Override
    public boolean executeCL() {
        Object[] args = openCLBufferArgs();
        boolean result = getCLIJ2().averageDistanceOfTouchingNeighbors((ClearCLBuffer) (args[0]), (ClearCLBuffer) (args[1]),  (ClearCLBuffer) (args[2]));
        releaseBuffers(args);
        return result;
    }

    public static boolean averageDistanceOfTouchingNeighbors(CLIJ2 clij2, ClearCLBuffer distance_matrix, ClearCLBuffer touch_matrix, ClearCLBuffer average_distancelist_destination) {

        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("src_distance_matrix", distance_matrix);
        parameters.put("src_touch_matrix", touch_matrix);
        parameters.put("dst_average_distance_list", average_distancelist_destination);

        long[] globalSizes = new long[]{distance_matrix.getWidth()};

        clij2.activateSizeIndependentKernelCompilation();
        clij2.execute(AverageDistanceOfTouchingNeighbors.class, "average_distance_of_touching_neighbors_x.cl", "average_distance_of_touching_neighbors", globalSizes, globalSizes, parameters);

        return true;
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input) {
        return clij.create(new long[]{input.getWidth(), 1, 1}, NativeTypeEnum.Float);
    }

    @Override
    public String getDescription() {
        return "Takes a touch matrix and a distance matrix to determine the average distance of touching neighbors \n " +
                "for every object.\n\n" +
                "Parameters\n" +
                "----------\n" +
                "distance_matrix : Image\n" +
                "    The a distance matrix to be processed.\n" +
                "touch_matrix : Image\n" +
                "    The binary touch matrix describing which distances should be taken into account.\n" +
                "distance_list_destination : Image\n" +
                "    A vector image with the same width as the distance matrix and height=1, depth=1.\n" +
                "    Determined average distances will be written into this vector.\n";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D";
    }
}
