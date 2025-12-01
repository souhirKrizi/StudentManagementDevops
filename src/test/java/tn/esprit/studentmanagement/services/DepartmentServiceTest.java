package tn.esprit.studentmanagement.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.studentmanagement.entities.Department;
import tn.esprit.studentmanagement.repositories.DepartmentRepository;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DepartmentServiceTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private DepartmentService departmentService;

    private Department department;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        department = new Department();
        department.setIdDepartment(1L);
        department.setName("Computer Science");
        department.setLocation("Building A, Room 101");
        department.setPhone("+216 70 000 000");
        department.setHead("Dr. Smith");
    }

    @Test
    void getAllDepartments_ShouldReturnAllDepartments() {
        // Arrange
        Department department2 = new Department();
        department2.setIdDepartment(2L);
        department2.setName("Mathematics");
        
        List<Department> departments = Arrays.asList(department, department2);
        when(departmentRepository.findAll()).thenReturn(departments);

        // Act
        List<Department> result = departmentService.getAllDepartments();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Computer Science", result.get(0).getName());
        verify(departmentRepository, times(1)).findAll();
    }

    @Test
    void getDepartmentById_WhenDepartmentExists_ShouldReturnDepartment() {
        // Arrange
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));

        // Act
        Department result = departmentService.getDepartmentById(1L);

        // Assert
        assertNotNull(result);
        assertEquals("Computer Science", result.getName());
        verify(departmentRepository, times(1)).findById(1L);
    }

    @Test
    void getDepartmentById_WhenDepartmentNotExists_ShouldThrowException() {
        // Arrange
        when(departmentRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> departmentService.getDepartmentById(1L));
        verify(departmentRepository, times(1)).findById(1L);
    }

    @Test
    void saveDepartment_ShouldReturnSavedDepartment() {
        // Arrange
        Department newDepartment = new Department();
        newDepartment.setName("Physics");
        
        when(departmentRepository.save(any(Department.class))).thenReturn(newDepartment);

        // Act
        Department result = departmentService.saveDepartment(newDepartment);

        // Assert
        assertNotNull(result);
        assertEquals("Physics", result.getName());
        verify(departmentRepository, times(1)).save(any(Department.class));
    }

    @Test
    void deleteDepartment_WhenDepartmentExists_ShouldDeleteDepartment() {
        // Arrange
        when(departmentRepository.existsById(1L)).thenReturn(true);
        doNothing().when(departmentRepository).deleteById(1L);

        // Act
        departmentService.deleteDepartment(1L);

        // Assert
        verify(departmentRepository, times(1)).existsById(1L);
        verify(departmentRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteDepartment_WhenDepartmentNotExists_ShouldThrowException() {
        // Arrange
        when(departmentRepository.existsById(1L)).thenReturn(false);

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> departmentService.deleteDepartment(1L));
        verify(departmentRepository, times(1)).existsById(1L);
        verify(departmentRepository, never()).deleteById(anyLong());
    }
}
