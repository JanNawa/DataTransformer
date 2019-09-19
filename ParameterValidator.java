package assignment1;

import java.util.*;

/**
 * Validate parameter according to method it assigns to
 * 
 * @author Jan
 */
public class ParameterValidator {
    
    // Define symbols to use in the program as a way of knowing what we're seeing in the text.
    private static final String NOT_ALPHABET = ".*[^a-zA-Z].*";
    private static final String EQUAL_SIGN = "=";
    private static final String VALID_OPERATORS = "[+|\\-|\\*|/]";
    private static final String DECIMAL_NUMBER = "-?\\d+(\\.\\d+)?";
    private static final String DECIMAL_WITHOUT_ZERO = "-?\\.\\d+";
    
    // Define value for using in equationCode in validateEquation method
    public static final int INVALID_EQUATION = -1;
    public static final int VALID_EQUATION = 1;
    public static final int NUMBER = 2;
    public static final int COLUMN_NAME = 3;
    
    //----------------------------------------------------------------------------//
    
    // check if new column is invalid
    public static boolean invalidNewColumn(String columnName, List<String> dataHeader){
        return invalidInput(columnName) 
                || isColumnExists(columnName, dataHeader);
    }

    // check if column name is invalid
    private static boolean invalidInput(String columnName) {
        return columnName == null
                || columnName.trim().isEmpty()
                || columnName.contains(" ")
                || columnName.matches(NOT_ALPHABET);
    }
    
    // check if column name already exists
    private static boolean isColumnExists(String columnName, List<String> dataHeader) {
        for (String existingColumn : dataHeader) {
            // if column exists
            if (existingColumn.equalsIgnoreCase(columnName)) {
                return true;
            }
        }
        return false;
    }
    
    //----------------------------------------------------------------------------//

    // validateEquation by returning the equation code
    public static String validateEquation(String equation, List<String> dataHeader, List<String> dataSample){
        int[] result = new int[6];
        String resultCode = "";
        
        // invalid equation
        if(isEquationExists(equation)){
            return resultCode;
        }
        String[] splitEquation = equation.split(DataTransformer.SPACES_SEPARATOR);
        if(splitEquation.length != DataTransformer.ASSIGN_VALUE) {
            if (splitEquation.length != DataTransformer.CALCULATE_VALUE){
                return resultCode;
            }  
        }
        
        String equationA = splitEquation[0];
        String equationEQ = splitEquation[1];
        String equationB = splitEquation[2];
        
        // validate equation individually
        result[0] = VALID_EQUATION;
        // validate equation A
        if(isColumnExists(equationA, dataHeader)){
            result[1] = VALID_EQUATION;
        } else {
            return resultCode;
        }
        // validate equation EQ (=)
        if (equationEQ.equals(EQUAL_SIGN)) {
            result[2] = VALID_EQUATION;
        } else {
            return resultCode; 
        }
        // validate equation B
        result[3] = validateParameter(equationB, dataHeader, dataSample);

        if(splitEquation.length == DataTransformer.CALCULATE_VALUE){
            String equationOP = splitEquation[3];
            String equationD = splitEquation[4];

            // validate equation C (+, -, *, /)
            if (equationOP.matches(VALID_OPERATORS)) {
                result[4] = VALID_EQUATION;
            } else {
                return resultCode;
            }

            // validate equation D
            result[5] = validateParameter(equationD, dataHeader, dataSample);
        }
        
        for(int num : result){
            resultCode += num;
        }
        return resultCode;
    }
    
    // check if equation is null or empty string
    private static boolean isEquationExists(String equation) {
        return equation == null
                || equation.trim().isEmpty();
    }
    
    // check if the string is a number
    private static boolean isNumber(String string) {
        return string.matches(DECIMAL_NUMBER) || string.matches(DECIMAL_WITHOUT_ZERO);
    }
 
    // check if parameter is number or column
    private static int validateParameter(String equation, List<String> dataHeader, List<String> dataSample) {
        int result = 0;
        if (isNumber(equation)) {
            result = NUMBER;
        } else if (isColumnExists(equation, dataHeader)) {
            int index = DataTransformer.columnNameIndex(equation, dataHeader);
            if (!isNumber(dataSample.get(index))) {
                result = INVALID_EQUATION;
            } else {
                result = COLUMN_NAME;
            }
        }
        return result;
    }
}
