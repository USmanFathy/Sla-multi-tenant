package com.tenantFlow.workspace.seeder;

import com.tenantFlow.workspace.model.dto.request.OwnerSeedData;
import com.tenantFlow.workspace.model.entity.Role;
import com.tenantFlow.workspace.model.entity.User;
import com.tenantFlow.workspace.model.enums.UserStatus;
import com.tenantFlow.workspace.repository.RoleRepository;
import com.tenantFlow.workspace.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class TenantOwnerSeeder {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    @Transactional("tenantTransactionManager")
    public void seed(OwnerSeedData data) {

        if (userRepository
                .findByOwnerRegistrationId(data.registrationId())
                .isPresent()) {

            log.info("Owner already exists");

            return;
        }

        Role ownerRole = roleRepository
                .findByName("OWNER")
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Owner role not found"
                        )
                );

        User user = new User();

        user.setName(data.name());
        user.setUsername(data.username());
        user.setEmail(data.email());

        user.setOwnerRegistrationId(
                data.registrationId()
        );

        user.setRole(ownerRole);

        user.setStatus(UserStatus.INVITED);

        userRepository.save(user);

        log.info("Owner account prepared: {}",
                user.getId());
    }
}