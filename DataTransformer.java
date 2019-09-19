package assignment1;

import java.io.*;
import java.util.*;

/**
 * Store and transform matrix of data
 *
 * @author Jan
 */
public class DataTransformer {

    // Define symbols to use in the program as a way of knowing what we're seeing in the text.
    private static final String TAB_SEPARATOR = "\t";
    public static final String SPACES_SEPARATOR = "\\s+";

    // Define validate symbol for calculate method in equationCode
    private static final String NUMBER = "111200";
    private static final String COLUMN = "111300";
    private static final String NUMBER_NUMBER = "111212";
    private static final String NUMBER_COLUMN = "111213";
    private static final String COLUMN_NUMBER = "111312";
    private static final String COLUMN_COLUMN = "111313";

    // Define length to differentiate "a = b" and "a = b c d" by length
    public static final int ASSIGN_VALUE = 3; // "a = b"
    public static final int CALCULATE_VALUE = 5; // "a = b c d"

    // data storage
    private List<List<String>> dataList = new ArrayList<>();
    private int dataRows = 0;

    //----------------------------------------------------------------------------// 
    // clear data storage
    public boolean clear() {
        reset();
        return dataList.isEmpty();
    }

    // reset data storage
    public void reset() {
        dataList.clear();
        dataRows = 0;
    }

