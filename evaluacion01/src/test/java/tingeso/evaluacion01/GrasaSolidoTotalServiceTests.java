package tingeso.evaluacion01;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import tingeso.evaluacion01.entities.GrasaSolidoTotalEntity;
import tingeso.evaluacion01.entities.ProveedorEntity;
import tingeso.evaluacion01.entities.QuincenaEntity;
import tingeso.evaluacion01.repositories.GrasaSolidoTotalRepository;
import tingeso.evaluacion01.services.GrasaSolidoTotalService;
import tingeso.evaluacion01.services.ProveedorService;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

@SpringBootTest
class GrasaSolidoTotalServiceTests {
    @Mock
    private GrasaSolidoTotalRepository grasaSolidoTotalRepositoryMock;
    @Mock
    private ProveedorService proveedorServiceMock;
    @InjectMocks
    private GrasaSolidoTotalService grasaSolidoTotalService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    //Test para guardar lista de grasa y solidos totales, el cual a su vez utiliza guardar grasa y solido total
    void testGuardarListaGrasasSolidosTotales(){
        ProveedorEntity proveedor1 = new ProveedorEntity("12345", "Proveedor 1", "A", "Si");
        QuincenaEntity quincena = new QuincenaEntity("2023/03/1", 2023, 03, 1);
        GrasaSolidoTotalEntity grasaSolidoTotal1 = new GrasaSolidoTotalEntity();
        grasaSolidoTotal1.setPorcentajeGrasa(40);
        grasaSolidoTotal1.setPorcentajeSolidoTotal(35);
        grasaSolidoTotal1.setProveedor(proveedor1);
        grasaSolidoTotal1.setQuincena(quincena);

        ProveedorEntity proveedor2 = new ProveedorEntity("54321", "Proveedor 2", "A", "Si");
        GrasaSolidoTotalEntity grasaSolidoTotal2 = new GrasaSolidoTotalEntity();
        grasaSolidoTotal2.setPorcentajeGrasa(60);
        grasaSolidoTotal2.setPorcentajeSolidoTotal(23);
        grasaSolidoTotal2.setProveedor(proveedor2);
        grasaSolidoTotal2.setQuincena(quincena);

        ArrayList<GrasaSolidoTotalEntity> grasasSolidosTotales = new ArrayList<>();
        grasasSolidosTotales.add(grasaSolidoTotal1);
        grasasSolidosTotales.add(grasaSolidoTotal2);

        grasaSolidoTotalService.guardarGrasasSolidosTotales(grasasSolidosTotales, quincena);
        verify(grasaSolidoTotalRepositoryMock, times(1)).save(grasaSolidoTotal1);
        verify(grasaSolidoTotalRepositoryMock, times(1)).save(grasaSolidoTotal2);
    }

    @Test
    //Test validar lista grasa y solidos totales para el caso exitoso
    void testValidarListaGrasasSolidosTotalesExitoso() {
        ProveedorEntity proveedor1 = new ProveedorEntity("12345", "Proveedor 1", "A", "Si");
        GrasaSolidoTotalEntity grasaSolidoTotal1 = new GrasaSolidoTotalEntity();
        grasaSolidoTotal1.setPorcentajeGrasa(40);
        grasaSolidoTotal1.setPorcentajeSolidoTotal(35);
        grasaSolidoTotal1.setProveedor(proveedor1);

        ProveedorEntity proveedor2 = new ProveedorEntity("54321", "Proveedor 2", "A", "Si");
        GrasaSolidoTotalEntity grasaSolidoTotal2 = new GrasaSolidoTotalEntity();
        grasaSolidoTotal2.setPorcentajeGrasa(60);
        grasaSolidoTotal2.setPorcentajeSolidoTotal(23);
        grasaSolidoTotal2.setProveedor(proveedor2);

        ArrayList<GrasaSolidoTotalEntity> grasasSolidosTotales = new ArrayList<>();
        grasasSolidosTotales.add(grasaSolidoTotal1);
        grasasSolidosTotales.add(grasaSolidoTotal2);

        when(proveedorServiceMock.existeProveedor(proveedor1)).thenReturn(true);
        when(proveedorServiceMock.existeProveedor(proveedor2)).thenReturn(true);

        assertAll(() -> grasaSolidoTotalService.validarListaGrasasSolidosTotales(grasasSolidosTotales));
    }

    @Test
    //Test para verificar que se lance una excepcion al tener un porcentaje de grasa no valido
    void testValidarGrasaSolidoTotalGrasaInvalida(){
        ProveedorEntity proveedor = new ProveedorEntity("12345", "Proveedor", "A", "Si");
        GrasaSolidoTotalEntity grasaSolidoTotal = new GrasaSolidoTotalEntity();
        grasaSolidoTotal.setPorcentajeGrasa(300);
        grasaSolidoTotal.setPorcentajeSolidoTotal(35);
        grasaSolidoTotal.setProveedor(proveedor);

        when(proveedorServiceMock.existeProveedor(proveedor)).thenReturn(true);
        Exception exception = assertThrows(Exception.class, () -> {
            grasaSolidoTotalService.validarGrasaSolidoTotal(grasaSolidoTotal);
        });

        assertEquals("El porcentaje de grasa no es valido", exception.getMessage());
    }

