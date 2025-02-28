package com.unir.back_end_ms_books_catalogue.tests;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features", // Carpeta donde están las pruebas de Cucumber
        glue = "com.unir.back_end_ms_books_catalogue.stepdefinitions",
        plugin = {"pretty", "html:target/cucumber-reports.html"}
)
public class RunCucumberTest {
}
