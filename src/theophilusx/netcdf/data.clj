(ns theophilusx.netcdf.data
  (:require [theophilusx.netcdf.arrays :as arrays]))

(defn read-scalar
  "Reads a scalar variable. The `v` argument is a variable map returned from a
  call to `theophilusx.netcdf.variables/variables` or
  `theophilusx.netcdf.variables/variable'. Will throw an exception if the
  variable is not a scalar variable or the data type of the variable is not
  recognised."
  [v]
  (when (not (:is-scalar? v))
    (throw (Exception. (str (:name v) " not a scalar variable"))))
  (condp = (:type v)
    :byte   (.readScalarByte (:obj v))
    :double (.readScalarDouble (:obj v))
    :float  (.readScalarFloat (:obj v))
    :int    (.readScalarInt (:obj v))
    :long   (.readScalarLong (:obj v))
    :short  (.readScalarShort (:obj v))
    :string (.readScalarString (:obj v))
    (throw (Exception. "Unknown data type of " (:type v)))))

(defn read-slice
  "Read a slice of data from a NetCDF variable. The `v` argument is a variable map
  returned from a call to `theophilusx.netcdf.variables/variable`. The `origin`
  argument is a `vector` of numbers defining the starting index for each
  dimension in the variable. The `size` argument is a vector of numbers which
  specify the number of elements to read in each dimension. The function returns
  an array map (see `theophilusx.netcdf.arrays`.)"
  [v origin size]
  (arrays/array->map (.read (:obj v) (int-array origin) (int-array size))))

(defn read-value
  "Returns a specific element from a NetCDF variable. The argument `v` is a
  variable map returned from a call to `theophilusx.netcdf.variables/variable`.
  The `origin` argument is a vector of numbers specifying the index within the
  variable of the element to return. There is a value for each dimension of the
  variable. The value returned is of the type specified by the `:type` key in
  the variable map."
  [v origin]
  (let [size   (take (count origin) (range 1 2 0))
        values (read-slice v origin size)]
    (arrays/get-element values 0))) 
