package com.sarcofuckusLesson.designhub.Controllers;

import com.sarcofuckusLesson.designhub.JwtService;
import com.sarcofuckusLesson.designhub.Services.ContractorService;
import com.sarcofuckusLesson.designhub.Services.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;
import com.sarcofuckusLesson.designhub.Tables.Contractors.*;
import com.sarcofuckusLesson.designhub.Tables.Customers.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final ContractorService contractorService;
    private final CustomerService customerService;
    private final JwtService jwtService;

    public AuthController(ContractorService contractorService, CustomerService customerService, JwtService jwtService) {
        this.contractorService = contractorService;
        this.customerService = customerService;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginData) {
        String email = loginData.get("email");
        String password = loginData.get("password");

        // Пробуем найти среди подрядчиков
        try {
            ContractorDto contractor = contractorService.findByEmailAndPassword(email, password);

            Map<String, Object> userMap = new HashMap<>();
            userMap.put("id", contractor.id());
            userMap.put("firstName", contractor.firstName());
            userMap.put("lastName", contractor.lastName());
            userMap.put("email", contractor.email());
            userMap.put("phone", contractor.phone());
            userMap.put("profileImage", contractor.profileImage());
            userMap.put("companyName", contractor.companyName());
            userMap.put("businessType", contractor.businessType());
            userMap.put("userType", "contractor");

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Успешный вход",
                    "user", userMap,
                    "token", jwtService.generateToken(contractor.id(), "contractor")
            ));
        } catch (RuntimeException e) {
            // Не нашли среди подрядчиков, пробуем среди заказчиков
        }

        // Пробуем найти среди заказчиков
        try {
            CustomerDto customer = customerService.findByEmailAndPassword(email, password);

            Map<String, Object> userMap = new HashMap<>();
            userMap.put("id", customer.id());
            userMap.put("firstName", customer.name());      // name → firstName
            userMap.put("lastName", customer.surname());   // surname → lastName
            userMap.put("email", customer.email());
            userMap.put("phone", customer.phone());
            userMap.put("profileImage", customer.profileImage());
            userMap.put("companyName", customer.companyName());
            userMap.put("businessType", customer.businessType());
            userMap.put("userType", "customer");

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Успешный вход",
                    "user", userMap,
                    "token", jwtService.generateToken(customer.id(), "customer")
            ));
        } catch (RuntimeException e) {
            // Не нашли ни там, ни там — единое сообщение независимо от причины (неверный email или неверный пароль)
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Неправильная почта или пароль"
            ));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> registerData) {
        try {
            String name = registerData.get("firstName");
            String surname = registerData.get("lastName");
            String patronymic = registerData.get("patronymic");
            String phone = registerData.get("phone");
            String email = registerData.get("email");
            String password = registerData.get("password");

            String companyName = registerData.get("companyName");
            String businessType = registerData.get("businessType");
            String address = registerData.get("address");
            String about = registerData.get("about");

            // Валидация обязательных полей
            if (name == null || name.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Имя обязательно"));
            }
            if (surname == null || surname.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Фамилия обязательна"));
            }
            if (phone == null || phone.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Телефон обязателен"));
            }
            if (email == null || email.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Email обязателен"));
            }
            if (password == null || password.length() < 8) {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Пароль должен быть не менее 8 символов"));
            }

            // Проверка на существующего пользователя (и среди подрядчиков, и среди заказчиков)
            try {
                contractorService.findByEmail(email);
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Пользователь с таким email уже существует (как подрядчик)"));
            } catch (RuntimeException e) {
                // Не найден среди подрядчиков — хорошо
            }

            if (customerService.existsByEmail(email)) {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Пользователь с таким email уже существует (как заказчик)"));
            }

            // Регистрируем как заказчика
            CustomerDto newCustomer = customerService.create(new CustomerDto(
                    null, name, surname, patronymic,
                    companyName, businessType, address,
                    phone, email, null, about,
                    null, null
            ), password);

            Map<String, Object> userMap = new HashMap<>();
            userMap.put("id", newCustomer.id());
            userMap.put("firstName", newCustomer.name());
            userMap.put("lastName", newCustomer.surname());
            userMap.put("email", newCustomer.email());
            userMap.put("phone", newCustomer.phone());
            userMap.put("profileImage", newCustomer.profileImage());
            userMap.put("companyName", newCustomer.companyName());
            userMap.put("businessType", newCustomer.businessType());
            userMap.put("userType", "customer");

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Регистрация прошла успешно",
                    "user", userMap,
                    "token", jwtService.generateToken(newCustomer.id(), "customer")
            ));

        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                    "success", false,
                    "message", "Ошибка при регистрации: " + e.getMessage()
            ));
        }
    }
}