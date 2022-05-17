package br.com.estudos.springboot.projetospringboot.service;

import br.com.estudos.springboot.projetospringboot.service.exceptions.FileException;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
public class ImageService {

    public BufferedImage getJpgImageFromFile(MultipartFile uploadFile){
        String extensao = FilenameUtils.getExtension(uploadFile.getOriginalFilename());
        if(!extensao.equalsIgnoreCase("png") && !extensao.equalsIgnoreCase("jpg")) throw new FileException("Somente imagens png ou jpg sao aceitas");

        try {
            BufferedImage img = ImageIO.read(uploadFile.getInputStream());
            if(extensao.equalsIgnoreCase("png")) img = pgnToJpg(img);
            return img;
        } catch (IOException e) {
            throw new FileException("getJpgImageFromFile() - Erro ao ler arquivo");
        }
    }

    public BufferedImage pgnToJpg(BufferedImage img) {
        BufferedImage jpgImage = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
        jpgImage.createGraphics().drawImage(img, 0, 0, Color.WHITE, null);
        return jpgImage;
    }

    public InputStream getInputStream(BufferedImage image, String extensao){
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(image, extensao, outputStream);
            return new ByteArrayInputStream(outputStream.toByteArray());
        } catch (IOException e) {
            throw new FileException("getInputStream() - Erro ao ler arquivo");
        }
    }
}
