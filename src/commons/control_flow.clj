(ns commons.control-flow
  (:require [such.immigration :as immigrate]))
(immigrate/selection 'swiss.arrows '[-<> -<>> -!> -!>> -!<> some-<> some-<>>])

(defmacro branch-on
  "    (branch-on (str \"one\" \"two\")
         vector?   :vector
         string?   :string
         :else     :unknown)

  Evaluates the `value-form` once, then checks that value against
  each predicate in the cond-like body. The value after the first
  matching predicate is returned. If there is no match and an `:else`
  clause is present, its value is returned, otherwise `nil`.
"
  [value-form & body]
  (let [value-sym (gensym "value-form-")
        cond-pairs (mapcat (fn [[branch-pred-form branch-val-form]]
                             (let [test (if (= branch-pred-form :else)
                                          :else
                                          `(~branch-pred-form ~value-sym))]
                             `(~test ~branch-val-form)))
                           (partition 2 body))]
    
    `(let [~value-sym ~value-form]
       (cond ~@cond-pairs))))
