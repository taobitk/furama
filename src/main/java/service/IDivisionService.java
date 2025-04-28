package service;

import model.Division;
import java.sql.SQLException;
import java.util.List;

public interface IDivisionService {
    List<Division> findAll() throws SQLException;
}