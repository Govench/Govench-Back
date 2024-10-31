package com.upao.govench.govench.repository;

import com.upao.govench.govench.model.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Integer> {
    public boolean existsByAddressAndDistrictAndProvinceAndDepartament(String address, String district, String province, String departament);
    public Optional<Location> findByAddressAndDistrictAndProvinceAndDepartament(String address, String district, String province, String departament);
}
