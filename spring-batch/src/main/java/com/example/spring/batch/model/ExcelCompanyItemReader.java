package com.example.spring.batch.model;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

//here ItemReader interface have one method read();
// T read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException;
//the above is that method,here,'T' is the type of object that ItemReader will return
//this method reads next item from data source and returns it.when there is no items left in data source
//it should return 'null'
//ExcelCompanyItemReader is an custom implementation of ItemReader interface
//here we are reading data from an excel file
public class ExcelCompanyItemReader implements ItemReader<Company> {

    //this list will be used to store data which we will read from excel file
    private List<Company> companies;

    //this int variable will be used in read() method
    private int nextCompanyIndex;


    public ExcelCompanyItemReader(String filePath) throws Exception {
        this.companies=new ArrayList<>();
        this.nextCompanyIndex=0;
        readExcelFile(filePath);
    }

    //method to read data from excel sheet
public void readExcelFile(String filePath) throws Exception {

        // here we created workbook object and also created file input stream
try(Workbook workbook = new XSSFWorkbook(new FileInputStream(filePath))) {

    DataFormatter dataFormatter = new DataFormatter();
    Sheet sheet = workbook.getSheetAt(0);

    for (Row row :sheet)
    {
        //here, we skipped header row of the sheet
        if (row.getRowNum()==0)
        {
            continue;

        }

        Company company= new Company();
        //dataFormatter always return value in string format irrespictive of data type of value
        //so,here we stored id in string id and then converted it to long
        String id = dataFormatter.formatCellValue(row.getCell(0));
        company.setComapnyId(Long.valueOf(id));
        company.setCompanyName(dataFormatter.formatCellValue(row.getCell(1)));
        company.setDescription(dataFormatter.formatCellValue(row.getCell(2)));
        company.setWebsite(dataFormatter.formatCellValue(row.getCell(3)));
        company.setContact(dataFormatter.formatCellValue(row.getCell(4)));
        company.setDate(dataFormatter.formatCellValue(row.getCell(5)));
        String price =dataFormatter.formatCellValue(row.getCell(6));
        String percentage =dataFormatter.formatCellValue(row.getCell(7));


      //  String updatedPrice = price.replace('$',' ').replace(",","");
       // String updatedPercentage = percentage.replace("%","");
      //  double priceDouble = Double.valueOf(updatedPrice);
      //  double percentageDouble = Double.valueOf(updatedPercentage);

        company.setPrice(price);
        company.setPercentage(percentage);
        //double pd = percentageDouble/100;
      //  company.setDiscounted_price(priceDouble*pd);

        companies.add(company);
        

    }


}catch (Exception e)
{
    throw new Exception("failed to read file",e);

}



}

// we must implement read() method from ItemReader interface
    @Override
    public Company read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {

        Company nextCompany = null;

        if (nextCompanyIndex < companies.size())
        {

           nextCompany = companies.get(nextCompanyIndex);
           nextCompanyIndex++;

        }

        return nextCompany;
    }
}
