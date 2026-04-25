import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoBD {

    private static final String URL    = "jdbc:postgresql://localhost:5432/academia";
    private static final String USUARIO = "postgres";
    private static final String SENHA   = "1234";

    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, SENHA);
    }
}