package site.easy.to.build.crm.service.imports;

import site.easy.to.build.crm.dto.ImportRequest;
import site.easy.to.build.crm.dto.ImportResult;

import java.util.List;

public interface ImportService {

    public String processImport (String json) throws Exception;
    public List<ImportRequest> parseJson(String json) throws Exception;

    
} 
