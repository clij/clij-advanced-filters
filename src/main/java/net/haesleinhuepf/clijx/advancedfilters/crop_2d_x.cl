__kernel void crop_2d(
    IMAGE_dst_TYPE dst, 
    IMAGE_src_TYPE src, 
    int start_x, 
    int start_y
) {
  const sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

  const int dx = get_global_id(0);
  const int dy = get_global_id(1);

  const int sx = start_x + dx;
  const int sy = start_y + dy;

  const int2 dpos = (int2){dx,dy};
  const int2 spos = (int2){sx,sy};

  const float out = READ_src_IMAGE(src,sampler,spos).x;
  WRITE_dst_IMAGE(dst,dpos, CONVERT_dst_PIXEL_TYPE(out));
}

