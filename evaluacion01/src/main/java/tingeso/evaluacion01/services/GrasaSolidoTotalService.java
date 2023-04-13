package tingeso.evaluacion01.services;

import lombok.Generated;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tingeso.evaluacion01.entities.GrasaSolidoTotalEntity;
import tingeso.evaluacion01.entities.ProveedorEntity;
import tingeso.evaluacion01.entities.QuincenaEntity;
import tingeso.evaluacion01.repositories.GrasaSolidoTotalRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
public class GrasaSolidoTotalService {
    @Autowired
    GrasaSolidoTotalRepository grasaSolidoTotalRepository;
    @Autowired
    ProveedorService proveedorService;

    public void guardarGrasaSolidoTotal(GrasaSolidoTotalEntity grasaSolidoTotal) {
        String codigoProveedor = grasaSolidoTotal.getProveedor().getCodigo();
        String quincena = grasaSolidoTotal.getQuincena().toString();
        String id = codigoProveedor + "-" + quincena;
        grasaSolidoTotal.setId(id);
        grasaSolidoTotalRepository.save(grasaSolidoTotal);
    }

    public void guardarGrasasSolidosTotales(List<GrasaSolidoTotalEntity> grasasSolidosTotales, QuincenaEntity quincena) {
        for (GrasaSolidoTotalEntity grasaSolidoTotal : grasasSolidosTotales) {
            grasaSolidoTotal.setQuincena(quincena);
            guardarGrasaSolidoTotal(grasaSolidoTotal);
        }
    }

    public void validarListaGrasasSolidosTotales(List<GrasaSolidoTotalEntity> grasasSolidosTotales) {
        for (GrasaSolidoTotalEntity grasaSolidoTotal : grasasSolidosTotales) {
            validarGrasaSolidoTotal(grasaSolidoTotal);
        }
    }

    public void validarGrasaSolidoTotal(GrasaSolidoTotalEntity grasaSolidoTotal) {
        ProveedorEntity proveedor = grasaSolidoTotal.getProveedor();
        Integer porcentajeGrasa = grasaSolidoTotal.getPorcentajeGrasa();
        Integer porcentajeSolidoTotal = grasaSolidoTotal.getPorcentajeSolidoTotal();
        if (porcentajeGrasa < 0 || porcentajeGrasa > 100) {
            throw new IllegalArgumentException("El porcentaje de grasa no es valido");
        }

        if (porcentajeSolidoTotal < 0 || porcentajeSolidoTotal > 100) {
            throw new IllegalArgumentException("El porcentaje de solido total no es valido");
        }

        if (!proveedorService.existeProveedor(proveedor)) {
            throw new IllegalArgumentException("Los proveedores tienen que estar registrados");
        }
    }

    public GrasaSolidoTotalEntity obtenerGrasaSolidoTotalPorProveedorQuincena(ProveedorEntity proveedor, QuincenaEntity quincena) {
        Optional<GrasaSolidoTotalEntity> grasaSolidoTotal =  grasaSolidoTotalRepository.findByProveedorAndQuincena(proveedor, quincena);
        if(!grasaSolidoTotal.isPresent()){
            throw new IllegalArgumentException("No existe datos de grasa y solido total para un proveedor dada la quincena ingresada");
        }

        return grasaSolidoTotal.get();
    }

    public boolean existeGrasaSolidoTotalPorQuincena(QuincenaEntity quincena) {
        return grasaSolidoTotalRepository.existsByQuincena(quincena);
    }

    @Generated
    public List<GrasaSolidoTotalEntity> leerExcel(MultipartFile file) {
        List<GrasaSolidoTotalEntity> grasasSolidosTotales = new ArrayList<>();
        String filename = file.getOriginalFilename();

        if (filename == null || !filename.endsWith(".xlsx")) {
            throw new IllegalArgumentException("El archivo ingresado no es un .xlsx");
        }
        XSSFWorkbook workbook;
        try {
            workbook = new XSSFWorkbook(file.getInputStream());
        } catch (IOException e) {
            throw new IllegalArgumentException("El archivo ingresado no pudo ser leido");
        }
        XSSFSheet worksheet = workbook.getSheetAt(0);
        boolean rowVerification = true;
        for (Row row : worksheet) {
            if (rowVerification) {
                rowVerification = false;
                continue;
            }

            Iterator<Cell> cellItr = row.iterator();
            int iCell = 0;
            GrasaSolidoTotalEntity grasaSolidoTotal = new GrasaSolidoTotalEntity();
            ProveedorEntity proveedor = new ProveedorEntity();
            while (cellItr.hasNext()) {
                Cell cell = cellItr.next();
                setValueByCell(grasaSolidoTotal, proveedor, cell, iCell);
                iCell++;
            }
            if (iCell == 3) {
                grasaSolidoTotal.setProveedor(proveedor);
                grasasSolidosTotales.add(grasaSolidoTotal);
            }
        }

        return grasasSolidosTotales;
    }

    @Generated
    private void setValueByCell(GrasaSolidoTotalEntity grasaSolidoTotal, ProveedorEntity proveedor, Cell cell, int iCell) {
        try {
            switch (iCell) {
                case 0 -> proveedor.setCodigo(getCodigoValuByCell(cell));
                case 1 -> grasaSolidoTotal.setPorcentajeGrasa((int) cell.getNumericCellValue());
                case 2 -> grasaSolidoTotal.setPorcentajeSolidoTotal((int) cell.getNumericCellValue());
                default -> {
                    //No pasa por aca
                }
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("El Excel ingresado contiene datos no validos.");
        }
    }

    @Generated
    private String getCodigoValuByCell(Cell cell){
        try {
            return cell.getStringCellValue();
        }
        catch (IllegalStateException e) {
            int codigo = (int) cell.getNumericCellValue();
            return Integer.toString(codigo);
        }
    }
}
