- [netcdf](#sec-1)
  - [Usage](#sec-1-1)
  - [Name Spaces](#sec-1-2)
  - [License](#sec-1-3)

# netcdf<a id="sec-1"></a>

A Clojure library which wraps the Java netcdf4 library from Unidata

At this stage, the focus of this library is for reading netcdf4 data files. There is no support for writing netcdf files at this time. As my use case is focused on processing large netcdf data files (i.e. reading), it is unlikely I will add support for writing/creating netcdf files.

If you are searching for a Clojure library which is considerably more mature and does support writing netcdf files, check out [netcdf-clj](https://github.com/r0man/netcdf-clj)

## Usage<a id="sec-1-1"></a>

The library provides a Clojure interface to the Unidata NetCDF4 Java library. This is a very large library and no attempt has been made to implement all of the functinality provided by the Java library. The focus here is on reading NetCDF files and extracting data for further processing.

Most of the interface is based on Clojure maps. Each key NetCDF data structure is represented as a Clojure map (variables, dimensions, attributes, etc). Each map has keys representing key information about the underlying object e.g. `:name`, `:type`, `:size` etc. In addition, each map also contains an `:obj` key which contains the raw underlying Java object. This allows easy access to additional Java library operations based on Clojure's Java interop facilities.

## Name Spaces<a id="sec-1-2"></a>

The following name spaces are implemented by the library.

| Name Space                    | Description                                   |
|----------------------------- |--------------------------------------------- |
| theophilusx.netcdf.attributes | Access NetCDF attribute data                  |
| theophilusx.netcdf.core       | Convenience name space which contains aliases |
|                               | to public functions in other name spaces      |
| theophilusx.netcdf.dimensions | Access to variable dimension information      |
| theophilusx.netcdf.file       | Functions to open/read NetCDF files           |
| theophilusx.netcdf.groups     | Access data on NetCDF groups                  |
| theophilusx.netcdf.ranges     | Generate and manipulate Range objects         |
| theophilusx.netcdf.variables  | Access and process NetCDF variables           |

## License<a id="sec-1-3"></a>

Copyright © 2019 Tim Cross

Distributed under the Eclipse Public License either version 1.0 or (at your option) any later version.
