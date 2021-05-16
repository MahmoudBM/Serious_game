package com.value.backend_sg.Configuration;

import com.value.backend_sg.Controllers.MedicItemProcess;
import com.value.backend_sg.Models.MedicDetails;
import com.value.backend_sg.Models.Medicament;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.data.builder.MongoItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {
    @Autowired
    public JobBuilderFactory jobBuilderFactory;
    @Autowired
    public StepBuilderFactory stepBuilderFactory;



    @Bean
    public FlatFileItemReader<MedicDetails> reader() {
        return new FlatFileItemReaderBuilder<MedicDetails>().name("medicItemReader")
                .resource(new ClassPathResource("test.csv")).delimited()
                .names(new String[] {"Nom", "Reference"})
                .linesToSkip(1)
                .fieldSetMapper(new BeanWrapperFieldSetMapper<MedicDetails>() {
                    {
                        setTargetType(MedicDetails.class);
                    }
                }).build();
    }

    @Bean
    public MongoItemWriter<Medicament> writer(MongoTemplate mongoTemplate) {
        return new MongoItemWriterBuilder<Medicament>().template(mongoTemplate).collection("medicament")
                .build();
    }


    @Bean
    public MedicItemProcess processor(){
        return new MedicItemProcess();
    }


    @Bean
    public Step step1(FlatFileItemReader<MedicDetails> itemReader, MongoItemWriter<Medicament> itemWriter)
            throws Exception {

        return this.stepBuilderFactory.get("step1")
                .<MedicDetails, Medicament>chunk(5)
                .reader(itemReader)
                .processor(processor())
                .writer(itemWriter).build();
    }

    @Bean
    public Job updateMedicJob(JobCompletionNotificationListener listener, Step step1)
            throws Exception {

        return this.jobBuilderFactory
                .get("updateMedicJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .start(step1)
                .build();
    }




}

