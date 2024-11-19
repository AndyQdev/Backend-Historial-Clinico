package app.backendclinic.service_medic.repositorys;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import app.backendclinic.service_medic.models.HorarioAtencion;

@Repository
public interface HorarioAtencionRepository extends JpaRepository<HorarioAtencion, String>{
    @Query("SELECT h FROM HorarioAtencion h WHERE h.medico.usuario.id = :idUsuario")
    List<HorarioAtencion> findByUsuarioId(@Param("idUsuario") String idUsuario);
}
