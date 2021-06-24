package net.haesleinhuepf.clij2.converters.implementations;

import net.haesleinhuepf.clij2.converters.helptypes.Short2;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.converters.AbstractCLIJConverter;
import net.haesleinhuepf.clij.converters.CLIJConverterPlugin;
import org.scijava.plugin.Plugin;

import java.nio.ShortBuffer;

/**
 * Todo: Shall we deal with float images differently than with double images?
 */
@Plugin(type = CLIJConverterPlugin.class)
public class ClearCLBufferToShort2Converter extends AbstractCLIJConverter<ClearCLBuffer, Short2> {

    @Override
    public Short2 convert(ClearCLBuffer source) {
        Short2 target = new Short2(new short[(int)source.getWidth()][(int)source.getHeight()]);
        short[] array = new short[(int)(source.getWidth() * source.getHeight())];

        ShortBuffer buffer = ShortBuffer.wrap(array);
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
    public Class<Short2> getTargetType() {
        return Short2.class;
    }

    @Override
    public Class<ClearCLBuffer> getSourceType() {
        return ClearCLBuffer.class;
    }
}
