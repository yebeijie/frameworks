package edu.nf;

import java.sql.*;

public class SQLExecutor {

    private Connection connection;

    //是否自动关闭连接，默认true为自动关闭
    private boolean autoClose = true;

    public SQLExecutor(Connection connection) {
        this.connection = connection;
    }


    /**
     * @param sql     sql语句
     * @param handler 查询解析器
     * @param params  查询语句所需的参数
     * @param <T>
     * @return
     */
    public <T> T executeQuery(String sql, ResultSetHandler handler, Object... params) {
        if (connection == null) {
            throw new SQLExecutorException("Null connection");
        }
        if (sql == null) {
            close();
            throw new SQLExecutorException("Null SQL statement");
        }
        if (handler == null) {
            close();
            throw new SQLExecutorException("Null ResultSetHandler");
        }
        PreparedStatement ps = null;
        ResultSet rs = null;
        T t = null;
        try {
            ps = connection.prepareStatement(sql);
            setParameters(ps, params);
            rs = ps.executeQuery();
            t = (T) handler.handle(rs);
        } catch (SQLException e) {
            throw new SQLExecutorException("Execute query fail.", e);
        } finally {
            close(rs);
            close(ps);
            if (autoClose) {
                close();
            }
        }
        return t;
    }

    /**
     * 执行DML操作
     *
     * @param sql
     * @param params
     * @return
     */
    public void executeUpdate(String sql, Object... params) {
        if (connection == null) {
            throw new SQLExecutorException("Null connection");
        }
        if (sql == null) {
            close();
            throw new SQLExecutorException("Null SQL statement");
        }
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            //设置SQL语句参数
            setParameters(ps, params);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new SQLExecutorException("Execute update fail.", e);
        } finally {
            //关闭PreparedStatement
            close(ps);
            //关闭连接
            if (autoClose) {
                close();
            }
        }

    }

    /**
     * 批量操作
     *
     * @param sql
     * @param params
     * @return
     * @throws SQLException
     */
    public void executeBatch(String sql, Object[][] params) {
        if (connection == null) {
            throw new SQLExecutorException("Null connection");
        }

        if (sql == null) {
            close();
            throw new SQLExecutorException("Null SQL statement");
        }

        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                setParameters(ps, params[i]);
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (SQLException e) {
            throw new SQLExecutorException("Execute batch fail.", e);
        } finally {
            close(ps);
            if (autoClose) {
                close();
            }
        }
    }

    /**
     * 设置SQL语句的参数
     *
     * @param ps
     * @param params
     * @throws SQLException
     */
    private void setParameters(PreparedStatement ps, Object[] params)
            throws SQLException {
        for (int i = 0; i < params.length; i++) {
            ps.setObject(i + 1, params[i]);
        }
    }

    /**
     * 开启事务
     */
    public void beginTransaction() {
        try {
            connection.setAutoCommit(false);
            autoClose = false;
        } catch (SQLException e) {
            throw new TransactionException("Begin transaction fail.", e);
        }
    }

    /**
     * 提交事务
     */
    public void commit() {
        try {
            connection.commit();
        } catch (SQLException e) {
            throw new TransactionException("Commit transaction fail.", e);
        }
        close();
    }

    /**
     * 回滚事务
     */
    public void rollback() {
        try {
            connection.rollback();
        } catch (SQLException e) {
            throw new TransactionException("Rollback transaction fail.", e);
        }
        close();
    }

    /**
     * 关闭Statement
     */
    private void close(Statement st) {
        try {
            if (st != null) {
                st.close();
            }
        } catch (SQLException e) {
            throw new CloseResourcesException("Close statement fail.", e);
        }
    }

    /**
     * 关闭结果集
     */
    private void close(ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            throw new CloseResourcesException("Close result set fail.", e);
        }
    }

    /**
     * 关闭连接
     */
    private void close() {
        try {
            if (!connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new CloseResourcesException("Close connection fail.", e);
        }
    }
}
