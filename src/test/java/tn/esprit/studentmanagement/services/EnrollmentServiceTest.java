package tn.esprit.studentmanagement.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.studentmanagement.entities.Enrollment;
import tn.esprit.studentmanagement.repositories.EnrollmentRepository;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EnrollmentServiceTest {

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @InjectMocks
    private EnrollmentService enrollmentService;

    private Enrollment enrollment1;
    private Enrollment enrollment2;

    @BeforeEach
    void setUp() {
        // Initialize test data
        enrollment1 = new Enrollment();
        enrollment1.setIdEnrollment(1L);
        enrollment1.setEnrollmentDate(java.time.LocalDate.now());
        
        enrollment2 = new Enrollment();
        enrollment2.setIdEnrollment(2L);
        enrollment2.setEnrollmentDate(java.time.LocalDate.now().minusDays(1));
    }

    @Test
    void getAllEnrollments_ShouldReturnAllEnrollments() {
        // Arrange
        List<Enrollment> enrollments = Arrays.asList(enrollment1, enrollment2);
        when(enrollmentRepository.findAll()).thenReturn(enrollments);

        // Act
        List<Enrollment> result = enrollmentService.getAllEnrollments();

        // Assert
        assertEquals(2, result.size());
        verify(enrollmentRepository, times(1)).findAll();
    }

    @Test
    void getEnrollmentById_WhenEnrollmentExists_ShouldReturnEnrollment() {
        // Arrange
        when(enrollmentRepository.findById(1L)).thenReturn(Optional.of(enrollment1));

        // Act
        Enrollment result = enrollmentService.getEnrollmentById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getIdEnrollment());
        verify(enrollmentRepository, times(1)).findById(1L);
    }

    @Test
    void getEnrollmentById_WhenEnrollmentNotExists_ShouldThrowException() {
        // Arrange
        when(enrollmentRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> enrollmentService.getEnrollmentById(99L));
        verify(enrollmentRepository, times(1)).findById(99L);
    }

    @Test
    void saveEnrollment_ShouldReturnSavedEnrollment() {
        // Arrange
        when(enrollmentRepository.save(any(Enrollment.class))).thenReturn(enrollment1);

        // Act
        Enrollment result = enrollmentService.saveEnrollment(enrollment1);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getIdEnrollment());
        verify(enrollmentRepository, times(1)).save(any(Enrollment.class));
    }

    @Test
    void deleteEnrollment_WhenEnrollmentExists_ShouldDeleteEnrollment() {
        // Arrange
        when(enrollmentRepository.existsById(1L)).thenReturn(true);
        doNothing().when(enrollmentRepository).deleteById(1L);

        // Act
        enrollmentService.deleteEnrollment(1L);

        // Assert
        verify(enrollmentRepository, times(1)).existsById(1L);
        verify(enrollmentRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteEnrollment_WhenEnrollmentNotExists_ShouldThrowException() {
        // Arrange
        when(enrollmentRepository.existsById(99L)).thenReturn(false);

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> enrollmentService.deleteEnrollment(99L));
        verify(enrollmentRepository, times(1)).existsById(99L);
        verify(enrollmentRepository, never()).deleteById(anyLong());
    }

    @Test
    void updateEnrollment_ShouldReturnUpdatedEnrollment() {
        // Utilisation de l'inscription initialisée dans setUp()
        Enrollment enrollmentToUpdate = enrollment1;
        
        // Création d'une nouvelle date de mise à jour
        java.time.LocalDate updatedDate = enrollment1.getEnrollmentDate().plusDays(5);
        
        // Configuration du mock pour simuler la sauvegarde
        when(enrollmentRepository.save(any(Enrollment.class))).thenAnswer(invocation -> {
            Enrollment savedEnrollment = invocation.getArgument(0);
            // Vérification des propriétés pendant la sauvegarde
            assertEquals(enrollment1.getIdEnrollment(), savedEnrollment.getIdEnrollment());
            assertEquals(updatedDate, savedEnrollment.getEnrollmentDate());
            return savedEnrollment;
        });

        // Mise à jour de la date d'inscription
        enrollmentToUpdate.setEnrollmentDate(updatedDate);
        
        // Appel de la méthode à tester
        Enrollment result = enrollmentService.saveEnrollment(enrollmentToUpdate);

        // Vérifications
        assertNotNull(result, "L'inscription mise à jour ne devrait pas être nulle");
        assertEquals(enrollment1.getIdEnrollment(), result.getIdEnrollment(), "L'ID ne devrait pas changer");
        assertEquals(updatedDate, result.getEnrollmentDate(), "La date d'inscription devrait être mise à jour");
        
        // Vérification que la méthode save a été appelée une fois avec le bon paramètre
        verify(enrollmentRepository, times(1)).save(enrollmentToUpdate);
    }
}
