package ru.hogwarts.hogwarts.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
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

    public AvatarController(AvatarServiceImp avatarServiceImp) {
        this.avatarServiceImp = avatarServiceImp;
    }

    @PostMapping(value = "{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadAvatar(@PathVariable long id,
                                               @RequestParam MultipartFile file) throws IOException {
        if (file.getSize() > 1024 * 300) {
            return ResponseEntity.badRequest().body("File is to big");
        }
        avatarServiceImp.uploadAvatar(id, file);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "{id}/avatar-from-db")
    public ResponseEntity<byte[]> downLoadAvatar(@PathVariable long id) throws Exception {
        Avatar avatar = avatarServiceImp.findAvatarById(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(avatar.getMediaType()));
        headers.setContentLength(avatar.getData().length);
        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(avatar.getData());
    }

    @GetMapping(value = "{id}/avatar-from-file")
    public void downLoadAvatar(@PathVariable long id, HttpServletResponse response) throws Exception {
        Avatar avatar = avatarServiceImp.findAvatarById(id);
        Path path = Paths.get(avatar.getFilePath());
        try (InputStream in = Files.newInputStream(path);
             OutputStream out = response.getOutputStream();
        ) {
            response.setStatus(200);
            response.setContentType(avatar.getMediaType());
            response.setContentLength((int) avatar.getFileSize());
            in.transferTo(out);
        }
    }

    @GetMapping
    public ResponseEntity<Collection<Avatar>> getAllAvatar(@RequestParam int page, @RequestParam int count) {
        return ResponseEntity.ok(avatarServiceImp.getAvatarPager(page, count));
    }
}
