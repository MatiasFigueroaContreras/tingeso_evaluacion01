package tingeso.evaluacion01;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import tingeso.evaluacion01.entities.*;
import tingeso.evaluacion01.repositories.DatosCentroAcopioRepository;
import tingeso.evaluacion01.services.AcopioLecheService;
import tingeso.evaluacion01.services.DatosCentroAcopioService;
import tingeso.evaluacion01.services.GrasaSolidoTotalService;
import tingeso.evaluacion01.services.ProveedorService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
public class DatosCentroAcopioServiceTests {
    @Mock
    DatosCentroAcopioRepository datos_centro_acopio_repository_mock;
    @Mock
    AcopioLecheService acopio_leche_service_mock;
    @Mock
    GrasaSolidoTotalService grasa_solido_total_service_mock;
    @Mock
    ProveedorService proveedor_service_mock;
    @InjectMocks
    DatosCentroAcopioService datos_centro_acopio_service;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGuardarListaDatosCA(){
        ProveedorEntity proveedor_1 = new ProveedorEntity("12345", "Proveedor 1", "A", "Si");
        QuincenaEntity quincena = new QuincenaEntity("2023/03/1", 2023, 03, 1);
        GrasaSolidoTotalEntity grasa_solido_total_1 = new GrasaSolidoTotalEntity("12345-2023/03/1", 25, 32, proveedor_1, quincena);
        DatosCentroAcopioEntity datos_ca = new DatosCentroAcopioEntity();
        datos_ca.setDias_envio_m_t(10);
        datos_ca.setDias_envio_m(1);
        datos_ca.setDias_envio_t(1);
        datos_ca.setTotal_kls_leche(1254);
        datos_ca.setVariacion_leche(10);
        datos_ca.setVariacion_grasa(4);
        datos_ca.setVariacion_solido_total(-30);
        datos_ca.setGrasa_solido_total(grasa_solido_total_1);
        datos_ca.setProveedor(proveedor_1);
        datos_ca.setQuincena(quincena);

        ProveedorEntity proveedor_2 = new ProveedorEntity("54321", "Proveedor 2", "A", "Si");
        GrasaSolidoTotalEntity grasa_solido_total_2 = new GrasaSolidoTotalEntity("54321-2023/03/1", 25, 32, proveedor_2, quincena);
        DatosCentroAcopioEntity datos_ca_2 = new DatosCentroAcopioEntity();
        datos_ca_2.setDias_envio_m_t(10);
        datos_ca_2.setDias_envio_m(1);
        datos_ca_2.setDias_envio_t(1);
        datos_ca_2.setTotal_kls_leche(1254);
        datos_ca_2.setVariacion_leche(10);
        datos_ca_2.setVariacion_grasa(4);
        datos_ca_2.setVariacion_solido_total(-30);
        datos_ca_2.setGrasa_solido_total(grasa_solido_total_2);
        datos_ca_2.setProveedor(proveedor_2);
        datos_ca_2.setQuincena(quincena);

        ArrayList<DatosCentroAcopioEntity> lista_datos_ca = new ArrayList<>();
        lista_datos_ca.add(datos_ca);
        lista_datos_ca.add(datos_ca_2);

        datos_centro_acopio_service.guardarListaDatosCA(lista_datos_ca);
    }

    @Test
    //Test que prueba el caso exitoso de calcular datos del centro de acopio por quincena,
    // tomando las funciones completas de calcularDatosCAPorProveedorQuincena, calcularDatosAcopioLeche
    // y solo un caso de calcularVariacionesDatosCA y obtenerDatosCAPorProveedorQuincena
    public void testCalcularDatosCAPorQuincena() throws Exception {
        QuincenaEntity quincena = new QuincenaEntity("2023/03/1", 2023, 03, 1);
        ProveedorEntity proveedor = new ProveedorEntity("12345", "Proveedor", "A", "Si");
        SimpleDateFormat date_format = new SimpleDateFormat("yyyy/MM/dd");
        AcopioLecheEntity acopio_leche_1 = new AcopioLecheEntity(
                proveedor.getCodigo() +  "-2023/03/01-" + "M",
                "M",
                100,
                date_format.parse("2023/03/01"),
                proveedor,
                quincena);
        AcopioLecheEntity acopio_leche_2 = new AcopioLecheEntity(
                proveedor.getCodigo() +  "-2023/03/01-" + "T",
                "T",
                250,
                date_format.parse("2023/03/01"),
                proveedor,
                quincena);
        AcopioLecheEntity acopio_leche_3 = new AcopioLecheEntity(
                proveedor.getCodigo() +  "-2023/03/02-" + "M",
                "M",
                75,
                date_format.parse("2023/03/02"),
                proveedor,
                quincena);
        AcopioLecheEntity acopio_leche_4 = new AcopioLecheEntity(
                proveedor.getCodigo() +  "-2023/03/03-" + "T",
                "T",
                300,
                date_format.parse("2023/03/03"),
                proveedor,
                quincena);
        ArrayList<AcopioLecheEntity> acopios_leche = new ArrayList<>();
        acopios_leche.add(acopio_leche_1);
        acopios_leche.add(acopio_leche_2);
        acopios_leche.add(acopio_leche_3);
        acopios_leche.add(acopio_leche_4);
        GrasaSolidoTotalEntity grasa_solido_total = new GrasaSolidoTotalEntity("12345-2023/03/1", 25, 32, proveedor, quincena);
        DatosCentroAcopioEntity datos_ca_anterior = new DatosCentroAcopioEntity(
                proveedor.getCodigo() + "-2023/02/2",
                5,
                2,
                3,
                700,
                0,
                0,
                0,
                grasa_solido_total,
                proveedor,
                quincena.obtenerQuincenaAnterior()
        );

        when(proveedor_service_mock.obtenerProveedores()).thenReturn(new ArrayList<>(Arrays.asList(proveedor)));
        when(acopio_leche_service_mock.obtenerAcopiosLechePorProveedorQuincena(proveedor, quincena)).thenReturn(acopios_leche);
        when(grasa_solido_total_service_mock.obtenerGrasaSolidoTotalPorProveedorQuincena(proveedor, quincena)).thenReturn(grasa_solido_total);
        when(datos_centro_acopio_repository_mock.findByProveedorAndQuincena(proveedor, quincena.obtenerQuincenaAnterior())).thenReturn(Optional.of(datos_ca_anterior));

        datos_centro_acopio_service.calcularDatosCAPorQuincena(quincena);
    }

