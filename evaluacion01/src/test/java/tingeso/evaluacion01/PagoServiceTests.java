package tingeso.evaluacion01;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import tingeso.evaluacion01.entities.*;
import tingeso.evaluacion01.repositories.PagoRepository;
import tingeso.evaluacion01.services.PagoService;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest
public class PagoServiceTests {
    @Mock
    PagoRepository pago_repository_mock;
    @InjectMocks
    PagoService pago_service;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testObtenerPagos(){
        ProveedorEntity proveedor = new ProveedorEntity("12345", "Proveedor", "A", "Si");
        QuincenaEntity quincena = new QuincenaEntity("2023/03/1", 2023, 3, 1);
        PagoEntity pago_1 = new PagoEntity("12345-2023/03/1", 500000, 12000, 3500, 20000, 150, 0, 0, 535350, 0, 535350, proveedor, quincena, new DatosCentroAcopioEntity());
        PagoEntity pago_2 = new PagoEntity("12345-2023/02/2", 500000, 12000, 3500, 20000, 150, 0, 0, 535350, 0, 535350, proveedor, quincena.obtenerQuincenaAnterior(), new DatosCentroAcopioEntity());
        ArrayList<PagoEntity> pagos = new ArrayList<>();
        pagos.add(pago_1);
        pagos.add(pago_2);
        when(pago_repository_mock.findAllByOrderByQuincenaDescProveedorCodigoAsc()).thenReturn(pagos);

        ArrayList<PagoEntity> respuesta = pago_service.obtenerPagos();
        assertEquals(respuesta, pagos);
    }

    @Test
    public void testObtenerPagosPorQuincena(){
        ProveedorEntity proveedor_1 = new ProveedorEntity("12345", "Proveedor 1", "A", "Si");
        ProveedorEntity proveedor_2 = new ProveedorEntity("54321", "Proveedor 2", "C", "Si");
        QuincenaEntity quincena = new QuincenaEntity("2023/03/1", 2023, 3, 1);
        PagoEntity pago_1 = new PagoEntity("12345-2023/03/1", 500000, 12000, 3500, 20000, 150, 0, 0, 535350, 0, 535350, proveedor_1, quincena, new DatosCentroAcopioEntity());
        PagoEntity pago_2 = new PagoEntity("54321-2023/03/1", 500000, 12000, 3500, 20000, 150, 0, 0, 535350, 0, 535350, proveedor_2, quincena, new DatosCentroAcopioEntity());
        ArrayList<PagoEntity> pagos = new ArrayList<>();
        pagos.add(pago_1);
        pagos.add(pago_2);

        when(pago_repository_mock.findAllByQuincena(quincena)).thenReturn(pagos);

        ArrayList<PagoEntity> respuesta = pago_service.obtenerPagosPorQuincena(quincena);
        assertEquals(respuesta, pagos);
    }

    @Test
    public void testExistenagosPorQuincena(){
        QuincenaEntity quincena = new QuincenaEntity("2023/03/1", 2023, 3, 1);

        when(pago_repository_mock.existsByQuincena(quincena)).thenReturn(true);

        boolean respuesta = pago_service.existenPagosPorQuincena(quincena);

        assertTrue(respuesta);
    }

    @Test
    public void testGuardarPagos(){
        ProveedorEntity proveedor_1 = new ProveedorEntity("12345", "Proveedor 1", "A", "Si");
        ProveedorEntity proveedor_2 = new ProveedorEntity("54321", "Proveedor 2", "C", "Si");
        QuincenaEntity quincena = new QuincenaEntity("2023/03/1", 2023, 3, 1);
        PagoEntity pago_1 = new PagoEntity("", 500000, 12000, 3500, 20000, 150, 0, 0, 535350, 0, 535350, proveedor_1, quincena, new DatosCentroAcopioEntity());
        PagoEntity pago_2 = new PagoEntity("", 500000, 12000, 3500, 20000, 150, 0, 0, 535350, 0, 535350, proveedor_2, quincena, new DatosCentroAcopioEntity());
        ArrayList<PagoEntity> pagos = new ArrayList<>();
        pagos.add(pago_1);
        pagos.add(pago_2);

        pago_service.guardarPagos(pagos);
    }

    @Test
    public void testCalcularPagos(){
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

        ArrayList<DatosCentroAcopioEntity> lista_datos_ca = new ArrayList<>();
        lista_datos_ca.add(datos_ca);

        pago_service.calcularPagos(lista_datos_ca);
    }

