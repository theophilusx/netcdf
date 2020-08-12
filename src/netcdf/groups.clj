(ns netcdf.groups
  (:require [netcdf.attributes :as attributes]
            [netcdf.dimensions :as dimensions]
            [netcdf.variables :as variables]
            [clojure.string :as string])
  (:import [ucar.nc2 NetcdfFile Group]))

(defn -mk-group-map
  "Returns internal representation of NetCDF group as a map"
  [^Group grp]
  {:short-name (if (.isRoot grp)
                 "Root"
                 (.getShortName grp))
   :root? (.isRoot grp)
   :attributes (attributes/-attributes->vector (.getAttributes grp))
   :dimensions (dimensions/-dimensions->vector (.getDimensions grp))
   :variables (variables/-variables->vector (.getVariables grp))
   :obj grp})

(defn -group->map
  "Returns a map representing a NetCDF group. The map contains the keys 
  `name` = group name, `root` = true if this is the root group, `attributes` 
  = vector of group attributes, `dimensions` = dimensions of variables in the group
  `variables` = vector of variables in the group and `obj` = the low level NetCDF 
   group object." 
  [^Group grp]
  (let [base (-mk-group-map grp)
        children (.getGroups grp)]
    (if (empty? children)
      (assoc base :children nil)
      (assoc base :children (mapv #'-group->map children)))))

(defn groups->vector [grp-list]
  (mapv #'-group->map grp-list))

(defn group->string
  ([grp-map]
   (group->string grp-map ""))
  ([grp-map indent]
   (str indent "Name: " (:name grp-map)
        " Root?: " (:root? grp-map) "\n"
        indent "Attributes:\n"
        (string/join "\n"
                             (map
                              #(attributes/attribute->string % (str indent "\t"))
                              (:attributes grp-map))) "\n"
        indent "Dimensions:\n"
        (string/join "\n"
                             (map
                              #(dimensions/dimension->string % (str indent "\t"))
                              (:dimensions grp-map))) "\n"
        indent "Variables:\n"
        (string/join "\n"
                             (map
                              #(variables/variable->string % (str indent "\t"))
                              (:variables grp-map))) "\n"
        (if (:children grp-map)
          (str indent "Children:\n"
               (string/join "\n"
                                    (map
                                     #(group->string % (str indent "\t"))
                                     (:children grp-map))))
          ""))))

(defn root-group
  "Returns a map for the root group containing keys for `:name`, `:root?`, 
  `:attributes`, `:dimensions`, `:variables`, `:obj` and `:children`. The 
  `nc` argument is a netcdf file object returned from a call to `open` or 
  `open-in-memory`. "  
  [^NetcdfFile nc]
  (-group->map (.getRootGroup nc)))

(defn group
  "return a group given the full group name."
  [^NetcdfFile nc group-name]
  (-group->map (.findGroup nc group-name)))
