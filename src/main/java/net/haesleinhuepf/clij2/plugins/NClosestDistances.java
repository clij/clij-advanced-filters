package net.haesleinhuepf.clij2.plugins;


import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clij2.utilities.IsCategorized;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_nClosestDistances")
public class NClosestDistances extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, IsCategorized {
    @Override
    public String getCategories() {
        return "Graph, Measurements";
    }

    @Override
    public String getParameterHelpText() {
        return "Image distance_matrix, ByRef Image distances_destination, ByRef Image indexlist_destination, Number nClosestPointsTofind";
    }

    @Override
    public boolean executeCL() {
        boolean result = getCLIJ2().nClosestDistances((ClearCLBuffer) (args[0]), (ClearCLBuffer) (args[1]), (ClearCLBuffer) (args[2]));
        return result;
    }

    public static boolean nClosestDistances(CLIJ2 clij2, ClearCLBuffer distance_matrix, ClearCLBuffer distancelist_destination, ClearCLBuffer indexlist_destination) {
        //ClearCLBuffer temp = clij.create(new long[]{input.getWidth(), 1, input.getHeight()}, input.getNativeType());

        if (distancelist_destination.getHeight() > 1000) {
            System.out.println("Warning: NClosestPoints is limited to n=1000 for technical reasons.");
        }

        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("src_distancematrix", distance_matrix);
        parameters.put("dst_distancelist", distancelist_destination);
        parameters.put("dst_indexlist", indexlist_destination);


        long[] globalSizes = new long[]{distance_matrix.getWidth(), 1, 1};
        clij2.activateSizeIndependentKernelCompilation();
        clij2.execute(NClosestDistances.class, "n_shortest_distances_x.cl", "find_n_closest_points", globalSizes, globalSizes, parameters);

        //temp.close();
        return true;
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input) {
        return clij.create(new long[]{input.getWidth(), asInteger(args[3])}, NativeTypeEnum.Float);
    }

    @Override
    public String getDescription() {
        return "Determine the n point indices with shortest distance for all points in a distance matrix. \n\n" +
                "This corresponds to the n row indices with minimum values for each column of the distance matrix." +
                "Returns the n shortest distances in one image and the point indices in another image.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D";
    }
}
