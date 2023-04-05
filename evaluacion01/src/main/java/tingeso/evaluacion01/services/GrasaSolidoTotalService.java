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

import java.util.ArrayList;
import java.util.Iterator;

@Service
public class GrasaSolidoTotalService {
    @Autowired
    GrasaSolidoTotalRepository grasa_solido_total_repository;
    @Autowired
    ProveedorService proveedor_service;

    public void guardarGrasaSolidoTotal(GrasaSolidoTotalEntity grasa_solido_total){
        String codigo_proveedor = grasa_solido_total.getProveedor().getCodigo();
        String quincena = grasa_solido_total.getQuincena().toString();
        String id = codigo_proveedor + "-" + quincena;
        grasa_solido_total.setId(id);
        grasa_solido_total_repository.save(grasa_solido_total);
    }

    public void guardarGrasasSolidosTotales(ArrayList<GrasaSolidoTotalEntity> grasas_solidos_totales, QuincenaEntity quincena) {
        for (GrasaSolidoTotalEntity grasa_solido_total : grasas_solidos_totales) {
            grasa_solido_total.setQuincena(quincena);
            guardarGrasaSolidoTotal(grasa_solido_total);
        }
    }

    public void validarDatosGrasasSolidosTotales(ArrayList<GrasaSolidoTotalEntity> grasas_solidos_totales) throws Exception{
        for (GrasaSolidoTotalEntity grasa_solido_total : grasas_solidos_totales) {
            ProveedorEntity proveedor = grasa_solido_total.getProveedor();
            Integer porcentaje_grasa = grasa_solido_total.getPorcentaje_grasa();
            Integer porcentaje_solido_total = grasa_solido_total.getPorcentaje_solido_total();
            if(porcentaje_grasa < 0 || porcentaje_grasa > 100){
                throw new Exception("El porcentaje de grasa no es valido");
            }

            if(porcentaje_solido_total < 0 || porcentaje_solido_total > 100){
                throw new Exception("El porcentaje de solido total no es valido");
            }

            if(!proveedor_service.existeProveedor(proveedor)) {
                throw new Exception("Los proveedores tienen que estar registrados");
            }
        }
    }

    @Generated
    public ArrayList<GrasaSolidoTotalEntity> leerExcel(MultipartFile file) throws Exception {
        ArrayList<GrasaSolidoTotalEntity> grasas_solidos_totales = new ArrayList<>();
        String filename = file.getOriginalFilename();
        if(!filename.endsWith(".xlsx")){
            throw new Exception("El archivo ingresado no es un .xlsx");
        }
        XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
        XSSFSheet worksheet = workbook.getSheetAt(0);
        boolean row_verification = true;
        for(Row row: worksheet){
            if (row_verification)
            {
                row_verification = false;
                continue;
            }

            Iterator<Cell> cell_itr = row.iterator();
            int i_cell = 0;
            GrasaSolidoTotalEntity grasa_solido_total = new GrasaSolidoTotalEntity();
            ProveedorEntity proveedor = new ProveedorEntity();
            while (cell_itr.hasNext()){
                Cell cell = cell_itr.next();
                try {
                    switch (i_cell) {
                        case 0 ->  {
                            try {
                                proveedor.setCodigo(cell.getStringCellValue());
                            } catch (IllegalStateException e) {
                                int codigo = (int) cell.getNumericCellValue();
                                proveedor.setCodigo(Integer.toString(codigo));
                            }
                            break;
                        }
                        case 1 -> grasa_solido_total.setPorcentaje_grasa((int) cell.getNumericCellValue());
                        case 2 -> grasa_solido_total.setPorcentaje_solido_total((int) cell.getNumericCellValue());
                        default -> {
                        }
                    }
                }catch (Exception e){
                    throw new Exception("El Excel ingresado contiene datos no validos.");
                }
                i_cell++;
            }
            if(i_cell == 3) {
                grasa_solido_total.setProveedor(proveedor);
                grasas_solidos_totales.add(grasa_solido_total);
            }
        }

        return grasas_solidos_totales;
    }
}
