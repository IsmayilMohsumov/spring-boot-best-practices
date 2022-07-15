package com.adios.springbootpractice.student;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public Student findStudentById(Long studentId) {
        return studentRepository.findById(studentId)
                .orElseThrow( () -> new IllegalStateException("Student not found"));
    }

    public void addNewStudent(Student student) {
        Optional<Student> studentByEmail = studentRepository.findStudentByEmail(student.getEmail());
        if (studentByEmail.isPresent()){
            throw new IllegalStateException(String.format("Email [%s] is taken",student.getEmail()));
        }
        studentRepository.save(student);
    }

    @Transactional
    public ResponseEntity<Student> updateStudent(Long studentId, Student student) {
        Student studentFoundById = studentRepository.findById(studentId)
                .orElseThrow( ()-> new IllegalStateException("Student not found with this id :: "+studentId));

        boolean isPresent = studentRepository.findStudentByEmail(student.getEmail()).isPresent();
        if (isPresent){
            throw new IllegalStateException("The provided email is already taken");
        }

        studentFoundById.setName(student.getName());
        studentFoundById.setEmail(student.getEmail());
        studentFoundById.setDob(student.getDob());
        return ResponseEntity.ok(studentFoundById);

    }

    public void deleteStudentById(Long studentId) {
        Optional<Student> studentOptional = studentRepository.findById(studentId);
        if (studentOptional.isEmpty()){
            throw new IllegalStateException("There is no student with this id");
        }
        studentRepository.deleteById(studentId);
    }
}
