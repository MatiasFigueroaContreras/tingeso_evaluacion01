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
import static org.mockito.Mockito.when;

@SpringBootTest
public class GrasaSolidoTotalServiceTests {
    @Mock
    private GrasaSolidoTotalRepository grasa_solido_total_repository_mock;
    @Mock
    private ProveedorService proveedor_service_mock;
    @InjectMocks
    private GrasaSolidoTotalService grasa_solido_total_service;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    //Test para guardar lista de grasa y solidos totales, el cual a su vez utiliza guardar grasa y solido total
    public void testGuardarListaGrasasSolidosTotales(){
        ProveedorEntity proveedor_1 = new ProveedorEntity("12345", "Proveedor 1", "A", "Si");
        QuincenaEntity quincena = new QuincenaEntity("2023/03/1", 2023, 03, 1);
        GrasaSolidoTotalEntity grasa_solido_total_1 = new GrasaSolidoTotalEntity();
        grasa_solido_total_1.setPorcentaje_grasa(40);
        grasa_solido_total_1.setPorcentaje_solido_total(35);
        grasa_solido_total_1.setProveedor(proveedor_1);
        grasa_solido_total_1.setQuincena(quincena);

        ProveedorEntity proveedor_2 = new ProveedorEntity("54321", "Proveedor 2", "A", "Si");
        GrasaSolidoTotalEntity grasa_solido_total_2 = new GrasaSolidoTotalEntity();
        grasa_solido_total_2.setPorcentaje_grasa(60);
        grasa_solido_total_2.setPorcentaje_solido_total(23);
        grasa_solido_total_2.setProveedor(proveedor_2);
        grasa_solido_total_2.setQuincena(quincena);

        ArrayList<GrasaSolidoTotalEntity>grasas_solidos_totales = new ArrayList<>();
        grasas_solidos_totales.add(grasa_solido_total_1);
        grasas_solidos_totales.add(grasa_solido_total_2);

        grasa_solido_total_service.guardarGrasasSolidosTotales(grasas_solidos_totales, quincena);
    }

    @Test
    //Test validar lista grasa y solidos totales para el caso exitoso
    public void testValidarListaGrasasSolidosTotalesExitoso() throws Exception{
        ProveedorEntity proveedor_1 = new ProveedorEntity("12345", "Proveedor 1", "A", "Si");
        GrasaSolidoTotalEntity grasa_solido_total_1 = new GrasaSolidoTotalEntity();
        grasa_solido_total_1.setPorcentaje_grasa(40);
        grasa_solido_total_1.setPorcentaje_solido_total(35);
        grasa_solido_total_1.setProveedor(proveedor_1);

        ProveedorEntity proveedor_2 = new ProveedorEntity("54321", "Proveedor 2", "A", "Si");
        GrasaSolidoTotalEntity grasa_solido_total_2 = new GrasaSolidoTotalEntity();
        grasa_solido_total_2.setPorcentaje_grasa(60);
        grasa_solido_total_2.setPorcentaje_solido_total(23);
        grasa_solido_total_2.setProveedor(proveedor_2);

        ArrayList<GrasaSolidoTotalEntity>grasas_solidos_totales = new ArrayList<>();
        grasas_solidos_totales.add(grasa_solido_total_1);
        grasas_solidos_totales.add(grasa_solido_total_2);

        when(proveedor_service_mock.existeProveedor(proveedor_1)).thenReturn(true);
        when(proveedor_service_mock.existeProveedor(proveedor_2)).thenReturn(true);

        grasa_solido_total_service.validarListaGrasasSolidosTotales(grasas_solidos_totales);
    }

    @Test
    //Test para verificar que se lance una excepcion al tener un porcentaje de grasa no valido
    public void testValidarGrasaSolidoTotalGrasaInvalida(){
        ProveedorEntity proveedor = new ProveedorEntity("12345", "Proveedor", "A", "Si");
        GrasaSolidoTotalEntity grasa_solido_total = new GrasaSolidoTotalEntity();
        grasa_solido_total.setPorcentaje_grasa(300);
        grasa_solido_total.setPorcentaje_solido_total(35);
        grasa_solido_total.setProveedor(proveedor);

        when(proveedor_service_mock.existeProveedor(proveedor)).thenReturn(true);
        Exception exception = assertThrows(Exception.class, () -> {
            grasa_solido_total_service.validarGrasaSolidoTotal(grasa_solido_total);
        });

        assertEquals("El porcentaje de grasa no es valido", exception.getMessage());
    }