    @Test
    public void testCalcularpago_lecheCategoriaA(){
        ProveedorEntity proveedor = new ProveedorEntity("12345", "Proveedor", "A", "Si");
        DatosCentroAcopioEntity datos_ca = new DatosCentroAcopioEntity();
        Integer kls_total = 1000;
        datos_ca.setTotal_kls_leche(kls_total);
        datos_ca.setProveedor(proveedor);

        Integer pago_leche = pago_service.calcularPagoLeche(datos_ca);

        assertEquals(700000, pago_leche);
    }

    @Test
    public void testCalcularpago_lecheCategoriaB(){
        ProveedorEntity proveedor = new ProveedorEntity("12345", "Proveedor", "B", "Si");
        DatosCentroAcopioEntity datos_ca = new DatosCentroAcopioEntity();
        Integer kls_total = 1000;
        datos_ca.setTotal_kls_leche(kls_total);
        datos_ca.setProveedor(proveedor);

        Integer pago_leche = pago_service.calcularPagoLeche(datos_ca);

        assertEquals(550000, pago_leche);
    }

    @Test
    public void testCalcularpago_lecheCategoriaC(){
        ProveedorEntity proveedor = new ProveedorEntity("12345", "Proveedor", "C", "Si");
        DatosCentroAcopioEntity datos_ca = new DatosCentroAcopioEntity();
        Integer kls_total = 1000;
        datos_ca.setTotal_kls_leche(kls_total);
        datos_ca.setProveedor(proveedor);

        Integer pago_leche = pago_service.calcularPagoLeche(datos_ca);

        assertEquals(400000, pago_leche);
    }

    @Test
    public void testCalcularpago_lecheCategoriaD(){
        ProveedorEntity proveedor = new ProveedorEntity("12345", "Proveedor", "D", "Si");
        DatosCentroAcopioEntity datos_ca = new DatosCentroAcopioEntity();
        Integer kls_total = 1000;
        datos_ca.setTotal_kls_leche(kls_total);
        datos_ca.setProveedor(proveedor);

        Integer pago_leche = pago_service.calcularPagoLeche(datos_ca);

        assertEquals(250000, pago_leche);
    }

    @Test
    public void testCalcularPagoGrasaRango0a20() {
        DatosCentroAcopioEntity datos_ca = new DatosCentroAcopioEntity();
        GrasaSolidoTotalEntity grasa_solido_total = new GrasaSolidoTotalEntity();
        grasa_solido_total.setPorcentaje_grasa(10);
        datos_ca.setGrasa_solido_total(grasa_solido_total);
        datos_ca.setTotal_kls_leche(1000);

        Integer pago_grasa = pago_service.calcularPagoGrasa(datos_ca);

        assertEquals(30000, pago_grasa);
    }

    @Test
    public void testCalcularPagoGrasaRango21a45() {
        DatosCentroAcopioEntity datos_ca = new DatosCentroAcopioEntity();
        GrasaSolidoTotalEntity grasa_solido_total = new GrasaSolidoTotalEntity();
        grasa_solido_total.setPorcentaje_grasa(30);
        datos_ca.setGrasa_solido_total(grasa_solido_total);
        datos_ca.setTotal_kls_leche(500);
        Integer pago_grasa = pago_service.calcularPagoGrasa(datos_ca);

        assertEquals(40000, pago_grasa);
    }

    @Test
    public void testCalcularPagoGrasaRangoMayor45() {
        DatosCentroAcopioEntity datos_ca = new DatosCentroAcopioEntity();
        GrasaSolidoTotalEntity grasa_solido_total = new GrasaSolidoTotalEntity();
        grasa_solido_total.setPorcentaje_grasa(50);
        datos_ca.setGrasa_solido_total(grasa_solido_total);
        datos_ca.setTotal_kls_leche(1500);
        Integer pago_grasa = pago_service.calcularPagoGrasa(datos_ca);

        assertEquals(180000, pago_grasa);
    }

    @Test
    public void testCalculoPagoSolidoTotalRango0a7() {
        GrasaSolidoTotalEntity grasaSolidoTotal = new GrasaSolidoTotalEntity();
        grasaSolidoTotal.setPorcentaje_solido_total(5);
        DatosCentroAcopioEntity datos_ca = new DatosCentroAcopioEntity();
        datos_ca.setGrasa_solido_total(grasaSolidoTotal);
        datos_ca.setTotal_kls_leche(100);
        Integer pago_solido_total = pago_service.calcularPagoSolidoTotal(datos_ca);
        assertEquals(-13000, pago_solido_total);
    }

