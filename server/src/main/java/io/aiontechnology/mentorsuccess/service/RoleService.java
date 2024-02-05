/*
 * Copyright 2020-2024 Aion Technology LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.aiontechnology.mentorsuccess.service;

import io.aiontechnology.mentorsuccess.entity.SchoolPersonRole;
import io.aiontechnology.mentorsuccess.repository.SchoolPersonRoleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

/**
 * Service that provides business logic for roles.
 *
 * @author Whitney Hunter
 * @since 0.1.0
 */
@Service
@RequiredArgsConstructor
public class RoleService {

    /** The repository used to interact with the database */
    private final SchoolPersonRoleRepository schoolPersonRoleRepository;

    /**
     * Create a role in the database by saving the provided {@link SchoolPersonRole}.
     *
     * @param role The {@link SchoolPersonRole} to save.
     * @return The resulting {@link SchoolPersonRole}. Will have a db generated id populated.
     */
    @Transactional
    public SchoolPersonRole createRole(SchoolPersonRole role) {
        return schoolPersonRoleRepository.save(role);
    }

    /**
     * Deactivate a {@link SchoolPersonRole} in the system.
     *
     * @param role The {@link SchoolPersonRole} to deactivate.
     * @return The {@link SchoolPersonRole} after being deactivated.
     */
    @Transactional
    public SchoolPersonRole deactivateRole(SchoolPersonRole role) {
        role.setIsActive(false);
        schoolPersonRoleRepository.save(role);
        return role;
    }

    /**
     * Find a {@link SchoolPersonRole} by its id.
     *
     * @param id The id of the desired {@link SchoolPersonRole}.
     * @return The {@link SchoolPersonRole} if it could be found.
     */
    public Optional<SchoolPersonRole> findRoleById(UUID id) {
        return schoolPersonRoleRepository.findById(id);
    }

    public Optional<SchoolPersonRole> findRoleByIdpUserId(UUID idpUserId) {
        return schoolPersonRoleRepository.findByIdpUserId(idpUserId);
    }

    /**
     * Update the given {@link SchoolPersonRole} in the database.
     *
     * @param role The {@link SchoolPersonRole} to update.
     * @return The updated {@link SchoolPersonRole}.
     */
    @Transactional
    public SchoolPersonRole updateRole(SchoolPersonRole role) {
        return schoolPersonRoleRepository.save(role);
    }

}
