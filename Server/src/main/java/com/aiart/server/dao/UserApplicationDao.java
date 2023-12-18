package com.aiart.server.dao;

import com.aiart.server.entity.UserApplicationEntity;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

//@Mapper
//public interface UserApplicationDao {
//
//    void insertUserApplication(UserApplicationEntity userApplication);
//
//    void deleteUserApplication(Long userId, Long applicationId);
//
//    List<Long> getApplicationsByUserId(Long userId);
//
//    List<Long> getUsersByApplicationId(Long applicationId);
//}

@Mapper
public interface UserApplicationDao {
//    @Insert("INSERT INTO user_application (user_id, application_id) " +
//            "VALUES (#{userId}, #{applicationId})")
//    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertUserApplication(UserApplicationEntity userApplication);

//    @Delete("DELETE FROM user_application " +
//            "WHERE user_id = #{userId} AND application_id = #{applicationId}")
    void deleteUserApplication(@Param("userId")Long userId, @Param("applicationId")Long applicationId);

//    @Select("SELECT application_id FROM user_application WHERE user_id = #{userId}")
    List<Long> getApplicationsByUserId(Long userId);

//    @Select("SELECT user_id FROM user_application WHERE application_id = #{applicationId}")
    List<Long> getUsersByApplicationId(Long applicationId);
}


