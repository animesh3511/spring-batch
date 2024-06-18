package com.example.spring.batch.writer;

import com.example.spring.batch.model.Company;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

//this class writes beans which are neccesary to write Company objects to mysql db
public class CompanyItemWriter {

    //this annotation tells us that it is a bean which is to be handled by spring container
    //the "companyItemWriter" method returns us JdbcBatchItemWriter object
    //this "JdbcBatchItemWriter" is used for writing a batch to relational db.It is one of the spring batch component
    //it handles bulk insertion of items into db
    //method takes datasource as arg this datasource provides details abt db connection which we have defined blow this
    //method
    @Bean
    public JdbcBatchItemWriter<Company> companyItemWriter(DataSource dataSource)
    {

   // JdbcBatchItemWriterBuilder is used to create and configure JdbcbatchItemWriter
        return new JdbcBatchItemWriterBuilder<Company>()
   //this provider maps the company objects properties to sql parameter
                .itemSqlParameterSourceProvider((new BeanPropertyItemSqlParameterSourceProvider<>()))
                .sql("INSERT INTO company (company_id, company_name, description, website, contact, date, price, percentage, discounted_price) VALUES (:comapnyId, :companyName, :description, :website, :contact, :date, :price, :percentage, :discounted_price)")
                .dataSource(dataSource)// sets the datasource to be used by JdbcBatchItemWriter
                                       //it is passed as parameter to the method
                .build();
    }

    //annotation indicates that this method produces a bean that is to be managed by spring container
    //the method returns datasource object configured for mysql
    @Bean
    public DataSource dataSource()
    {
        //here, we have set all configuration for connection with mysql db
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/batch");
        dataSource.setUsername("root");
        dataSource.setPassword("oms123");
        return dataSource;

    }


}
