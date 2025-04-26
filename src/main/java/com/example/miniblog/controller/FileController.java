package com.example.miniblog.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Paths;

@RestController
@RequestMapping("/uploads")
@CrossOrigin(origins = "*")
public class FileController {

    @GetMapping("/{filename:.+}")
    public @ResponseBody Resource serveFile(@PathVariable String filename, HttpServletResponse response) throws IOException {
        var filePath = Paths.get("uploads").resolve(filename).normalize();
        var resource = new FileSystemResource(filePath.toFile());

        if (resource.exists() || resource.isReadable()) {
            response.setHeader("Content-Disposition", "inline; filename=\"" + filename + "\"");
            response.setContentType(MediaType.IMAGE_JPEG_VALUE); // или IMAGE_PNG_VALUE для png
            return resource;
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
    }
}
