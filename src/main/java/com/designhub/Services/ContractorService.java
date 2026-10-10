package com.designhub.Services;

import com.designhub.Tables.Contractors.ContractorDto;
import com.designhub.Tables.Contractors.ContractorEntity;
import com.designhub.Tables.Contractors.ContractorRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ContractorService {

    private final ContractorRepository contractorRepository;
    private final PasswordEncoder passwordEncoder;

    public ContractorService(ContractorRepository contractorRepository, PasswordEncoder passwordEncoder) {
        this.contractorRepository = contractorRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<ContractorDto> findAll() {
        return contractorRepository.findAll()
                .stream()
                .map(ContractorDto::new)
                .collect(Collectors.toList());
    }

    public ContractorDto findById(Long id) {
        ContractorEntity entity = contractorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Подрядчик не найден с id: " + id));
        return new ContractorDto(entity);
    }

    public ContractorDto findByEmail(String email) {
        ContractorEntity entity = contractorRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Подрядчик не найден с email: " + email));
        return new ContractorDto(entity);
    }

    public void updateProfile(Map<String, Object> data) {
        Long id = ((Number) data.get("id")).longValue();
        ContractorEntity entity = contractorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Исполнитель не найден"));

        if (data.containsKey("firstName")) entity.setFirstName((String) data.get("firstName"));
        if (data.containsKey("lastName")) entity.setLastName((String) data.get("lastName"));
        if (data.containsKey("companyName")) entity.setCompanyName((String) data.get("companyName"));
        if (data.containsKey("businessType")) entity.setBusinessType((String) data.get("businessType"));
        if (data.containsKey("address")) entity.setAddress((String) data.get("address"));
        if (data.containsKey("phone")) entity.setPhone((String) data.get("phone"));
        if (data.containsKey("email")) entity.setEmail((String) data.get("email"));
        if (data.containsKey("aboutCompany")) entity.setAboutCompany((String) data.get("aboutCompany"));
        if (data.containsKey("password")) entity.setPassword(passwordEncoder.encode((String) data.get("password")));

        contractorRepository.save(entity);
    }

    public ContractorDto findByEmailAndPassword(String email, String password) {
        ContractorEntity entity = contractorRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Пользователь с таким email не найден"));

        if (!passwordEncoder.matches(password, entity.getPassword())) {
            throw new RuntimeException("Неверный пароль");
        }

        return new ContractorDto(entity);
    }

    public ContractorDto create(ContractorDto dto, String password) {
        ContractorEntity entity = new ContractorEntity();
        entity.setFirstName(dto.firstName());
        entity.setLastName(dto.lastName());
        entity.setPatronymic(dto.patronymic());
        entity.setCompanyName(dto.companyName());
        entity.setBusinessType(dto.businessType());
        entity.setAddress(dto.address());
        entity.setPhone(dto.phone());
        entity.setEmail(dto.email());
        entity.setProfileImage(dto.profileImage());
        entity.setAboutCompany(dto.aboutCompany());
        entity.setServicesIds(dto.servicesIds());
        entity.setPassword(passwordEncoder.encode(password));

        ContractorEntity saved = contractorRepository.save(entity);
        return new ContractorDto(saved);
    }

    public boolean existsByEmail(String email) {
        return contractorRepository.existsByEmail(email);
    }
}