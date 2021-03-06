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

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_drawMeshBetweenProximalLabels")
public class DrawMeshBetweenProximalLabels extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, IsCategorized, HasClassifiedInputOutput {
    @Override
    public String getInputType() {
        return "Label Image";
    }

    @Override
    public String getOutputType() {
        return "Image";
    }

    @Override
    public String getParameterHelpText() {
        return "Image input, ByRef Image destination, Number maximum_distance";
    }

    @Override
    public boolean executeCL() {
        return drawMeshBetweenProximalLabels(getCLIJ2(), (ClearCLBuffer) args[0], (ClearCLBuffer) args[1], asFloat(args[2]));
    }

    public static boolean drawMeshBetweenProximalLabels(CLIJ2 clij2, ClearCLBuffer pushed, ClearCLBuffer result, Float maximum_distance) {
        int number_of_labels = (int)clij2.maximumOfAllPixels(pushed);
        //System.out.println("Labels count " + number_of_labels);

        ClearCLBuffer pointlist = clij2.create(number_of_labels, pushed.getDimension());
        clij2.centroidsOfLabels(pushed, pointlist);

        ClearCLBuffer distance_matrix = clij2.create(number_of_labels + 1, number_of_labels + 1);
        clij2.generateDistanceMatrix(pointlist, pointlist, distance_matrix);

        clij2.set(result, 0);
        clij2.distanceMatrixToMesh(pointlist, distance_matrix, result, maximum_distance);

        pointlist.close();
        distance_matrix.close();

        return true;
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input)
    {
        return getCLIJ2().create(input.getDimensions(), NativeTypeEnum.Float);
    }

    @Override
    public String getDescription() {
        return "Starting from a label map, draw lines between labels that are closer than a given distance resulting in a mesh.\n\n" +
                "The end points of the lines correspond to the centroids of the labels.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }

    @Override
    public String getCategories() {
        return "Measurement, Graph, Label";
    }
}
