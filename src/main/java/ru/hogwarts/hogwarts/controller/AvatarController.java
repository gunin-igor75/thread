package ru.hogwarts.hogwarts.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.hogwarts.exception_handling.NoSuchAvatarException;
import ru.hogwarts.hogwarts.model.Avatar;
import ru.hogwarts.hogwarts.service.AvatarServiceImp;


import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;

@RestController
@RequestMapping("/avatar")
public class AvatarController {
    private final AvatarServiceImp avatarServiceImp;

    private static final Logger lpg = LoggerFactory.getLogger(AvatarController.class);

    public AvatarController(AvatarServiceImp avatarServiceImp) {
        this.avatarServiceImp = avatarServiceImp;
    }

    @PostMapping(value = "{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadAvatar(@PathVariable long id,
                                               @RequestParam MultipartFile file) {
        lpg.debug("file size check");
        if (file.getSize() > 1024 * 300) {
            lpg.error("File is to big");
            return ResponseEntity.badRequest().body("File is to big");
        }
        try {
            lpg.debug("search student by id and upload avatar");
            avatarServiceImp.uploadAvatar(id, file);
        } catch (IOException e) {
            lpg.error("Problems opening a file {}", file.getName());
            throw new RuntimeException(e);
        }
        lpg.info("avatar save successfully");
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "{id}/avatar-from-db")
    public ResponseEntity<byte[]> downLoadAvatar(@PathVariable long id) {
        Avatar avatar;
        try {
            lpg.debug("search avatar by id");
            avatar = avatarServiceImp.findAvatarById(id);
            lpg.debug("avatar found {}", avatar);
        } catch (Exception e) {
            lpg.error("No find ID {}", id);
            throw new NoSuchAvatarException(e.getMessage());
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(avatar.getMediaType()));
        headers.setContentLength(avatar.getData().length);
        lpg.info("avtar transferred to user");
        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(avatar.getData());
    }

    @GetMapping(value = "{id}/avatar-from-file")
    public void downLoadAvatar(@PathVariable long id, HttpServletResponse response) {
        Avatar avatar;
        try {
            lpg.debug("search avatar by id");
            avatar = avatarServiceImp.findAvatarById(id);
        } catch (Exception e) {
            lpg.error("No find ID {}", id);
            throw new NoSuchAvatarException(e.getMessage());
        }
        Path path = Paths.get(avatar.getFilePath());
        try (InputStream in = Files.newInputStream(path);
             OutputStream out = response.getOutputStream();
        ) {
            response.setStatus(200);
            response.setContentType(avatar.getMediaType());
            response.setContentLength((int) avatar.getFileSize());
            in.transferTo(out);
            lpg.info("avtar transferred to user");
        } catch (IOException e) {
            lpg.error("Problems opening a file {}", path.toFile().getName());
            throw  new RuntimeException();
        }
    }

    @GetMapping
    public ResponseEntity<Collection<Avatar>> getAllAvatar(@RequestParam int page, @RequestParam int count) {
        lpg.debug("getting an avatar page by page");
        return ResponseEntity.ok(avatarServiceImp.getAvatarPager(page, count));
    }
}
