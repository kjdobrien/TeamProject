package teamproject.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * Class for connecting to database.
 */
@ManagedBean(name = "dbConnector")
@ApplicationScoped
public class DatabaseConnector {
    
    /** The connnection. */
    private static Connection con;
    
    /** The datasources. */
    private DataSource ds;

    /**
     * Instantiates a new database connector.
     */
    public DatabaseConnector() {
        try {
            Context ctx = new InitialContext();
            ds = (DataSource) ctx
                    .lookup( "java:comp/env/jdbc/TeamProject" );
        } catch (NamingException e) {
            e.printStackTrace();
        }
        try {
            if ( ds == null )
                throw new SQLException( "Can't get data source" );

            // get database connection
            con = ds.getConnection();

            if ( con == null )
                throw new SQLException( "Can't get database connection" );
        } catch (SQLException e) {
            System.err.println( e.getMessage() );
        }
    }

    /**
     * Execute query.
     *
     * @param statement the SQL statement
     * @return the result set
     */
    public ResultSet executeQuery( String statement ) {
        ResultSet result = null;
        try {
            PreparedStatement ps = con.prepareStatement( statement );
            // get customer data from database
            result = ps.executeQuery();
        } catch (SQLException e) {
            System.err.println( "Could not execute query: \"" + statement
                    + "\"\n" + e.getMessage() );
        }
        return result;
    }
    
    /**
     * Execute insert.
     *
     * @param statement the SQL statement
     */
    public int executeInsert( String statement ) {
        try {
            PreparedStatement ps = con.prepareStatement( statement, Statement.RETURN_GENERATED_KEYS );
            // get customer data from database
           ps.executeUpdate();
           ResultSet id =  ps.getGeneratedKeys();
          if (id.next()) {
              return id.getInt(1);
          }
        } catch (SQLException e) {
            System.err.println( "Could not execute query: \"" + statement
                    + "\"\n" + e.getMessage() );
        }
        return -1;
    }

}
