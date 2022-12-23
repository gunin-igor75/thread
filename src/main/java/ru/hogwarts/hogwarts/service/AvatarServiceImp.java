package ru.hogwarts.hogwarts.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.hogwarts.model.Avatar;
import ru.hogwarts.hogwarts.model.Student;
import ru.hogwarts.hogwarts.repository.AvatarRepository;

import javax.transaction.Transactional;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Objects;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
@Transactional
public class AvatarServiceImp {

    @Value("${students.avatar.dir.path}")
    private String avatarDir;

    private final StudentService studentService;
    private final AvatarRepository avatarRepository;

    public AvatarServiceImp(StudentService studentService, AvatarRepository avatarRepository) {
        this.studentService = studentService;
        this.avatarRepository = avatarRepository;
    }

    public void uploadAvatar(long studentId, MultipartFile file) throws IOException {
        Student student = studentService.findStudent(studentId);

        Path filePath = Path.of(avatarDir, studentId + "." +
                getExtension(Objects.requireNonNull(file.getOriginalFilename())));

        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);
        try (InputStream in = file.getInputStream();
             OutputStream out = Files.newOutputStream(filePath, CREATE_NEW);
             BufferedInputStream bis = new BufferedInputStream(in, 1024);
             BufferedOutputStream bos = new BufferedOutputStream(out, 1024)
        ) {
            bis.transferTo(bos);
        }

        Avatar avatar = findAvatar(studentId);
        avatar.setStudent(student);
        avatar.setFilePath(filePath.toString());
        avatar.setFileSize(file.getSize());
        avatar.setMediaType(file.getContentType());
        avatar.setData(file.getBytes());

        avatarRepository.save(avatar);
    }

    public Avatar findAvatarById(long studentId) throws Exception {
        return avatarRepository.findByStudentId(studentId).orElseThrow(() -> new RuntimeException("No find ID"));
    }

    private Avatar findAvatar(long studentId) {
        return avatarRepository.findByStudentId(studentId).orElse(new Avatar());
    }

    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    public Collection<Avatar> getAvatarPager(int page, int count) {
        int countAvatar = avatarRepository.getCountAvatar();
        if (page > countAvatar && page < 1 ||
                count > countAvatar && count < 1) {
            throw new RuntimeException("Invalid number entered");
        }
        PageRequest pageRequest = PageRequest.of(page - 1, count);
        return avatarRepository.findAll(pageRequest).getContent();
    }
}
