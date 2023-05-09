package com.noldaga.util;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.noldaga.exception.ErrorCode;
import com.noldaga.exception.SnsApplicationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
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
        File uploadFile = convert(multipartFile).orElseThrow(
                ()-> new SnsApplicationException(ErrorCode.CONVERT_MULTIPART_FILE_FAILE));
        return upload(uploadFile, dirName);
    }

    //이미지 리스트로 업로드
    public List<String> uploadList(List<MultipartFile> images, String dirName) throws IOException {
        List<String> imgUrlList = new ArrayList<>();
        // forEach 구문을 통해 multipartFile로 넘어온 파일들 하나씩 url 반환
        for(MultipartFile file:images){
            String url = upload(file, dirName);
            imgUrlList.add(url);
        }
        return imgUrlList;
    }

    //S3에 실제 업로드하는 과정
    private String upload(File uploadFile, String dirName){
        //s3저장+ UUID.randomUUID()난수 생성추가해서 파일명 중복방지
        String fileName = dirName+"/"+ createFileName(uploadFile.getName());//UUID.randomUUID()+uploadFile.getName();
        String uploadImageUrl = putS3(uploadFile, fileName);//s3에 업로드 후 url 반환

        removeNewFile(uploadFile);  // 로컬에 생성된 File 삭제 (MultipartFile -> File 전환 하며 로컬에 파일 생성됨)

        return uploadImageUrl; //업로드된 파일의 url 주소 반환
    }

    //파일 명으로 S3에서 이미지 삭제
    public void deleteImage(String fileName) throws UnsupportedEncodingException{
        fileName = URLDecoder.decode(fileName, "UTF-8"); //한글 인코딩
        amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, fileName));
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

    //S3로 업로드
    private String putS3(File uploadFile, String fileName){
        amazonS3Client.putObject(
                new PutObjectRequest(bucket, fileName, uploadFile)
                        .withCannedAcl(CannedAccessControlList.PublicRead) //PublicRead 권한으로 업로드되야 url로 접근해 볼 수 있음
        );
        return amazonS3Client.getUrl(bucket, fileName).toString(); //url만 반환
    }

    //로컬에 저장된 이미지 삭제
    private void removeNewFile(File target){
        if(target.delete()){
            log.info("파일이 삭제되었습니다.");
        }else {
            log.info("파일 삭제에 실패했습니다.");
        }
    }


    //로컬 파일 업로드시 multipart로 받은파일을 file로 변환시켜줌..
    private Optional<File> convert(MultipartFile multipartFile) throws IOException{
        // MultipartFile의 기존 이름을 사용하여 생성 (변환된 파일을 저장하는 데 사용)
        File convertFile = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        //지정된 경로에 파일이 생성
        if(convertFile.createNewFile()){//multipartFile을 convertFile에 새로 저장
            try(FileOutputStream fos = new FileOutputStream(convertFile)){
                fos.write(multipartFile.getBytes());
            }
            return Optional.of(convertFile);
        }
        return Optional.empty(); //파일 기록이 실패시 빈파일 반환
    }

}
