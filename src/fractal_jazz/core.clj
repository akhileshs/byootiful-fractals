(ns fractal-jazz.core
  (:import (javax.swing JFrame JLabel)
           (java.awt.image BufferedImage)
           (java.awt Dimension Color))
  (:gen-class))

(defn transform-one [[x y]]
  [0 (* 0.19 y)])

(defn transform-two [[x y]]
  [(- (* 0.23 x) (* 0.29 y))
   (+ (* 0.26 x) (* 0.24 y))])

(defn transform-three [[x y]]
  [(+ (* -0.18 x) (* 0.32 y))
   (+ (* 0.30 x) (* 0.28 y) 0.50)])

(defn transform-four [[x y]]
  [(+ (* 0.89 x) (* 0.09 y))
   (+ (* -0.008 x) (* 0.90 y) 1.8)])

(defn transform
  [target]
  (let [percentage (rand-int 101)]
    (cond
          (<= percentage 3) (transform-one target)
          (<= percentage 10) (transform-two target)
          (<= percentage 17) (transform-three target)
          (<= percentage 100) (transform-four target))))

(defn paint-point
  [width height [x y] graphics]
  (let [scale (int (/ height 11))
        y (- (- height 25) (* scale y))
        x (+ (/ width 2) (* scale x))]
    (.drawLine graphics x y x y)))

(defn paint-fern [width height max-points graphics]
  (doseq [coord (take max-points (iterate transform [0 0]))]
    (paint-point width height coord graphics)))

(defn draw-fractal [width height points]
  (let [image (BufferedImage. width height BufferedImage/TYPE_INT_RGB)
        canvas (proxy [JLabel] []
                 (paint [g]
                   (.drawImage g image 0 0 this)))
        graphics (.createGraphics image)]
    (.setColor graphics Color/red)
    (paint-fern width height points graphics)

    (doto (JFrame.)
      (.add canvas)
      (.setSize (Dimension. width height))
      (.show))))

(defn -main []
  (draw-fractal 600 600 100000))
