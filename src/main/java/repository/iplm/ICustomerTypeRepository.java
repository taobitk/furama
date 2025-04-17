
package repository.iplm;

import model.CustomerType;
import java.util.List;

public interface ICustomerTypeRepository {
    List<CustomerType> findAll();
}