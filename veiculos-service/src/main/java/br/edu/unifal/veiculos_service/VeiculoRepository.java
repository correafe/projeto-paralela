package br.edu.unifal.veiculos_service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VeiculoRepository extends JpaRepository<Veiculo, String> {
    // Pronto! O Spring faz a mágica do SQL aqui.
}