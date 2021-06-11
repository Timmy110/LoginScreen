package com.example.LoginScreen;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {

    public User findByUsername(String username);

    public boolean existsUserByUsername(String username);
}
