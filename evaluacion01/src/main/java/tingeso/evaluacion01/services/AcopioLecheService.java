package tingeso.evaluacion01.services;

import org.apache.poi.ss.formula.atp.Switch;
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
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

@Service
public class AcopioLecheService {
    @Autowired
    AcopioLecheRepository acopio_leche_repository;

    public void guardarAcopioLeche(AcopioLecheEntity acopio_leche){
        // Agregar validacion de datos (Turno: M o T, verificar que proveedor exista, fecha valida)
        acopio_leche_repository.save(acopio_leche);
    }

    public void guardarAcopiosLecheQuincena(ArrayList<AcopioLecheEntity> acopios_leche, QuincenaEntity quincena) {
        for (AcopioLecheEntity acopio_leche : acopios_leche) {
            acopio_leche.setQuincena(quincena);
            guardarAcopioLeche(acopio_leche);
        }
    }

    @Generated
    public ArrayList<AcopioLecheEntity> leerExcel(MultipartFile file) throws IOException {
        ArrayList<AcopioLecheEntity> acopios_leche = new ArrayList<>();
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
                switch(i_cell) {
                    case 0 -> acopio_leche.setFecha(cell.getDateCellValue());
                    case 1 -> acopio_leche.setTurno(cell.getStringCellValue());
                    case 2 -> proveedor.setCodigo(cell.getStringCellValue());
                    case 3 -> acopio_leche.setCantidad_leche((int) cell.getNumericCellValue());
                    default -> {
                    }
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
