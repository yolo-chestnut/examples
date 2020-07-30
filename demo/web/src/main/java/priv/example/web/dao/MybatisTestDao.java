package priv.example.web.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import priv.example.web.domain.dataobject.OrgDO;
import priv.example.web.domain.dataobject.OrgUsersDO;
import priv.example.web.domain.dataobject.UserDO;

import java.util.List;
import java.util.Optional;

@Mapper
public interface MybatisTestDao {

    Optional<UserDO> findUserByUserId(@Param("userId") Long userId);

    Optional<UserDO> findUserByOrgId(@Param("userId") Long userId);

    Optional<OrgDO> findOrgByOrgId(@Param("orgId") Long orgId);

    Optional<OrgUsersDO> findOrgUsersByOrgId(@Param("orgId") Long orgId);

    Optional<OrgUsersDO> findOrgUsersByOrgIdAnother(@Param("orgId") Long orgId);

    int addUser(UserDO user);

    int addUserBatch(List<UserDO> userList);

}