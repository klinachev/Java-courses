(defn isLenEqual [vectors] (apply == (mapv count vectors)))

(defn isNumbers [s] (every? number? s))

(defn isVector [v] (and (vector? v) (isNumbers v)))

(defn checkVecArg [vectors] (and (every? isVector vectors) (isLenEqual vectors)))

(defn checkVecAns [ans v] (and (isVector ans) (== (count ans) (count v))))

(defn vOperator [f]
  (fn [& vectors]
    {:pre [(checkVecArg vectors)]
     :post [(checkVecAns % (nth vectors 0))]}
    (apply mapv f vectors)))

(def v+ (vOperator +))
(def v* (vOperator *))
(def v- (vOperator -))

(defn scalar [& vectors]
  {:pre [(checkVecArg vectors)]
   :post [(number? %)]}
  (apply + (apply v* vectors)))

(defn vect [& vectors]
  {:pre [(checkVecArg vectors) (== (count (nth vectors 0)) 3)]
   :post [(checkVecAns % (nth vectors 0))]}
  (reduce (fn [v1, v2]
            (mapv
              (fn [a]
                (let [p1 a p2 (mod (+ a 1) 3)]
                  (- (* (nth v1 p1) (nth v2 p2))
                     (* (nth v1 p2) (nth v2 p1))))
                )
              [1 2 0]))
          vectors))


(defn v*s [v & s]
  {:pre [(isVector v)]
   :post [(vector? %)]}
  (comment ":NOTE: for each element in vector you recalculate production of scalars. This is not good")
  (let [mulS (apply * s)]
    (mapv (fn [elem] (* elem mulS)) v)))

(defn isMatrix [m] (and (vector? m) (checkVecArg m) ))

(defn checkMatrixArg [matrix]
  (and (every? isMatrix matrix)
       (isLenEqual matrix)))

(defn checkMatrixAns [ans m]
  (and (isMatrix ans) (== (count ans) (count m))))

(defn mOperator [f]
  (fn [& matrix]
    {:pre [(checkMatrixArg matrix)]
     :post [(checkMatrixAns % (nth matrix 0))]}
    (apply mapv f matrix)))

(def m+ (mOperator v+))
(def m- (mOperator v-))
(def m* (mOperator v*))

(defn m*s [matrix & s]
  {:pre [(isNumbers s) (isMatrix matrix)]
   :post [(checkMatrixAns % matrix)]}
  (let [mulS (apply * s)]
    (mapv (fn [v] (v*s v mulS)) matrix)))

(defn m*v [matrix & v]
  {:pre [(isMatrix matrix) (checkVecArg v)]
   :post [(== (count %) (count matrix)) (mapv (fn [x] (number? x)) %)]}
  (mapv (fn [mv] (apply scalar mv v)) matrix))

(defn transpose [matrix]
  {:pre [(isMatrix matrix)]
   :post [(isMatrix %) (== (count matrix) (count (nth % 0))) (== (count (nth matrix 0)) (count %))]}
  (apply mapv vector matrix))


(defn m*m [& matrix]
  {:pre [(every? isMatrix matrix)]
   :post [(isMatrix %)
          (== (count (nth matrix 0)) (count %))
          (== (count (nth (nth matrix (- (count matrix) 1)) 0)) (count (nth % 0)))]}
  (reduce
    (fn [m1, m2]
      (mapv
        (fn [v] (m*v (transpose m2) v)) m1)) matrix))

(defn isSameStruct [a, t]
  (and
      (== (count a) (count t))
      (or (and (isNumbers t) (isNumbers a))
          (and
              (every? vector? t)
              (isLenEqual t)
              (every? (fn [x] (isSameStruct (nth a 0) x)) t)
            ))))

(defn tOperator [f]
  (fn [& t]
     {:pre [(every? vector? t) (isLenEqual t) (every? (fn [x] (isSameStruct (nth t 0) x)) t)]
     :post [(isSameStruct (nth t 0) %)]}
    (apply
        (fn rec [& t]
         (if (number? (nth t 0))
            (apply f t)
            (apply mapv rec t))) t)))

(def t+ (tOperator +))
(def t- (tOperator -))
(def t* (tOperator *))