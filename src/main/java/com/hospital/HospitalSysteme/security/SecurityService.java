//package com.hospital.HospitalSysteme.security;
//
//import com.hospital.HospitalSysteme.entity.User;
//import com.hospital.HospitalSysteme.repository.UserRepository;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Service;
//
//@Service
//public class SecurityService {
//
//    private final UserRepository userRepository;
//
//    public SecurityService(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }
//
//    public boolean isCurrentUser(Long userId) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication == null || !authentication.isAuthenticated()) {
//            return false;
//        }
//
//        String email = authentication.getName();
//        User user = userRepository.findByEmail(email)
//                .orElse(null);
//
//        return user != null && user.getId().equals(userId);
//    }
//}