    @Test
    public void testCalculoPagoSolidoTotalRango8a18() {
        GrasaSolidoTotalEntity grasaSolidoTotal = new GrasaSolidoTotalEntity();
        grasaSolidoTotal.setPorcentaje_solido_total(15);
        DatosCentroAcopioEntity datos_ca = new DatosCentroAcopioEntity();
        datos_ca.setGrasa_solido_total(grasaSolidoTotal);
        datos_ca.setTotal_kls_leche(100);
        Integer pago_solido_total = pago_service.calcularPagoSolidoTotal(datos_ca);
        assertEquals(-9000, pago_solido_total);
    }

    @Test
    public void testCalculoPagoSolidoTotalRango19a35() {
        GrasaSolidoTotalEntity grasaSolidoTotal = new GrasaSolidoTotalEntity();
        grasaSolidoTotal.setPorcentaje_solido_total(25);
        DatosCentroAcopioEntity datos_ca = new DatosCentroAcopioEntity();
        datos_ca.setGrasa_solido_total(grasaSolidoTotal);
        datos_ca.setTotal_kls_leche(100);
        Integer pago_solido_total = pago_service.calcularPagoSolidoTotal(datos_ca);
        assertEquals(9500, pago_solido_total);
    }

    @Test
    public void testCalculoPagoSolidoTotalMayor35() {
        GrasaSolidoTotalEntity grasaSolidoTotal = new GrasaSolidoTotalEntity();
        grasaSolidoTotal.setPorcentaje_solido_total(40);
        DatosCentroAcopioEntity datos_ca = new DatosCentroAcopioEntity();
        datos_ca.setGrasa_solido_total(grasaSolidoTotal);
        datos_ca.setTotal_kls_leche(100);
        Integer pago_solido_total = pago_service.calcularPagoSolidoTotal(datos_ca);
        assertEquals(15000, pago_solido_total);
    }

    @Test
    void testCalcularBonificacionMayorA10DiasEnvioMT() {
        DatosCentroAcopioEntity datos_ca = new DatosCentroAcopioEntity();
        datos_ca.setDias_envio_m_t(15);
        Integer pago_leche = 1000;
        Integer bonificacion = pago_service.calcularBonificacionPorFrecuencia(datos_ca, pago_leche);
        assertEquals(200, bonificacion);
    }

    @Test
    void testCalcularBonificacionMayorA10DiasEnvioM() {
        DatosCentroAcopioEntity datos_ca = new DatosCentroAcopioEntity();
        datos_ca.setDias_envio_m_t(5);
        datos_ca.setDias_envio_m(12);
        Integer pago_leche = 1000;
        Integer bonificacion = pago_service.calcularBonificacionPorFrecuencia(datos_ca, pago_leche);
        assertEquals(120, bonificacion);
    }

    @Test
    void testCalcularBonificacionMayorA10DiasEnvioT() {
        DatosCentroAcopioEntity datos_ca = new DatosCentroAcopioEntity();
        datos_ca.setDias_envio_m_t(5);
        datos_ca.setDias_envio_m(7);
        datos_ca.setDias_envio_t(11);
        Integer pago_leche = 1000;
        Integer bonificacion = pago_service.calcularBonificacionPorFrecuencia(datos_ca, pago_leche);
        assertEquals( 80, bonificacion);
    }

    @Test
    void testCalcularBonificacionSinBonificacion() {
        DatosCentroAcopioEntity datos_ca = new DatosCentroAcopioEntity();
        datos_ca.setDias_envio_m_t(5);
        datos_ca.setDias_envio_m(7);
        datos_ca.setDias_envio_t(9);
        Integer pago_leche = 1000;
        Integer bonificacion = pago_service.calcularBonificacionPorFrecuencia(datos_ca, pago_leche);
        assertEquals(0, bonificacion);
    }

    @Test
    void testCalcularDctoVariacionNegativaLecheRango9a25() {
        DatosCentroAcopioEntity datos_ca = new DatosCentroAcopioEntity();
        datos_ca.setVariacion_leche(-15);
        Integer pago_acopio_leche = 1000;
        Integer dcto_variacion_leche = pago_service.calcularDctoVariacionLeche(datos_ca, pago_acopio_leche);
        assertEquals(70, dcto_variacion_leche);
    }

