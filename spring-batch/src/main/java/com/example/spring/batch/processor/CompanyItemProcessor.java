package com.example.spring.batch.processor;

import com.example.spring.batch.model.Company;
import org.springframework.batch.item.ItemProcessor;

// the two arguments are input and output arguments types,here, both the types are company
//it can be different at times first arg is type of input ,second arg is type of output
public class CompanyItemProcessor implements ItemProcessor<Company,Company> {

   // we must override this 'process' method of ItemProcessor interface
    //here,we process data that is read by ItemRader interface. we process that data acc to our
    //business requirment
    @Override
    public Company process(Company company) throws Exception {
        //here we removed '$' and ',' from our price in excel sheet and also '%' from percentage
        String updatedPrice = company.getPrice().replace('$', ' ').replace(",", "");
        String updatedPercentage = company.getPercentage().replace("%", "");
        double priceDouble = Double.valueOf(updatedPrice);
        double percentageDouble = Double.valueOf(updatedPercentage);

        double pd = percentageDouble/100;
        company.setDiscounted_price(priceDouble * pd);

        return company;

    }
}
