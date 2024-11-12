package app.backendclinic.Reportes.repositorys;

import app.backendclinic.Reportes.models.Bitacora;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BitacoraRepository extends JpaRepository<Bitacora, String> {
    List<Bitacora> findByUsuarioId(String usuarioId);
}
