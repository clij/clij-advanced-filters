package net.haesleinhuepf.clij2;

import ij.IJ;
import ij.ImagePlus;
import ij.plugin.Duplicator;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCL;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.clearcl.ClearCLKernel;
import net.haesleinhuepf.clij.clearcl.enums.ImageChannelDataType;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.clearcl.util.ElapsedTime;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.clearcl.util.CLKernelExecutor;
import net.haesleinhuepf.clij2.converters.helptypes.*;
import net.haesleinhuepf.clij2.converters.implementations.*;
import net.haesleinhuepf.clij2.plugins.Clear;
import net.imglib2.Cursor;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.view.Views;

import java.io.File;
import java.io.FilenameFilter;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.Map;
import java.util.Stack;


/**
 * The CLIJ2 gateway
 *
 * Author: @haesleinhuepf
 * December 2019
 */
public class CLIJ2 implements CLIJ2Ops {
    private static CLIJ2 instance;
    private final long max_num_bytes;
    private boolean doTimeTracing = false;

    protected CLIJ clij;

    protected boolean waitForKernelFinish = true;


    public CLIJ getCLIJ() {
        return clij;
    }
    public CLIJ2 getCLIJ2() {
        return this;
    }

    protected final CLKernelExecutor mCLKernelExecutor;

    /**
     * Marking this as deprecated as it will very likely go away before release.
     * Use CLIJx.getInstance() instead.
     * @param clij
     */
    @Deprecated
    public CLIJ2(CLIJ clij) {
        this.clij = clij;
        mCLKernelExecutor = new CLKernelExecutor(clij.getClearCLContext());
        max_num_bytes = clij.getClearCLContext().getDevice().getMaxMemoryAllocationSizeInBytes();
    }

    static {
        checkInstallation();
    }

    private static  void checkInstallation() {
        try {
            String dir = IJ.getDirectory("imagej");
            if (!dir.contains("null") && dir.toLowerCase().contains("fiji")) {
                // we're in a Fiji folder
                File plugins_dir = new File(dir + "/plugins");
                if (jarExists(plugins_dir, "clij2_") && !jarExists(plugins_dir, "clij_")) {
                    System.out.println("CLIJ2 is not installed correctly. Please activate the 'clij' update site");
                }
                if (jarExists(plugins_dir, "clijx-assistant-bonej") && !jarExists(plugins_dir, "bonej-legacy")) {
                    System.out.println("CLIJx extension for BoneJ is not installed correctly. Please activate the 'BoneJ' update site");
                }
                if (jarExists(plugins_dir, "clijx-assistant-morpholibj_") && !jarExists(plugins_dir, "MorphoLibJ_")) {
                    System.out.println("CLIJx extension for MorpholibJ is not installed correctly. Please activate the 'IJPB-Plugins' update site");
                }
                if (jarExists(plugins_dir, "clijx-assistant-imagej3dsuite_") && !jarExists(plugins_dir, "mcib3d-suite")) {
                    System.out.println("CLIJx extension for the ImageJ 3D Suite is not installed correctly. Please activate the '3D ImageJ Suite' update site");
                }
            }
        }catch (Exception e) {
            System.out.println("Error while checking the CLIJ2 installation:");
            System.out.println(e.getMessage());
        }
    }

    private static boolean jarExists(File folder, String name) {
        return folder.list((dir, name1) -> name1.contains(name)).length > 0;
    }

    public static CLIJ2 getInstance() {
        CLIJ clij = CLIJ.getInstance();
        if (instance == null || instance.clij != CLIJ.getInstance()) {
            instance = new CLIJ2(clij);
        }
        return instance;
    }

    public static CLIJ2 getInstance(String id) {
        CLIJ clij = CLIJ.getInstance(id);
        if (instance == null || instance.clij != clij) {
            instance = new CLIJ2(clij);
        }
        return instance;
    }

    public static String clinfo() {
        return CLIJ.clinfo();
    }

    public String getGPUName() {
        return clij.getGPUName();
    }

    public double getOpenCLVersion() {
        return clij.getOpenCLVersion();
    }

    public ClearCLBuffer push(Object object) {
        ClearCLBuffer buffer = clij.convert(object, ClearCLBuffer.class);
        return buffer;
    }

