package net.haesleinhuepf.clij.advancedmath;

import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

/**
 * 	Author: @haesleinhuepf
 * 	        August 2019
 */

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ_greaterOrEqualConstant")
public class GreaterOrEqualConstant extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        boolean result = greaterOrEqualConstant(clij, (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), asFloat(args[2]));
        return result;
    }

    public static boolean greaterOrEqualConstant(CLIJ clij, ClearCLBuffer src1, ClearCLBuffer dst, Float constant) {

        HashMap<String, Object> parameters = new HashMap<>();
        
        parameters.clear();
        parameters.put("src1", src1);
        parameters.put("constant", constant);
        parameters.put("dst", dst);

        return clij.execute(GreaterOrEqualConstant.class, "comparison_constants.cl", "greater_or_equal_" + src1.getDimension() + "d", parameters);
    }
        
    
    
    @Override
    public String getParameterHelpText() {
        return "Image source, Image destination, Number constant";
    }

    @Override
    public String getDescription() {
        return "Determines if two images A and B greater or equal pixel wise.\n\nf(a, b) = 1 if a >= b; 0 otherwise. ";
    }
    
    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
