(ns theophilusx.netcdf.keys-test)

(def variables #{:description :element-size :name :rank :type
                 :dimensions :size :shape :ranges :obj :attributes
                 :is-coordinate? :is-metadata? :is-scalar? :is-unlimited?
                 :is-variable-length?})

(def variable-types #{:byte :char :double :enum1 :enum2 :enum4 :float :int
                      :long :object :opaque :sequence :short :string :structure})

(def dimensions #{:name :length :unlimited? :varying? :obj})

(def ranges #{:name :first :last :length :stride :obj})

(def attributes #{:name :type :length :value :obj})

(def attribute-types #{:byte :char :double :enum1 :enum2 :enum4 :float :int
                       :long :object :opaque :sequence :short :string :structure})

(def groups #{:short-name :root? :attributes :dimensions :variables
              :obj :children})
