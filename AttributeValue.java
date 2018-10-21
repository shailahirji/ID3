

//for the attributes different values
public class AttributeValue implements Comparable<AttributeValue> {

    private String value;
    private double yes_count;//defaulted
    private double no_count;//not defaulted,

    public AttributeValue(String attribute, String value) {

        this.value=value;
        this.yes_count = 0;
        this.no_count = 0;

    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public double getApperancesYes() {
        return this.yes_count;
    }

    public double getApperancesNo() {
        return this.no_count;
    }

    public void setApperances(String classifier) {
        if (classifier.equals("yes")) {
            this.yes_count++; //increment attribute values yes count
        } else {
            this.no_count++;//otherwise increment no count
        }
    }

    public double getTotalApperance() {
        return this.no_count + this.yes_count;
    }

 @Override
    public boolean equals(Object o){

     if(this == o)
         return true;

     if(o == null || o.getClass()!= this.getClass())
         return false;

     // type casting of the argument.
     AttributeValue av = (AttributeValue) o;

     return this.getValue().equals(av.getValue()) ;

    }
    @Override

    public int hashCode()
    {

       char c[]=this.value.toCharArray();
       int ascii=0;
       for(char x:c){
           ascii+=(int)x;
       }
        return ascii;
    }

    @Override
    public String toString(){
        return this.value;
    }

    @Override
    public int compareTo(AttributeValue o) {
        if (this.getTotalApperance()-o.getTotalApperance() == 0)
            return 0;
        else if (this.getTotalApperance()-o.getTotalApperance() < 0)
            return -1;
        else
            return 1;
    }
}
