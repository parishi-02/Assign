package com.parishi.service;
import com.parishi.dao.UserRepository;
import com.parishi.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService
{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void registerUser(String username, String role,String password)
    {
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));  // Default password
        user.setEnabled(true);
        //System.out.println();
        userRepository.save(user);
        // Assign roles
        userRepository.addRole(username, role);
    }
    @Override
    public void changePassword(String username, String newPassword)
    {
        User user = userRepository.findByUsername(username);
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException
    {
        return userRepository.findByUsername(userName);
    }
}
