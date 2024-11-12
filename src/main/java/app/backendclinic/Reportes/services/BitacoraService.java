package app.backendclinic.Reportes.services;
import app.backendclinic.Reportes.Dtos.BitacoraDto;
import app.backendclinic.Reportes.models.Bitacora;
import app.backendclinic.Reportes.repositorys.BitacoraRepository;
import app.backendclinic.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

//todo para la exportacion pdf
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;

import java.io.ByteArrayOutputStream;
@Service
public class BitacoraService {

    private static final Logger logger = LoggerFactory.getLogger(BitacoraService.class);

    @Autowired
    private BitacoraRepository bitacoraRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW) // Asegura la transacción para el método
    public Bitacora registrarAccion(String accion, User usuario, String tipoAccion, String ipAddress, String resultado) {


        Bitacora bitacora = Bitacora.builder()
                .accion(accion)
                .fechaHora(LocalDateTime.now())
                .usuario(usuario)
                .tipoAccion(tipoAccion)
                .ipAddress(ipAddress)
                .resultado(resultado)
                .build();

        Bitacora savedBitacora = bitacoraRepository.save(bitacora);
        bitacoraRepository.flush();  // Fuerza la persistencia inmediata



        return savedBitacora;
    }

    // Método para obtener todas las bitácoras como BitacoraDto
    public List<BitacoraDto> obtenerAccionesDeHoyDto() {
        LocalDate hoy = LocalDate.now();
        return bitacoraRepository.findAll().stream()
                .filter(bitacora -> bitacora.getFechaHora().toLocalDate().equals(hoy))
                .map(this::convertirABitacoraDto)
                .collect(Collectors.toList());
    }
    public List<BitacoraDto> obtenerAccionesPorUsuarioDto(String usuarioId) {
        return bitacoraRepository.findByUsuarioId(usuarioId).stream()
                .map(this::convertirABitacoraDto)
                .collect(Collectors.toList());
    }

    public List<BitacoraDto> obtenerTodasLasAccionesDto() {
        return bitacoraRepository.findAll().stream()
                .map(this::convertirABitacoraDto)
                .collect(Collectors.toList());
    }

    private BitacoraDto convertirABitacoraDto(Bitacora bitacora) {
        String usuarioNombre = bitacora.getUsuario().getNombre();
        String usuarioRol = bitacora.getUsuario().getRole().getName();
        return new BitacoraDto(
                bitacora.getAccion(),
                bitacora.getFechaHora(),
                bitacora.getTipoAccion(),
                bitacora.getIpAddress(),
                bitacora.getResultado(),
                usuarioNombre,
                usuarioRol
        );
    }
    public ByteArrayOutputStream exportarBitacoraPDFPorUsuario(String usuarioId) {
        List<Bitacora> bitacoras = bitacoraRepository.findByUsuarioId(usuarioId);

        // Buscar el usuario para obtener los detalles adicionales
        User usuario = bitacoras.isEmpty() ? null : bitacoras.get(0).getUsuario();
        if (usuario == null) {
            throw new RuntimeException("No se encontraron bitácoras para el usuario especificado.");
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Título del reporte
            document.add(new Paragraph("Reporte de Bitácora para Usuario").setBold().setFontSize(16));

            // Información del usuario
            String usuarioInfo = "Nombre: " + usuario.getNombre() + " " + usuario.getApellido() + "\n" +
                    "Teléfono: " + usuario.getTelefono() + "\n" +
                    "Rol: " + usuario.getRole().getName();
            document.add(new Paragraph(usuarioInfo).setFontSize(12).setMarginBottom(10));

            // Tabla de bitácora
            Table table = new Table(new float[]{3, 3, 2, 2, 2});
            table.addHeaderCell(new Cell().add(new Paragraph("Fecha y Hora")));
            table.addHeaderCell(new Cell().add(new Paragraph("Acción")));
            table.addHeaderCell(new Cell().add(new Paragraph("Tipo Acción")));
            table.addHeaderCell(new Cell().add(new Paragraph("IP Address")));
            table.addHeaderCell(new Cell().add(new Paragraph("Resultado")));

            for (Bitacora bitacora : bitacoras) {
                table.addCell(new Cell().add(new Paragraph(bitacora.getFechaHora().toString())));
                table.addCell(new Cell().add(new Paragraph(bitacora.getAccion())));
                table.addCell(new Cell().add(new Paragraph(bitacora.getTipoAccion())));
                table.addCell(new Cell().add(new Paragraph(bitacora.getIpAddress())));
                table.addCell(new Cell().add(new Paragraph(bitacora.getResultado())));
            }

            document.add(table);
            document.close();
        } catch (Exception e) {
            throw new RuntimeException("Error al exportar la bitácora a PDF", e);
        }

        return baos;
    }
}