    public ClearCLBuffer pushCurrentZStack(ImagePlus imp) {
        ClearCLBuffer buffer = clij.pushCurrentZStack(imp);
        return buffer;
    }

    public ClearCLBuffer pushCurrentSlice(ImagePlus imp) {
        ClearCLBuffer buffer = clij.pushCurrentSlice(imp);
        return buffer;
    }

    public ClearCLBuffer pushCurrentSelection(ImagePlus imp) {
        imp = new Duplicator().run(imp);
        ClearCLBuffer buffer = clij.pushCurrentSlice(imp);
        return buffer;
    }

    public ImagePlus pull(Object object) {
        return clij.convert(object, ImagePlus.class);
    }

    public RandomAccessibleInterval pullRAI(Object object) {
        return clij.convert(object, RandomAccessibleInterval.class);
    }

    public RandomAccessibleInterval<BitType> pullBinaryRAI(Object object) {
        ClearCLBuffer buffer = convert(object, ClearCLBuffer.class);
        return clij.pullBinaryRAI(buffer);
    }

    public ClearCLBuffer pushMatXYZ(Object object) {
        if (object instanceof ClearCLBuffer) {
            return (ClearCLBuffer) object;
        }

        ClearCLBuffer result = null;
        if (object instanceof double[][][]) {
            Double3 double3 = new Double3((double[][][]) object);
            //System.out.println("d3 size: " + double3.data.length + "/" + double3.data[0].length + "/" + double3.data[0][0].length);
            Double3ToClearCLBufferConverter converter = new Double3ToClearCLBufferConverter();
            converter.setCLIJ(clij);
            result = converter.convert(double3);
        } else if (object instanceof double[][]) {
            Double2 double2 = new Double2((double[][]) object);
            ///System.out.println("d2 size: " + double2.data.length + "/" + double2.data[0].length);
            Double2ToClearCLBufferConverter converter = new Double2ToClearCLBufferConverter();
            converter.setCLIJ(clij);
            result = converter.convert(double2);
        } else if (object instanceof double[]) {
            Double1 double1 = new Double1((double[]) object);
            //System.out.println("d1 size: " + double1.data.length);
            Double1ToClearCLBufferConverter converter = new Double1ToClearCLBufferConverter();
            converter.setCLIJ(clij);
            result = converter.convert(double1);
        } else if (object instanceof float[][][]) {
            Float3 double3 = new Float3((float[][][]) object);
            //System.out.println("d3 size: " + double3.data.length + "/" + double3.data[0].length + "/" + double3.data[0][0].length);
            Float3ToClearCLBufferConverter converter = new Float3ToClearCLBufferConverter();
            converter.setCLIJ(clij);
            result = converter.convert(double3);
        } else if (object instanceof float[][]) {
            Float2 float2 = new Float2((float[][]) object);
            //System.out.println("d2 size: " + float2.data.length + "/" + float2.data[0].length);
            Float2ToClearCLBufferConverter converter = new Float2ToClearCLBufferConverter();
            converter.setCLIJ(clij);
            result = converter.convert(float2);
        } else if (object instanceof float[]) {
            Float1 float1 = new Float1((float[]) object);
            //System.out.println("d1 size: " + float1.data.length);
            Float1ToClearCLBufferConverter converter = new Float1ToClearCLBufferConverter();
            converter.setCLIJ(clij);
            result = converter.convert(float1);
        } else if (object instanceof char[][][]) {
            Char3 char3 = new Char3((char[][][]) object);
            //System.out.println("b3 size: " + byte3.data.length + "/" + byte3.data[0].length + "/" + byte3.data[0][0].length);
            Char3ToClearCLBufferConverter converter = new Char3ToClearCLBufferConverter();
            converter.setCLIJ(clij);
            result = converter.convert(char3);
        } else if (object instanceof char[][]) {
            Char2 char2 = new Char2((char[][]) object);
            //System.out.println("b2 size: " + byte2.data.length + "/" + byte2.data[0].length);
            Char2ToClearCLBufferConverter converter = new Char2ToClearCLBufferConverter();
            converter.setCLIJ(clij);
            result = converter.convert(char2);
        } else if (object instanceof char[]) {
            Char1 char1 = new Char1((char[]) object);
            //System.out.println("b1 size: " + byte1.data.length);
            Char1ToClearCLBufferConverter converter = new Char1ToClearCLBufferConverter();
            converter.setCLIJ(clij);
            result = converter.convert(char1);

        } else if (object instanceof byte[][][]) {
            Byte3 byte3 = new Byte3((byte[][][]) object);
            //System.out.println("b3 size: " + byte3.data.length + "/" + byte3.data[0].length + "/" + byte3.data[0][0].length);
            Byte3ToClearCLBufferConverter converter = new Byte3ToClearCLBufferConverter();
            converter.setCLIJ(clij);
            result = converter.convert(byte3);
        } else if (object instanceof byte[][]) {
            Byte2 byte2 = new Byte2((byte[][]) object);
            //System.out.println("b2 size: " + byte2.data.length + "/" + byte2.data[0].length);
            Byte2ToClearCLBufferConverter converter = new Byte2ToClearCLBufferConverter();
            converter.setCLIJ(clij);
            result = converter.convert(byte2);
        } else if (object instanceof byte[]) {
            Byte1 byte1 = new Byte1((byte[]) object);
            //System.out.println("b1 size: " + byte1.data.length);
            Byte1ToClearCLBufferConverter converter = new Byte1ToClearCLBufferConverter();
            converter.setCLIJ(clij);
            result = converter.convert(byte1);

        } else if (object instanceof int[][][]) {
            Integer3 int3 = new Integer3((int[][][]) object);
            //System.out.println("i3 size: " + int3.data.length + "/" + int3.data[0].length + "/" + int3.data[0][0].length);
            Integer3ToClearCLBufferConverter converter = new Integer3ToClearCLBufferConverter();
            converter.setCLIJ(clij);
            result = converter.convert(int3);
        } else if (object instanceof int[][]) {
            Integer2 int2 = new Integer2((int[][]) object);
            //System.out.println("i2 size: " + int2.data.length + "/" + int2.data[0].length);
            Integer2ToClearCLBufferConverter converter = new Integer2ToClearCLBufferConverter();
            converter.setCLIJ(clij);
            result = converter.convert(int2);
        } else if (object instanceof int[]) {
            Integer1 int1 = new Integer1((int[]) object);
            //System.out.println("i1 size: " + int1.data.length);
            Integer1ToClearCLBufferConverter converter = new Integer1ToClearCLBufferConverter();
            converter.setCLIJ(clij);
            result = converter.convert(int1);

        } else if (object instanceof short[][][]) {
            Short3 short3 = new Short3((short[][][]) object);
            //System.out.println("s3 size: " + short3.data.length + "/" + short3.data[0].length + "/" + short3.data[0][0].length);
            Short3ToClearCLBufferConverter converter = new Short3ToClearCLBufferConverter();
            converter.setCLIJ(clij);
            result = converter.convert(short3);
        } else if (object instanceof short[][]) {
            Short2 short2 = new Short2((short[][]) object);
            //System.out.println("s2 size: " + short2.data.length + "/" + short2.data[0].length);
            Short2ToClearCLBufferConverter converter = new Short2ToClearCLBufferConverter();
            converter.setCLIJ(clij);
            result = converter.convert(short2);
        } else if (object instanceof short[]) {
            Short1 short1 = new Short1((short[]) object);
            //System.out.println("s1 size: " + short1.data.length);
            Short1ToClearCLBufferConverter converter = new Short1ToClearCLBufferConverter();
            converter.setCLIJ(clij);
            result = converter.convert(short1);

        } else {
            throw new IllegalArgumentException("Conversion of " + object +
                    " / " + object.getClass().getName() + " not supported");
        }
        return result;
    }

