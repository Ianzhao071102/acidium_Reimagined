package org.izdevs.acidium.serialization;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ResourceRepository extends JpaRepository<Resource, Long> {
    Resource findByNameAndApiIs(String name, boolean api);
    Resource findByNameAndAssociatedApi(String name,API api);

    @Query
    Resource findResource(@Param("name") String name,@Param("api") String api);
}
