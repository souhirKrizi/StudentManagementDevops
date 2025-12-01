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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class StudentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private IStudentService studentService;

    @InjectMocks
    private StudentController studentController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(studentController).build();
    }

    @Test
    public void testGetAllStudents() throws Exception {
        // Given
        Student student1 = new Student("john.doe@example.com", "John", "Doe");
        student1.setIdStudent(1L);
        student1.setPhone("1234567890");

        Student student2 = new Student("jane.doe@example.com", "Jane", "Doe");
        student2.setIdStudent(2L);
        student2.setPhone("0987654321");

        List<Student> students = Arrays.asList(student1, student2);

        when(studentService.getAllStudents()).thenReturn(students);

        // When & Then
        mockMvc.perform(get("/students/getAllStudents"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idStudent").value(1))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[1].idStudent").value(2))
                .andExpect(jsonPath("$[1].firstName").value("Jane"));

        verify(studentService, times(1)).getAllStudents();
    }

    @Test
    public void testGetStudentById() throws Exception {
        // Given
        Student student = new Student("john.doe@example.com", "John", "Doe");
        student.setIdStudent(1L);
        student.setPhone("1234567890");

        when(studentService.getStudentById(1L)).thenReturn(student);

        // When & Then
        mockMvc.perform(get("/students/getStudent/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idStudent").value(1))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));

        verify(studentService, times(1)).getStudentById(1L);
    }

    @Test
    public void testCreateStudent() throws Exception {
        // Given
        Student student = new Student("john.doe@example.com", "John", "Doe");
        student.setIdStudent(1L);
        student.setPhone("1234567890");

        when(studentService.saveStudent(any(Student.class))).thenReturn(student);

        // When & Then
        mockMvc.perform(post("/students/createStudent")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idStudent").value(1))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));

        verify(studentService, times(1)).saveStudent(any(Student.class));
    }

    @Test
    public void testUpdateStudent() throws Exception {
        // Given
        Student student = new Student("john.doe@example.com", "John", "Doe");
        student.setIdStudent(1L);
        student.setPhone("1234567890");

        when(studentService.saveStudent(any(Student.class))).thenReturn(student);

        // When & Then
        mockMvc.perform(put("/students/updateStudent")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idStudent").value(1))
                .andExpect(jsonPath("$.firstName").value("John"));

        verify(studentService, times(1)).saveStudent(any(Student.class));
    }

    @Test
    public void testDeleteStudent() throws Exception {
        // Given
        doNothing().when(studentService).deleteStudent(1L);

        // When & Then
        mockMvc.perform(delete("/students/deleteStudent/1"))
                .andExpect(status().isOk());

        verify(studentService, times(1)).deleteStudent(1L);
    }
}
