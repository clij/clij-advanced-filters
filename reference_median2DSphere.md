## median2DSphere
![Image](images/mini_clijx_logo.png)

Computes the local median of a pixels ellipsoidal neighborhood. The ellipses size is specified by 
its half-width and half-height (radius).

For technical reasons, the area of the ellipse must have less than 1000 pixels.

### Usage in ImageJ macro
```
Ext.CLIJx_median2DSphere(Image source, Image destination, Number radiusX, Number radiusY);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clijx.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJx clijx = CLIJx.getInstance();

// get input parameters
ClearCLBuffer arg1 = clijx.push(arg1ImagePlus);
ClearCLBuffer arg2 = clijx.push(arg2ImagePlus);
int arg3 = 10;
int arg4 = 20;
```

```
// Execute operation on GPU
clijx.median2DSphere(clij, arg1, arg2, arg3, arg4);
```

```
//show result

// cleanup memory on GPU
arg1.close();
arg2.close();
```


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)