/*
     Author:Shaila Hirji
     Course: CS460 Machine Learning, Bellevue College
     Professor: Alfred Nehme
    -This class generates a decision tree(N-Ary tree) based on ID3 Algorithm
    -The ID3 Object receives a 2D array that contains data
    -The data is split into attributes and its corresponding attribute values
    -Information gain calculations are then performed allowing us to build the decision tree for the data
    -The information gain is only computed on a data set with more than 60 instances to prevent the model from over fitting

 */
import java.util.*;
public class ID3 {

    private ArrayList<Attribute> attribute_infoGain;
    private int initial_dataSize;
    private int numberAttributes;
    private HashMap<String, HashSet<AttributeValue>> attributeMap;
    private LinkedList<String> visited = new LinkedList<>();
    private boolean root_found = false;
    private String[][] data;
    private NaryTree decisionTree;
    private Node current;


    public ID3(String[][] ds, int rows, int columns) {
        this.data = ds;
        initial_dataSize = rows;
        numberAttributes = columns;
        attribute_infoGain = new ArrayList<>();
        attributeMap = new HashMap<>();
        decisionTree = new NaryTree(100);


    }

    public void run_ID3() {
        //assign data as attributes and their associated values
        readAttributes(data, initial_dataSize, numberAttributes);
    }

    /*
    Prepares data for informationGain calculations
    Splits the data in 2D array into Attributes and Attribute values bu adding them into AttributeMap <Attribute, HashSet<AttributeValues>
     */
    public void readAttributes(String[][] data, int rows, int columns) {

        //keep count of datas classifiers
        int positives = 0;
        int negatives = 0;

        for (int col = 0; col < columns - 1; col++) {//columns-1 coz we don't want the last classifer colomn

            String name = data[0][col];//gets table header at given index
            HashSet<AttributeValue> values = new HashSet<>();//prepares hashset for attribute values linked to attribute

            //add the possible attribute values for this colomn into HashSet of Attribute values
            for (int row = 1; row < rows; row++) {

                AttributeValue av = new AttributeValue(name, data[row][col]);
                values.add(av);//add this new att value into HashSet, if already exists, no change

                //get classifier, increment count of the AV based on how the data is classified, Y/N
                String classifier = data[row][columns - 1];

                for (AttributeValue x : values) {
                    //update the counter of the attribute value
                    if (x.getValue().equals(data[row][col])) { //if AV in data matches that to HashSet, increment classifier count accordingly
                        x.setApperances(classifier);
                    }

                }

                //count total yes, total no in whole data set
                if (col == 0) {
                    if (classifier.equals("yes")) {
                        positives++;
                    } else {
                        negatives++;
                    }
                }
            }
            //add Attribute(key)->HashSet of AttributeValues<value> pair into map
            attributeMap.put(name, values);
        }

        calculateInfoGain(data, positives, negatives, positives + negatives);
    }


