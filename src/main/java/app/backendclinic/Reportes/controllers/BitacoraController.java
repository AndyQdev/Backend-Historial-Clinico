package app.backendclinic.Reportes.controllers;

import app.backendclinic.Reportes.Dtos.BitacoraDto;
import app.backendclinic.Reportes.models.Bitacora;
import app.backendclinic.Reportes.services.BitacoraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.util.List;
import org.springframework.http.MediaType;

@RestController
@RequestMapping("/api/bitacora")
public class BitacoraController {

    @Autowired
    private BitacoraService bitacoraService;

    @GetMapping
    public ResponseEntity<List<BitacoraDto>> obtenerAcciones(
            @RequestParam(defaultValue = "true") boolean hoy) {
        try {
            List<BitacoraDto> bitacoras = hoy ? bitacoraService.obtenerAccionesDeHoyDto()
                    : bitacoraService.obtenerTodasLasAccionesDto();
            return new ResponseEntity<>(bitacoras, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<BitacoraDto>> obtenerAccionesPorUsuario(@PathVariable String usuarioId) {
        try {
            List<BitacoraDto> bitacoras = bitacoraService.obtenerAccionesPorUsuarioDto(usuarioId);
            if (bitacoras.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(bitacoras, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/export/pdf")
    public ResponseEntity<byte[]> exportarBitacoraPDFPorUsuario(@RequestParam String usuarioId) {
        ByteArrayOutputStream pdfStream = bitacoraService.exportarBitacoraPDFPorUsuario(usuarioId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=bitacora_" + usuarioId + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfStream.toByteArray());
    }
}
