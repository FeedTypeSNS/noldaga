package com.noldaga.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UploadDto {

    private String uuid;
    private String filename;
    private boolean img;
    public String getLink(){
        if(img)
            return "s_"+uuid+"_"+filename;
        else
            return uuid+"_"+filename;
    }
}
