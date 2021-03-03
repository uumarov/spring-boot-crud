package com.javamentor.crudapp.repositories;

import com.javamentor.crudapp.entities.Role;
import com.javamentor.crudapp.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String username);

//    @Modifying
//    @Query("update User u set u.firstName = :firstName, u.lastName = :lastName, u.age = :age, " +
//            "u.email = :email, u.password = :password, u.roles = :roles " +
//            "where u.id = :id")
//    int update(@Param("firstName") String firstName, @Param("lastName") String lastName, @Param("age") String age,
//               @Param("email") String email, @Param("password") String password, @Param("roles") Collection<Role> roles,
//               @Param("id") Long id);

    @Modifying
    @Query("update User u set u.firstName = :#{#user.firstName}, u.lastName = :#{#user.lastName}, u.age = :#{#user.age}, " +
            "u.email = :#{#user.email}, u.password = :password " +
            "where u.id = :id")
    int update(User user, @Param("password") String password, @Param("id") Long id);

    @Modifying
    @Query("update User u set u.roles = :roles where u.id = :id")
    int updateUserRoles(Set<Role> roles, Long id);


}
