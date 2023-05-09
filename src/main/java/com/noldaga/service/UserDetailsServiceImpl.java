package com.noldaga.service;

import com.noldaga.domain.userdto.security.UserDetailsImpl;
import com.noldaga.exception.ErrorCode;
import com.noldaga.exception.SnsApplicationException;
import com.noldaga.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


//todo 나중에 삭제 예정
//@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).map(UserDetailsImpl::fromEntity).orElseThrow(()->
                        new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", username))
                );
    }
}
