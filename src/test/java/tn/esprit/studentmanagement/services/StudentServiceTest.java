package tn.esprit.studentmanagement.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.studentmanagement.entities.Student;
import tn.esprit.studentmanagement.repositories.StudentRepository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    private Student student;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        student = new Student("john.doe@example.com", "John", "Doe");
        student.setIdStudent(1L);
        student.setPhone("1234567890");
        student.setDateOfBirth(LocalDate.of(2000, 1, 1));
        student.setAddress("123 Main St");
    }

    @Test
    void getAllStudents_ShouldReturnAllStudents() {
        // Arrange
        Student student2 = new Student("jane.doe@example.com", "Jane", "Doe");
        List<Student> students = Arrays.asList(student, student2);
        when(studentRepository.findAll()).thenReturn(students);

        // Act
        List<Student> result = studentService.getAllStudents();

        // Assert
        assertEquals(2, result.size());
        verify(studentRepository, times(1)).findAll();
    }

    @Test
    void getStudentById_WhenStudentExists_ShouldReturnStudent() {
        // Arrange
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        // Act
        Student result = studentService.getStudentById(1L);

        // Assert
        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        verify(studentRepository, times(1)).findById(1L);
    }

    @Test
    void getStudentById_WhenStudentNotExists_ShouldThrowException() {
        // Arrange
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> studentService.getStudentById(1L));
        verify(studentRepository, times(1)).findById(1L);
    }

    @Test
    void saveStudent_ShouldReturnSavedStudent() {
        // Arrange
        Student newStudent = new Student("jane.doe@example.com", "Jane", "Doe");
        when(studentRepository.save(any(Student.class))).thenReturn(newStudent);

        // Act
        Student result = studentService.saveStudent(newStudent);

        // Assert
        assertNotNull(result);
        assertEquals("Jane", result.getFirstName());
        verify(studentRepository, times(1)).save(any(Student.class));
    }

    @Test
    void deleteStudent_WhenStudentExists_ShouldDeleteStudent() {
        // Arrange
        when(studentRepository.existsById(1L)).thenReturn(true);
        doNothing().when(studentRepository).deleteById(1L);

        // Act
        studentService.deleteStudent(1L);

        // Assert
        verify(studentRepository, times(1)).existsById(1L);
        verify(studentRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteStudent_WhenStudentNotExists_ShouldThrowException() {
        // Arrange
        when(studentRepository.existsById(1L)).thenReturn(false);

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> studentService.deleteStudent(1L));
        verify(studentRepository, times(1)).existsById(1L);
        verify(studentRepository, never()).deleteById(anyLong());
    }
}
