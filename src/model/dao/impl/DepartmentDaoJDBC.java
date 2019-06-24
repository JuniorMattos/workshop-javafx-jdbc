/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.dao.impl;

import db.DB;
import db.DbException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.dao.DepartmentDao;
import model.entities.Department;

/**
 *
 * @author Junior
 */
public class DepartmentDaoJDBC implements DepartmentDao {

    private Connection conn;

    public DepartmentDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Department obj) {
        PreparedStatement ps = null;

        try {
            ps = conn.prepareStatement(
                    "INSERT INTO department "
                    + "(Name) "
                    + "VALUES "
                    + "(?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            ps.setString(1, obj.getName());

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet rs = ps.getGeneratedKeys();

                if (rs.next()) {
                    int id = rs.getInt(1);
                    obj.setId(id);
                }
                DB.closeResultSet(rs);
            } else {
                throw new DbException("Unexpected erros! No rows Affected!");
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(ps);
        }
    }

    @Override
    public void updade(Department obj) {
        PreparedStatement ps = null;

        try {
            ps = conn.prepareStatement(
                    "UPDATE department "
                    + "SET Name = ? "
                    + "WHERE (Id = ?)"
            );

            ps.setString(1, obj.getName());
            ps.setInt(2, obj.getId());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(ps);
        }
    }

    @Override
    public void deleteById(Integer id) {
        PreparedStatement ps = null;

        try {
            ps = conn.prepareStatement("DELETE FROM department WHERE Id = ?");

            ps.setInt(1, id);
            int rows = ps.executeUpdate();

            if (rows == 0) {
                throw new DbException("Unexpected erros! No rows Affected!");
            }

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(ps);
        }
    }

    @Override
    public Department findById(Integer id) {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = conn.prepareStatement(
                    "SELECT * FROM department "
                    + "WHERE Id = ?"
            );

            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                Department dep = instantiateDepartment(rs);
                return dep;
            }
            return null;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeResultSet(rs);
            DB.closeStatement(ps);
        }
    }

    @Override
    public List<Department> findAll() {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = conn.prepareStatement(
                    "SELECT * FROM department "
            );

            rs = ps.executeQuery();
            List<Department> list = new ArrayList<>();

            while (rs.next()) {
                Department dep = instantiateDepartment(rs);
                list.add(dep);
            }
            return list;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeResultSet(rs);
            DB.closeStatement(ps);
        }
    }

    private Department instantiateDepartment(ResultSet rs) throws SQLException {
        Department dep = new Department();
        dep.setId(rs.getInt("Id"));
        dep.setName(rs.getString("Name"));
        return dep;
    }

}
