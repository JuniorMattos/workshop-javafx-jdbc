/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.services;

import java.util.List;
import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

/**
 *
 * @author Junior
 */
public class DepartmentService {

    private DepartmentDao dao = DaoFactory.createDepartmentDao();

    public List<Department> findAll() {
        return dao.findAll();
    }

    public void saveOrUpdate(Department obj) {
        if (obj.getId() == null) {
            dao.insert(obj);
        } else {
            dao.updade(obj);
        }
    }
    
    public void remove(Department obj){
        dao.deleteById(obj.getId());
    }
}
