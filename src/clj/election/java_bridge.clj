(ns election.java-bridge)

(defn random-uuid []
  (str (java.util.UUID/randomUUID)))