    /*
    Calculates the information gain for a given data set
    determines the next attribute to split at
    Calls on ReadAttributes to prepare data for next info gain calculation based on new split Att

     */
    public void calculateInfoGain(String[][] data, int yes, int no, double data_size) {

        //--------------INFORMATION GAIN CALCULATIONS---------------//


        //overall Entropy, using total Y and total N
        double entropyDS = entropyFormula(yes, no);

        attribute_infoGain = new ArrayList<>();//holds each attribute and its assigned IG value

        //summation of Entropies per Attribute

        for (String attribute_name : attributeMap.keySet()) {
            double summation = 0;
            for (AttributeValue av : attributeMap.get(attribute_name)) {
                double entropy_av = (av.getTotalApperance() / data_size) * (entropyFormula(av.getApperancesYes(), av.getApperancesNo()));
                summation += entropy_av;
            }
            attribute_infoGain.add(new Attribute(attribute_name, entropyDS - summation));
        }

        //get largest info gain attribute
        Collections.sort(attribute_infoGain);
        Attribute splitAttribute = attribute_infoGain.get(attribute_infoGain.size() - 1);//get highest Attribute


        //--------------END OF INFORMATION GAIN CALCULATIONS---------------//


        //-------INVESTIGATE IF SPLIT ATTRIBUTE'S ATTRIBUTE VALUE HAS IMMEDIATE LEAF, YES OR NO-----//

        HashSet<AttributeValue> av_toBe_removed = new HashSet<>();//keeps track of attribute values with leafs so we can remove from AttributeMap


        //root attribute case
        if (!root_found) {
//            System.out.println("(ROOT) Split att-->" + splitAttribute.getName() + "   ");
//            System.out.println();
            //add the root into our tree
            current = decisionTree.addRoot(splitAttribute.getName());
        }

        for (AttributeValue av : attributeMap.get(splitAttribute.getName())) {

            if (!check_leaf(av).equals("no leaf")) {
                visited.add(splitAttribute.getName() + " " + av.getValue());
//                        System.out.println(attribute_infoGain.get(attribute_infoGain.size() - 1).getName() + ", ");
//                        System.out.println(av + "--->" + check_leaf(av) + " ");
//                        System.out.println();
                av_toBe_removed.add(av);

                Node temp = decisionTree.addNewNodeVasithChildOfNodeU(current, splitAttribute.getName());
                temp = decisionTree.addNewNodeVasithChildOfNodeU(temp, av.getValue());
                decisionTree.addNewNodeVasithChildOfNodeU(temp, check_leaf(av));

            }
        }

        //remove the attributeValue from map
        for (AttributeValue att : av_toBe_removed) {
            attributeMap.get(splitAttribute.getName()).remove(att);
        }

        root_found = true;

        //----------END OF INVESTIGATE IF SPLIT ATTRIBUTE'S ATTRIBUTE VALUE HAS IMMEDIATE LEAF---------//


                /*  Will re-initialize each time split Attribute updates
                    Enables us to recurse and make sure that we analyze each path of the tree but visiting every AV linked to the Attribute
                 */
        PriorityQueue<AttributeValue> av_to_visit = new PriorityQueue<>();

        //pick one of the attributes of splitting feature
        for (AttributeValue av : attributeMap.get(splitAttribute.getName())) {
            av_to_visit.add(av);//this will allow us to focus our dataSet to only where the av is the given one
        }

        Node parent = current;
        while (!av_to_visit.isEmpty()) {//this for loop picks an attribute value of split attribute


            Node temp = new Node(splitAttribute.getName());
            temp = decisionTree.addNewNodeVasithChildOfNodeU(parent, temp.details);
            current = decisionTree.addNewNodeVasithChildOfNodeU(temp, av_to_visit.peek().getValue());

            //-----------------CREATE NEW DATA SET RESTRICTED TO SELECTED SPLIT ATTRIBUTE------------------//

            int attribute_col = -1;
            //find the col where header's title is your constrained attribute i.e. split attribute
            for (int col = 0; col < numberAttributes - 1; col++) {
                if (data[0][col].equals(splitAttribute.getName())) {
                    attribute_col = col;
                }
            }

            data_size = (av_to_visit.peek()).getTotalApperance() + 1;
            String[][] dataSet_refined = new String[(int) data_size][numberAttributes];

            for (int k = 0; k < numberAttributes; k++) { //add header row into new data set
                dataSet_refined[0][k] = data[0][k];
            }

            //build new data set with selected AV
            int new_dataArray_counter = 1;
            //if data has desired AV, add the entire row of data into new dataset
            for (int row_src = 1; row_src < data.length; row_src++) {
                if (av_to_visit.size() != 0 && data[row_src][attribute_col].equals(av_to_visit.peek().getValue())) {
                    for (int j = 0; j < numberAttributes; j++) {//gets whole row of data into refined data array
                        dataSet_refined[new_dataArray_counter][j] = data[row_src][j];
                    }
                    new_dataArray_counter++;
                }
            }


            //-----------------END OF CREATE NEW DATA SET RESTRICTED TO SELECTED SPLIT ATTRIBUTE------------------//

            //now have a new data set to calculate entropy, your input data will change,

            //current becomes our new node that's being investigated

            AttributeValue selected_av = av_to_visit.peek();

            visited.add(splitAttribute.getName() + " " + selected_av.getValue());

            if (dataSet_refined.length > 60) {
                //create attributes and attribute values of the new data set, preparing IG calculations on new data set
                if (dataSet_refined[1][0] != null)
                    readAttributes(dataSet_refined, (int) data_size, numberAttributes);
            }
            //remove from av_to_visit Queue so you observe the next attribute value
            av_to_visit.remove();
        }

    }


    /*
    This method checks if there is a classifier Y/N for the the given attribute or does it branch to another attribute
     */
    private String check_leaf(AttributeValue av) {
        //checks if theres all yes or all no for any attribute given its a split feature

        if (av.getApperancesYes() != 0 && av.getApperancesNo() == 0) {
            return "yes";
        } else if (av.getApperancesNo() != 0 && av.getApperancesYes() == 0) {
            return "no";
        } else {
            return "no leaf";
        }
    }

    public NaryTree getDecisionTree() {
        return decisionTree;
    }

    private double entropyFormula(double positive, double negative) {
        double entropy = 0;
        double total = positive + negative;
        if (positive != 0 && negative != 0) {
            entropy = ((-1) * (positive / total) * (Math.log(positive / total) / Math.log(2))) - ((negative / total) * (Math.log(negative / total) / Math.log(2)));
        }
        return entropy;

    }
}

