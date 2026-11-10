package com.emilio.sorpyco_tfg;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.emilio.sorpyco_tfg.servicio.MatrizCostoService;

@SpringBootApplication
public class SorpycoTfgApplication {


	
    public static void main(String[] args) {
       
      SpringApplication.run(SorpycoTfgApplication.class, args);
    }
}