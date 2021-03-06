package com.bootcamp.recommendations;
import java.io.File;
import java.util.List;

import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;

import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;

import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;

import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.UserBasedRecommender;

import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.eval.AverageAbsoluteDifferenceRecommenderEvaluator;
import org.apache.mahout.cf.taste.recommender.Recommender;
 
public class UserBasedWithEvaluation
{
    public static void main( String[] args )
    {
        try {
          DataModel model = new FileDataModel(new File("/home/nadine/collaborativenotes/book_ratings2.csv"));
          RecommenderBuilder builder = new RecommenderBuilder() {
            public Recommender buildRecommender(DataModel model) {
              try {
                UserSimilarity similarity = new PearsonCorrelationSimilarity(model);
                //UserNeighborhood neighborhood = new ThresholdUserNeighborhood(0.9, similarity, model);
                UserNeighborhood neighborhood =new NearestNUserNeighborhood(30, similarity, model);
                return new GenericUserBasedRecommender(model, neighborhood, similarity);
              } catch(Exception e) {
                System.out.println("Error :(");
                System.out.println(e.getMessage());
              }
              return null;
            }
          };
          RecommenderEvaluator evaluator = new AverageAbsoluteDifferenceRecommenderEvaluator();
          //trainingPercentage (0.7) - percentage of each user's preferences to use to produce recommendations; the rest are compared to estimated preference values to evaluate Recommender performance
          //evaluationPercentage (1.0) - percentage of users to use in evaluation
          double evaluation = evaluator.evaluate(builder, null, model, 0.7, 1.0);
          System.out.println(String.valueOf(evaluation));
        } catch(Exception e) {
              System.out.println("Error :(");
              System.out.println(e.getMessage());
        }
    }
}

