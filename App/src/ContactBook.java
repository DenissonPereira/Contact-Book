import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

class Contato {
    private String nome;
    private String telefone;

    public Contato(String nome, String telefone) {
        this.nome = nome;
        this.telefone = telefone;
    }

    public String getNome() {
        return nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String novoTelefone) {
        this.telefone = novoTelefone;
    }
}

public class ContactBook {
    private ArrayList<Contato> contatos;
    private JFrame frame;
    private DefaultListModel<String> listModel;
    private JList<String> listaContatos;
    private JTextPane detalhesContato;

    public ContactBook() {
        contatos = new ArrayList<>();
        frame = new JFrame("Agenda de Contatos");
        listModel = new DefaultListModel<>();
        listaContatos = new JList<>(listModel);
        detalhesContato = new JTextPane();

        // Configuração da janelina
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Painel para entrada de dados
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2));

        JLabel labelNome = new JLabel("Nome:");
        JTextField textFieldNome = new JTextField(20);
        JLabel labelTelefone = new JLabel("Telefone:");
        JTextField textFieldTelefone = new JTextField(20);
        JButton buttonAdicionar = new JButton("Adicionar Contato");
        JButton buttonEditar = new JButton("Editar Contato");
        JButton buttonExcluir = new JButton("Excluir Contato");
        JButton buttonSalvar = new JButton("Salvar Contatos");

        panel.add(labelNome);
        panel.add(textFieldNome);
        panel.add(labelTelefone);
        panel.add(textFieldTelefone);
        panel.add(buttonAdicionar);
        panel.add(buttonEditar);
        panel.add(buttonExcluir);
        panel.add(buttonSalvar);

        // Painel para exibir os contatos
        JScrollPane scrollPane = new JScrollPane(listaContatos);

        frame.add(panel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(detalhesContato, BorderLayout.SOUTH);

        buttonAdicionar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nome = textFieldNome.getText();
                String telefone = textFieldTelefone.getText();
                adicionarContato(nome, telefone);
                atualizarListaContatos();
                textFieldNome.setText("");
                textFieldTelefone.setText("");
            }
        });

        buttonEditar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = listaContatos.getSelectedIndex();
                if (selectedIndex >= 0) {
                    String novoTelefone = textFieldTelefone.getText();
                    editarContato(selectedIndex, novoTelefone);
                    atualizarListaContatos();
                    textFieldTelefone.setText("");
                }
            }
        });

        buttonExcluir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = listaContatos.getSelectedIndex();
                if (selectedIndex >= 0) {
                    excluirContato(selectedIndex);
                    atualizarListaContatos();
                    detalhesContato.setText(""); // Limpando os detalhes do contato
                }
            }
        });

        buttonSalvar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exportarContatosParaArquivo();
            }
        });

        listaContatos.addListSelectionListener(e -> {
            int selectedIndex = listaContatos.getSelectedIndex();
            if (selectedIndex >= 0) {
                visualizarDetalhesContato(selectedIndex);
            }
        });

        frame.setVisible(true);
    }

    public void adicionarContato(String nome, String telefone) {
        Contato novoContato = new Contato(nome, telefone);
        contatos.add(novoContato);
    }

    public void editarContato(int index, String novoTelefone) {
        contatos.get(index).setTelefone(novoTelefone);
    }

    public void excluirContato(int index) {
        contatos.remove(index);
    }

    public void atualizarListaContatos() {
        listModel.clear();
        for (Contato contato : contatos) {
            listModel.addElement("Nome: " + contato.getNome() + ", Telefone: " + contato.getTelefone());
        }
    }

    public void visualizarDetalhesContato(int index) {
        Contato contato = contatos.get(index);
        detalhesContato.setText("Nome: " + contato.getNome() + "\nTelefone: " + contato.getTelefone());
    }

    public void exportarContatosParaArquivo() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Salvar Contatos em um Arquivo de Texto");
        int userSelection = fileChooser.showSaveDialog(frame);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            try {
                String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
                for (Contato contato : contatos) {
                    writer.write("Nome: " + contato.getNome() + ", Telefone: " + contato.getTelefone());
                    writer.newLine();
                }
                writer.close();
                JOptionPane.showMessageDialog(frame, "Contatos exportados com sucesso.");
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Erro ao exportar contatos.");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ContactBook();
            }
        });
    }
}
