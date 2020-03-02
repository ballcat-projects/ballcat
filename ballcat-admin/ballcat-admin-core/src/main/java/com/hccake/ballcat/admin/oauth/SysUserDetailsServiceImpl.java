package com.hccake.ballcat.admin.oauth;

import cn.hutool.core.collection.CollectionUtil;
import com.hccake.ballcat.admin.modules.sys.model.entity.SysUser;
import com.hccake.ballcat.admin.modules.sys.model.vo.UserInfo;
import com.hccake.ballcat.admin.modules.sys.service.SysUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/9/25 20:44
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysUserDetailsServiceImpl implements UserDetailsService {
    private final SysUserService sysUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser sysUser = sysUserService.getByUsername(username);
        if(sysUser == null){
            log.error("登陆：用户名错误，用户名：{}", username);
            throw new UsernameNotFoundException("username error!");
        }
        UserInfo userInfo = sysUserService.findUserInfo(sysUser);
        return getUserDetailsByUserInfo(userInfo);
    }

    /**
     * 根据UserInfo 获取 UserDetails
     * @param userInfo
     * @return
     */
    private UserDetails getUserDetailsByUserInfo(UserInfo userInfo) {

        SysUser user = userInfo.getSysUser();
        List<String> roles = userInfo.getRoles();
        List<Integer> roleIds = userInfo.getRoleIds();
        List<String> permissions = userInfo.getPermissions();

        Set<String> dbAuthsSet = new HashSet<>();
        if (CollectionUtil.isNotEmpty(roles)) {
            // 获取角色
            dbAuthsSet.addAll(roles);
            // 获取资源
            dbAuthsSet.addAll(permissions);

        }
        Collection<? extends GrantedAuthority> authorities
                = AuthorityUtils.createAuthorityList(dbAuthsSet.toArray(new String[0]));

        return new SysUserDetails(user, roles, roleIds, permissions, authorities);

    }

}
