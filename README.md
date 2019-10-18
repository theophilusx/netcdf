
# Table of Contents

1.  [netcdf](#org9dbef9b)
    1.  [Why](#org529c207)
    2.  [Usage](#org6b0035d)
    3.  [License](#org322ba23)


<a id="org9dbef9b"></a>

# netcdf

A Clojure library which wraps the Java netcdf4 library from Unidata

At this stage, the focus of this library is for reading netcdf4 data
files. There is no support for writing netcdf files at this time. As my use case
is focused on processing large netcdf data files (i.e. reading), it is unlikely
I will add support for writing/creating netcdf files. 

If you are searching for a Clojure library which is considerably more mature and
does support writing netcdf files, check out [netcdf-clj](https://github.com/r0man/netcdf-clj)


<a id="org529c207"></a>

## Why

Why write another Clojure wrapper for the netcdf4 Java library?

Well, main reason is that I found netcdf-clj to be too slow for my
requirements. However, as I'm inexperienced with netcdf and the Java library, I
wasn't sure why it was so slow. Experiments using other libraries (such as
Javascript) and tests using the native Java library show that processing netcdf
files can be very fast. Therefore, either the netcdf-clj library is slow or how
I was using it was slow or inefficient. My suspicion is it is the latter. 

To learn more about the Java library and Clojure interop, the solution is to try
implementing my own wrapper library. 


<a id="org6b0035d"></a>

## Usage

FIXME


<a id="org322ba23"></a>

## License

Copyright © 2019 Tim Cross

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.

