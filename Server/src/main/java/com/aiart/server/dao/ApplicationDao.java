package com.aiart.server.dao;

import com.aiart.server.entity.ApplicationEntity;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;


//@Mapper
//public interface ApplicationDao {
//    int insertApplication(ApplicationEntity application);
//    ApplicationEntity getApplicationById(Long applicationId);
//    int updateApplication(ApplicationEntity application);
//    int deleteApplicationById(Long applicationId);
//}

@Mapper
public interface ApplicationDao {
//    @Insert("INSERT INTO application (applicationName, publisher, applicationField, server_addr, server_port) " +
//            "VALUES (#{applicationName}, #{publisher}, #{applicationField}, #{serverAddr}, #{serverPort})")
//    @Options(useGeneratedKeys = true, keyProperty = "applicationId")
    int insertApplication(ApplicationEntity application);

//    @Select("SELECT * FROM application WHERE applicationId = #{applicationId}")
    ApplicationEntity getApplicationById(Long applicationId);

//    @Update("UPDATE application " +
//            "SET applicationName = #{applicationName}, publisher = #{publisher}, " +
//            "applicationField = #{applicationField}, server_addr = #{serverAddr}, server_port = #{serverPort} " +
//            "WHERE applicationId = #{applicationId}")
    int updateApplication(ApplicationEntity application);

//    @Delete("DELETE FROM application WHERE applicationId = #{applicationId}")
    int deleteApplicationById(Long applicationId);
    String getUrlById(Long applicationId);
}
