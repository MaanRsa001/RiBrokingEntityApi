package com.maan.insurance.common.DB;
import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.springframework.jdbc.core.JdbcTemplate;

public class DatabaseDAO {

    private org.apache.logging.log4j.Logger logger = LogManager.getLogger(DatabaseDAO.class);
    private static DatabaseDAO mymanager = new DatabaseDAO();
    private transient JdbcTemplate template;
    private transient DataSource dataSource;

    private DatabaseDAO() {
        try {
            final InitialContext cxt = new InitialContext();
            dataSource = (DataSource) cxt.lookup("java:/comp/env/jdbc/UGReLive");
            // dataSource = (DataSource) cxt.lookup("java:/comp/env/jdbc/UGReLive");
            template = new JdbcTemplate(dataSource);
        } catch (Exception e) {
            logger.error("Error while creating DB instance ",e);
        }
    }

    public static DatabaseDAO getInstance() {
        return mymanager;
    }

    public Connection getDBConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public JdbcTemplate gettemplate() throws SQLException {
        return template;
    }

    public DataSource getDataSource() {
        return dataSource;
    }
}
