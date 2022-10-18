package ch.ts.flashcardsservice.service;

import ch.ts.flashcardsservice.exception.ServiceException;
import ch.ts.flashcardsservice.mapper.CardMapper;
import ch.ts.flashcardsservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    private final CardMapper cardMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return cardMapper.map(userRepository.findByUsername(username)
                .orElseThrow(() -> new ServiceException("User with username " + username + " was not found", HttpStatus.UNAUTHORIZED)));
    }
}
