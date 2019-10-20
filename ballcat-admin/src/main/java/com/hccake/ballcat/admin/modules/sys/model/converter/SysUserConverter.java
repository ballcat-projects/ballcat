package com.hccake.ballcat.admin.modules.sys.model.converter;

import com.hccake.ballcat.admin.modules.sys.model.dto.SysUserDTO;
import com.hccake.ballcat.admin.modules.sys.model.entity.SysUser;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/9/17 15:26
 */
@Mapper
public interface SysUserConverter {

    SysUserConverter INSTANCE = Mappers.getMapper(SysUserConverter.class);

    /**
     * 转换DTO 为 PO
     * @param sysUserDTO
     * @return
     */
    // @Mapping(target = "password", expression = "java( encodePassword(sysUserDTO) )")
    SysUser dtoToPo(SysUserDTO sysUserDTO);


    /**
     * 将前端传输密码进行加解密
     * @param sysUserDTO
     * @return
     */
/*    default String encodePassword(SysUserDTO sysUserDTO){
        String pass = sysUserDTO.getPass();
        return "encode"+pass;
    }*/
}
