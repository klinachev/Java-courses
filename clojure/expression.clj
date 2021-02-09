
(def constant constantly)

(defn variable [name] (fn [variables] (get variables name)))

(defn operation [f]
  (fn [& args]
    (fn [element]
      (apply f (mapv
                 (fn [func] (func element))
                 args)))))

(def add (operation +))

(def subtract (operation -))

(def negate subtract)

(def multiply (operation *))

(def divide (operation (fn [x & y] (/ (double x) (apply * y)))))

(def avg (operation (fn [& args] (/ (apply + args) (count args)))))

(def med (operation (fn [& args] (nth (sort args) (/ (count args) 2)))))

(def availableFunctions {'+ add, '- subtract, '* multiply, '/ divide, 'negate negate, 'avg avg, 'med med})

(defn parse [constCreate variableCreate operationMap f]
  (fn inParse [expression]
    (cond
      (number? expression) (constCreate expression)
      (symbol? expression) (variableCreate (str expression))
      (seq? expression) (apply (f (get operationMap (first expression))) (mapv inParse (rest expression))))))

(def parseFunction (comp (parse constant variable availableFunctions #(%1)) read-string))

; hw 11
(defn proto-get [obj key]
  (cond
    (contains? obj key) (obj key)
    (contains? obj :prototype) (proto-get (obj :prototype) key)
    :else nil))
(defn proto-call [this key & args]
  (apply (proto-get this key) this args))
(defn field [key]
  #(proto-get % key))
(def _args (field :args))
(def _binary (field :binary))
(def _arg (field :arg))
(def _calc (field :calc))
(def _toStr (field :toStr))
(def _diffCalc (field :diffCalc))
(defn method [key]
  (fn [this & args] (apply proto-call this key args)))
(def _evaluate (method :evaluate))
(def _toString (method :toString))
(def _toStringSuffix (method :toStringSuffix))
(def _toStringInfix (method :toStringInfix))
(def _diff (method :diff))
(defn constructor [ctor prototype]
  (fn [& args] (apply ctor {:prototype prototype} args)))
(defn protoFactory [eval print diff]
  {:evaluate       eval,
   :toString       print,
   :toStringSuffix print,
   :toStringInfix  print
   :diff diff
   })
(declare zero)
(defn ConstVarCons [this arg] (assoc this :arg arg))
(def Constant 
  (constructor 
    ConstVarCons 
    (protoFactory
      (fn [this _] (_arg this))
      (fn [this] (format "%.1f" (double (_arg this))))
      (fn [_ _] zero)
    )))
(def zero (Constant 0))
(def one (Constant 1))
(def Variable 
  (constructor 
    ConstVarCons 
    (protoFactory
      (fn [this args] (get args (_arg this)))
      (fn [this] (_arg this))
      (fn [this dName]
        (if (= dName (_arg this)) one zero))
    )))
(defn OperationCons [this binary calc toStr diffCalc args]
  (assoc this
    :binary binary
    :calc calc,
    :toStr toStr,
    :diffCalc diffCalc,
    :args args))
(def printArgument (fn [this pos] (str (_toStringInfix (nth (_args this) pos)))))
(def printArgs (fn [args mode] (apply str (mode (first args)) (mapv #(str " " (mode %)) (rest args)))))
(defn printOperation [first second]
  (str "(" first " " second ")"))
(def operationPrototype
    {
     :evaluate (fn [this variables]
                 (apply (_calc this) (mapv #(_evaluate % variables) (_args this))))
     :toString (fn [this]
                 (printOperation (_toStr this) (printArgs (_args this) _toString)))
     :toStringSuffix (fn [this]
                       (printOperation (printArgs (_args this) _toStringSuffix) (_toStr this)))
     :toStringInfix (fn [this]
                      (if (_binary this)
                        (printOperation (printArgument this 0)
                                        (str (_toStr this) " " (printArgument this 1)))
                        (str (_toStr this) "(" (printArgs (_args this) _toStringInfix) ")")))
     :diff (fn [this dVariable]
             (apply (_diffCalc this) (mapv (fn [x] [x (_diff x dVariable)]) (_args this))))
     })
(def Operation (constructor OperationCons operationPrototype))

(defn makeOperation [bin f str diff]
  (fn [& args] (Operation bin f str diff args)))

(def Negate (makeOperation false - 'negate (fn [[_ dx]] (Negate dx))))
(defn first_ [[x _]] x)
(defn second_ [[_ dx]] dx)

(defn sumDiff [this] (fn [& args] (apply (this) (mapv second_ args))))
(def Add (makeOperation true + '+ (sumDiff (fn [] Add))))
(def Subtract
  (makeOperation true - '- (sumDiff (fn [] Subtract))))
(declare Multiply)
(defn mulDiff [& args]
  ((fn rec [list]
     (if (= (count list) 1)
       (second_ (first list))
       (Add (Multiply (first_ (first list)) (rec (rest list)))
            (apply Multiply (second_ (first list)) (mapv #(first_ %) (rest list)))
            ))) args))
(def Multiply (makeOperation true * '* mulDiff))
(def Divide
  (makeOperation
    true
    (fn [x & y] (/ (double x) (apply * y)))
    '/
    (fn [& args]
      (let [fArg (first args)
            gArg (rest args)
            f (first_ fArg)
            g ((fn [[a]] a) (mapv #(first %) gArg))
            df (second_ fArg)
            dg (apply mulDiff gArg)]
        (Subtract
          (Divide df g)
          (Divide (Multiply f dg) (Multiply g g)))))
    ))
(def Log
  (makeOperation true (fn [x y] (/ (Math/log (Math/abs y)) (Math/log (Math/abs x)))) "//" #()))
(def Pow (makeOperation true (fn [x y] (Math/pow x y)) '** #()))
(def Sum (makeOperation false + 'sum (sumDiff (fn [] Sum))))
(def Avg (makeOperation false
                        (fn [& args] (/ (apply + args) (count args)))
                        'avg
                        (sumDiff (fn [] Avg))))

(def evaluate _evaluate)
(def toString _toString)
(def toStringSuffix _toStringSuffix)
(def toStringInfix _toStringInfix)
(def diff _diff)

(def operationsMap {'+ [Add 2 false], '- [Subtract 2 false] , '* [Multiply 3 false],
                    '/ [Divide 3 false], 'negate [Negate -1 false], 'sum [Sum -1 false],
                    'avg [Avg -1 false] '** [Pow 4 true], (symbol "//") [Log 4 true]})
(defn mapFunc [[a _ _]] a)
(defn mapOrder [[_ a _]] a)
(defn mapRight [[_ _ a]] a)
(def parseObject (comp (parse Constant Variable operationsMap mapFunc) read-string))

; hw 12

(defn -return [value tail] {:value value :tail tail})
(def -valid? boolean)
(def -value :value)
(def -tail :tail)
(defn _show [result]
  (if (-valid? result) (str "-> " (pr-str (-value result)) " | " (pr-str (apply str (-tail result))))
                       "!"))
(defn tabulate [parser inputs]
  (run! (fn [input] (printf "    %-10s %s\n" (pr-str input) (_show (parser input)))) inputs))
;simple functions
(defn _empty [value] (partial -return value))
(defn _char [p]
  (fn [[c & cs]]
    (if (and c (p c)) (-return c cs))))
(defn _map [f result]
  (if (-valid? result)
    (-return (f (-value result)) (-tail result))))
(defn _myMap [f result]
  (if (-valid? result)
    (f result)))
(defn _combine [f a b]
  (fn [str]
    (let [ar ((force a) str)]
      (if (-valid? ar)
        (_map (partial f (-value ar))
              ((force b) (-tail ar)))))))
(defn _either [a b]
  (fn [str]
    (let [ar ((force a) str)]
      (if (-valid? ar) ar ((force b) str)))))
(defn _parser [p]
  (fn [input]
    (-value ((_combine (fn [v _] v) p (_char #{\u0000})) (str input \u0000)))))
;Combinators
(def +parser _parser)
(defn +char [chars] (_char (set chars)))
(defn +char-not [chars] (_char (comp not (set chars))))
(defn +map [f parser] (comp (partial _map f) parser))
(defn +myMap [f parser] (comp (partial _myMap f) parser))
(def +ignore (partial +map (constantly 'ignore)))
(defn iconj [coll value]
  (if (= value 'ignore) coll (conj coll value)))
(defn +seq [& ps]
  (reduce (partial _combine iconj) (_empty []) ps))
(defn +seqf [f & ps] (+map (partial apply f) (apply +seq ps)))
(defn +seqn [n & ps] (apply +seqf (fn [& vs] (nth vs n)) ps))
(defn +or [p & ps]
  (reduce _either p ps))
(defn +opt [p]
  (+or p (_empty nil)))
(defn +star [p]
  (letfn [(rec [] (+or (+seqf cons p (delay (rec))) (_empty ())))] (rec)))
(defn +plus [p] (+seqf cons p (+star p)))
(defn +str [p] (+map (partial apply str) p))
;Simple parsers
(def *digit (+char ".,0123456789"))
(def *space (+char " \t\n\r"))
(def *ws (+ignore (+star *space)))
(def *all-chars (mapv char (range 32 128)))
(defn *seq [begin p end]
  (+seqn 1 (+char begin) (+opt (+seqf cons *ws p (+star (+seqn 1 *ws (+char ",") *ws p)))) *ws (+char end)))

(def *number (+map read-string (+str (+plus *digit))))
(def *signNumber (+map read-string (+str (+seqf cons (+opt (+char "-")) (+plus *digit)))))
(def toConstant (+map Constant *signNumber))

(def *letter (+char (apply str (filter #(Character/isLetter %) *all-chars))))
(def *symbol (+str (+seq (+char "+-*/") (+opt (+char "*/")))))
(def *identifier (+str (+plus *letter)))

(defn parseToken [a b]
  #(let [symb (get operationsMap (symbol (str (-value %))))]
     (if (-valid? symb)
       (a % symb)
       (b %))))
(def toVariable (+myMap
                  (parseToken (constantly nil)
                              #(-return (Variable (-value %)) (-tail %)))
                  *identifier))
(def *symbols (+str (+plus *symbol)))
(def toOperationToken (+myMap
               (parseToken #(-return %2 (-tail %1))
                           (constantly nil))
               (+or *identifier *symbol)))

(defn *infSeq [p]
  (+seqn 1 *ws (+char "(") *ws p *ws (+char ")")))

(defn binaryOperation [prior pars]
   (fn [expr]
    ((fn rec [a]
       ((fn [f]
          (if (-valid? f)
            (let [priorF (mapOrder (-value f))]
              (if (or (> priorF prior) (and (= priorF prior) (mapRight (-value f))))
                ((fn [second]
                   (rec (-return (apply (mapFunc (-value f)) (mapv -value (vector a second)))
                                 (-tail second))))
                 ((binaryOperation priorF pars) (-tail f)))
                a))
            a))
        ((+seqn 0 *ws toOperationToken) (-tail a))))
   ((+seqn 0 *ws pars) expr))))


(defn notBinaryOperation [p]
  (fn [expr]
    ((fn [a]
       (if (and (-valid? a) (> 0 (mapOrder (-value a))))
             (let [argSeq (+seqn 1 (+plus *space) (force p))
                   args ((+or (+plus argSeq)
                              (*infSeq (+seqf cons *ws (force p) (+star argSeq)))) (-tail a))]
               (-return (apply (mapFunc (-value a)) (-value args))
                        (-tail args)))
        ))
     ((+seqn 0 *ws toOperationToken) expr))))


(def parseObjectInfix
  (letfn [(*arg [p]
            (delay (+or toConstant
                        toVariable
                        (*infSeq p)
                        (notBinaryOperation (*arg p)))))
          (*value []
            (delay
              (binaryOperation 0 (*arg (*value)))))]
    (+parser (+seqn 0 *ws (*value) *ws))))
