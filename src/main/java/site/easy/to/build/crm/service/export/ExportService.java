package site.easy.to.build.crm.service.export;

import site.easy.to.build.crm.dto.ExportDataDTO;

public interface ExportService {

    ExportDataDTO exportDataToJson(int customerId);

    String exportDataToCsv(int customerId);

    
} 
