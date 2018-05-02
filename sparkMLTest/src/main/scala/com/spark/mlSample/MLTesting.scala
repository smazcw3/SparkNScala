package com.spark.mlSample

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

import org.apache.spark.mllib.linalg.{Vector, Vectors}
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.mllib.linalg.{Matrix, Matrices}
import org.apache.spark.mllib.linalg.distributed.{IndexedRow, IndexedRowMatrix, RowMatrix}
import org.apache.spark.mllib.linalg.distributed.MatrixEntry
import org.apache.spark.mllib.linalg.distributed.CoordinateMatrix

import org.apache.spark.mllib.stat.Statistics
import org.apache.spark.mllib.stat.MultivariateStatisticalSummary

import org.apache.spark.mllib.random.RandomRDDs._

object MLTesting {
    def main(args: Array[String]) = {
      
      //Start the Spark context
      val conf = new SparkConf().setAppName("MLTesting").setMaster("local")
      val sc = new SparkContext(conf)
      
      //Dense Vector
      val dense_vector1 = Vectors.dense(44.0, 0.0, 55.0)
      
      //Sparse Vector - Method 1
      val sparse_vector1 = Vectors.sparse(3, Array(0, 2), Array(44.0, 55.0))
      
      //Sparse Vector - Method 2
      val sparse_vector2 = Vectors.sparse(3, Seq((0, 44.0), (2, 55.0)))
      
      //Labeled Point - Method 1      
      val labeled_point1 = LabeledPoint(1.0, Vectors.dense(44.0, 0.0, 55.0))
      
      //Labeled Point - Method 2
      val labeled_point2 = LabeledPoint(0.0, Vectors.sparse(3, Array(0, 2), Array(44.0, 55.0)))
      
      //Dense Matrices
      val dense_matrix1 = Matrices.dense(3, 2, Array(1, 3, 5, 4, 2, 6))
      
      //Sparse Matrices
      val sparse_matrix1 = Matrices.sparse(5, 4, Array(0, 0, 1, 2, 2), Array(1, 3), Array(34, 55))
      
      
      //Distributed Matrices - Row Matrix
      val rows1: RDD[Vector] = sc.parallelize(Array(
          Vectors.dense(1.0, 2.0),
          Vectors.dense(4.0, 5.0),
          Vectors.dense(7.0, 8.0)))
          
      val mat: RowMatrix = new RowMatrix(rows1)
      val m = mat.numRows()
      val n = mat.numCols()
      
      //Distributed Matrices - IndexedRowMatrix
      val rows2: RDD[IndexedRow] = sc.parallelize(Array(
          IndexedRow(0, Vectors.dense(1.0, 2.0)),
          IndexedRow(0, Vectors.dense(4.0, 5.0)),
          IndexedRow(0, Vectors.dense(7.0, 8.0))))
          
      val idxMat: IndexedRowMatrix = new IndexedRowMatrix(rows2)
      
      //Distributed Matrices - CoordinateMatrix
      val entries: RDD[MatrixEntry] = sc.parallelize(Array(
          	MatrixEntry(0, 0, 9.0),
          	MatrixEntry(1, 1, 8.0),
          	MatrixEntry(2, 1, 6.0))) 

      val coordMat: CoordinateMatrix = new CoordinateMatrix(entries)
      
      //Summary Statistics
      val observations: RDD[Vector] = sc.parallelize(Array(
          Vectors.dense(1.0, 2.0),
          Vectors.dense(4.0, 5.0),
          Vectors.dense(7.0, 8.0)))

      val summary: MultivariateStatisticalSummary = Statistics.colStats(observations)
      println(summary.mean)
      println(summary.variance)
      println(summary.numNonzeros)
      
      //Correlations - Method 1
      val X: RDD[Double] = sc.parallelize(Array(2.0, 9.0, -7.0))
      val Y: RDD[Double] = sc.parallelize(Array(1.0, 3.0, 5.0))
      val correlation: Double = Statistics.corr(X, Y, "pearson")
      
      //Correlations - Method 2
      val data: RDD[Vector] = sc.parallelize(Array(
          Vectors.dense(2.0, 9.0, -7.0),
          Vectors.dense(1.0, -3.0, 5.0),
          Vectors.dense(4.0, 0.0, -5.0)))

      val correlMatrix: Matrix = Statistics.corr(data, "pearson")
      
      //Pearson vs Spearman Correlation among Series
      val ranks: RDD[Vector] = sc.parallelize(Array(Vectors.dense(1.0, 2.0, 3.0),
        Vectors.dense(5.0, 6.0, 4.0), Vectors.dense(7.0, 8.0, 9.0)))

      val corrPearsonMatrix: Matrix = Statistics.corr(ranks, "pearson")
      val corrSpearmanMatrix: Matrix = Statistics.corr(ranks, "spearman")
      
      // Random data generation     
      val million = poissonRDD(sc, mean=1.0, size=1000000L, numPartitions=10)
      println(million.mean)
      println(million.variance)
      
      val data2 = normalVectorRDD(sc, numRows=10000L, numCols=3, numPartitions=10)
      val stats: MultivariateStatisticalSummary = Statistics.colStats(data2)
      println(stats.mean)
      println(stats.variance)
      
      //Simple Subsampling
      val elements: RDD[Vector] = sc.parallelize(Array(
        Vectors.dense(4.0, 7.0, 13.0),
        Vectors.dense(-2.0, 8.0, 4.0),
        Vectors.dense(3.0, -11.0, 19.0)))

      elements.sample(withReplacement=false, fraction=0.5, seed=10L).collect().foreach(println)
      elements.sample(withReplacement=false, fraction=0.5, seed=7L).collect().foreach(println)
      elements.sample(withReplacement=false, fraction=0.5, seed=64L).collect().foreach(println)    
      
      //Random split     
      val data3 = sc.parallelize(1 to 1000000)  //creating and RDD of integers from 1 to 1 million
      val splits = data3.randomSplit(Array(0.6, 0.2, 0.2), seed=13L)  //split the RDD into three different datasets containing respectively 60%, 20% and 20% of the original data, it returns an array of RDDs
      
      val training = splits(0)     
      val test = splits(1)
      val validation = splits(2)
      
      println(splits.map(_.count()).mkString(" "))      
      
      //Stop the Spark context
      sc.stop
      
  }
}