    @Test
    //Test para verificar que se lance la excepcion de que existen datos para la quincena
    // anterior los cuales no fueron calculados.
    public void testCalcularVariacionesDatosCADatosQuincenaAnteriorNoCalculados(){
        QuincenaEntity quincena = new QuincenaEntity("2023/03/1", 2023, 03, 1);
        ProveedorEntity proveedor = new ProveedorEntity("12345", "Proveedor", "A", "Si");
        GrasaSolidoTotalEntity grasa_solido_total = new GrasaSolidoTotalEntity("12345-2023/03/1", 25, 32, proveedor, quincena);
        DatosCentroAcopioEntity datos_ca = new DatosCentroAcopioEntity(
                proveedor.getCodigo() + "-2023/02/2",
                5,
                2,
                3,
                700,
                0,
                0,
                0,
                grasa_solido_total,
                proveedor,
                quincena
        );

        when(datos_centro_acopio_repository_mock.findByProveedorAndQuincena(proveedor, quincena.obtenerQuincenaAnterior())).thenReturn(Optional.empty());
        when(acopio_leche_service_mock.existenAcopiosLechePorQuincena(quincena.obtenerQuincenaAnterior())).thenReturn(true);

        Exception exception = assertThrows(Exception.class, () -> {
            datos_centro_acopio_service.calcularVariacionesDatosCA(datos_ca);
        });
        assertEquals("Existen pagos del centro de acopio no calculados para la quincena anterior", exception.getMessage());
    }

    @Test
    //Test para para verificar el caso en que no existen datos para la quincena anterior
    public void testCalcularVariacionesDatosCANoExistenDatosQuincenaAnterior() throws Exception{
        QuincenaEntity quincena = new QuincenaEntity("2023/03/1", 2023, 03, 1);
        ProveedorEntity proveedor = new ProveedorEntity("12345", "Proveedor", "A", "Si");
        GrasaSolidoTotalEntity grasa_solido_total = new GrasaSolidoTotalEntity("12345-2023/03/1", 25, 32, proveedor, quincena);
        DatosCentroAcopioEntity datos_ca = new DatosCentroAcopioEntity(
                proveedor.getCodigo() + "-2023/02/2",
                5,
                2,
                3,
                700,
                0,
                0,
                0,
                grasa_solido_total,
                proveedor,
                quincena
        );

        when(datos_centro_acopio_repository_mock.findByProveedorAndQuincena(proveedor, quincena.obtenerQuincenaAnterior())).thenReturn(Optional.empty());
        when(acopio_leche_service_mock.existenAcopiosLechePorQuincena(quincena.obtenerQuincenaAnterior())).thenReturn(false);
        datos_centro_acopio_service.calcularVariacionesDatosCA(datos_ca);
    }

    @Test
    //Test para la funcion de existencia de datos para el calculo de los datos
    // del centro de acopio para una quincena dada
    public void testExistenDatosCAParaCalculoPorQuincena(){
        QuincenaEntity quincena = new QuincenaEntity("2023/03/1", 2023, 03, 1);

        when(acopio_leche_service_mock.existenAcopiosLechePorQuincena(quincena)).thenReturn(true);
        when(grasa_solido_total_service_mock.existeGrasaSolidoTotalPorQuincena(quincena)).thenReturn(true);

        datos_centro_acopio_service.existenDatosCAParaCalculoPorQuincena(quincena);
    }
}
