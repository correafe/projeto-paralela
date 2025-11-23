package br.edu.unifal.veiculos_service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;
import java.util.Locale; // Adicionado para corrigir a formatação de números

public class ClienteHttp {
    private static final String BASE_URL = "http://localhost:8082/veiculos";
    private static final HttpClient client = HttpClient.newHttpClient();

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Conectado ao API Gateway (Microserviço).");

            while (true) {
                exibirMenu();
                try {
                    // Lê a linha inteira e converte, para evitar bugs de pular linha
                    String linha = scanner.nextLine();
                    if (linha.isEmpty()) continue; // Ignora enter vazio
                    int escolha = Integer.parseInt(linha);

                    switch (escolha) {
                        case 1: inserirVeiculo(scanner); break;
                        case 2: removerVeiculo(scanner); break;
                        case 3: buscarVeiculo(scanner); break;
                        case 4: alterarVeiculo(scanner); break;
                        case 5: listarTodos(); break;
                        case 0: System.out.println("Saindo..."); return;
                        default: System.out.println("Opcao invalida.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Por favor, digite um numero.");
                } catch (Exception e) {
                    System.out.println("Erro: " + e.getMessage());
                }
            }
        }
    }

    private static void exibirMenu() {
        System.out.println("\n--- Gestao de Veiculos (Microservicos) ---");
        System.out.println("1. Inserir");
        System.out.println("2. Remover");
        System.out.println("3. Buscar por placa");
        System.out.println("4. Alterar");
        System.out.println("5. Listar todos");
        System.out.println("0. Sair");
        System.out.print("Escolha uma opcao: ");
    }

    private static void inserirVeiculo(Scanner scanner) throws IOException, InterruptedException {
        System.out.print("Placa: "); String placa = scanner.nextLine();
        System.out.print("Marca: "); String marca = scanner.nextLine();
        System.out.print("Modelo: "); String modelo = scanner.nextLine();
        
        System.out.print("Ano: "); 
        int ano = Integer.parseInt(scanner.nextLine());
        
        System.out.print("Cor: "); String cor = scanner.nextLine();
        
        System.out.print("KM: "); 
        double km = Double.parseDouble(scanner.nextLine());
        
        System.out.print("Valor: "); 
        double valor = Double.parseDouble(scanner.nextLine());

        // CORREÇÃO: Usando Locale.US para garantir pontos em decimais no JSON
        String json = String.format(Locale.US,
            "{\"placa\":\"%s\",\"marca\":\"%s\",\"modelo\":\"%s\",\"ano\":%d,\"cor\":\"%s\",\"quilometragem\":%.2f,\"valor\":%.2f}",
            placa, marca, modelo, ano, cor, km, valor
        );

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() == 200) {
            System.out.println("Sucesso! Veiculo inserido.");
        } else {
            System.out.println("Erro ao inserir: " + response.statusCode());
            System.out.println("Resposta do servidor: " + response.body());
        }
    }

    private static void removerVeiculo(Scanner scanner) throws IOException, InterruptedException {
        System.out.print("Placa para remover: ");
        String placa = scanner.nextLine();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + placa))
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() == 200) {
            System.out.println("Veiculo removido com sucesso.");
        } else {
            System.out.println("Erro ao remover (talvez nao exista): " + response.statusCode());
        }
    }

    private static void buscarVeiculo(Scanner scanner) throws IOException, InterruptedException {
        System.out.print("Placa para buscar: ");
        String placa = scanner.nextLine();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + placa))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() == 200 && response.body() != null && !response.body().isEmpty()) {
            System.out.println("Veiculo encontrado: " + response.body());
        } else {
            System.out.println("Veiculo nao encontrado.");
        }
    }

    private static void listarTodos() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("--- Lista de Veiculos ---");
        System.out.println(response.body());
    }

    private static void alterarVeiculo(Scanner scanner) throws IOException, InterruptedException {
        System.out.print("Placa do veiculo para alterar: ");
        String placa = scanner.nextLine();
        
        System.out.println("Digite os novos dados:");
        System.out.print("Marca: "); String marca = scanner.nextLine();
        System.out.print("Modelo: "); String modelo = scanner.nextLine();
        System.out.print("Ano: "); int ano = Integer.parseInt(scanner.nextLine());
        System.out.print("Cor: "); String cor = scanner.nextLine();
        System.out.print("KM: "); double km = Double.parseDouble(scanner.nextLine());
        System.out.print("Valor: "); double valor = Double.parseDouble(scanner.nextLine());

        // CORREÇÃO: Usando Locale.US aqui também
        String json = String.format(Locale.US,
            "{\"placa\":\"%s\",\"marca\":\"%s\",\"modelo\":\"%s\",\"ano\":%d,\"cor\":\"%s\",\"quilometragem\":%.2f,\"valor\":%.2f}",
            placa, marca, modelo, ano, cor, km, valor
        );

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + placa))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() == 200) {
            System.out.println("Veiculo alterado com sucesso!");
        } else {
            System.out.println("Erro ao alterar: " + response.statusCode());
        }
    }
}