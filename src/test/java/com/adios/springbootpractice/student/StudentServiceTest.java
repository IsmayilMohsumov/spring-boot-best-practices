package com.adios.springbootpractice.student;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    private StudentService underTest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        underTest = new StudentService(studentRepository);
    }

    @Test
    void itShouldGetAllStudents() {
        //Given
        Student studentIsmayil = new Student(
                "Ismayil",
                "fake",
                LocalDate.of(2001, Month.MARCH, 4));

        // Another student with same email
        Student studentNelson = new Student(
                "Nelson",
                "fake2",
                LocalDate.of(1995, Month.JANUARY, 6));

        //When
        doReturn(Arrays.asList(studentIsmayil,studentNelson)).when(studentRepository).findAll();

        //Then
        List<Student> allStudents = underTest.getAllStudents();

        assertThat(allStudents).isNotEmpty();
    }

    @Test
    void itShouldSaveStudent() {
        //Given
        String email = "mohsumovismayil@gmail.com";
        Student studentIsmayil = new Student(
                "Ismayil",
                email,
                LocalDate.of(2001, Month.MARCH, 4));

        // There shouldn't be email taken to make this test pass
        given(studentRepository.findStudentByEmail(email)).willReturn(Optional.empty());

        //When
        underTest.addNewStudent(studentIsmayil);

        //Then
        ArgumentCaptor<Student> studentArgumentCaptor = ArgumentCaptor.forClass(Student.class);

        then(studentRepository).should().save(studentArgumentCaptor.capture());

        Student studentArgumentCaptorValue = studentArgumentCaptor.getValue();

        assertThat(studentArgumentCaptorValue)
                .isEqualTo(studentIsmayil);

    }

    @Test
    void itShouldThrowWhenEmailIsTaken() {
        //Given
        String email = "mohsumovismayil@gmail.com";
        Student studentIsmayil = new Student(
                "Ismayil",
                email,
                LocalDate.of(2001, Month.MARCH, 4));

        // Another student with same email
        Student studentNelson = new Student(
                "Nelson",
                email,
                LocalDate.of(1995, Month.JANUARY, 6));

        //Return a customer with the same email
        given(studentRepository.findStudentByEmail(email)).willReturn(Optional.of(studentNelson));

        //When
        //Then
        assertThatThrownBy(() -> underTest.addNewStudent(studentIsmayil))
                .isInstanceOf(IllegalStateException.class)
                        .hasMessageContaining(String.format("Email [%s] is taken",studentIsmayil.getEmail()));

        //Finally
        then(studentRepository).should(never()).save(any(Student.class));
    }
}