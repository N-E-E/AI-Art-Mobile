package com.aiart.server.dao;

import com.aiart.server.entity.UserEntity;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

//@Mapper
//public interface UserDao {
//    int insertUser(UserEntity user);
//    UserEntity getUserById(Long userId);
//    int updateUser(UserEntity user);
//    int deleteUserById(Long userId);
//
//    UserEntity getUserByAccount(String account);
//
//    boolean updateUserInfo(
//            @Param("userId") Long userId,
//            @Param("newNickname") String newNickname,
//            @Param("newGender") String newGender
//    );
//}

@Mapper
public interface UserDao {
//    @Insert("INSERT INTO user (account, password, nickname, gender, model_library) " +
//            "VALUES (#{account}, #{password}, #{nickname}, #{gender}, #{modelLibrary})")
//    @Options(useGeneratedKeys = true, keyProperty = "userId")  // FIXME: add this annotation!
    int insertUser(UserEntity user);

//    @Select("SELECT * FROM user WHERE userId = #{userId}")
    UserEntity getUserById(Long userId);

//    @Update("UPDATE user " +
//            "SET account = #{account}, password = #{password}, nickname = #{nickname}, " +
//            "gender = #{gender}, model_library = #{modelLibrary} " +
//            "WHERE userId = #{userId}")
    int updateUser(UserEntity user);

//    @Delete("DELETE FROM user WHERE userId = #{userId}")
    int deleteUserById(Long userId);

//    @Select("SELECT * FROM user WHERE account = #{account}")
    UserEntity getUserByAccount(String account);

//    @Update("UPDATE user " +
//            "SET nickname = #{newNickname}, gender = #{newGender} " +
//            "WHERE userId = #{userId}")
    boolean updateUserInfo(
            @Param("userId") Long userId,
            @Param("newNickname") String newNickname,
            @Param("newGender") String newGender
    );
}
