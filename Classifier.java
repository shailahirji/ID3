/*
     Author:Shaila Hirji
     Course: CS460 Machine Learning, Bellevue College
     Professor: Alfred Nehme

     -This class classifies a given set of instances as boolean Yes or No
     -This class calculates the accuracy of the model based on the experimental Vs theoratical classification
     of the given instance

 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;

public class Classifier {

    private String[][] test_data; //get classify_data data
    private HashMap<String, Integer> attribute_index;
    private NaryTree decisionTree;
    private String[] results;
    private String[] actual_results;
    private String file_actual_results;

    public Classifier(String[][] data, NaryTree dt, int col, String file) {
        this.test_data = data;
        attribute_index = new HashMap<>();
        decisionTree = dt;
        results = new String[data.length];
        actual_results = new String[data.length];
        this.file_actual_results = file;

        get_attribute_index(col);
        classify_data();
    }

    private void classify_data() {
        for (int row = 1; row < test_data.length; row++) {
            String[] instance = test_data[row];
            String result = classify_data(decisionTree.root, instance);
            results[row] = result;
            System.out.println(row + ": " + result);
        }
        System.out.println("Accuracy of the model: " + calculate_accuracy(file_actual_results) + "%");
    }

    /*
    This method calculates how accurately our model can classify given instances
     */
    public double calculate_accuracy(String file) {

        BufferedReader br = null;
        String line = "";
        double accuracy = 0;

        //file line of .cvs is attribute names
        try {
            br = new BufferedReader(new FileReader(file));

            line = br.readLine();

            for (int i = 1; i < test_data.length; i++) {
                if ((line = br.readLine()) != null) {
                    actual_results[i] = line;
                }
            }
        } catch (Exception e) {
            System.out.println("error");
        }

        //calculate calculate_accuracy
        int correct = 0;
        int missClassified = 0;
        for (int i = 1; i < test_data.length; i++) {
            if (results[i].equals(actual_results[i])) {
                correct++;
            } else {
                missClassified++;
            }

        }

        accuracy = (double) correct / (correct + missClassified);

        return accuracy * 100;
    }

    /*
    This method classifies data by running an instance through the decision tree
     */
    private String classify_data(Node node, String[] instance) {

        //case 1: for the root node
        if (node.parent == null) {
            int index = -1;
            Node current = node;
            if (node.children.size() != 0) {
                if (attribute_index.containsKey(node.details)) {
                    index = attribute_index.get(node.details);
                }
                for (Node n : node.children) {
                    if (n.details.equals(instance[index])) {
                        current = n;
                        current.addParent(node);
                        break;
                    }
                }
                return classify_data(current, instance);//once child of root found, recurse
            } else {
                return "no"; //cant be classified bcoz root has no children
            }
        }


        while (node.children.size() > 1) {
            int index = -1;
            Node current = node;

            for (Node child : node.children)
                if (attribute_index.containsKey(child.details)) {
                    index = attribute_index.get(child.details);
                    current = child;
                    for (Node x : child.children) {
                        if (x.details.equals(instance[index])) {
                            current = x;
                            current.addParent(child);
                            return classify_data(current, instance);
                        }
                    }

                }
            return "no";

        }

        if (node.children.size() == 1) {
            for (Node n : node.children) {
                if (n.details.equals("yes")) {
                    return "yes";
                } else if (n.details.equals("no")) {
                return "no";
                }
            }
        }
        return "no"; //default not classifed?

    }


    /*
    This method adds attributes into a map with their corresponding index in an instance
     */
    private void get_attribute_index(int col) {
        for (int i = 0; i < col; i++) {
            attribute_index.put(test_data[0][i], i);
        }

    }

}
