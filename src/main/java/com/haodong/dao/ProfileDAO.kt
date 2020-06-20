package com.haodong.dao

import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Select
import org.springframework.stereotype.Repository

@Mapper
@Repository
interface ProfileDAO {


    @Select("select * from profile")
    fun getProfile();
}