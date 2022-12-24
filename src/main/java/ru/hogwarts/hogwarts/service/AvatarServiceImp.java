package ru.hogwarts.hogwarts.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.hogwarts.exception_handling.NoSuchAvatarException;
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

    private static final Logger lpg = LoggerFactory.getLogger(AvatarServiceImp.class);

    public AvatarServiceImp(StudentService studentService, AvatarRepository avatarRepository) {
        lpg.debug("Initialization studentService avatarRepository");
        this.studentService = studentService;
        this.avatarRepository = avatarRepository;
    }

    public void uploadAvatar(long studentId, MultipartFile file) throws IOException {
        lpg.debug("Search student By ID {}",studentId);
        Student student = studentService.findStudent(studentId);
        lpg.debug("Create path and file for avatar");
        Path filePath = Path.of(avatarDir, studentId + "." +
                getExtension(Objects.requireNonNull(file.getOriginalFilename())));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);
        try (InputStream in = file.getInputStream();
             OutputStream out = Files.newOutputStream(filePath, CREATE_NEW);
             BufferedInputStream bis = new BufferedInputStream(in, 1024);
             BufferedOutputStream bos = new BufferedOutputStream(out, 1024)
        ) {
            lpg.debug("writing the author to a file");
            bis.transferTo(bos);
            lpg.info("avatar saved to file");
        }
        lpg.debug("Search avatar By ID student or create new avatar");
        Avatar avatar = findAvatar(studentId);
        avatar.setStudent(student);
        avatar.setFilePath(filePath.toString());
        avatar.setFileSize(file.getSize());
        avatar.setMediaType(file.getContentType());
        avatar.setData(file.getBytes());
        lpg.info("avatar save in DB");
        avatarRepository.save(avatar);
    }

    public Avatar findAvatarById(long studentId) throws Exception {
        lpg.debug("Search avatar By ID student  or exception");
        return avatarRepository.findByStudentId(studentId).orElseThrow(() -> new NoSuchAvatarException("No find ID"));
    }

    private Avatar findAvatar(long studentId) {
        lpg.debug("Search avatar By ID student  or create new avatar");
        return avatarRepository.findByStudentId(studentId).orElse(new Avatar());
    }

    private String getExtension(String fileName) {
        lpg.debug("extract file extension");
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    public Collection<Avatar> getAvatarPager(int page, int count) {
        lpg.debug("count avatar in BD");
        int countAvatar = avatarRepository.getCountAvatar();
        if (page > countAvatar && page < 1 ||
                count > countAvatar && count < 1) {
            lpg.error("not valid data");
            throw new RuntimeException("Invalid number entered");
        }
        PageRequest pageRequest = PageRequest.of(page - 1, count);
        lpg.info("provision of avatars upon request");
        return avatarRepository.findAll(pageRequest).getContent();
    }
}
