package br.com.romel.lounge.postgresqlasmq;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.postgresql.PGConnection;
import org.postgresql.PGNotification;

/**
 *
 * @author Carlos Romel Pereira da Silva <carlos.romel@gmail.com>
 */
public class PostgreSQLasMQ {

    public static void main(String[] args) {
        Properties propriedades = new Properties();
        int mensagens = 0;

        try {
            propriedades.load(new FileReader("conexao.properties"));
            String url = propriedades.getProperty("url", "jdbc:postgresql://localhost/template1");
            String lista = propriedades.getProperty("list", "geral");

            try (Connection conn = DriverManager.getConnection(url, propriedades)) {
                System.out.printf("Conexão: [%s]\n", conn.getClientInfo());
                System.out.printf("Lista..: [%s]\n", lista);
                conn.createStatement().execute(String.format("listen %s", lista));

                while (true) {
                    PGNotification[] ns = ((PGConnection) conn).getNotifications(10);

                    if (ns != null) {
                        int mensagensInternas = 0;
                        for (PGNotification n : ns) {
                            System.out.printf("#: [%8d, %8d], PID: [%8d], Evento: [%s], Tamaho: [%8d], Carga: [%s]\n",
                                    ++mensagens,
                                    ++mensagensInternas,
                                    n.getPID(),
                                    n.getName(),
                                    ns.length,
                                    n.getParameter());
                        }
                    }
                    Thread.sleep(10000);
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(PostgreSQLasMQ.class.getName()).log(Level.SEVERE,
                        "Não foi possível interromper a thread.", ex);
            } catch (SQLException ex) {
                Logger.getLogger(PostgreSQLasMQ.class.getName()).log(Level.SEVERE,
                        "A conexão não pode ser estabelecida.", ex);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PostgreSQLasMQ.class.getName()).log(Level.SEVERE,
                    "O arquivo não existe.", ex);
        } catch (IOException ex) {
            Logger.getLogger(PostgreSQLasMQ.class.getName()).log(Level.SEVERE,
                    "O arquivo não pode ser lido.", ex);
        }
    }
}
