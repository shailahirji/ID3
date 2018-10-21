/*
Author:Shaila Hirji
Course: CS460 Machine Learning, Bellevue College
Professor: Alfred Nehme
 */

import java.util.ArrayList;
public class Node {

    public Node parent;
    public ArrayList<Node> children;
    public Object details;

    public static int max_Children;

    public Node(Object details){
        this.details=details;
        children=new ArrayList<>(max_Children);

    }

    public void addChild(Node childNode){
        children.add(childNode);

    }

    public void addParent(Node parent){
        this.parent=parent;
    }

}
