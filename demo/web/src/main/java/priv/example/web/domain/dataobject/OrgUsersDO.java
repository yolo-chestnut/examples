package priv.example.web.domain.dataobject;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@ToString
@Getter
@Setter
public class OrgUsersDO {

    private Long orgId;

    private String orgName;

    private List<UserDO> users;

}
