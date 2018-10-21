/*
     Author:Shaila Hirji
     Course: CS460 Machine Learning, Bellevue College
     Professor: Alfred Nehme
    This class builds a tree with N number of children at every node
 */
import java.util.ArrayList;

public class NaryTree {

    public Node root;

    public NaryTree(int k){
        Node.max_Children=k;
    }

    public Node addRoot(Object details){
        root=new Node(details);
        root.parent=null;
        root.children=new ArrayList<Node>(Node.max_Children);
        return root;
    }

    public Node addNewNodeVasithChildOfNodeU(Node u,Object info){
        if(!u.details.equals(info)) {
            Node child = new Node(info);
            u.addChild(child);

            return child;
        }else{
            return u;
        }
    }

    public int numberOfNodesInTree(Node rootNode){
        int count=0;

        count++;
        if(rootNode.children.size()!=0){
            for(Node n:rootNode.children){
                count=count+numberOfNodesInTree(n);
            }
        }
        return count;
    }

    public int numberNodesInTree(){
        return numberOfNodesInTree(this.root);
    }

    public void changeRoot(Node newRoot, int i){
        Node oldRoot=this.root;
        newRoot.parent=null;
        newRoot.addChild(oldRoot);
        oldRoot.parent=newRoot;
        this.root=newRoot;

    }

    public void traverseTree(Node parent,Node current){
       //System.out.println(root.details);

        //base case parent is null
//        if(current.children.size()!=0){
//            for(Node n:current.children) {
//                System.out.println(current.details);
//                traverseTree(n);
//
//            }
//        }else if(current.details.equals("yes")||current.details.equals("no")){
//            System.out.println(current.details);
//        }
//
//        System.out.println();



        //for root
        if(parent==null){
            parent=current;
            //System.out.println(current.details);
        }
        //one child
        if(current.children.size()!=0){
            if(current.children.size()==1){
               // System.out.println(current.details);

                traverseTree(current,current.children.get(0));
            }else{
                for(Node n:current.children){
                    //if(!current.equals(parent)){System.out.println(parent.details);}
                    //if(!current.equals(parent)){System.out.println(current.details);}
                    traverseTree(current,n);
                }
            }
        }else if(current.details.equals("yes")||current.details.equals("no")){
            //System.out.println(current.details);
           // System.out.println();
        }


    }
}
