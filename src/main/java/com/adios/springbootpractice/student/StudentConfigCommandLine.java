package com.adios.springbootpractice.student;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.Month;

@Configuration
public class StudentConfigCommandLine {

    @Bean
    CommandLineRunner commandLineRunner(StudentRepository repository){
        return args -> {
            Student ismayil = new Student(
                    "Ismayil",
                    "mohsumovismayil@gmail.com",
                    LocalDate.of(2001, Month.MARCH, 4));
            repository.save(ismayil);
        };
    }
}
