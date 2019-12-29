__constant sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;


__kernel void split_3_stacks(IMAGE_src_TYPE src, IMAGE_dst0_TYPE dst0, IMAGE_dst1_TYPE dst1, IMAGE_dst2_TYPE dst2){

   const int i = get_global_id(0), j = get_global_id(1), k = get_global_id(2);

   WRITE_dst0_IMAGE(dst0,(int4)(i,j,k,0),CONVERT_dst0_PIXEL_TYPE(READ_src_IMAGE(src,sampler,(const int4)(i,j,3*k,0)).x));
   WRITE_dst1_IMAGE(dst1,(int4)(i,j,k,0),CONVERT_dst1_PIXEL_TYPE(READ_src_IMAGE(src,sampler,(int4)(i,j,3*k+1,0)).x));
   WRITE_dst2_IMAGE(dst2,(int4)(i,j,k,0),CONVERT_dst2_PIXEL_TYPE(READ_src_IMAGE(src,sampler,(int4)(i,j,3*k+2,0)).x));
}