    public ClearCLBuffer pushMat(Object object) {
        ClearCLBuffer result = pushMatXYZ(object);
        if (result.getDimension() != 3) {
            ClearCLBuffer transposed = create(new long[]{result.getHeight(), result.getWidth()}, result.getNativeType());
            getCLIJ2().transposeXY(result, transposed);
            result.close();
            return transposed;
        } else {
            ClearCLBuffer transposed = create(new long[]{result.getDepth(), result.getHeight(), result.getWidth()}, result.getNativeType());
            getCLIJ2().transposeXZ(result, transposed);
            result.close();
            return transposed;
        }
    }

    public Object pullMat(ClearCLBuffer input) {
        ClearCLBuffer buffer = input;
        if (buffer.getDimension() == 1 || (buffer.getHeight() == 1 && buffer.getDepth() == 1)) {
            buffer = create(new long[]{input.getHeight(), input.getWidth()}, input.getNativeType());
            transposeXY(input, buffer);
        } else if (buffer.getDimension() == 2 || (buffer.getDepth() == 1)) {
            buffer = create(new long[]{input.getHeight(), input.getWidth()}, input.getNativeType());
            transposeXY(input, buffer);
        } else if (buffer.getDimension() == 3) {
            buffer = create(new long[]{input.getDepth(), input.getHeight(), input.getWidth()}, input.getNativeType());
            transposeXZ(input, buffer);
        }
        Object result = pullMatXYZ(buffer);
        buffer.close();
        return result;
    }

