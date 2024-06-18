package com.example.spring.batch.config;


import com.example.spring.batch.model.Company;
import com.example.spring.batch.model.ExcelCompanyItemReader;
import com.example.spring.batch.processor.CompanyItemProcessor;
import com.example.spring.batch.writer.CompanyItemWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

//this annotation indicates that this class contains one or more bean definations
//spring will process this class to generate spring beans
@Configuration
//this annotation enables spring batch features also provides base config to set up batch jobs in spring app
@EnableBatchProcessing
public class BatchConfig {

  @Value("${input.file.path}")
  private String filePath;

  //this annotation indicates that this method produces bean to be managed by spring container
  @Bean
  //defines method that returns ItemReader for 'Company' objects we have implemented 'ExcelCompanyItemReader' class
  //which implements 'ItemReader' interface this class reads company related data from excel sheet and set that
  //data to Company objects
  public ItemReader<Company> itemReader() throws Exception {
      return new ExcelCompanyItemReader(filePath);

  }

  //indicates that this method produces a bean to be managed by spring container
  @Bean
  //this method returns instance of CompanyItemProcessor which will process Company objects
  //the CompanyItemProcessor class implements ItemProcessor interface
  public ItemProcessor<Company,Company> itemProcessor()
  {

      return new CompanyItemProcessor();
  }

  //this annotation indicates that this method will create bean to be handled by spring container
  @Bean
  //this method returns itemwriter for Company entity this itemwriter will write data to db
  //for details of db it accepts the 'dataSource' arg
  public ItemWriter<Company> itemWriter(DataSource dataSource)
  {

   return new CompanyItemWriter().companyItemWriter(dataSource);

  }

  //this annotation indicates that this method will create bean which is to be managed by spring container
  @Bean
  //this method will return a 'job' it takes several parameters.this parameters will be injected by spring
 public Job importCompanyJob(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory,ItemReader<Company> reader,
                             ItemProcessor<Company,Company> processor,ItemWriter<Company> writer)
 {

      Step step = stepBuilderFactory.get("step 1")//creates new step named 'step 1' using 'stepBuilderFactory'
                  .<Company,Company>chunk(5)//specifies the step to process the data in chunk of 5
                                       //meaning 5 entries of data will be saved at a time in db
                  .reader(reader)// sets the ItemReader for step with provide 'reader' argument
              .processor(processor)// sets ItemProcessor for step with provided 'processor' argument
              .writer(writer)//sets ItemWriter for the step with provided 'writer' argument
              .build();//builds the step

      return jobBuilderFactory.get("importJobBuilder")//creating new job named "importJobBuilder" using
                                                //jobBuilderFactory.get() method
             .incrementer(new RunIdIncrementer())//here we set RunIdIncrementer for job which provides unique id
                                         //for each job run
              .flow(step)//defines the jobs flow to start with the step which we defined above
              .end()//marks the end of job flow
              .build();//builds and returns the job
  }

// spring batch properly run honyasathi tula 'schema-mysql.sql' he file tuzya mysql tool (wrokbench) vrti execute karavi lagte
// hya schema nusar tables tuzya designated db madhe tayar hotat. ty tables shivay batch run hot nahi
//ha 'schema-mysql.sql' schema external dependencies mdhe 'spring-batch-core' hya dependency chya under asto
// ti dependency tu right click karun 'open in explorer ' krr or
    //"C:\Users\OMS-DESKTOP\.m2\repository\org\springframework\batch\spring-batch-core\4.3.7\org\springframework\batch"
// above path vr jaun tila extract krr. extract kelyavr tith 'schem-mysql.sql' ha schema yel to ahe asa copy karun
    //workbench vrr tuzya designated db madhe execute krr je lagat astil te table create hotil
}
