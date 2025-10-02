package com.biblioteca;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    
    private static final LivroDAO livroDAO = new LivroDAO();
    private static final AutorDAO autorDAO = new AutorDAO();
    private static final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private static final EmprestimoDAO emprestimoDAO = new EmprestimoDAO();
    private static final LivroAutorDAO livroAutorDAO = new LivroAutorDAO();
    private static final EditoraDAO editoraDAO = new EditoraDAO();
    private static final CategoriaDAO categoriaDAO = new CategoriaDAO();
    private static final ExemplarDAO exemplarDAO = new ExemplarDAO();
    private static final ReservaDAO reservaDAO = new ReservaDAO();

    public static void main(String[] args) {
        DatabaseManager.initializeDatabase();

        while (true) {
            printMenu();
            int choice = readInt("Escolha uma opção: ");

            switch (choice) {
                case 1:
                    menuCRUD();
                    break;
                case 2:
                    menuProcessosDeNegocio();
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

    private static void printMenu() {
        System.out.println("\n--- MENU PRINCIPAL ---");
        System.out.println("1. Operações CRUD");
        System.out.println("2. Processos de Negócio");
        System.out.println("3. Relatórios do Sistema");
        System.out.println("0. Sair");
    }

    private static void menuCRUD() {
        while (true) {
            System.out.println("\n--- MENU CRUD ---");
            System.out.println("1. Gerenciar Livros");
            System.out.println("2. Gerenciar Autores");
            System.out.println("3. Gerenciar Usuários");
            System.out.println("4. Gerenciar Editoras");
            System.out.println("5. Gerenciar Categorias");
            System.out.println("6. Gerenciar Exemplares");
            System.out.println("7. Gerenciar Reservas");
            System.out.println("0. Voltar ao menu principal");
            int choice = readInt("Escolha uma opção: ");

            switch (choice) {
                case 1:
                    menuCRUDLivros();
                    break;
                case 2:
                    menuCRUDAutores();
                    break;
                case 3:
                    menuCRUDUsuarios();
                    break;
                case 4:
                    menuCRUDEditoras();
                    break;
                case 5:
                    menuCRUDCategorias();
                    break;
                case 6:
                    menuCRUDExemplares();
                    break;
                case 7:
                    menuCRUDReservas();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }

    private static void menuCRUDLivros() {
        while (true) {
            System.out.println("\n--- CRUD LIVROS (TÍTULOS) ---");
            System.out.println("1. Cadastrar Título de Livro");
            System.out.println("2. Consultar Livro por ID");
            System.out.println("3. Atualizar Livro");
            System.out.println("4. Remover Livro");
            System.out.println("5. Listar Todos os Livros");
            System.out.println("0. Voltar ao menu CRUD");
            int choice = readInt("Escolha uma opção: ");

            switch (choice) {
                case 1:
                    System.out.print("Título: ");
                    String titulo = scanner.nextLine();
                    int ano = readInt("Ano de Publicação: ");
                    int idEditora = readInt("ID da Editora: ");
                    int idCategoria = readInt("ID da Categoria: ");
                    
                    Editora editora = editoraDAO.read(idEditora);
                    Categoria categoria = categoriaDAO.read(idCategoria);
                    
                    if (editora != null && categoria != null) {
                        int novoLivroId = livroDAO.create(new Livro(0, titulo, ano, editora, categoria));
                        if (novoLivroId != -1) {
                            int idAutor = readInt("ID do Autor para associar ao livro (0 para pular): ");
                            if (idAutor != 0) {
                                Autor autor = autorDAO.read(idAutor);
                                if (autor != null) {
                                    livroAutorDAO.adicionarAutorALivro(novoLivroId, idAutor);
                                } else {
                                    System.out.println("Aviso: Autor não encontrado. Apenas o livro foi cadastrado.");
                                }
                            }
                        }
                    } else {
                        System.out.println("Erro: Editora ou Categoria não encontrados.");
                    }
                    break;
                case 2:
                    int id = readInt("ID do Livro: ");
                    Livro livro = livroDAO.read(id);
                    System.out.println(livro != null ? livro : "Livro não encontrado.");
                    break;
                case 3:
                    int idUpdate = readInt("ID do Livro a ser atualizado: ");
                    System.out.print("Novo Título: ");
                    String novoTitulo = scanner.nextLine();
                    int novoAno = readInt("Novo Ano de Publicação: ");
                    int novoIdEditora = readInt("Novo ID da Editora: ");
                    int novoIdCategoria = readInt("Novo ID da Categoria: ");
                    
                    Editora novaEditora = editoraDAO.read(novoIdEditora);
                    Categoria novaCategoria = categoriaDAO.read(novoIdCategoria);
                    
                    if (novaEditora != null && novaCategoria != null) {
                        livroDAO.update(new Livro(idUpdate, novoTitulo, novoAno, novaEditora, novaCategoria));
                    } else {
                        System.out.println("Erro: Nova Editora ou Categoria não encontrados.");
                    }
                    break;
                case 4:
                    int idDelete = readInt("ID do Livro a ser removido: ");
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

    private static void menuCRUDAutores() {
        while (true) {
            System.out.println("\n--- CRUD AUTORES ---");
            System.out.println("1. Cadastrar Autor");
            System.out.println("2. Consultar Autor por ID");
            System.out.println("3. Atualizar Autor");
            System.out.println("4. Remover Autor");
            System.out.println("5. Listar Todos os Autores");
            System.out.println("0. Voltar ao menu CRUD");
            int choice = readInt("Escolha uma opção: ");

            switch (choice) {
                case 1:
                    System.out.print("Nome do Autor: ");
                    String nome = scanner.nextLine();
                    autorDAO.create(new Autor(0, nome));
                    break;
                case 2:
                    int id = readInt("ID do Autor: ");
                    Autor autor = autorDAO.read(id);
                    System.out.println(autor != null ? autor : "Autor não encontrado.");
                    break;
                case 3:
                    int idUpdate = readInt("ID do Autor a ser atualizado: ");
                    System.out.print("Novo Nome: ");
                    String novoNome = scanner.nextLine();
                    autorDAO.update(new Autor(idUpdate, novoNome));
                    break;
                case 4:
                    int idDelete = readInt("ID do Autor a ser removido: ");
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

    private static void menuCRUDUsuarios() {
        while (true) {
            System.out.println("\n--- CRUD USUÁRIOS ---");
            System.out.println("1. Cadastrar Usuário");
            System.out.println("2. Consultar Usuário por ID");
            System.out.println("3. Atualizar Usuário");
            System.out.println("4. Remover Usuário");
            System.out.println("5. Listar Todos os Usuários");
            System.out.println("0. Voltar ao menu CRUD");
            int choice = readInt("Escolha uma opção: ");

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
                    int id = readInt("ID do Usuário: ");
                    Usuario usuario = usuarioDAO.read(id);
                    System.out.println(usuario != null ? usuario : "Usuário não encontrado.");
                    break;
                case 3:
                    int idUpdate = readInt("ID do Usuário a ser atualizado: ");
                    System.out.print("Novo Nome: ");
                    String novoNome = scanner.nextLine();
                    System.out.print("Novo Email: ");
                    String novoEmail = scanner.nextLine();
                    System.out.print("Novo Endereço: ");
                    String novoEndereco = scanner.nextLine();
                    usuarioDAO.update(new Usuario(idUpdate, novoNome, novoEmail, novoEndereco));
                    break;
                case 4:
                    int idDelete = readInt("ID do Usuário a ser removido: ");
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
    
    private static void menuCRUDEditoras() {
        while (true) {
            System.out.println("\n--- CRUD EDITORAS ---");
            System.out.println("1. Cadastrar Editora");
            System.out.println("2. Consultar Editora por ID");
            System.out.println("3. Atualizar Editora");
            System.out.println("4. Remover Editora");
            System.out.println("5. Listar Todas as Editoras");
            System.out.println("0. Voltar ao menu CRUD");
            int choice = readInt("Escolha uma opção: ");

            switch (choice) {
                case 1:
                    System.out.print("Nome da Editora: ");
                    String nome = scanner.nextLine();
                    editoraDAO.create(new Editora(0, nome));
                    break;
                case 2:
                    int id = readInt("ID da Editora: ");
                    Editora editora = editoraDAO.read(id);
                    System.out.println(editora != null ? editora : "Editora não encontrada.");
                    break;
                case 3:
                    int idUpdate = readInt("ID da Editora a ser atualizada: ");
                    System.out.print("Novo Nome: ");
                    String novoNome = scanner.nextLine();
                    editoraDAO.update(new Editora(idUpdate, novoNome));
                    break;
                case 4:
                    int idDelete = readInt("ID da Editora a ser removida: ");
                    editoraDAO.delete(idDelete);
                    break;
                case 5:
                    List<Editora> editoras = editoraDAO.readAll();
                    editoras.forEach(System.out::println);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }
    
    private static void menuCRUDCategorias() {
        while (true) {
            System.out.println("\n--- CRUD CATEGORIAS ---");
            System.out.println("1. Cadastrar Categoria");
            System.out.println("2. Consultar Categoria por ID");
            System.out.println("3. Atualizar Categoria");
            System.out.println("4. Remover Categoria");
            System.out.println("5. Listar Todas as Categorias");
            System.out.println("0. Voltar ao menu CRUD");
            int choice = readInt("Escolha uma opção: ");

            switch (choice) {
                case 1:
                    System.out.print("Nome da Categoria: ");
                    String nome = scanner.nextLine();
                    categoriaDAO.create(new Categoria(0, nome));
                    break;
                case 2:
                    int id = readInt("ID da Categoria: ");
                    Categoria categoria = categoriaDAO.read(id);
                    System.out.println(categoria != null ? categoria : "Categoria não encontrada.");
                    break;
                case 3:
                    int idUpdate = readInt("ID da Categoria a ser atualizada: ");
                    System.out.print("Novo Nome: ");
                    String novoNome = scanner.nextLine();
                    categoriaDAO.update(new Categoria(idUpdate, novoNome));
                    break;
                case 4:
                    int idDelete = readInt("ID da Categoria a ser removida: ");
                    categoriaDAO.delete(idDelete);
                    break;
                case 5:
                    List<Categoria> categorias = categoriaDAO.readAll();
                    categorias.forEach(System.out::println);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }

    private static void menuCRUDExemplares() {
        while (true) {
            System.out.println("\n--- CRUD EXEMPLARES (CÓPIAS FÍSICAS) ---");
            System.out.println("1. Cadastrar Novo Exemplar para um Livro");
            System.out.println("2. Consultar Exemplar por ID");
            System.out.println("3. Atualizar Status de Exemplar");
            System.out.println("4. Remover Exemplar");
            System.out.println("5. Listar Todos os Exemplares");
            System.out.println("0. Voltar ao menu CRUD");
            int choice = readInt("Escolha uma opção: ");

            switch (choice) {
                case 1:
                    int idLivroExemplar = readInt("ID do Livro (título) ao qual este exemplar pertence: ");
                    Livro livroAssociado = livroDAO.read(idLivroExemplar);
                    if (livroAssociado != null) {
                        exemplarDAO.create(new Exemplar(0, livroAssociado, "Disponível"));
                    } else {
                        System.out.println("Erro: Livro (título) não encontrado.");
                    }
                    break;
                case 2:
                    int id = readInt("ID do Exemplar: ");
                    Exemplar exemplar = exemplarDAO.read(id);
                    System.out.println(exemplar != null ? exemplar : "Exemplar não encontrado.");
                    break;
                case 3:
                    int idUpdate = readInt("ID do Exemplar a ser atualizado: ");
                    Exemplar exemplarUpdate = exemplarDAO.read(idUpdate);
                    if (exemplarUpdate != null) {
                        System.out.print("Novo Status (Disponível, Emprestado, Reservado): ");
                        String novoStatus = scanner.nextLine();
                        exemplarUpdate.setStatus(novoStatus);
                        exemplarDAO.update(exemplarUpdate);
                    } else {
                        System.out.println("Exemplar não encontrado.");
                    }
                    break;
                case 4:
                    int idDelete = readInt("ID do Exemplar a ser removido: ");
                    exemplarDAO.delete(idDelete);
                    break;
                case 5:
                    List<Exemplar> exemplares = exemplarDAO.readAll();
                    exemplares.forEach(System.out::println);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }
    
    private static void menuCRUDReservas() {
        while (true) {
            System.out.println("\n--- CRUD RESERVAS ---");
            System.out.println("1. Listar todas as Reservas");
            System.out.println("2. Consultar Reserva por ID");
            System.out.println("3. Remover Reserva");
            System.out.println("0. Voltar ao menu CRUD");
            int choice = readInt("Escolha uma opção: ");

            switch (choice) {
                case 1:
                    List<Reserva> reservas = reservaDAO.readAll();
                    reservas.forEach(System.out::println);
                    break;
                case 2:
                    int id = readInt("ID da Reserva: ");
                    Reserva reserva = reservaDAO.read(id);
                    System.out.println(reserva != null ? reserva : "Reserva não encontrada.");
                    break;
                case 3:
                    int idDelete = readInt("ID da Reserva a ser removida: ");
                    reservaDAO.delete(idDelete);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }

    private static void menuProcessosDeNegocio() {
        while (true) {
            System.out.println("\n--- PROCESSOS DE NEGÓCIO ---");
            System.out.println("1. Realizar Empréstimo ou Reserva de Livro");
            System.out.println("2. Devolver Livro");
            System.out.println("3. Adicionar Autor a um Livro");
            System.out.println("4. Renovar Empréstimo");
            System.out.println("0. Voltar ao menu principal");
            int choice = readInt("Escolha uma opção: ");

            switch (choice) {
                case 1:
                    int idLivroEmprestimo = readInt("ID do Livro (título): ");
                    int idUsuarioEmprestimo = readInt("ID do Usuário: ");
                    
                    Livro livro = livroDAO.read(idLivroEmprestimo);
                    Usuario usuario = usuarioDAO.read(idUsuarioEmprestimo);

                    if (livro != null && usuario != null) {
                        emprestimoDAO.realizarEmprestimo(idLivroEmprestimo, idUsuarioEmprestimo);
                    } else {
                        System.out.println("Erro: Livro e/ou Usuário não encontrados. Tente novamente.");
                    }
                    break;
                case 2:
                    int idEmprestimo = readInt("ID do Empréstimo a ser finalizado: ");
                    emprestimoDAO.devolverLivro(idEmprestimo);
                    break;
                case 3:
                    int idLivroAutor = readInt("ID do Livro: ");
                    int idAutorLivro = readInt("ID do Autor: ");
                    livroAutorDAO.adicionarAutorALivro(idLivroAutor, idAutorLivro);
                    break;
                case 4:
                    int idEmprestimoRenovacao = readInt("ID do Empréstimo a ser renovado: ");
                    emprestimoDAO.renovarEmprestimo(idEmprestimoRenovacao);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }

    private static void menuRelatorios() {
        System.out.println("\n--- RELATÓRIOS DO SISTEMA ---");
        
        System.out.println("\n--- Relatório 1: Empréstimos Ativos ---");
        List<Emprestimo> emprestimosAtivos = emprestimoDAO.relatorioEmprestimosAtivos();
        if (emprestimosAtivos.isEmpty()) {
            System.out.println("Não há empréstimos ativos no momento.");
        } else {
            emprestimosAtivos.forEach(e -> {
                Exemplar exemplar = exemplarDAO.read(e.getIdExemplar());
                if (exemplar != null) {
                    Livro livro = exemplar.getLivro();
                    Usuario usuario = usuarioDAO.read(e.getIdUsuario());
                    if (livro != null && usuario != null) {
                        System.out.println("ID: " + e.getId() + " | Livro: " + livro.getTitulo() + " | Usuário: " + usuario.getNome() + " | Data Empréstimo: " + e.getDataEmprestimo());
                    }
                }
            });
        }
        
        System.out.println("\n--- Relatório 2: Livros por Autor ---");
        int autorId = readInt("Digite o ID do Autor para gerar o relatório: ");
        List<Livro> livrosPorAutor = autorDAO.relatorioLivrosPorAutor(autorId);
        if (livrosPorAutor.isEmpty()) {
            System.out.println("Nenhum livro encontrado para o autor com ID " + autorId + ".");
        } else {
            Autor autor = autorDAO.read(autorId);
            System.out.println("Livros de " + (autor != null ? autor.getNome() : "Autor Desconhecido") + ":");
            livrosPorAutor.forEach(l -> System.out.println("   - " + l.getTitulo() + " (" + l.getAnoPublicacao() + ")"));
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
    
    private static int readInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                int value = scanner.nextInt();
                scanner.nextLine();
                return value;
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, digite um número inteiro.");
                scanner.nextLine(); 
            }
        }
    }
}