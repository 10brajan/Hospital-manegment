package com.example.zajecia7doktorki.jakiesTesty;

import com.example.zajecia7doktorki.domain.Patient;
import com.example.zajecia7doktorki.service.PatientService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PatientServiceTest {

//    @Test
//    void changeNameIf() {
//        //given
//        Patient expectedPatient = new Patient(1L, "Andrzej", "Szymanski", 26, "12345678901", List.of());
//        Patient actualPatient = new Patient(1L, "Brajan", "Szymanski", 26, "12345678902", List.of());
//        //when
//        PatientService.changeNameIf(actualPatient);
//        //then
////        w junit podajemy najpierw expected value a potem actual
//        Assertions.assertEquals(expectedPatient.getName(), actualPatient.getName());
//        Assertions.assertEquals(actualPatient.getName(), "Andrzej");
////        w assertJ kolejnosc jest odwrotna do Junit tutaj najpierw podajemy actual a potme expected value
//        assertThat(actualPatient.getName())
//                .isEqualTo(expectedPatient.getName())
//                .isEqualTo("Andrzej");
////        sa jeszcze testy gdzie uzywa sue bibliotek hamcrest, ale one sa spierdolone
//    }
}