// BaseDAO.java - NEW FILE
 

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseDAO<T> {
    protected Connection connection;
    
    public BaseDAO(Connection connection) {
        this.connection = connection;
    }
    
    protected abstract T mapResultSetToEntity(ResultSet rs) throws SQLException;
    
    protected List<T> executeQuery(String sql, Object... params) throws SQLException {
        List<T> results = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            setParameters(stmt, params);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    results.add(mapResultSetToEntity(rs));
                }
            }
        }
        return results;
    }
    
    protected int executeUpdate(String sql, Object... params) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            setParameters(stmt, params);
            return stmt.executeUpdate();
        }
    }
    
    private void setParameters(PreparedStatement stmt, Object... params) throws SQLException {
    for (int i = 0; i < params.length; i++) { 
        stmt.setObject(i + 1, params[i]);
    }

        }
    }

