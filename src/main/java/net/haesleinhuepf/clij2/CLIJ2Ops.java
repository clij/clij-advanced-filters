package net.haesleinhuepf.clij2;
import net.haesleinhuepf.clijx.CLIJx;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLKernel;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import ij.measure.ResultsTable;
import ij.gui.Roi;
import ij.plugin.frame.RoiManager;
import net.haesleinhuepf.clij2.plugins.BinaryUnion;
import net.haesleinhuepf.clij2.plugins.BinaryIntersection;
import net.haesleinhuepf.clij2.plugins.ConnectedComponentsLabeling;
import net.haesleinhuepf.clij2.plugins.CountNonZeroPixels;
import net.haesleinhuepf.clij2.plugins.DifferenceOfGaussian2D;
import net.haesleinhuepf.clij2.plugins.DifferenceOfGaussian3D;
import net.haesleinhuepf.clij2.plugins.MaskLabel;
import net.haesleinhuepf.clij2.plugins.MeanClosestSpotDistance;
import net.haesleinhuepf.clij2.plugins.MeanSquaredError;
import net.haesleinhuepf.clij2.plugins.MedianZProjection;
import net.haesleinhuepf.clij2.plugins.NonzeroMinimumDiamond;
import net.haesleinhuepf.clij2.plugins.Paste2D;
import net.haesleinhuepf.clij2.plugins.Paste3D;
import net.haesleinhuepf.clij2.plugins.JaccardIndex;
import net.haesleinhuepf.clij2.plugins.SorensenDiceCoefficient;
import net.haesleinhuepf.clij2.plugins.StandardDeviationZProjection;
import net.haesleinhuepf.clij2.plugins.TopHatBox;
import net.haesleinhuepf.clij2.plugins.TopHatSphere;
import net.haesleinhuepf.clij2.plugins.Exponential;
import net.haesleinhuepf.clij2.plugins.Logarithm;
import net.haesleinhuepf.clij2.plugins.GenerateDistanceMatrix;
import net.haesleinhuepf.clij2.plugins.ShortestDistances;
import net.haesleinhuepf.clij2.plugins.SpotsToPointList;
import net.haesleinhuepf.clij2.plugins.TransposeXY;
import net.haesleinhuepf.clij2.plugins.TransposeXZ;
import net.haesleinhuepf.clij2.plugins.TransposeYZ;
import net.haesleinhuepf.clij2.plugins.SetWhereXequalsY;
import net.haesleinhuepf.clij2.plugins.LaplaceDiamond;
import net.haesleinhuepf.clij2.plugins.Image2DToResultsTable;
import net.haesleinhuepf.clij2.plugins.WriteValuesToPositions;
import net.haesleinhuepf.clij2.plugins.GetSize;
import net.haesleinhuepf.clij2.plugins.MultiplyMatrix;
import net.haesleinhuepf.clij2.plugins.Equal;
import net.haesleinhuepf.clij2.plugins.GreaterOrEqual;
import net.haesleinhuepf.clij2.plugins.Greater;
import net.haesleinhuepf.clij2.plugins.Smaller;
import net.haesleinhuepf.clij2.plugins.SmallerOrEqual;
import net.haesleinhuepf.clij2.plugins.NotEqual;
import net.haesleinhuepf.clij2.plugins.EqualConstant;
import net.haesleinhuepf.clij2.plugins.GreaterOrEqualConstant;
import net.haesleinhuepf.clij2.plugins.GreaterConstant;
import net.haesleinhuepf.clij2.plugins.SmallerConstant;
import net.haesleinhuepf.clij2.plugins.SmallerOrEqualConstant;
import net.haesleinhuepf.clij2.plugins.NotEqualConstant;
import net.haesleinhuepf.clij2.plugins.DrawBox;
import net.haesleinhuepf.clij2.plugins.DrawLine;
import net.haesleinhuepf.clij2.plugins.DrawSphere;
import net.haesleinhuepf.clij2.plugins.ReplaceIntensity;
import net.haesleinhuepf.clij2.plugins.BoundingBox;
import net.haesleinhuepf.clij2.plugins.MinimumOfMaskedPixels;
import net.haesleinhuepf.clij2.plugins.MaximumOfMaskedPixels;
import net.haesleinhuepf.clij2.plugins.MeanOfMaskedPixels;
import net.haesleinhuepf.clij2.plugins.LabelToMask;
import net.haesleinhuepf.clij2.plugins.NClosestPoints;
import net.haesleinhuepf.clij2.plugins.StatisticsOfLabelledPixels;
import net.haesleinhuepf.clij2.plugins.VarianceOfAllPixels;
import net.haesleinhuepf.clij2.plugins.StandardDeviationOfAllPixels;
import net.haesleinhuepf.clij2.plugins.VarianceOfMaskedPixels;
import net.haesleinhuepf.clij2.plugins.StandardDeviationOfMaskedPixels;
import net.haesleinhuepf.clij2.plugins.ExcludeLabelsOnEdges;
import net.haesleinhuepf.clij2.plugins.BinarySubtract;
import net.haesleinhuepf.clij2.plugins.BinaryEdgeDetection;
import net.haesleinhuepf.clij2.plugins.DistanceMap;
import net.haesleinhuepf.clij2.plugins.PullAsROI;
import net.haesleinhuepf.clij2.plugins.PullLabelsToROIManager;
import net.haesleinhuepf.clij2.plugins.NonzeroMaximumDiamond;
import net.haesleinhuepf.clij2.plugins.OnlyzeroOverwriteMaximumDiamond;
import net.haesleinhuepf.clij2.plugins.OnlyzeroOverwriteMaximumBox;
import net.haesleinhuepf.clij2.plugins.GenerateTouchMatrix;
import net.haesleinhuepf.clij2.plugins.DetectLabelEdges;
import net.haesleinhuepf.clij2.plugins.CountTouchingNeighbors;
import net.haesleinhuepf.clij2.plugins.ReplaceIntensities;
import net.haesleinhuepf.clij2.plugins.AverageDistanceOfNClosestPoints;
import net.haesleinhuepf.clij2.plugins.TouchMatrixToMesh;
import net.haesleinhuepf.clij2.plugins.Resample;
import net.haesleinhuepf.clij2.plugins.ResliceRadial;
import net.haesleinhuepf.clij2.plugins.Sobel;
import net.haesleinhuepf.clij2.plugins.Absolute;
import net.haesleinhuepf.clij2.plugins.LaplaceBox;
import net.haesleinhuepf.clij2.plugins.BottomHatBox;
import net.haesleinhuepf.clij2.plugins.BottomHatSphere;
import net.haesleinhuepf.clij2.plugins.ClosingBox;
import net.haesleinhuepf.clij2.plugins.ClosingDiamond;
import net.haesleinhuepf.clij2.plugins.OpeningBox;
import net.haesleinhuepf.clij2.plugins.OpeningDiamond;
import net.haesleinhuepf.clij2.plugins.MaximumXProjection;
import net.haesleinhuepf.clij2.plugins.MaximumYProjection;
import net.haesleinhuepf.clij2.plugins.MaximumZProjectionBounded;
import net.haesleinhuepf.clij2.plugins.MinimumZProjectionBounded;
import net.haesleinhuepf.clij2.plugins.MeanZProjectionBounded;
import net.haesleinhuepf.clij2.plugins.NonzeroMaximumBox;
import net.haesleinhuepf.clij2.plugins.NonzeroMinimumBox;
import net.haesleinhuepf.clij2.plugins.MinimumZProjectionThresholdedBounded;
import net.haesleinhuepf.clij2.plugins.MeanOfPixelsAboveThreshold;
import net.haesleinhuepf.clij2.plugins.DistanceMatrixToMesh;
import net.haesleinhuepf.clij2.plugins.PointIndexListToMesh;
import net.haesleinhuepf.clij2.plugins.MinimumOctagon;
import net.haesleinhuepf.clij2.plugins.MaximumOctagon;
import net.haesleinhuepf.clij2.plugins.AddImages;
import net.haesleinhuepf.clij2.plugins.AddImagesWeighted;
import net.haesleinhuepf.clij2.plugins.SubtractImages;
import net.haesleinhuepf.clij2.plugins.AffineTransform2D;
import net.haesleinhuepf.clij2.plugins.AffineTransform3D;
import net.haesleinhuepf.clij2.plugins.ApplyVectorField2D;
import net.haesleinhuepf.clij2.plugins.ApplyVectorField3D;
import net.haesleinhuepf.clij2.plugins.ArgMaximumZProjection;
import net.haesleinhuepf.clij2.plugins.Histogram;
import net.haesleinhuepf.clij2.plugins.AutomaticThreshold;
import net.haesleinhuepf.clij2.plugins.Threshold;
import net.haesleinhuepf.clij2.plugins.BinaryOr;
import net.haesleinhuepf.clij2.plugins.BinaryAnd;
import net.haesleinhuepf.clij2.plugins.BinaryXOr;
import net.haesleinhuepf.clij2.plugins.BinaryNot;
import net.haesleinhuepf.clij2.plugins.ErodeSphere;
import net.haesleinhuepf.clij2.plugins.ErodeBox;
import net.haesleinhuepf.clij2.plugins.ErodeSphereSliceBySlice;
import net.haesleinhuepf.clij2.plugins.ErodeBoxSliceBySlice;
import net.haesleinhuepf.clij2.plugins.DilateSphere;
import net.haesleinhuepf.clij2.plugins.DilateBox;
import net.haesleinhuepf.clij2.plugins.DilateSphereSliceBySlice;
import net.haesleinhuepf.clij2.plugins.DilateBoxSliceBySlice;
import net.haesleinhuepf.clij2.plugins.Copy;
import net.haesleinhuepf.clij2.plugins.CopySlice;
import net.haesleinhuepf.clij2.plugins.Crop2D;
import net.haesleinhuepf.clij2.plugins.Crop3D;
import net.haesleinhuepf.clij2.plugins.Set;
import net.haesleinhuepf.clij2.plugins.Flip2D;
import net.haesleinhuepf.clij2.plugins.Flip3D;
import net.haesleinhuepf.clij2.plugins.RotateCounterClockwise;
import net.haesleinhuepf.clij2.plugins.RotateClockwise;
import net.haesleinhuepf.clij2.plugins.Mask;
import net.haesleinhuepf.clij2.plugins.MaskStackWithPlane;
import net.haesleinhuepf.clij2.plugins.MaximumZProjection;
import net.haesleinhuepf.clij2.plugins.MeanZProjection;
import net.haesleinhuepf.clij2.plugins.MinimumZProjection;
import net.haesleinhuepf.clij2.plugins.Power;
import net.haesleinhuepf.clij2.plugins.DivideImages;
import net.haesleinhuepf.clij2.plugins.MaximumImages;
import net.haesleinhuepf.clij2.plugins.MaximumImageAndScalar;
import net.haesleinhuepf.clij2.plugins.MinimumImages;
import net.haesleinhuepf.clij2.plugins.MinimumImageAndScalar;
import net.haesleinhuepf.clij2.plugins.MultiplyImageAndScalar;
import net.haesleinhuepf.clij2.plugins.MultiplyStackWithPlane;
import net.haesleinhuepf.clij2.plugins.CountNonZeroPixels2DSphere;
import net.haesleinhuepf.clij2.plugins.CountNonZeroPixelsSliceBySliceSphere;
import net.haesleinhuepf.clij2.plugins.CountNonZeroVoxels3DSphere;
import net.haesleinhuepf.clij2.plugins.SumZProjection;
import net.haesleinhuepf.clij2.plugins.SumOfAllPixels;
import net.haesleinhuepf.clij2.plugins.CenterOfMass;
import net.haesleinhuepf.clij2.plugins.Invert;
import net.haesleinhuepf.clij2.plugins.DownsampleSliceBySliceHalfMedian;
import net.haesleinhuepf.clij2.plugins.LocalThreshold;
import net.haesleinhuepf.clij2.plugins.GradientX;
import net.haesleinhuepf.clij2.plugins.GradientY;
import net.haesleinhuepf.clij2.plugins.GradientZ;
import net.haesleinhuepf.clij2.plugins.MultiplyImageAndCoordinate;
import net.haesleinhuepf.clij2.plugins.Mean2DBox;
import net.haesleinhuepf.clij2.plugins.Mean2DSphere;
import net.haesleinhuepf.clij2.plugins.Mean3DBox;
import net.haesleinhuepf.clij2.plugins.Mean3DSphere;
import net.haesleinhuepf.clij2.plugins.MeanSliceBySliceSphere;
import net.haesleinhuepf.clij2.plugins.MeanOfAllPixels;
import net.haesleinhuepf.clij2.plugins.Median2DBox;
import net.haesleinhuepf.clij2.plugins.Median2DSphere;
import net.haesleinhuepf.clij2.plugins.Median3DBox;
import net.haesleinhuepf.clij2.plugins.Median3DSphere;
import net.haesleinhuepf.clij2.plugins.MedianSliceBySliceBox;
import net.haesleinhuepf.clij2.plugins.MedianSliceBySliceSphere;
import net.haesleinhuepf.clij2.plugins.Maximum2DSphere;
import net.haesleinhuepf.clij2.plugins.Maximum3DSphere;
import net.haesleinhuepf.clij2.plugins.Maximum2DBox;
import net.haesleinhuepf.clij2.plugins.Maximum3DBox;
import net.haesleinhuepf.clij2.plugins.MaximumSliceBySliceSphere;
import net.haesleinhuepf.clij2.plugins.Minimum2DSphere;
import net.haesleinhuepf.clij2.plugins.Minimum3DSphere;
import net.haesleinhuepf.clij2.plugins.Minimum2DBox;
import net.haesleinhuepf.clij2.plugins.Minimum3DBox;
import net.haesleinhuepf.clij2.plugins.MinimumSliceBySliceSphere;
import net.haesleinhuepf.clij2.plugins.MultiplyImages;
import net.haesleinhuepf.clij2.plugins.GaussianBlur2D;
import net.haesleinhuepf.clij2.plugins.GaussianBlur3D;
import net.haesleinhuepf.clij2.plugins.ResliceBottom;
import net.haesleinhuepf.clij2.plugins.ResliceTop;
import net.haesleinhuepf.clij2.plugins.ResliceLeft;
import net.haesleinhuepf.clij2.plugins.ResliceRight;
import net.haesleinhuepf.clij2.plugins.Rotate2D;
import net.haesleinhuepf.clij2.plugins.Rotate3D;
import net.haesleinhuepf.clij2.plugins.Scale2D;
import net.haesleinhuepf.clij2.plugins.Scale3D;
import net.haesleinhuepf.clij2.plugins.Translate2D;
import net.haesleinhuepf.clij2.plugins.Translate3D;
import net.haesleinhuepf.clij2.plugins.AddImageAndScalar;
import net.haesleinhuepf.clij2.plugins.DetectMinimaBox;
import net.haesleinhuepf.clij2.plugins.DetectMaximaBox;
import net.haesleinhuepf.clij2.plugins.DetectMaximaSliceBySliceBox;
import net.haesleinhuepf.clij2.plugins.DetectMinimaSliceBySliceBox;
import net.haesleinhuepf.clij2.plugins.MaximumOfAllPixels;
import net.haesleinhuepf.clij2.plugins.MinimumOfAllPixels;
import net.haesleinhuepf.clij2.plugins.SetColumn;
import net.haesleinhuepf.clij2.plugins.SetRow;
import net.haesleinhuepf.clij2.plugins.SumYProjection;
import net.haesleinhuepf.clij2.plugins.AverageDistanceOfTouchingNeighbors;
import net.haesleinhuepf.clij2.plugins.LabelledSpotsToPointList;
import net.haesleinhuepf.clij2.plugins.LabelSpots;
import net.haesleinhuepf.clij2.plugins.MinimumDistanceOfTouchingNeighbors;
import net.haesleinhuepf.clij2.plugins.SetWhereXgreaterThanY;
import net.haesleinhuepf.clij2.plugins.SetWhereXsmallerThanY;
import net.haesleinhuepf.clij2.plugins.SetNonZeroPixelsToPixelIndex;
import net.haesleinhuepf.clij2.plugins.CloseIndexGapsInLabelMap;
// this is generated code. See src/test/java/net/haesleinhuepf/clijx/codegenerator for details
public abstract interface CLIJ2Ops {
   CLIJ getCLIJ();
   CLIJ2 getCLIJ2();
   default CLIJx getCLIJx() {return null;}
   

    // net.haesleinhuepf.clij2.plugins.BinaryUnion
    //----------------------------------------------------
    /**
     * Computes a binary image (containing pixel values 0 and 1) from two images X and Y by connecting pairs of
     * pixels x and y with the binary union operator |.
     * All pixel values except 0 in the input images are interpreted as 1.<pre>f(x, y) = x | y</pre>
     */
    default boolean binaryUnion(ClearCLBuffer operand1, ClearCLBuffer operand2, ClearCLBuffer destination) {
        return BinaryUnion.binaryUnion(getCLIJ2(), operand1, operand2, destination);
    }


    // net.haesleinhuepf.clij2.plugins.BinaryIntersection
    //----------------------------------------------------
    /**
     * Computes a binary image (containing pixel values 0 and 1) from two images X and Y by connecting pairs of
     * pixels x and y with the binary intersection operator &.
     * All pixel values except 0 in the input images are interpreted as 1.
     * 
     * <pre>f(x, y) = x & y</pre>
     */
    default boolean binaryIntersection(ClearCLBuffer operand1, ClearCLBuffer operand2, ClearCLBuffer destination) {
        return BinaryIntersection.binaryIntersection(getCLIJ2(), operand1, operand2, destination);
    }


    // net.haesleinhuepf.clij2.plugins.ConnectedComponentsLabeling
    //----------------------------------------------------
    /**
     * Performs connected components analysis to a binary image and generates a label map.
     */
    default boolean connectedComponentsLabeling(ClearCLBuffer binary_input, ClearCLBuffer labeling_destination) {
        return ConnectedComponentsLabeling.connectedComponentsLabeling(getCLIJ2(), binary_input, labeling_destination);
    }


    // net.haesleinhuepf.clij2.plugins.CountNonZeroPixels
    //----------------------------------------------------
    /**
     * Determines the number of all pixels in a given image which are not equal to 0. It will be stored in a new row of ImageJs
     * Results table in the column 'CountNonZero'.
     */
    default double countNonZeroPixels(ClearCLBuffer source) {
        return CountNonZeroPixels.countNonZeroPixels(getCLIJ2(), source);
    }


    // net.haesleinhuepf.clij2.plugins.DifferenceOfGaussian2D
    //----------------------------------------------------
    /**
     * Applies Gaussian blur to the input image twice with different sigma values resulting in two images which are then subtracted from each other.
     * 
     * It is recommended to apply this operation to images of type Float (32 bit) as results might be negative.
     */
    default boolean differenceOfGaussian(ClearCLBuffer input, ClearCLBuffer destination, double sigma1x, double sigma1y, double sigma2x, double sigma2y) {
        return DifferenceOfGaussian2D.differenceOfGaussian(getCLIJ2(), input, destination, new Double (sigma1x).floatValue(), new Double (sigma1y).floatValue(), new Double (sigma2x).floatValue(), new Double (sigma2y).floatValue());
    }

    /**
     * Applies Gaussian blur to the input image twice with different sigma values resulting in two images which are then subtracted from each other.
     * 
     * It is recommended to apply this operation to images of type Float (32 bit) as results might be negative.
     */
    default boolean differenceOfGaussian2D(ClearCLBuffer input, ClearCLBuffer destination, double sigma1x, double sigma1y, double sigma2x, double sigma2y) {
        return DifferenceOfGaussian2D.differenceOfGaussian2D(getCLIJ2(), input, destination, new Double (sigma1x).floatValue(), new Double (sigma1y).floatValue(), new Double (sigma2x).floatValue(), new Double (sigma2y).floatValue());
    }


