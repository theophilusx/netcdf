(defproject theophilusx/netcdf "1.0.0" 
  :description "Basic support for reading NetCDF 4 data files"
  :url "https://github.com/theophilusx/netcdf"
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [edu.ucar/netcdf4 "5.3.3"]
                 [org.clojure/tools.logging "1.1.0"]
                 [ch.qos.logback/logback-classic "1.2.3"]]
  :repositories [["unidata" "https://artifacts.unidata.ucar.edu/content/repositories/unidata-releases"]]
  :source-paths ["src"]
  :plugins [[lein-codox "0.10.7"]]
  :codox {:source-url "https://github.com/tehophilusx/netcdf/blob/master/{filepath}#{line}"
          :metadata {:doc/format :markdown}
          :output-path "docs"
          :namespaces :all
          :language :clojure})
