/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import db.DbException;
import gui.listiners.DataChangeListiner;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;
import model.exceptions.ValidationException;
import model.services.DepartmentService;

/**
 * FXML Controller class
 *
 * @author Junior
 */
public class DepartmentFormController implements Initializable {
    
    @FXML
    private TextField txtId;
    
    @FXML
    private TextField txtName;
    
    @FXML
    private Label labelErrorName;
    
    @FXML
    private Button btSave;
    
    @FXML
    private Button btCancel;
    
    private Department entity;
    private DepartmentService service;
    private List<DataChangeListiner> dataChangeListiners = new ArrayList<>();
    
    public void setDepartment(Department entity) {
        this.entity = entity;
    }
    
    public void setDepartmentService(DepartmentService service) {
        this.service = service;
    }
    
    public void subscribeDataChangeListiner(DataChangeListiner listiner) {
        dataChangeListiners.add(listiner);
    }
    
    @FXML
    public void onBtSaveAction(ActionEvent event) {
        if (entity == null) {
            throw new IllegalStateException("Entity was null!");
        }
        if (service == null) {
            throw new IllegalStateException("service was null!");
        }
        try {
            entity = getFormData();
            service.saveOrUpdate(entity);
            notifyDataChangeListiners();
            Utils.currentStage(event).close();
        } 
        catch (ValidationException e) {
            setErrorMessages(e.getErrors());
        }
        catch (DbException e) {
            Alerts.showAlert("Error saving object", null, e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    @FXML
    public void onBtCancelAction(ActionEvent event) {
        Utils.currentStage(event).close();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeNodes();
    }
    
    private void initializeNodes() {
        Constraints.setTextFieldInteger(txtId);
        Constraints.setTextFieldMaxLength(txtName, 30);
    }
    
    public void updateFormData() {
        if (entity == null) {
            throw new IllegalStateException("Entity was null!");
        }
        txtId.setText(String.valueOf(entity.getId()));
        txtName.setText(entity.getName());
    }
    
    private Department getFormData() {
        Department obj = new Department();
        
        ValidationException exception = new ValidationException("Validation Error!");
        
        obj.setId(Utils.tryParseToInt(txtId.getText()));
        
        if (txtName.getText() == null || txtName.getText().trim().equals("")) {
            exception.addError("name", "Field can't be empty!");
        }
        obj.setName(txtName.getText());
        
        if (exception.getErrors().size() > 0) {
            throw exception;
        }
        
        return obj;
    }
    
    private void notifyDataChangeListiners() {
        for (DataChangeListiner listiner : dataChangeListiners) {
            listiner.onDataChange();
        }
    }
    
    private void setErrorMessages(Map<String, String> errors) {
        Set<String> fields = errors.keySet();
        
        if (fields.contains("name")) {
            labelErrorName.setText(errors.get("name"));
        }
    }
}
