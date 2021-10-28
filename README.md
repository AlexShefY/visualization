# Курс основ программирования на МКН СПбГУ
## Project 3: visualization of data

[How it works](description.md)

The program accept name of file with data as input.

To run the program firstly you should run it from the command line
in the directory pf-2021-viz-AlexShefY with command:

gradlew run --args="[name of file with data]".

Example:

gradlew run --args="exampleDistributionGraph.txt"

Decription of input file for each type of histogram:
1. clustered histogram

   To build this histogram you should input:

       1.1 number of categories
 
       1.2 number of fields
 
       1.3 fields

       1.4 for each category you should input name and values of the corresponding fields

       1.5 file to output 
2. graph

   To build this histogram you should input:

       2.1 fields

       2.2 number of graphs

       2.3 number of points for each graph

       2.4 x-value and y-value for each point on a separate line

       2.5 file to output

3. pie chart

   To build this histogram you should input:

       3.1 number of pais key-value

       3.2 pairs key-value (each pair on a separate line)

       3.3 file to output

4. distribution graph

    To build this histogram you should input:

       4.1 number of pairs key-value

       4.2 pairs key-value (key and value should be float)

       4.3 file to output

5. stacked histogram

   To build this histogram you should input:

       5.1 number of categories

       5.2 number of fields

       5.3 fields

       5.4 for each category you should input name and values of the corresponding fields (each category on a separate line)

       5.5 file to output

6. bar chart

   To build this histogram you should input:

       6.1 number of pais key-value

       6.2 pairs key-value (each pair on a separate line)

       6.3 file to output

>Examples of input:
>
>[example input file Bar Chart](exampleBarChart.txt)
>
>[example input file Clustered Histogram](exampleClusteredHistogram.txt)
>
>[example input file Distribution Graph](exampleDistributionGraph.txt)
> 
>[example input file Graph](exampleGraph.txt)
>
>[example input file Pie Chart](examplePieChart.txt)
>
>[example input file Stacked Histogram](exampleStackedHistogram.txt)
 
>Examples of output:
> 
>[example output for Bar Char](exampleBarChartOut.png)
> 
>[example output for Clustered Histogram](exampleClusteredHistogramOut.png)
> 
>[example output for Distribution Graph](exampleDistributionGraphOut.png)
> 
>[example output for Graph](exampleGraphOut.png)
> 
>[example output for Pie Chart](examplePieChartOut.png)
> 
>[example output for Stacked Histogram](exampleStackedHistogramOut.png)