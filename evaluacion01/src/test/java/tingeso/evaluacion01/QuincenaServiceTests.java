package tingeso.evaluacion01;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tingeso.evaluacion01.entities.QuincenaEntity;
import tingeso.evaluacion01.repositories.QuincenaRepository;
import tingeso.evaluacion01.services.QuincenaService;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class QuincenaServiceTests {
    @Mock
    private QuincenaRepository quincena_repository_mock;
    @InjectMocks
    private QuincenaService quincena_service;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    //Test para verificar cuando se ingresa una quincena valida
    void testIngresarQuincenaExitosa() throws Exception {
        QuincenaEntity quincena = new QuincenaEntity();
        quincena.setYear(2023);
        quincena.setMes(3);
        quincena.setNumero(1);
        String id = quincena.toString();
        quincena.setId(id);

        when(quincena_repository_mock.save(any(QuincenaEntity.class))).thenReturn(quincena);

        QuincenaEntity resultado = quincena_service.ingresarQuincena(quincena.getYear(), quincena.getMes(), quincena.getNumero());
        assertEquals(quincena, resultado);
    }

    @Test
    //Test para verificar que se lance una excepcion al ingreso de quincena con un año no valido
    void testIngresarQuincenaYearInvalido() {
        Integer year = -1;
        Integer mes = 8;
        Integer numero = 1;

        Exception exception = assertThrows(Exception.class, () -> quincena_service.ingresarQuincena(year, mes, numero));
        assertEquals("El año ingresado no es valido", exception.getMessage());
    }

    @Test
    //Test para verificar que se lance una excepcion al ingreso de quincena con un mes no valido
    void testIngresarQuincenaMesInvalido() {
        Integer year = 2023;
        Integer mes = 13;
        Integer numero = 1;

        Exception exception = assertThrows(Exception.class, () -> quincena_service.ingresarQuincena(year, mes, numero));
        assertEquals("El mes de la quincena no es valido", exception.getMessage());
    }

    @Test
    //Test para verificar que se lance una excepcion al ingreso de quincena con un numero no valido
    void testIngresarQuincenaNumeroInvalido() {
        Integer year = 2023;
        Integer mes = 12;
        Integer numero = 5;

        Exception exception = assertThrows(Exception.class, () -> quincena_service.ingresarQuincena(year, mes, numero));
        assertEquals("El numero de quincena no es valido", exception.getMessage());
    }

    @Test
    //Test para verificar que se lance una excepcion cuando la fecha es superior a la fecha actual
    void testIngresarQuincenaFechaSuperior() {
        LocalDateTime fecha_actual = LocalDateTime.now();
        Integer year = fecha_actual.getYear() + 1;
        Integer mes = 11;
        Integer numero = 2;

        Exception exception = assertThrows(Exception.class, () -> quincena_service.ingresarQuincena(year, mes, numero));
        assertEquals("La quincena ingresada es superior a la fecha actual", exception.getMessage());
    }

    @Test
    //Test para verificar cuando la quincena ya está registrada
    void testEstaRegistradaQuincenaTrue() {
        QuincenaEntity quincena = new QuincenaEntity();
        quincena.setYear(2023);
        quincena.setMes(3);
        quincena.setNumero(1);
        String id = quincena.toString();
        quincena.setId(id);

        when(quincena_repository_mock.findById(any(String.class))).thenReturn(Optional.of(quincena));

        boolean resultado = quincena_service.estaRegistradaQuincena(quincena.getYear(), quincena.getMes(), quincena.getNumero());
        assertTrue(resultado);
    }

    @Test
    //Test para verificar cuando la quincena no está registrada
    void testEstaRegistradaQuincenaFalse() {
        QuincenaEntity quincena = new QuincenaEntity();
        quincena.setYear(2023);
        quincena.setMes(3);
        quincena.setNumero(2);
        String id = quincena.toString();
        quincena.setId(id);

        when(quincena_repository_mock.findById(any(String.class))).thenReturn(Optional.empty());

        boolean resultado = quincena_service.estaRegistradaQuincena(2023, 1, 1);
        assertFalse(resultado);
    }
}
