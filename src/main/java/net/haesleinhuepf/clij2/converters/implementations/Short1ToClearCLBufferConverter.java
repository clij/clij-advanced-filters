package net.haesleinhuepf.clij2.converters.implementations;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.converters.AbstractCLIJConverter;
import net.haesleinhuepf.clij.converters.CLIJConverterPlugin;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij2.converters.helptypes.Short1;

import org.scijava.plugin.Plugin;

import java.nio.ShortBuffer;

@Plugin(type = CLIJConverterPlugin.class)
public class Short1ToClearCLBufferConverter extends AbstractCLIJConverter<Short1, ClearCLBuffer> {

    @Override
    public ClearCLBuffer convert(Short1 source) {
        long[] dimensions = new long[]{
                source.data.length,
                1
        };

        ClearCLBuffer target = clij.createCLBuffer(dimensions, NativeTypeEnum.UnsignedShort);
        short[] inputArray = source.data;

        ShortBuffer byteBuffer = ShortBuffer.wrap(inputArray);
        target.readFrom(byteBuffer, true);
        return target;
    }

    @Override
    public Class<Short1> getSourceType() {
        return Short1.class;
    }

    @Override
    public Class<ClearCLBuffer> getTargetType() {
        return ClearCLBuffer.class;
    }
}
