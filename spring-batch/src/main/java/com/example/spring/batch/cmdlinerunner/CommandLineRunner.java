package com.example.spring.batch.cmdlinerunner;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

//this annotation indicates that this class is spring managed bean
//it will be automatically detected and instantiated by spring component scanning
@Component
//here,class JobRunner implements CommandLineRunner interface which means it will override 'run' method
    //this method will be executed after the spring boot application is started
class JobRunner implements CommandLineRunner {

    @Autowired
    //this is a spring batch interface used to launch a job with specific parameters
    private JobLauncher jobLauncher;

    @Autowired
    //it represents a batch job to be executed
    private Job importCompanyJob;

    //denotes we have overriden the 'run' method of CommandLineRunner interface
    @Override
    //this method is executed when spring boot application starts it takes array of string as argument which can be
    //passed through command line
    public void run(String... args) throws Exception {
  //here, the JobParametersBuilder() is the builder to build different types of JobParameters,here, we are creating
  // jobParameter of type Long. JobParametersBuilder() have several methods like 'addLong' that we used here
        JobParameters jobParameters = new JobParametersBuilder()
                //here, we created jobParameter named "time" with current system time in milliseconds
                //this ensures that each job instance has a unique identifier and it avoids the reuse of any job inst.
                .addLong("time", System.currentTimeMillis())
                //converts a builder to 'JobParameters' object
                .toJobParameters();
        //jobLauncher is an interface in spring batch.it is used for execution of job.job is executed using
        //jobLauncher.run method,here we are running job named 'importCompanyJob' which we have created in
        //BatchConfig class inside a method which has return return type as 'job' and also we have passed jobParameters which
        //we have created above
        jobLauncher.run(importCompanyJob, jobParameters);
    }
}
// when we implement CommandLineRunner interface it allows the job execution to start as soon as the spring boot
// application is up and running


// instead of using 'CommandLineRunner' for execution of job you can also use 'Scheduler'
//'scheduler' will execute the job at any given time or at fixed intervals of time
//for that you have to use "@EnableScheduler" annotation at your main class
//and inside your 'JobRunner' class above 'runJob' method you have to use "@Scheduled" annotation
//there are three types of arguments inside '@Scheduler' annotation "fixedRate" it is time interval
// between start time of each job. "fixedDelay" it is time between end of one job to start of another job
//"cron" expression. using this we can schedule our job to be executed at specific time at spcific day
//it is used something like this @Scheduled(cron= * * * * * *)
//here, each star means seconds, minutes, hours, day of the month, month, day of the week
// some examples of cron expression are as following
//0 * * * * * => every hour at minute 0
//0 0 0 * * * = > every day at midnight
//0 30 15 * * * => every day at 3.30 pm
//0 0 8 * * MON => every manoday at 8 1m