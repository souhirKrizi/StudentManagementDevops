package tn.esprit.studentmanagement.services;

import org.springframework.stereotype.Service;
import tn.esprit.studentmanagement.entities.Student;
import tn.esprit.studentmanagement.repositories.StudentRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class StudentService implements IStudentService {
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public List<Student> getAllStudents() { 
        return studentRepository.findAll(); 
    }

    @Override
    public Student getStudentById(Long id) { 
        return studentRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("Student not found with id: " + id)); 
    }

    @Override
    public Student saveStudent(Student student) { 
        return studentRepository.save(student); 
    }

    @Override
    public void deleteStudent(Long id) { 
        if (!studentRepository.existsById(id)) {
            throw new NoSuchElementException("Cannot delete. Student not found with id: " + id);
        }
        studentRepository.deleteById(id); 
    }
}