    @Test
    void testCalcularDctoVariacionNegativaLecheRango26a45() {
        DatosCentroAcopioEntity datos_ca = new DatosCentroAcopioEntity();
        datos_ca.setVariacion_leche(-35);
        Integer pago_acopio_leche = 1000;
        Integer dcto_variacion_leche = pago_service.calcularDctoVariacionLeche(datos_ca, pago_acopio_leche);
        assertEquals(150, dcto_variacion_leche);
    }

    @Test
    void testCalcularDctoVariacionNegativaLecheMayorA45() {
        DatosCentroAcopioEntity datos_ca = new DatosCentroAcopioEntity();
        datos_ca.setVariacion_leche(-60);
        Integer pago_acopio_leche = 1000;
        Integer dcto_variacion_leche = pago_service.calcularDctoVariacionLeche(datos_ca, pago_acopio_leche);
        assertEquals(300, dcto_variacion_leche);
    }

    @Test
    void testCalcularDctoVariacionGrasaRango16a25() {
        DatosCentroAcopioEntity datos_ca = new DatosCentroAcopioEntity();
        datos_ca.setVariacion_grasa(-20);
        Integer pago_acopio_leche = 10000;
        Integer dcto_variacion_grasa = pago_service.calcularDctoVariacionGrasa(datos_ca, pago_acopio_leche);

        assertEquals(1200, dcto_variacion_grasa);
    }

    @Test
    void testCalcularDctoVariacionGrasaRango26a40() {
        DatosCentroAcopioEntity datos_ca = new DatosCentroAcopioEntity();
        datos_ca.setVariacion_grasa(-35);
        Integer pago_acopio_leche = 10000;
        Integer dcto_variacion_grasa = pago_service.calcularDctoVariacionGrasa(datos_ca, pago_acopio_leche);

        assertEquals(2000, dcto_variacion_grasa);
    }

    @Test
    void testCalcularDctoVariacionGrasaRangoMayorA41() {
        DatosCentroAcopioEntity datos_ca = new DatosCentroAcopioEntity();
        datos_ca.setVariacion_grasa(-50);
        Integer pago_acopio_leche = 10000;
        Integer dcto_variacion_grasa = pago_service.calcularDctoVariacionGrasa(datos_ca, pago_acopio_leche);

        assertEquals(3000, dcto_variacion_grasa);
    }

    @Test
    public void testCalcularDctoVariacionSolidoTotalRango7a12() {
        DatosCentroAcopioEntity datos_ca = new DatosCentroAcopioEntity();
        datos_ca.setVariacion_solido_total(-8);
        Integer pago_acopio_leche = 1000;
        Integer dcto_variacion_st = pago_service.calcularDctoVariacionSolidoTotal(datos_ca, pago_acopio_leche);
        assertEquals(180, dcto_variacion_st);
    }

    @Test
    public void testCalcularDctoVariacionSolidoTotalRango13a35() {
        DatosCentroAcopioEntity datos_ca = new DatosCentroAcopioEntity();
        datos_ca.setVariacion_solido_total(-18);
        Integer pago_acopio_leche = 1000;
        Integer dcto_variacion_st = pago_service.calcularDctoVariacionSolidoTotal(datos_ca, pago_acopio_leche);
        assertEquals(270, dcto_variacion_st);
    }

    @Test
    public void testCalcularDctoVariacionSolidoTotalMayorQue36() {
        DatosCentroAcopioEntity datos_ca = new DatosCentroAcopioEntity();
        datos_ca.setVariacion_solido_total(-40);
        Integer pago_acopio_leche = 1000;
        Integer dcto_variacion_st = pago_service.calcularDctoVariacionSolidoTotal(datos_ca, pago_acopio_leche);
        assertEquals(450, dcto_variacion_st);
    }

    @Test
    public void testCalcularMontoRetencionPagaRetencion(){
        ProveedorEntity proveedor = new ProveedorEntity("12345", "Proveedor", "A", "Si");
        PagoEntity pago = new PagoEntity();
        pago.setPago_total(1000000);
        pago.setProveedor(proveedor);
        Integer monto_retencion = pago_service.calcularMontoRetencion(pago);

        assertEquals(130000, monto_retencion);
    }

    @Test
    public void testCalcularMontoRetencionNoPagaRetencion(){
        ProveedorEntity proveedor = new ProveedorEntity("12345", "Proveedor", "A", "No");
        PagoEntity pago = new PagoEntity();
        pago.setPago_total(1000000);
        pago.setProveedor(proveedor);
        Integer monto_retencion = pago_service.calcularMontoRetencion(pago);

        assertEquals(0, monto_retencion);
    }
}
