package br.edu.unifal.veiculos_service;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "tb_veiculos")
@Data // O Lombok cria os Getters, Setters, toString, etc. automaticamente
@NoArgsConstructor // Construtor vazio
@AllArgsConstructor // Construtor com todos os argumentos
public class Veiculo {

    @Id
    private String placa;
    private String marca;
    private String modelo;
    private int ano;
    private String cor;
    private double quilometragem;
    private double valor;
}