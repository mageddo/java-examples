package com.mageddo.jaeger.spark;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.elasticsearch.spark.rdd.api.java.JavaEsSpark;

public class Main {
	public static void main(String[] args) {
		final SparkConf sparkConf = new SparkConf()
			.setAppName("WordCount")
			.setMaster("local[6]")
		;

		final JavaSparkContext sc = new JavaSparkContext(sparkConf);
		sc.setLogLevel("ERROR");

		JavaPairRDD<String, Iterable<Span>> traces = JavaEsSpark.esJsonRDD(sc, )
			.map(new ElasticTupleToSpan())
			.groupBy(Span::getTraceId);


	}
}
