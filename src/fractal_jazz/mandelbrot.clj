(ns fractal-jazz.mandelbrot
  (:import (javax.swing JFrame JLabel)
           (java.awt.image BufferedImage)
           (java.awt Dimension Color)
           (java.lang Number)
           (org.apache.commons.math.complex Complex)))

;; based off of @nakkaya's algorithm:
;;  1. Choose a rectangle in the complex plane.
;;  2. Map the image to the chosen complex plane.
;;  3. Calculate the no of iterations that are needed for the point.
;;  4. Paint the point according to it's iteration.
;;  Be amazed!!

(defn calc-iterations [p q max-iterations]
  (let [c (Complex. p q)]
    (loop [z c
           iterations 0]
      (if (or (> (.abs z) 2.0)
              (> iterations max-iterations))
        (if (= 0 iterations)
          0
          (- iterations 1))
        (recur (.add c (.multiply z z)) (inc iterations))))))

(defn find-pixel-color [iterations max-iterations]
  (if (or (< iterations 10)
          (= iterations max-iterations))
    (Color. 0 0 0)
    (let [gray (int (/ (* iterations 255) max-iterations))
          r    gray
          g    (min (int (/ (* 3 (* gray gray)) 255)) 255)
          b    (min (int (+ 25 (/ (* 5 (* gray gray)) 255))) 255)]
      (Color. r g b))))

(defn generate-fractal [x y width height max-iterations
               graphics surface-width surface-height]
  (doseq [i (range surface-width)
          j (range surface-height)]
    (let [p (+ x (* width (/ i surface-width)))
          q (+ y (* height (/ j surface-height)))
          iterations (calc-iterations p q max-iterations)
          color (find-pixel-color iterations max-iterations)]
      (.setColor graphics color)
      (.drawLine graphics i j i j))))

(defn draw [x y width height iterations surface-width surface-height]
  (let [image (BufferedImage. surface-width surface-height BufferedImage/TYPE_INT_RGB)
        canvas   (proxy [JLabel] []
                   (paint [g]
                     (.drawImage g image 0 0 this)))]
    (generate-fractal x y width height iterations 
              (.createGraphics image) surface-width surface-height)

    (doto (JFrame.)
      (.add canvas)
      (.setSize (Dimension. surface-width surface-height))
      (.show))))
