import java.io.BufferedReader;
import java.io.FileReader;

public class ArrayLoader {


    String fileName;
    int rows;
    int col;
    Boolean contains_continious;

    public ArrayLoader(String fileName,int rows,int col,Boolean contains_continious){
        this.fileName=fileName;
        this.rows=rows;
        this.col=col;
        this.contains_continious=contains_continious;
        loadArray();

    }

    public String [][]loadArray(){
            String [][] data = new String[rows][col];

                BufferedReader br = null;
                String line = "";
                int counter = 0;

                    //file line of .cvs is attribute names
                    try {
                        br = new BufferedReader(new FileReader(fileName));

                        line = br.readLine();
                        //fill in header
                        if (counter == 0) {
                            for (int j = 0; j < col; j++) {
                                String[] input = line.split(",");
                                data[counter][j] = input[j];
                            }
                        }
                        counter++;
                        while ((line = br.readLine()) != null) {

                            //use comma as separator
                            for (int j = 0; j < col; j++) {
                                String[] input = line.split(",");
                                if ((j != 1 && j != 4 && j != 8) || (!contains_continious)) {
                                    data[counter][j] = input[j];
                                } else {
                                    //transform input into ranges
                                    data[counter][j] = getRange(j, input[j]);

                                }
                            }
                            counter++;
                        }

                    } catch (Exception e) {
                        System.out.println("caught exception");
                    }


            return data;
        }


    private String getRange(int att_number,String value){
        switch (att_number) {
            case 1://months
                return valueToRange(value, 4, 75, 17);
            case 4://amount
                return valueToRange(value, 250, 19000, 750);

            case 8://age
                return valueToRange(value, 19, 75, 7);

        }
        return null;
    }

    private String valueToRange(String attributeValue, int smallest, int largest, int increment) {
        String range = "";
        int lower = smallest;//smallest value for attribute
        int max=largest;//largest value for attribute
        int upper = increment+lower; //increment by

        int value = Integer.parseInt(attributeValue);

        while (range.equals("")) {

            if (value < lower) {
                range = "<"+lower;
            } else if (value >= lower && value <= upper && upper <= max) {
                range = lower + "-" + upper;

            }else if(value>max) {
                range=max+" <";
            }

            else {
                lower = upper + 1;
                upper = lower + increment;
            }
        }
        return range;
    }
}
