package br.edu.unifal.veiculos_service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/veiculos")
public class VeiculoController {

    @Autowired
    private VeiculoRepository repository;

    @GetMapping
    public List<Veiculo> listarTodos() {
        return repository.findAll();
    }

    @GetMapping("/{placa}")
    public Veiculo buscarPorPlaca(@PathVariable String placa) {
        return repository.findById(placa).orElse(null);
    }

    @PostMapping
    public Veiculo inserir(@RequestBody Veiculo veiculo) {
        return repository.save(veiculo);
    }

    @DeleteMapping("/{placa}")
    public void remover(@PathVariable String placa) {
        repository.deleteById(placa);
    }
    
    @PutMapping("/{placa}")
    public Veiculo alterar(@PathVariable String placa, @RequestBody Veiculo veiculo) {
        veiculo.setPlaca(placa);
        return repository.save(veiculo);
    }
}