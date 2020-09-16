- [Overview](#sec-1)
- [Usage](#sec-2)
  - [Name Spaces](#sec-2-1)
  - [Opening a Netcdf File](#sec-2-2)
  - [Reading Data](#sec-2-3)
- [License](#sec-3)


# Overview<a id="sec-1"></a>

A Clojure library which wraps the Java netcdf4 library from Unidata. See <https://www.unidata.ucar.edu/software/netcdf-java/>

This is a very basic wrapper around only part of the Java API based on what I needed for my own NetCDF file processing. At this point, there is no support for writing NetCDF files, only reading and extracting data from variables in these files. The API is based around Clojure `map` structures representing NetCDF attributes, variables and arrays. Each map also contains an `:obj` key, which contains the corresponding raw Java object i.e. `Attibute`, `Variable`, `Array` etc. This makes it easy to call any of the many other methods provided by the various Java classes which do not have a basic Clojure based function wrapper.

In addition to the `:obj` key, each map also contains keys representing various static data associated with each object e.g. attribute name, variable name or type, array rank, size and shape etc. See the documentation for each `map` for details.

# Usage<a id="sec-2"></a>

The most frequently used functions in this library are all aliased in the `theophilusx.netcdf.core` namespace. In most cases, just loading this namespace will provide all the functionality you need.

Most of the interface is based on Clojure maps. Each key NetCDF data structure is represented as a Clojure map (variables, dimensions, attributes, etc). Each map has keys representing key information about the underlying object e.g. `:name`, `:type`, `:size` etc. In addition, each map also contains an `:obj` key which contains the raw underlying Java object. This allows easy access to additional Java library operations based on Clojure's Java interop facilities.

## Name Spaces<a id="sec-2-1"></a>

The following name spaces are implemented by the library.

| Name Space                    | Description                                          |
|----------------------------- |---------------------------------------------------- |
| theophilusx.netcdf.arrays     | Processing of array data                             |
| theophilusx.netcdf.attributes | Access NetCDF attribute data                         |
| theophilusx.netcdf.core       | Convenience name space which contains aliases        |
|                               | to public functions in other name spaces             |
| theophilusx.netcdf.data       | Basic functions to read data from a NeteCDF variable |
| theophilusx.netcdf.dimensions | Access to variable dimension information             |
| theophilusx.netcdf.file       | Functions to open/read NetCDF files                  |
| theophilusx.netcdf.groups     | Access data on NetCDF groups                         |
| theophilusx.netcdf.ranges     | Generate and manipulate Range objects                |
| theophilusx.netcdf.variables  | Access and process NetCDF variables                  |

## Opening a Netcdf File<a id="sec-2-2"></a>

The `theophilusx.netecdf.file` namespace contains various functions used to open a NetCDF file and retrieve some metadata about the file. The two main functions for opening a NetCDF file are `open` and `open-in-memory`. There is also a `close` function to close the file. The opening functions return a `NetcdfFile` Java object. Often, you won't use these functions directly, but instead use the macros `with-netcdf` and `with-memory-netcdf` instead. These macros allow you to open a NetCDF file, perform some operations and then automatically close and free the resource once done.

```clojure
(ns demo.core
  (:require [theophilusx.netcdf.core :as core]))

(core/with-netcdf [nc "./data.nc"]
  (let [vs (core/variables nc)]
    (for [v vs]
      (println (str "Name: " (:name v)
                    " Description: " (:description v))))))
```

## Reading Data<a id="sec-2-3"></a>

The `read-value` function provides a way to read a single value from a NetCDF variable. For example

```clojure
(ns demo.core
  (:require [theophilusx.netcdf.core :as core]))

(core/with-netcdf [nc "./data.nc"]
  (let [v (core/variable nc "tasmax")
        val (core/read-value v [1 300 400])]
    (println (str "The value at index 1 300 400 is " val
                  " for variable tasmax"))))
```

In the above example, the variable `tasmax` is a 3 dimensional array. The example returns the value at indice `[1 300 400]`. While this is fine for reading a single value, it is an inefficient method for reading multiple values. For this, you would use the `read-slice` function. for example

```clojure
(ns demo.core
  (:require [theophilusx.netcdf.core :as core]))

(core/with-netcdf [nc "./data.nc"]
  (let [v (core/variable nc "tasmax")
        ar (core/read-slice v [0 300 400] [2 2 2])]
    (for [j [0 1]
          k [0 1]
          l [0 1]]
      (println (str "Value at [" j " " k " " l "] is "
                    (core/get-value ar j k l))))))
```

The `read-slice` function returns a multi-dimensional array of the values from the `tasmax` variable. The array specifies the `origin` i.e. starting index for each dimension and the `shape` i.e. number of elements from each dimension. In the above example, the array is 3 dimensional with 2 elements in each dimension with each dimension starting at `[0 300 400]` respectively.

# License<a id="sec-3"></a>

Copyright © 2019 Tim Cross

Distributed under the Eclipse Public License either version 1.0 or (at your option) any later version.