    public Object pullMatXYZ(ClearCLBuffer input) {
        ClearCLBuffer buffer = input;

        Object result = null;
        if (input.getNativeType() == NativeTypeEnum.Double) {
            if (buffer.getDimension() == 1 || (buffer.getHeight() == 1 && buffer.getDepth() == 1)) {
                result = new ClearCLBufferToDouble2Converter().convert(buffer).data[1];
            } else if (buffer.getDimension() == 2 || (buffer.getDepth() == 1)) {
                result = new ClearCLBufferToDouble2Converter().convert(buffer).data;
            } else if (buffer.getDimension() == 3) {
                result = new ClearCLBufferToDouble3Converter().convert(buffer).data;
            } else {
                throw new IllegalArgumentException("Conversion of " + buffer +
                        " / " + buffer.getClass().getName() + " not supported");
            }
        } else if (input.getNativeType() == Float) {
                if (buffer.getDimension() == 1 || (buffer.getHeight() == 1 && buffer.getDepth() == 1)) {
                    result = new ClearCLBufferToFloat2Converter().convert(buffer).data[1];
                } else if (buffer.getDimension() == 2 || (buffer.getDepth() == 1)) {
                    result = new ClearCLBufferToFloat2Converter().convert(buffer).data;
                } else if (buffer.getDimension() == 3) {
                    result = new ClearCLBufferToFloat3Converter().convert(buffer).data;
                } else {
                    throw new IllegalArgumentException("Conversion of " + buffer +
                            " / " + buffer.getClass().getName() + " not supported");
                }

        /*
        } else if (input.getNativeType() == UnsignedShort) {
            if (buffer.getDimension() == 1 || (buffer.getHeight() == 1 && buffer.getDepth() == 1)) {
                result = new ClearCLBufferToChar2Converter().convert(buffer).data[1];
            } else if (buffer.getDimension() == 2 || (buffer.getDepth() == 1)) {
                result = new ClearCLBufferToChar2Converter().convert(buffer).data;
            } else if (buffer.getDimension() == 3) {
                result = new ClearCLBufferToChar3Converter().convert(buffer).data;
            } else {
                throw new IllegalArgumentException("Conversion of " + buffer +
                        " / " + buffer.getClass().getName() + " not supported");
            }
        */

        } else if (input.getNativeType() == UnsignedShort) {
            if (buffer.getDimension() == 1 || (buffer.getHeight() == 1 && buffer.getDepth() == 1)) {
                result = new ClearCLBufferToShort2Converter().convert(buffer).data[1];
            } else if (buffer.getDimension() == 2 || (buffer.getDepth() == 1)) {
                result = new ClearCLBufferToShort2Converter().convert(buffer).data;
            } else if (buffer.getDimension() == 3) {
                result = new ClearCLBufferToShort3Converter().convert(buffer).data;
            } else {
                throw new IllegalArgumentException("Conversion of " + buffer +
                        " / " + buffer.getClass().getName() + " not supported");
            }
            
        } else if (input.getNativeType() == NativeTypeEnum.UnsignedInt) {
            if (buffer.getDimension() == 1 || (buffer.getHeight() == 1 && buffer.getDepth() == 1)) {
                result = new ClearCLBufferToInteger2Converter().convert(buffer).data[1];
            } else if (buffer.getDimension() == 2 || (buffer.getDepth() == 1)) {
                result = new ClearCLBufferToInteger2Converter().convert(buffer).data;
            } else if (buffer.getDimension() == 3) {
                result = new ClearCLBufferToInteger3Converter().convert(buffer).data;
            } else {
                throw new IllegalArgumentException("Conversion of " + buffer +
                        " / " + buffer.getClass().getName() + " not supported");
            }

        } else if (input.getNativeType() == UnsignedByte) {
            if (buffer.getDimension() == 1 || (buffer.getHeight() == 1 && buffer.getDepth() == 1)) {
                result = new ClearCLBufferToByte2Converter().convert(buffer).data[1];
            } else if (buffer.getDimension() == 2 || (buffer.getDepth() == 1)) {
                result = new ClearCLBufferToByte2Converter().convert(buffer).data;
            } else if (buffer.getDimension() == 3) {
                result = new ClearCLBufferToByte3Converter().convert(buffer).data;
            } else {
                throw new IllegalArgumentException("Conversion of " + buffer +
                        " / " + buffer.getClass().getName() + " not supported");
            }
        }
        return result;
    }

