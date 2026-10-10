package com.designhub.Services;

import com.designhub.Tables.Customers.CustomerDto;
import com.designhub.Tables.Customers.CustomerEntity;
import com.designhub.Tables.Customers.CustomerRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomerService(CustomerRepository customerRepository, PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public CustomerDto findByEmail(String email) {
        CustomerEntity entity = customerRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Заказчик не найден с email: " + email));
        return new CustomerDto(entity);
    }

    public void updateProfile(Map<String, Object> data) {
        Long id = ((Number) data.get("id")).longValue();
        CustomerEntity entity = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Заказчик не найден"));

        if (data.containsKey("firstName")) entity.setName((String) data.get("firstName"));
        if (data.containsKey("lastName")) entity.setSurname((String) data.get("lastName"));
        if (data.containsKey("companyName")) entity.setCompanyName((String) data.get("companyName"));
        if (data.containsKey("businessType")) entity.setBusinessType((String) data.get("businessType"));
        if (data.containsKey("address")) entity.setAddress((String) data.get("address"));
        if (data.containsKey("phone")) entity.setPhone((String) data.get("phone"));
        if (data.containsKey("email")) entity.setEmail((String) data.get("email"));
        if (data.containsKey("about")) entity.setAbout((String) data.get("about"));
        if (data.containsKey("password")) entity.setPassword(passwordEncoder.encode((String) data.get("password")));

        customerRepository.save(entity);
    }

    public CustomerDto findById(Long id) {
        CustomerEntity entity = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("заказчик не найден с id: " + id));
        return new CustomerDto(entity);
    }

    public CustomerDto findByEmailAndPassword(String email, String password) {
        CustomerEntity entity = customerRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Пользователь с таким email не найден"));

        if (!passwordEncoder.matches(password, entity.getPassword())) {
            throw new RuntimeException("Неверный пароль");
        }

        return new CustomerDto(entity);
    }

    public CustomerDto create(CustomerDto dto, String password) {
        CustomerEntity entity = new CustomerEntity();
        entity.setName(dto.name());
        entity.setSurname(dto.surname());
        entity.setPatronymic(dto.patronymic());
        entity.setCompanyName(dto.companyName());
        entity.setBusinessType(dto.businessType());
        entity.setAddress(dto.address());
        entity.setPhone(dto.phone());
        entity.setEmail(dto.email());
        entity.setProfileImage(dto.profileImage());
        entity.setAbout(dto.about());
        entity.setPassword(passwordEncoder.encode(password));

        CustomerEntity saved = customerRepository.save(entity);
        return new CustomerDto(saved);
    }

    public boolean existsByEmail(String email) {
        return customerRepository.existsByEmail(email);
    }
}