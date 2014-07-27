drug-data
=========
DESCRIPTION

This code is used to calculate all pairs of drugs that were administered together above a certain threshold. The input is in the form of a comma-delimited file in which each line represents a single drug administration with a patient identifier, a date of administration, and the name of the drug. For example:

A123,2014-01-01,5FU
A123,2014-01-02,fluorouracil
B456,1990-06-01,fluorouracil
A123,2014-01-02,oxaliplatin
B456,1990-06-01,oxaliplatin

If we were to ask for the number of drug pairs administered together over a threshold of 2, the calculator would yield a single pair - fluorouracil and oxaliplatin. For convenience, the results are output to file in comma-delimited form. There is no guarantee of ordering of pairs or of ordering of drugs within the pair. It is guaranteed that each pair will only be output once (for example, you will not see "drugA,drugB" and "drugB,drugA"). 

USAGE

To obtain the calculator, import from github https://github.com/monicalhamilton/drug-data into a Maven project in Eclipse. This should allow you to get the required dependencies.

To run the calculator, run the DrugPairCalculationMain with an input filename, an output filename if desired, and a threshold if desired (defaults to 25). Usage is as follows:

java DrugPairCalculationMain --in <input_filename> [--out <output_filename>] [--min <minimum_occurrences>]

TESTING

Unit tests can be found in src/test/java/api/AdministrationInstanceTest.java and src/test/java/calc/DrugPairCalculatorTest.java. Additional testing was also performed locally using test files, such as src/main/resources/Sample.txt. 

SCALABILITY

An interesting question is what happens when the file of drug administration data becomes too large to hold in memory. One solution involves sorting the input file by patient id and date. Then we read in all rows of the file that correspond to a given administration instance, find all of the pairs of drugs within this instance, and store the drug pairs in memory with a count of how many times each has occurred. This way we only have to hold in memory all of the drug pairs administered together, rather than every row in the file.