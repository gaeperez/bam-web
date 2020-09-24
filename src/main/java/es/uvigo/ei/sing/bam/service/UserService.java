package es.uvigo.ei.sing.bam.service;

import es.uvigo.ei.sing.bam.entity.RoleEntity;
import es.uvigo.ei.sing.bam.entity.UserEntity;
import es.uvigo.ei.sing.bam.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    public Set<UserEntity> findAll() {
        return userRepository.findAll();
    }

    public UserEntity findById(Integer id) {
        return userRepository.findById(id).orElse(null);
    }

    public UserEntity findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    public UserEntity findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public void delete(UserEntity userEntity) {
        userRepository.delete(userEntity);
    }

    public void deleteById(Integer id) {
        userRepository.deleteById(id);
    }

    public UserEntity save(UserEntity userEntity) {
        return userRepository.save(userEntity);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity possibleUser = userRepository.findByUsername(username).orElse(null);
        if (possibleUser != null)
            return new User(possibleUser.getUsername(), possibleUser.getPassword(),
                    mapRolesToAuthorities(Arrays.asList(possibleUser.getRole())));
        else
            throw new UsernameNotFoundException("Invalid username or password.");
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<RoleEntity> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getRole())).collect(Collectors.toList());
    }
}
