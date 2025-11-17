import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class AgendaApplication {
    private static final AgendaManager manager = new AgendaManager();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean rodando = true;
        while (rodando) {
            exibirMenu();
            String opc = scanner.nextLine().trim();
            switch (opc) {
                case "1": adicionarContato(); break;
                case "2": buscarContato(); break;
                case "3": removerContato(); break;
                case "4": listarTodos(); break;
                case "5": salvarCSV(); break;
                case "6": carregarCSV(); break;
                case "7":
                    System.out.println("Saindo...");
                    rodando = false; break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
            System.out.println();
        }
        scanner.close();
    }

    private static void exibirMenu() {
        System.out.println("=== AGENDA ELETRÔNICA ===");
        System.out.println("1. Adicionar Contato");
        System.out.println("2. Buscar Contato");
        System.out.println("3. Remover Contato");
        System.out.println("4. Listar Todos os Contatos");
        System.out.println("5. Salvar em CSV");
        System.out.println("6. Carregar de CSV");
        System.out.println("7. Sair");
        System.out.print("Escolha uma opção: ");
    }

    private static void adicionarContato() {
        System.out.print("Nome: ");
        String nome = scanner.nextLine().trim();
        System.out.print("Telefone: ");
        String tel = scanner.nextLine().trim();
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        try {
            manager.adicionarContato(new Contato(nome, tel, email));
            System.out.println("Contato adicionado com sucesso.");
        } catch (ContatoExistenteException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void buscarContato() {
        System.out.print("Nome para buscar: ");
        String nome = scanner.nextLine().trim();
        try {
            Contato c = manager.buscarContato(nome);
            System.out.println("Contato encontrado:\n" + c);
        } catch (ContatoNaoEncontradoException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void removerContato() {
        System.out.print("Nome para remover: ");
        String nome = scanner.nextLine().trim();
        try {
            manager.removerContato(nome);
            System.out.println("Contato removido com sucesso.");
        } catch (ContatoNaoEncontradoException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void listarTodos() {
        List<Contato> lista = manager.listarContatosOrdenados();
        if (lista.isEmpty()) {
            System.out.println("Agenda vazia.");
            return;
        }
        System.out.println("--- Lista de Contatos (ordenada) ---");
        for (Contato c : lista) System.out.println(c);
    }

    private static void salvarCSV() {
        System.out.print("Nome do arquivo para salvar (ex: contatos.csv): ");
        String nomeArquivo = scanner.nextLine().trim();
        try {
            manager.salvarContatosCSV(nomeArquivo);
            System.out.println("Contatos salvos em: " + nomeArquivo);
        } catch (IOException e) {
            System.out.println("Erro ao salvar arquivo: " + e.getMessage());
        }
    }

    private static void carregarCSV() {
        System.out.print("Nome do arquivo para carregar (ex: contatos.csv): ");
        String nomeArquivo = scanner.nextLine().trim();
        try {
            manager.carregarContatosCSV(nomeArquivo);
            System.out.println("Contatos carregados de: " + nomeArquivo);
        } catch (IOException e) {
            System.out.println("Erro ao carregar arquivo: " + e.getMessage());
        }
    }
}