    public void pullToRAI(Object object, RandomAccessibleInterval target) {
        RandomAccessibleInterval rai = pullRAI(object);

        Cursor<RealType> cursor = Views.iterable(rai).cursor();
        Cursor<RealType> target_cursor = Views.iterable(target).cursor();

        while(cursor.hasNext() && target_cursor.hasNext()) {
            target_cursor.next().set(cursor.next());
        }
    }

    public void show(Object object, String title) {
        ImagePlus imp = clij.convert(object, ImagePlus.class);
        clij.show(imp, title);
    }

    public <T> T convert(Object object, Class<T> klass) {
        return clij.convert(object, klass);
    }

    public ClearCLBuffer create(ClearCLBuffer buffer) {
        ClearCLBuffer result = create(buffer.getDimensions(),  buffer.getNativeType());
        return result;
    }

    public ClearCLImage create(ClearCLImage image) {
        ClearCLImage result = clij.create(image.getDimensions(), image.getChannelDataType());
        return result;
    }

    public ClearCLBuffer create_like(ClearCLBuffer buffer) {
        ClearCLBuffer result = create(buffer.getDimensions(),  buffer.getNativeType());
        return result;
    }

    public ClearCLImage create_like(ClearCLImage image) {
        ClearCLImage result = clij.create(image.getDimensions(), image.getChannelDataType());
        return result;
    }

    public ClearCLBuffer create(long dimensionX, long dimensionY, long dimensionZ) {
        return create(new long[]{dimensionX, dimensionY, dimensionZ}, NativeTypeEnum.Float);
    }

    public ClearCLBuffer create(long dimensionX, long dimensionY) {
        return create(new long[]{dimensionX, dimensionY}, NativeTypeEnum.Float);
    }

    public ClearCLBuffer create(long[] dimensions) {
        ClearCLBuffer buffer = create(dimensions, NativeTypeEnum.Float);
        return buffer;
    }

    public ClearCLBuffer create(double[] dblDimensions) {
        long[] dimensions = new long[dblDimensions.length];
        for (int i = 0; i < dimensions.length; i++) {
            dimensions[i] = (long)dblDimensions[i];
        }
        return create(dimensions, NativeTypeEnum.Float);
    }


    public ClearCLBuffer create(long[] dimensions, NativeTypeEnum typeEnum) {
        checkMaxImageSize(dimensions, typeEnum.getSizeInBytes());

        try {
            return clij.create(dimensions, typeEnum);
        } catch (Exception e) {
            System.out.println(clij.humanReadableErrorMessage(e.getMessage()));
            throw (e);
        }
    }

