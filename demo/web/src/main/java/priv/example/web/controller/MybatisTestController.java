package priv.example.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import priv.example.web.dao.MybatisTestDao;
import priv.example.web.domain.dataobject.OrgDO;
import priv.example.web.domain.dataobject.OrgUsersDO;
import priv.example.web.domain.dataobject.UserDO;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class MybatisTestController {

    private MybatisTestDao mtDao;

    @Autowired
    public MybatisTestController(MybatisTestDao mtDao) {
        this.mtDao = mtDao;
    }

    @GetMapping("/user/{userId}")
    public UserDO testUser(@PathVariable("userId") Long userId) {
        Optional<UserDO> result = mtDao.findUserByUserId(userId);
        return result.orElse(null);
    }

    @GetMapping("/org/{orgId}")
    public OrgDO testOrg(@PathVariable("orgId") Long orgId) {
        Optional<OrgDO> result = mtDao.findOrgByOrgId(orgId);
        return result.orElse(null);
    }

    @GetMapping("/test/{orgId}")
    public OrgUsersDO testOrgUser(@PathVariable("orgId") Long orgId) {
        Optional<OrgUsersDO> result = mtDao.findOrgUsersByOrgId(orgId);
        return result.orElse(null);
    }

    @GetMapping("/testA/{orgId}")
    public OrgUsersDO testOrgUserAnother(@PathVariable("orgId") Long orgId) {
        Optional<OrgUsersDO> result = mtDao.findOrgUsersByOrgIdAnother(orgId);
        return result.orElse(null);
    }

    @GetMapping("/user/{name}/{orgId}")
    public Long addUser(@PathVariable("name") String name, @PathVariable Long orgId) {
        UserDO user = new UserDO();
        user.setUserName(name);
        user.setOrgId(orgId);
        mtDao.addUser(user);

        return user.getUserId();
    }

    @GetMapping("/userBatch/{name}/{orgId}")
    public List<Long> addUserPatch(@PathVariable("name") String name, @PathVariable Long orgId) {
        UserDO user1 = new UserDO();
        user1.setUserName(name);
        user1.setOrgId(orgId);

        UserDO user2 = new UserDO();
        user2.setUserName(name + "1");
        user2.setOrgId(orgId + 1);

        List<UserDO> users = Arrays.asList(user1, user2);
        mtDao.addUserBatch(users);

        return users.stream().map(UserDO::getUserId).collect(Collectors.toList());
    }

}
