package app.backendclinic.repositorys;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.backendclinic.models.Usuario;

@Repository
public interface UserRepository extends JpaRepository<Usuario, String>{
    Optional<Usuario> findByUsername(String username);
}
