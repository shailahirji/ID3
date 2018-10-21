public class Attribute implements Comparable<Attribute>{

    private String name;

    private double informationGain;

    public Attribute(String name,double ig){
        this.name=name;
        this.informationGain=ig;

    }

    public String getName() {
        return name;
    }


    @Override
    public String toString(){
        return this.name+" "+this.informationGain;
    }

    @Override
    public int compareTo(Attribute o) {
        if (this.informationGain-o.informationGain == 0)
            return 0;
        else if (this.informationGain-o.informationGain < 0)
            return -1;
        else
            return 1;
    }
}


