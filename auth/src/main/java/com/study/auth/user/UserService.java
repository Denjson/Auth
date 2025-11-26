package com.study.auth.user;

import java.security.Principal;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

  private final PasswordEncoder passwordEncoder;
  private final UserRepository repository;

  public void changePassword(ChangePasswordRequest request, Principal connectedUser) {

    var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

    if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
      throw new IllegalStateException("Wrong password");
    }

    if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
      throw new IllegalStateException("Password are not the same");
    }

    user.setPassword(passwordEncoder.encode(request.getNewPassword()));

    repository.save(user);
  }
}
