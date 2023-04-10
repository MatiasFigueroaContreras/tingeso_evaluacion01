package tingeso.evaluacion01;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tingeso.evaluacion01.entities.AcopioLecheEntity;
import tingeso.evaluacion01.entities.ProveedorEntity;
import tingeso.evaluacion01.entities.QuincenaEntity;
import tingeso.evaluacion01.repositories.AcopioLecheRepository;
import tingeso.evaluacion01.services.AcopioLecheService;
import tingeso.evaluacion01.services.ProveedorService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class AcopioLecheServiceTests {
    @Mock
    private AcopioLecheRepository acopio_leche_repository_mock;
    @Mock
    private ProveedorService proveedor_service_mock;
    @InjectMocks
    private AcopioLecheService acopio_leche_service;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    //Test para guardar lista de acopios leche, el cual a su vez utiliza guardar acopio leche
    public void testGuardarAcopiosLeche(){
        ProveedorEntity proveedor = new ProveedorEntity("12345", "Proveedor", "A", "Si");
        QuincenaEntity quincena = new QuincenaEntity("2023/03/1", 2023, 03, 1);

        AcopioLecheEntity acopio_leche_1 = new AcopioLecheEntity();
        acopio_leche_1.setTurno("M");
        acopio_leche_1.setCantidad_leche(250);
        acopio_leche_1.setFecha(new Date());
        acopio_leche_1.setProveedor(proveedor);
        acopio_leche_1.setQuincena(quincena);

        AcopioLecheEntity acopio_leche_2 = new AcopioLecheEntity();
        acopio_leche_2.setTurno("T");
        acopio_leche_2.setCantidad_leche(120);
        acopio_leche_2.setFecha(new Date());
        acopio_leche_2.setProveedor(proveedor);
        acopio_leche_2.setQuincena(quincena);

        ArrayList<AcopioLecheEntity> acopios_leche = new ArrayList<>();
        acopios_leche.add(acopio_leche_1);
        acopios_leche.add(acopio_leche_2);

        acopio_leche_service.guardarAcopiosLeches(acopios_leche);
    }

    @Test
    //Test para verificar el funcionamiento de obtener acopios leche por quincena
    public void testObtenerAcopiosLechePorProveedorQuincena(){
        ProveedorEntity proveedor = new ProveedorEntity("12345", "Proveedor", "A", "Si");
        QuincenaEntity quincena = new QuincenaEntity("2023/03/1", 2023, 03, 1);

        when(acopio_leche_repository_mock.findAllByProveedorAndQuincena(proveedor, quincena)).thenReturn(new ArrayList<>());
        acopio_leche_service.obtenerAcopiosLechePorProveedorQuincena(proveedor, quincena);
    }

    @Test
    //Test para verificar el cuando existen acopios de leche por quincena
    public void testExistenAcopiosLechePorQuincenaTrue(){
        QuincenaEntity quincena = new QuincenaEntity("2023/03/1", 2023, 03, 1);
        when(acopio_leche_repository_mock.existsByQuincena(quincena)).thenReturn(true);
        assertTrue(acopio_leche_service.existenAcopiosLechePorQuincena(quincena));
    }

    @Test
    //Test para verificar el cuando NO existen acopios de leche por quincena
    public void testExistenAcopiosLechePorQuincenaFalse(){
        QuincenaEntity quincena = new QuincenaEntity("2023/03/1", 2023, 03, 1);
        when(acopio_leche_repository_mock.existsByQuincena(quincena)).thenReturn(false);
        assertFalse(acopio_leche_service.existenAcopiosLechePorQuincena(quincena));
    }

    @Test
    //Test validar lista acopio leche quincena con el caso de acopios validos
    public void testValidarListaAcopioLecheQuincena() throws Exception {
        ProveedorEntity proveedor = new ProveedorEntity("12345", "Proveedor", "A", "Si");
        QuincenaEntity quincena = new QuincenaEntity("2023/03/1", 2023, 03, 1);
        SimpleDateFormat date_format = new SimpleDateFormat("yyyy/MM/dd");

        AcopioLecheEntity acopio_leche_1 = new AcopioLecheEntity();
        acopio_leche_1.setTurno("M");
        acopio_leche_1.setCantidad_leche(250);
        acopio_leche_1.setFecha(date_format.parse("2023/03/10"));
        acopio_leche_1.setProveedor(proveedor);

        AcopioLecheEntity acopio_leche_2 = new AcopioLecheEntity();
        acopio_leche_2.setTurno("T");
        acopio_leche_2.setCantidad_leche(120);
        acopio_leche_2.setFecha(date_format.parse("2023/03/10"));
        acopio_leche_2.setProveedor(proveedor);

        ArrayList<AcopioLecheEntity> acopios_leche = new ArrayList<>();
        acopios_leche.add(acopio_leche_1);
        acopios_leche.add(acopio_leche_2);

        when(proveedor_service_mock.existeProveedor(proveedor)).thenReturn(true);

        acopio_leche_service.validarListaAcopioLecheQuincena(acopios_leche, quincena);
    }

    @Test
    //Test para verificar que se lance una excepcion al tener un turno no valido
    public void testValidarAcopioLecheTurnoInvalido(){
        ProveedorEntity proveedor = new ProveedorEntity("12345", "Proveedor", "A", "Si");
        QuincenaEntity quincena = new QuincenaEntity("2023/03/1", 2023, 03, 1);

        AcopioLecheEntity acopio_leche = new AcopioLecheEntity();
        acopio_leche.setTurno("F");
        acopio_leche.setCantidad_leche(250);
        acopio_leche.setFecha(new Date());
        acopio_leche.setProveedor(proveedor);
        acopio_leche.setQuincena(quincena);

        when(proveedor_service_mock.existeProveedor(proveedor)).thenReturn(true);
        Exception exception = assertThrows(Exception.class, () -> {
            acopio_leche_service.validarAcopioLeche(acopio_leche);
        });

        assertEquals("Algun turno no es valido, debe ser M o T", exception.getMessage());
    }

    @Test
    //Test para verificar que se lance una excepcion al tener kilos de leche no valido
    public void testValidarAcopioLecheKlsLecheInvalido(){
        ProveedorEntity proveedor = new ProveedorEntity("12345", "Proveedor", "A", "Si");
        QuincenaEntity quincena = new QuincenaEntity("2023/03/1", 2023, 03, 1);

        AcopioLecheEntity acopio_leche = new AcopioLecheEntity();
        acopio_leche.setTurno("M");
        acopio_leche.setCantidad_leche(-1);
        acopio_leche.setFecha(new Date());
        acopio_leche.setProveedor(proveedor);
        acopio_leche.setQuincena(quincena);

        when(proveedor_service_mock.existeProveedor(proveedor)).thenReturn(true);
        Exception exception = assertThrows(Exception.class, () -> {
            acopio_leche_service.validarAcopioLeche(acopio_leche);
        });

        assertEquals("Los kilos de leche tienen que ser positivos", exception.getMessage());
    }

    @Test
    //Test para verificar que se lance una excepcion al tener una fecha no valida
    public void testValidarAcopioLecheFechaInvalida() throws Exception{
        ProveedorEntity proveedor = new ProveedorEntity("12345", "Proveedor", "A", "Si");
        QuincenaEntity quincena = new QuincenaEntity("2022/03/1", 2022, 03, 1);
        SimpleDateFormat date_format = new SimpleDateFormat("yyyy/MM/dd");

        AcopioLecheEntity acopio_leche = new AcopioLecheEntity();
        acopio_leche.setTurno("M");
        acopio_leche.setCantidad_leche(250);
        acopio_leche.setFecha(date_format.parse("2023/03/14"));
        acopio_leche.setProveedor(proveedor);
        acopio_leche.setQuincena(quincena);

        when(proveedor_service_mock.existeProveedor(proveedor)).thenReturn(true);
        Exception exception = assertThrows(Exception.class, () -> {
            acopio_leche_service.validarAcopioLeche(acopio_leche);
        });

        assertEquals("Las fechas ingresadas tienen que coincidir con la quincena", exception.getMessage());
    }

    @Test
    //Test para verificar que se lance una excepcion al tener un proveedor no registrado
    public void testValidarAcopioLecheProveedorNoRegistrado() throws Exception{
        ProveedorEntity proveedor = new ProveedorEntity("12345", "Proveedor", "A", "Si");
        QuincenaEntity quincena = new QuincenaEntity("2023/03/1", 2023, 03, 1);
        SimpleDateFormat date_format = new SimpleDateFormat("yyyy/MM/dd");

        AcopioLecheEntity acopio_leche = new AcopioLecheEntity();
        acopio_leche.setTurno("M");
        acopio_leche.setCantidad_leche(250);
        acopio_leche.setFecha(date_format.parse("2023/03/10"));
        acopio_leche.setProveedor(proveedor);
        acopio_leche.setQuincena(quincena);

        when(proveedor_service_mock.existeProveedor(proveedor)).thenReturn(false);
        Exception exception = assertThrows(Exception.class, () -> {
            acopio_leche_service.validarAcopioLeche(acopio_leche);
        });

        assertEquals("Los proveedores tienen que estar registrados", exception.getMessage());
    }
}
