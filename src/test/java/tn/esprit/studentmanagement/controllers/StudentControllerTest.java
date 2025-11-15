package tn.esprit.studentmanagement.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tn.esprit.studentmanagement.entities.Student;
import tn.esprit.studentmanagement.services.IStudentService;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class StudentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private IStudentService studentService;

    @InjectMocks
    private StudentController studentController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(studentController).build();
    }

    @Test
    void getAllStudents_ShouldReturnListOfStudents() throws Exception {
        // Arrange
        Student student1 = new Student();
        student1.setIdStudent(1L);
        student1.setFirstName("John");
        student1.setLastName("Doe");
        student1.setEmail("john.doe@example.com");
        
        Student student2 = new Student();
        student2.setIdStudent(2L);
        student2.setFirstName("Jane");
        student2.setLastName("Smith");
        student2.setEmail("jane.smith@example.com");
        List<Student> students = Arrays.asList(student1, student2);

        when(studentService.getAllStudents()).thenReturn(students);

        // Act & Assert
        mockMvc.perform(get("/students/getAllStudents")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].firstName", is("John")))
                .andExpect(jsonPath("$[1].firstName", is("Jane")));

        verify(studentService, times(1)).getAllStudents();
    }

    @Test
    void getStudent_ShouldReturnStudent() throws Exception {
        // Arrange
        Student student = new Student();
        student.setIdStudent(1L);
        student.setFirstName("John");
        student.setLastName("Doe");
        student.setEmail("john.doe@example.com");
        when(studentService.getStudentById(1L)).thenReturn(student);

        // Act & Assert
        mockMvc.perform(get("/students/getStudent/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idStudent", is(1)))
                .andExpect(jsonPath("$.firstName", is("John")))
                .andExpect(jsonPath("$.lastName", is("Doe")));

        verify(studentService, times(1)).getStudentById(1L);
    }
    
    
    @Test
    void getAllStudents_WhenNoStudents_ShouldReturnEmptyList() throws Exception {
        // Arrange
        when(studentService.getAllStudents()).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/students/getAllStudents")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        verify(studentService, times(1)).getAllStudents();
    }

    @Test
    void createStudent_ShouldReturnCreatedStudent() throws Exception {
        // Arrange
        Student studentToCreate = new Student();
        studentToCreate.setFirstName("John");
        studentToCreate.setLastName("Doe");
        studentToCreate.setEmail("john.doe@example.com");
        
        Student createdStudent = new Student();
        createdStudent.setIdStudent(1L);
        createdStudent.setFirstName("John");
        createdStudent.setLastName("Doe");
        createdStudent.setEmail("john.doe@example.com");
        
        when(studentService.saveStudent(any(Student.class))).thenReturn(createdStudent);

        // Act & Assert
        mockMvc.perform(post("/students/createStudent")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(studentToCreate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idStudent", is(1)))
                .andExpect(jsonPath("$.firstName", is("John")));

        verify(studentService, times(1)).saveStudent(any(Student.class));
    }

    @Test
    void updateStudent_ShouldReturnUpdatedStudent() throws Exception {
        // Arrange
        Student studentToUpdate = new Student();
        studentToUpdate.setIdStudent(1L);
        studentToUpdate.setFirstName("John");
        studentToUpdate.setLastName("Doe");
        studentToUpdate.setEmail("john.doe@example.com");
        
        Student updatedStudent = new Student();
        updatedStudent.setIdStudent(1L);
        updatedStudent.setFirstName("John");
        updatedStudent.setLastName("Doe Updated");
        updatedStudent.setEmail("john.doe@example.com");
        
        when(studentService.saveStudent(any(Student.class))).thenReturn(updatedStudent);

        // Act & Assert
        mockMvc.perform(put("/students/updateStudent")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(studentToUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastName", is("Doe Updated")));

        verify(studentService, times(1)).saveStudent(any(Student.class));
    }

    @Test
    void deleteStudent_ShouldReturnNoContent() throws Exception {
        // Arrange
        Long studentId = 1L;
        doNothing().when(studentService).deleteStudent(studentId);

        // Act & Assert
        mockMvc.perform(delete("/students/deleteStudent/" + studentId))
                .andExpect(status().isOk());

        verify(studentService, times(1)).deleteStudent(studentId);
    }
}
