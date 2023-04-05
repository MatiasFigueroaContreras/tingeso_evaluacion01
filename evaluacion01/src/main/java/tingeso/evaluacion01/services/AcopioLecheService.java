package tingeso.evaluacion01.services;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tingeso.evaluacion01.entities.AcopioLecheEntity;
import tingeso.evaluacion01.entities.ProveedorEntity;
import tingeso.evaluacion01.entities.QuincenaEntity;
import tingeso.evaluacion01.repositories.AcopioLecheRepository;

import lombok.Generated;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

@Service
public class AcopioLecheService {
    @Autowired
    AcopioLecheRepository acopio_leche_repository;
    @Autowired
    ProveedorService proveedor_service;

    public void guardarAcopioLeche(AcopioLecheEntity acopio_leche){
        acopio_leche_repository.save(acopio_leche);
    }

    public void guardarAcopiosLeches(ArrayList<AcopioLecheEntity> acopios_leche) {
        for (AcopioLecheEntity acopio_leche : acopios_leche) {
            guardarAcopioLeche(acopio_leche);
        }
    }

    public void validarDatosAcopioLecheQuincena(ArrayList<AcopioLecheEntity> acopios_leche, QuincenaEntity quincena) throws Exception{
        for (AcopioLecheEntity acopio_leche : acopios_leche) {
            String turno = acopio_leche.getTurno();
            Integer kls_leche = acopio_leche.getCantidad_leche();
            Date fecha = acopio_leche.getFecha();
            ProveedorEntity proveedor = acopio_leche.getProveedor();
            if(!turno.equals("M") && !turno.equals("T")){
                throw new Exception("Algun turno no es valido, debe ser M o T");
            }

            if(kls_leche < 0){
                throw new Exception("Los kilos de leche tienen que ser positivos");
            }

            if(!quincena.estaDentroQuincena(fecha)){
                throw new Exception("Las fechas ingresadas tienen que coincidir con la quincena");
            }

            if(!proveedor_service.existeProveedor(proveedor)) {
                throw new Exception("Los proveedores tienen que estar registrados");
            }
            acopio_leche.setQuincena(quincena);
        }
    }

    @Generated
    public ArrayList<AcopioLecheEntity> leerExcel(MultipartFile file) throws Exception {
        ArrayList<AcopioLecheEntity> acopios_leche = new ArrayList<>();
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
            AcopioLecheEntity acopio_leche = new AcopioLecheEntity();
            ProveedorEntity proveedor = new ProveedorEntity();
            while (cell_itr.hasNext()){
                Cell cell = cell_itr.next();
                try {
                    switch (i_cell) {
                        case 0 -> acopio_leche.setFecha(cell.getDateCellValue());
                        case 1 -> acopio_leche.setTurno(cell.getStringCellValue());
                        case 2 -> {
                            try {
                                proveedor.setCodigo(cell.getStringCellValue());
                            } catch (IllegalStateException e) {
                                int codigo = (int) cell.getNumericCellValue();
                                proveedor.setCodigo(Integer.toString(codigo));
                            }
                            break;
                        }
                        case 3 -> acopio_leche.setCantidad_leche((int) cell.getNumericCellValue());
                        default -> {
                        }
                    }
                }catch (Exception e){
                    throw new Exception("El Excel ingresado contiene datos no validos.");
                }
                i_cell++;
            }
            if(i_cell == 4) {
                acopio_leche.setProveedor(proveedor);
                acopios_leche.add(acopio_leche);
            }
        }

        return acopios_leche;
    }
}
