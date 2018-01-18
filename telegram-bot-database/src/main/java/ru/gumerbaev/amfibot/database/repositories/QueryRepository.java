package ru.gumerbaev.amfibot.database.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.gumerbaev.amfibot.database.entities.Query;

@RepositoryRestResource(collectionResourceRel="queries", path="queries")
public interface QueryRepository extends JpaRepository<Query, Long>{

}