    public ClearCLImage create(long[] dimensions, ImageChannelDataType typeEnum) {
        checkMaxImageSize(dimensions, typeEnum.getNativeType().getSizeInBytes());

        try {
            return clij.create(dimensions, typeEnum);
        } catch (Exception e) {
            System.out.println(clij.humanReadableErrorMessage(e.getMessage()));
            throw(e);
        }
    }

    private void checkMaxImageSize(long[] dimensions, long bytes_per_pixel) {
        long num_pixels = 1;
        for (long dim : dimensions) {
            num_pixels = num_pixels * dim;
        }
        long num_bytes = num_pixels * bytes_per_pixel;
        if (num_bytes > max_num_bytes) {
            warn("CLIJ2 Warning: You're creating an image with size " + humanReadableBytes(num_bytes) + ", which exceeds your GPUs capabilities (max " + humanReadableBytes(max_num_bytes) + ").");
        }
    }

    private String humanReadableBytes(double num_bytes) {
        if (num_bytes > 1024) {
            num_bytes = num_bytes / 1024;
            if (num_bytes > 1024) {
                num_bytes = num_bytes / 1024;
                if (num_bytes > 1024) {
                    num_bytes = num_bytes / 1024;
                    if (num_bytes > 1024) {
                        num_bytes = num_bytes / 1024;
                        return "" + ((double)((long)(num_bytes * 10))/10) + " terabytes";
                    } else {
                        return "" + ((double)((long)(num_bytes * 10))/10) + " gigabytes";
                    }
                } else {
                    return "" + ((double)((long)(num_bytes * 10))/10) + " megabytes";
                }
            } else {
                return "" + ((double)((long)(num_bytes * 10))/10) + " kilobytes";
            }
        } else {
            return "" + ((double)((long)(num_bytes * 10))/10) + " bytes";
        }
    }

    public void warn(String text) {
        System.out.println(text);
    }

    public void execute(String programFilename, String kernelname, long[] dimensions, long[] globalsizes, Map<String, Object> parameters, Map<String, Object> constants) {
        ClearCLKernel kernel = executeSubsequently(null, programFilename, kernelname,  dimensions, globalsizes, parameters, constants, null);
        kernel.close();
    }

    public void execute(Class anchorClass, String programFilename, String kernelname, long[] dimensions, long[] globalsizes, Map<String, Object> parameters, Map<String, Object> constants) {
        ClearCLKernel kernel = executeSubsequently(anchorClass, programFilename, kernelname,  dimensions, globalsizes, parameters, constants, null);
        kernel.close();
    }

    public void execute(Class anchorClass, String programFilename, String kernelname, long[] dimensions, long[] globalsizes, Map<String, Object> parameters) {
        ClearCLKernel kernel = executeSubsequently(anchorClass, programFilename, kernelname,  dimensions, globalsizes, parameters, null);
        kernel.close();
    }

    public void execute(Class anchorClass, String programFilename, String kernelname, long[] dimensions, long[] globalsizes, long[] localSizes, Map<String, Object> parameters) {
        ClearCLKernel kernel = executeSubsequently(anchorClass, programFilename, kernelname,  dimensions, globalsizes, localSizes, parameters, null,null);
        kernel.close();
    }

    public void execute(Class anchorClass, String programFilename, String kernelname, long[] dimensions, long[] globalsizes, long[] localSizes, Map<String, Object> parameters, Map<String, Object> constants) {
        ClearCLKernel kernel = executeSubsequently(anchorClass, programFilename, kernelname,  dimensions, globalsizes, localSizes, parameters, constants,null);
        kernel.close();
    }

    public ClearCLKernel executeSubsequently(Class anchorClass, String pProgramFilename, String pKernelname, long[] dimensions, long[] globalsizes, Map<String, Object> parameters, ClearCLKernel kernel) {
        return executeSubsequently(anchorClass, pProgramFilename, pKernelname, dimensions, globalsizes, parameters, null, kernel);
    }


    public ClearCLKernel executeSubsequently(Class anchorClass, String pProgramFilename, String pKernelname, long[] dimensions, long[] globalsizes, Map<String, Object> parameters, Map<String, Object> constants, ClearCLKernel kernel) {
        return executeSubsequently(anchorClass, pProgramFilename, pKernelname, dimensions, globalsizes, null, parameters, constants, kernel);
    }

