package app.backendclinic.atencioncitas.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.backendclinic.atencioncitas.models.Cita;


@Repository
public interface CitasRepository extends JpaRepository<Cita, String>{
}
