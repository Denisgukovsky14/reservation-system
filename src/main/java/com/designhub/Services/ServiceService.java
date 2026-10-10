package com.designhub.Services;

import com.designhub.Tables.Services.ServiceDto;
import com.designhub.Tables.Services.ServiceRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServiceService {

    private final ServiceRepository serviceRepository;

    public ServiceService(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    public List<ServiceDto> findAll() {
        return serviceRepository.findAll()
                .stream()
                .map(ServiceDto::new)
                .collect(Collectors.toList());
    }

    public List<ServiceDto> findByIds(List<Long> ids) {
        return serviceRepository.findAllById(ids)
                .stream()
                .map(ServiceDto::new)
                .collect(Collectors.toList());
    }
}
