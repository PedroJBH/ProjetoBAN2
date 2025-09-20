package com.biblioteca;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final String DATABASE_URL = "jdbc:sqlite:biblioteca.db";

    public static void main(String[] args) {

        DatabaseManager.initializeDatabase();

        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                printMenu();
                int choice = -1;
                try {
                    choice = scanner.nextInt();
                    scanner.nextLine(); 
                } catch (InputMismatchException e) {
                    System.out.println("Entrada inválida. Por favor, digite um número.");
                    scanner.nextLine(); 
                    continue;
                }

                switch (choice) {
                    case 1:
                        menuCRUD(scanner);
                        break;
                    case 2:
                        menuProcessosDeNegocio(scanner);
                        break;
                    case 3:
                        menuRelatorios();
                        break;
                    case 0:
                        System.out.println("Saindo da aplicação. Até mais!");
                        return;
                    default:
                        System.out.println("Opção inválida. Tente novamente.");
                }
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n--- MENU PRINCIPAL ---");
        System.out.println("1. Operações CRUD");
        System.out.println("2. Processos de Negócio");
        System.out.println("3. Relatórios do Sistema");
        System.out.println("0. Sair");
        System.out.print("Escolha uma opção: ");
    }

    private static void menuCRUD(Scanner scanner) {
        while (true) {
            System.out.println("\n--- MENU CRUD ---");
            System.out.println("1. Gerenciar Livros");
            System.out.println("2. Gerenciar Autores");
            System.out.println("3. Gerenciar Usuários");
            System.out.println("0. Voltar ao menu principal");
            System.out.print("Escolha uma opção: ");
            int choice = -1;
            try {
                choice = scanner.nextInt();
                scanner.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, digite um número.");
                scanner.nextLine();
                continue;
            }

            switch (choice) {
                case 1:
                    menuCRUDLivros(scanner);
                    break;
                case 2:
                    menuCRUDAutores(scanner);
                    break;
                case 3:
                    menuCRUDUsuarios(scanner);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }

    private static void menuCRUDLivros(Scanner scanner) {
        LivroDAO livroDAO = new LivroDAO();
        while (true) {
            System.out.println("\n--- CRUD LIVROS ---");
            System.out.println("1. Cadastrar Livro");
            System.out.println("2. Consultar Livro por ID");
            System.out.println("3. Atualizar Livro");
            System.out.println("4. Remover Livro");
            System.out.println("5. Listar Todos os Livros");
            System.out.println("0. Voltar ao menu CRUD");
            System.out.print("Escolha uma opção: ");
            int choice = -1;
            try {
                choice = scanner.nextInt();
                scanner.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, digite um número.");
                scanner.nextLine();
                continue;
            }

            switch (choice) {
                case 1:
                    System.out.print("Título: ");
                    String titulo = scanner.nextLine();
                    System.out.print("Ano de Publicação: ");
                    int ano = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Editora: ");
                    String editora = scanner.nextLine();
                    livroDAO.create(new Livro(0, titulo, ano, editora));
                    break;
                case 2:
                    System.out.print("ID do Livro: ");
                    int id = scanner.nextInt();
                    scanner.nextLine();
                    Livro livro = livroDAO.read(id);
                    System.out.println(livro != null ? livro : "Livro não encontrado.");
                    break;
                case 3:
                    System.out.print("ID do Livro a ser atualizado: ");
                    int idUpdate = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Novo Título: ");
                    String novoTitulo = scanner.nextLine();
                    System.out.print("Novo Ano de Publicação: ");
                    int novoAno = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Nova Editora: ");
                    String novaEditora = scanner.nextLine();
                    livroDAO.update(new Livro(idUpdate, novoTitulo, novoAno, novaEditora));
                    break;
                case 4:
                    System.out.print("ID do Livro a ser removido: ");
                    int idDelete = scanner.nextInt();
                    scanner.nextLine();
                    livroDAO.delete(idDelete);
                    break;
                case 5:
                    List<Livro> livros = livroDAO.readAll();
                    livros.forEach(System.out::println);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }

    private static void menuCRUDAutores(Scanner scanner) {
        AutorDAO autorDAO = new AutorDAO();
        while (true) {
            System.out.println("\n--- CRUD AUTORES ---");
            System.out.println("1. Cadastrar Autor");
            System.out.println("2. Consultar Autor por ID");
            System.out.println("3. Atualizar Autor");
            System.out.println("4. Remover Autor");
            System.out.println("5. Listar Todos os Autores");
            System.out.println("0. Voltar ao menu CRUD");
            System.out.print("Escolha uma opção: ");
            int choice = -1;
            try {
                choice = scanner.nextInt();
                scanner.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, digite um número.");
                scanner.nextLine();
                continue;
            }

            switch (choice) {
                case 1:
                    System.out.print("Nome do Autor: ");
                    String nome = scanner.nextLine();
                    autorDAO.create(new Autor(0, nome));
                    break;
                case 2:
                    System.out.print("ID do Autor: ");
                    int id = scanner.nextInt();
                    scanner.nextLine();
                    Autor autor = autorDAO.read(id);
                    System.out.println(autor != null ? autor : "Autor não encontrado.");
                    break;
                case 3:
                    System.out.print("ID do Autor a ser atualizado: ");
                    int idUpdate = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Novo Nome: ");
                    String novoNome = scanner.nextLine();
                    autorDAO.update(new Autor(idUpdate, novoNome));
                    break;
                case 4:
                    System.out.print("ID do Autor a ser removido: ");
                    int idDelete = scanner.nextInt();
                    scanner.nextLine();
                    autorDAO.delete(idDelete);
                    break;
                case 5:
                    List<Autor> autores = autorDAO.readAll();
                    autores.forEach(System.out::println);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }

    private static void menuCRUDUsuarios(Scanner scanner) {
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        while (true) {
            System.out.println("\n--- CRUD USUÁRIOS ---");
            System.out.println("1. Cadastrar Usuário");
            System.out.println("2. Consultar Usuário por ID");
            System.out.println("3. Atualizar Usuário");
            System.out.println("4. Remover Usuário");
            System.out.println("5. Listar Todos os Usuários");
            System.out.println("0. Voltar ao menu CRUD");
            System.out.print("Escolha uma opção: ");
            int choice = -1;
            try {
                choice = scanner.nextInt();
                scanner.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, digite um número.");
                scanner.nextLine();
                continue;
            }

            switch (choice) {
                case 1:
                    System.out.print("Nome: ");
                    String nome = scanner.nextLine();
                    System.out.print("Email: ");
                    String email = scanner.nextLine();
                    System.out.print("Endereço: ");
                    String endereco = scanner.nextLine();
                    usuarioDAO.create(new Usuario(0, nome, email, endereco));
                    break;
                case 2:
                    System.out.print("ID do Usuário: ");
                    int id = scanner.nextInt();
                    scanner.nextLine();
                    Usuario usuario = usuarioDAO.read(id);
                    System.out.println(usuario != null ? usuario : "Usuário não encontrado.");
                    break;
                case 3:
                    System.out.print("ID do Usuário a ser atualizado: ");
                    int idUpdate = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Novo Nome: ");
                    String novoNome = scanner.nextLine();
                    System.out.print("Novo Email: ");
                    String novoEmail = scanner.nextLine();
                    System.out.print("Novo Endereço: ");
                    String novoEndereco = scanner.nextLine();
                    usuarioDAO.update(new Usuario(idUpdate, novoNome, novoEmail, novoEndereco));
                    break;
                case 4:
                    System.out.print("ID do Usuário a ser removido: ");
                    int idDelete = scanner.nextInt();
                    scanner.nextLine();
                    usuarioDAO.delete(idDelete);
                    break;
                case 5:
                    List<Usuario> usuarios = usuarioDAO.readAll();
                    usuarios.forEach(System.out::println);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }

    private static void menuProcessosDeNegocio(Scanner scanner) {
        EmprestimoDAO emprestimoDAO = new EmprestimoDAO();
        while (true) {
            System.out.println("\n--- PROCESSOS DE NEGÓCIO ---");
            System.out.println("1. Realizar Empréstimo de Livro");
            System.out.println("2. Devolver Livro");
            System.out.println("3. Adicionar Autor a um Livro");
            System.out.println("0. Voltar ao menu principal");
            System.out.print("Escolha uma opção: ");
            int choice = -1;
            try {
                choice = scanner.nextInt();
                scanner.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, digite um número.");
                scanner.nextLine();
                continue;
            }

            switch (choice) {
                case 1:
                    System.out.print("ID do Livro a ser emprestado: ");
                    int idLivroEmprestimo = scanner.nextInt();
                    System.out.print("ID do Usuário: ");
                    int idUsuarioEmprestimo = scanner.nextInt();
                    scanner.nextLine();
                    emprestimoDAO.realizarEmprestimo(idLivroEmprestimo, idUsuarioEmprestimo);
                    break;
                case 2:
                    System.out.print("ID do Empréstimo a ser finalizado: ");
                    int idEmprestimo = scanner.nextInt();
                    scanner.nextLine();
                    emprestimoDAO.devolverLivro(idEmprestimo);
                    break;
                case 3:
                    System.out.print("ID do Livro: ");
                    int idLivroAutor = scanner.nextInt();
                    System.out.print("ID do Autor: ");
                    int idAutorLivro = scanner.nextInt();
                    scanner.nextLine();
                    new LivroAutorDAO().adicionarAutorALivro(idLivroAutor, idAutorLivro);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }

    private static void menuRelatorios() {
        EmprestimoDAO emprestimoDAO = new EmprestimoDAO();
        AutorDAO autorDAO = new AutorDAO();
        System.out.println("\n--- RELATÓRIOS DO SISTEMA ---");
        
        System.out.println("\n--- Relatório 1: Empréstimos Ativos ---");
        List<Emprestimo> emprestimosAtivos = emprestimoDAO.relatorioEmprestimosAtivos();
        if (emprestimosAtivos.isEmpty()) {
            System.out.println("Não há empréstimos ativos no momento.");
        } else {
            emprestimosAtivos.forEach(e -> {
                Livro livro = new LivroDAO().read(e.getIdLivro());
                Usuario usuario = new UsuarioDAO().read(e.getIdUsuario());
                if (livro != null && usuario != null) {
                    System.out.println("ID: " + e.getId() + " | Livro: " + livro.getTitulo() + " | Usuário: " + usuario.getNome() + " | Data Empréstimo: " + e.getDataEmprestimo());
                }
            });
        }
        
        System.out.println("\n--- Relatório 2: Livros por Autor ---");
        Scanner scanner = new Scanner(System.in);
        System.out.print("Digite o ID do Autor para gerar o relatório: ");
        int autorId = -1;
        try {
            autorId = scanner.nextInt();
            scanner.nextLine();
        } catch (InputMismatchException e) {
            System.out.println("Entrada inválida. Por favor, digite um número.");
            scanner.nextLine();
        }

        if (autorId != -1) {
            List<Livro> livrosPorAutor = autorDAO.relatorioLivrosPorAutor(autorId);
            if (livrosPorAutor.isEmpty()) {
                System.out.println("Nenhum livro encontrado para o autor com ID " + autorId + ".");
            } else {
                Autor autor = autorDAO.read(autorId);
                System.out.println("Livros de " + (autor != null ? autor.getNome() : "Autor Desconhecido") + ":");
                livrosPorAutor.forEach(l -> System.out.println("  - " + l.getTitulo() + " (" + l.getAnoPublicacao() + ")"));
            }
        }

        System.out.println("\n--- Relatório 3: Usuários com Empréstimos Atrasados ---");
        List<Usuario> usuariosAtrasados = emprestimoDAO.relatorioUsuariosComAtraso();
        if (usuariosAtrasados.isEmpty()) {
            System.out.println("Nenhum usuário com empréstimos atrasados.");
        } else {
            usuariosAtrasados.forEach(u -> System.out.println("ID: " + u.getId() + " | Nome: " + u.getNome() + " | Email: " + u.getEmail()));
        }
        System.out.println();
    }
}