    // net.haesleinhuepf.clij2.plugins.DifferenceOfGaussian3D
    //----------------------------------------------------
    /**
     * Applies Gaussian blur to the input image twice with different sigma values resulting in two images which are then subtracted from each other.
     * 
     * It is recommended to apply this operation to images of type Float (32 bit) as results might be negative.
     */
    default boolean differenceOfGaussian(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3, double arg4, double arg5, double arg6, double arg7, double arg8) {
        return DifferenceOfGaussian3D.differenceOfGaussian(getCLIJ2(), arg1, arg2, new Double (arg3).floatValue(), new Double (arg4).floatValue(), new Double (arg5).floatValue(), new Double (arg6).floatValue(), new Double (arg7).floatValue(), new Double (arg8).floatValue());
    }

    /**
     * Applies Gaussian blur to the input image twice with different sigma values resulting in two images which are then subtracted from each other.
     * 
     * It is recommended to apply this operation to images of type Float (32 bit) as results might be negative.
     */
    default boolean differenceOfGaussian3D(ClearCLBuffer input, ClearCLBuffer destination, double sigma1x, double sigma1y, double sigma1z, double sigma2x, double sigma2y, double sigma2z) {
        return DifferenceOfGaussian3D.differenceOfGaussian3D(getCLIJ2(), input, destination, new Double (sigma1x).floatValue(), new Double (sigma1y).floatValue(), new Double (sigma1z).floatValue(), new Double (sigma2x).floatValue(), new Double (sigma2y).floatValue(), new Double (sigma2z).floatValue());
    }


    // net.haesleinhuepf.clij2.plugins.MaskLabel
    //----------------------------------------------------
    /**
     * Computes a masked image by applying a label mask to an image. All pixel values x of image X will be copied
     * to the destination image in case pixel value m at the same position in the label_map image has the right index value i.
     * 
     * f(x,m,i) = (x if (m == i); (0 otherwise))
     */
    default boolean maskLabel(ClearCLBuffer source, ClearCLBuffer label_map, ClearCLBuffer destination, double label_index) {
        return MaskLabel.maskLabel(getCLIJ2(), source, label_map, destination, new Double (label_index).floatValue());
    }


    // net.haesleinhuepf.clij2.plugins.MeanClosestSpotDistance
    //----------------------------------------------------
    /**
     * 
     */
    default double meanClosestSpotDistances(ClearCLBuffer arg1, ClearCLBuffer arg2) {
        return MeanClosestSpotDistance.meanClosestSpotDistances(getCLIJ2(), arg1, arg2);
    }

    /**
     * 
     */
    default double[] meanClosestSpotDistances(ClearCLBuffer arg1, ClearCLBuffer arg2, boolean arg3) {
        return MeanClosestSpotDistance.meanClosestSpotDistances(getCLIJ2(), arg1, arg2, arg3);
    }


    // net.haesleinhuepf.clij2.plugins.MeanSquaredError
    //----------------------------------------------------
    /**
     * Determines the mean squared error (MSE) between two images. The MSE will be stored in a new row of ImageJs
     * Results table in the column 'MSE'.
     */
    default double meanSquaredError(ClearCLBuffer source1, ClearCLBuffer source2) {
        return MeanSquaredError.meanSquaredError(getCLIJ2(), source1, source2);
    }


    // net.haesleinhuepf.clij2.plugins.MedianZProjection
    //----------------------------------------------------
    /**
     * Determines the median projection of an image stack along Z.
     */
    default boolean medianZProjection(ClearCLImageInterface arg1, ClearCLImageInterface arg2) {
        return MedianZProjection.medianZProjection(getCLIJ2(), arg1, arg2);
    }


    // net.haesleinhuepf.clij2.plugins.NonzeroMinimumDiamond
    //----------------------------------------------------
    /**
     * Apply a minimum filter (diamond shape) to the input image. The radius is fixed to 1 and pixels with value 0 are ignored.
     */
    default ClearCLKernel nonzeroMinimumDiamond(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLImageInterface arg3, ClearCLKernel arg4) {
        return NonzeroMinimumDiamond.nonzeroMinimumDiamond(getCLIJ2(), arg1, arg2, arg3, arg4);
    }

    /**
     * Apply a minimum filter (diamond shape) to the input image. The radius is fixed to 1 and pixels with value 0 are ignored.
     */
    default boolean nonzeroMinimumDiamond(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLImageInterface arg3) {
        return NonzeroMinimumDiamond.nonzeroMinimumDiamond(getCLIJ2(), arg1, arg2, arg3);
    }