    @Test
    //Test para verificar que se lance una excepcion al tener un porcentaje de solido total no valido
    void testValidarGrasaSolidoTotalSolidoTotalInvalido(){
        ProveedorEntity proveedor = new ProveedorEntity("12345", "Proveedor", "A", "Si");
        GrasaSolidoTotalEntity grasaSolidoTotal = new GrasaSolidoTotalEntity();
        grasaSolidoTotal.setPorcentajeGrasa(60);
        grasaSolidoTotal.setPorcentajeSolidoTotal(260);
        grasaSolidoTotal.setProveedor(proveedor);

        when(proveedorServiceMock.existeProveedor(proveedor)).thenReturn(true);
        Exception exception = assertThrows(Exception.class, () -> {
            grasaSolidoTotalService.validarGrasaSolidoTotal(grasaSolidoTotal);
        });

        assertEquals("El porcentaje de solido total no es valido", exception.getMessage());
    }

    @Test
    //Test para verificar que se lance una excepcion al tener un proveedor no registrado
    void testValidarGrasaSolidoTotalSolidoTotalProveedorNoRegistrado(){
        ProveedorEntity proveedor = new ProveedorEntity("12345", "Proveedor", "A", "Si");
        GrasaSolidoTotalEntity grasaSolidoTotal = new GrasaSolidoTotalEntity();
        grasaSolidoTotal.setPorcentajeGrasa(60);
        grasaSolidoTotal.setPorcentajeSolidoTotal(20);
        grasaSolidoTotal.setProveedor(proveedor);

        when(proveedorServiceMock.existeProveedor(proveedor)).thenReturn(false);
        Exception exception = assertThrows(Exception.class, () -> {
            grasaSolidoTotalService.validarGrasaSolidoTotal(grasaSolidoTotal);
        });

        assertEquals("Los proveedores tienen que estar registrados", exception.getMessage());
    }

    @Test
    //Test para verificar el funcionamiento de obtener grasa solido total por proveedor y quincena
    void testObtenerGrasaSolidoTotalPorProveedorQuincena() {
        ProveedorEntity proveedor = new ProveedorEntity("12345", "Proveedor", "A", "Si");
        QuincenaEntity quincena = new QuincenaEntity("2023/03/1", 2023, 03, 1);
        GrasaSolidoTotalEntity grasaSolidoTotal = new GrasaSolidoTotalEntity("12345-2023/03/1", 25, 32, proveedor, quincena);

        when(grasaSolidoTotalRepositoryMock.findByProveedorAndQuincena(proveedor, quincena)).thenReturn(Optional.of(grasaSolidoTotal));
        GrasaSolidoTotalEntity respuesta = grasaSolidoTotalService.obtenerGrasaSolidoTotalPorProveedorQuincena(proveedor, quincena);
        assertEquals(grasaSolidoTotal, respuesta);
    }

    @Test
    //Test para verificar el lancamiento de la excepcion de obtener grasa solido total por proveedor y quincena
    void testObtenerGrasaSolidoTotalPorProveedorQuincenaNoExiste() {
        ProveedorEntity proveedor = new ProveedorEntity("12345", "Proveedor", "A", "Si");
        QuincenaEntity quincena = new QuincenaEntity("2023/03/1", 2023, 03, 1);

        when(grasaSolidoTotalRepositoryMock.findByProveedorAndQuincena(proveedor, quincena)).thenReturn(Optional.empty());
        Exception exception = assertThrows(Exception.class, () -> {
            grasaSolidoTotalService.obtenerGrasaSolidoTotalPorProveedorQuincena(proveedor, quincena);
        });

        assertEquals("No existe datos de grasa y solido total para un proveedor dada la quincena ingresada", exception.getMessage());
    }

    @Test
    //Test para verificar el cuando existe grasa solido total por quincena
    void testExisteGrasaSolidoTotalPorQuincenaTrue(){
        QuincenaEntity quincena = new QuincenaEntity("2023/03/1", 2023, 03, 1);
        when(grasaSolidoTotalRepositoryMock.existsByQuincena(quincena)).thenReturn(true);
        assertTrue(grasaSolidoTotalService.existeGrasaSolidoTotalPorQuincena(quincena));
    }

    @Test
    //Test para verificar el cuando NO existe grasa solido total por quincena
    void testExisteGrasaSolidoTotalPorQuincenaFalse(){
        QuincenaEntity quincena = new QuincenaEntity("2023/03/1", 2023, 03, 1);
        when(grasaSolidoTotalRepositoryMock.existsByQuincena(quincena)).thenReturn(false);
        assertFalse(grasaSolidoTotalService.existeGrasaSolidoTotalPorQuincena(quincena));
    }
}