    public synchronized ClearCLKernel executeSubsequently(Class anchorClass, String pProgramFilename, String pKernelname, long[] dimensions, long[] globalsizes, long[] localSizes, Map<String, Object> parameters, Map<String, Object> constants, ClearCLKernel kernel) {

        final ClearCLKernel[] result = {kernel};

        if (CLIJ.debug) {
            for (String key : parameters.keySet()) {
                System.out.println(key + " = " + parameters.get(key));
            }
        }

        ElapsedTime.measure("kernel + build " + pKernelname, () -> {
            mCLKernelExecutor.setProgramFilename(pProgramFilename);
            mCLKernelExecutor.setKernelName(pKernelname);
            mCLKernelExecutor.setAnchorClass(anchorClass);
            mCLKernelExecutor.setParameterMap(parameters);
            mCLKernelExecutor.setConstantsMap(constants);
            mCLKernelExecutor.setGlobalSizes(globalsizes);
            mCLKernelExecutor.setLocalSizes(localSizes);

            try {
                result[0] = mCLKernelExecutor.enqueue(waitForKernelFinish, kernel);
            } catch (Exception e) {
                System.out.println(clij.humanReadableErrorMessage(e.getMessage()));
                throw(e);
            }

            mCLKernelExecutor.setImageSizeIndependentCompilation(false);
        });

        return result[0];
    }



    public void executeCode(String sourceCode, String kernelname, long[] dimensions, long[] globalsizes, Map<String, Object> parameters, Map<String, Object> constants) {
        ClearCLKernel kernel = executeCodeSubsequently(sourceCode, kernelname,  dimensions, globalsizes, parameters, constants, null);
        kernel.close();
    }

    public void executeCode(String sourceCode, String kernelname, long[] dimensions, long[] globalsizes, Map<String, Object> parameters) {
        ClearCLKernel kernel = executeCodeSubsequently(sourceCode, kernelname,  dimensions, globalsizes, parameters, null);
        kernel.close();
    }

    public void executeCode(String sourceCode, String kernelname, long[] dimensions, long[] globalsizes, long[] localSizes, Map<String, Object> parameters) {
        ClearCLKernel kernel = executeCodeSubsequently(sourceCode, kernelname,  dimensions, globalsizes, localSizes, parameters, null,null);
        kernel.close();
    }

    public ClearCLKernel executeCodeSubsequently(String sourceCode, String pKernelname, long[] dimensions, long[] globalsizes, Map<String, Object> parameters, ClearCLKernel kernel) {
        return executeCodeSubsequently(sourceCode, pKernelname, dimensions, globalsizes, parameters, null, kernel);
    }


    public ClearCLKernel executeCodeSubsequently(String sourceCode, String pKernelname, long[] dimensions, long[] globalsizes, Map<String, Object> parameters, Map<String, Object> constants, ClearCLKernel kernel) {
        return executeCodeSubsequently(sourceCode, pKernelname, dimensions, globalsizes, null, parameters, constants, kernel);
    }


    public ClearCLKernel executeCodeSubsequently(String sourceCode, String pKernelname, long[] dimensions, long[] globalsizes, long[] localSizes, Map<String, Object> parameters, Map<String, Object> constants, ClearCLKernel kernel) {

        final ClearCLKernel[] result = {kernel};

        if (CLIJ.debug) {
            for (String key : parameters.keySet()) {
                System.out.println(key + " = " + parameters.get(key));
            }
        }

        ElapsedTime.measure("kernel + build " + pKernelname, () -> {
            mCLKernelExecutor.setProgramSourceCode(sourceCode);
            mCLKernelExecutor.setKernelName(pKernelname);
            mCLKernelExecutor.setAnchorClass(Object.class);
            mCLKernelExecutor.setParameterMap(parameters);
            mCLKernelExecutor.setConstantsMap(constants);
            mCLKernelExecutor.setGlobalSizes(globalsizes);
            mCLKernelExecutor.setLocalSizes(localSizes);

//            result[0] = mCLKernelExecutor.enqueue(waitForKernelFinish, kernel);
            try {
                result[0] = mCLKernelExecutor.enqueue(waitForKernelFinish, kernel);
            } catch (Exception e) {
                System.out.println(clij.humanReadableErrorMessage(e.getMessage()));
                throw(e);
            }

            mCLKernelExecutor.setImageSizeIndependentCompilation(false);
        });

        return result[0];
    }

