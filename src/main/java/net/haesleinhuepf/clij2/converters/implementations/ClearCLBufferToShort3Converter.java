package net.haesleinhuepf.clij2.converters.implementations;

import net.haesleinhuepf.clij2.converters.helptypes.Short3;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.converters.AbstractCLIJConverter;
import net.haesleinhuepf.clij.converters.CLIJConverterPlugin;
import org.scijava.plugin.Plugin;

import java.nio.ShortBuffer;

@Plugin(type = CLIJConverterPlugin.class)
public class ClearCLBufferToShort3Converter extends AbstractCLIJConverter<ClearCLBuffer, Short3> {

    @Override
    public Short3 convert(ClearCLBuffer source) {
        Short3 target = new Short3(new short[(int)source.getWidth()][(int)source.getHeight()][(int)source.getDepth()]);
        short[] array = new short[(int)(source.getWidth() * source.getHeight() * source.getDepth())];

        ShortBuffer buffer = ShortBuffer.wrap(array);
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
    public Class<Short3> getTargetType() {
        return Short3.class;
    }

    @Override
    public Class<ClearCLBuffer> getSourceType() {
        return ClearCLBuffer.class;
    }
}
