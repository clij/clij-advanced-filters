package net.haesleinhuepf.clij2.converters.implementations;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.converters.AbstractCLIJConverter;
import net.haesleinhuepf.clij.converters.CLIJConverterPlugin;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij2.converters.helptypes.Short2;
import org.scijava.plugin.Plugin;

import java.nio.ShortBuffer;

@Plugin(type = CLIJConverterPlugin.class)
public class Short2ToClearCLBufferConverter extends AbstractCLIJConverter<Short2, ClearCLBuffer> {

    @Override
    public ClearCLBuffer convert(Short2 source) {
        long[] dimensions = new long[]{
                source.data.length,
                source.data[0].length
        };

        int numberOfPixelsPerSlice = (int)(dimensions[0] * dimensions[1]);
        long numberOfPixels = numberOfPixelsPerSlice;


        ClearCLBuffer target = clij.createCLBuffer(dimensions, NativeTypeEnum.UnsignedShort);
        short[] inputArray = new short[(int)numberOfPixels];

        int count = 0;
        for (int y = 0; y < dimensions[1]; y++) {
            for (int x = 0; x < dimensions[0]; x++) {
                inputArray[count] = (short)source.data[x][y];
                count++;
            }
        }
        ShortBuffer byteBuffer = ShortBuffer.wrap(inputArray);
        target.readFrom(byteBuffer, true);
        return target;
    }

    @Override
    public Class<Short2> getSourceType() {
        return Short2.class;
    }

    @Override
    public Class<ClearCLBuffer> getTargetType() {
        return ClearCLBuffer.class;
    }
}