    //----------------------------------------------------------------------------//
    // read data from file and store in data storage
    public Integer read(String filename) {
        String input;
        reset();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            // read data from file
            while ((input = br.readLine()) != null) {
                String[] line = input.split(TAB_SEPARATOR);
                List<String> row = new ArrayList<>();
                // add data to data storage
                for (String item : line) {
                    row.add(item);
                }
                dataList.add(row);
            }
            // update number of rows in data storage
            dataRows = dataList.size() - 1;
        } catch (Exception e) {
            // if file doesn't exist, null & empty file name, then return -1 
            return -1;
        }
        return dataRows >= 1 ? dataRows : 0;
    }

    //----------------------------------------------------------------------------//
    // add new column to data storage and add default value in row to 0
    public boolean newColumn(String columnName) {
        List<String> dataHeader;
        if(dataList.size() < 1){
            return false;
        } else {
            dataHeader = dataList.get(0);
        }
        
        if (ParameterValidator.invalidNewColumn(columnName, dataHeader)) {
            return false;
        } else {
            // add new column
            dataList.get(0).add(columnName);
            for (int i = 1; i < dataList.size(); i++) {
                dataList.get(i).add(String.valueOf(0));
            }
            return true;
        }
    }

    //----------------------------------------------------------------------------//
    // calculate the new values in data storage using equation user input
    public Integer calculate(String equation) {
        // no row to do calculation, return 0
        if (dataRows <= 0) {
            return 0;
        }

        List<String> dataHeader = dataList.get(0);
        List<String> dataSample = dataList.get(1);
        int rowChanged;

        String equationCode = ParameterValidator.validateEquation(equation, dataHeader, dataSample);
        if(invalidCode(equationCode)) {
            return -1;
        }

        // assign value and do calculation
        String[] splitEquation = equation.split(SPACES_SEPARATOR);
        String equationA = splitEquation[0];
        String equationB = splitEquation[2];
        String equationOP = "";
        String equationD = "";
        if(splitEquation.length == CALCULATE_VALUE){
            equationOP = splitEquation[3];
            equationD = splitEquation[4];
        }

        int index = columnNameIndex(equationA, dataHeader);
        List<Double> list1;
        List<Double> list2;
        int columnIndex1;
        int columnIndex2;

        switch (equationCode) {
            case NUMBER:
                // assign number to column (a = number)
                list1 = createNumberList(equationB);
                rowChanged = setValues(index, list1);
                break;
            case COLUMN:
                // assign another column value to column name (a = column name)
                columnIndex1 = columnNameIndex(equationB, dataHeader);
                list1 = createColumnList(columnIndex1);
                rowChanged = setValues(index, list1);
                break;
            case NUMBER_NUMBER:
                // a = number (+, -, *, /) number
                if (divideByZero(equationOP, equationD)){
                    return 0;
                }
                list1 = createNumberList(equationB);
                list2 = createNumberList(equationD);
                rowChanged = setValues(index, equationOP, list1, list2);
                break;
            case NUMBER_COLUMN:
                // a = number (+, -, *, /) column name
                list1 = createNumberList(equationB);
                columnIndex2 = columnNameIndex(equationD, dataHeader);
                list2 = createColumnList(columnIndex2);
                rowChanged = setValues(index, equationOP, list1, list2);
                break;
            case COLUMN_NUMBER:
                // a = column name (+, -, *, /) number
                if (divideByZero(equationOP, equationD)) {
                    return 0;
                }
                columnIndex1 = columnNameIndex(equationB, dataHeader);
                list1 = createColumnList(columnIndex1);
                list2 = createNumberList(equationD);
                rowChanged = setValues(index, equationOP, list1, list2);
                break;
            case COLUMN_COLUMN:
                // a = column name (+, -, *, /) column name
                columnIndex1 = columnNameIndex(equationB, dataHeader);
                list1 = createColumnList(columnIndex1);
                columnIndex2 = columnNameIndex(equationD, dataHeader);
                list2 = createColumnList(columnIndex2);
                rowChanged = setValues(index, equationOP, list1, list2);
                break;
            default:
                // none case matched
                rowChanged = -1;
                break;
        }
        return rowChanged;
    }

    // invalid code for equationCode
    private boolean invalidCode(String equationCode){
        boolean result;
        switch(equationCode){
            case NUMBER:
            case COLUMN:
            case NUMBER_NUMBER:
            case NUMBER_COLUMN:
            case COLUMN_NUMBER:
            case COLUMN_COLUMN:
                result = false;
                break;
            default:
                result = true;
                break;
        }
        return result;
    }
    
    private boolean divideByZero(String equationOP, String equationD){
        return equationOP.equals("/") && equationD.equals("0");
    }
    
    // find index of column from column name
    public static int columnNameIndex(String columnName, List<String> dataHeader) {
        int index = -1;

        for (String existingColumn : dataHeader) {
            // if column exists, assign index
            if (existingColumn.equalsIgnoreCase(columnName)) {
                index = dataHeader.indexOf(existingColumn);
            }
        }

        return index;
    }

    // create list of number for calculating in calcuate method
    private List<Double> createNumberList(String v1) {
        List<Double> list = new ArrayList<>();
        list.add(0.0);
        for (int i = 1; i < dataList.size(); i++) {
            list.add(Double.parseDouble(v1));
        }
        return list;
    }

    // create list of column for calculating in calcuate method
    private List<Double> createColumnList(int v1Index) {
        List<Double> list = new ArrayList<>();
        list.add(0.0);
        for (int i = 1; i < dataList.size(); i++) {
            list.add(Double.parseDouble(dataList.get(i).get(v1Index)));
        }
        return list;
    }

    // set values in "a = b"
    private int setValues(int index, List<Double> list) {
        for (int i = 1; i < dataList.size(); i++) {
            dataList.get(i).set(index, String.format("%.0f", list.get(i)));
        }
        return dataRows;
    }

    // set values in "a = b c d"
    private int setValues(int index, String operator, List<Double> list1, List<Double> list2) {
        int nonOperateValue = 0;
        for (int i = 1; i < dataList.size(); i++) {
            double num1 = list1.get(i);
            double num2 = list2.get(i);
            if(operator.equals("/") && num2 == 0){
                nonOperateValue++;
                continue;
            }
            dataList.get(i).set(index, String.valueOf(operation(num1, operator, num2)));
        }
        return dataRows - nonOperateValue;
    }

    // do calculation according to given operator
    private int operation(double num1, String operator, double num2) {
        double value = 0;
        switch (operator) {
            case "+":
                value = num1 + num2;
                break;
            case "-":
                value = num1 - num2;
                break;
            case "*":
                value = num1 * num2;
                break;
            case "/":
                value = num1 / num2;
                break;
        }
        return (int) Math.round(value);
    }

    //----------------------------------------------------------------------------//
    
    // see top 5 rows of data
    public void top() {
        showData(5);
    }

    //----------------------------------------------------------------------------//
    
    // print all data
    public void print() {
        showData(dataList.size());
    }

    // show data in data storage according to limit set
    private void showData(int limit) {
        int count = 0;
        for (List<String> row : dataList) {
            for (String item : row) {
                System.out.print(item + TAB_SEPARATOR);
            }
            count++;
            if (count > limit) { // reach limit, then break
                break;
            }
            System.out.println();
        }
    }

    //----------------------------------------------------------------------------//
    
    // write data to the file
    public Integer write(String filename) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename, true))) {
            for (List<String> row : dataList) {
                for (String item : row) {
                    bw.write(item + TAB_SEPARATOR);
                }
                bw.newLine();
            }
        } catch (Exception e) {
            // if file doesn't exist, null & empty file name, then return -1 
            return -1;
        }
        return dataRows >= 1 ? dataRows : 0;
    }
}
