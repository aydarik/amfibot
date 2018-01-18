package ru.gumerbaev.amfibot.database.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.gumerbaev.amfibot.database.entities.User;

import java.util.Optional;

@RepositoryRestResource(collectionResourceRel="user", path="user")
public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findUserByTelegramId(long id);
}
