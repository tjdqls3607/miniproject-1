package com.mycom.myapp.domain.userGame;

import com.mycom.myapp.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserGameService {
    private final UserGameRepository userGameRepository;
}