    @Test
    //Test para verificar que se lance una excepcion al tener un porcentaje de solido total no valido
    public void testValidarGrasaSolidoTotalSolidoTotalInvalido(){
        ProveedorEntity proveedor = new ProveedorEntity("12345", "Proveedor", "A", "Si");
        GrasaSolidoTotalEntity grasa_solido_total = new GrasaSolidoTotalEntity();
        grasa_solido_total.setPorcentaje_grasa(60);
        grasa_solido_total.setPorcentaje_solido_total(260);
        grasa_solido_total.setProveedor(proveedor);

        when(proveedor_service_mock.existeProveedor(proveedor)).thenReturn(true);
        Exception exception = assertThrows(Exception.class, () -> {
            grasa_solido_total_service.validarGrasaSolidoTotal(grasa_solido_total);
        });

        assertEquals("El porcentaje de solido total no es valido", exception.getMessage());
    }

    @Test
    //Test para verificar que se lance una excepcion al tener un proveedor no registrado
    public void testValidarGrasaSolidoTotalSolidoTotalProveedorNoRegistrado(){
        ProveedorEntity proveedor = new ProveedorEntity("12345", "Proveedor", "A", "Si");
        GrasaSolidoTotalEntity grasa_solido_total = new GrasaSolidoTotalEntity();
        grasa_solido_total.setPorcentaje_grasa(60);
        grasa_solido_total.setPorcentaje_solido_total(20);
        grasa_solido_total.setProveedor(proveedor);

        when(proveedor_service_mock.existeProveedor(proveedor)).thenReturn(false);
        Exception exception = assertThrows(Exception.class, () -> {
            grasa_solido_total_service.validarGrasaSolidoTotal(grasa_solido_total);
        });

        assertEquals("Los proveedores tienen que estar registrados", exception.getMessage());
    }

    @Test
    //Test para verificar el funcionamiento de obtener grasa solido total por proveedor y quincena
    public void testObtenerGrasaSolidoTotalPorProveedorQuincena(){
        ProveedorEntity proveedor = new ProveedorEntity("12345", "Proveedor", "A", "Si");
        QuincenaEntity quincena = new QuincenaEntity("2023/03/1", 2023, 03, 1);
        GrasaSolidoTotalEntity grasa_solido_total = new GrasaSolidoTotalEntity("12345-2023/03/1", 25, 32, proveedor, quincena);

        when(grasa_solido_total_repository_mock.findByProveedorAndQuincena(proveedor, quincena)).thenReturn(Optional.of(grasa_solido_total));
        grasa_solido_total_service.obtenerGrasaSolidoTotalPorProveedorQuincena(proveedor, quincena);
    }

    @Test
    //Test para verificar el cuando existe grasa solido total por quincena
    public void testExisteGrasaSolidoTotalPorQuincenaTrue(){
        QuincenaEntity quincena = new QuincenaEntity("2023/03/1", 2023, 03, 1);
        when(grasa_solido_total_repository_mock.existsByQuincena(quincena)).thenReturn(true);
        assertTrue(grasa_solido_total_service.existeGrasaSolidoTotalPorQuincena(quincena));
    }

    @Test
    //Test para verificar el cuando NO existe grasa solido total por quincena
    public void testExisteGrasaSolidoTotalPorQuincenaFalse(){
        QuincenaEntity quincena = new QuincenaEntity("2023/03/1", 2023, 03, 1);
        when(grasa_solido_total_repository_mock.existsByQuincena(quincena)).thenReturn(false);
        assertFalse(grasa_solido_total_service.existeGrasaSolidoTotalPorQuincena(quincena));
    }
}
