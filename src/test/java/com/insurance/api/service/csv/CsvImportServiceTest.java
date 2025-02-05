import com.insurance.api.service.CsvImportService;
import com.insurance.api.service.CsvProcessorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CsvImportServiceTest {

    @Mock
    private CsvProcessorService csvProcessorService;

    @InjectMocks
    private CsvImportService csvImportService;

    @Test
    void deveImportarCsvComSucesso() throws IOException {
        MultipartFile file = mock(MultipartFile.class);
        when(file.isEmpty()).thenReturn(false);
        when(file.getInputStream()).thenReturn(new ByteArrayInputStream("cabecalho\ndado1,dado2,dado3,100.00".getBytes()));

        assertDoesNotThrow(() -> csvImportService.processarCsv(file));
        verify(csvProcessorService, times(1)).processarCsv(file);
    }

    @Test
    void deveLancarErroAoImportarCsvNulo() {
        MultipartFile file = null;

        Exception exception = assertThrows(IllegalArgumentException.class, () -> csvImportService.processarCsv(file));
        assertEquals("O arquivo CSV está nulo ou vazio.", exception.getMessage());
    }

    @Test
    void deveLancarErroAoImportarCsvVazio() {
        MultipartFile file = mock(MultipartFile.class);
        when(file.isEmpty()).thenReturn(true);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> csvImportService.processarCsv(file));
        assertEquals("O arquivo CSV está nulo ou vazio.", exception.getMessage());
    }
}
