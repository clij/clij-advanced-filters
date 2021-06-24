package net.haesleinhuepf.clij2.converters.implementations;

import net.haesleinhuepf.clij2.converters.helptypes.Integer2;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.converters.AbstractCLIJConverter;
import net.haesleinhuepf.clij.converters.CLIJConverterPlugin;
import org.scijava.plugin.Plugin;

import java.nio.IntBuffer;

/**
 * Todo: Shall we deal with float images differently than with double images?
 */
@Plugin(type = CLIJConverterPlugin.class)
public class ClearCLBufferToInteger2Converter extends AbstractCLIJConverter<ClearCLBuffer, Integer2> {

    @Override
    public Integer2 convert(ClearCLBuffer source) {
        Integer2 target = new Integer2(new int[(int)source.getWidth()][(int)source.getHeight()]);
        int[] array = new int[(int)(source.getWidth() * source.getHeight())];

        IntBuffer buffer = IntBuffer.wrap(array);
        source.writeTo(buffer, true);

        int count = 0;
        for (int y = 0; y < target.data[0].length; y++) {
            for (int x = 0; x < target.data.length; x++) {
                target.data[x][y] = array[count];
                count++;
            }
        }
        return target;
    }

    @Override
    public Class<Integer2> getTargetType() {
        return Integer2.class;
    }

    @Override
    public Class<ClearCLBuffer> getSourceType() {
        return ClearCLBuffer.class;
    }
}
