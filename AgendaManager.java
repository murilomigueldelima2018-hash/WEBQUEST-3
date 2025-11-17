import java.io.*;
import java.util.*;

public class AgendaManager implements GerenciadorContatos {
    private final Map<String, Contato> contatos = new HashMap<>();

    @Override
    public synchronized void adicionarContato(Contato contato) throws ContatoExistenteException {
        String chave = contato.getNome().toLowerCase();
        if (contatos.containsKey(chave)) {
            throw new ContatoExistenteException("Contato já existente: " + contato.getNome());
        }
        contatos.put(chave, contato);
    }

    @Override
    public synchronized Contato buscarContato(String nome) throws ContatoNaoEncontradoException {
        Contato c = contatos.get(nome.toLowerCase());
        if (c == null) throw new ContatoNaoEncontradoException("Contato não encontrado: " + nome);
        return c;
    }

    @Override
    public synchronized void removerContato(String nome) throws ContatoNaoEncontradoException {
        String chave = nome.toLowerCase();
        if (!contatos.containsKey(chave)) {
            throw new ContatoNaoEncontradoException("Contato não encontrado: " + nome);
        }
        contatos.remove(chave);
    }

    @Override
    public synchronized List<Contato> listarTodosContatos() {
        return new ArrayList<>(contatos.values());
    }

    public List<Contato> listarContatosOrdenados() {
        List<Contato> lista = listarTodosContatos();
        lista.sort(Comparator.comparing(c -> c.getNome().toLowerCase()));
        return lista;
    }

    public List<Contato> buscarPorDominioEmail(String dominio) {
        if (dominio == null) return Collections.emptyList();
        String d = dominio.toLowerCase();
        List<Contato> resultado = new ArrayList<>();
        for (Contato c : contatos.values()) {
            String email = c.getEmail();
            if (email != null && email.toLowerCase().endsWith("@" + d)) {
                resultado.add(c);
            } else if (email != null && email.toLowerCase().contains("@") && email.toLowerCase().endsWith(d)) {
                resultado.add(c);
            }
        }
        return resultado;
    }

    public synchronized void salvarContatosCSV(String nomeArquivo) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(nomeArquivo))) {
            for (Contato c : listarContatosOrdenados()) {
                String linha = String.format("%s;%s;%s",
                        c.getNome().replace(";", ","),
                        c.getTelefone().replace(";", ","),
                        c.getEmail().replace(";", ","));
                bw.write(linha);
                bw.newLine();
            }
        }
    }

    public synchronized void carregarContatosCSV(String nomeArquivo) throws IOException {
        Map<String, Contato> novo = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(nomeArquivo))) {
            String linha;
            int linhaNum = 0;
            while ((linha = br.readLine()) != null) {
                linhaNum++;
                if (linha.trim().isEmpty()) continue;
                String[] partes = linha.split(";");
                if (partes.length < 3) {
                    System.err.println("Linha " + linhaNum + " inválida no CSV: " + linha);
                    continue;
                }
                String nome = partes[0].trim();
                String telefone = partes[1].trim();
                String email = partes[2].trim();
                Contato c = new Contato(nome, telefone, email);
                novo.put(nome.toLowerCase(), c);
            }
        }
        contatos.clear();
        contatos.putAll(novo);
    }
}
