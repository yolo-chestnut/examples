package priv.example.web.domain.dataobject;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class UserDO {

    private Long userId;

    private String userName;

    private Long orgId;

}
