
package service;

import model.CustomerType;
import repository.CustomerTypeRepository;
import repository.iplm.ICustomerTypeRepository;
import java.util.List;

public class CustomerTypeService implements ICustomerTypeService {

    private ICustomerTypeRepository customerTypeRepository = new CustomerTypeRepository();

    @Override
    public List<CustomerType> findAll() {
        return customerTypeRepository.findAll();
    }
}