    // net.haesleinhuepf.clij2.plugins.Paste2D
    //----------------------------------------------------
    /**
     * Pastes an image into another image at a given position.
     */
    default boolean paste2D(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4) {
        return Paste2D.paste2D(getCLIJ2(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue());
    }

    /**
     * Pastes an image into another image at a given position.
     */
    default boolean paste(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4) {
        return Paste2D.paste(getCLIJ2(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue());
    }


    // net.haesleinhuepf.clij2.plugins.Paste3D
    //----------------------------------------------------
    /**
     * Pastes an image into another image at a given position.
     */
    default boolean paste3D(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4, double arg5) {
        return Paste3D.paste3D(getCLIJ2(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue(), new Double (arg5).intValue());
    }

    /**
     * Pastes an image into another image at a given position.
     */
    default boolean paste(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4, double arg5) {
        return Paste3D.paste(getCLIJ2(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue(), new Double (arg5).intValue());
    }


    // net.haesleinhuepf.clij2.plugins.JaccardIndex
    //----------------------------------------------------
    /**
     * Determines the overlap of two binary images using the Jaccard index.
     * A value of 0 suggests no overlap, 1 means perfect overlap.
     * The resulting Jaccard index is saved to the results table in the 'Jaccard_Index' column.
     * Note that the Sorensen-Dice coefficient can be calculated from the Jaccard index j using this formula:
     * <pre>s = f(j) = 2 j / (j + 1)</pre>
     */
    default double jaccardIndex(ClearCLBuffer source1, ClearCLBuffer source2) {
        return JaccardIndex.jaccardIndex(getCLIJ2(), source1, source2);
    }


    // net.haesleinhuepf.clij2.plugins.SorensenDiceCoefficient
    //----------------------------------------------------
    /**
     * Determines the overlap of two binary images using the Sorensen-Dice coefficent.
     * A value of 0 suggests no overlap, 1 means perfect overlap.
     * The Sorensen-Dice coefficient is saved in the colum 'Sorensen_Dice_coefficient'.
     * Note that the Sorensen-Dice coefficient s can be calculated from the Jaccard index j using this formula:
     * <pre>s = f(j) = 2 j / (j + 1)</pre>
     */
    default double sorensenDiceCoefficient(ClearCLBuffer source1, ClearCLBuffer source2) {
        return SorensenDiceCoefficient.sorensenDiceCoefficient(getCLIJ2(), source1, source2);
    }


    // net.haesleinhuepf.clij2.plugins.StandardDeviationZProjection
    //----------------------------------------------------
    /**
     * Determines the standard deviation projection of an image stack along Z.
     */
    default boolean standardDeviationZProjection(ClearCLImageInterface arg1, ClearCLImageInterface arg2) {
        return StandardDeviationZProjection.standardDeviationZProjection(getCLIJ2(), arg1, arg2);
    }


    // net.haesleinhuepf.clij2.plugins.TopHatBox
    //----------------------------------------------------
    /**
     * Applies a top-hat filter for background subtraction to the input image.
     */
    default boolean topHatBox(ClearCLBuffer input, ClearCLBuffer destination, double radiusX, double radiusY, double radiusZ) {
        return TopHatBox.topHatBox(getCLIJ2(), input, destination, new Double (radiusX).intValue(), new Double (radiusY).intValue(), new Double (radiusZ).intValue());
    }


    // net.haesleinhuepf.clij2.plugins.TopHatSphere
    //----------------------------------------------------
    /**
     * Applies a top-hat filter for background subtraction to the input image.
     */
    default boolean topHatSphere(ClearCLBuffer input, ClearCLBuffer destination, double radiusX, double radiusY, double radiusZ) {
        return TopHatSphere.topHatSphere(getCLIJ2(), input, destination, new Double (radiusX).intValue(), new Double (radiusY).intValue(), new Double (radiusZ).intValue());
    }


    // net.haesleinhuepf.clij2.plugins.Exponential
    //----------------------------------------------------
    /**
     * Computes base exponential of all pixels values.
     * 
     * f(x) = exp(x)
     */
    default boolean exponential(ClearCLImageInterface arg1, ClearCLImageInterface arg2) {
        return Exponential.exponential(getCLIJ2(), arg1, arg2);
    }


    // net.haesleinhuepf.clij2.plugins.Logarithm
    //----------------------------------------------------
    /**
     * Computes base e logarithm of all pixels values.
     * 
     * f(x) = log(x)
     */
    default boolean logarithm(ClearCLImageInterface arg1, ClearCLImageInterface arg2) {
        return Logarithm.logarithm(getCLIJ2(), arg1, arg2);
    }


    // net.haesleinhuepf.clij2.plugins.GenerateDistanceMatrix
    //----------------------------------------------------
    /**
     * Takes two images containing coordinates and builds up a matrix containing distance between the points. Convention: image width represents number of points, height represents dimensionality (2D, 3D, ... 10D). The result image has width the first input image and height equals to the width of the second input image.
     */
    default boolean generateDistanceMatrix(ClearCLBuffer coordinate_list1, ClearCLBuffer coordinate_list2, ClearCLBuffer distance_matrix_destination) {
        return GenerateDistanceMatrix.generateDistanceMatrix(getCLIJ2(), coordinate_list1, coordinate_list2, distance_matrix_destination);
    }


    // net.haesleinhuepf.clij2.plugins.ShortestDistances
    //----------------------------------------------------
    /**
     * Determine the shortest distance from a distance matrix. This corresponds to the minimum in a matrix for each individial column.
     */
    default boolean shortestDistances(ClearCLBuffer distance_matrix, ClearCLBuffer destination_minimum_distances) {
        return ShortestDistances.shortestDistances(getCLIJ2(), distance_matrix, destination_minimum_distances);
    }


    // net.haesleinhuepf.clij2.plugins.SpotsToPointList
    //----------------------------------------------------
    /**
     * Transforms a spots image as resulting from maximum/minimum detection in an image where every column contains d 
     * pixels (with d = dimensionality of the original image) with the coordinates of the maxima/minima.
     */
    default boolean spotsToPointList(ClearCLBuffer input_spots, ClearCLBuffer destination_pointlist) {
        return SpotsToPointList.spotsToPointList(getCLIJ2(), input_spots, destination_pointlist);
    }


    // net.haesleinhuepf.clij2.plugins.TransposeXY
    //----------------------------------------------------
    /**
     * Transpose X and Y axes of an image.
     */
    default boolean transposeXY(ClearCLBuffer input, ClearCLBuffer destination) {
        return TransposeXY.transposeXY(getCLIJ2(), input, destination);
    }


    // net.haesleinhuepf.clij2.plugins.TransposeXZ
    //----------------------------------------------------
    /**
     * Transpose X and Z axes of an image.
     */
    default boolean transposeXZ(ClearCLBuffer input, ClearCLBuffer destination) {
        return TransposeXZ.transposeXZ(getCLIJ2(), input, destination);
    }


    // net.haesleinhuepf.clij2.plugins.TransposeYZ
    //----------------------------------------------------
    /**
     * Transpose Y and Z axes of an image.
     */
    default boolean transposeYZ(ClearCLBuffer input, ClearCLBuffer destination) {
        return TransposeYZ.transposeYZ(getCLIJ2(), input, destination);
    }


    // net.haesleinhuepf.clij2.plugins.SetWhereXequalsY
    //----------------------------------------------------
    /**
     * Sets all pixel values a of a given image A to a constant value v in case its coordinates x == y. Otherwise the pixel is not overwritten.
     * If you want to initialize an identity transfrom matrix, set all pixels to 0 first.
     * 
     * <pre>f(a) = v</pre>
     */
    default boolean setWhereXequalsY(ClearCLImageInterface arg1, double arg2) {
        return SetWhereXequalsY.setWhereXequalsY(getCLIJ2(), arg1, new Double (arg2).floatValue());
    }


    // net.haesleinhuepf.clij2.plugins.LaplaceDiamond
    //----------------------------------------------------
    /**
     * Applies the Laplace operator (Diamond neighborhood) to an image.
     */
    default boolean laplaceSphere(ClearCLBuffer input, ClearCLBuffer destination) {
        return LaplaceDiamond.laplaceSphere(getCLIJ(), input, destination);
    }


    // net.haesleinhuepf.clij2.plugins.Image2DToResultsTable
    //----------------------------------------------------
    /**
     * Converts an image into a table.
     */
    default ResultsTable image2DToResultsTable(ClearCLBuffer arg1, ResultsTable arg2) {
        return Image2DToResultsTable.image2DToResultsTable(getCLIJ2(), arg1, arg2);
    }

    /**
     * Converts an image into a table.
     */
    default ResultsTable image2DToResultsTable(ClearCLImage arg1, ResultsTable arg2) {
        return Image2DToResultsTable.image2DToResultsTable(getCLIJ2(), arg1, arg2);
    }


    // net.haesleinhuepf.clij2.plugins.WriteValuesToPositions
    //----------------------------------------------------
    /**
     * Takes an image with three/four rows (2D: height = 3; 3D: height = 4): x, y [, z] and v and target image. The value v will be written at position x/y[/z] in the target image.
     */
    default boolean writeValuesToPositions(ClearCLBuffer positionsAndValues, ClearCLBuffer destination) {
        return WriteValuesToPositions.writeValuesToPositions(getCLIJ2(), positionsAndValues, destination);
    }


    // net.haesleinhuepf.clij2.plugins.GetSize
    //----------------------------------------------------
    /**
     * Reads out the size of an image [stack] and writes it to the results table in the columns 'Width', 'Height' and 'Depth'.
     */
    default long[] getSize(ClearCLBuffer source) {
        return GetSize.getSize(getCLIJ2(), source);
    }


    // net.haesleinhuepf.clij2.plugins.MultiplyMatrix
    //----------------------------------------------------
    /**
     * Multiplies two matrices with each other.
     */
    default boolean multiplyMatrix(ClearCLBuffer matrix1, ClearCLBuffer matrix2, ClearCLBuffer matrix_destination) {
        return MultiplyMatrix.multiplyMatrix(getCLIJ2(), matrix1, matrix2, matrix_destination);
    }


    // net.haesleinhuepf.clij2.plugins.Equal
    //----------------------------------------------------
    /**
     * Determines if two images A and B equal pixel wise.
     * 
     * f(a, b) = 1 if a == b; 0 otherwise. 
     */
    default boolean equal(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLImageInterface arg3) {
        return Equal.equal(getCLIJ2(), arg1, arg2, arg3);
    }


    // net.haesleinhuepf.clij2.plugins.GreaterOrEqual
    //----------------------------------------------------
    /**
     * Determines if two images A and B greater or equal pixel wise.
     * 
     * f(a, b) = 1 if a >= b; 0 otherwise. 
     */
    default boolean greaterOrEqual(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLImageInterface arg3) {
        return GreaterOrEqual.greaterOrEqual(getCLIJ2(), arg1, arg2, arg3);
    }


    // net.haesleinhuepf.clij2.plugins.Greater
    //----------------------------------------------------
    /**
     * Determines if two images A and B greater pixel wise.
     * 
     * f(a, b) = 1 if a > b; 0 otherwise. 
     */
    default boolean greater(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLImageInterface arg3) {
        return Greater.greater(getCLIJ2(), arg1, arg2, arg3);
    }


    // net.haesleinhuepf.clij2.plugins.Smaller
    //----------------------------------------------------
    /**
     * Determines if two images A and B smaller pixel wise.
     * 
     * f(a, b) = 1 if a < b; 0 otherwise. 
     */
    default boolean smaller(ClearCLBuffer source1, ClearCLBuffer source2, ClearCLBuffer destination) {
        return Smaller.smaller(getCLIJ2(), source1, source2, destination);
    }


    // net.haesleinhuepf.clij2.plugins.SmallerOrEqual
    //----------------------------------------------------
    /**
     * Determines if two images A and B smaller or equal pixel wise.
     * 
     * f(a, b) = 1 if a <= b; 0 otherwise. 
     */
    default boolean smallerOrEqual(ClearCLBuffer source1, ClearCLBuffer source2, ClearCLBuffer destination) {
        return SmallerOrEqual.smallerOrEqual(getCLIJ2(), source1, source2, destination);
    }


    // net.haesleinhuepf.clij2.plugins.NotEqual
    //----------------------------------------------------
    /**
     * Determines if two images A and B equal pixel wise.
     * 
     * f(a, b) = 1 if a != b; 0 otherwise. 
     */
    default boolean notEqual(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLBuffer arg3) {
        return NotEqual.notEqual(getCLIJ2(), arg1, arg2, arg3);
    }


    // net.haesleinhuepf.clij2.plugins.EqualConstant
    //----------------------------------------------------
    /**
     * Determines if an image A and a constant b are equal.
     * 
     * f(a, b) = 1 if a == b; 0 otherwise. 
     */
    default boolean equalConstant(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3) {
        return EqualConstant.equalConstant(getCLIJ2(), arg1, arg2, new Double (arg3).floatValue());
    }


    // net.haesleinhuepf.clij2.plugins.GreaterOrEqualConstant
    //----------------------------------------------------
    /**
     * Determines if two images A and B greater or equal pixel wise.
     * 
     * f(a, b) = 1 if a >= b; 0 otherwise. 
     */
    default boolean greaterOrEqualConstant(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3) {
        return GreaterOrEqualConstant.greaterOrEqualConstant(getCLIJ2(), arg1, arg2, new Double (arg3).floatValue());
    }


    // net.haesleinhuepf.clij2.plugins.GreaterConstant
    //----------------------------------------------------
    /**
     * Determines if two images A and B greater pixel wise.
     * 
     * f(a, b) = 1 if a > b; 0 otherwise. 
     */
    default boolean greaterConstant(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3) {
        return GreaterConstant.greaterConstant(getCLIJ2(), arg1, arg2, new Double (arg3).floatValue());
    }


    // net.haesleinhuepf.clij2.plugins.SmallerConstant
    //----------------------------------------------------
    /**
     * Determines if two images A and B smaller pixel wise.
     * 
     * f(a, b) = 1 if a < b; 0 otherwise. 
     */
    default boolean smallerConstant(ClearCLBuffer source, ClearCLBuffer destination, double constant) {
        return SmallerConstant.smallerConstant(getCLIJ2(), source, destination, new Double (constant).floatValue());
    }


    // net.haesleinhuepf.clij2.plugins.SmallerOrEqualConstant
    //----------------------------------------------------
    /**
     * Determines if two images A and B smaller or equal pixel wise.
     * 
     * f(a, b) = 1 if a <= b; 0 otherwise. 
     */
    default boolean smallerOrEqualConstant(ClearCLBuffer source, ClearCLBuffer destination, double constant) {
        return SmallerOrEqualConstant.smallerOrEqualConstant(getCLIJ2(), source, destination, new Double (constant).floatValue());
    }


    // net.haesleinhuepf.clij2.plugins.NotEqualConstant
    //----------------------------------------------------
    /**
     * Determines if two images A and B equal pixel wise.
     * 
     * f(a, b) = 1 if a != b; 0 otherwise. 
     */
    default boolean notEqualConstant(ClearCLBuffer source, ClearCLBuffer destination, double constant) {
        return NotEqualConstant.notEqualConstant(getCLIJ2(), source, destination, new Double (constant).floatValue());
    }


    // net.haesleinhuepf.clij2.plugins.DrawBox
    //----------------------------------------------------
    /**
     * Draws a box at a given start point with given size. All pixels other than in the box are untouched. Consider using clij.op.set(buffer, 0); in advance.
     */
    default boolean drawBox(ClearCLBuffer arg1, double arg2, double arg3, double arg4, double arg5) {
        return DrawBox.drawBox(getCLIJ2(), arg1, new Double (arg2).floatValue(), new Double (arg3).floatValue(), new Double (arg4).floatValue(), new Double (arg5).floatValue());
    }

    /**
     * Draws a box at a given start point with given size. All pixels other than in the box are untouched. Consider using clij.op.set(buffer, 0); in advance.
     */
    default boolean drawBox(ClearCLBuffer destination, double x, double y, double z, double width, double height, double depth) {
        return DrawBox.drawBox(getCLIJ2(), destination, new Double (x).floatValue(), new Double (y).floatValue(), new Double (z).floatValue(), new Double (width).floatValue(), new Double (height).floatValue(), new Double (depth).floatValue());
    }


    // net.haesleinhuepf.clij2.plugins.DrawLine
    //----------------------------------------------------
    /**
     * Draws a line between two points with a given thickness. All pixels other than on the line are untouched. Consider using clij.op.set(buffer, 0); in advance.
     */
    default boolean drawLine(ClearCLBuffer destination, double x1, double y1, double z1, double x2, double y2, double z2, double thickness) {
        return DrawLine.drawLine(getCLIJ2(), destination, new Double (x1).floatValue(), new Double (y1).floatValue(), new Double (z1).floatValue(), new Double (x2).floatValue(), new Double (y2).floatValue(), new Double (z2).floatValue(), new Double (thickness).floatValue());
    }


    // net.haesleinhuepf.clij2.plugins.DrawSphere
    //----------------------------------------------------
    /**
     * Draws a sphere around a given point with given radii in x, y and z (if 3D). All pixels other than in the sphere are untouched. Consider using clij.op.set(buffer, 0); in advance.
     */
    default boolean drawSphere(ClearCLBuffer arg1, double arg2, double arg3, double arg4, double arg5) {
        return DrawSphere.drawSphere(getCLIJ(), arg1, new Double (arg2).floatValue(), new Double (arg3).floatValue(), new Double (arg4).floatValue(), new Double (arg5).floatValue());
    }

    /**
     * Draws a sphere around a given point with given radii in x, y and z (if 3D). All pixels other than in the sphere are untouched. Consider using clij.op.set(buffer, 0); in advance.
     */
    default boolean drawSphere(ClearCLBuffer destination, double x, double y, double z, double radius_x, double radius_y, double radius_z) {
        return DrawSphere.drawSphere(getCLIJ(), destination, new Double (x).floatValue(), new Double (y).floatValue(), new Double (z).floatValue(), new Double (radius_x).floatValue(), new Double (radius_y).floatValue(), new Double (radius_z).floatValue());
    }


    // net.haesleinhuepf.clij2.plugins.ReplaceIntensity
    //----------------------------------------------------
    /**
     * Replaces a specific intensity in an image with a given new value.
     */
    default boolean replaceIntensity(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4) {
        return ReplaceIntensity.replaceIntensity(getCLIJ2(), arg1, arg2, new Double (arg3).floatValue(), new Double (arg4).floatValue());
    }


    // net.haesleinhuepf.clij2.plugins.BoundingBox
    //----------------------------------------------------
    /**
     * Determines the bounding box of all non-zero pixels in a binary image. The positions will be stored in a new row of ImageJs
     * Results table in the column 'BoundingBoxX', 'BoundingBoxY', 'BoundingBoxZ', 'BoundingBoxWidth', 'BoundingBoxHeight' 'BoundingBoxDepth'.In case of 2D images Z and depth will be zero.
     */
    default double[] boundingBox(ClearCLBuffer source) {
        return BoundingBox.boundingBox(getCLIJ2(), source);
    }


    // net.haesleinhuepf.clij2.plugins.MinimumOfMaskedPixels
    //----------------------------------------------------
    /**
     * Determines the minimum intensity in an image, but only in pixels which have non-zero values in another mask image.
     */
    default double minimumOfMaskedPixels(ClearCLBuffer source, ClearCLBuffer mask) {
        return MinimumOfMaskedPixels.minimumOfMaskedPixels(getCLIJ2(), source, mask);
    }


    // net.haesleinhuepf.clij2.plugins.MaximumOfMaskedPixels
    //----------------------------------------------------
    /**
     * Determines the maximum intensity in an image, but only in pixels which have non-zero values in another mask image.
     */
    default double maximumOfMaskedPixels(ClearCLBuffer source, ClearCLBuffer mask) {
        return MaximumOfMaskedPixels.maximumOfMaskedPixels(getCLIJ2(), source, mask);
    }


    // net.haesleinhuepf.clij2.plugins.MeanOfMaskedPixels
    //----------------------------------------------------
    /**
     * Determines the mean intensity in an image, but only in pixels which have non-zero values in another binary mask image.
     */
    default double meanOfMaskedPixels(ClearCLBuffer source, ClearCLBuffer mask) {
        return MeanOfMaskedPixels.meanOfMaskedPixels(getCLIJ2(), source, mask);
    }


    // net.haesleinhuepf.clij2.plugins.LabelToMask
    //----------------------------------------------------
    /**
     * Masks a single label in a label map: Sets all pixels in the target image to 1, where the given label index was present in the label map. Other pixels are set to 0.
     */
    default boolean labelToMask(ClearCLBuffer label_map_source, ClearCLBuffer mask_destination, double label_index) {
        return LabelToMask.labelToMask(getCLIJ2(), label_map_source, mask_destination, new Double (label_index).floatValue());
    }


    // net.haesleinhuepf.clij2.plugins.NClosestPoints
    //----------------------------------------------------
    /**
     * Determine the n point indices with shortest distance for all points in a distance matrix.
     * This corresponds to the n row indices with minimum values for each column of the distance matrix.
     */
    default boolean nClosestPoints(ClearCLBuffer arg1, ClearCLBuffer arg2) {
        return NClosestPoints.nClosestPoints(getCLIJ2(), arg1, arg2);
    }


    // net.haesleinhuepf.clij2.plugins.StatisticsOfLabelledPixels
    //----------------------------------------------------
    /**
     * Determines bounding box, area (in pixels/voxels), min, max and mean intensity  of a labelled object in a label map and corresponding pixels in the original image.Instead of a label map, you can also use a binary image as a binary image is a label map with just one label.
     */
    default double[] statisticsOfLabelledPixels(ClearCLBuffer arg1, ClearCLBuffer arg2, int arg3) {
        return StatisticsOfLabelledPixels.statisticsOfLabelledPixels(getCLIJ2(), arg1, arg2, arg3);
    }

    /**
     * Determines bounding box, area (in pixels/voxels), min, max and mean intensity  of a labelled object in a label map and corresponding pixels in the original image.Instead of a label map, you can also use a binary image as a binary image is a label map with just one label.
     */
    default ResultsTable statisticsOfLabelledPixels(ClearCLBuffer arg1, ClearCLBuffer arg2, ResultsTable arg3) {
        return StatisticsOfLabelledPixels.statisticsOfLabelledPixels(getCLIJ2(), arg1, arg2, arg3);
    }

    /**
     * Determines bounding box, area (in pixels/voxels), min, max and mean intensity  of a labelled object in a label map and corresponding pixels in the original image.Instead of a label map, you can also use a binary image as a binary image is a label map with just one label.
     */
    default double[][] statisticsOfLabelledPixels(ClearCLBuffer arg1, ClearCLBuffer arg2, int arg3, int arg4) {
        return StatisticsOfLabelledPixels.statisticsOfLabelledPixels(getCLIJ2(), arg1, arg2, arg3, arg4);
    }

    /**
     * Determines bounding box, area (in pixels/voxels), min, max and mean intensity  of a labelled object in a label map and corresponding pixels in the original image.Instead of a label map, you can also use a binary image as a binary image is a label map with just one label.
     */
    default double[][] statisticsOfLabelledPixels(ClearCLBuffer input, ClearCLBuffer labelmap) {
        return StatisticsOfLabelledPixels.statisticsOfLabelledPixels(getCLIJ2(), input, labelmap);
    }


    // net.haesleinhuepf.clij2.plugins.VarianceOfAllPixels
    //----------------------------------------------------
    /**
     * Determines the variance of all pixels in an image. The value will be stored in a new row of ImageJs
     * Results table in the column 'Variance'.
     */
    default double varianceOfAllPixels(ClearCLBuffer source) {
        return VarianceOfAllPixels.varianceOfAllPixels(getCLIJ2(), source);
    }

    /**
     * Determines the variance of all pixels in an image. The value will be stored in a new row of ImageJs
     * Results table in the column 'Variance'.
     */
    default double varianceOfAllPixels(ClearCLImageInterface arg1, double arg2) {
        return VarianceOfAllPixels.varianceOfAllPixels(getCLIJ2(), arg1, new Double (arg2).floatValue());
    }


    // net.haesleinhuepf.clij2.plugins.StandardDeviationOfAllPixels
    //----------------------------------------------------
    /**
     * Determines the standard deviation of all pixels in an image. The value will be stored in a new row of ImageJs
     * Results table in the column 'Standard_deviation'.
     */
    default double standardDeviationOfAllPixels(ClearCLImageInterface arg1) {
        return StandardDeviationOfAllPixels.standardDeviationOfAllPixels(getCLIJ2(), arg1);
    }

    /**
     * Determines the standard deviation of all pixels in an image. The value will be stored in a new row of ImageJs
     * Results table in the column 'Standard_deviation'.
     */
    default double standardDeviationOfAllPixels(ClearCLImageInterface arg1, double arg2) {
        return StandardDeviationOfAllPixels.standardDeviationOfAllPixels(getCLIJ2(), arg1, new Double (arg2).floatValue());
    }


    // net.haesleinhuepf.clij2.plugins.VarianceOfMaskedPixels
    //----------------------------------------------------
    /**
     * Determines the variance in an image, but only in pixels which have non-zero values in another binary mask image. The result is put in the results table as new column named 'Masked_variance'.
     */
    default double varianceOfMaskedPixels(ClearCLBuffer source, ClearCLBuffer mask) {
        return VarianceOfMaskedPixels.varianceOfMaskedPixels(getCLIJ2(), source, mask);
    }

    /**
     * Determines the variance in an image, but only in pixels which have non-zero values in another binary mask image. The result is put in the results table as new column named 'Masked_variance'.
     */
    default double varianceOfMaskedPixels(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3) {
        return VarianceOfMaskedPixels.varianceOfMaskedPixels(getCLIJ2(), arg1, arg2, new Double (arg3).floatValue());
    }


    // net.haesleinhuepf.clij2.plugins.StandardDeviationOfMaskedPixels
    //----------------------------------------------------
    /**
     * Determines the standard deviation of all pixels in an image which have non-zero value in a corresponding mask image. The value will be stored in a new row of ImageJs
     * Results table in the column 'Masked_standard_deviation'.
     */
    default double standardDeviationOfMaskedPixels(ClearCLBuffer source, ClearCLBuffer mask) {
        return StandardDeviationOfMaskedPixels.standardDeviationOfMaskedPixels(getCLIJ2(), source, mask);
    }

    /**
     * Determines the standard deviation of all pixels in an image which have non-zero value in a corresponding mask image. The value will be stored in a new row of ImageJs
     * Results table in the column 'Masked_standard_deviation'.
     */
    default double standardDeviationOfMaskedPixels(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3) {
        return StandardDeviationOfMaskedPixels.standardDeviationOfMaskedPixels(getCLIJ2(), arg1, arg2, new Double (arg3).floatValue());
    }


    // net.haesleinhuepf.clij2.plugins.ExcludeLabelsOnEdges
    //----------------------------------------------------
    /**
     * Removes all labels from a label map which touch the edges of the image (in X, Y and Z if the image is 3D). Remaining label elements are renumbered afterwards.
     */
    default boolean excludeLabelsOnEdges(ClearCLBuffer label_map_input, ClearCLBuffer label_map_destination) {
        return ExcludeLabelsOnEdges.excludeLabelsOnEdges(getCLIJ2(), label_map_input, label_map_destination);
    }


    // net.haesleinhuepf.clij2.plugins.BinarySubtract
    //----------------------------------------------------
    /**
     * Subtracts one binary image from another.
     */
    default boolean binarySubtract(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLImageInterface arg3) {
        return BinarySubtract.binarySubtract(getCLIJ2(), arg1, arg2, arg3);
    }


    // net.haesleinhuepf.clij2.plugins.BinaryEdgeDetection
    //----------------------------------------------------
    /**
     * Determines pixels/voxels which are on the surface of a binary objects and sets only them to 1 in the destination image. All other pixels are set to 0.
     */
    default boolean binaryEdgeDetection(ClearCLImageInterface arg1, ClearCLImageInterface arg2) {
        return BinaryEdgeDetection.binaryEdgeDetection(getCLIJ2(), arg1, arg2);
    }


    // net.haesleinhuepf.clij2.plugins.DistanceMap
    //----------------------------------------------------
    /**
     * 
     */
    default boolean localPositiveMinimum(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLImageInterface arg3) {
        return DistanceMap.localPositiveMinimum(getCLIJ2(), arg1, arg2, arg3);
    }

    /**
     * Generates a distance map from a binary image. Pixels with non-zero value in the binary image are set to a number representing the distance to the closest zero-value pixel.
     */
    default boolean distanceMap(ClearCLBuffer source, ClearCLBuffer destination) {
        return DistanceMap.distanceMap(getCLIJ2(), source, destination);
    }


    // net.haesleinhuepf.clij2.plugins.PullAsROI
    //----------------------------------------------------
    /**
     * Pulls a binary image from the GPU memory and puts it on the currently active ImageJ window.
     */
    default Roi pullAsROI(ClearCLBuffer binary_input) {
        return PullAsROI.pullAsROI(getCLIJ2(), binary_input);
    }


    // net.haesleinhuepf.clij2.plugins.PullLabelsToROIManager
    //----------------------------------------------------
    /**
     * Pulls all labels in a label map as ROIs to the ROI manager.
     */
    default boolean pullLabelsToROIManager(ClearCLBuffer arg1, RoiManager arg2) {
        return PullLabelsToROIManager.pullLabelsToROIManager(getCLIJ2(), arg1, arg2);
    }

    /**
     * Pulls all labels in a label map as ROIs to the ROI manager.
     */
    default boolean pullLabelsToROIManager(ClearCLBuffer binary_input) {
        return PullLabelsToROIManager.pullLabelsToROIManager(getCLIJ2(), binary_input);
    }


    // net.haesleinhuepf.clij2.plugins.NonzeroMaximumDiamond
    //----------------------------------------------------
    /**
     * Apply a maximum filter (diamond shape) to the input image. The radius is fixed to 1 and pixels with value 0 are ignored.
     */
    default ClearCLKernel nonzeroMaximumDiamond(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLImageInterface arg3, ClearCLKernel arg4) {
        return NonzeroMaximumDiamond.nonzeroMaximumDiamond(getCLIJ2(), arg1, arg2, arg3, arg4);
    }

    /**
     * Apply a maximum filter (diamond shape) to the input image. The radius is fixed to 1 and pixels with value 0 are ignored.
     */
    default boolean nonzeroMaximumDiamond(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLImageInterface arg3) {
        return NonzeroMaximumDiamond.nonzeroMaximumDiamond(getCLIJ2(), arg1, arg2, arg3);
    }


    // net.haesleinhuepf.clij2.plugins.OnlyzeroOverwriteMaximumDiamond
    //----------------------------------------------------
    /**
     * TODO
     */
    default boolean onlyzeroOverwriteMaximumDiamond(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLImageInterface arg3) {
        return OnlyzeroOverwriteMaximumDiamond.onlyzeroOverwriteMaximumDiamond(getCLIJ2(), arg1, arg2, arg3);
    }


    // net.haesleinhuepf.clij2.plugins.OnlyzeroOverwriteMaximumBox
    //----------------------------------------------------
    /**
     * 
     */
    default boolean onlyzeroOverwriteMaximumBox(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLImageInterface arg3) {
        return OnlyzeroOverwriteMaximumBox.onlyzeroOverwriteMaximumBox(getCLIJ2(), arg1, arg2, arg3);
    }


    // net.haesleinhuepf.clij2.plugins.GenerateTouchMatrix
    //----------------------------------------------------
    /**
     * Takes a labelmap with n labels and generates a (n+1)*(n+1) matrix where all pixels are set to 0 exept those where labels are touching.Only half of the matrix is filled (with x < y). For example, if labels 3 and 4 are touching then the pixel (3,4) in the matrix will be set to 1.
     */
    default boolean generateTouchMatrix(ClearCLBuffer label_map, ClearCLBuffer touch_matrix_destination) {
        return GenerateTouchMatrix.generateTouchMatrix(getCLIJ2(), label_map, touch_matrix_destination);
    }


    // net.haesleinhuepf.clij2.plugins.DetectLabelEdges
    //----------------------------------------------------
    /**
     * Takes a labelmap and returns an image where all pixels on label edges are set to 1 and all other pixels to 0.
     */
    default boolean detectLabelEdges(ClearCLBuffer label_map, ClearCLBuffer edge_image_destination) {
        return DetectLabelEdges.detectLabelEdges(getCLIJ2(), label_map, edge_image_destination);
    }


    // net.haesleinhuepf.clij2.plugins.CountTouchingNeighbors
    //----------------------------------------------------
    /**
     * Takes a touching-neighbors-matrix as input and delivers a vector with number of touching neighbors per label as a vector.
     */
    default boolean countTouchingNeighbors(ClearCLBuffer touch_matrix, ClearCLBuffer touching_neighbors_count_destination) {
        return CountTouchingNeighbors.countTouchingNeighbors(getCLIJ2(), touch_matrix, touching_neighbors_count_destination);
    }


    // net.haesleinhuepf.clij2.plugins.ReplaceIntensities
    //----------------------------------------------------
    /**
     * Replaces integer intensities specified in a vector image. The vector image must be 3D with size (m, 1, 1) where m corresponds to the maximum intensity in the original image. Assuming the vector image contains values (0, 1, 0, 2) means: 
     *  * All pixels with value 0 (first entry in the vector image) get value 0
     *  * All pixels with value 1 get value 1
     *  * All pixels with value 2 get value 0
     *  * All pixels with value 3 get value 2
     * 
     */
    default boolean replaceIntensities(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLImageInterface arg3) {
        return ReplaceIntensities.replaceIntensities(getCLIJ2(), arg1, arg2, arg3);
    }


    // net.haesleinhuepf.clij2.plugins.AverageDistanceOfNClosestPoints
    //----------------------------------------------------
    /**
     * 
     */
    default boolean averageDistanceOfClosestPoints(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3) {
        return AverageDistanceOfNClosestPoints.averageDistanceOfClosestPoints(getCLIJ2(), arg1, arg2, new Double (arg3).intValue());
    }

    /**
     * Determine the n point indices with shortest distance for all points in a distance matrix.
     * This corresponds to the n row indices with minimum values for each column of the distance matrix.
     */
    default boolean averageDistanceOfNClosestPoints(ClearCLBuffer distance_matrix, ClearCLBuffer indexlist_destination, double nClosestPointsTofind) {
        return AverageDistanceOfNClosestPoints.averageDistanceOfNClosestPoints(getCLIJ2(), distance_matrix, indexlist_destination, new Double (nClosestPointsTofind).intValue());
    }


    // net.haesleinhuepf.clij2.plugins.TouchMatrixToMesh
    //----------------------------------------------------
    /**
     * Takes a pointlist with dimensions n*d with n point coordinates in d dimensions and a touch matrix of size n*n to draw lines from all points to points if the corresponding pixel in the touch matrix is 1.
     */
    default boolean touchMatrixToMesh(ClearCLBuffer pointlist, ClearCLBuffer touch_matrix, ClearCLBuffer mesh_destination) {
        return TouchMatrixToMesh.touchMatrixToMesh(getCLIJ2(), pointlist, touch_matrix, mesh_destination);
    }


    // net.haesleinhuepf.clij2.plugins.Resample
    //----------------------------------------------------
    /**
     * Resamples an image with given size factors using an affine transform.
     */
    default boolean resample(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4, double arg5, boolean arg6) {
        return Resample.resample(getCLIJ2(), arg1, arg2, new Double (arg3).floatValue(), new Double (arg4).floatValue(), new Double (arg5).floatValue(), arg6);
    }


    // net.haesleinhuepf.clij2.plugins.ResliceRadial
    //----------------------------------------------------
    /**
     * Computes a radial projection of an image stack. Starting point for the line is the center in any 
     * X/Y-plane of a given input image stack. This operation is similar to ImageJs 'Radial Reslice' method but offers less flexibility.
     */
    default boolean resliceRadial(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3, double arg4, double arg5, double arg6, double arg7, double arg8) {
        return ResliceRadial.resliceRadial(getCLIJ2(), arg1, arg2, new Double (arg3).floatValue(), new Double (arg4).floatValue(), new Double (arg5).floatValue(), new Double (arg6).floatValue(), new Double (arg7).floatValue(), new Double (arg8).floatValue());
    }

    /**
     * Computes a radial projection of an image stack. Starting point for the line is the center in any 
     * X/Y-plane of a given input image stack. This operation is similar to ImageJs 'Radial Reslice' method but offers less flexibility.
     */
    default boolean resliceRadial(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3) {
        return ResliceRadial.resliceRadial(getCLIJ2(), arg1, arg2, new Double (arg3).floatValue());
    }

    /**
     * Computes a radial projection of an image stack. Starting point for the line is the center in any 
     * X/Y-plane of a given input image stack. This operation is similar to ImageJs 'Radial Reslice' method but offers less flexibility.
     */
    default boolean resliceRadial(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3, double arg4, double arg5) {
        return ResliceRadial.resliceRadial(getCLIJ2(), arg1, arg2, new Double (arg3).floatValue(), new Double (arg4).floatValue(), new Double (arg5).floatValue());
    }

    /**
     * 
     */
    @Deprecated
    default boolean radialProjection(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3) {
        return ResliceRadial.radialProjection(getCLIJ2(), arg1, arg2, new Double (arg3).floatValue());
    }


    // net.haesleinhuepf.clij2.plugins.Sobel
    //----------------------------------------------------
    /**
     * Convolve the image with the Sobel kernel.
     */
    default boolean sobel(ClearCLBuffer source, ClearCLBuffer destination) {
        return Sobel.sobel(getCLIJ2(), source, destination);
    }


    // net.haesleinhuepf.clij2.plugins.Absolute
    //----------------------------------------------------
    /**
     * Computes the absolute value of every individual pixel x in a given image.
     * 
     * <pre>f(x) = |x| </pre>
     */
    default boolean absolute(ClearCLImageInterface arg1, ClearCLImageInterface arg2) {
        return Absolute.absolute(getCLIJ2(), arg1, arg2);
    }


    // net.haesleinhuepf.clij2.plugins.LaplaceBox
    //----------------------------------------------------
    /**
     * Applies the Laplace operator (Box neighborhood) to an image.
     */
    default boolean laplaceBox(ClearCLBuffer input, ClearCLBuffer destination) {
        return LaplaceBox.laplaceBox(getCLIJ(), input, destination);
    }


    // net.haesleinhuepf.clij2.plugins.BottomHatBox
    //----------------------------------------------------
    /**
     * Apply a bottom-hat filter for background subtraction to the input image.
     */
    default boolean bottomHatBox(ClearCLBuffer input, ClearCLBuffer destination, double radiusX, double radiusY, double radiusZ) {
        return BottomHatBox.bottomHatBox(getCLIJ2(), input, destination, new Double (radiusX).intValue(), new Double (radiusY).intValue(), new Double (radiusZ).intValue());
    }


    // net.haesleinhuepf.clij2.plugins.BottomHatSphere
    //----------------------------------------------------
    /**
     * Applies a bottom-hat filter for background subtraction to the input image.
     */
    default boolean bottomHatSphere(ClearCLBuffer input, ClearCLBuffer destination, double radiusX, double radiusY, double radiusZ) {
        return BottomHatSphere.bottomHatSphere(getCLIJ2(), input, destination, new Double (radiusX).intValue(), new Double (radiusY).intValue(), new Double (radiusZ).intValue());
    }


    // net.haesleinhuepf.clij2.plugins.ClosingBox
    //----------------------------------------------------
    /**
     * Apply a binary closing to the input image by calling n dilations and n erosions subsequenntly.
     */
    default boolean closingBox(ClearCLBuffer input, ClearCLBuffer destination, double number_of_dilations_and_erosions) {
        return ClosingBox.closingBox(getCLIJ2(), input, destination, new Double (number_of_dilations_and_erosions).intValue());
    }


    // net.haesleinhuepf.clij2.plugins.ClosingDiamond
    //----------------------------------------------------
    /**
     * Apply a binary closing to the input image by calling n dilations and n erosions subsequenntly.
     */
    default boolean closingDiamond(ClearCLBuffer input, ClearCLBuffer destination, double number_of_dilations_and_erotions) {
        return ClosingDiamond.closingDiamond(getCLIJ2(), input, destination, new Double (number_of_dilations_and_erotions).intValue());
    }


    // net.haesleinhuepf.clij2.plugins.OpeningBox
    //----------------------------------------------------
    /**
     * Apply a binary opening to the input image by calling n erosions and n dilations subsequenntly.
     */
    default boolean openingBox(ClearCLBuffer input, ClearCLBuffer destination, double number_of_erotions_and_dilations) {
        return OpeningBox.openingBox(getCLIJ2(), input, destination, new Double (number_of_erotions_and_dilations).intValue());
    }


    // net.haesleinhuepf.clij2.plugins.OpeningDiamond
    //----------------------------------------------------
    /**
     * Apply a binary opening to the input image by calling n erosions and n dilations subsequenntly.
     */
    default boolean openingDiamond(ClearCLBuffer input, ClearCLBuffer destination, double number_of_erotions_and_dilations) {
        return OpeningDiamond.openingDiamond(getCLIJ2(), input, destination, new Double (number_of_erotions_and_dilations).intValue());
    }


    // net.haesleinhuepf.clij2.plugins.MaximumXProjection
    //----------------------------------------------------
    /**
     * Determines the maximum projection of an image along X.
     */
    default boolean maximumXProjection(ClearCLImageInterface arg1, ClearCLImageInterface arg2) {
        return MaximumXProjection.maximumXProjection(getCLIJ2(), arg1, arg2);
    }


    // net.haesleinhuepf.clij2.plugins.MaximumYProjection
    //----------------------------------------------------
    /**
     * Determines the maximum projection of an image along X.
     */
    default boolean maximumYProjection(ClearCLImageInterface arg1, ClearCLImageInterface arg2) {
        return MaximumYProjection.maximumYProjection(getCLIJ2(), arg1, arg2);
    }


    // net.haesleinhuepf.clij2.plugins.MaximumZProjectionBounded
    //----------------------------------------------------
    /**
     * 
     */
    default boolean projectMaximumZBounded(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3, double arg4) {
        return MaximumZProjectionBounded.maximumZProjectionBounded(getCLIJ2(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue());
    }


    // net.haesleinhuepf.clij2.plugins.MinimumZProjectionBounded
    //----------------------------------------------------
    /**
     * 
     */
    default boolean projectMinimumZBounded(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3, double arg4) {
        return MinimumZProjectionBounded.minimumZProjectionBounded(getCLIJ2(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue());
    }


    // net.haesleinhuepf.clij2.plugins.MeanZProjectionBounded
    //----------------------------------------------------
    /**
     * 
     */
    default boolean projectMeanZBounded(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3, double arg4) {
        return MeanZProjectionBounded.projectMeanZBounded(getCLIJ(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue());
    }


    // net.haesleinhuepf.clij2.plugins.NonzeroMaximumBox
    //----------------------------------------------------
    /**
     * Apply a maximum filter (box shape) to the input image. The radius is fixed to 1 and pixels with value 0 are ignored.
     */
    default ClearCLKernel nonzeroMaximumBox(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLImageInterface arg3, ClearCLKernel arg4) {
        return NonzeroMaximumBox.nonzeroMaximumBox(getCLIJ2(), arg1, arg2, arg3, arg4);
    }

    /**
     * Apply a maximum filter (box shape) to the input image. The radius is fixed to 1 and pixels with value 0 are ignored.
     */
    default boolean nonzeroMaximumBox(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLImageInterface arg3) {
        return NonzeroMaximumBox.nonzeroMaximumBox(getCLIJ2(), arg1, arg2, arg3);
    }


    // net.haesleinhuepf.clij2.plugins.NonzeroMinimumBox
    //----------------------------------------------------
    /**
     * Apply a minimum filter (box shape) to the input image. The radius is fixed to 1 and pixels with value 0 are ignored.
     */
    default ClearCLKernel nonzeroMinimumBox(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLImageInterface arg3, ClearCLKernel arg4) {
        return NonzeroMinimumBox.nonzeroMinimumBox(getCLIJ2(), arg1, arg2, arg3, arg4);
    }

    /**
     * Apply a minimum filter (box shape) to the input image. The radius is fixed to 1 and pixels with value 0 are ignored.
     */
    default boolean nonzeroMinimumBox(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLImageInterface arg3) {
        return NonzeroMinimumBox.nonzeroMinimumBox(getCLIJ2(), arg1, arg2, arg3);
    }


    // net.haesleinhuepf.clij2.plugins.MinimumZProjectionThresholdedBounded
    //----------------------------------------------------
    /**
     * 
     */
    default boolean projectMinimumThresholdedZBounded(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3, double arg4, double arg5) {
        return MinimumZProjectionThresholdedBounded.minimumZProjectionThresholdedBounded(getCLIJ2(), arg1, arg2, new Double (arg3).floatValue(), new Double (arg4).intValue(), new Double (arg5).intValue());
    }


    // net.haesleinhuepf.clij2.plugins.MeanOfPixelsAboveThreshold
    //----------------------------------------------------
    /**
     * Determines the mean intensity in an image, but only in pixels which are above a given threshold.
     */
    default double meanOfPixelsAboveThreshold(ClearCLBuffer source, double threshold) {
        return MeanOfPixelsAboveThreshold.meanOfPixelsAboveThreshold(getCLIJ2(), source, new Double (threshold).floatValue());
    }


    // net.haesleinhuepf.clij2.plugins.DistanceMatrixToMesh
    //----------------------------------------------------
    /**
     * Takes a pointlist with dimensions n*d with n point coordinates in d dimensions and a distance matrix of size n*n to draw lines from all points to points if the corresponding pixel in the distance matrix is smaller than a given distance threshold.
     */
    default boolean distanceMatrixToMesh(ClearCLBuffer pointlist, ClearCLBuffer distance_matrix, ClearCLBuffer mesh_destination, double maximumDistance) {
        return DistanceMatrixToMesh.distanceMatrixToMesh(getCLIJ2(), pointlist, distance_matrix, mesh_destination, new Double (maximumDistance).floatValue());
    }


    // net.haesleinhuepf.clij2.plugins.PointIndexListToMesh
    //----------------------------------------------------
    /**
     * Meshes all points in a given point list which are indiced in a corresponding index list. TODO: Explain better
     */
    default boolean pointIndexListToMesh(ClearCLBuffer pointlist, ClearCLBuffer indexList, ClearCLBuffer Mesh) {
        return PointIndexListToMesh.pointIndexListToMesh(getCLIJ2(), pointlist, indexList, Mesh);
    }


    // net.haesleinhuepf.clij2.plugins.MinimumOctagon
    //----------------------------------------------------
    /**
     * Applies a minimum filter with kernel size 3x3 n times to an image iteratively. Odd iterations are done with box neighborhood, even iterations with a diamond. Thus, with n > 2, the filter shape is an octagon. The given number of iterations makes the filter result very similar to minimum sphere. Approximately:radius = iterations - 2
     */
    default boolean minimumOctagon(ClearCLBuffer input, ClearCLBuffer destination, double iterations) {
        return MinimumOctagon.minimumOctagon(getCLIJ2(), input, destination, new Double (iterations).intValue());
    }

    /**
     * 
     */
    default ClearCLKernel minimumDiamond(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLKernel arg3) {
        return MinimumOctagon.minimumDiamond(getCLIJ2(), arg1, arg2, arg3);
    }

    /**
     * Computes the local minimum of a pixels rectangular neighborhood. The rectangles size is specified by 
     * its half-width and half-height (radius).
     */
    default ClearCLKernel minimumBox(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLKernel arg3) {
        return MinimumOctagon.minimumBox(getCLIJ2(), arg1, arg2, arg3);
    }


    // net.haesleinhuepf.clij2.plugins.MaximumOctagon
    //----------------------------------------------------
    /**
     * Applies a maximum filter with kernel size 3x3 n times to an image iteratively. Odd iterations are done with box neighborhood, even iterations with a diamond. Thus, with n > 2, the filter shape is an octagon. The given number of iterations makes the filter result very similar to minimum sphere. Approximately:radius = iterations - 2
     */
    default boolean maximumOctagon(ClearCLBuffer input, ClearCLBuffer destination, double iterations) {
        return MaximumOctagon.maximumOctagon(getCLIJ2(), input, destination, new Double (iterations).intValue());
    }

    /**
     * 
     */
    default ClearCLKernel maximumDiamond(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLKernel arg3) {
        return MaximumOctagon.maximumDiamond(getCLIJ2(), arg1, arg2, arg3);
    }

    /**
     * Computes the local maximum of a pixels rectangular neighborhood. The rectangles size is specified by 
     * its half-width and half-height (radius).
     */
    default ClearCLKernel maximumBox(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLKernel arg3) {
        return MaximumOctagon.maximumBox(getCLIJ2(), arg1, arg2, arg3);
    }


    // net.haesleinhuepf.clij2.plugins.AddImages
    //----------------------------------------------------
    /**
     * Calculates the sum of pairs of pixels x and y of two images X and Y.
     * 
     * <pre>f(x, y) = x + y</pre>
     */
    default boolean addImages(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLImageInterface arg3) {
        return AddImages.addImages(getCLIJ2(), arg1, arg2, arg3);
    }


    // net.haesleinhuepf.clij2.plugins.AddImagesWeighted
    //----------------------------------------------------
    /**
     * Calculates the sum of pairs of pixels x and y from images X and Y weighted with factors a and b.
     * 
     * <pre>f(x, y, a, b) = x * a + y * b</pre>
     */
    default boolean addImagesWeighted(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLImageInterface arg3, double arg4, double arg5) {
        return AddImagesWeighted.addImagesWeighted(getCLIJ2(), arg1, arg2, arg3, new Double (arg4).floatValue(), new Double (arg5).floatValue());
    }


    // net.haesleinhuepf.clij2.plugins.SubtractImages
    //----------------------------------------------------
    /**
     * Subtracts one image X from another image Y pixel wise.
     * 
     * <pre>f(x, y) = x - y</pre>
     */
    default boolean subtractImages(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLImageInterface arg3) {
        return SubtractImages.subtractImages(getCLIJ2(), arg1, arg2, arg3);
    }

    /**
     * Subtracts one image X from another image Y pixel wise.
     * 
     * <pre>f(x, y) = x - y</pre>
     */
    @Deprecated
    default boolean subtract(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLImageInterface arg3) {
        return SubtractImages.subtract(getCLIJ2(), arg1, arg2, arg3);
    }


    // net.haesleinhuepf.clij2.plugins.AffineTransform2D
    //----------------------------------------------------
    /**
     * Applies an affine transform to a 2D image. Individual transforms must be separated by spaces.
     * 
     * Supported transforms:
     * * center: translate the coordinate origin to the center of the image
     * * -center: translate the coordinate origin back to the initial origin
     * * rotate=[angle]: rotate in X/Y plane (around Z-axis) by the given angle in degrees
     * * scale=[factor]: isotropic scaling according to given zoom factor
     * * scaleX=[factor]: scaling along X-axis according to given zoom factor
     * * scaleY=[factor]: scaling along Y-axis according to given zoom factor
     * * shearXY=[factor]: shearing along X-axis in XY plane according to given factor
     * * translateX=[distance]: translate along X-axis by distance given in pixels
     * * translateY=[distance]: translate along X-axis by distance given in pixels
     * 
     * Example transform:
     * transform = "center scale=2 rotate=45 -center";
     */
    default boolean affineTransform2D(ClearCLImage arg1, ClearCLImageInterface arg2, float[] arg3) {
        return AffineTransform2D.affineTransform2D(getCLIJ2(), arg1, arg2, arg3);
    }

    /**
     * Applies an affine transform to a 2D image. Individual transforms must be separated by spaces.
     * 
     * Supported transforms:
     * * center: translate the coordinate origin to the center of the image
     * * -center: translate the coordinate origin back to the initial origin
     * * rotate=[angle]: rotate in X/Y plane (around Z-axis) by the given angle in degrees
     * * scale=[factor]: isotropic scaling according to given zoom factor
     * * scaleX=[factor]: scaling along X-axis according to given zoom factor
     * * scaleY=[factor]: scaling along Y-axis according to given zoom factor
     * * shearXY=[factor]: shearing along X-axis in XY plane according to given factor
     * * translateX=[distance]: translate along X-axis by distance given in pixels
     * * translateY=[distance]: translate along X-axis by distance given in pixels
     * 
     * Example transform:
     * transform = "center scale=2 rotate=45 -center";
     */
    default boolean affineTransform2D(ClearCLImage arg1, ClearCLImageInterface arg2, net.imglib2.realtransform.AffineTransform2D arg3) {
        return AffineTransform2D.affineTransform2D(getCLIJ2(), arg1, arg2, arg3);
    }

    /**
     * Applies an affine transform to a 2D image. Individual transforms must be separated by spaces.
     * 
     * Supported transforms:
     * * center: translate the coordinate origin to the center of the image
     * * -center: translate the coordinate origin back to the initial origin
     * * rotate=[angle]: rotate in X/Y plane (around Z-axis) by the given angle in degrees
     * * scale=[factor]: isotropic scaling according to given zoom factor
     * * scaleX=[factor]: scaling along X-axis according to given zoom factor
     * * scaleY=[factor]: scaling along Y-axis according to given zoom factor
     * * shearXY=[factor]: shearing along X-axis in XY plane according to given factor
     * * translateX=[distance]: translate along X-axis by distance given in pixels
     * * translateY=[distance]: translate along X-axis by distance given in pixels
     * 
     * Example transform:
     * transform = "center scale=2 rotate=45 -center";
     */
    default boolean affineTransform2D(ClearCLBuffer arg1, ClearCLBuffer arg2, String arg3) {
        return AffineTransform2D.affineTransform2D(getCLIJ2(), arg1, arg2, arg3);
    }

    /**
     * Applies an affine transform to a 2D image. Individual transforms must be separated by spaces.
     * 
     * Supported transforms:
     * * center: translate the coordinate origin to the center of the image
     * * -center: translate the coordinate origin back to the initial origin
     * * rotate=[angle]: rotate in X/Y plane (around Z-axis) by the given angle in degrees
     * * scale=[factor]: isotropic scaling according to given zoom factor
     * * scaleX=[factor]: scaling along X-axis according to given zoom factor
     * * scaleY=[factor]: scaling along Y-axis according to given zoom factor
     * * shearXY=[factor]: shearing along X-axis in XY plane according to given factor
     * * translateX=[distance]: translate along X-axis by distance given in pixels
     * * translateY=[distance]: translate along X-axis by distance given in pixels
     * 
     * Example transform:
     * transform = "center scale=2 rotate=45 -center";
     */
    default boolean affineTransform2D(ClearCLBuffer arg1, ClearCLBuffer arg2, float[] arg3) {
        return AffineTransform2D.affineTransform2D(getCLIJ2(), arg1, arg2, arg3);
    }

    /**
     * Applies an affine transform to a 2D image. Individual transforms must be separated by spaces.
     * 
     * Supported transforms:
     * * center: translate the coordinate origin to the center of the image
     * * -center: translate the coordinate origin back to the initial origin
     * * rotate=[angle]: rotate in X/Y plane (around Z-axis) by the given angle in degrees
     * * scale=[factor]: isotropic scaling according to given zoom factor
     * * scaleX=[factor]: scaling along X-axis according to given zoom factor
     * * scaleY=[factor]: scaling along Y-axis according to given zoom factor
     * * shearXY=[factor]: shearing along X-axis in XY plane according to given factor
     * * translateX=[distance]: translate along X-axis by distance given in pixels
     * * translateY=[distance]: translate along X-axis by distance given in pixels
     * 
     * Example transform:
     * transform = "center scale=2 rotate=45 -center";
     */
    default boolean affineTransform2D(ClearCLBuffer arg1, ClearCLBuffer arg2, net.imglib2.realtransform.AffineTransform2D arg3) {
        return AffineTransform2D.affineTransform2D(getCLIJ2(), arg1, arg2, arg3);
    }


    // net.haesleinhuepf.clij2.plugins.AffineTransform3D
    //----------------------------------------------------
    /**
     * Applies an affine transform to a 3D image. Individual transforms must be separated by spaces.
     * 
     * Supported transforms:
     * * center: translate the coordinate origin to the center of the image
     * * -center: translate the coordinate origin back to the initial origin
     * * rotate=[angle]: rotate in X/Y plane (around Z-axis) by the given angle in degrees
     * * rotateX=[angle]: rotate in Y/Z plane (around X-axis) by the given angle in degrees
     * * rotateY=[angle]: rotate in X/Z plane (around Y-axis) by the given angle in degrees
     * * rotateZ=[angle]: rotate in X/Y plane (around Z-axis) by the given angle in degrees
     * * scale=[factor]: isotropic scaling according to given zoom factor
     * * scaleX=[factor]: scaling along X-axis according to given zoom factor
     * * scaleY=[factor]: scaling along Y-axis according to given zoom factor
     * * scaleZ=[factor]: scaling along Z-axis according to given zoom factor
     * * shearXY=[factor]: shearing along X-axis in XY plane according to given factor
     * * shearXZ=[factor]: shearing along X-axis in XZ plane according to given factor
     * * shearYX=[factor]: shearing along Y-axis in XY plane according to given factor
     * * shearYZ=[factor]: shearing along Y-axis in YZ plane according to given factor
     * * shearZX=[factor]: shearing along Z-axis in XZ plane according to given factor
     * * shearZY=[factor]: shearing along Z-axis in YZ plane according to given factor
     * * translateX=[distance]: translate along X-axis by distance given in pixels
     * * translateY=[distance]: translate along X-axis by distance given in pixels
     * * translateZ=[distance]: translate along X-axis by distance given in pixels
     * 
     * Example transform:
     * transform = "center scale=2 rotate=45 -center";
     */
    default boolean affineTransform3D(ClearCLImage arg1, ClearCLImageInterface arg2, float[] arg3) {
        return AffineTransform3D.affineTransform3D(getCLIJ2(), arg1, arg2, arg3);
    }

    /**
     * Applies an affine transform to a 3D image. Individual transforms must be separated by spaces.
     * 
     * Supported transforms:
     * * center: translate the coordinate origin to the center of the image
     * * -center: translate the coordinate origin back to the initial origin
     * * rotate=[angle]: rotate in X/Y plane (around Z-axis) by the given angle in degrees
     * * rotateX=[angle]: rotate in Y/Z plane (around X-axis) by the given angle in degrees
     * * rotateY=[angle]: rotate in X/Z plane (around Y-axis) by the given angle in degrees
     * * rotateZ=[angle]: rotate in X/Y plane (around Z-axis) by the given angle in degrees
     * * scale=[factor]: isotropic scaling according to given zoom factor
     * * scaleX=[factor]: scaling along X-axis according to given zoom factor
     * * scaleY=[factor]: scaling along Y-axis according to given zoom factor
     * * scaleZ=[factor]: scaling along Z-axis according to given zoom factor
     * * shearXY=[factor]: shearing along X-axis in XY plane according to given factor
     * * shearXZ=[factor]: shearing along X-axis in XZ plane according to given factor
     * * shearYX=[factor]: shearing along Y-axis in XY plane according to given factor
     * * shearYZ=[factor]: shearing along Y-axis in YZ plane according to given factor
     * * shearZX=[factor]: shearing along Z-axis in XZ plane according to given factor
     * * shearZY=[factor]: shearing along Z-axis in YZ plane according to given factor
     * * translateX=[distance]: translate along X-axis by distance given in pixels
     * * translateY=[distance]: translate along X-axis by distance given in pixels
     * * translateZ=[distance]: translate along X-axis by distance given in pixels
     * 
     * Example transform:
     * transform = "center scale=2 rotate=45 -center";
     */
    default boolean affineTransform3D(ClearCLImage arg1, ClearCLImageInterface arg2, net.imglib2.realtransform.AffineTransform3D arg3) {
        return AffineTransform3D.affineTransform3D(getCLIJ2(), arg1, arg2, arg3);
    }

    /**
     * Applies an affine transform to a 3D image. Individual transforms must be separated by spaces.
     * 
     * Supported transforms:
     * * center: translate the coordinate origin to the center of the image
     * * -center: translate the coordinate origin back to the initial origin
     * * rotate=[angle]: rotate in X/Y plane (around Z-axis) by the given angle in degrees
     * * rotateX=[angle]: rotate in Y/Z plane (around X-axis) by the given angle in degrees
     * * rotateY=[angle]: rotate in X/Z plane (around Y-axis) by the given angle in degrees
     * * rotateZ=[angle]: rotate in X/Y plane (around Z-axis) by the given angle in degrees
     * * scale=[factor]: isotropic scaling according to given zoom factor
     * * scaleX=[factor]: scaling along X-axis according to given zoom factor
     * * scaleY=[factor]: scaling along Y-axis according to given zoom factor
     * * scaleZ=[factor]: scaling along Z-axis according to given zoom factor
     * * shearXY=[factor]: shearing along X-axis in XY plane according to given factor
     * * shearXZ=[factor]: shearing along X-axis in XZ plane according to given factor
     * * shearYX=[factor]: shearing along Y-axis in XY plane according to given factor
     * * shearYZ=[factor]: shearing along Y-axis in YZ plane according to given factor
     * * shearZX=[factor]: shearing along Z-axis in XZ plane according to given factor
     * * shearZY=[factor]: shearing along Z-axis in YZ plane according to given factor
     * * translateX=[distance]: translate along X-axis by distance given in pixels
     * * translateY=[distance]: translate along X-axis by distance given in pixels
     * * translateZ=[distance]: translate along X-axis by distance given in pixels
     * 
     * Example transform:
     * transform = "center scale=2 rotate=45 -center";
     */
    default boolean affineTransform3D(ClearCLBuffer arg1, ClearCLBuffer arg2, String arg3) {
        return AffineTransform3D.affineTransform3D(getCLIJ2(), arg1, arg2, arg3);
    }

    /**
     * Applies an affine transform to a 3D image. Individual transforms must be separated by spaces.
     * 
     * Supported transforms:
     * * center: translate the coordinate origin to the center of the image
     * * -center: translate the coordinate origin back to the initial origin
     * * rotate=[angle]: rotate in X/Y plane (around Z-axis) by the given angle in degrees
     * * rotateX=[angle]: rotate in Y/Z plane (around X-axis) by the given angle in degrees
     * * rotateY=[angle]: rotate in X/Z plane (around Y-axis) by the given angle in degrees
     * * rotateZ=[angle]: rotate in X/Y plane (around Z-axis) by the given angle in degrees
     * * scale=[factor]: isotropic scaling according to given zoom factor
     * * scaleX=[factor]: scaling along X-axis according to given zoom factor
     * * scaleY=[factor]: scaling along Y-axis according to given zoom factor
     * * scaleZ=[factor]: scaling along Z-axis according to given zoom factor
     * * shearXY=[factor]: shearing along X-axis in XY plane according to given factor
     * * shearXZ=[factor]: shearing along X-axis in XZ plane according to given factor
     * * shearYX=[factor]: shearing along Y-axis in XY plane according to given factor
     * * shearYZ=[factor]: shearing along Y-axis in YZ plane according to given factor
     * * shearZX=[factor]: shearing along Z-axis in XZ plane according to given factor
     * * shearZY=[factor]: shearing along Z-axis in YZ plane according to given factor
     * * translateX=[distance]: translate along X-axis by distance given in pixels
     * * translateY=[distance]: translate along X-axis by distance given in pixels
     * * translateZ=[distance]: translate along X-axis by distance given in pixels
     * 
     * Example transform:
     * transform = "center scale=2 rotate=45 -center";
     */
    default boolean affineTransform3D(ClearCLBuffer arg1, ClearCLBuffer arg2, float[] arg3) {
        return AffineTransform3D.affineTransform3D(getCLIJ2(), arg1, arg2, arg3);
    }

    /**
     * Applies an affine transform to a 3D image. Individual transforms must be separated by spaces.
     * 
     * Supported transforms:
     * * center: translate the coordinate origin to the center of the image
     * * -center: translate the coordinate origin back to the initial origin
     * * rotate=[angle]: rotate in X/Y plane (around Z-axis) by the given angle in degrees
     * * rotateX=[angle]: rotate in Y/Z plane (around X-axis) by the given angle in degrees
     * * rotateY=[angle]: rotate in X/Z plane (around Y-axis) by the given angle in degrees
     * * rotateZ=[angle]: rotate in X/Y plane (around Z-axis) by the given angle in degrees
     * * scale=[factor]: isotropic scaling according to given zoom factor
     * * scaleX=[factor]: scaling along X-axis according to given zoom factor
     * * scaleY=[factor]: scaling along Y-axis according to given zoom factor
     * * scaleZ=[factor]: scaling along Z-axis according to given zoom factor
     * * shearXY=[factor]: shearing along X-axis in XY plane according to given factor
     * * shearXZ=[factor]: shearing along X-axis in XZ plane according to given factor
     * * shearYX=[factor]: shearing along Y-axis in XY plane according to given factor
     * * shearYZ=[factor]: shearing along Y-axis in YZ plane according to given factor
     * * shearZX=[factor]: shearing along Z-axis in XZ plane according to given factor
     * * shearZY=[factor]: shearing along Z-axis in YZ plane according to given factor
     * * translateX=[distance]: translate along X-axis by distance given in pixels
     * * translateY=[distance]: translate along X-axis by distance given in pixels
     * * translateZ=[distance]: translate along X-axis by distance given in pixels
     * 
     * Example transform:
     * transform = "center scale=2 rotate=45 -center";
     */
    default boolean affineTransform3D(ClearCLBuffer arg1, ClearCLBuffer arg2, net.imglib2.realtransform.AffineTransform3D arg3) {
        return AffineTransform3D.affineTransform3D(getCLIJ2(), arg1, arg2, arg3);
    }


    // net.haesleinhuepf.clij2.plugins.ApplyVectorField2D
    //----------------------------------------------------
    /**
     * Deforms an image according to distances provided in the given vector images. It is recommended to use 32-bit images for input, output and vector images. 
     */
    default boolean applyVectorfield2D(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLImageInterface arg3, ClearCLImageInterface arg4) {
        return ApplyVectorField2D.applyVectorfield2D(getCLIJ2(), arg1, arg2, arg3, arg4);
    }

    /**
     * Deforms an image according to distances provided in the given vector images. It is recommended to use 32-bit images for input, output and vector images. 
     */
    default boolean applyVectorfield(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLImageInterface arg3, ClearCLImageInterface arg4) {
        return ApplyVectorField2D.applyVectorfield(getCLIJ2(), arg1, arg2, arg3, arg4);
    }


    // net.haesleinhuepf.clij2.plugins.ApplyVectorField3D
    //----------------------------------------------------
    /**
     * Deforms an image stack according to distances provided in the given vector image stacks. It is recommended to use 32-bit image stacks for input, output and vector image stacks. 
     */
    default boolean applyVectorfield3D(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLImageInterface arg3, ClearCLImageInterface arg4, ClearCLImageInterface arg5) {
        return ApplyVectorField3D.applyVectorfield3D(getCLIJ2(), arg1, arg2, arg3, arg4, arg5);
    }

    /**
     * Deforms an image according to distances provided in the given vector images. It is recommended to use 32-bit images for input, output and vector images. 
     */
    default boolean applyVectorfield(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLImageInterface arg3, ClearCLImageInterface arg4, ClearCLImageInterface arg5) {
        return ApplyVectorField3D.applyVectorfield(getCLIJ2(), arg1, arg2, arg3, arg4, arg5);
    }


    // net.haesleinhuepf.clij2.plugins.ArgMaximumZProjection
    //----------------------------------------------------
    /**
     * Determines the maximum projection of an image stack along Z.
     * Furthermore, another 2D image is generated with pixels containing the z-index where the maximum was found (zero based).
     */
    default boolean argMaximumZProjection(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLImageInterface arg3) {
        return ArgMaximumZProjection.argMaximumZProjection(getCLIJ2(), arg1, arg2, arg3);
    }


    // net.haesleinhuepf.clij2.plugins.Histogram
    //----------------------------------------------------
    /**
     * Determines the histogram of a given image.
     */
    default boolean histogram(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3, double arg4, double arg5, boolean arg6) {
        return Histogram.histogram(getCLIJ2(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).floatValue(), new Double (arg5).floatValue(), arg6);
    }

    /**
     * Determines the histogram of a given image.
     */
    default float[] histogram(ClearCLBuffer arg1, double arg2, double arg3, double arg4) {
        return Histogram.histogram(getCLIJ2(), arg1, new Double (arg2).floatValue(), new Double (arg3).floatValue(), new Double (arg4).intValue());
    }

    /**
     * Determines the histogram of a given image.
     */
    default boolean histogram(ClearCLBuffer arg1, ClearCLBuffer arg2) {
        return Histogram.histogram(getCLIJ2(), arg1, arg2);
    }

    /**
     * Determines the histogram of a given image.
     */
    default boolean histogram(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3, double arg4, double arg5, boolean arg6, boolean arg7) {
        return Histogram.histogram(getCLIJ2(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).floatValue(), new Double (arg5).floatValue(), arg6, arg7);
    }

    /**
     * Determines the histogram of a given image.
     */
    default ClearCLBuffer histogram(ClearCLBuffer arg1) {
        return Histogram.histogram(getCLIJ2(), arg1);
    }

    /**
     * 
     */
    @Deprecated
    default boolean fillHistogram(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3, double arg4) {
        return Histogram.fillHistogram(getCLIJ2(), arg1, arg2, new Double (arg3).floatValue(), new Double (arg4).floatValue());
    }


    // net.haesleinhuepf.clij2.plugins.AutomaticThreshold
    //----------------------------------------------------
    /**
     * The automatic thresholder utilizes the threshold methods from ImageJ on a histogram determined on 
     * the GPU to create binary images as similar as possible to ImageJ 'Apply Threshold' method. Enter one 
     * of these methods in the method text field:
     * [Default, Huang, Intermodes, IsoData, IJ_IsoData, Li, MaxEntropy, Mean, MinError, Minimum, Moments, Otsu, Percentile, RenyiEntropy, Shanbhag, Triangle, Yen]
     */
    default boolean automaticThreshold(ClearCLBuffer arg1, ClearCLBuffer arg2, String arg3, double arg4, double arg5, double arg6) {
        return AutomaticThreshold.automaticThreshold(getCLIJ2(), arg1, arg2, arg3, new Double (arg4).floatValue(), new Double (arg5).floatValue(), new Double (arg6).intValue());
    }

    /**
     * The automatic thresholder utilizes the threshold methods from ImageJ on a histogram determined on 
     * the GPU to create binary images as similar as possible to ImageJ 'Apply Threshold' method. Enter one 
     * of these methods in the method text field:
     * [Default, Huang, Intermodes, IsoData, IJ_IsoData, Li, MaxEntropy, Mean, MinError, Minimum, Moments, Otsu, Percentile, RenyiEntropy, Shanbhag, Triangle, Yen]
     */
    default boolean automaticThreshold(ClearCLBuffer arg1, ClearCLBuffer arg2, String arg3) {
        return AutomaticThreshold.automaticThreshold(getCLIJ2(), arg1, arg2, arg3);
    }


    // net.haesleinhuepf.clij2.plugins.Threshold
    //----------------------------------------------------
    /**
     * Computes a binary image with pixel values 0 and 1. All pixel values x of a given input image with 
     * value larger or equal to a given threshold t will be set to 1.
     * 
     * f(x,t) = (1 if (x >= t); (0 otherwise))
     * 
     * This plugin is comparable to setting a raw threshold in ImageJ and using the 'Convert to Mask' menu.
     */
    default boolean threshold(ClearCLBuffer source, ClearCLBuffer destination, double threshold) {
        return Threshold.threshold(getCLIJ2(), source, destination, new Double (threshold).floatValue());
    }


    // net.haesleinhuepf.clij2.plugins.BinaryOr
    //----------------------------------------------------
    /**
     * Computes a binary image (containing pixel values 0 and 1) from two images X and Y by connecting pairs of
     * pixels x and y with the binary OR operator |.
     * All pixel values except 0 in the input images are interpreted as 1.<pre>f(x, y) = x | y</pre>
     */
    default boolean binaryOr(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLImageInterface arg3) {
        return BinaryOr.binaryOr(getCLIJ2(), arg1, arg2, arg3);
    }


    // net.haesleinhuepf.clij2.plugins.BinaryAnd
    //----------------------------------------------------
    /**
     * Computes a binary image (containing pixel values 0 and 1) from two images X and Y by connecting pairs of
     * pixels x and y with the binary AND operator &.
     * All pixel values except 0 in the input images are interpreted as 1.
     * 
     * <pre>f(x, y) = x & y</pre>
     */
    default boolean binaryAnd(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLImageInterface arg3) {
        return BinaryAnd.binaryAnd(getCLIJ2(), arg1, arg2, arg3);
    }


    // net.haesleinhuepf.clij2.plugins.BinaryXOr
    //----------------------------------------------------
    /**
     * Computes a binary image (containing pixel values 0 and 1) from two images X and Y by connecting pairs of
     * pixels x and y with the binary operators AND &, OR | and NOT ! implementing the XOR operator.
     * All pixel values except 0 in the input images are interpreted as 1.
     * 
     * <pre>f(x, y) = (x & !y) | (!x & y)</pre>
     */
    default boolean binaryXOr(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLImageInterface arg3) {
        return BinaryXOr.binaryXOr(getCLIJ2(), arg1, arg2, arg3);
    }


    // net.haesleinhuepf.clij2.plugins.BinaryNot
    //----------------------------------------------------
    /**
     * Computes a binary image (containing pixel values 0 and 1) from an image X by negating its pixel values
     * x using the binary NOT operator !
     * All pixel values except 0 in the input image are interpreted as 1.
     * 
     * <pre>f(x) = !x</pre>
     */
    default boolean binaryNot(ClearCLImageInterface arg1, ClearCLImageInterface arg2) {
        return BinaryNot.binaryNot(getCLIJ2(), arg1, arg2);
    }


    // net.haesleinhuepf.clij2.plugins.ErodeSphere
    //----------------------------------------------------
    /**
     * Computes a binary image with pixel values 0 and 1 containing the binary erosion of a given input image.
     * The erosion takes the von-Neumann-neighborhood (4 pixels in 2D and 6 pixels in 3d) into account.
     * The pixels in the input image with pixel value not equal to 0 will be interpreted as 1.
     */
    default boolean erodeSphere(ClearCLImageInterface arg1, ClearCLImageInterface arg2) {
        return ErodeSphere.erodeSphere(getCLIJ2(), arg1, arg2);
    }


    // net.haesleinhuepf.clij2.plugins.ErodeBox
    //----------------------------------------------------
    /**
     * Computes a binary image with pixel values 0 and 1 containing the binary erosion of a given input image.
     * The erosion takes the Moore-neighborhood (8 pixels in 2D and 26 pixels in 3d) into account.
     * The pixels in the input image with pixel value not equal to 0 will be interpreted as 1.
     * 
     * This method is comparable to the 'Erode' menu in ImageJ in case it is applied to a 2D image. The only
     * difference is that the output image contains values 0 and 1 instead of 0 and 255.
     */
    default boolean erodeBox(ClearCLImageInterface arg1, ClearCLImageInterface arg2) {
        return ErodeBox.erodeBox(getCLIJ2(), arg1, arg2);
    }


    // net.haesleinhuepf.clij2.plugins.ErodeSphereSliceBySlice
    //----------------------------------------------------
    /**
     * Computes a binary image with pixel values 0 and 1 containing the binary erosion of a given input image.
     * The erosion takes the von-Neumann-neighborhood (4 pixels in 2D and 6 pixels in 3d) into account.
     * The pixels in the input image with pixel value not equal to 0 will be interpreted as 1.
     * 
     * This filter is applied slice by slice in 2D.
     */
    default boolean erodeSphereSliceBySlice(ClearCLImageInterface arg1, ClearCLImageInterface arg2) {
        return ErodeSphereSliceBySlice.erodeSphereSliceBySlice(getCLIJ2(), arg1, arg2);
    }


    // net.haesleinhuepf.clij2.plugins.ErodeBoxSliceBySlice
    //----------------------------------------------------
    /**
     * Computes a binary image with pixel values 0 and 1 containing the binary erosion of a given input image.
     * The erosion takes the Moore-neighborhood (8 pixels in 2D and 26 pixels in 3d) into account.
     * The pixels in the input image with pixel value not equal to 0 will be interpreted as 1.
     * 
     * This method is comparable to the 'Erode' menu in ImageJ in case it is applied to a 2D image. The only
     * difference is that the output image contains values 0 and 1 instead of 0 and 255.
     * 
     * This filter is applied slice by slice in 2D.
     */
    default boolean erodeBoxSliceBySlice(ClearCLImageInterface arg1, ClearCLImageInterface arg2) {
        return ErodeBoxSliceBySlice.erodeBoxSliceBySlice(getCLIJ2(), arg1, arg2);
    }


    // net.haesleinhuepf.clij2.plugins.DilateSphere
    //----------------------------------------------------
    /**
     * Computes a binary image with pixel values 0 and 1 containing the binary dilation of a given input image.
     * The dilation takes the von-Neumann-neighborhood (4 pixels in 2D and 6 pixels in 3d) into account.
     * The pixels in the input image with pixel value not equal to 0 will be interpreted as 1.
     */
    default boolean dilateSphere(ClearCLImageInterface arg1, ClearCLImageInterface arg2) {
        return DilateSphere.dilateSphere(getCLIJ2(), arg1, arg2);
    }


    // net.haesleinhuepf.clij2.plugins.DilateBox
    //----------------------------------------------------
    /**
     * Computes a binary image with pixel values 0 and 1 containing the binary dilation of a given input image.
     * The dilation takes the Moore-neighborhood (8 pixels in 2D and 26 pixels in 3d) into account.
     * The pixels in the input image with pixel value not equal to 0 will be interpreted as 1.
     * 
     * This method is comparable to the 'Dilate' menu in ImageJ in case it is applied to a 2D image. The only
     * difference is that the output image contains values 0 and 1 instead of 0 and 255.
     */
    default boolean dilateBox(ClearCLImageInterface arg1, ClearCLImageInterface arg2) {
        return DilateBox.dilateBox(getCLIJ2(), arg1, arg2);
    }


    // net.haesleinhuepf.clij2.plugins.DilateSphereSliceBySlice
    //----------------------------------------------------
    /**
     * Computes a binary image with pixel values 0 and 1 containing the binary dilation of a given input image.
     * The dilation takes the von-Neumann-neighborhood (4 pixels in 2D and 6 pixels in 3d) into account.
     * The pixels in the input image with pixel value not equal to 0 will be interpreted as 1.
     * 
     * This filter is applied slice by slice in 2D.
     */
    default boolean dilateSphereSliceBySlice(ClearCLImageInterface arg1, ClearCLImageInterface arg2) {
        return DilateSphereSliceBySlice.dilateSphereSliceBySlice(getCLIJ2(), arg1, arg2);
    }


    // net.haesleinhuepf.clij2.plugins.DilateBoxSliceBySlice
    //----------------------------------------------------
    /**
     * Computes a binary image with pixel values 0 and 1 containing the binary dilation of a given input image.
     * The dilation takes the Moore-neighborhood (8 pixels in 2D and 26 pixels in 3d) into account.
     * The pixels in the input image with pixel value not equal to 0 will be interpreted as 1.
     * 
     * This method is comparable to the 'Dilate' menu in ImageJ in case it is applied to a 2D image. The only
     * difference is that the output image contains values 0 and 1 instead of 0 and 255.
     * 
     * This filter is applied slice by slice in 2D.
     */
    default boolean dilateBoxSliceBySlice(ClearCLImageInterface arg1, ClearCLImageInterface arg2) {
        return DilateBoxSliceBySlice.dilateBoxSliceBySlice(getCLIJ2(), arg1, arg2);
    }


    // net.haesleinhuepf.clij2.plugins.Copy
    //----------------------------------------------------
    /**
     * Copies an image.
     * 
     * <pre>f(x) = x</pre>
     */
    default boolean copy(ClearCLImageInterface arg1, ClearCLImageInterface arg2) {
        return Copy.copy(getCLIJ2(), arg1, arg2);
    }


    // net.haesleinhuepf.clij2.plugins.CopySlice
    //----------------------------------------------------
    /**
     * This method has two purposes: 
     * It copies a 2D image to a given slice z position in a 3D image stack or 
     * It copies a given slice at position z in an image stack to a 2D image.
     * 
     * The first case is only available via ImageJ macro. If you are using it, it is recommended that the 
     * target 3D image already pre-exists in GPU memory before calling this method. Otherwise, CLIJ create 
     * the image stack with z planes.
     */
    default boolean copySlice(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3) {
        return CopySlice.copySlice(getCLIJ2(), arg1, arg2, new Double (arg3).intValue());
    }


    // net.haesleinhuepf.clij2.plugins.Crop2D
    //----------------------------------------------------
    /**
     * Crops a given rectangle out of a given image.
     * 
     * Note: If the destination image pre-exists already, it will be overwritten and keep it's dimensions.
     */
    default boolean crop(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4) {
        return Crop2D.crop(getCLIJ2(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue());
    }

    /**
     * Crops a given rectangle out of a given image.
     * 
     * Note: If the destination image pre-exists already, it will be overwritten and keep it's dimensions.
     */
    default boolean crop2D(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4) {
        return Crop2D.crop2D(getCLIJ2(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue());
    }


    // net.haesleinhuepf.clij2.plugins.Crop3D
    //----------------------------------------------------
    /**
     * Crops a given rectangle out of a given image.
     * 
     * Note: If the destination image pre-exists already, it will be overwritten and keep it's dimensions.
     */
    default boolean crop(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4, double arg5) {
        return Crop3D.crop(getCLIJ2(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue(), new Double (arg5).intValue());
    }

    /**
     * Crops a given sub-stack out of a given image stack.
     * 
     * Note: If the destination image pre-exists already, it will be overwritten and keep it's dimensions.
     */
    default boolean crop3D(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4, double arg5) {
        return Crop3D.crop3D(getCLIJ2(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue(), new Double (arg5).intValue());
    }


    // net.haesleinhuepf.clij2.plugins.Set
    //----------------------------------------------------
    /**
     * Sets all pixel values x of a given image X to a constant value v.
     * 
     * <pre>f(x) = v</pre>
     */
    default boolean set(ClearCLImageInterface arg1, double arg2) {
        return Set.set(getCLIJ2(), arg1, new Double (arg2).floatValue());
    }


    // net.haesleinhuepf.clij2.plugins.Flip2D
    //----------------------------------------------------
    /**
     * Flips an image in X and/or Y direction depending on boolean flags.
     */
    default boolean flip(ClearCLImageInterface arg1, ClearCLImageInterface arg2, boolean arg3, boolean arg4) {
        return Flip2D.flip(getCLIJ2(), arg1, arg2, arg3, arg4);
    }

    /**
     * Flips an image in X and/or Y direction depending on boolean flags.
     */
    default boolean flip2D(ClearCLImageInterface arg1, ClearCLImageInterface arg2, boolean arg3, boolean arg4) {
        return Flip2D.flip2D(getCLIJ2(), arg1, arg2, arg3, arg4);
    }


    // net.haesleinhuepf.clij2.plugins.Flip3D
    //----------------------------------------------------
    /**
     * Flips an image in X and/or Y direction depending on boolean flags.
     */
    default boolean flip(ClearCLImageInterface arg1, ClearCLImageInterface arg2, boolean arg3, boolean arg4, boolean arg5) {
        return Flip3D.flip(getCLIJ2(), arg1, arg2, arg3, arg4, arg5);
    }

    /**
     * Flips an image in X, Y and/or Z direction depending on boolean flags.
     */
    default boolean flip3D(ClearCLImageInterface arg1, ClearCLImageInterface arg2, boolean arg3, boolean arg4, boolean arg5) {
        return Flip3D.flip3D(getCLIJ2(), arg1, arg2, arg3, arg4, arg5);
    }


    // net.haesleinhuepf.clij2.plugins.RotateCounterClockwise
    //----------------------------------------------------
    /**
     * Rotates a given input image by 90 degrees counter-clockwise. For that, X and Y axis of an image stack
     * are flipped. This operation is similar to ImageJs 'Reslice [/]' method but offers less flexibility 
     * such as interpolation.
     */
    @Deprecated
    default boolean rotateLeft(ClearCLImageInterface arg1, ClearCLImageInterface arg2) {
        return RotateCounterClockwise.rotateLeft(getCLIJ2(), arg1, arg2);
    }

    /**
     * Rotates a given input image by 90 degrees counter-clockwise. For that, X and Y axis of an image stack
     * are flipped. This operation is similar to ImageJs 'Reslice [/]' method but offers less flexibility 
     * such as interpolation.
     */
    default boolean rotateCounterClockwise(ClearCLImageInterface arg1, ClearCLImageInterface arg2) {
        return RotateCounterClockwise.rotateCounterClockwise(getCLIJ2(), arg1, arg2);
    }


    // net.haesleinhuepf.clij2.plugins.RotateClockwise
    //----------------------------------------------------
    /**
     * Rotates a given input image by 90 degrees clockwise. For that, X and Y axis of an image stack
     * are flipped. This operation is similar to ImageJs 'Reslice [/]' method but offers less flexibility 
     * such as interpolation.
     */
    default boolean rotateRight(ClearCLImageInterface arg1, ClearCLImageInterface arg2) {
        return RotateClockwise.rotateRight(getCLIJ2(), arg1, arg2);
    }

    /**
     * 
     */
    @Deprecated
    default boolean rotatedRight(ClearCLImageInterface arg1, ClearCLImageInterface arg2) {
        return RotateClockwise.rotatedRight(getCLIJ2(), arg1, arg2);
    }

    /**
     * Rotates a given input image by 90 degrees clockwise. For that, X and Y axis of an image stack
     * are flipped. This operation is similar to ImageJs 'Reslice [/]' method but offers less flexibility 
     * such as interpolation.
     */
    default boolean rotateClockwise(ClearCLImageInterface arg1, ClearCLImageInterface arg2) {
        return RotateClockwise.rotateClockwise(getCLIJ2(), arg1, arg2);
    }


    // net.haesleinhuepf.clij2.plugins.Mask
    //----------------------------------------------------
    /**
     * Computes a masked image by applying a mask to an image. All pixel values x of image X will be copied
     * to the destination image in case pixel value m at the same position in the mask image is not equal to 
     * zero.
     * 
     * <pre>f(x,m) = (x if (m != 0); (0 otherwise))</pre>
     */
    default boolean mask(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLImageInterface arg3) {
        return Mask.mask(getCLIJ2(), arg1, arg2, arg3);
    }


    // net.haesleinhuepf.clij2.plugins.MaskStackWithPlane
    //----------------------------------------------------
    /**
     * Computes a masked image by applying a 2D mask to an image stack. All pixel values x of image X will be copied
     * to the destination image in case pixel value m at the same spatial position in the mask image is not equal to 
     * zero.
     * 
     * <pre>f(x,m) = (x if (m != 0); (0 otherwise))</pre>
     */
    default boolean maskStackWithPlane(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLImageInterface arg3) {
        return MaskStackWithPlane.maskStackWithPlane(getCLIJ2(), arg1, arg2, arg3);
    }


    // net.haesleinhuepf.clij2.plugins.MaximumZProjection
    //----------------------------------------------------
    /**
     * Determines the maximum projection of an image along Z.
     */
    default boolean maximumZProjection(ClearCLImageInterface arg1, ClearCLImageInterface arg2) {
        return MaximumZProjection.maximumZProjection(getCLIJ2(), arg1, arg2);
    }


    // net.haesleinhuepf.clij2.plugins.MeanZProjection
    //----------------------------------------------------
    /**
     * Determines the mean average projection of an image along Z.
     */
    default boolean meanZProjection(ClearCLImageInterface arg1, ClearCLImageInterface arg2) {
        return MeanZProjection.meanZProjection(getCLIJ2(), arg1, arg2);
    }


    // net.haesleinhuepf.clij2.plugins.MinimumZProjection
    //----------------------------------------------------
    /**
     * Determines the minimum projection of an image along Z.
     */
    default boolean minimumZProjection(ClearCLImageInterface arg1, ClearCLImageInterface arg2) {
        return MinimumZProjection.minimumZProjection(getCLIJ2(), arg1, arg2);
    }


    // net.haesleinhuepf.clij2.plugins.Power
    //----------------------------------------------------
    /**
     * Computes all pixels value x to the power of a given exponent a.
     * 
     * <pre>f(x, a) = x ^ a</pre>
     */
    default boolean power(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3) {
        return Power.power(getCLIJ2(), arg1, arg2, new Double (arg3).floatValue());
    }


    // net.haesleinhuepf.clij2.plugins.DivideImages
    //----------------------------------------------------
    /**
     * Divides two images X and Y by each other pixel wise.
     * 
     * <pre>f(x, y) = x / y</pre>
     */
    default boolean divideImages(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLImageInterface arg3) {
        return DivideImages.divideImages(getCLIJ2(), arg1, arg2, arg3);
    }


    // net.haesleinhuepf.clij2.plugins.MaximumImages
    //----------------------------------------------------
    /**
     * Computes the maximum of a pair of pixel values x, y from two given images X and Y.
     * 
     * <pre>f(x, y) = max(x, y)</pre>
     */
    default boolean maximumImages(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLImageInterface arg3) {
        return MaximumImages.maximumImages(getCLIJ2(), arg1, arg2, arg3);
    }


    // net.haesleinhuepf.clij2.plugins.MaximumImageAndScalar
    //----------------------------------------------------
    /**
     * Computes the maximum of a constant scalar s and each pixel value x in a given image X.
     * 
     * <pre>f(x, s) = max(x, s)</pre>
     */
    default boolean maximumImageAndScalar(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3) {
        return MaximumImageAndScalar.maximumImageAndScalar(getCLIJ2(), arg1, arg2, new Double (arg3).floatValue());
    }


    // net.haesleinhuepf.clij2.plugins.MinimumImages
    //----------------------------------------------------
    /**
     * Computes the minimum of a pair of pixel values x, y from two given images X and Y.
     * 
     * <pre>f(x, y) = min(x, y)</pre>
     */
    default boolean minimumImages(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLImageInterface arg3) {
        return MinimumImages.minimumImages(getCLIJ2(), arg1, arg2, arg3);
    }


    // net.haesleinhuepf.clij2.plugins.MinimumImageAndScalar
    //----------------------------------------------------
    /**
     * Computes the maximum of a constant scalar s and each pixel value x in a given image X.
     * 
     * <pre>f(x, s) = min(x, s)</pre>
     */
    default boolean minimumImageAndScalar(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3) {
        return MinimumImageAndScalar.minimumImageAndScalar(getCLIJ2(), arg1, arg2, new Double (arg3).floatValue());
    }


    // net.haesleinhuepf.clij2.plugins.MultiplyImageAndScalar
    //----------------------------------------------------
    /**
     * Multiplies all pixels value x in a given image X with a constant scalar s.
     * 
     * <pre>f(x, s) = x * s</pre>
     */
    default boolean multiplyImageAndScalar(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3) {
        return MultiplyImageAndScalar.multiplyImageAndScalar(getCLIJ2(), arg1, arg2, new Double (arg3).floatValue());
    }


    // net.haesleinhuepf.clij2.plugins.MultiplyStackWithPlane
    //----------------------------------------------------
    /**
     * Multiplies all pairs of pixel values x and y from an image stack X and a 2D image Y. x and y are at 
     * the same spatial position within a plane.
     * 
     * <pre>f(x, y) = x * y</pre>
     */
    default boolean multiplyStackWithPlane(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLImageInterface arg3) {
        return MultiplyStackWithPlane.multiplyStackWithPlane(getCLIJ2(), arg1, arg2, arg3);
    }


    // net.haesleinhuepf.clij2.plugins.CountNonZeroPixels2DSphere
    //----------------------------------------------------
    /**
     * Counts non-zero pixels in a sphere around every pixel.Put the number in the result image.
     */
    default boolean countNonZeroPixels2DSphere(ClearCLBuffer source, ClearCLBuffer destination, double radiusX, double radiusY) {
        return CountNonZeroPixels2DSphere.countNonZeroPixels2DSphere(getCLIJ2(), source, destination, new Double (radiusX).intValue(), new Double (radiusY).intValue());
    }

    /**
     * 
     */
    @Deprecated
    default boolean countNonZeroPixelsLocally(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3, double arg4) {
        return CountNonZeroPixels2DSphere.countNonZeroPixelsLocally(getCLIJ2(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue());
    }


    // net.haesleinhuepf.clij2.plugins.CountNonZeroPixelsSliceBySliceSphere
    //----------------------------------------------------
    /**
     * 
     */
    @Deprecated
    default boolean countNonZeroPixelsLocallySliceBySlice(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3, double arg4) {
        return CountNonZeroPixelsSliceBySliceSphere.countNonZeroPixelsLocallySliceBySlice(getCLIJ2(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue());
    }

    /**
     * Counts non-zero pixels in a sphere around every pixel slice by slice in a stack and puts the resulting number in the destination image stack.
     */
    default boolean countNonZeroPixelsSliceBySliceSphere(ClearCLBuffer source, ClearCLBuffer destination, double radiusX, double radiusY) {
        return CountNonZeroPixelsSliceBySliceSphere.countNonZeroPixelsSliceBySliceSphere(getCLIJ2(), source, destination, new Double (radiusX).intValue(), new Double (radiusY).intValue());
    }


    // net.haesleinhuepf.clij2.plugins.CountNonZeroVoxels3DSphere
    //----------------------------------------------------
    /**
     * Counts non-zero voxels in a sphere around every voxel.Put the number in the result image.
     */
    default boolean countNonZeroVoxels3DSphere(ClearCLBuffer source, ClearCLBuffer destination, double radiusX, double radiusY, double radiusZ) {
        return CountNonZeroVoxels3DSphere.countNonZeroVoxels3DSphere(getCLIJ2(), source, destination, new Double (radiusX).intValue(), new Double (radiusY).intValue(), new Double (radiusZ).intValue());
    }

    /**
     * 
     */
    @Deprecated
    default boolean countNonZeroVoxelsLocally(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3, double arg4, double arg5) {
        return CountNonZeroVoxels3DSphere.countNonZeroVoxelsLocally(getCLIJ2(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue(), new Double (arg5).intValue());
    }


    // net.haesleinhuepf.clij2.plugins.SumZProjection
    //----------------------------------------------------
    /**
     * Determines the sum projection of an image along Z.
     */
    default boolean sumZProjection(ClearCLImageInterface arg1, ClearCLImageInterface arg2) {
        return SumZProjection.sumZProjection(getCLIJ2(), arg1, arg2);
    }


    // net.haesleinhuepf.clij2.plugins.SumOfAllPixels
    //----------------------------------------------------
    /**
     * Determines the sum of all pixels in a given image. It will be stored in a new row of ImageJs
     * Results table in the column 'Sum'.
     */
    default double sumOfAllPixels(ClearCLImageInterface arg1) {
        return SumOfAllPixels.sumOfAllPixels(getCLIJ2(), arg1);
    }

    /**
     * Determines the sum of all pixels in a given image. It will be stored in a new row of ImageJs
     * Results table in the column 'Sum'.
     */
    @Deprecated
    default double sumPixels(ClearCLImageInterface arg1) {
        return SumOfAllPixels.sumPixels(getCLIJ2(), arg1);
    }


    // net.haesleinhuepf.clij2.plugins.CenterOfMass
    //----------------------------------------------------
    /**
     * Determines the center of mass of an image or image stack and writes the result in the results table
     * in the columns MassX, MassY and MassZ.
     */
    default double[] centerOfMass(ClearCLBuffer source) {
        return CenterOfMass.centerOfMass(getCLIJ2(), source);
    }


    // net.haesleinhuepf.clij2.plugins.Invert
    //----------------------------------------------------
    /**
     * Computes the negative value of all pixels in a given image. It is recommended to convert images to 
     * 32-bit float before applying this operation.
     * 
     * <pre>f(x) = - x</pre>
     * 
     * For binary images, use binaryNot.
     */
    default boolean invert(ClearCLImageInterface arg1, ClearCLImageInterface arg2) {
        return Invert.invert(getCLIJ2(), arg1, arg2);
    }


    // net.haesleinhuepf.clij2.plugins.DownsampleSliceBySliceHalfMedian
    //----------------------------------------------------
    /**
     * Scales an image using scaling factors 0.5 for X and Y dimensions. The Z dimension stays untouched. Thus, each slice is processed separately.
     * The median method is applied. Thus, each pixel value in the destination image equals to the median of
     * four corresponding pixels in the source image.
     */
    default boolean downsampleSliceBySliceHalfMedian(ClearCLImageInterface arg1, ClearCLImageInterface arg2) {
        return DownsampleSliceBySliceHalfMedian.downsampleSliceBySliceHalfMedian(getCLIJ2(), arg1, arg2);
    }


    // net.haesleinhuepf.clij2.plugins.LocalThreshold
    //----------------------------------------------------
    /**
     * Computes a binary image with pixel values 0 and 1 depending on if a pixel value x in image X 
     * was above of equal to the pixel value m in mask image M.
     * 
     * <pre>f(x) = (1 if (x >=  m)); (0 otherwise)</pre>
     */
    default boolean localThreshold(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLImageInterface arg3) {
        return LocalThreshold.localThreshold(getCLIJ2(), arg1, arg2, arg3);
    }


    // net.haesleinhuepf.clij2.plugins.GradientX
    //----------------------------------------------------
    /**
     * Computes the gradient of gray values along X. Assuming a, b and c are three adjacent
     *  pixels in X direction. In the target image will be saved as: <pre>b' = c - a;</pre>
     */
    default boolean gradientX(ClearCLBuffer source, ClearCLBuffer destination) {
        return GradientX.gradientX(getCLIJ2(), source, destination);
    }


    // net.haesleinhuepf.clij2.plugins.GradientY
    //----------------------------------------------------
    /**
     * Computes the gradient of gray values along Y. Assuming a, b and c are three adjacent
     *  pixels in Y direction. In the target image will be saved as: <pre>b' = c - a;</pre>
     */
    default boolean gradientY(ClearCLBuffer source, ClearCLBuffer destination) {
        return GradientY.gradientY(getCLIJ2(), source, destination);
    }


    // net.haesleinhuepf.clij2.plugins.GradientZ
    //----------------------------------------------------
    /**
     * Computes the gradient of gray values along Z. Assuming a, b and c are three adjacent
     *  pixels in Z direction. In the target image will be saved as: <pre>b' = c - a;</pre>
     */
    default boolean gradientZ(ClearCLBuffer source, ClearCLBuffer destination) {
        return GradientZ.gradientZ(getCLIJ2(), source, destination);
    }


    // net.haesleinhuepf.clij2.plugins.MultiplyImageAndCoordinate
    //----------------------------------------------------
    /**
     * Multiplies all pixel intensities with the x, y or z coordinate, depending on specified dimension.</pre>
     */
    default boolean multiplyImageAndCoordinate(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3) {
        return MultiplyImageAndCoordinate.multiplyImageAndCoordinate(getCLIJ2(), arg1, arg2, new Double (arg3).intValue());
    }


    // net.haesleinhuepf.clij2.plugins.Mean2DBox
    //----------------------------------------------------
    /**
     * Computes the local mean average of a pixels rectangular neighborhood. The rectangles size is specified by 
     * its half-width and half-height (radius).
     */
    default boolean mean2DBox(ClearCLImageInterface arg1, ClearCLImageInterface arg2, int arg3, int arg4) {
        return Mean2DBox.mean2DBox(getCLIJ2(), arg1, arg2, arg3, arg4);
    }


    // net.haesleinhuepf.clij2.plugins.Mean2DSphere
    //----------------------------------------------------
    /**
     * Computes the local mean average of a pixels ellipsoidal neighborhood. The ellipses size is specified by 
     * its half-width and half-height (radius).
     */
    default boolean mean2DSphere(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4) {
        return Mean2DSphere.mean2DSphere(getCLIJ2(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue());
    }


    // net.haesleinhuepf.clij2.plugins.Mean3DBox
    //----------------------------------------------------
    /**
     * Computes the local mean average of a pixels cube neighborhood. The cubes size is specified by 
     * its half-width, half-height and half-depth (radius).
     */
    default boolean mean3DBox(ClearCLImageInterface arg1, ClearCLImageInterface arg2, int arg3, int arg4, int arg5) {
        return Mean3DBox.mean3DBox(getCLIJ2(), arg1, arg2, arg3, arg4, arg5);
    }

    /**
     * Computes the local mean average of a pixels rectangular neighborhood. The rectangles size is specified by 
     * its half-width and half-height (radius).
     */
    default boolean meanBox(ClearCLImageInterface arg1, ClearCLImageInterface arg2, int arg3, int arg4, int arg5) {
        return Mean3DBox.meanBox(getCLIJ2(), arg1, arg2, arg3, arg4, arg5);
    }


    // net.haesleinhuepf.clij2.plugins.Mean3DSphere
    //----------------------------------------------------
    /**
     * Computes the local mean average of a pixels spherical neighborhood. The spheres size is specified by 
     * its half-width, half-height and half-depth (radius).
     */
    default boolean mean3DSphere(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4, double arg5) {
        return Mean3DSphere.mean3DSphere(getCLIJ2(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue(), new Double (arg5).intValue());
    }


    // net.haesleinhuepf.clij2.plugins.MeanSliceBySliceSphere
    //----------------------------------------------------
    /**
     * Computes the local mean average of a pixels ellipsoidal 2D neighborhood in an image stack 
     * slice by slice. The ellipses size is specified by its half-width and half-height (radius).
     * 
     * This filter is applied slice by slice in 2D.
     */
    default boolean meanSliceBySliceSphere(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4) {
        return MeanSliceBySliceSphere.meanSliceBySliceSphere(getCLIJ2(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue());
    }


    // net.haesleinhuepf.clij2.plugins.MeanOfAllPixels
    //----------------------------------------------------
    /**
     * Determines the mean average of all pixels in a given image. It will be stored in a new row of ImageJs
     * Results table in the column 'Mean'.
     */
    default double meanOfAllPixels(ClearCLImageInterface arg1) {
        return MeanOfAllPixels.meanOfAllPixels(getCLIJ2(), arg1);
    }


    // net.haesleinhuepf.clij2.plugins.Median2DBox
    //----------------------------------------------------
    /**
     * Computes the local median of a pixels rectangular neighborhood. The rectangle is specified by 
     * its half-width and half-height (radius).
     * 
     * For technical reasons, the area of the rectangle must have less than 1000 pixels.
     */
    default boolean median2DBox(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4) {
        return Median2DBox.median2DBox(getCLIJ2(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue());
    }


    // net.haesleinhuepf.clij2.plugins.Median2DSphere
    //----------------------------------------------------
    /**
     * Computes the local median of a pixels ellipsoidal neighborhood. The ellipses size is specified by 
     * its half-width and half-height (radius).
     * 
     * For technical reasons, the area of the ellipse must have less than 1000 pixels.
     */
    default boolean median2DSphere(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4) {
        return Median2DSphere.median2DSphere(getCLIJ2(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue());
    }


    // net.haesleinhuepf.clij2.plugins.Median3DBox
    //----------------------------------------------------
    /**
     * Computes the local median of a pixels cuboid neighborhood. The cuboid size is specified by 
     * its half-width, half-height and half-depth (radius).
     * 
     * For technical reasons, the volume of the cuboid must contain less than 1000 voxels.
     */
    default boolean median3DBox(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4, double arg5) {
        return Median3DBox.median3DBox(getCLIJ2(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue(), new Double (arg5).intValue());
    }


    // net.haesleinhuepf.clij2.plugins.Median3DSphere
    //----------------------------------------------------
    /**
     * Computes the local median of a pixels spherical neighborhood. The spheres size is specified by 
     * its half-width, half-height and half-depth (radius).
     * 
     * For technical reasons, the volume of the sphere must contain less than 1000 voxels.
     */
    default boolean median3DSphere(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4, double arg5) {
        return Median3DSphere.median3DSphere(getCLIJ2(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue(), new Double (arg5).intValue());
    }


    // net.haesleinhuepf.clij2.plugins.MedianSliceBySliceBox
    //----------------------------------------------------
    /**
     * Computes the local median of a pixels rectangular neighborhood. This is done slice-by-slice in a 3D 
     * image stack. The rectangle is specified by its half-width and half-height (radius).
     * 
     * For technical reasons, the area of the rectangle must have less than 1000 pixels.
     */
    default boolean median3DSliceBySliceBox(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4) {
        return MedianSliceBySliceBox.median3DSliceBySliceBox(getCLIJ2(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue());
    }


    // net.haesleinhuepf.clij2.plugins.MedianSliceBySliceSphere
    //----------------------------------------------------
    /**
     * Computes the local median of a pixels ellipsoidal neighborhood. This is done slice-by-slice in a 3D 
     * image stack. The ellipses size is specified by its half-width and half-height (radius).
     * 
     * For technical reasons, the area of the ellipse must have less than 1000 pixels.
     */
    default boolean median3DSliceBySliceSphere(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4) {
        return MedianSliceBySliceSphere.median3DSliceBySliceSphere(getCLIJ2(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue());
    }


    // net.haesleinhuepf.clij2.plugins.Maximum2DSphere
    //----------------------------------------------------
    /**
     * Computes the local maximum of a pixels ellipsoidal neighborhood. The ellipses size is specified by 
     * its half-width and half-height (radius).
     */
    default boolean maximum2DSphere(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4) {
        return Maximum2DSphere.maximum2DSphere(getCLIJ2(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue());
    }


    // net.haesleinhuepf.clij2.plugins.Maximum3DSphere
    //----------------------------------------------------
    /**
     * Computes the local maximum of a pixels spherical neighborhood. The spheres size is specified by 
     * its half-width, half-height and half-depth (radius).
     */
    default boolean maximum3DSphere(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4, double arg5) {
        return Maximum3DSphere.maximum3DSphere(getCLIJ2(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue(), new Double (arg5).intValue());
    }


    // net.haesleinhuepf.clij2.plugins.Maximum2DBox
    //----------------------------------------------------
    /**
     * Computes the local maximum of a pixels rectangular neighborhood. The rectangles size is specified by 
     * its half-width and half-height (radius).
     */
    default boolean maximum2DBox(ClearCLImageInterface arg1, ClearCLImageInterface arg2, int arg3, int arg4) {
        return Maximum2DBox.maximum2DBox(getCLIJ2(), arg1, arg2, arg3, arg4);
    }

    /**
     * Computes the local maximum of a pixels rectangular neighborhood. The rectangles size is specified by 
     * its half-width and half-height (radius).
     */
    default boolean maximumBox(ClearCLImageInterface arg1, ClearCLImageInterface arg2, int arg3, int arg4) {
        return Maximum2DBox.maximumBox(getCLIJ2(), arg1, arg2, arg3, arg4);
    }


    // net.haesleinhuepf.clij2.plugins.Maximum3DBox
    //----------------------------------------------------
    /**
     * Computes the local maximum of a pixels cube neighborhood. The cubes size is specified by 
     * its half-width, half-height and half-depth (radius).
     */
    default boolean maximum3DBox(ClearCLImageInterface arg1, ClearCLImageInterface arg2, int arg3, int arg4, int arg5) {
        return Maximum3DBox.maximum3DBox(getCLIJ2(), arg1, arg2, arg3, arg4, arg5);
    }

    /**
     * Computes the local maximum of a pixels rectangular neighborhood. The rectangles size is specified by 
     * its half-width and half-height (radius).
     */
    default boolean maximumBox(ClearCLImageInterface arg1, ClearCLImageInterface arg2, int arg3, int arg4, int arg5) {
        return Maximum3DBox.maximumBox(getCLIJ2(), arg1, arg2, arg3, arg4, arg5);
    }


    // net.haesleinhuepf.clij2.plugins.MaximumSliceBySliceSphere
    //----------------------------------------------------
    /**
     * Computes the local maximum of a pixels ellipsoidal 2D neighborhood in an image stack 
     * slice by slice. The ellipses size is specified by its half-width and half-height (radius).
     * 
     * This filter is applied slice by slice in 2D.
     */
    default boolean maximum3DSliceBySliceSphere(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4) {
        return MaximumSliceBySliceSphere.maximum3DSliceBySliceSphere(getCLIJ2(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue());
    }


    // net.haesleinhuepf.clij2.plugins.Minimum2DSphere
    //----------------------------------------------------
    /**
     * Computes the local minimum of a pixels ellipsoidal neighborhood. The ellipses size is specified by 
     * its half-width and half-height (radius).
     */
    default boolean minimum2DSphere(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4) {
        return Minimum2DSphere.minimum2DSphere(getCLIJ2(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue());
    }


    // net.haesleinhuepf.clij2.plugins.Minimum3DSphere
    //----------------------------------------------------
    /**
     * Computes the local minimum of a pixels spherical neighborhood. The spheres size is specified by 
     * its half-width, half-height and half-depth (radius).
     */
    default boolean minimum3DSphere(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4, double arg5) {
        return Minimum3DSphere.minimum3DSphere(getCLIJ2(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue(), new Double (arg5).intValue());
    }


    // net.haesleinhuepf.clij2.plugins.Minimum2DBox
    //----------------------------------------------------
    /**
     * Computes the local minimum of a pixels rectangular neighborhood. The rectangles size is specified by 
     * its half-width and half-height (radius).
     */
    default boolean minimum2DBox(ClearCLImageInterface arg1, ClearCLImageInterface arg2, int arg3, int arg4) {
        return Minimum2DBox.minimum2DBox(getCLIJ2(), arg1, arg2, arg3, arg4);
    }

    /**
     * Computes the local minimum of a pixels rectangular neighborhood. The rectangles size is specified by 
     * its half-width and half-height (radius).
     */
    default boolean minimumBox(ClearCLImageInterface arg1, ClearCLImageInterface arg2, int arg3, int arg4) {
        return Minimum2DBox.minimumBox(getCLIJ2(), arg1, arg2, arg3, arg4);
    }


    // net.haesleinhuepf.clij2.plugins.Minimum3DBox
    //----------------------------------------------------
    /**
     * Computes the local minimum of a pixels cube neighborhood. The cubes size is specified by 
     * its half-width, half-height and half-depth (radius).
     */
    default boolean minimum3DBox(ClearCLImageInterface arg1, ClearCLImageInterface arg2, int arg3, int arg4, int arg5) {
        return Minimum3DBox.minimum3DBox(getCLIJ2(), arg1, arg2, arg3, arg4, arg5);
    }

    /**
     * Computes the local minimum of a pixels rectangular neighborhood. The rectangles size is specified by 
     * its half-width and half-height (radius).
     */
    default boolean minimumBox(ClearCLImageInterface arg1, ClearCLImageInterface arg2, int arg3, int arg4, int arg5) {
        return Minimum3DBox.minimumBox(getCLIJ2(), arg1, arg2, arg3, arg4, arg5);
    }


    // net.haesleinhuepf.clij2.plugins.MinimumSliceBySliceSphere
    //----------------------------------------------------
    /**
     * Computes the local minimum of a pixels ellipsoidal 2D neighborhood in an image stack 
     * slice by slice. The ellipses size is specified by its half-width and half-height (radius).
     * 
     * This filter is applied slice by slice in 2D.
     */
    default boolean minimum3DSliceBySliceSphere(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4) {
        return MinimumSliceBySliceSphere.minimum3DSliceBySliceSphere(getCLIJ2(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue());
    }


    // net.haesleinhuepf.clij2.plugins.MultiplyImages
    //----------------------------------------------------
    /**
     * Multiplies all pairs of pixel values x and y from two image X and Y.
     * 
     * <pre>f(x, y) = x * y</pre>
     */
    default boolean multiplyImages(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLImageInterface arg3) {
        return MultiplyImages.multiplyImages(getCLIJ2(), arg1, arg2, arg3);
    }


    // net.haesleinhuepf.clij2.plugins.GaussianBlur2D
    //----------------------------------------------------
    /**
     * Computes the Gaussian blurred image of an image given two sigma values in X and Y. Thus, the filterkernel can have non-isotropic shape.
     * 
     * The implementation is done separable. In case a sigma equals zero, the direction is not blurred.
     */
    @Deprecated
    default boolean blur2D(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4) {
        return GaussianBlur2D.blur2D(getCLIJ2(), arg1, arg2, new Double (arg3).floatValue(), new Double (arg4).floatValue());
    }

    /**
     * Computes the Gaussian blurred image of an image given two sigma values in X and Y. Thus, the filterkernel can have non-isotropic shape.
     * 
     * The implementation is done separable. In case a sigma equals zero, the direction is not blurred.
     */
    default boolean gaussianBlur2D(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4) {
        return GaussianBlur2D.gaussianBlur2D(getCLIJ2(), arg1, arg2, new Double (arg3).floatValue(), new Double (arg4).floatValue());
    }

    /**
     * Computes the Gaussian blurred image of an image given two sigma values in X and Y. Thus, the filterkernel can have non-isotropic shape.
     * 
     * The implementation is done separable. In case a sigma equals zero, the direction is not blurred.
     */
    default boolean gaussianBlur(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4) {
        return GaussianBlur2D.gaussianBlur(getCLIJ2(), arg1, arg2, new Double (arg3).floatValue(), new Double (arg4).floatValue());
    }

    /**
     * Computes the Gaussian blurred image of an image given two sigma values in X and Y. Thus, the filterkernel can have non-isotropic shape.
     * 
     * The implementation is done separable. In case a sigma equals zero, the direction is not blurred.
     */
    @Deprecated
    default boolean blur(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4) {
        return GaussianBlur2D.blur(getCLIJ2(), arg1, arg2, new Double (arg3).floatValue(), new Double (arg4).floatValue());
    }


    // net.haesleinhuepf.clij2.plugins.GaussianBlur3D
    //----------------------------------------------------
    /**
     * Computes the Gaussian blurred image of an image given two sigma values in X, Y and Z. Thus, the filterkernel can have non-isotropic shape.
     * 
     * The implementation is done separable. In case a sigma equals zero, the direction is not blurred.
     */
    @Deprecated
    default boolean blur3D(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4, double arg5) {
        return GaussianBlur3D.blur3D(getCLIJ2(), arg1, arg2, new Double (arg3).floatValue(), new Double (arg4).floatValue(), new Double (arg5).floatValue());
    }

    /**
     * Computes the Gaussian blurred image of an image given two sigma values in X, Y and Z. Thus, the filterkernel can have non-isotropic shape.
     * 
     * The implementation is done separable. In case a sigma equals zero, the direction is not blurred.
     */
    default boolean gaussianBlur3D(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4, double arg5) {
        return GaussianBlur3D.gaussianBlur3D(getCLIJ2(), arg1, arg2, new Double (arg3).floatValue(), new Double (arg4).floatValue(), new Double (arg5).floatValue());
    }

    /**
     * Computes the Gaussian blurred image of an image given two sigma values in X and Y. Thus, the filterkernel can have non-isotropic shape.
     * 
     * The implementation is done separable. In case a sigma equals zero, the direction is not blurred.
     */
    default boolean gaussianBlur(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4, double arg5) {
        return GaussianBlur3D.gaussianBlur(getCLIJ2(), arg1, arg2, new Double (arg3).floatValue(), new Double (arg4).floatValue(), new Double (arg5).floatValue());
    }

    /**
     * Computes the Gaussian blurred image of an image given two sigma values in X and Y. Thus, the filterkernel can have non-isotropic shape.
     * 
     * The implementation is done separable. In case a sigma equals zero, the direction is not blurred.
     */
    @Deprecated
    default boolean blur(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4, double arg5) {
        return GaussianBlur3D.blur(getCLIJ2(), arg1, arg2, new Double (arg3).floatValue(), new Double (arg4).floatValue(), new Double (arg5).floatValue());
    }


    // net.haesleinhuepf.clij2.plugins.ResliceBottom
    //----------------------------------------------------
    /**
     * Flippes Y and Z axis of an image stack. This operation is similar to ImageJs 'Reslice [/]' method but
     * offers less flexibility such as interpolation.
     */
    default boolean resliceBottom(ClearCLImageInterface arg1, ClearCLImageInterface arg2) {
        return ResliceBottom.resliceBottom(getCLIJ2(), arg1, arg2);
    }


    // net.haesleinhuepf.clij2.plugins.ResliceTop
    //----------------------------------------------------
    /**
     * Flippes Y and Z axis of an image stack. This operation is similar to ImageJs 'Reslice [/]' method but
     * offers less flexibility such as interpolation.
     */
    default boolean resliceTop(ClearCLImageInterface arg1, ClearCLImageInterface arg2) {
        return ResliceTop.resliceTop(getCLIJ2(), arg1, arg2);
    }


    // net.haesleinhuepf.clij2.plugins.ResliceLeft
    //----------------------------------------------------
    /**
     * Flippes X, Y and Z axis of an image stack. This operation is similar to ImageJs 'Reslice [/]' method 
     *  but offers less flexibility such as interpolation.
     */
    default boolean resliceLeft(ClearCLImageInterface arg1, ClearCLImageInterface arg2) {
        return ResliceLeft.resliceLeft(getCLIJ2(), arg1, arg2);
    }


    // net.haesleinhuepf.clij2.plugins.ResliceRight
    //----------------------------------------------------
    /**
     * Flippes X, Y and Z axis of an image stack. This operation is similar to ImageJs 'Reslice [/]' method 
     *  but offers less flexibility such as interpolation.
     */
    default boolean resliceRight(ClearCLImageInterface arg1, ClearCLImageInterface arg2) {
        return ResliceRight.resliceRight(getCLIJ2(), arg1, arg2);
    }


    // net.haesleinhuepf.clij2.plugins.Rotate2D
    //----------------------------------------------------
    /**
     * Rotates an image in plane. All angles are entered in degrees. If the image is not rotated around 
     * the center, it is rotated around the coordinate origin.
     * 
     * It is recommended to apply the rotation to an isotropic image.
     */
    default boolean rotate2D(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3, boolean arg4) {
        return Rotate2D.rotate2D(getCLIJ2(), arg1, arg2, new Double (arg3).floatValue(), arg4);
    }


    // net.haesleinhuepf.clij2.plugins.Rotate3D
    //----------------------------------------------------
    /**
     * Rotates an image stack in 3D. All angles are entered in degrees. If the image is not rotated around 
     * the center, it is rotated around the coordinate origin.
     * 
     * It is recommended to apply the rotation to an isotropic image stack.
     */
    default boolean rotate3D(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3, double arg4, double arg5, boolean arg6) {
        return Rotate3D.rotate3D(getCLIJ2(), arg1, arg2, new Double (arg3).floatValue(), new Double (arg4).floatValue(), new Double (arg5).floatValue(), arg6);
    }


    // net.haesleinhuepf.clij2.plugins.Scale2D
    //----------------------------------------------------
    /**
     * DEPRECATED: CLIJ scale() is deprecated. Use scale2D or scale3D instead!
     */
    default boolean scale(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3, double arg4) {
        return Scale2D.scale(getCLIJ2(), arg1, arg2, new Double (arg3).floatValue(), new Double (arg4).floatValue());
    }

    /**
     * DEPRECATED: CLIJ scale() is deprecated. Use scale2D or scale3D instead!
     */
    default boolean scale(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3) {
        return Scale2D.scale(getCLIJ2(), arg1, arg2, new Double (arg3).floatValue());
    }

    /**
     * Scales an image with a given factor.
     */
    default boolean scale2D(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3, double arg4) {
        return Scale2D.scale2D(getCLIJ2(), arg1, arg2, new Double (arg3).floatValue(), new Double (arg4).floatValue());
    }

    /**
     * Scales an image with a given factor.
     */
    default boolean scale2D(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3, double arg4, boolean arg5) {
        return Scale2D.scale2D(getCLIJ2(), arg1, arg2, new Double (arg3).floatValue(), new Double (arg4).floatValue(), arg5);
    }


    // net.haesleinhuepf.clij2.plugins.Scale3D
    //----------------------------------------------------
    /**
     * DEPRECATED: CLIJ scale() is deprecated. Use scale2D or scale3D instead!
     */
    default boolean scale(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3, double arg4, double arg5) {
        return Scale3D.scale(getCLIJ2(), arg1, arg2, new Double (arg3).floatValue(), new Double (arg4).floatValue(), new Double (arg5).floatValue());
    }

    /**
     * Scales an image with a given factor.
     */
    default boolean scale3D(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3, double arg4, double arg5) {
        return Scale3D.scale3D(getCLIJ2(), arg1, arg2, new Double (arg3).floatValue(), new Double (arg4).floatValue(), new Double (arg5).floatValue());
    }

    /**
     * Scales an image with a given factor.
     */
    default boolean scale3D(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3, double arg4, double arg5, boolean arg6) {
        return Scale3D.scale3D(getCLIJ2(), arg1, arg2, new Double (arg3).floatValue(), new Double (arg4).floatValue(), new Double (arg5).floatValue(), arg6);
    }


    // net.haesleinhuepf.clij2.plugins.Translate2D
    //----------------------------------------------------
    /**
     * Translate an image stack in X and Y.
     */
    default boolean translate2D(ClearCLBuffer source, ClearCLBuffer destination, double translateX, double translateY) {
        return Translate2D.translate2D(getCLIJ2(), source, destination, new Double (translateX).floatValue(), new Double (translateY).floatValue());
    }


    // net.haesleinhuepf.clij2.plugins.Translate3D
    //----------------------------------------------------
    /**
     * Translate an image stack in X, Y and Z.
     */
    default boolean translate3D(ClearCLBuffer source, ClearCLBuffer destination, double translateX, double translateY, double translateZ) {
        return Translate3D.translate3D(getCLIJ2(), source, destination, new Double (translateX).floatValue(), new Double (translateY).floatValue(), new Double (translateZ).floatValue());
    }


    // net.haesleinhuepf.clij2.plugins.Clear
    //----------------------------------------------------

    // net.haesleinhuepf.clij2.plugins.ClInfo
    //----------------------------------------------------

    // net.haesleinhuepf.clij2.plugins.ConvertFloat
    //----------------------------------------------------

    // net.haesleinhuepf.clij2.plugins.ConvertUInt8
    //----------------------------------------------------

    // net.haesleinhuepf.clij2.plugins.ConvertUInt16
    //----------------------------------------------------

    // net.haesleinhuepf.clij2.plugins.Create2D
    //----------------------------------------------------

    // net.haesleinhuepf.clij2.plugins.Create3D
    //----------------------------------------------------

    // net.haesleinhuepf.clij2.plugins.Pull
    //----------------------------------------------------

    // net.haesleinhuepf.clij2.plugins.PullBinary
    //----------------------------------------------------

    // net.haesleinhuepf.clij2.plugins.Push
    //----------------------------------------------------

    // net.haesleinhuepf.clij2.plugins.PushCurrentSlice
    //----------------------------------------------------

    // net.haesleinhuepf.clij2.plugins.PushCurrentZStack
    //----------------------------------------------------

    // net.haesleinhuepf.clij2.plugins.Release
    //----------------------------------------------------

    // net.haesleinhuepf.clij2.plugins.AddImageAndScalar
    //----------------------------------------------------
    /**
     * Adds a scalar value s to all pixels x of a given image X.
     * 
     * <pre>f(x, s) = x + s</pre>
     */
    default boolean addImageAndScalar(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3) {
        return AddImageAndScalar.addImageAndScalar(getCLIJ2(), arg1, arg2, new Double (arg3).floatValue());
    }


    // net.haesleinhuepf.clij2.plugins.DetectMinimaBox
    //----------------------------------------------------
    /**
     * Detects local minima in a given square/cubic neighborhood. Pixels in the resulting image are set to 1 if
     * there is no other pixel in a given radius which has a lower intensity, and to 0 otherwise.
     */
    default boolean detectMinimaBox(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3) {
        return DetectMinimaBox.detectMinimaBox(getCLIJ2(), arg1, arg2, new Double (arg3).intValue());
    }


    // net.haesleinhuepf.clij2.plugins.DetectMaximaBox
    //----------------------------------------------------
    /**
     * Detects local maxima in a given square/cubic neighborhood. Pixels in the resulting image are set to 1 if
     * there is no other pixel in a given radius which has a higher intensity, and to 0 otherwise.
     */
    default boolean detectMaximaBox(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3) {
        return DetectMaximaBox.detectMaximaBox(getCLIJ2(), arg1, arg2, new Double (arg3).intValue());
    }


    // net.haesleinhuepf.clij2.plugins.DetectMaximaSliceBySliceBox
    //----------------------------------------------------
    /**
     * Detects local maxima in a given square neighborhood of an input image stack. The input image stack is 
     * processed slice by slice. Pixels in the resulting image are set to 1 if there is no other pixel in a 
     * given radius which has a higher intensity, and to 0 otherwise.
     */
    default boolean detectMaximaSliceBySliceBox(ClearCLBuffer source, ClearCLBuffer destination, double radius) {
        return DetectMaximaSliceBySliceBox.detectMaximaSliceBySliceBox(getCLIJ2(), source, destination, new Double (radius).intValue());
    }


    // net.haesleinhuepf.clij2.plugins.DetectMinimaSliceBySliceBox
    //----------------------------------------------------
    /**
     * Detects local minima in a given square neighborhood of an input image stack. The input image stack is 
     * processed slice by slice. Pixels in the resulting image are set to 1 if there is no other pixel in a 
     * given radius which has a lower intensity, and to 0 otherwise.
     */
    default boolean detectMinimaSliceBySliceBox(ClearCLBuffer source, ClearCLBuffer destination, double radius) {
        return DetectMinimaSliceBySliceBox.detectMinimaSliceBySliceBox(getCLIJ2(), source, destination, new Double (radius).intValue());
    }


    // net.haesleinhuepf.clij2.plugins.MaximumOfAllPixels
    //----------------------------------------------------
    /**
     * Determines the maximum of all pixels in a given image. It will be stored in a new row of ImageJs
     * Results table in the column 'Max'.
     */
    default double maximumOfAllPixels(ClearCLImageInterface arg1) {
        return MaximumOfAllPixels.maximumOfAllPixels(getCLIJ2(), arg1);
    }


    // net.haesleinhuepf.clij2.plugins.MinimumOfAllPixels
    //----------------------------------------------------
    /**
     * Determines the minimum of all pixels in a given image. It will be stored in a new row of ImageJs
     * Results table in the column 'Min'.
     */
    default double minimumOfAllPixels(ClearCLImageInterface arg1) {
        return MinimumOfAllPixels.minimumOfAllPixels(getCLIJ2(), arg1);
    }


    // net.haesleinhuepf.clij2.plugins.ReportMemory
    //----------------------------------------------------

    // net.haesleinhuepf.clij2.plugins.SetColumn
    //----------------------------------------------------
    /**
     * Sets all pixel values x of a given column in X to a constant value v.
     * 
     * <pre>f(x) = v</pre>
     */
    default boolean setColumn(ClearCLImageInterface arg1, double arg2, double arg3) {
        return SetColumn.setColumn(getCLIJ2(), arg1, new Double (arg2).intValue(), new Double (arg3).floatValue());
    }


    // net.haesleinhuepf.clij2.plugins.SetRow
    //----------------------------------------------------
    /**
     * Sets all pixel values x of a given row in X to a constant value v.
     * 
     * <pre>f(x) = v</pre>
     */
    default boolean setRow(ClearCLImageInterface arg1, double arg2, double arg3) {
        return SetRow.setRow(getCLIJ2(), arg1, new Double (arg2).intValue(), new Double (arg3).floatValue());
    }


    // net.haesleinhuepf.clij2.plugins.SumYProjection
    //----------------------------------------------------
    /**
     * Determines the sum intensity projection of an image along Z.
     */
    default boolean sumYProjection(ClearCLImageInterface arg1, ClearCLImageInterface arg2) {
        return SumYProjection.sumYProjection(getCLIJ2(), arg1, arg2);
    }


    // net.haesleinhuepf.clij2.plugins.AverageDistanceOfTouchingNeighbors
    //----------------------------------------------------
    /**
     * Takes a touch matrix and a distance matrix to determine the average distance of touching neighbors for every object.
     */
    default boolean averageDistanceOfTouchingNeighbors(ClearCLBuffer distance_matrix, ClearCLBuffer touch_matrix, ClearCLBuffer average_distancelist_destination) {
        return AverageDistanceOfTouchingNeighbors.averageDistanceOfTouchingNeighbors(getCLIJ2(), distance_matrix, touch_matrix, average_distancelist_destination);
    }


    // net.haesleinhuepf.clij2.plugins.LabelledSpotsToPointList
    //----------------------------------------------------
    /**
     * Transforms a labelmap of spots (single pixels with values 1, 2, ..., n for n spots) as resulting from connected components analysis in an image where every column contains d 
     * pixels (with d = dimensionality of the original image) with the coordinates of the maxima/minima.
     */
    default boolean labelledSpotsToPointList(ClearCLBuffer input_labelled_spots, ClearCLBuffer destination_pointlist) {
        return LabelledSpotsToPointList.labelledSpotsToPointList(getCLIJ2(), input_labelled_spots, destination_pointlist);
    }


    // net.haesleinhuepf.clij2.plugins.LabelSpots
    //----------------------------------------------------
    /**
     * Transforms a spots image as resulting from maximum/minimum detection in an image of the same size where every spot has a number 1, 2, ... n.
     */
    default boolean labelSpots(ClearCLBuffer input_spots, ClearCLBuffer labelled_spots_destination) {
        return LabelSpots.labelSpots(getCLIJ2(), input_spots, labelled_spots_destination);
    }


    // net.haesleinhuepf.clij2.plugins.MinimumDistanceOfTouchingNeighbors
    //----------------------------------------------------
    /**
     * Takes a touch matrix and a distance matrix to determine the shortest distance of touching neighbors for every object.
     */
    default boolean minimumDistanceOfTouchingNeighbors(ClearCLBuffer distance_matrix, ClearCLBuffer touch_matrix, ClearCLBuffer minimum_distancelist_destination) {
        return MinimumDistanceOfTouchingNeighbors.minimumDistanceOfTouchingNeighbors(getCLIJ2(), distance_matrix, touch_matrix, minimum_distancelist_destination);
    }


    // net.haesleinhuepf.clij2.plugins.SetWhereXgreaterThanY
    //----------------------------------------------------
    /**
     * Sets all pixel values a of a given image A to a constant value v in case its coordinates x > y. Otherwise the pixel is not overwritten.
     * If you want to initialize an identity transfrom matrix, set all pixels to 0 first.
     * 
     * <pre>f(a) = v</pre>
     */
    default boolean setWhereXgreaterThanY(ClearCLImageInterface arg1, double arg2) {
        return SetWhereXgreaterThanY.setWhereXgreaterThanY(getCLIJ2(), arg1, new Double (arg2).floatValue());
    }


    // net.haesleinhuepf.clij2.plugins.SetWhereXsmallerThanY
    //----------------------------------------------------
    /**
     * Sets all pixel values a of a given image A to a constant value v in case its coordinates x < y. Otherwise the pixel is not overwritten.
     * If you want to initialize an identity transfrom matrix, set all pixels to 0 first.
     * 
     * <pre>f(a) = v</pre>
     */
    default boolean setWhereXsmallerThanY(ClearCLImageInterface arg1, double arg2) {
        return SetWhereXsmallerThanY.setWhereXsmallerThanY(getCLIJ2(), arg1, new Double (arg2).floatValue());
    }


    // net.haesleinhuepf.clij2.plugins.SetNonZeroPixelsToPixelIndex
    //----------------------------------------------------
    /**
     * Sets all pixels in an image which are not zero to the index of the pixel. This can be used for Connected Components Analysis.
     */
    default boolean setNonZeroPixelsToPixelIndex(ClearCLImageInterface arg1, ClearCLImageInterface arg2) {
        return SetNonZeroPixelsToPixelIndex.setNonZeroPixelsToPixelIndex(getCLIJ2(), arg1, arg2);
    }


    // net.haesleinhuepf.clij2.plugins.CloseIndexGapsInLabelMap
    //----------------------------------------------------
    /**
     * 
     */
    @Deprecated
    default boolean shiftIntensitiesToCloseGaps(ClearCLBuffer arg1, ClearCLBuffer arg2) {
        return CloseIndexGapsInLabelMap.shiftIntensitiesToCloseGaps(getCLIJ2(), arg1, arg2);
    }

    /**
     * Analyses a label map and if there are gaps in the indexing (e.g. label 5 is not present) all 
     * subsequent labels will be relabelled. Thus, afterwards number of labels and maximum label index are equal.
     * 
     */
    default boolean closeIndexGapsInLabelMap(ClearCLBuffer binary_input, ClearCLBuffer labeling_destination) {
        return CloseIndexGapsInLabelMap.closeIndexGapsInLabelMap(getCLIJ2(), binary_input, labeling_destination);
    }

}
// 283 methods generated.