package br.edu.unifal.veiculos_service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VeiculoRepository extends JpaRepository<Veiculo, String> {
// Interface de persistência gerenciada pelo Spring Data JPA
}