package is.hi.hbv501gteam23.Services.Interfaces;

import is.hi.hbv501gteam23.Persistence.Entities.User;
import is.hi.hbv501gteam23.Persistence.dto.ProfileDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProfileService {

    User getProfile(String email);

    User updateProfile(User user, ProfileDto.UpdateProfileRequest request);

    User updatePassword(User user, ProfileDto.UpdatePassword request);

    User uploadAvatar(User user, MultipartFile file) throws IOException;

    User deleteAvatar(User user);

    void deactivateAccount(User user);
}
