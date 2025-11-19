package is.hi.hbv501gteam23.Services.Implementation;

import is.hi.hbv501gteam23.Persistence.Entities.User;
import is.hi.hbv501gteam23.Persistence.dto.ProfileDto;
import is.hi.hbv501gteam23.Persistence.dto.UserDto;
import is.hi.hbv501gteam23.Services.Interfaces.ProfileService;
import is.hi.hbv501gteam23.Services.Interfaces.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@Transactional
@RequiredArgsConstructor
public class ProfileServiceImplementation implements ProfileService {

    private final UserService userService;

    @Override
    public User getProfile(String email) {
        return userService.findByEmail(email)
            .filter(User::isActive)
            .orElseThrow(() -> new EntityNotFoundException("Active user not found for email: " + email));
    }

    @Override
    public User updateProfile(User user, ProfileDto.UpdateProfileRequest request) {
        if (request == null) throw new IllegalArgumentException("UpdateProfileRequest cannot be null");
        UserDto.UpdateProfileRequest userRequest = new UserDto.UpdateProfileRequest(
            request.username(),
            request.gender()
        );
        return userService.updateProfile(user, userRequest);
    }

    @Override
    public User updatePassword(User user, ProfileDto.UpdatePassword request) {
        if (request == null) throw new IllegalArgumentException("UpdatePassword request cannot be null");
        UserDto.UpdatePassword userRequest = new UserDto.UpdatePassword(
            request.newPassword(),
            request.oldPassword()
        );
        return userService.updatePassword(user, userRequest);
    }

    @Override
    public User uploadAvatar(User user, MultipartFile file) throws IOException {
        if (user == null) throw new IllegalArgumentException("User cannot be null");
        return userService.uploadImage(user, file);
    }

    @Override
    public User deleteAvatar(User user) {
        if (user == null) throw new IllegalArgumentException("User cannot be null");
        return userService.deleteImage(user);
    }

    @Override
    public void deactivateAccount(User user) {
        userService.deactivateUser(user.getId());
    }
}
