package net.haesleinhuepf.clij2.converters.implementations;

import net.haesleinhuepf.clij2.converters.helptypes.Integer3;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.converters.AbstractCLIJConverter;
import net.haesleinhuepf.clij.converters.CLIJConverterPlugin;
import org.scijava.plugin.Plugin;

import java.nio.IntBuffer;

@Plugin(type = CLIJConverterPlugin.class)
public class ClearCLBufferToInteger3Converter extends AbstractCLIJConverter<ClearCLBuffer, Integer3> {

    @Override
    public Integer3 convert(ClearCLBuffer source) {
        Integer3 target = new Integer3(new int[(int)source.getWidth()][(int)source.getHeight()][(int)source.getDepth()]);
        int[] array = new int[(int)(source.getWidth() * source.getHeight() * source.getDepth())];

        IntBuffer buffer = IntBuffer.wrap(array);
        source.writeTo(buffer, true);

        int count = 0;
        for (int z = 0; z < target.data[0][0].length; z++) {
            for (int y = 0; y < target.data[0].length; y++) {
                for (int x = 0; x < target.data.length; x++) {
                    target.data[x][y][z] = array[count];
                    count++;
                }
            }
        }
        return target;
    }

    @Override
    public Class<Integer3> getTargetType() {
        return Integer3.class;
    }

    @Override
    public Class<ClearCLBuffer> getSourceType() {
        return ClearCLBuffer.class;
    }
}
