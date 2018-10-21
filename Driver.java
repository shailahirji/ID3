/*
    Author:Shaila Hirji
    Course: CS460 Machine Learning, Bellevue College
    Professor: Alfred Nehme

    This class allow us to do the following
    1. generate decision tree using training data
    2. Run test data, with instances that need to be classified against the generated decision tree
    3. Run part of the training data against the tree to get accuracy of model on training data
    4. Calculate the accuracy of the model on training and testing data
 */
public class Driver {

public static void main(String [] args){

    //weather data
//    ArrayLoader al= new ArrayLoader("weather.csv",15,5,false);
//    ID3 ds= new ID3(al.loadArray(),15,5);
//    ds.run_ID3();
//    ArrayLoader test= new ArrayLoader("weather_test.csv",15,4,false);
//    Classifier c= new Classifier(test.loadArray(), ds.getDecisionTree(),4,"weather_results.csv");


    //loan data

    //build tree
    ArrayLoader al= new ArrayLoader("train_12_900.csv",901,12,true);
    ID3 ds= new ID3(al.loadArray(),901,12);
    ds.run_ID3();


    System.out.println("Accuracy test on Training data");
    //build 2D array for sample training data to be classified
    ArrayLoader classify_data= new ArrayLoader("train_100_to_classify.csv",101,11,true);
    //classify samples training data against model, check accuracy and overfitting
    Classifier c= new Classifier(classify_data.loadArray(), ds.getDecisionTree(),11,"training_actual_result.csv");

    System.out.println();
    System.out.println();

    System.out.println("Accuracy test on testing data");
    //build 2D array for sample testing data to be classified
    ArrayLoader test1= new ArrayLoader("test_100_to_classify.csv",101,11,true);
    //classify samples testing data against model and check accuracy
    Classifier c2= new Classifier(test1.loadArray(), ds.getDecisionTree(),11,"test_actual_results.csv");


}

}
