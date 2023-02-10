package com.example.trialtask.services;

import com.example.trialtask.models.User;
import com.example.trialtask.repositories.UsersRepository;
import com.example.trialtask.util.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UsersService {
    private final UsersRepository usersRepository;

    @Autowired
    public UsersService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public User findById(int id) {
        return usersRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    @Transactional
    public User register(User user) {
        return usersRepository.save(user);
    }

    public User findByUsername(String username) {
        return usersRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
    }
}
