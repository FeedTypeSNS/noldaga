package com.noldaga.util;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.noldaga.exception.ErrorCode;
import com.noldaga.exception.SnsApplicationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URI;
import java.net.URLDecoder;
import java.util.*;

@Service
@RequiredArgsConstructor //final 멤버 변수를 생성자 빈에 등록해줌..
@Component
@Slf4j
public class S3Uploader {
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    // MultipartFile을 전달받아 File로 전환한 후 upload로 전달
    public String upload(MultipartFile multipartFile, String dirName) throws IOException{
        String uploadImageUrl;

        String fileName = createFileName(multipartFile.getOriginalFilename());
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(multipartFile.getSize());
        objectMetadata.setContentType(multipartFile.getContentType());

        try(InputStream inputStream = multipartFile.getInputStream()) {
            amazonS3Client.putObject(new PutObjectRequest(bucket+dirName, fileName, inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));

            uploadImageUrl = amazonS3Client.getUrl(bucket+dirName, fileName).toString(); //업로드 후 url 반환
        } catch(IOException e) {
            throw new IllegalArgumentException("이미지 업로드 에러");
        }
        return uploadImageUrl;
    }

    //이미지 리스트로 업로드
    public List<String> uploadList(List<MultipartFile> images, String dirName) throws IOException {
        List<String> imgUrlList = new ArrayList<>();
        // forEach 구문을 통해 multipartFile로 넘어온 파일들 하나씩 url 반환
        for(MultipartFile file:images){
            String fileName = createFileName(file.getOriginalFilename());
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(file.getSize());
            objectMetadata.setContentType(file.getContentType());

            try(InputStream inputStream = file.getInputStream()) {
                amazonS3Client.putObject(new PutObjectRequest(bucket+dirName, fileName, inputStream, objectMetadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead));
                imgUrlList.add(amazonS3Client.getUrl(bucket+dirName, fileName).toString());
            } catch(IOException e) {
                throw new IllegalArgumentException("이미지 업로드 에러");
            }
        }
        return imgUrlList;
    }



    //파일 명으로 S3에서 이미지 삭제
    public String deleteImage(String url) throws UnsupportedEncodingException{
        String fileUrl = URLDecoder.decode(url, "UTF-8"); //한글 인코딩
        String s3 = "noldaga-s3";
        int index = fileUrl.indexOf(s3);
        String str = fileUrl.substring(index+s3.length());
        int lastIndex = str.lastIndexOf('/');

        String fileName = str.substring(lastIndex + 1); //파일 명
        String dirName = str.replace("/"+fileName, ""); //폴더 명


        amazonS3Client.deleteObject(new DeleteObjectRequest(bucket+dirName, fileName));
        return fileName;
    }


    // 이미지파일명 중복 방지
    private String createFileName(String fileName){
        return UUID.randomUUID().toString().concat(getFileExtension(fileName));
    }

    //파일 유효성 검사
    private String getFileExtension(String fileName){
        if(fileName.length()==0){
            throw new SnsApplicationException(ErrorCode.FILE_NOT_EXIST);
        }
        ArrayList<String> fileValidate = new ArrayList<>();
        fileValidate.add(".jpg");
        fileValidate.add(".jpeg");
        fileValidate.add(".png");
        fileValidate.add(".JPG");
        fileValidate.add(".JPEG");
        fileValidate.add(".PNG"); //이미지 파일 포맷을 확인하기 위해..
        String idxFileName = fileName.substring(fileName.lastIndexOf("."));
        if (!fileValidate.contains(idxFileName)){
            throw new SnsApplicationException(ErrorCode.FILE_FORMAT_INVALID);
        }
        return fileName.substring(fileName.lastIndexOf("."));
    }


}
