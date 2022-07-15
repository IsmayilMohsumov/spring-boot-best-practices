package com.adios.springbootpractice.student;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;
    public List<Student> getAllStudents(){
        return studentRepository.findAll();
    }

    public void addNewStudent(Student student) {
        Optional<Student> studentByEmail = studentRepository.findStudentByEmail(student.getEmail());
        if (studentByEmail.isPresent()){
            throw new IllegalStateException(String.format("Email [%s] is taken",student.getEmail()));
        }
        studentRepository.save(student);
    }

    @Transactional
    public void updateStudent(Long studentId, Student student) {
        Student studentFoundById = studentRepository.findById(studentId).get();
        studentFoundById.setName(student.getName());
        studentFoundById.setEmail(student.getEmail());
    }
}
