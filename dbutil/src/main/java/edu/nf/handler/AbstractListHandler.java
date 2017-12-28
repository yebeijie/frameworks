package edu.nf.handler;

import edu.nf.ResultSetHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public abstract class AbstractListHandler<T> implements ResultSetHandler<List<T>> {

    public List<T> handle(ResultSet rs) throws SQLException {
        List<T> rows = new ArrayList<T>();
        while (rs.next()) {
            rows.add(getRow(rs));
        }
        return rows;
    }
	
	protected abstract T getRow(ResultSet rs) throws SQLException;
}
