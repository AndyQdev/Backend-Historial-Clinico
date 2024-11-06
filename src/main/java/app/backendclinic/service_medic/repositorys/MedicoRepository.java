package app.backendclinic.service_medic.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.backendclinic.service_medic.models.Medico;


@Repository
public interface MedicoRepository extends JpaRepository<Medico, String>{
}