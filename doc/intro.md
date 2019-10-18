
# Table of Contents

1.  [Overview](#orgc9cedaa)
2.  [Modules](#org672b0cb)
    1.  [clj-netcdf.netcdf4](#org094e915)


<a id="orgc9cedaa"></a>

# Overview

This library is basically a wrapper around the UniData NetCDF Java library with
some additional abstractions to make it integrate better with
Clojure. Documentation for the Java library, plus tutorials and other
information can be found at the [UniData Java Library Home Page](https://www.unidata.ucar.edu/software/thredds/current/netcdf-java/documentation.htm) . 

Unlike the base Java library, this library makes no attempt (at this time) to
provide functionality for the creation of NetCDF files. The focus of this
library is the exploration and extraction of data from NetCDF files.

This library is broken up into a number of modules

-   **attributes:** Functions dealing with variable attribute information
-   **dimensions:** Functions dealing with variable dimension information
-   **groups:** Functions dealing with NetCDF groups
-   **netcdf4:** Low level NetCDF file operations
-   **ranges:** Functions dealing with ranges of data in NetCDF variables
-   **variables:** Functions to manipulate NetCDF variables

The library attempts to provide high level functions which are useful for
exploring NetCDF files such as determining the number and structure of groups,
variables and dimensions etc. The library also provides functions to extract
individual data elements, slices of data variables or whole grids of data.  

One aim of this library is to make the processing of data from NetCDF files
reasonably efficient. This library is currently being used to extract weather
records for processing and insertion into a Postgres database. A secondary use
of this library is for the exploration of NetCDF data files containing weather
data and help identify the source of anomalies and errors in various predictive
models that use the data from these files. 


<a id="org672b0cb"></a>

# Modules


<a id="org094e915"></a>

## clj-netcdf.netcdf4

This is a low level module which provides basic NetCDF file processing
functions. 
