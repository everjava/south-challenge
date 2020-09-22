
# Developer test
You must create a data analysis system, where the system must import batches of files, read and analyze the data and produce a report. There are 3 types of data within these files.
#### For each type of data there is a different layout.
##### Seller details
 The salesperson data has the format id 001 and the line will have the following format: 001çCPFçNameçSalary

##### Client's data
  The customer data has the format id 002 and the line will have the following format: 002çCNPJçNameçBusiness Area

##### Sales data
  Sales data have the format id 003. Within the sales line, there is the list of items, which is surrounded by square brackets []. The line will have the following format: 003çSale IDç [Item ID-Item Quantity-Item Price] çSalesman name

##### Sample Data
The following is an example of the data that the system should be able to read.
> 001ç1234567891234çPedroç50000
> 001ç3245678865434çPauloç40000.99
> 002ç2345675434544345çJose da SilvaçRural
> 002ç2345675433444345çEduardo PereiraçRural
> 003ç10ç [1-10-100,2-30-2.50,3-40-3.10] çPedro
> 003ç08ç [1-34-10,2-33-1.50,3-40-0.10] çPaulo

##### Data analysis
- Your system should read data from the default directory, located at% HOMEPATH% / data / in.
- The system should read only .dat files.
- After processing all files within the standard input directory, the system should create a file within the standard output directory, located at% HOMEPATH% / data / out.
- The file name must follow the pattern, {flat_file_name} .done.dat.

##### The contents of the output file must summarize the following data:
- Number of customers in the input file
- Salesperson quantity in the input file
- Most expensive sale ID
- The worst seller
- The system must be working all the time.
- All new files are available, everything must be executed
- Your code must be written in Java.
- You have complete freedom to use google with what you need. Feel free to choose any external library if necessary.

##### Rating criteria
- Clean Code
- Simplicity
- Logic
- SOC (Separation of Concerns)
- Flexibility / Extensibility
- Scalability / Performance
