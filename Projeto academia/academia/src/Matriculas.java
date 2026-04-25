import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.sql.*;


public class Matriculas extends JFrame {

    private JComboBox<ComboItem> comboAluno, comboPlano, comboInstrutor;
    private JComboBox<String> comboStatus, comboFiltroStatus;
    private JTable tabela;
    private DefaultTableModel modelo;
    private int idSelecionado = -1;

    public Matriculas() {
        setTitle("Gerenciar Matrículas");
        setSize(900, 560);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        construirTela();
        carregarCombos();
        carregarTabela("todas");
    }

   
    static class ComboItem {
        int id;
        String descricao;
        ComboItem(int id, String descricao) { this.id = id; this.descricao = descricao; }
        public String toString() { return descricao; }
    }

    
    private void construirTela() {


        JPanel form = new JPanel(new GridLayout(4, 2, 8, 8));
        form.setBorder(BorderFactory.createTitledBorder("Dados da Matrícula"));
        form.setPreferredSize(new Dimension(0, 165));

        form.add(new JLabel("Aluno:"));
        comboAluno = new JComboBox<>();
        form.add(comboAluno);

        form.add(new JLabel("Plano:"));
        comboPlano = new JComboBox<>();
        form.add(comboPlano);

        form.add(new JLabel("Instrutor (opcional):"));
        comboInstrutor = new JComboBox<>();
        form.add(comboInstrutor);

        form.add(new JLabel("Status:"));
        comboStatus = new JComboBox<>(new String[]{"ativa", "cancelada"});
        form.add(comboStatus);

       
        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 6));
        JButton btnInserir   = new JButton("➕ Matricular");
        JButton btnAtualizar = new JButton("✏️ Atualizar Status");
        JButton btnExcluir   = new JButton("🗑 Excluir");
        JButton btnLimpar    = new JButton("🔄 Limpar");

        btnInserir.setBackground(new Color(76, 175, 80));    btnInserir.setForeground(Color.WHITE);
        btnAtualizar.setBackground(new Color(33, 150, 243)); btnAtualizar.setForeground(Color.WHITE);
        btnExcluir.setBackground(new Color(244, 67, 54));    btnExcluir.setForeground(Color.WHITE);

        for (JButton b : new JButton[]{btnInserir, btnAtualizar, btnExcluir, btnLimpar}) {
            b.setFocusPainted(false);
            b.setCursor(new Cursor(Cursor.HAND_CURSOR));
            botoes.add(b);
        }

        
        JPanel filtroPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        filtroPanel.add(new JLabel("Filtrar por status:"));
        comboFiltroStatus = new JComboBox<>(new String[]{"todas", "ativa", "cancelada"});
        JButton btnFiltrar = new JButton("🔍 Filtrar");
        JButton btnJoin    = new JButton("📊 Relatório JOIN");
        btnJoin.setToolTipText("Exibe consulta com INNER JOIN e LEFT JOIN no console");
        filtroPanel.add(comboFiltroStatus);
        filtroPanel.add(btnFiltrar);
        filtroPanel.add(btnJoin);

        
        modelo = new DefaultTableModel(
            new String[]{"ID", "Aluno", "Plano", "Valor (R$)", "Instrutor", "Status", "Início"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabela = new JTable(modelo);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.getColumnModel().getColumn(0).setMaxWidth(50);
        tabela.getColumnModel().getColumn(3).setMaxWidth(90);
        tabela.getColumnModel().getColumn(5).setMaxWidth(80);
        tabela.setRowHeight(24);

    
        JPanel topo = new JPanel(new BorderLayout());
        topo.add(form,   BorderLayout.CENTER);
        topo.add(botoes, BorderLayout.SOUTH);

        JPanel centro = new JPanel(new BorderLayout(0, 4));
        centro.add(filtroPanel,             BorderLayout.NORTH);
        centro.add(new JScrollPane(tabela), BorderLayout.CENTER);

        setLayout(new BorderLayout(0, 8));
        add(topo,   BorderLayout.NORTH);
        add(centro, BorderLayout.CENTER);

        
        btnInserir.addActionListener(e   -> inserir());
        btnAtualizar.addActionListener(e -> atualizar());
        btnExcluir.addActionListener(e   -> excluir());
        btnLimpar.addActionListener(e    -> limparCampos());
        btnFiltrar.addActionListener(e   -> carregarTabela((String) comboFiltroStatus.getSelectedItem()));
        btnJoin.addActionListener(e      -> exibirRelatorioJoin());

        tabela.getSelectionModel().addListSelectionListener(e -> {
            int linha = tabela.getSelectedRow();
            if (linha >= 0) {
                idSelecionado = (int) modelo.getValueAt(linha, 0);
                String statusLinha = (String) modelo.getValueAt(linha, 5);
                comboStatus.setSelectedItem(statusLinha);
            }
        });
    }

  
    private void carregarCombos() {
  
        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement ps = conn.prepareStatement(
                "SELECT id_aluno, nome FROM alunos ORDER BY nome");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next())
                comboAluno.addItem(new ComboItem(rs.getInt(1), rs.getString(2)));
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar alunos: " + ex.getMessage());
        }

     
        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement ps = conn.prepareStatement(
                "SELECT id_plano, nome FROM planos ORDER BY nome");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next())
                comboPlano.addItem(new ComboItem(rs.getInt(1), rs.getString(2)));
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar planos: " + ex.getMessage());
        }

       
        comboInstrutor.addItem(new ComboItem(-1, "— Nenhum —"));
        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement ps = conn.prepareStatement(
                "SELECT id_instrutor, nome FROM instrutores ORDER BY nome");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next())
                comboInstrutor.addItem(new ComboItem(rs.getInt(1), rs.getString(2)));
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar instrutores: " + ex.getMessage());
        }
    }

    private void carregarTabela(String filtroStatus) {
        modelo.setRowCount(0);

        String sql = "SELECT m.id_matricula, a.nome AS aluno, p.nome AS plano, "
                   + "       p.valor, i.nome AS instrutor, m.status, m.data_inicio "
                   + "FROM matriculas m "
                   + "INNER JOIN alunos      a ON m.id_aluno     = a.id_aluno "
                   + "INNER JOIN planos      p ON m.id_plano     = p.id_plano "
                   + "LEFT  JOIN instrutores i ON m.id_instrutor = i.id_instrutor "
                   + (filtroStatus.equals("todas") ? "" : "WHERE m.status = ? ")
                   + "ORDER BY m.id_matricula DESC";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (!filtroStatus.equals("todas")) ps.setString(1, filtroStatus);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getInt("id_matricula"),
                    rs.getString("aluno"),
                    rs.getString("plano"),
                    String.format("%.2f", rs.getDouble("valor")),
                    rs.getString("instrutor") != null ? rs.getString("instrutor") : "—",
                    rs.getString("status"),
                    rs.getDate("data_inicio")
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar matrículas: " + ex.getMessage());
        }
    }

    // ── INSERT ───────────────────────────────────────────────────────────────
    private void inserir() {
        ComboItem aluno     = (ComboItem) comboAluno.getSelectedItem();
        ComboItem plano     = (ComboItem) comboPlano.getSelectedItem();
        ComboItem instrutor = (ComboItem) comboInstrutor.getSelectedItem();

        if (aluno == null || plano == null) {
            JOptionPane.showMessageDialog(this, "Selecione aluno e plano.");
            return;
        }

        String sql = "INSERT INTO matriculas (id_aluno, id_plano, id_instrutor, status) "
                   + "VALUES (?, ?, ?, ?)";
        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, aluno.id);
            ps.setInt(2, plano.id);
            if (instrutor != null && instrutor.id != -1)
                ps.setInt(3, instrutor.id);
            else
                ps.setNull(3, Types.INTEGER);
            ps.setString(4, (String) comboStatus.getSelectedItem());
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Matrícula realizada com sucesso!");
            limparCampos();
            carregarTabela("todas");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao matricular: " + ex.getMessage());
        }
    }

   
    private void atualizar() {
        if (idSelecionado == -1) {
            JOptionPane.showMessageDialog(this, "Clique em uma matrícula na tabela para editar.");
            return;
        }
        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement ps = conn.prepareStatement(
                "UPDATE matriculas SET status=? WHERE id_matricula=?")) {

            ps.setString(1, (String) comboStatus.getSelectedItem());
            ps.setInt(2, idSelecionado);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Status atualizado!");
            limparCampos();
            carregarTabela("todas");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar: " + ex.getMessage());
        }
    }

  
    private void excluir() {
        if (idSelecionado == -1) {
            JOptionPane.showMessageDialog(this, "Clique em uma matrícula para excluir.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,
            "Excluir matrícula selecionada?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement ps = conn.prepareStatement(
                "DELETE FROM matriculas WHERE id_matricula=?")) {

            ps.setInt(1, idSelecionado);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Matrícula excluída!");
            limparCampos();
            carregarTabela("todas");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao excluir: " + ex.getMessage());
        }
    }

   
    private void exibirRelatorioJoin() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-5s %-25s %-18s %10s %-20s %-10s%n",
            "ID", "Aluno", "Plano", "Valor (R$)", "Instrutor", "Status"));
        sb.append("-".repeat(95)).append("\n");

        String sql = "SELECT m.id_matricula, a.nome AS aluno, p.nome AS plano, "
                   + "       p.valor, i.nome AS instrutor, m.status "
                   + "FROM matriculas m "
                   + "INNER JOIN alunos      a ON m.id_aluno     = a.id_aluno "
                   + "INNER JOIN planos      p ON m.id_plano     = p.id_plano "
                   + "LEFT  JOIN instrutores i ON m.id_instrutor = i.id_instrutor "
                   + "ORDER BY a.nome";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                sb.append(String.format("%-5d %-25s %-18s %10.2f %-20s %-10s%n",
                    rs.getInt("id_matricula"),
                    rs.getString("aluno"),
                    rs.getString("plano"),
                    rs.getDouble("valor"),
                    rs.getString("instrutor") != null ? rs.getString("instrutor") : "—",
                    rs.getString("status")));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
            return;
        }

        JTextArea area = new JTextArea(sb.toString());
        area.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        area.setEditable(false);
        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new Dimension(780, 320));

        JOptionPane.showMessageDialog(this, scroll,
            "Relatório: INNER JOIN + LEFT JOIN", JOptionPane.INFORMATION_MESSAGE);
    }

    private void limparCampos() {
        if (comboAluno.getItemCount()     > 0) comboAluno.setSelectedIndex(0);
        if (comboPlano.getItemCount()     > 0) comboPlano.setSelectedIndex(0);
        if (comboInstrutor.getItemCount() > 0) comboInstrutor.setSelectedIndex(0);
        comboStatus.setSelectedIndex(0);
        idSelecionado = -1;
        tabela.clearSelection();
    }
}
