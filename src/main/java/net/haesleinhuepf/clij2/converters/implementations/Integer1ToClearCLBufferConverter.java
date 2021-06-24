package net.haesleinhuepf.clij2.converters.implementations;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.converters.AbstractCLIJConverter;
import net.haesleinhuepf.clij.converters.CLIJConverterPlugin;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij2.converters.helptypes.Integer1;

import org.scijava.plugin.Plugin;

import java.nio.IntBuffer;

@Plugin(type = CLIJConverterPlugin.class)
public class Integer1ToClearCLBufferConverter extends AbstractCLIJConverter<Integer1, ClearCLBuffer> {

    @Override
    public ClearCLBuffer convert(Integer1 source) {
        long[] dimensions = new long[]{
                source.data.length,
                1
        };

        ClearCLBuffer target = clij.createCLBuffer(dimensions, NativeTypeEnum.UnsignedInt);
        int[] inputArray = source.data;

        IntBuffer byteBuffer = IntBuffer.wrap(inputArray);
        target.readFrom(byteBuffer, true);
        return target;
    }

    @Override
    public Class<Integer1> getSourceType() {
        return Integer1.class;
    }

    @Override
    public Class<ClearCLBuffer> getTargetType() {
        return ClearCLBuffer.class;
    }
}
