package ru.hogwarts.hogwarts.service;

import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.hogwarts.model.Avatar;

import java.util.Collection;

public interface AvatarService {

    void uploadAvatar(long studentId, MultipartFile file);

    Avatar findAvatarById(long studentId);

    Avatar findAvatar(long studentId);

    Collection<Avatar> getAvatarPager(int page, int count);
}
