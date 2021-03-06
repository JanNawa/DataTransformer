# External Documentation

## Overview

This program converts a text file that have tab separator as delimiter 
into matrices of data that can do simple data analysis tasks. 
Then, that data could be written into the text file that have tab separator
as delimiter.

## Files and External Data

There are 3 main files :
* DataChangeUI.java
	* main for the program to interact with user
* DataTransformer.java
	* class that make data transformation
* ParameterValidator.java
	* class that validate some parameter in DataTransformer

## Data Structures and their relations to each other

The program uses 2D Dynamic Array to maintain order of data 
and data can be accessed by index to refer as column.
Everytime the new column is added, it will be added to the end of the Array.

The program uses Static Array to validate the equation.
Change the Static Array to String to check with the Equation Code
and do the calculation.

## Assumptions

* File name will contain the full path to the file, including file extension.
* Column names will be a single alphabetic string (no spaces).
* Column Header will only contain alphabetic characters.
* Data in column will have same data type.

## Key algorithms and design elements

* read\
The program read from file and processes the input file 1 line at a time. 
It adds the data to the Dynamic Array, process repeated until the end of the file.

The other operations can do in any order that user expected it to do.
* newColumn\
	Create new column in the data storage (2D array)
	First, added the header. Then, added the default value 0 to all rows.
	New column will always append to the end of the array.
* calculate\
	Calculate the equation user entered and add to the data row.
	Use equationCode to validate the parameter and operation of calculation in data row.
	Noted for equationCode :
		There are 6 digits number. Each digit has different representation.
		
		A B C D E F
		_ _ _ _ _ _
		
		A - represent valid equation (1)
		B - represent column name to validate equation (1)
		C - represent "=" to validate equation (1)
		D - represent whether it is number (2), column name (3) or invalid value (-1)
		E - represent valid operator (1); if no operator, it will be default value (0)
		F - represent whether it is number (2), column name (3) or invalid value (-1); 
		    if no value, it will be default value (0)

		number representative in the digits
		-------------------------------------------------
		|	-1 = invalid equation			|
		|	0 = default value (when no value)	|
		|	1 = valid equation			|
		|	2 = is number;				|
		|	3 = is column name;			|
		-------------------------------------------------
* clear\
	clear all data in data storage
* top\
	show top 5 rows of data in data storage
* print\
	show all rows of data in data storage
* write\
	write the data in data storage to the text file (seperated by tab)
	If the file already exists, it will append to new file.	Otherwise, the new file will be created.

## Limitations

The current design is limited to tab separated file.
The row can't be added, only column can be added.