    public boolean isSizeIndependentKernelCompilation() {
        return mCLKernelExecutor.isImageSizeIndependentCompilation();
    }

    public void activateSizeIndependentKernelCompilation() {
        mCLKernelExecutor.setImageSizeIndependentCompilation(true);
    }
    public void setWaitForKernelFinish(boolean waitForKernelFinish) {
        this.waitForKernelFinish = waitForKernelFinish;
    }


    @Deprecated
    public CLIJ getClij() {
        return clij;
    }

    /**
     * This method is for debugging purposes only
     * @param keepReferences
     */
    @Deprecated
    public void setKeepReferences(boolean keepReferences) {
        System.out.println("CLIJ2.setKeepReferences is obsolete.");
    }

    public void release(ClearCLImageInterface image) {
        if (image != null) {
            image.close();
        }
    }

    public void clear() {
        getCLIJ().getClearCLContext().releaseImages();
    }

    public String reportMemory() {
        return getCLIJ().getClearCLContext().reportAboutAllocatedImages();
    }


    public void close() {
        clear();

        if (this == instance) {
            instance = null;
        }

        clij.close();
    }

    public final NativeTypeEnum Float = NativeTypeEnum.Float;
    public final NativeTypeEnum UnsignedShort = NativeTypeEnum.UnsignedShort;
    public final NativeTypeEnum UnsignedByte = NativeTypeEnum.UnsignedByte;

    public CLIJ2 __enter__() {
        clear();
        return this;
    }

    public void __exit__(Object... args) {
        clear();
    }

    public boolean hasImageSupport() {
        return clij.hasImageSupport();
    }

    public ImagePlus pullBinary(ClearCLBuffer input) {
        return clij.pullBinary(input);
    }

    public void invalidateKernelCahe() {
        mCLKernelExecutor.close();
    }

    /**
     * Transfer a buffer from a different OpenCLDevice
     * @param input
     * @return
     */
    public ClearCLBuffer transfer(ClearCLBuffer input) {
        ClearCLBuffer output = create(input);
        transferTo(input, output);
        return output;
    }

    /**
     * Transfer a buffer between different OpenCLDevices
     * @param input
     * @param output
     * @return
     */
    public void transferTo(ClearCLBuffer input, ClearCLBuffer output) {
        //System.out.println("Transfer from: " + input);
        //System.out.println("Transfer to: " + output);
        ByteBuffer buffer = ByteBuffer.allocate((int) input.getSizeInBytes());
        input.writeTo(buffer, true);
        output.readFrom(buffer, true);

    }

    public boolean doTimeTracing() {
        return doTimeTracing;
    }

    public void setDoTimeTracing(boolean doTimeTracing) {
        this.doTimeTracing = doTimeTracing;
        if (doTimeTracing) {
            resetTimeTraces();
            recordMethodStart("timeTracing");
        } else {
            recordMethodEnd("timeTracing");
        }
    }

    public String getTimeTraces() {
        return timeTraces.toString();
    }

    Stack<Long> times;
    public void recordMethodStart(String method) {
        for (int i = 0; i < times.size(); i++) {
            timeTraces.append(" ");
        }
        timeTraces.append("> " + method + "\n");
        times.push(System.nanoTime());

    }

    StringBuilder timeTraces = new StringBuilder();
    public void recordMethodEnd(String method) {
        double duration = (double)(System.nanoTime() - times.pop()) / 1000000;
        int charCount = 0;
        for (int i = 0; i < times.size(); i++) {
            timeTraces.append(" ");
            charCount++;
        }
        timeTraces.append("< " + method);
        charCount += method.length();
        for (int i = charCount; i < 30; i++) {
            timeTraces.append(" ");
        }
        timeTraces.append("" + duration + " ms");
        timeTraces.append("\n");
    }

    public void resetTimeTraces() {
        timeTraces = new StringBuilder();
        times = new Stack<Long>();
    }


}
